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
 * 这是一个自适应高度的View
 */
@SuppressWarnings("deprecation")
public class MonthViewPager extends ViewPager {
    private CustomCalendarViewDelegate mDelegate;
    CalendarLayout mParentLayout;
    WeekViewPager mWeekPager;

    public MonthViewPager(Context context) {
        this(context, null);
    }

    public MonthViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        addOnPageChangeListener(new OnPageChangeListener() {
//            /**
//             * 这里现在暂时弃用，后续不显示其它月份可以实现
//             * @param position position如果小于当前position，则代表往前一页滑动，否则为下一页
//             * @param positionOffset 滑动比例
//             * @param positionOffsetPixels 滑动像素
//             */
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////                if (getTranslationY() != 0) {
////                    return;
////                }
////                int height;
////                if (position < getCurrentItem()) {//右滑-1
////                    height = (int) ((mPreViewHeight)
////                            * (1 - positionOffset) +
////                            mCurrentViewHeight
////                                    * positionOffset);
////                } else {//左滑+！
////                    height = (int) ((mCurrentViewHeight)
////                            * (1 - positionOffset) +
////                            (mNextViewHeight)
////                                    * positionOffset);
////                }
////                ViewGroup.LayoutParams params = getLayoutParams();
////                params.height = height;
////                setLayoutParams(params);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                int year = position / 12 + mDelegate.getMinYear();
////                int month = position % 12 + 1;
////                mCurrentViewHeight = getCardHeight();
////                if (month == 1) {
////                    mNextViewHeight = getCardHeight();
////                    mPreViewHeight = getCardHeight();
////                } else {
////                    mPreViewHeight = getCardHeight();
////                    if (month == 12) {
////                        mNextViewHeight = getCardHeight();
////                    } else {
////                        mNextViewHeight = getCardHeight();
////                    }
////                }
////                if (isFirstInit) {
////                    ViewGroup.LayoutParams params = getLayoutParams();
////                    params.height = mCurrentViewHeight;
////                    setLayoutParams(params);
////                    isFirstInit = false;
////                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    void setup(CustomCalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        init();
    }

    private void init() {
        setAdapter(new MonthViewPagerAdapter());

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar calendar = new Calendar();
                calendar.setYear(position / 12 + mDelegate.getMinYear());
                calendar.setMonth(position % 12 + 1);
                calendar.setDay(1);
                calendar.setCurrentMonth(calendar.getYear() == mDelegate.getCurrentDay().getYear() &&
                        calendar.getMonth() == mDelegate.getCurrentDay().getMonth());
                calendar.setLunar(LunarCalendar.numToChineseDay(LunarCalendar.solarToLunar(calendar.getYear(), calendar.getMonth(), 1)[2]));


                if (mParentLayout == null || getVisibility() == INVISIBLE || mWeekPager.getVisibility() == VISIBLE) {
                    if (mDelegate.mDateChangeListener != null) {
                        mDelegate.mDateChangeListener.onDateChange(calendar);
                    }
                    return;
                }
                if (!calendar.isCurrentMonth()) {
                    mDelegate.mSelectedCalendar = calendar;
                } else {
                    mDelegate.mSelectedCalendar = mDelegate.getCurrentDay();
                }
                if (mDelegate.mDateChangeListener != null) {
                    mDelegate.mDateChangeListener.onDateChange(mDelegate.mSelectedCalendar);
                }
                if (mDelegate.mDateSelectedListener != null) {
                    mDelegate.mDateSelectedListener.onDateSelected(mDelegate.mSelectedCalendar);
                }

                BaseCalendarCardView view = (BaseCalendarCardView) findViewWithTag(position);
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


    /**
     * 滚动到当前日期
     */
    void scrollToCurrent() {
        int position = 12 * (mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear()) + mDelegate.getCurrentDay().getMonth() - 1;
        setCurrentItem(position);
        BaseCalendarCardView view = (BaseCalendarCardView) findViewWithTag(position);
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
            BaseCalendarCardView view = (BaseCalendarCardView) getChildAt(i);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }

    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseCalendarCardView view = (BaseCalendarCardView) getChildAt(i);
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
            return 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear());
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int year = position / 12 + mDelegate.getMinYear();
            int month = position % 12 + 1;
            BaseCalendarCardView view;
            if (TextUtils.isEmpty(mDelegate.getCalendarCardViewClass())) {
                view = new DefaultCalendarCardView(getContext());
            } else {
                try {
                    Class cls = Class.forName(mDelegate.getCalendarCardViewClass());
                    @SuppressWarnings("unchecked")
                    Constructor constructor = cls.getConstructor(Context.class);
                    view = (BaseCalendarCardView) constructor.newInstance(getContext());
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
