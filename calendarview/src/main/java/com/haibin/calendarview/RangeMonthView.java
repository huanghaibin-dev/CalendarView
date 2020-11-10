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
 * 范围选择月视图
 * Created by huanghaibin on 2018/9/11.
 */
public abstract class RangeMonthView extends BaseMonthView {

    public RangeMonthView(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mLineCount == 0)
            return;
        mItemWidth = (getWidth() -
                mDelegate.getCalendarPaddingLeft() -
                mDelegate.getCalendarPaddingRight()) / 7;
        onPreviewHook();
        int count = mLineCount * 7;
        int d = 0;
        for (int i = 0; i < mLineCount; i++) {
            for (int j = 0; j < 7; j++) {
                Calendar calendar = mItems.get(d);
                if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ONLY_CURRENT_MONTH) {
                    if (d > mItems.size() - mNextDiff) {
                        return;
                    }
                    if (!calendar.isCurrentMonth()) {
                        ++d;
                        continue;
                    }
                } else if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_FIT_MONTH) {
                    if (d >= count) {
                        return;
                    }
                }
                draw(canvas, calendar, i, j);
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
     */
    private void draw(Canvas canvas, Calendar calendar, int i, int j) {
        int x = j * mItemWidth + mDelegate.getCalendarPaddingLeft();
        int y = i * mItemHeight;
        onLoopStart(x, y);
        boolean isSelected = isCalendarSelected(calendar);
        boolean hasScheme = calendar.hasScheme();
        boolean isPreSelected = isSelectPreCalendar(calendar);
        boolean isNextSelected = isSelectNextCalendar(calendar);

        if (hasScheme) {
            //标记的日子
            boolean isDrawSelected = false;//是否继续绘制选中的onDrawScheme
            if (isSelected) {
                isDrawSelected = onDrawSelected(canvas, calendar, x, y, true, isPreSelected, isNextSelected);
            }
            if (isDrawSelected || !isSelected) {
                //将画笔设置为标记颜色
                mSchemePaint.setColor(calendar.getSchemeColor() != 0 ? calendar.getSchemeColor() : mDelegate.getSchemeThemeColor());
                onDrawScheme(canvas, calendar, x, y, true);
            }
        } else {
            if (isSelected) {
                onDrawSelected(canvas, calendar, x, y, false, isPreSelected, isNextSelected);
            }
        }
        onDrawText(canvas, calendar, x, y, hasScheme, isSelected);
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

        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ONLY_CURRENT_MONTH
                && !calendar.isCurrentMonth()) {
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
                mParentLayout.updateSelectPosition(mItems.indexOf(calendar));
            } else {
                mParentLayout.updateSelectWeek(CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart()));
            }
        }
        if (mDelegate.mCalendarRangeSelectListener != null) {
            mDelegate.mCalendarRangeSelectListener.onCalendarRangeSelect(calendar,
                    mDelegate.mSelectedEndRangeCalendar != null);
        }
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
     * @param y              日历Card y起点坐标
     * @param hasScheme      hasScheme 非标记的日期
     * @param isSelectedPre  上一个日期是否选中
     * @param isSelectedNext 下一个日期是否选中
     * @return 是否继续绘制onDrawScheme，true or false
     */
    protected abstract boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme,
                                              boolean isSelectedPre, boolean isSelectedNext);

    /**
     * 绘制标记的日期,这里可以是背景色，标记色什么的
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param isSelected 是否选中
     */
    protected abstract void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected);


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
