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
import android.graphics.Canvas;
import android.view.View;

/**
 * 周视图，因为日历UI采用热插拔实现，所以这里必须继承实现，达到UI一致即可
 * Created by huanghaibin on 2017/11/21.
 */

@SuppressWarnings("deprecation")
public abstract class WeekView extends BaseView {

    public WeekView(Context context) {
        super(context);
    }

    /**
     * 绘制日历文本
     *
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (mItems.size() == 0)
            return;
        mItemWidth = (getWidth() - 2 * mDelegate.getCalendarPadding()) / 7;
        onPreviewHook();

        for (int i = 0; i < 7; i++) {
            int x = i * mItemWidth + mDelegate.getCalendarPadding();
            onLoopStart(x);
            Calendar calendar = mItems.get(i);
            boolean isSelected = i == mCurrentItem;
            boolean hasScheme = calendar.hasScheme();
            if (hasScheme) {
                boolean isDrawSelected = false;//是否继续绘制选中的onDrawScheme
                if (isSelected) {
                    isDrawSelected = onDrawSelected(canvas, calendar, x, true);
                }
                if (isDrawSelected || !isSelected) {
                    //将画笔设置为标记颜色
                    mSchemePaint.setColor(calendar.getSchemeColor() != 0 ? calendar.getSchemeColor() : mDelegate.getSchemeThemeColor());
                    onDrawScheme(canvas, calendar, x);
                }
            } else {
                if (isSelected) {
                    onDrawSelected(canvas, calendar, x, false);
                }
            }
            onDrawText(canvas, calendar, x, hasScheme, isSelected);
        }
    }

    @Override
    public void onClick(View v) {
        if (!isClick) {
            return;
        }
        Calendar calendar = getIndex();
        if (calendar == null) {
            return;
        }
        if (onCalendarIntercept(calendar)) {
            mDelegate.mCalendarInterceptListener.onCalendarInterceptClick(calendar, true);
            return;
        }
        if (!isInRange(calendar)) {
            if (mDelegate.mCalendarSelectListener != null) {
                mDelegate.mCalendarSelectListener.onCalendarOutOfRange(calendar);
            }
            return;
        }

        mCurrentItem = mItems.indexOf(calendar);

        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendar, true);
        }
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }

        if (mDelegate.mDateSelectedListener != null) {
            mDelegate.mDateSelectedListener.onDateSelected(calendar, true);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, true);
        }

        invalidate();
    }


    @Override
    public boolean onLongClick(View v) {
        if (mDelegate.mDateLongClickListener == null &&
                mDelegate.mCalendarLongClickListener == null)
            return false;
        if (!isClick) {
            return false;
        }
        Calendar calendar = getIndex();
        if (calendar == null) {
            return false;
        }
        if (onCalendarIntercept(calendar)) {
            mDelegate.mCalendarInterceptListener.onCalendarInterceptClick(calendar, true);
            return true;
        }
        boolean isCalendarInRange = isInRange(calendar);

        if (!isCalendarInRange) {
            if (mDelegate.mCalendarLongClickListener != null) {
                mDelegate.mCalendarLongClickListener.onCalendarLongClickOutOfRange(calendar);
            }
            return true;
        }

        if (mDelegate.isPreventLongPressedSelected()) {//如果启用拦截长按事件不选择日期
            if (mDelegate.mDateLongClickListener != null) {
                mDelegate.mDateLongClickListener.onDateLongClick(calendar);
            }
            if (mDelegate.mCalendarLongClickListener != null) {
                mDelegate.mCalendarLongClickListener.onCalendarLongClick(calendar);
            }
            return true;
        }


        mCurrentItem = mItems.indexOf(calendar);

        mDelegate.mIndexCalendar = mDelegate.mSelectedCalendar;

        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendar, true);
        }
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }

        if (mDelegate.mDateSelectedListener != null) {
            mDelegate.mDateSelectedListener.onDateSelected(calendar, true);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, true);
        }

        if (mDelegate.mDateLongClickListener != null) {
            mDelegate.mDateLongClickListener.onDateLongClick(calendar);
        }
        if (mDelegate.mCalendarLongClickListener != null) {
            mDelegate.mCalendarLongClickListener.onCalendarLongClick(calendar);
        }

        invalidate();
        return true;
    }

    /**
     * 周视图切换点击默认位置
     *
     * @param calendar calendar
     * @param isNotice isNotice
     */
    void performClickCalendar(Calendar calendar, boolean isNotice) {
        if (mParentLayout == null || mDelegate.mInnerListener == null || mItems == null || mItems.size() == 0) {
            return;
        }

        int week = CalendarUtil.getWeekViewIndexFromCalendar(calendar, mDelegate.getWeekStart());
        if (mItems.contains(mDelegate.getCurrentDay())) {
            week = CalendarUtil.getWeekViewIndexFromCalendar(mDelegate.getCurrentDay(), mDelegate.getWeekStart());
        }

        int curIndex = week;

        Calendar currentCalendar = mItems.get(week);
        if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_SINGLE) {
            if (mItems.contains(mDelegate.mSelectedCalendar)) {
                currentCalendar = mDelegate.mSelectedCalendar;
            } else {
                mCurrentItem = -1;
            }
        }

        if (!isInRange(currentCalendar)) {
            curIndex = getEdgeIndex(isMinRangeEdge(currentCalendar));
            currentCalendar = mItems.get(curIndex);
        }


        currentCalendar.setCurrentDay(currentCalendar.equals(mDelegate.getCurrentDay()));
        mDelegate.mInnerListener.onWeekDateSelected(currentCalendar, false);
        int i = CalendarUtil.getWeekFromDayInMonth(currentCalendar, mDelegate.getWeekStart());
        mParentLayout.updateSelectWeek(i);

        if (mDelegate.mDateSelectedListener != null
                && isNotice
                && mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
            mDelegate.mDateSelectedListener.onDateSelected(currentCalendar, false);
        }

        if (mDelegate.mCalendarSelectListener != null
                && isNotice
                && mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(currentCalendar, false);
        }

        mParentLayout.updateContentViewTranslateY();
        if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
            mCurrentItem = curIndex;
        }
        mDelegate.mIndexCalendar = currentCalendar;
        invalidate();
    }

    /**
     * 是否是最小访问边界了
     *
     * @param calendar calendar
     * @return 是否是最小访问边界了
     */
    private boolean isMinRangeEdge(Calendar calendar) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(mDelegate.getMinYear(), mDelegate.getMinYearMonth() - 1, mDelegate.getMinYearDay());
        long minTime = c.getTimeInMillis();
        c.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime < minTime;
    }

    /**
     * 获得边界范围内下标
     *
     * @param isMinEdge isMinEdge
     * @return 获得边界范围内下标
     */
    private int getEdgeIndex(boolean isMinEdge) {
        for (int i = 0; i < mItems.size(); i++) {
            Calendar item = mItems.get(i);
            boolean isInRange = isInRange(item);
            if (isMinEdge && isInRange) {
                return i;
            } else if (!isMinEdge && !isInRange) {
                return i - 1;
            }
        }
        return isMinEdge ? 6 : 0;
    }


    /**
     * 获取点击的日历
     *
     * @return 获取点击的日历
     */
    protected Calendar getIndex() {

        int indexX = (int) mX / mItemWidth;
        if (indexX >= 7) {
            indexX = 6;
        }
        int indexY = (int) mY / mItemHeight;
        int position = indexY * 7 + indexX;// 选择项
        if (position >= 0 && position < mItems.size())
            return mItems.get(position);
        return null;
    }


    /**
     * 记录已经选择的日期
     *
     * @param calendar calendar
     */
    void setSelectedCalendar(Calendar calendar) {
        if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_SINGLE &&
                !calendar.equals(mDelegate.mSelectedCalendar)) {
            return;
        }
        mCurrentItem = mItems.indexOf(calendar);
    }


    /**
     * 初始化周视图控件
     *
     * @param calendar calendar
     */
    void setup(Calendar calendar) {

        mItems = CalendarUtil.initCalendarForWeekView(calendar, mDelegate, mDelegate.getWeekStart());
        if (mDelegate.getSchemeType() == CalendarViewDelegate.SCHEME_TYPE_LIST) {
            addSchemesFromList();
        } else {
            addSchemesFromMap();
        }
        invalidate();
    }

    /**
     * 更新显示模式
     */
    void updateShowMode() {
        invalidate();
    }

    /**
     * 更新周起始
     */
    void updateWeekStart() {

        int position = (int) getTag();
        Calendar calendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMinYearDay(),
                position + 1,
                mDelegate.getWeekStart());
        setSelectedCalendar(mDelegate.mSelectedCalendar);
        setup(calendar);
    }

    /**
     * 更新当选模式
     */
    void updateSingleSelect() {
        if (!mItems.contains(mDelegate.mSelectedCalendar)) {
            mCurrentItem = -1;
            invalidate();
        }
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY);
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
     */
    @SuppressWarnings("unused")
    protected void onLoopStart(int x) {
        // TODO: 2017/11/16
    }

    @Override
    protected void onDestroy() {

    }

    /**
     * 绘制选中的日期
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 是否绘制 onDrawScheme
     */
    protected abstract boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme);

    /**
     * 绘制标记的日期
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     */
    protected abstract void onDrawScheme(Canvas canvas, Calendar calendar, int x);


    /**
     * 绘制日历文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    protected abstract void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected);
}
