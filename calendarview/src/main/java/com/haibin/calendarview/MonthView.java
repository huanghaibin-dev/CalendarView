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
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;

/**
 * 月视图基础控件,请使用 MonthView替换，没有任何不同，只是规范命名
 * pleased using MonthView replace BaseCalendarCardView
 * Created by huanghaibin on 2017/11/15.
 */
public abstract class MonthView extends BaseView {

    /**
     * 月视图ViewPager
     */
    MonthViewPager mMonthViewPager;

    /**
     * 当前日历卡年份
     */
    private int mYear;

    /**
     * 当前日历卡月份
     */
    private int mMonth;


    /**
     * 日历的行数
     */
    private int mLineCount;

    /**
     * 日历高度
     */
    private int mHeight;


    /**
     * 下个月偏移的数量
     */
    private int mNextDiff;

    public MonthView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLineCount == 0)
            return;
        mItemWidth = getWidth() / 7;
        onPreviewHook();
        int count = mLineCount * 7;
        int d = 0;
        for (int i = 0; i < mLineCount; i++) {
            for (int j = 0; j < 7; j++) {
                Calendar calendar = mItems.get(d);
                if (mDelegate.getMonthViewShowMode() == CustomCalendarViewDelegate.MODE_ONLY_CURRENT_MONTH) {
                    if (d > mItems.size() - mNextDiff) {
                        return;
                    }
                    if (!calendar.isCurrentMonth()) {
                        ++d;
                        continue;
                    }
                } else if (mDelegate.getMonthViewShowMode() == CustomCalendarViewDelegate.MODE_FIT_MONTH) {
                    if (d >= count) {
                        return;
                    }
                }
                draw(canvas, calendar, i, j, d);
                ++d;
            }
        }


    }


    /**
     * 开始绘制
     *
     * @param canvas   canvas
     * @param calendar 对应日历
     * @param i        i
     * @param j        j
     * @param d        d
     */
    private void draw(Canvas canvas, Calendar calendar, int i, int j, int d) {
        int x = j * mItemWidth;
        int y = i * mItemHeight;
        onLoopStart(x, y);
        boolean isSelected = d == mCurrentItem;
        boolean hasScheme = calendar.hasScheme();

        if (hasScheme) {
            //标记的日子
            boolean isDrawSelected = false;//是否继续绘制选中的onDrawScheme
            if (isSelected) {
                isDrawSelected = onDrawSelected(canvas, calendar, x, y, true);
            }
            if (isDrawSelected || !isSelected) {
                //将画笔设置为标记颜色
                mSchemePaint.setColor(calendar.getSchemeColor() != 0 ? calendar.getSchemeColor() : mDelegate.getSchemeThemeColor());
                onDrawScheme(canvas, calendar, x, y);
            }
        } else {
            if (isSelected) {
                onDrawSelected(canvas, calendar, x, y, false);
            }
        }
        onDrawText(canvas, calendar, x, y, hasScheme, isSelected);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if (isClick) {
            Calendar calendar = getIndex();
            if (calendar != null) {

                if (mDelegate.getMonthViewShowMode() == CustomCalendarViewDelegate.MODE_ONLY_CURRENT_MONTH &&
                        !calendar.isCurrentMonth()) {
                    mCurrentItem = mItems.indexOf(mDelegate.mSelectedCalendar);
                    return;
                }

                if (!CalendarUtil.isCalendarInRange(calendar, mDelegate.getMinYear(),
                        mDelegate.getMinYearMonth(), mDelegate.getMaxYear(), mDelegate.getMaxYearMonth())) {
                    mCurrentItem = mItems.indexOf(mDelegate.mSelectedCalendar);
                    return;
                }

                if (!calendar.isCurrentMonth() && mMonthViewPager != null) {
                    int cur = mMonthViewPager.getCurrentItem();
                    int position = mCurrentItem < 7 ? cur - 1 : cur + 1;
                    mMonthViewPager.setCurrentItem(position);
                }

                if (mDelegate.mInnerListener != null) {
                    mDelegate.mInnerListener.onMonthDateSelected(calendar, true);
                }

                if (mParentLayout != null) {
                    if (calendar.isCurrentMonth()) {
                        mParentLayout.setSelectPosition(mItems.indexOf(calendar));
                    } else {
                        mParentLayout.setSelectWeek(CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart()));
                    }

                }

                if (mDelegate.mDateSelectedListener != null) {
                    mDelegate.mDateSelectedListener.onDateSelected(calendar, true);
                }
                invalidate();
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mDelegate.mDateLongClickListener == null)
            return false;
        if (isClick) {
            Calendar calendar = getIndex();
            if (calendar != null) {

                boolean isCalendarInRange = CalendarUtil.isCalendarInRange(calendar, mDelegate.getMinYear(),
                        mDelegate.getMinYearMonth(), mDelegate.getMaxYear(), mDelegate.getMaxYearMonth());

                if (mDelegate.isPreventLongPressedSelected() && isCalendarInRange) {
                    mDelegate.mDateLongClickListener.onDateLongClick(calendar);
                    mCurrentItem = mItems.indexOf(mDelegate.mSelectedCalendar);
                    return true;
                }

                if (mDelegate.getMonthViewShowMode() == CustomCalendarViewDelegate.MODE_ONLY_CURRENT_MONTH &&
                        !calendar.isCurrentMonth()) {
                    mCurrentItem = mItems.indexOf(mDelegate.mSelectedCalendar);
                    return false;
                }

                if (!isCalendarInRange) {
                    mCurrentItem = mItems.indexOf(mDelegate.mSelectedCalendar);
                    return false;
                }

                if (!calendar.isCurrentMonth() && mMonthViewPager != null) {
                    int cur = mMonthViewPager.getCurrentItem();
                    int position = mCurrentItem < 7 ? cur - 1 : cur + 1;
                    mMonthViewPager.setCurrentItem(position);
                }

                if (mDelegate.mInnerListener != null) {
                    mDelegate.mInnerListener.onMonthDateSelected(calendar, true);
                }

                if (mParentLayout != null) {
                    if (calendar.isCurrentMonth()) {
                        mParentLayout.setSelectPosition(mItems.indexOf(calendar));
                    } else {
                        mParentLayout.setSelectWeek(CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart()));
                    }

                }

                if (mDelegate.mDateSelectedListener != null) {
                    mDelegate.mDateSelectedListener.onDateSelected(calendar, true);
                }

                mDelegate.mDateLongClickListener.onDateLongClick(calendar);
                invalidate();
            }
        }
        return false;
    }

    private Calendar getIndex() {
        int width = getWidth() / 7;
        int indexX = (int) mX / width;
        if (indexX >= 7) {
            indexX = 6;
        }
        int indexY = (int) mY / mItemHeight;
        mCurrentItem = indexY * 7 + indexX;// 选择项
        if (mCurrentItem >= 0 && mCurrentItem < mItems.size())
            return mItems.get(mCurrentItem);
        return null;
    }


    /**
     * 记录已经选择的日期
     *
     * @param calendar calendar
     */
    void setSelectedCalendar(Calendar calendar) {
        mCurrentItem = mItems.indexOf(calendar);
    }

    /**
     * 初始化日期
     *
     * @param year  year
     * @param month month
     */
    void setCurrentDate(int year, int month) {
        mYear = year;
        mMonth = month;
        initCalendar();
        if (mDelegate.getMonthViewShowMode() == CustomCalendarViewDelegate.MODE_ALL_MONTH) {
            mHeight = mItemHeight * mLineCount;
        } else {
            mHeight = CalendarUtil.getMonthViewHeight(year, month, mItemHeight, mDelegate.getWeekStart());
        }

    }

    /**
     * 初始化日历
     */
    @SuppressLint("WrongConstant")
    private void initCalendar() {

        mNextDiff = CalendarUtil.getMonthEndDiff(mYear, mMonth, mDelegate.getWeekStart());
        int preDiff = CalendarUtil.getMonthViewStartDiff(mYear, mMonth, mDelegate.getWeekStart());
        int monthDayCount = CalendarUtil.getMonthDaysCount(mYear, mMonth);

        mItems = CalendarUtil.initCalendarForMonthView(mYear, mMonth, mDelegate.getCurrentDay(), mDelegate.getWeekStart());

        if (mItems.contains(mDelegate.getCurrentDay())) {
            mCurrentItem = mItems.indexOf(mDelegate.getCurrentDay());
        }

        if (mDelegate.getMonthViewShowMode() == CustomCalendarViewDelegate.MODE_ALL_MONTH) {
            mLineCount = 6;
        } else {
            mLineCount = (preDiff + monthDayCount + mNextDiff) / 7;
        }
        if (mDelegate.mSchemeDate != null) {
            for (Calendar a : mItems) {
                for (Calendar d : mDelegate.mSchemeDate) {
                    if (d.equals(a)) {
                        a.setScheme(TextUtils.isEmpty(d.getScheme()) ? mDelegate.getSchemeText() : d.getScheme());
                        a.setSchemeColor(d.getSchemeColor());
                        a.setSchemes(d.getSchemes());
                    }
                }
            }
        }
        invalidate();
    }

    @Override
    void updateCurrentDate() {
        if (mItems == null)
            return;

        if (mItems.contains(mDelegate.getCurrentDay())) {
            for (Calendar a : mItems) {//添加操作
                a.setCurrentDay(false);
            }
            int index = mItems.indexOf(mDelegate.getCurrentDay());
            mItems.get(index).setCurrentDay(true);
        }
        invalidate();
    }

    @Override
    void update() {
        if (mDelegate.mSchemeDate == null || mDelegate.mSchemeDate.size() == 0) {//清空操作
            for (Calendar a : mItems) {
                a.setScheme("");
                a.setSchemeColor(0);
                a.setSchemes(null);
            }
            invalidate();
            return;
        }
        for (Calendar a : mItems) {//添加操作
            if (mDelegate.mSchemeDate.contains(a)) {
                Calendar d = mDelegate.mSchemeDate.get(mDelegate.mSchemeDate.indexOf(a));
                a.setScheme(TextUtils.isEmpty(d.getScheme()) ? mDelegate.getSchemeText() : d.getScheme());
                a.setSchemeColor(d.getSchemeColor());
                a.setSchemes(d.getSchemes());
            } else {
                a.setScheme("");
                a.setSchemeColor(0);
                a.setSchemes(null);
            }
        }
        invalidate();
    }


    int getSelectedIndex(Calendar calendar) {
        return mItems.indexOf(calendar);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLineCount != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 开始绘制前的钩子，这里做一些初始化的操作，每次绘制只调用一次，性能高效
     * 没有需要可忽略不实现
     * 例如：
     * 1、需要绘制圆形标记事件背景，可以在这里计算半径
     * 2、绘制矩形选中效果，也可以在这里计算矩形宽和高
     */
    protected void onPreviewHook() {
        // TODO: 2017/11/16
    }


    /**
     * 循环绘制开始的回调，不需要可忽略
     * 绘制每个日历项的循环，用来计算baseLine、圆心坐标等都可以在这里实现
     *
     * @param x 日历Card x起点坐标
     * @param y 日历Card y起点坐标
     */
    protected void onLoopStart(int x, int y) {
        // TODO: 2017/11/16  
    }

    /**
     * 绘制选中的日期
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 是否绘制onDrawScheme，true or false
     */
    protected abstract boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme);

    /**
     * 绘制标记的日期,这里可以是背景色，标记色什么的
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    protected abstract void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y);


    /**
     * 绘制日历文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    protected abstract void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected);
}
