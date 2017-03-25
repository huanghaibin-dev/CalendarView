/*
 * Copyright (C) 2016 huanghaibin_dev <huanghaibin_dev@163.com>
 * WebSite https://github.com/MiracleTimes-Dev
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haibin.calendarview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
public class CalendarView extends FrameLayout {
    private ViewPager mViewPager;
    private List<Calendar> mSchemeDate;
    private OnDateChangeListener mListener;
    private OnDateSelectedListener mDateSelectedListener;
    private OnInnerDateSelectedListener mInnerListener;
    private SelectLayout mSelectLayout;
    private LinearLayout mLinearWeek;
    private int mCurYear, mCurMonth, mCurDay;
    private int mCurDayColor, mSchemeThemeColor, mWeekBackground, mWeekTextColor, mSelectedColor,
            mSelectedTextColor;
    private int mMinYear, mMaxYear;
    private String mScheme;
    private Calendar mSelectedCalendar;

    public CalendarView(@NonNull Context context) {
        super(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCurDayColor = array.getColor(R.styleable.CalendarView_current_day_color, Color.RED);
        mSchemeThemeColor = array.getColor(R.styleable.CalendarView_scheme_theme_color, Color.RED);
        mWeekBackground = array.getColor(R.styleable.CalendarView_week_background, Color.WHITE);
        mWeekTextColor = array.getColor(R.styleable.CalendarView_week_text_color, Color.RED);
        mSelectedColor = array.getColor(R.styleable.CalendarView_selected_color, 0x50CFCFCF);
        mSelectedTextColor = array.getColor(R.styleable.CalendarView_selected_text_color, 0xFF111111);
        mMinYear = array.getInt(R.styleable.CalendarView_min_year, 2010);
        mMaxYear = array.getInt(R.styleable.CalendarView_max_year, 2050);
        mScheme = array.getString(R.styleable.CalendarView_scheme_text);
        if (mMinYear <= 1900) mMaxYear = 1900;
        if (mMaxYear >= 2099) mMaxYear = 2099;
        array.recycle();
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_calendar_view_mvp, this, true);
        this.mViewPager = (ViewPager) findViewById(R.id.vp_calendar);
        this.mLinearWeek = (LinearLayout) findViewById(R.id.ll_week);
        mSelectLayout = (SelectLayout) findViewById(R.id.selectLayout);
        mLinearWeek.setBackgroundColor(mWeekBackground);
        for (int i = 0; i < mLinearWeek.getChildCount(); i++) {
            ((TextView) mLinearWeek.getChildAt(i)).setTextColor(mWeekTextColor);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    int year = position / 12 + mMinYear;
                    int month = position % 12 + 1;
                    int[] lunar = LunarCalendar.solarToLunar(year, month, 1);
                    mListener.onDateChange(year, month, 1, LunarCalendar.numToChineseDay(lunar[2]), "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSelectLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onYearChange(position + mMinYear);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mInnerListener = new OnInnerDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar) {
                mSelectedCalendar = calendar;
            }
        };

        int position;
        mSelectedCalendar = new Calendar();

        Date d = new Date();
        mSelectedCalendar.setYear(Util.getDate("yyyy", d));
        mSelectedCalendar.setMonth(Util.getDate("MM", d));
        mSelectedCalendar.setDay(Util.getDate("dd", d));

        mCurYear = mSelectedCalendar.getYear();
        if (mMinYear >= mCurYear) mMinYear = mCurYear;
        if (mMaxYear <= mCurYear) mMaxYear = mCurYear + 2;
        mSelectLayout.setYearSpan(mMinYear, mMaxYear);
        mCurMonth = mSelectedCalendar.getMonth();
        mCurDay = mSelectedCalendar.getDay();
        int y = mSelectedCalendar.getYear() - mMinYear;
        position = 12 * y + mSelectedCalendar.getMonth() - 1;
        CalendarViewPagerAdapter adapter = new CalendarViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);

        mSelectLayout.setOnMonthSelectedListener(new MonthRecyclerView.OnMonthSelectedListener() {
            @Override
            public void onMonthSelected(int year, int month) {
                int position = 12 * (year - mMinYear) + month - 1;
                closeSelectLayout(position);
            }
        });
    }

    public int getCurDay() {
        return mCurDay;
    }

    public int getCurMonth() {
        return mCurMonth;
    }

    public int getCurYear() {
        return mCurYear;
    }

    public void showSelectLayout(final int year) {
        mLinearWeek.animate()
                .translationY(-mLinearWeek.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLinearWeek.setVisibility(GONE);
                        mSelectLayout.setVisibility(VISIBLE);
                        mSelectLayout.init(year);
                    }
                });
        mViewPager.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(180)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mViewPager.setVisibility(GONE);
                    }
                });
    }

    public void scrollToCurrent() {
        mViewPager.setCurrentItem(12 * (mCurYear - mMinYear) + mCurMonth - 1);
    }

    public void scrollToYear(int year) {
        mViewPager.setCurrentItem(12 * (year - mMinYear) + mCurMonth - 1);
    }

    public void closeSelectLayout(final int position) {
        mSelectLayout.setVisibility(GONE);
        mLinearWeek.setVisibility(VISIBLE);
        mViewPager.setVisibility(VISIBLE);
        mViewPager.setCurrentItem(position, true);
        mLinearWeek.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLinearWeek.setVisibility(VISIBLE);
                    }
                });
        mViewPager.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(180)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mViewPager.setVisibility(VISIBLE);

                    }
                });
    }

    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.mListener = listener;
    }

    private class CalendarViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 12 * (mMaxYear - mMinYear);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int year = position / 12 + mMinYear;
            int month = position % 12 + 1;
            CalendarCardView view = new CalendarCardView(getContext(), null);
            view.setSelectedColor(mSelectedColor,mSelectedTextColor);
            view.setInnerListener(mInnerListener);
            view.setSchemes(mSchemeDate);
            view.setScheme(mScheme);
            view.setOnDateSelectedListener(mDateSelectedListener);
            view.setCurrentDate(year, month);
            view.setSelectedCalendar(mSelectedCalendar);
            view.setOnDateChangeListener(mListener);
            view.setStyle(mSchemeThemeColor, mCurDayColor);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((CalendarCardView) object);
        }

    }

    /**
     * 标记哪些日期有事件
     *
     * @param mSchemeDate mSchemeDate 通过自己的需求转换即可
     */
    public void setSchemeDate(List<Calendar> mSchemeDate) {
        this.mSchemeDate = mSchemeDate;
        mSelectLayout.setSchemes(mSchemeDate);
        for (int i = 0; i < mViewPager.getChildCount(); i++) {
            CalendarCardView view = (CalendarCardView) mViewPager.getChildAt(i);
            view.setSchemes(mSchemeDate);
        }
    }

    public void setStyle(int schemeThemeColor, int selectLayoutBackground, int lineBg) {
        this.mSchemeThemeColor = schemeThemeColor;
        mSelectLayout.setSchemeColor(mSchemeThemeColor);
        mSelectLayout.setBackgroundColor(selectLayoutBackground);
        findViewById(R.id.line).setBackgroundColor(lineBg);
        mSelectLayout.update();
    }

    public void update() {
        for (int i = 0; i < mViewPager.getChildCount(); i++) {
            CalendarCardView view = (CalendarCardView) mViewPager.getChildAt(i);
            view.setStyle(mSchemeThemeColor, mCurDayColor);
            view.getAdapter().notifyDataSetChanged();
        }
    }

    public void setWeekStyle(int mWeekBackground, int mWeekTextColor) {
        this.mWeekBackground = mWeekBackground;
        this.mWeekTextColor = mWeekTextColor;
        mLinearWeek.setBackgroundColor(mWeekBackground);
        for (int i = 0; i < mLinearWeek.getChildCount(); i++) {
            ((TextView) mLinearWeek.getChildAt(i)).setTextColor(mWeekTextColor);
        }
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mDateSelectedListener = listener;
    }

    interface OnInnerDateSelectedListener {
        void onDateSelected(Calendar calendar);
    }
}
