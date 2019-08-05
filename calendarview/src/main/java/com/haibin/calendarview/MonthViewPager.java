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

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Constructor;
import java.util.List;


/**
 * 月份切换ViewPager，自定义适应高度
 */
public final class MonthViewPager extends ViewPager {

    private boolean isUpdateMonthView;

    private int mMonthCount;

    private CalendarViewDelegate mDelegate;

    private int mNextViewHeight, mPreViewHeight, mCurrentViewHeight;

    CalendarLayout mParentLayout;

    WeekViewPager mWeekPager;

    WeekBar mWeekBar;

    /**
     * 是否使用滚动到某一天
     */
    private boolean isUsingScrollToCalendar = false;

    public MonthViewPager(Context context) {
        this(context, null);
    }

    public MonthViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化
     *
     * @param delegate delegate
     */
    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;

        updateMonthViewHeight(mDelegate.getCurrentDay().getYear(),
                mDelegate.getCurrentDay().getMonth());

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mCurrentViewHeight;
        setLayoutParams(params);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
        setAdapter(new MonthViewPagerAdapter());
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
                    return;
                }
                int height;
                if (position < getCurrentItem()) {//右滑-1
                    height = (int) ((mPreViewHeight)
                            * (1 - positionOffset) +
                            mCurrentViewHeight
                                    * positionOffset);
                } else {//左滑+！
                    height = (int) ((mCurrentViewHeight)
                            * (1 - positionOffset) +
                            (mNextViewHeight)
                                    * positionOffset);
                }
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = height;
                setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                Calendar calendar = CalendarUtil.getFirstCalendarFromMonthViewPager(position, mDelegate);
                if (getVisibility() == VISIBLE) {
                    if (!mDelegate.isShowYearSelectedLayout &&
                            mDelegate.mIndexCalendar != null &&
                            calendar.getYear() != mDelegate.mIndexCalendar.getYear() &&
                            mDelegate.mYearChangeListener != null) {
                        mDelegate.mYearChangeListener.onYearChange(calendar.getYear());
                    }
                    mDelegate.mIndexCalendar = calendar;
                }
                //月份改变事件
                if (mDelegate.mMonthChangeListener != null) {
                    mDelegate.mMonthChangeListener.onMonthChange(calendar.getYear(), calendar.getMonth());
                }

                //周视图显示的时候就需要动态改变月视图高度
                if (mWeekPager.getVisibility() == VISIBLE) {
                    updateMonthViewHeight(calendar.getYear(), calendar.getMonth());
                    return;
                }


                if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
                    if (!calendar.isCurrentMonth()) {
                        mDelegate.mSelectedCalendar = calendar;
                    } else {
                        mDelegate.mSelectedCalendar = CalendarUtil.getRangeEdgeCalendar(calendar, mDelegate);
                    }
                    mDelegate.mIndexCalendar = mDelegate.mSelectedCalendar;
                } else {
                    if (mDelegate.mSelectedStartRangeCalendar != null &&
                            mDelegate.mSelectedStartRangeCalendar.isSameMonth(mDelegate.mIndexCalendar)) {
                        mDelegate.mIndexCalendar = mDelegate.mSelectedStartRangeCalendar;
                    } else {
                        if (calendar.isSameMonth(mDelegate.mSelectedCalendar)) {
                            mDelegate.mIndexCalendar = mDelegate.mSelectedCalendar;
                        }
                    }
                }

                mDelegate.updateSelectCalendarScheme();
                if (!isUsingScrollToCalendar && mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
                    mWeekBar.onDateSelected(mDelegate.mSelectedCalendar, mDelegate.getWeekStart(), false);
                    if (mDelegate.mCalendarSelectListener != null) {
                        mDelegate.mCalendarSelectListener.onCalendarSelect(mDelegate.mSelectedCalendar, false);
                    }
                }

                BaseMonthView view = findViewWithTag(position);
                if (view != null) {
                    int index = view.getSelectedIndex(mDelegate.mIndexCalendar);
                    if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
                        view.mCurrentItem = index;
                    }
                    if (index >= 0 && mParentLayout != null) {
                        mParentLayout.updateSelectPosition(index);
                    }
                    view.invalidate();
                }
                mWeekPager.updateSelected(mDelegate.mIndexCalendar, false);
                updateMonthViewHeight(calendar.getYear(), calendar.getMonth());
                isUsingScrollToCalendar = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 更新月视图的高度
     *
     * @param year  year
     * @param month month
     */
    private void updateMonthViewHeight(int year, int month) {
        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {//非动态高度就不需要了
            mCurrentViewHeight = 6 * mDelegate.getCalendarItemHeight();
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = mCurrentViewHeight;
            return;
        }

        if (mParentLayout != null) {
            if (getVisibility() != VISIBLE) {//如果已经显示周视图，则需要动态改变月视图高度，否则显示就有bug
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = CalendarUtil.getMonthViewHeight(year, month,
                        mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                        mDelegate.getMonthViewShowMode());
                setLayoutParams(params);
            }
            mParentLayout.updateContentViewTranslateY();
        }
        mCurrentViewHeight = CalendarUtil.getMonthViewHeight(year, month,
                mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                mDelegate.getMonthViewShowMode());
        if (month == 1) {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year - 1, 12,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
            mNextViewHeight = CalendarUtil.getMonthViewHeight(year, 2,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
        } else {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year, month - 1,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
            if (month == 12) {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year + 1, 1,
                        mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                        mDelegate.getMonthViewShowMode());
            } else {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year, month + 1,
                        mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                        mDelegate.getMonthViewShowMode());
            }
        }
    }

    /**
     * 刷新
     */
    void notifyDataSetChanged() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
        notifyAdapterDataSetChanged();
    }

    /**
     * 更新月视图Class
     */
    void updateMonthViewClass() {
        isUpdateMonthView = true;
        notifyAdapterDataSetChanged();
        isUpdateMonthView = false;
    }

    /**
     * 更新日期范围
     */
    final void updateRange() {
        isUpdateMonthView = true;
        notifyDataSetChanged();
        isUpdateMonthView = false;
        if (getVisibility() != VISIBLE) {
            return;
        }
        isUsingScrollToCalendar = false;
        Calendar calendar = mDelegate.mSelectedCalendar;
        int y = calendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendar.getMonth() - mDelegate.getMinYearMonth();
        setCurrentItem(position, false);
        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }


        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendar, false);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }
        updateSelected();
    }

    /**
     * 滚动到指定日期
     *
     * @param year           年
     * @param month          月
     * @param day            日
     * @param invokeListener 调用日期事件
     */
    void scrollToCalendar(int year, int month, int day, boolean smoothScroll, boolean invokeListener) {
        isUsingScrollToCalendar = true;
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setCurrentDay(calendar.equals(mDelegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendar);
        mDelegate.mIndexCalendar = calendar;
        mDelegate.mSelectedCalendar = calendar;
        mDelegate.updateSelectCalendarScheme();
        int y = calendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendar.getMonth() - mDelegate.getMinYearMonth();
        int curItem = getCurrentItem();
        if (curItem == position) {
            isUsingScrollToCalendar = false;
        }
        setCurrentItem(position, smoothScroll);

        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }

        if (mDelegate.mCalendarSelectListener != null && invokeListener) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }
        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendar, false);
        }

        updateSelected();
    }

    /**
     * 滚动到当前日期
     */
    void scrollToCurrent(boolean smoothScroll) {
        isUsingScrollToCalendar = true;
        int position = 12 * (mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear()) +
                mDelegate.getCurrentDay().getMonth() - mDelegate.getMinYearMonth();
        int curItem = getCurrentItem();
        if (curItem == position) {
            isUsingScrollToCalendar = false;
        }

        setCurrentItem(position, smoothScroll);

        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.getCurrentDay());
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.getCurrentDay()));
            }
        }

        if (mDelegate.mCalendarSelectListener != null && getVisibility() == VISIBLE) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(mDelegate.mSelectedCalendar, false);
        }
    }

    /**
     * 获取当前月份数据
     *
     * @return 获取当前月份数据
     */
    List<Calendar> getCurrentMonthCalendars() {
        BaseMonthView view = findViewWithTag(getCurrentItem());
        if (view == null) {
            return null;
        }
        return view.mItems;
    }

    /**
     * 更新为默认选择模式
     */
    void updateDefaultSelect() {
        BaseMonthView view = findViewWithTag(getCurrentItem());
        if (view != null) {
            int index = view.getSelectedIndex(mDelegate.mSelectedCalendar);
            view.mCurrentItem = index;
            if (index >= 0 && mParentLayout != null) {
                mParentLayout.updateSelectPosition(index);
            }
            view.invalidate();
        }
    }


    /**
     * 更新选择效果
     */
    void updateSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }

    /**
     * 更新字体颜色大小
     */
    final void updateStyle() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateStyle();
            view.invalidate();
        }
    }

    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.update();
        }
    }

    /**
     * 更新当前日期，夜间过度的时候调用这个函数，一般不需要调用
     */
    void updateCurrentDate() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateCurrentDate();
        }
    }


    /**
     * 更新显示模式
     */
    void updateShowMode() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateShowMode();
            view.requestLayout();
        }
        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
            mCurrentViewHeight = 6 * mDelegate.getCalendarItemHeight();
            mNextViewHeight = mCurrentViewHeight;
            mPreViewHeight = mCurrentViewHeight;
        } else {
            updateMonthViewHeight(mDelegate.mSelectedCalendar.getYear(), mDelegate.mSelectedCalendar.getMonth());
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mCurrentViewHeight;
        setLayoutParams(params);
        if (mParentLayout != null) {
            mParentLayout.updateContentViewTranslateY();
        }
    }

    /**
     * 更新周起始
     */
    void updateWeekStart() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateWeekStart();
            view.requestLayout();
        }

        updateMonthViewHeight(mDelegate.mSelectedCalendar.getYear(), mDelegate.mSelectedCalendar.getMonth());
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mCurrentViewHeight;
        setLayoutParams(params);
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(mDelegate.mSelectedCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }
        updateSelected();
    }

    /**
     * 更新高度
     */
    final void updateItemHeight() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateItemHeight();
            view.requestLayout();
        }

        int year = mDelegate.mIndexCalendar.getYear();
        int month = mDelegate.mIndexCalendar.getMonth();
        mCurrentViewHeight = CalendarUtil.getMonthViewHeight(year, month,
                mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                mDelegate.getMonthViewShowMode());
        if (month == 1) {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year - 1, 12,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
            mNextViewHeight = CalendarUtil.getMonthViewHeight(year, 2,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
        } else {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year, month - 1,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
            if (month == 12) {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year + 1, 1,
                        mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                        mDelegate.getMonthViewShowMode());
            } else {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year, month + 1,
                        mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                        mDelegate.getMonthViewShowMode());
            }
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mCurrentViewHeight;
        setLayoutParams(params);
    }

    /**
     * 清除选择范围
     */
    final void clearSelectRange() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.invalidate();
        }
    }

    /**
     * 清除单选选择
     */
    final void clearSingleSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.mCurrentItem = -1;
            view.invalidate();
        }
    }

    /**
     * 清除单选选择
     */
    final void clearMultiSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.mCurrentItem = -1;
            view.invalidate();
        }
    }

    private void notifyAdapterDataSetChanged() {
        if (getAdapter() == null) {
            return;
        }
        getAdapter().notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDelegate.isMonthViewScrollable() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDelegate.isMonthViewScrollable() && super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (Math.abs(getCurrentItem() - item) > 1) {
            super.setCurrentItem(item, false);
        } else {
            super.setCurrentItem(item, smoothScroll);
        }
    }

    /**
     * 日历卡月份Adapter
     */
    private final class MonthViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mMonthCount;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return isUpdateMonthView ? POSITION_NONE : super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            int year = (position + mDelegate.getMinYearMonth() - 1) / 12 + mDelegate.getMinYear();
            int month = (position + mDelegate.getMinYearMonth() - 1) % 12 + 1;
            BaseMonthView view;
            try {
                Constructor constructor = mDelegate.getMonthViewClass().getConstructor(Context.class);
                view = (BaseMonthView) constructor.newInstance(getContext());
            } catch (Exception e) {
                e.printStackTrace();
                return new DefaultMonthView(getContext());
            }
            view.mMonthViewPager = MonthViewPager.this;
            view.mParentLayout = mParentLayout;
            view.setup(mDelegate);
            view.setTag(position);
            view.initMonthWithDate(year, month);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            BaseView view = (BaseView) object;
            view.onDestroy();
            container.removeView(view);
        }
    }


}
