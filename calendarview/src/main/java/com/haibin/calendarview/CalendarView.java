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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

/**
 * 日历布局
 */
@SuppressWarnings("unused")
public class CalendarView extends FrameLayout {

    /**自定义自适应高度的ViewPager*/
    private WrapViewPager mViewPager;

    /**标记的日期*/
    private List<Calendar> mSchemeDate;

    /**日期切换监听*/
    private OnDateChangeListener mListener;

    /**日期被选中监听*/
    private OnDateSelectedListener mDateSelectedListener;

    /**内部日期切换监听，用于内部更新计算*/
    private OnInnerDateSelectedListener mInnerListener;

    /**月份快速选取*/
    private MonthSelectLayout mSelectLayout;

    /**星期栏*/
    private LinearLayout mLinearWeek;

    /**日历外部收缩布局*/
    CalendarLayout mParentLayout;

    /***/
    private int mCurYear, mCurMonth, mCurDay;

    /**各种字体颜色，看名字知道对应的地方*/
    private int mCurDayTextColor, mWeekTextColor,
            mSchemeTextColor,
            mSchemeLunarTextColor,
            mOtherMonthTextColor,
            mCurrentMonthTextColor,
            mSelectedTextColor,
            mSelectedLunarTextColor,
            mCurMonthLunarTextColor,
            mOtherMonthLunarTextColor;

    /**标记的主题色和选中的主题色*/
    private int mSchemeThemeColor, mSelectedThemeColor;

    /**星期栏的背景*/
    private int mWeekBackground;

    /**自定义的日历路径*/
    private String mCalendarCardViewClass;

    /**最小年份和最大年份*/
    static int mMinYear, mMaxYear;

    /**保存选中的日期*/
    private Calendar mSelectedCalendar;

    /**日期和农历文本大小*/
    private int mDayTextSize, mLunarTextSize;

    /**日历卡的项高度*/
    private int mCalendarItemHeight;


    public CalendarView(@NonNull Context context) {
        this(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCurDayTextColor = array.getColor(R.styleable.CalendarView_current_day_text_color, Color.RED);
        mSchemeTextColor = array.getColor(R.styleable.CalendarView_scheme_text_color, 0xFFFFFFFF);
        mSchemeLunarTextColor = array.getColor(R.styleable.CalendarView_scheme_lunar_text_color, 0xFFe1e1e1);
        mSchemeThemeColor = array.getColor(R.styleable.CalendarView_scheme_theme_color, 0x50CFCFCF);
        mCalendarCardViewClass = array.getString(R.styleable.CalendarView_calendar_card_view);

        mWeekBackground = array.getColor(R.styleable.CalendarView_week_background, Color.WHITE);
        mWeekTextColor = array.getColor(R.styleable.CalendarView_week_text_color, Color.BLACK);

        mSelectedThemeColor = array.getColor(R.styleable.CalendarView_selected_theme_color, 0x50CFCFCF);
        mSelectedTextColor = array.getColor(R.styleable.CalendarView_selected_text_color, 0xFF111111);
        mSelectedLunarTextColor = array.getColor(R.styleable.CalendarView_selected_lunar_text_color, 0xFF111111);
        mCurrentMonthTextColor = array.getColor(R.styleable.CalendarView_current_month_text_color, 0xFF111111);
        mOtherMonthTextColor = array.getColor(R.styleable.CalendarView_other_month_text_color, 0xFFe1e1e1);

        mCurMonthLunarTextColor = array.getColor(R.styleable.CalendarView_current_month_lunar_text_color, Color.GRAY);
        mOtherMonthLunarTextColor = array.getColor(R.styleable.CalendarView_other_month_lunar_text_color, Color.GRAY);
        mMinYear = array.getInt(R.styleable.CalendarView_min_year, 2010);
        mMaxYear = array.getInt(R.styleable.CalendarView_max_year, 2050);

        mDayTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_day_text_size, Util.dipToPx(context, 16));
        mLunarTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_lunar_text_size, Util.dipToPx(context, 10));
        mCalendarItemHeight = (int) array.getDimension(R.styleable.CalendarView_calendar_height, Util.dipToPx(context, 56));
        if (mMinYear <= 1900) mMaxYear = 1900;
        if (mMaxYear >= 2099) mMaxYear = 2099;
        array.recycle();
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.cv_layout_calendar_view, this, true);
        this.mViewPager = (WrapViewPager) findViewById(R.id.vp_calendar);
        this.mViewPager.mItemHeight = mCalendarItemHeight;
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
                Calendar calendar = new Calendar();
                calendar.setYear(position / 12 + mMinYear);
                calendar.setMonth(position % 12 + 1);
                calendar.setDay(1);
                calendar.setLunar(LunarCalendar.numToChineseDay(LunarCalendar.solarToLunar(calendar.getYear(), calendar.getMonth(), 1)[2]));
                if (mListener != null) {
                    mListener.onDateChange(calendar);
                }
                if (mParentLayout != null) {
                    View view = mViewPager.findViewWithTag(position);
                    if (view != null) {
                        int index = ((BaseCalendarCardView) view).getSelectedIndex(mSelectedCalendar);
                        if (index >= 0) {
                            mParentLayout.setSelectPosition(index);
                        }
                    }
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
                    BaseCalendarCardView view = (BaseCalendarCardView) mViewPager.getChildAt(i);
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

    /**
     * 获取当天
     *
     * @return 返回今天
     */
    public int getCurDay() {
        return mCurDay;
    }

    /**
     * 获取本月
     *
     * @return 返回本月
     */
    public int getCurMonth() {
        return mCurMonth;
    }

    /**
     * 获取本年
     *
     * @return 返回本年
     */
    public int getCurYear() {
        return mCurYear;
    }

    /**
     * 打开日历月份快速选择
     *
     * @param year 年
     */
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

    /**
     * 滚动到当前
     */
    public void scrollToCurrent() {
        mViewPager.setCurrentItem(12 * (mCurYear - mMinYear) + mCurMonth - 1);
    }

    /**
     * 滚动到某一年
     *
     * @param year 快速滚动的年份
     */
    public void scrollToYear(int year) {
        mViewPager.setCurrentItem(12 * (year - mMinYear) + mCurMonth - 1);
    }

    /**
     * 关闭日历布局，同时会滚动到指定的位置
     *
     * @param position 某一年
     */
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

    /**
     * 日期改变监听器
     *
     * @param listener 监听
     */
    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置日期选中事件
     *
     * @param listener 日期选中事件
     */
    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mDateSelectedListener = listener;
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
            BaseCalendarCardView view;
            if (TextUtils.isEmpty(mCalendarCardViewClass)) {
                view = new SimpleCalendarCardView(getContext());
            } else {
                try {
                    Class cls = Class.forName(mCalendarCardViewClass);
                    @SuppressWarnings("unchecked")
                    Constructor constructor = cls.getConstructor(Context.class);
                    view = (BaseCalendarCardView) constructor.newInstance(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            view.setItemHeight(mCalendarItemHeight);
            view.setDayTextSize(mDayTextSize, mLunarTextSize);
            view.mParentLayout = mParentLayout;
            view.mSchemes = mSchemeDate;
            view.mListener = mListener;
            view.mDateSelectedListener = mDateSelectedListener;
            view.mInnerListener = mInnerListener;
            view.setTag(position);
            view.setCurrentDate(year, month);
            view.setSelectedCalendar(mSelectedCalendar);
            view.setSchemeColor(mSchemeThemeColor, mSchemeTextColor, mSchemeLunarTextColor);
            view.setSelectColor(mSelectedThemeColor, mSelectedTextColor, mSelectedLunarTextColor);
            view.setTextColor(mCurDayTextColor, mCurrentMonthTextColor, mOtherMonthTextColor, mCurMonthLunarTextColor, mOtherMonthLunarTextColor);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    /**
     * 初始化时初始化日历卡默认选择位置
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() != null && getParent() instanceof CalendarLayout) {
            mParentLayout = (CalendarLayout) getParent();
            mParentLayout.mItemHeight = mCalendarItemHeight;
            mViewPager.mParentLayout = mParentLayout;
            mParentLayout.initCalendarPosition(mSelectedCalendar);
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
            BaseCalendarCardView view = (BaseCalendarCardView) mViewPager.getChildAt(i);
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
     * @param curMonthTextColor        当前月份字体颜色
     * @param otherMonthColor          其它月份字体颜色
     * @param curMonthLunarTextColor   当前月份农历字体颜色
     * @param otherMonthLunarTextColor 其它农历字体颜色
     */
    public void setTextColor(int curMonthTextColor,
                             int otherMonthColor,
                             int curMonthLunarTextColor,
                             int otherMonthLunarTextColor) {
        this.mCurrentMonthTextColor = curMonthTextColor;
        this.mCurMonthLunarTextColor = curMonthLunarTextColor;
        this.mOtherMonthTextColor = otherMonthColor;
        this.mOtherMonthLunarTextColor = otherMonthLunarTextColor;
    }

    /**
     * 设置选择的效果
     *
     * @param selectedThemeColor 选中的标记颜色
     * @param selectedTextColor  选中的字体颜色
     */
    public void setSelectedColor(int selectedThemeColor, int selectedTextColor) {
        this.mSelectedThemeColor = selectedThemeColor;
        this.mSelectedTextColor = selectedTextColor;
    }

    /**
     * 设置标记的色
     *
     * @param schemeColor     标记背景色
     * @param schemeTextColor 标记字体颜色
     */
    public void setSchemeColor(int schemeColor, int schemeTextColor) {
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
            BaseCalendarCardView view = (BaseCalendarCardView) mViewPager.getChildAt(i);
            view.setSchemeColor(mSchemeThemeColor, mSchemeTextColor, mSchemeLunarTextColor);
            view.setSelectColor(mSelectedThemeColor, mSelectedTextColor, mSelectedLunarTextColor);
            view.setTextColor(mCurDayTextColor, mCurrentMonthTextColor, mOtherMonthTextColor, mCurMonthLunarTextColor, mOtherMonthLunarTextColor);
            view.update();
        }
    }

    /**
     * 获取选择的日期
     */
    public Calendar getSelectedCalendar() {
        return mSelectedCalendar;
    }


    /**
     * 日期改变、左右切换、快速年份、月份切换
     */
    public interface OnDateChangeListener {
        void onDateChange(Calendar calendar);

        void onYearChange(int year);
    }


    /**
     * 内部日期选择，不暴露外部使用
     */
    interface OnInnerDateSelectedListener {
        void onDateSelected(Calendar calendar);
    }

    /**
     * 外部日期选择事件
     */
    public interface OnDateSelectedListener {
        void onDateSelected(Calendar calendar);
    }
}
