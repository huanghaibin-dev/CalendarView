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
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 周视图，因为日历UI采用热插拔实现，所以这里必须继承实现，达到UI一致即可
 * Created by huanghaibin on 2017/11/21.
 */

@SuppressWarnings("deprecation")
public abstract class WeekView extends View implements View.OnClickListener {

    private CustomCalendarViewDelegate mDelegate;
    /**
     * 当前月份日期的笔
     */
    protected Paint mCurMonthTextPaint = new Paint();

    /**
     * 其它月份日期颜色
     */
    protected Paint mOtherMonthTextPaint = new Paint();

    /**
     * 当前月份农历文本颜色
     */
    protected Paint mCurMonthLunarTextPaint = new Paint();

    /**
     * 其它月份农历文本颜色
     */
    protected Paint mOtherMonthLunarTextPaint = new Paint();

    /**
     * 标记的日期问爸爸颜色
     */
    protected Paint mSchemePaint = new Paint();

    /**
     * 标记的文本颜色
     */
    protected Paint mSchemeTextPaint = new Paint();

    /**
     * 当前日期文本颜色
     */
    protected Paint mCurDayTextPaint = new Paint();

    /**
     * 被选择的日期背景色
     */
    protected Paint mSelectedPaint = new Paint();


    /**
     * 日历布局，需要在日历下方放自己的布局
     */
    CalendarLayout mParentLayout;

    /**
     * 日历项
     */
    private List<Calendar> mItems;


    /**
     * 每一项的高度
     */
    protected int mItemHeight;

    /**
     * 每一项的宽度
     */
    protected int mItemWidth;

    /**
     * Text的基线
     */
    protected float mTextBaseLine;

    /**
     * 点击的x、y坐标
     */
    private float mX, mY;

    /**
     * 是否点击
     */
    private boolean isClick = true;

    /**
     * 字体大小
     */
    static final int TEXT_SIZE = 14;

    /**
     * 是否有padding
     */
    private int mPaddingLeft, mPaddingRight;

    /**
     * 当前点击项
     */
    int mCurrentItem = -1;

    /**
     * 标记的字体颜色
     */
    protected int mSchemeTextColor;

    /**
     * 标记的农历字体颜色
     */
    protected int mSchemeLunarTextColor;

    /**
     * 当前月份字体颜色
     */
    protected int mCurMonthTextColor;

    /**
     * 选中的字体颜色
     */
    protected int mSelectedTextColor;

    /**
     * 选中农历颜色
     */
    protected int mSelectedLunarTextColor;

    /**
     * 标记颜色
     */
    protected int mSchemeColor;

    /**
     * 当前月份农历颜色
     */
    protected int mCurMonthLunarTextColor;

    /**
     * 其它月份农历颜色
     */
    protected int mOtherMonthLunarTextColor;


    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    /**
     * 初始化配置
     *
     * @param context context
     */
    private void initPaint(Context context) {
        mCurMonthTextPaint.setAntiAlias(true);
        mCurMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurMonthTextPaint.setColor(0xFF111111);
        mCurMonthTextPaint.setFakeBoldText(true);
        mCurMonthTextPaint.setTextSize(Util.dipToPx(context, TEXT_SIZE));

        mOtherMonthTextPaint.setAntiAlias(true);
        mOtherMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthTextPaint.setFakeBoldText(true);
        mOtherMonthTextPaint.setTextSize(Util.dipToPx(context, TEXT_SIZE));

        mCurMonthLunarTextPaint.setAntiAlias(true);
        mCurMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mOtherMonthLunarTextPaint.setAntiAlias(true);
        mOtherMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeTextPaint.setAntiAlias(true);
        mSchemeTextPaint.setStyle(Paint.Style.FILL);
        mSchemeTextPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeTextPaint.setColor(0xffed5353);
        mSchemeTextPaint.setFakeBoldText(true);
        mSchemeTextPaint.setTextSize(Util.dipToPx(context, TEXT_SIZE));

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setStrokeWidth(2);
        mSchemePaint.setColor(0xffefefef);

        mCurDayTextPaint.setAntiAlias(true);
        mCurDayTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayTextPaint.setColor(Color.RED);
        mCurDayTextPaint.setFakeBoldText(true);
        mCurDayTextPaint.setTextSize(Util.dipToPx(context, TEXT_SIZE));

        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(2);

        mPaddingLeft = Util.dipToPx(context, 8);
        mPaddingRight = Util.dipToPx(context, 8);

        setOnClickListener(this);
    }

    void setup(CustomCalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        mCurMonthTextColor = delegate.getCurrentMonthTextColor();
        mCurMonthLunarTextColor = delegate.getCurrentMonthLunarTextColor();
        mCurDayTextPaint.setColor(delegate.getCurDayTextColor());
        mCurMonthTextPaint.setColor(delegate.getCurrentMonthTextColor());
        mOtherMonthTextPaint.setColor(delegate.getOtherMonthTextColor());
        mCurMonthLunarTextPaint.setColor(delegate.getCurrentMonthLunarTextColor());
        mOtherMonthLunarTextColor = delegate.getOtherMonthLunarTextColor();
        mOtherMonthLunarTextPaint.setColor(mOtherMonthLunarTextColor);


        this.mSchemeColor = delegate.getSchemeThemeColor();
        this.mSchemePaint.setColor(mSchemeColor);
        this.mSchemeTextColor = delegate.getSchemeTextColor();
        this.mSchemeTextPaint.setColor(mSchemeTextColor);
        this.mSchemeLunarTextColor = delegate.getSchemeLunarTextColor();


        mCurMonthTextPaint.setTextSize(delegate.getDayTextSize());
        mOtherMonthTextPaint.setTextSize(mCurMonthTextPaint.getTextSize());
        mCurDayTextPaint.setTextSize(mCurMonthTextPaint.getTextSize());
        mSchemeTextPaint.setTextSize(mCurMonthTextPaint.getTextSize());
        mCurMonthLunarTextPaint.setTextSize(delegate.getLunarTextSize());

        mSelectedPaint.setStyle(Paint.Style.FILL);
        this.mSelectedPaint.setColor(delegate.getSelectedThemeColor());
        this.mSelectedTextColor = delegate.getSelectedTextColor();
        this.mSelectedLunarTextColor = delegate.getSelectedLunarTextColor();
        setItemHeight(delegate.getCalendarItemHeight());
    }

    /**
     * 绘制日历文本
     *
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mItems.size() == 0)
            return;
        mItemWidth = (getWidth() - mPaddingLeft - mPaddingRight) / 7;
        onPreviewHook();

        for (int i = 0; i < 7; i++) {
            int x = i * mItemWidth + mPaddingLeft;
            onLoopStart(x);
            Calendar calendar = mItems.get(i);
            mCurMonthLunarTextPaint.setColor(mCurMonthLunarTextColor);
            mOtherMonthLunarTextPaint.setColor(mOtherMonthLunarTextColor);
            boolean isSelected = i == mCurrentItem;
            if (mDelegate.mSchemeDate != null && mDelegate.mSchemeDate.contains(calendar)) {
                //标记的日子
                Calendar scheme = mDelegate.mSchemeDate.get(mDelegate.mSchemeDate.indexOf(calendar));
                calendar.setScheme(scheme.getScheme());
                calendar.setSchemeColor(scheme.getSchemeColor());

                //if判断规范必须要else，避免错位
                if (isSelected) {
                    //将画笔设置为选中颜色
                    mSchemeTextPaint.setColor(mSelectedTextColor);
                    mCurMonthLunarTextPaint.setColor(mSelectedLunarTextColor);
                    onDrawSelected(canvas, calendar, x, true);
                } else {
                    //将画笔设置为标记颜色
                    mSchemePaint.setColor(calendar.getSchemeColor() != 0 ? calendar.getSchemeColor() : mSchemeColor);
                    mSchemeTextPaint.setColor(mSchemeTextColor);
                    mCurMonthLunarTextPaint.setColor(mSchemeLunarTextColor);
                    onDrawScheme(canvas, scheme, x);
                }
                onDrawText(canvas, calendar, x, true, isSelected);
            } else {
                mCurMonthTextPaint.setColor(mCurMonthTextColor);
                if (isSelected) {
                    //将画笔设置为选中颜色
                    mCurMonthTextPaint.setColor(mSelectedTextColor);
                    mCurMonthLunarTextPaint.setColor(mSelectedLunarTextColor);
                    onDrawSelected(canvas, calendar, x, false);
                }
                onDrawText(canvas, calendar, x, false, isSelected);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1)
            return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float mDY;
                if (isClick) {
                    mDY = event.getY() - mY;
                    isClick = Math.abs(mDY) <= 50;
                }
                break;
            case MotionEvent.ACTION_UP:
                mX = event.getX();
                mY = event.getY();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if (isClick) {
            Calendar calendar = getIndex();
            if (calendar != null) {
                if (!Util.isCalendarInRange(calendar, mDelegate.getMinYear(),
                        mDelegate.getMinYearMonth(), mDelegate.getMaxYear(), mDelegate.getMaxYearMonth())) {
                    mCurrentItem = mItems.indexOf(mDelegate.mSelectedCalendar);
                    return;
                }
                if (mDelegate.mInnerListener != null) {
                    mDelegate.mInnerListener.onWeekSelected(calendar);
                }
                if (mParentLayout != null) {
                    int i = Util.getWeekFromDayInMonth(calendar);
                    mParentLayout.setSelectWeek(i);
                }

                if (mDelegate.mDateChangeListener != null) {
                    mDelegate.mDateChangeListener.onDateChange(calendar);
                }

                if (mDelegate.mDateSelectedListener != null) {
                    mDelegate.mDateSelectedListener.onDateSelected(calendar, true);
                }

                invalidate();
            }
        }
    }

    /**
     * 周视图切换点击默认位置
     *
     * @param calendar calendar
     */
    void performClickCalendar(Calendar calendar, boolean isNotice) {
        if (mItems == null || mDelegate.mInnerListener == null || mParentLayout == null || mItems.size() == 0) {
            return;
        }

        int week = Util.getWeekFormCalendar(calendar);
        if (mItems.contains(mDelegate.getCurrentDay())) {
            week = Util.getWeekFormCalendar(mDelegate.getCurrentDay());
        }

        mCurrentItem = week;

        Calendar currentCalendar = mItems.get(week);

        if (!Util.isCalendarInRange(currentCalendar, mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(), mDelegate.getMaxYear(), mDelegate.getMaxYearMonth())) {
            mCurrentItem = getEdgeIndex(isLeftEdge(currentCalendar));
            currentCalendar = mItems.get(mCurrentItem);
        }

        currentCalendar.setCurrentDay(currentCalendar.equals(mDelegate.getCurrentDay()));
        mDelegate.mInnerListener.onWeekSelected(currentCalendar);

        int i = Util.getWeekFromDayInMonth(currentCalendar);
        mParentLayout.setSelectWeek(i);

        if (mDelegate.mDateChangeListener != null && isNotice) {
            mDelegate.mDateChangeListener.onDateChange(currentCalendar);
        }
        if (mDelegate.mDateSelectedListener != null && isNotice) {
            mDelegate.mDateSelectedListener.onDateSelected(currentCalendar, false);
        }
        invalidate();
    }

    private boolean isLeftEdge(Calendar calendar) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(mDelegate.getMinYear(), mDelegate.getMinYearMonth() - 1, 1);
        long minTime = c.getTimeInMillis();
        c.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime < minTime;
    }

    private int getEdgeIndex(boolean isMinEdge) {
        for (int i = 0; i < mItems.size(); i++) {
            Calendar item = mItems.get(i);
            if (isMinEdge && Util.isCalendarInRange(item, mDelegate.getMinYear(), mDelegate.getMinYearMonth(),
                    mDelegate.getMaxYear(), mDelegate.getMaxYearMonth())) {
                return i;
            } else if (!isMinEdge && !Util.isCalendarInRange(item, mDelegate.getMinYear(), mDelegate.getMinYearMonth(),
                    mDelegate.getMaxYear(), mDelegate.getMaxYearMonth())) {
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
    private Calendar getIndex() {
        int width = (getWidth() - mPaddingLeft - mPaddingRight) / 7;
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
     * 初始化周视图控件
     *
     * @param calendar calendar
     */
    void setup(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        int week = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//星期几,星期天 == 0，也就是前面偏差多少天
        int dayCount = Util.getMonthDaysCount(calendar.getYear(), calendar.getMonth());//获取某个月有多少天

        if (mItems == null) {
            mItems = new ArrayList<>();
        }

        int preDiff = 0, nextDiff = 0;
        int preMonthDaysCount = 0;
        int preYear = 0, preMonth = 0;
        int nextYear = 0, nextMonth = 0;

        if (calendar.getDay() - week <= 0) {//如果某月某天-星期<0，则说明前面需要上个月的补数
            date.set(calendar.getYear(), calendar.getMonth() - 1, 1);
            preDiff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0，补数量就是偏差量diff;
            if (calendar.getMonth() == 1) {//取上一年的12月份
                preMonthDaysCount = 31;
                preYear = calendar.getYear() - 1;
                preMonth = 12;
            } else {//否则取上一个月份天数
                preMonthDaysCount = Util.getMonthDaysCount(calendar.getYear(), calendar.getMonth() - 1);
                preYear = calendar.getYear();
                preMonth = calendar.getMonth() - 1;
            }
        } else if (calendar.getDay() + 6 - week > dayCount) {//往后偏移多少天，即当前月份的最后一天不是星期6，则需要往后取补数
            nextDiff = calendar.getDay() + 6 - week - dayCount;//往后偏移多少天，补差diff
            if (calendar.getMonth() == 12) {
                nextMonth = 1;
                nextYear = calendar.getYear() + 1;
            } else {
                nextMonth = calendar.getMonth() + 1;
                nextYear = calendar.getYear();
            }
        }
        int nextDay = 1;
        int day = calendar.getDay() - week;
        for (int i = 0; i < 7; i++) {
            Calendar calendarDate = new Calendar();
            if (i < preDiff) {//如果前面有补数
                calendarDate.setYear(preYear);
                calendarDate.setMonth(preMonth);
                calendarDate.setDay(preMonthDaysCount - preDiff + i + 1);
                day += 1;
            } else if (nextDiff > 0 && i >= (7 - nextDiff)) {
                calendarDate.setYear(nextYear);
                calendarDate.setMonth(nextMonth);
                calendarDate.setDay(nextDay);
                nextDay += 1;
            } else {
                calendarDate.setYear(calendar.getYear());
                calendarDate.setMonth(calendar.getMonth());
                calendarDate.setDay(day);
                day += 1;
            }
            calendarDate.setWeekend(Util.isWeekend(calendarDate));
            calendarDate.setWeek(Util.getWeekFormCalendar(calendarDate));
            calendarDate.setCurrentDay(calendarDate.equals(mDelegate.getCurrentDay()));
            calendarDate.setLunar(LunarCalendar.getLunarText(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDay()));
            mItems.add(calendarDate);
        }
        invalidate();
    }


    /**
     * 更新界面
     */
    void update() {
        if (mDelegate.mSchemeDate != null) {
            for (Calendar a : mItems) {
                a.setScheme("");
                for (Calendar d : mDelegate.mSchemeDate) {
                    if (d.equals(a)) {
                        a.setScheme(d.getScheme());
                    }
                }
            }
            invalidate();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置高度
     *
     * @param itemHeight itemHeight
     */
    private void setItemHeight(int itemHeight) {
        this.mItemHeight = itemHeight;
        Paint.FontMetrics metrics = mCurMonthTextPaint.getFontMetrics();
        mTextBaseLine = mItemHeight / 2 - metrics.descent + (metrics.bottom - metrics.top) / 2;
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

    /**
     * 绘制选中的日期
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     */
    protected abstract void onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme);

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
