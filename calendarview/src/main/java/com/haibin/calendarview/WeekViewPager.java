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
 * 周视图滑动ViewPager，需要动态固定高度
 * 周视图是连续不断的视图，因此不能简单的得出每年都有52+1周，这样会计算重叠的部分
 * WeekViewPager需要和CalendarView关联:
 */

public final class WeekViewPager extends ViewPager {
    private boolean isUpdateWeekView;
    private int mWeekCount;
    private CalendarViewDelegate mDelegate;

    /**
     * 日历布局，需要在日历下方放自己的布局
     */
    CalendarLayout mParentLayout;

    /**
     * 是否使用滚动到某一天
     */
    private boolean isUsingScrollToCalendar = false;

    public WeekViewPager(Context context) {
        this(context, null);
    }

    public WeekViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        init();
    }

    private void init() {
        mWeekCount = CalendarUtil.getWeekCountBetweenBothCalendar(
                mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMinYearDay(),
                mDelegate.getMaxYear(),
                mDelegate.getMaxYearMonth(),
                mDelegate.getMaxYearDay(),
                mDelegate.getWeekStart());
        setAdapter(new WeekViewPagerAdapter());
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //默认的显示星期四，周视图切换就显示星期4
                if (getVisibility() != VISIBLE) {
                    isUsingScrollToCalendar = false;
                    return;
                }
                if (isUsingScrollToCalendar) {
                    isUsingScrollToCalendar = false;
                    return;
                }
                BaseWeekView view = findViewWithTag(position);
                if (view != null) {
                    view.performClickCalendar(mDelegate.getSelectMode() != CalendarViewDelegate.SELECT_MODE_DEFAULT ?
                            mDelegate.mIndexCalendar : mDelegate.mSelectedCalendar, !isUsingScrollToCalendar);
                    if (mDelegate.mWeekChangeListener != null) {
                        mDelegate.mWeekChangeListener.onWeekChange(getCurrentWeekCalendars());
                    }
                }
                isUsingScrollToCalendar = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取当前周数据
     *
     * @return 获取当前周数据
     */
    List<Calendar> getCurrentWeekCalendars() {
        List<Calendar> calendars = CalendarUtil.getWeekCalendars(mDelegate.mIndexCalendar,
                mDelegate);
        mDelegate.addSchemesFromMap(calendars);
        return calendars;
    }


    /**
     * 更新周视图
     */
    void notifyDataSetChanged() {
        mWeekCount = CalendarUtil.getWeekCountBetweenBothCalendar(
                mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMinYearDay(),
                mDelegate.getMaxYear(),
                mDelegate.getMaxYearMonth(),
                mDelegate.getMaxYearDay(),
                mDelegate.getWeekStart());
        notifyAdapterDataSetChanged();
    }

    /**
     * 更新周视图布局
     */
    void updateWeekViewClass() {
        isUpdateWeekView = true;
        notifyAdapterDataSetChanged();
        isUpdateWeekView = false;
    }

    /**
     * 更新日期范围
     */
    void updateRange() {
        isUpdateWeekView = true;
        notifyDataSetChanged();
        isUpdateWeekView = false;
        if (getVisibility() != VISIBLE) {
            return;
        }
        isUsingScrollToCalendar = true;
        Calendar calendar = mDelegate.mSelectedCalendar;
        updateSelected(calendar, false);
        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendar, false);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }

        int i = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
        mParentLayout.updateSelectWeek(i);
    }

    /**
     * 滚动到指定日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
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
        updateSelected(calendar, smoothScroll);
        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendar, false);
        }
        if (mDelegate.mCalendarSelectListener != null && invokeListener) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }
        int i = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
        mParentLayout.updateSelectWeek(i);
    }

    /**
     * 滚动到当前
     */
    void scrollToCurrent(boolean smoothScroll) {
        isUsingScrollToCalendar = true;
        int position = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(mDelegate.getCurrentDay(),
                mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMinYearDay(),
                mDelegate.getWeekStart()) - 1;
        int curItem = getCurrentItem();
        if (curItem == position) {
            isUsingScrollToCalendar = false;
        }
        setCurrentItem(position, smoothScroll);
        BaseWeekView view = findViewWithTag(position);
        if (view != null) {
            view.performClickCalendar(mDelegate.getCurrentDay(), false);
            view.setSelectedCalendar(mDelegate.getCurrentDay());
            view.invalidate();
        }

        if (mDelegate.mCalendarSelectListener != null && getVisibility() == VISIBLE) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(mDelegate.mSelectedCalendar, false);
        }

        if (getVisibility() == VISIBLE) {
            mDelegate.mInnerListener.onWeekDateSelected(mDelegate.getCurrentDay(), false);
        }
        int i = CalendarUtil.getWeekFromDayInMonth(mDelegate.getCurrentDay(), mDelegate.getWeekStart());
        mParentLayout.updateSelectWeek(i);
    }

    /**
     * 更新任意一个选择的日期
     */
    void updateSelected(Calendar calendar, boolean smoothScroll) {
        int position = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendar,
                mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMinYearDay(),
                mDelegate.getWeekStart()) - 1;
        int curItem = getCurrentItem();
        isUsingScrollToCalendar = curItem != position;
        setCurrentItem(position, smoothScroll);
        BaseWeekView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(calendar);
            view.invalidate();
        }
    }


    /**
     * 更新单选模式
     */
    void updateSingleSelect() {
        if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.updateSingleSelect();
        }
    }

    /**
     * 更新为默认选择模式
     */
    void updateDefaultSelect() {
        BaseWeekView view = findViewWithTag(getCurrentItem());
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }

    /**
     * 更新选择效果
     */
    void updateSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }

    /**
     * 更新字体颜色大小
     */
    final void updateStyle() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.updateStyle();
            view.invalidate();
        }
    }

    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.update();
        }
    }

    /**
     * 更新当前日期，夜间过度的时候调用这个函数，一般不需要调用
     */
    void updateCurrentDate() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.updateCurrentDate();
        }
    }

    /**
     * 更新显示模式
     */
    void updateShowMode() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.updateShowMode();
        }
    }

    /**
     * 更新周起始
     */
    void updateWeekStart() {
        if (getAdapter() == null) {
            return;
        }
        int count = getAdapter().getCount();
        mWeekCount = CalendarUtil.getWeekCountBetweenBothCalendar(
                mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMinYearDay(),
                mDelegate.getMaxYear(),
                mDelegate.getMaxYearMonth(),
                mDelegate.getMaxYearDay(),
                mDelegate.getWeekStart());
        /*
         * 如果count发生变化，意味着数据源变化，则必须先调用notifyDataSetChanged()，
         * 否则会抛出异常
         */
        if (count != mWeekCount) {
            isUpdateWeekView = true;
            getAdapter().notifyDataSetChanged();
        }
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.updateWeekStart();
        }
        isUpdateWeekView = false;
        updateSelected(mDelegate.mSelectedCalendar, false);
    }

    /**
     * 更新高度
     */
    final void updateItemHeight() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.updateItemHeight();
            view.requestLayout();
        }
    }

    /**
     * 清除选择范围
     */
    final void clearSelectRange() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.invalidate();
        }
    }

    final void clearSingleSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
            view.mCurrentItem = -1;
            view.invalidate();
        }
    }

    final void clearMultiSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseWeekView view = (BaseWeekView) getChildAt(i);
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
        return mDelegate.isWeekViewScrollable() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDelegate.isWeekViewScrollable() && super.onInterceptTouchEvent(ev);
    }

    /**
     * 周视图的高度应该与日历项的高度一致
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mDelegate.getCalendarItemHeight(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 周视图切换
     */
    private class WeekViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mWeekCount;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return isUpdateWeekView ? POSITION_NONE : super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Calendar calendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(mDelegate.getMinYear(),
                    mDelegate.getMinYearMonth(),
                    mDelegate.getMinYearDay(),
                    position + 1,
                    mDelegate.getWeekStart());
            BaseWeekView view;
            try {
                Constructor constructor = mDelegate.getWeekViewClass().getConstructor(Context.class);
                view = (BaseWeekView) constructor.newInstance(getContext());
            } catch (Exception e) {
                e.printStackTrace();
                return new DefaultWeekView(getContext());
            }
            view.mParentLayout = mParentLayout;
            view.setup(mDelegate);
            view.setup(calendar);
            view.setTag(position);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            BaseWeekView view = (BaseWeekView) object;
            view.onDestroy();
            container.removeView(view);
        }
    }
}
