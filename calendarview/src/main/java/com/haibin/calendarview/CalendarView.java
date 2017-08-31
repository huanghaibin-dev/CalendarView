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
    private WrapViewPager mViewPager;
    private List<Calendar> mSchemeDate;
    private OnDateChangeListener mListener;
    private OnDateSelectedListener mDateSelectedListener;
    @SuppressWarnings("all")
    private OnInnerDateSelectedListener mInnerListener;
    private MonthSelectLayout mSelectLayout;
    private LinearLayout mLinearWeek;
    private int mCurYear, mCurMonth, mCurDay;
    private int mCurDayTextColor;
    private int mSchemeThemeColor;
    private int mSchemeTextColor;
    private int mWeekBackground;
    private int mWeekTextColor;
    private int mSelectedThemeColor;
    private int mCurrentMonthTextColor;
    private int mOtherMonthTextColor;
    private int mSelectedTextColor;
    private int mLunarColor;
    static int mMinYear, mMaxYear;
    private Calendar mSelectedCalendar;
    CalendarLayout mParentLayout;
    private int mSchemeStyle;
    private int mSelectThemeStyle;
    private boolean isShowLunar;

    public CalendarView(@NonNull Context context) {
        super(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCurDayTextColor = array.getColor(R.styleable.CalendarView_current_day_text_color, Color.RED);
        mSchemeStyle = array.getInt(R.styleable.CalendarView_scheme_theme_style, CalendarCardView.STYLE_STROKE);
        mSelectThemeStyle = array.getInt(R.styleable.CalendarView_selected_theme_style, CalendarCardView.STYLE_STROKE);
        mSchemeTextColor = array.getColor(R.styleable.CalendarView_scheme_text_color, Color.RED);
        mSchemeThemeColor = array.getColor(R.styleable.CalendarView_scheme_theme_color, 0x50CFCFCF);
        isShowLunar = array.getBoolean(R.styleable.CalendarView_show_lunar, true);

        mWeekBackground = array.getColor(R.styleable.CalendarView_week_background, Color.WHITE);
        mWeekTextColor = array.getColor(R.styleable.CalendarView_week_text_color, Color.BLACK);

        mSelectedThemeColor = array.getColor(R.styleable.CalendarView_selected_theme_color, 0x50CFCFCF);
        mSelectedTextColor = array.getColor(R.styleable.CalendarView_selected_text_color, 0xFF111111);

        mCurrentMonthTextColor = array.getColor(R.styleable.CalendarView_current_month_text_color, 0xFF111111);
        mOtherMonthTextColor = array.getColor(R.styleable.CalendarView_other_month_text_color, 0xFFe1e1e1);

        mLunarColor = array.getColor(R.styleable.CalendarView_lunar_text_color, Color.GRAY);
        mMinYear = array.getInt(R.styleable.CalendarView_min_year, 2010);
        mMaxYear = array.getInt(R.styleable.CalendarView_max_year, 2050);
        if (mMinYear <= 1900) mMaxYear = 1900;
        if (mMaxYear >= 2099) mMaxYear = 2099;
        array.recycle();
        init(context);
    }

    private void init(Context context) {
        CalendarCardView.ITEM_HEIGHT = isShowLunar ? 58 : 46;
        CalendarCardView.mItemHeight = Util.dipToPx(context, CalendarCardView.ITEM_HEIGHT);
        LayoutInflater.from(context).inflate(R.layout.layout_calendar_view, this, true);
        this.mViewPager = (WrapViewPager) findViewById(R.id.vp_calendar);
        this.mLinearWeek = (LinearLayout) findViewById(R.id.ll_week);
        mSelectLayout = (MonthSelectLayout) findViewById(R.id.selectLayout);
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
                    Calendar calendar = new Calendar();
                    calendar.setYear(position / 12 + mMinYear);
                    calendar.setMonth(position % 12 + 1);
                    calendar.setDay(1);
                    calendar.setLunar(LunarCalendar.numToChineseDay(LunarCalendar.solarToLunar(calendar.getYear(), calendar.getMonth(), 1)[2]));
                    mListener.onDateChange(calendar);
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
                for (int i = 0; i < mViewPager.getChildCount(); i++) {
                    CalendarCardView view = (CalendarCardView) mViewPager.getChildAt(i);
                    view.setSelectedCalendar(mSelectedCalendar);
                    view.invalidate();
                }
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
        mSelectLayout.setSchemeColor(mSchemeThemeColor);
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
        if (mParentLayout != null && mParentLayout.mContentView != null) {
            mParentLayout.mContentView.setVisibility(GONE);
        }
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
        if (position == mViewPager.getCurrentItem()) {
            if (mListener != null) {
                Calendar calendar = new Calendar();
                calendar.setYear(position / 12 + mMinYear);
                calendar.setMonth(position % 12 + 1);
                calendar.setDay(1);
                calendar.setLunar(LunarCalendar.numToChineseDay(LunarCalendar.solarToLunar(calendar.getYear(), calendar.getMonth(), 1)[2]));
                mListener.onDateChange(calendar);
            }
        } else {
            mViewPager.setCurrentItem(position, true);
        }
        mLinearWeek.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLinearWeek.setVisibility(VISIBLE);
                        if (mParentLayout != null && mParentLayout.mContentView != null) {
                            mParentLayout.mContentView.setVisibility(VISIBLE);
                        }
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
            CalendarCardView view = isShowLunar ? new LunarCalendarCardView(getContext(), null) :
                    new CalendarCardView(getContext(), null);
            view.setDayTextSize(isShowLunar ? 16 : CalendarCardView.TEXT_SIZE, 10);
            view.mParentLayout = mParentLayout;
            view.mSchemes = mSchemeDate;
            view.isShowLunar = isShowLunar;
            view.mListener = mListener;
            view.mDateSelectedListener = mDateSelectedListener;
            view.mInnerListener = mInnerListener;
            view.setCurrentDate(year, month);
            view.setSelectedCalendar(mSelectedCalendar);
            view.setSchemeColor(mSchemeStyle, mSchemeThemeColor, mSchemeTextColor);
            view.setSelectColor(mSelectThemeStyle, mSelectedThemeColor, mSelectedTextColor);
            view.setTextColor(mCurDayTextColor, mCurrentMonthTextColor, mOtherMonthTextColor, mLunarColor);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() != null && getParent() instanceof CalendarLayout) {
            mParentLayout = (CalendarLayout) getParent();
            mViewPager.mParentLayout = mParentLayout;
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
            view.mSchemes = mSchemeDate;
            view.update();
        }
    }
    /**
     * 设置背景色
     *
     * @param monthLayoutBackground 月份卡片的背景色
     * @param weekBackground        星期栏背景色
     * @param lineBg                线的颜色
     */
    public void setBackground(int monthLayoutBackground, int weekBackground, int lineBg) {
        mLinearWeek.setBackgroundColor(weekBackground);
        mSelectLayout.setBackgroundColor(monthLayoutBackground);
        findViewById(R.id.line).setBackgroundColor(lineBg);
    }


    /**
     * 设置文本颜色
     *
     * @param curMonthTextColor 当前月份字体颜色
     * @param otherMonthColor   其它月份字体颜色
     * @param lunarTextColor    农历字体颜色
     */
    public void setTextColor(int curMonthTextColor,
                             int otherMonthColor,
                             int lunarTextColor) {
        this.mCurrentMonthTextColor = curMonthTextColor;
        this.mLunarColor = lunarTextColor;
        this.mOtherMonthTextColor = otherMonthColor;
    }

    /**
     * 设置选择的效果
     *
     * @param style              选中的style CalendarCardView.STYLE_FILL or CalendarCardView.STYLE_STROKE
     * @param selectedThemeColor 选中的标记颜色
     * @param selectedTextColor  选中的字体颜色
     */
    public void setSelectedColor(int style, int selectedThemeColor, int selectedTextColor) {
        this.mSelectThemeStyle = style;
        this.mSelectedThemeColor = selectedThemeColor;
        this.mSelectedTextColor = selectedTextColor;
    }

    /**
     * 设置标记的色
     *
     * @param style           标记的style CalendarCardView.STYLE_FILL or CalendarCardView.STYLE_STROKE
     * @param schemeColor     标记背景色
     * @param schemeTextColor 标记字体颜色
     */
    public void setSchemeColor(int style, int schemeColor, int schemeTextColor) {
        this.mSchemeStyle = style;
        this.mSchemeThemeColor = schemeColor;
        this.mSchemeTextColor = schemeTextColor;
        mSelectLayout.setSchemeColor(mSchemeThemeColor);
    }

    /**
     * 设置星期栏的背景和字体颜色
     *
     * @param weekBackground 背景色
     * @param weekTextColor  字体颜色
     */
    public void setWeeColor(int weekBackground, int weekTextColor) {
        this.mWeekBackground = weekBackground;
        this.mWeekTextColor = weekTextColor;
        mLinearWeek.setBackgroundColor(mWeekBackground);
        for (int i = 0; i < mLinearWeek.getChildCount(); i++) {
            ((TextView) mLinearWeek.getChildAt(i)).setTextColor(mWeekTextColor);
        }
    }


    /**
     * 更新界面，
     * 重新设置颜色等都需要调用该方法
     */
    public void update() {
        mSelectLayout.update();
        for (int i = 0; i < mViewPager.getChildCount(); i++) {
            CalendarCardView view = (CalendarCardView) mViewPager.getChildAt(i);
            view.setSchemeColor(mSchemeStyle, mSchemeThemeColor, mSchemeTextColor);
            view.setSelectColor(mSelectThemeStyle, mSelectedThemeColor, mSelectedTextColor);
            view.setTextColor(mCurDayTextColor, mCurrentMonthTextColor, mOtherMonthTextColor, mLunarColor);
            view.update();
        }
    }

    /**
     * 获取选择的日期
     */
    public Calendar getSelectedCalendar() {
        return mSelectedCalendar;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mDateSelectedListener = listener;
    }


    public interface OnDateChangeListener {
        void onDateChange(Calendar calendar);

        void onYearChange(int year);
    }


    interface OnInnerDateSelectedListener {
        void onDateSelected(Calendar calendar);
    }

    public interface OnDateSelectedListener {
        void onDateSelected(Calendar calendar);
    }
}
