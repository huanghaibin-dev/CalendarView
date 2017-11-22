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
 * 我们知道一年有365或366天，都不是7的整数倍，所以很容易确定每年都有53个星期，是个定值，
 * 所以Adapter的count数量就是: (maxYear-minYear)*53
 * WeekViewPager需要和CalendarView关联:
 * 1、WeekViewPager切换时，如果月份改变，CalendarView需要一起改变；如果月份没有改变，则改变CalendarView的选择，从而改变滚动
 * CalendarView每个月份都是6周，因此CalendarView切换时，WeekViewPager的position应该至少切换
 */
@SuppressWarnings("unused")
public class WeekViewPager extends ViewPager {

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
        init(context);
    }


    private void init(Context context) {
        setAdapter(new WeekViewPagerAdapter());
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int year = position / 53 + mMinYear;
                int month = (position % 53) % 12 + 1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setup() {

    }

    public void change(Calendar calendar, int line) {

    }


    void updateSelected(Calendar calendar) {
        this.mSelectedCalendar = calendar;
        setCurrentItem(53 * (mSelectedCalendar.getYear() - mMinYear) + Util.getWeekFromCalendarInYear(mSelectedCalendar) - 1);
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
            return (mMaxYear - mMinYear) * 53;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int year = position / 53 + mMinYear;
            int week = position % 53 + 1;
            Calendar calendar = Util.getFirstCalendarFormWeekInYear(year, week);
            Log.e("instantiateItem", "  --  " + year + "  --   " + calendar.getMonth() + "  --  " + calendar.getDay() + "   --    " + week);
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
