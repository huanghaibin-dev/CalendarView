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
 * 月份切换ViewPager，自定义适应高度
 */
@SuppressWarnings("deprecation")
public class MonthViewPager extends ViewPager {

    private int mMonthCount;
    private CustomCalendarViewDelegate mDelegate;
    CalendarLayout mParentLayout;
    WeekViewPager mWeekPager;

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
    void setup(CustomCalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        init();
    }

    private void init() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
        setAdapter(new MonthViewPagerAdapter());
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar calendar = new Calendar();
                calendar.setYear((position + mDelegate.getMinYearMonth() - 1) / 12 + mDelegate.getMinYear());
                calendar.setMonth((position + mDelegate.getMinYearMonth() - 1) % 12 + 1);
                calendar.setDay(1);
                calendar.setCurrentMonth(calendar.getYear() == mDelegate.getCurrentDay().getYear() &&
                        calendar.getMonth() == mDelegate.getCurrentDay().getMonth());
                calendar.setLunar(LunarCalendar.getLunarText(calendar));


                if (mParentLayout == null || getVisibility() == INVISIBLE || mWeekPager.getVisibility() == VISIBLE) {
                    if (mDelegate.mDateChangeListener != null) {
                        mDelegate.mDateChangeListener.onDateChange(calendar);
                    }
                    return;
                }

                if (!calendar.isCurrentMonth()) {
                    mDelegate.mSelectedCalendar = calendar;
                } else {
                    mDelegate.mSelectedCalendar = mDelegate.createCurrentDate();
                }
                if (mDelegate.mDateChangeListener != null) {
                    mDelegate.mDateChangeListener.onDateChange(mDelegate.mSelectedCalendar);
                }

                if (mDelegate.mDateSelectedListener != null) {
                    mDelegate.mDateSelectedListener.onDateSelected(mDelegate.mSelectedCalendar, false);
                }

                MonthView view = (MonthView) findViewWithTag(position);
                if (view != null) {
                    int index = view.getSelectedIndex(mDelegate.mSelectedCalendar);
                    view.mCurrentItem = index;
                    if (index >= 0) {
                        mParentLayout.setSelectPosition(index);
                    }
                    view.invalidate();
                }
                mWeekPager.updateSelected(mDelegate.mSelectedCalendar);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    void notifyDataSetChanged() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
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

        int y = calendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendar.getMonth() - mDelegate.getMinYearMonth();
        setCurrentItem(position);

        MonthView view = (MonthView) findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.setSelectPosition(view.getSelectedIndex(mDelegate.mSelectedCalendar));
            }
        }
        if (mParentLayout != null) {
            int i = Util.getWeekFromDayInMonth(calendar);
            mParentLayout.setSelectWeek(i);
        }


        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onDateSelected(calendar);
        }

        if (mDelegate.mDateSelectedListener != null) {
            mDelegate.mDateSelectedListener.onDateSelected(calendar, false);
        }
        if (mDelegate.mDateChangeListener != null) {
            mDelegate.mDateChangeListener.onDateChange(calendar);
        }

        updateSelected();
    }

    /**
     * 滚动到当前日期
     */
    void scrollToCurrent() {
        int position = 12 * (mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear()) +
                mDelegate.getCurrentDay().getMonth() - mDelegate.getMinYearMonth();
        setCurrentItem(position);
        MonthView view = (MonthView) findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.getCurrentDay());
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.setSelectPosition(view.getSelectedIndex(mDelegate.getCurrentDay()));
            }
        }
    }


    /**
     * 更新选择效果
     */
    void updateSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            MonthView view = (MonthView) getChildAt(i);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }

    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            MonthView view = (MonthView) getChildAt(i);
            view.update();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getCardHeight(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 日历卡月份Adapter
     */
    private class MonthViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mMonthCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int year = (position + mDelegate.getMinYearMonth() - 1) / 12 + mDelegate.getMinYear();
            int month = (position + mDelegate.getMinYearMonth() - 1) % 12 + 1;
            MonthView view;
            if (TextUtils.isEmpty(mDelegate.getCalendarCardViewClass())) {
                view = new DefaultMonthView(getContext());
            } else {
                try {
                    Class cls = Class.forName(mDelegate.getCalendarCardViewClass());
                    @SuppressWarnings("unchecked")
                    Constructor constructor = cls.getConstructor(Context.class);
                    view = (MonthView) constructor.newInstance(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            view.mParentLayout = mParentLayout;

            view.setup(mDelegate);
            view.setTag(position);
            view.setCurrentDate(year, month);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取日历卡高度
     *
     * @return 获取日历卡高度
     */
    private int getCardHeight() {
        return 6 * mDelegate.getCalendarItemHeight();
    }

}
