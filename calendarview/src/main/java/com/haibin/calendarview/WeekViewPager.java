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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

/**
 * 周视图滑动ViewPager，需要动态固定高度
 * 周视图是连续不断的视图，因此不能简单的得出每年都有52+1周，这样会计算重叠的部分
 * WeekViewPager需要和CalendarView关联:
 */

public class WeekViewPager extends ViewPager {

    private int mWeekCount;
    private CustomCalendarViewDelegate mDelegate;

    /**
     * 日历布局，需要在日历下方放自己的布局
     */
    CalendarLayout mParentLayout;


    public WeekViewPager(Context context) {
        this(context, null);
    }

    public WeekViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setup(CustomCalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        init();
    }

    private void init() {
        mWeekCount = Util.getWeekCountBetweenYearAndYear(mDelegate.getMinYear(), mDelegate.getMinYearMonth(),
                mDelegate.getMaxYear(), mDelegate.getMaxYearMonth());
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
                    view.performClickCalendar(mDelegate.mSelectedCalendar, true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void notifyDataSetChanged(){
        mWeekCount = Util.getWeekCountBetweenYearAndYear(mDelegate.getMinYear(), mDelegate.getMinYearMonth(),
                mDelegate.getMaxYear(), mDelegate.getMaxYearMonth());
        getAdapter().notifyDataSetChanged();
    }

    /**
     * 滚动到指定日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    void scrollToCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setCurrentDay(calendar.equals(mDelegate.getCurrentDay()));
        mDelegate.mSelectedCalendar = calendar;
        updateSelected(calendar);
    }

    /**
     * 滚动到当前
     */
    void scrollToCurrent() {
        int position = Util.getWeekFromCalendarBetweenYearAndYear(mDelegate.getCurrentDay(),
                mDelegate.getMinYear(),
                mDelegate.getMinYearMonth()) - 1;
        setCurrentItem(position);
        WeekView view = (WeekView) findViewWithTag(position);
        if (view != null) {
            view.performClickCalendar(mDelegate.getCurrentDay(), false);
            view.setSelectedCalendar(mDelegate.getCurrentDay());
            view.invalidate();
        }
    }

    /**
     * 更新任意一个选择的日期
     */
    void updateSelected(Calendar calendar) {
        int position = Util.getWeekFromCalendarBetweenYearAndYear(calendar, mDelegate.getMinYear(), mDelegate.getMinYearMonth()) - 1;
        setCurrentItem(position);
        WeekView view = (WeekView) findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(calendar);
            view.invalidate();
        }
    }


    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.update();
        }
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
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Calendar calendar = Util.getFirstCalendarFromWeekCount(mDelegate.getMinYear(), mDelegate.getMinYearMonth(), position + 1);
            WeekView view;
            if (TextUtils.isEmpty(mDelegate.getWeekViewClass())) {
                view = new DefaultWeekView(getContext());
            } else {
                try {
                    Class cls = Class.forName(mDelegate.getWeekViewClass());
                    @SuppressWarnings("unchecked")
                    Constructor constructor = cls.getConstructor(Context.class);
                    view = (WeekView) constructor.newInstance(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
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
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
