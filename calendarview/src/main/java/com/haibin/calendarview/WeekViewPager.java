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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 周视图滑动ViewPager，需要动态固定高度
 * 周视图是连续不断的视图，因此不能简单的得出每年都有52+1周，这样会计算重叠的部分
 * WeekViewPager需要和CalendarView关联:
 */

public class WeekViewPager extends ViewPager {

    /**
     * 日期被选中监听
     */
    CalendarView.OnDateSelectedListener mDateSelectedListener;

    /**
     * 内部日期切换监听，用于内部更新计算
     */
    CalendarView.OnInnerDateSelectedListener mInnerListener;

    /**
     * 日期切换监听
     */
    CalendarView.OnDateChangeListener mListener;

    /**
     * 日历布局，需要在日历下方放自己的布局
     */
    CalendarLayout mParentLayout;

    private String mWeekViewClass;
    /**
     * 周视图高度
     */
    private int mItemHeight;

    /**
     * 最小年份和最大年份
     */
    int mMinYear, mMaxYear;

    /**
     * 标记的日期
     */
    List<Calendar> mSchemeDate;

    /**
     * 各种字体颜色，看名字知道对应的地方
     */
    int mCurDayTextColor, mWeekTextColor,
            mSchemeTextColor,
            mSchemeLunarTextColor,
            mOtherMonthTextColor,
            mCurrentMonthTextColor,
            mSelectedTextColor,
            mSelectedLunarTextColor,
            mCurMonthLunarTextColor,
            mOtherMonthLunarTextColor;

    int mDayTextSize, mLunarTextSize;
    /**
     * 标记的主题色和选中的主题色
     */
    int mSchemeThemeColor, mSelectedThemeColor;

    Calendar mSelectedCalendar;

    public WeekViewPager(Context context) {
        this(context, null);
    }

    public WeekViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setAdapter(new WeekViewPagerAdapter());
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //默认的显示星期四，周视图切换就显示星期4
                if (getVisibility() == GONE)
                    return;
                WeekView view = (WeekView) findViewWithTag(position);
                if (view != null) {
                    view.performClickCalendar(mSelectedCalendar);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 返回到当前的日历周视图，这个方法调用了，月视图就不调用滚动
     */
    void scrollToCurrent(Calendar calendar) {
        this.mSelectedCalendar = calendar;
        int position = Util.getWeekFromCalendarBetweenYearAndYear(mSelectedCalendar, mMinYear) - 1;
        setCurrentItem(position);
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.setSelectedCalendar(mSelectedCalendar);
        }
    }

    void updateSelected(Calendar calendar) {
        this.mSelectedCalendar = calendar;
        int position = Util.getWeekFromCalendarBetweenYearAndYear(calendar, mMinYear) - 1;
        setCurrentItem(position);
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.setSelectedCalendar(mSelectedCalendar);
        }
    }

    /**
     * 设置周视图高度
     *
     * @param mItemHeight 周视图高度
     */
    public void setItemHeight(int mItemHeight) {
        this.mItemHeight = mItemHeight;
    }

    /**
     * 周视图的高度应该与日历项的高度一致
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 周视图切换
     */
    private class WeekViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Util.getWeekCountBetweenYearAndYear(mMinYear, mMaxYear);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Calendar calendar = Util.getFirstCalendarFromWeekCount(mMinYear, position + 1);
            WeekView view;
            try {
                Class cls = Class.forName(mWeekViewClass);
                @SuppressWarnings("unchecked")
                Constructor constructor = cls.getConstructor(Context.class);
                view = (WeekView) constructor.newInstance(getContext());
            } catch (Exception e) {
                e.printStackTrace();
                view = new TestWeekView(getContext());
            }
            view.mListener = mListener;
            view.mDateSelectedListener = mDateSelectedListener;
            view.mInnerListener = mInnerListener;
            view.mParentLayout = mParentLayout;
            view.setItemHeight(mItemHeight);
            view.setup(calendar);
            view.setDayTextSize(mDayTextSize, mLunarTextSize);
            view.setTag(position);
            view.setSelectedCalendar(mSelectedCalendar);
            view.mSchemes = mSchemeDate;
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
}
