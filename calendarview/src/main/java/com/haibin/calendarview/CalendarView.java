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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 日历布局
 * 各个类使用包权限，避免不必要的public
 */
@SuppressWarnings("unused")
public class CalendarView extends FrameLayout {

    /**
     * 使用google官方推荐的方式抽取自定义属性
     */
    private CustomCalendarViewDelegate mDelegate;

    /**
     * 自定义自适应高度的ViewPager
     */
    private MonthViewPager mMonthPager;

    /**
     * 日历周视图
     */
    private WeekViewPager mWeekPager;

    /**
     * 星期栏的线
     */
    private View mWeekLine;

    /**
     * 月份快速选取
     */
    private YearSelectLayout mSelectLayout;

    /**
     * 星期栏
     */
    private WeekBar mWeekBar;

    /**
     * 日历外部收缩布局
     */
    CalendarLayout mParentLayout;


    public CalendarView(@NonNull Context context) {
        this(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDelegate = new CustomCalendarViewDelegate(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.cv_layout_calendar_view, this, true);
        FrameLayout frameContent = (FrameLayout) findViewById(R.id.frameContent);
        this.mWeekPager = (WeekViewPager) findViewById(R.id.vp_week);
        this.mWeekPager.setup(mDelegate);

        if (TextUtils.isEmpty(mDelegate.getWeekBarClass())) {
            this.mWeekBar = new WeekBar(getContext());
        } else {
            try {
                Class<?> cls = Class.forName(mDelegate.getWeekBarClass());
                Constructor constructor = cls.getConstructor(Context.class);
                mWeekBar = (WeekBar) constructor.newInstance(getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        frameContent.addView(mWeekBar, 2);
        mWeekBar.setup(mDelegate);
        mWeekBar.onWeekStartChange(mDelegate.getWeekStart());

        this.mWeekLine = findViewById(R.id.line);
        this.mWeekLine.setBackgroundColor(mDelegate.getWeekLineBackground());
        FrameLayout.LayoutParams lineParams = (FrameLayout.LayoutParams) this.mWeekLine.getLayoutParams();
        lineParams.setMargins(lineParams.leftMargin, mDelegate.getWeekBarHeight(), lineParams.rightMargin, 0);
        this.mWeekLine.setLayoutParams(lineParams);

        this.mMonthPager = (MonthViewPager) findViewById(R.id.vp_calendar);
        this.mMonthPager.mWeekPager = mWeekPager;
        this.mMonthPager.mWeekBar = mWeekBar;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.mMonthPager.getLayoutParams();
        params.setMargins(0, mDelegate.getWeekBarHeight() + CalendarUtil.dipToPx(context, 1), 0, 0);
        mWeekPager.setLayoutParams(params);


        mSelectLayout = (YearSelectLayout) findViewById(R.id.selectLayout);
        mSelectLayout.setBackgroundColor(mDelegate.getYearViewBackground());
        mSelectLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mWeekPager.getVisibility() == VISIBLE) {
                    return;
                }
                if (mDelegate.mYearChangeListener != null) {
                    mDelegate.mYearChangeListener.onYearChange(position + mDelegate.getMinYear());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mDelegate.mInnerListener = new OnInnerDateSelectedListener() {
            @Override
            public void onMonthDateSelected(Calendar calendar, boolean isClick) {
                if (calendar.getYear() == mDelegate.getCurrentDay().getYear() &&
                        calendar.getMonth() == mDelegate.getCurrentDay().getMonth()
                        && mMonthPager.getCurrentItem() != mDelegate.mCurrentMonthViewItem) {
                    return;
                }
                mDelegate.mSelectedCalendar = calendar;
                mWeekPager.updateSelected(mDelegate.mSelectedCalendar, false);
                mMonthPager.updateSelected();
                if (mWeekBar != null) {
                    mWeekBar.onDateSelected(calendar, mDelegate.getWeekStart(), isClick);
                }
            }

            @Override
            public void onWeekDateSelected(Calendar calendar, boolean isClick) {
                mDelegate.mSelectedCalendar = calendar;
                int y = calendar.getYear() - mDelegate.getMinYear();
                int position = 12 * y + mDelegate.mSelectedCalendar.getMonth() - mDelegate.getMinYearMonth();
                mMonthPager.setCurrentItem(position,false);
                mMonthPager.updateSelected();
                if (mWeekBar != null) {
                    mWeekBar.onDateSelected(calendar, mDelegate.getWeekStart(), isClick);
                }
            }
        };

        mDelegate.mSelectedCalendar = mDelegate.createCurrentDate();
        mWeekBar.onDateSelected(mDelegate.mSelectedCalendar, mDelegate.getWeekStart(), false);

        int mCurYear = mDelegate.mSelectedCalendar.getYear();
        mMonthPager.setup(mDelegate);
        mMonthPager.setCurrentItem(mDelegate.mCurrentMonthViewItem);
        mSelectLayout.setOnMonthSelectedListener(new YearRecyclerView.OnMonthSelectedListener() {
            @Override
            public void onMonthSelected(int year, int month) {
                int position = 12 * (year - mDelegate.getMinYear()) + month - mDelegate.getMinYearMonth();
                mDelegate.isShowYearSelectedLayout = false;
                closeSelectLayout(position);
            }
        });
        mSelectLayout.setup(mDelegate);
        mWeekPager.updateSelected(mDelegate.mSelectedCalendar, false);
    }

    /**
     * 设置日期范围
     *
     * @param minYear      最小年份
     * @param minYearMonth 最小年份对应月份
     * @param maxYear      最大月份
     * @param maxYearMonth 最大月份对应月份
     */
    public void setRange(int minYear, int minYearMonth,
                         int maxYear, int maxYearMonth) {
        mDelegate.setRange(minYear, minYearMonth,
                maxYear, maxYearMonth);
        mWeekPager.notifyDataSetChanged();
        mSelectLayout.notifyDataSetChanged();
        mMonthPager.notifyDataSetChanged();
        if (CalendarUtil.isCalendarInRange(mDelegate.mSelectedCalendar, mDelegate)) {
            scrollToCalendar(mDelegate.mSelectedCalendar.getYear(),
                    mDelegate.mSelectedCalendar.getMonth(),
                    mDelegate.mSelectedCalendar.getDay());

        } else {
            scrollToCurrent();
        }

    }

    /**
     * 获取当天
     *
     * @return 返回今天
     */
    public int getCurDay() {
        return mDelegate.getCurrentDay().getDay();
    }

    /**
     * 获取本月
     *
     * @return 返回本月
     */
    public int getCurMonth() {
        return mDelegate.getCurrentDay().getMonth();
    }

    /**
     * 获取本年
     *
     * @return 返回本年
     */
    public int getCurYear() {
        return mDelegate.getCurrentDay().getYear();
    }


    /**
     * 打开日历年月份快速选择
     *
     * @param year 年
     */
    @SuppressWarnings("deprecation")
    public void showYearSelectLayout(final int year) {
        showSelectLayout(year);
    }

    /**
     * 打开日历年月份快速选择
     * 请使用 showYearSelectLayout(final int year) 代替，这个没什么，越来越规范
     *
     * @param year 年
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public void showSelectLayout(final int year) {
        if (mParentLayout != null && mParentLayout.mContentView != null) {
            if (!mParentLayout.isExpand()) {
                mParentLayout.expand();
                return;
            }
        }
        mWeekPager.setVisibility(GONE);
        mDelegate.isShowYearSelectedLayout = true;
        if (mParentLayout != null) {
            mParentLayout.hideContentView();
        }
        mWeekBar.animate()
                .translationY(-mWeekBar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(260)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mWeekBar.setVisibility(GONE);
                        mSelectLayout.setVisibility(VISIBLE);
                        mSelectLayout.scrollToYear(year, false);
                        if (mParentLayout != null && mParentLayout.mContentView != null) {
                            mParentLayout.expand();
                        }
                    }
                });

        mMonthPager.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(260)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
    }


    /**
     * 年月份选择视图是否打开
     *
     * @return true or false
     */
    public boolean isYearSelectLayoutVisible() {
        return mSelectLayout.getVisibility() == VISIBLE;
    }

    /**
     * 关闭年月视图选择布局
     */
    public void closeYearSelectLayout() {
        int position = 12 * (mDelegate.mSelectedCalendar.getYear() - mDelegate.getMinYear()) +
                mDelegate.mSelectedCalendar.getMonth() - mDelegate.getMinYearMonth();
        closeSelectLayout(position);
    }

    /**
     * 关闭日历布局，同时会滚动到指定的位置
     *
     * @param position 某一年
     */
    private void closeSelectLayout(final int position) {
        mSelectLayout.setVisibility(GONE);
        mWeekBar.setVisibility(VISIBLE);
        if (position == mMonthPager.getCurrentItem()) {
            if (mDelegate.mDateSelectedListener != null) {
                mDelegate.mDateSelectedListener.onDateSelected(mDelegate.mSelectedCalendar, false);
            }
        } else {
            mMonthPager.setCurrentItem(position, false);
        }
        mWeekBar.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(280)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mWeekBar.setVisibility(VISIBLE);
                    }
                });
        mMonthPager.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(180)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mMonthPager.setVisibility(VISIBLE);
                        mMonthPager.clearAnimation();
                        if (mParentLayout != null) {
                            mParentLayout.showContentView();
                        }
                    }
                });
    }

    /**
     * 滚动到当前
     */
    public void scrollToCurrent() {
        scrollToCurrent(false);
    }

    /**
     * 滚动到当前
     *
     * @param smoothScroll smoothScroll
     */
    public void scrollToCurrent(boolean smoothScroll) {
        if (!CalendarUtil.isCalendarInRange(mDelegate.getCurrentDay(), mDelegate)) {
            return;
        }
        mDelegate.mSelectedCalendar = mDelegate.createCurrentDate();
        mWeekBar.onDateSelected(mDelegate.mSelectedCalendar, mDelegate.getWeekStart(), false);
        mWeekPager.scrollToCurrent(smoothScroll);

        mMonthPager.scrollToCurrent(smoothScroll);
        mSelectLayout.scrollToYear(mDelegate.getCurrentDay().getYear(), smoothScroll);
    }


    /**
     * 滚动到下一个月
     */
    public void scrollToNext() {
        scrollToNext(false);
    }

    /**
     * 滚动到下一个月
     *
     * @param smoothScroll smoothScroll
     */
    @SuppressWarnings("all")
    public void scrollToNext(boolean smoothScroll) {
        if (mWeekPager.getVisibility() == VISIBLE) {
            mWeekPager.setCurrentItem(mWeekPager.getCurrentItem() + 1, smoothScroll);
        } else {
            mMonthPager.setCurrentItem(mMonthPager.getCurrentItem() + 1, smoothScroll);
        }

    }

    /**
     * 滚动到上一个月
     */
    public void scrollToPre() {
        scrollToPre(false);
    }

    /**
     * 滚动到上一个月
     *
     * @param smoothScroll smoothScroll
     */
    @SuppressWarnings("all")
    public void scrollToPre(boolean smoothScroll) {
        if (mWeekPager.getVisibility() == VISIBLE) {
            mWeekPager.setCurrentItem(mWeekPager.getCurrentItem() - 1, smoothScroll);
        } else {
            mMonthPager.setCurrentItem(mMonthPager.getCurrentItem() - 1, smoothScroll);
        }
    }

    /**
     * 滚动到指定日期
     *
     * @param year  year
     * @param month month
     * @param day   day
     */
    public void scrollToCalendar(int year, int month, int day) {
        scrollToCalendar(year, month, day, false);
    }

    /**
     * 滚动到指定日期
     *
     * @param year         year
     * @param month        month
     * @param day          day
     * @param smoothScroll smoothScroll
     */
    @SuppressWarnings("all")
    public void scrollToCalendar(int year, int month, int day, boolean smoothScroll) {
        if (mWeekPager.getVisibility() == VISIBLE) {
            mWeekPager.scrollToCalendar(year, month, day, smoothScroll);
        } else {
            mMonthPager.scrollToCalendar(year, month, day, smoothScroll);
        }
    }

    /**
     * 滚动到某一年
     *
     * @param year 快速滚动的年份
     */
    public void scrollToYear(int year) {
        scrollToYear(year, false);
    }

    /**
     * 滚动到某一年
     *
     * @param year         快速滚动的年份
     * @param smoothScroll smoothScroll
     */
    @SuppressWarnings("all")
    public void scrollToYear(int year, boolean smoothScroll) {
        mMonthPager.setCurrentItem(12 * (year - mDelegate.getMinYear()) +
                mDelegate.getCurrentDay().getMonth() - mDelegate.getMinYearMonth(), smoothScroll);
        mSelectLayout.scrollToYear(year, smoothScroll);
    }


    /**
     * 年份改变事件
     *
     * @param listener listener
     */
    public void setOnYearChangeListener(OnYearChangeListener listener) {
        this.mDelegate.mYearChangeListener = listener;
    }

    /**
     * 月份改变事件
     *
     * @param listener listener
     */
    public void setOnMonthChangeListener(OnMonthChangeListener listener) {
        this.mDelegate.mMonthChangeListener = listener;
    }

    /**
     * 设置日期选中事件
     *
     * @param listener 日期选中事件
     */
    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mDelegate.mDateSelectedListener = listener;
        if (mDelegate.mDateSelectedListener != null) {
            if (!CalendarUtil.isCalendarInRange(mDelegate.mSelectedCalendar, mDelegate)) {
                return;
            }
            mDelegate.mDateSelectedListener.onDateSelected(mDelegate.mSelectedCalendar, false);
        }
    }

    /**
     * 日期长按事件
     *
     * @param listener listener
     */
    public void setOnDateLongClickListener(OnDateLongClickListener listener) {
        this.mDelegate.mDateLongClickListener = listener;
    }


    /**
     * 日期长按事件
     *
     * @param preventLongPressedSelect 防止长按选择日期
     * @param listener                 listener
     */
    public void setOnDateLongClickListener(OnDateLongClickListener listener, boolean preventLongPressedSelect) {
        this.mDelegate.mDateLongClickListener = listener;
        this.mDelegate.setPreventLongPressedSelected(preventLongPressedSelect);
    }

    /**
     * 初始化时初始化日历卡默认选择位置
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() != null && getParent() instanceof CalendarLayout) {
            mParentLayout = (CalendarLayout) getParent();
            mParentLayout.mItemHeight = mDelegate.getCalendarItemHeight();
            mMonthPager.mParentLayout = mParentLayout;
            mWeekPager.mParentLayout = mParentLayout;
            mParentLayout.mWeekBar = mWeekBar;
            mParentLayout.setup(mDelegate);
            mParentLayout.initStatus();
        }
    }


    /**
     * 标记哪些日期有事件
     *
     * @param mSchemeDate mSchemeDate 通过自己的需求转换即可
     */
    public void setSchemeDate(List<Calendar> mSchemeDate) {
        this.mDelegate.mSchemeDate = mSchemeDate;
        mMonthPager.updateScheme();
        mWeekPager.updateScheme();
    }


    /**
     * 清空日期标记
     */
    public void clearSchemeDate() {
        this.mDelegate.mSchemeDate = null;
        mMonthPager.updateScheme();
        mWeekPager.updateScheme();
    }


    /**
     * 移除某天的标记
     * 这个API是安全的，无效try cache
     *
     * @param calendar calendar
     */
    public void removeSchemeDate(Calendar calendar) {
        if (mDelegate.mSchemeDate == null ||
                mDelegate.mSchemeDate.size() == 0 ||
                calendar == null) {
            return;
        }
        if (mDelegate.mSchemeDate.contains(calendar)) {
            mDelegate.mSchemeDate.remove(calendar);
        }
        mMonthPager.updateScheme();
        mWeekPager.updateScheme();
    }

    /**
     * 设置背景色
     *
     * @param yearViewBackground 年份卡片的背景色
     * @param weekBackground     星期栏背景色
     * @param lineBg             线的颜色
     */
    public void setBackground(int yearViewBackground, int weekBackground, int lineBg) {
        mWeekBar.setBackgroundColor(weekBackground);
        mSelectLayout.setBackgroundColor(yearViewBackground);
        mWeekLine.setBackgroundColor(lineBg);
    }


    /**
     * 设置文本颜色
     *
     * @param currentDayTextColor      今天字体颜色
     * @param curMonthTextColor        当前月份字体颜色
     * @param otherMonthColor          其它月份字体颜色
     * @param curMonthLunarTextColor   当前月份农历字体颜色
     * @param otherMonthLunarTextColor 其它农历字体颜色
     */
    public void setTextColor(
            int currentDayTextColor,
            int curMonthTextColor,
            int otherMonthColor,
            int curMonthLunarTextColor,
            int otherMonthLunarTextColor) {
        mDelegate.setTextColor(currentDayTextColor, curMonthTextColor, otherMonthColor, curMonthLunarTextColor, otherMonthLunarTextColor);
    }

    /**
     * 设置选择的效果
     *
     * @param selectedThemeColor     选中的标记颜色
     * @param selectedTextColor      选中的字体颜色
     * @param selectedLunarTextColor 选中的农历字体颜色
     */
    public void setSelectedColor(int selectedThemeColor, int selectedTextColor, int selectedLunarTextColor) {
        mDelegate.setSelectColor(selectedThemeColor, selectedTextColor, selectedLunarTextColor);
    }

    /**
     * 定制颜色
     *
     * @param selectedThemeColor 选中的标记颜色
     * @param schemeColor        标记背景色
     */
    public void setThemeColor(int selectedThemeColor, int schemeColor) {
        mDelegate.setThemeColor(selectedThemeColor, schemeColor);
    }

    /**
     * 设置标记的色
     *
     * @param schemeLunarTextColor 标记农历颜色
     * @param schemeColor          标记背景色
     * @param schemeTextColor      标记字体颜色
     */
    public void setSchemeColor(int schemeColor, int schemeTextColor, int schemeLunarTextColor) {
        mDelegate.setSchemeColor(schemeColor, schemeTextColor, schemeLunarTextColor);
    }

    /**
     * 设置年视图的颜色
     *
     * @param yearViewMonthTextColor 年视图月份颜色
     * @param yearViewDayTextColor   年视图天的颜色
     * @param yarViewSchemeTextColor 年视图标记颜色
     */
    public void setYearViewTextColor(int yearViewMonthTextColor, int yearViewDayTextColor, int yarViewSchemeTextColor) {
        mDelegate.setYearViewTextColor(yearViewMonthTextColor, yearViewDayTextColor, yarViewSchemeTextColor);
    }

    /**
     * 设置星期栏的背景和字体颜色
     *
     * @param weekBackground 背景色
     * @param weekTextColor  字体颜色
     */
    public void setWeeColor(int weekBackground, int weekTextColor) {
        mWeekBar.setBackgroundColor(weekBackground);
        mWeekBar.setTextColor(weekTextColor);
    }


    /**
     * 更新界面，
     * 重新设置颜色等都需要调用该方法
     */
    public void update() {
        mWeekBar.onWeekStartChange(mDelegate.getWeekStart());
        mSelectLayout.update();
        mMonthPager.updateScheme();
        mWeekPager.updateScheme();
    }

    /**
     * 更新周视图
     */
    public void updateWeekBar() {
        mWeekBar.onWeekStartChange(mDelegate.getWeekStart());
    }


    /**
     * 更新当前日期
     */
    public void updateCurrentDate(){
        mDelegate.updateCurrentDay();
        mMonthPager.updateCurrentDate();
        mWeekPager.updateCurrentDate();
        //mWeekBar.onDateSelected(mDelegate.getCurrentDay(),mDelegate.getWeekStart(),false);
    }

    /**
     * 获取选择的日期
     *
     * @return 获取选择的日期
     */
    public Calendar getSelectedCalendar() {
        return mDelegate.mSelectedCalendar;
    }


    /**
     * 年份改变事件，快速年份切换
     */
    public interface OnYearChangeListener {
        void onYearChange(int year);
    }

    /**
     * 月份切换事件
     */
    public interface OnMonthChangeListener {
        void onMonthChange(int year, int month);
    }

    /**
     * 内部日期选择，不暴露外部使用
     * 主要是用于更新日历CalendarLayout位置
     */
    interface OnInnerDateSelectedListener {
        /**
         * 月视图点击
         *
         * @param calendar calendar
         * @param isClick  是否是点击
         */
        void onMonthDateSelected(Calendar calendar, boolean isClick);

        /**
         * 周视图点击
         *
         * @param calendar calendar
         */
        void onWeekDateSelected(Calendar calendar, boolean isClick);
    }

    /**
     * 外部日期选择事件
     */
    public interface OnDateSelectedListener {
        void onDateSelected(Calendar calendar, boolean isClick);
    }


    /**
     * 外部日期长按事件
     */
    public interface OnDateLongClickListener {
        void onDateLongClick(Calendar calendar);
    }


}
