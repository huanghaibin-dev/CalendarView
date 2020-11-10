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
 * 范围选择周视图
 * Created by huanghaibin on 2018/9/11.
 */
public abstract class RangeWeekView extends BaseWeekView {

    public RangeWeekView(Context context) {
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
        mItemWidth = (getWidth() -
                mDelegate.getCalendarPaddingLeft() -
                mDelegate.getCalendarPaddingRight()) / 7;
        onPreviewHook();

        for (int i = 0; i < 7; i++) {
            int x = i * mItemWidth + mDelegate.getCalendarPaddingLeft();
            onLoopStart(x);
            Calendar calendar = mItems.get(i);
            boolean isSelected = isCalendarSelected(calendar);
            boolean isPreSelected = isSelectPreCalendar(calendar);
            boolean isNextSelected = isSelectNextCalendar(calendar);
            boolean hasScheme = calendar.hasScheme();
            if (hasScheme) {
                boolean isDrawSelected = false;//是否继续绘制选中的onDrawScheme
                if (isSelected) {
                    isDrawSelected = onDrawSelected(canvas, calendar, x, true, isPreSelected, isNextSelected);
                }
                if (isDrawSelected || !isSelected) {
                    //将画笔设置为标记颜色
                    mSchemePaint.setColor(calendar.getSchemeColor() != 0 ? calendar.getSchemeColor() : mDelegate.getSchemeThemeColor());
                    onDrawScheme(canvas, calendar, x, isSelected);
                }
            } else {
                if (isSelected) {
                    onDrawSelected(canvas, calendar, x, false, isPreSelected, isNextSelected);
                }
            }
            onDrawText(canvas, calendar, x, hasScheme, isSelected);
        }
    }


    /**
     * 日历是否被选中
     *
     * @param calendar calendar
     * @return 日历是否被选中
     */
    protected boolean isCalendarSelected(Calendar calendar) {
        if (mDelegate.mSelectedStartRangeCalendar == null) {
            return false;
        }
        if (onCalendarIntercept(calendar)) {
            return false;
        }
        if (mDelegate.mSelectedEndRangeCalendar == null) {
            return calendar.compareTo(mDelegate.mSelectedStartRangeCalendar) == 0;
        }
        return calendar.compareTo(mDelegate.mSelectedStartRangeCalendar) >= 0 &&
                calendar.compareTo(mDelegate.mSelectedEndRangeCalendar) <= 0;
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
            if (mDelegate.mCalendarRangeSelectListener != null) {
                mDelegate.mCalendarRangeSelectListener.onCalendarSelectOutOfRange(calendar);
            }
            return;
        }

        //优先判断各种直接return的情况，减少代码深度
        if (mDelegate.mSelectedStartRangeCalendar != null && mDelegate.mSelectedEndRangeCalendar == null) {
            int minDiffer = CalendarUtil.differ(calendar, mDelegate.mSelectedStartRangeCalendar);
            if (minDiffer >= 0 && mDelegate.getMinSelectRange() != -1 && mDelegate.getMinSelectRange() > minDiffer + 1) {
                if (mDelegate.mCalendarRangeSelectListener != null) {
                    mDelegate.mCalendarRangeSelectListener.onSelectOutOfRange(calendar, true);
                }
                return;
            } else if (mDelegate.getMaxSelectRange() != -1 && mDelegate.getMaxSelectRange() <
                    CalendarUtil.differ(calendar, mDelegate.mSelectedStartRangeCalendar) + 1) {
                if (mDelegate.mCalendarRangeSelectListener != null) {
                    mDelegate.mCalendarRangeSelectListener.onSelectOutOfRange(calendar, false);
                }
                return;
            }
        }

        if (mDelegate.mSelectedStartRangeCalendar == null || mDelegate.mSelectedEndRangeCalendar != null) {
            mDelegate.mSelectedStartRangeCalendar = calendar;
            mDelegate.mSelectedEndRangeCalendar = null;
        } else {
            int compare = calendar.compareTo(mDelegate.mSelectedStartRangeCalendar);
            if (mDelegate.getMinSelectRange() == -1 && compare <= 0) {
                mDelegate.mSelectedStartRangeCalendar = calendar;
                mDelegate.mSelectedEndRangeCalendar = null;
            } else if (compare < 0) {
                mDelegate.mSelectedStartRangeCalendar = calendar;
                mDelegate.mSelectedEndRangeCalendar = null;
            } else if (compare == 0 &&
                    mDelegate.getMinSelectRange() == 1) {
                mDelegate.mSelectedEndRangeCalendar = calendar;
            } else {
                mDelegate.mSelectedEndRangeCalendar = calendar;
            }

        }

        mCurrentItem = mItems.indexOf(calendar);

        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendar, true);
        }
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }

        if (mDelegate.mCalendarRangeSelectListener != null) {
            mDelegate.mCalendarRangeSelectListener.onCalendarRangeSelect(calendar,
                    mDelegate.mSelectedEndRangeCalendar != null);
        }

        invalidate();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    /**
     * 上一个日期是否选中
     *
     * @param calendar 当前日期
     * @return 上一个日期是否选中
     */
    protected final boolean isSelectPreCalendar(Calendar calendar) {
        Calendar preCalendar = CalendarUtil.getPreCalendar(calendar);
        mDelegate.updateCalendarScheme(preCalendar);
        return mDelegate.mSelectedStartRangeCalendar != null &&
                isCalendarSelected(preCalendar);
    }

    /**
     * 下一个日期是否选中
     *
     * @param calendar 当前日期
     * @return 下一个日期是否选中
     */
    protected final boolean isSelectNextCalendar(Calendar calendar) {
        Calendar nextCalendar = CalendarUtil.getNextCalendar(calendar);
        mDelegate.updateCalendarScheme(nextCalendar);
        return mDelegate.mSelectedStartRangeCalendar != null &&
                isCalendarSelected(nextCalendar);
    }

    /**
     * 绘制选中的日期
     *
     * @param canvas         canvas
     * @param calendar       日历日历calendar
     * @param x              日历Card x起点坐标
     * @param hasScheme      hasScheme 非标记的日期
     * @param isSelectedPre  上一个日期是否选中
     * @param isSelectedNext 下一个日期是否选中
     * @return 是否绘制 onDrawScheme
     */
    protected abstract boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme,
                                              boolean isSelectedPre, boolean isSelectedNext);

    /**
     * 绘制标记的日期
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param isSelected 是否选中
     */
    protected abstract void onDrawScheme(Canvas canvas, Calendar calendar, int x, boolean isSelected);


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
