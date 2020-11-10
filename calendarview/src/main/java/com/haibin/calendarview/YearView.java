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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * 年视图
 * Created by huanghaibin on 2018/10/9.
 */
@SuppressWarnings("unused")
public abstract class YearView extends View {

    CalendarViewDelegate mDelegate;

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
     * 当前月份农历文本颜色
     */
    protected Paint mSelectedLunarTextPaint = new Paint();

    /**
     * 其它月份农历文本颜色
     */
    protected Paint mOtherMonthLunarTextPaint = new Paint();

    /**
     * 其它月份农历文本颜色
     */
    protected Paint mSchemeLunarTextPaint = new Paint();

    /**
     * 标记的日期背景颜色画笔
     */
    protected Paint mSchemePaint = new Paint();

    /**
     * 被选择的日期背景色
     */
    protected Paint mSelectedPaint = new Paint();

    /**
     * 标记的文本画笔
     */
    protected Paint mSchemeTextPaint = new Paint();

    /**
     * 选中的文本画笔
     */
    protected Paint mSelectTextPaint = new Paint();

    /**
     * 当前日期文本颜色画笔
     */
    protected Paint mCurDayTextPaint = new Paint();

    /**
     * 当前日期文本颜色画笔
     */
    protected Paint mCurDayLunarTextPaint = new Paint();

    /**
     * 月份画笔
     */
    protected Paint mMonthTextPaint = new Paint();

    /**
     * 周栏画笔
     */
    protected Paint mWeekTextPaint = new Paint();

    /**
     * 日历项
     */
    List<Calendar> mItems;

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
     * Text的基线
     */
    protected float mMonthTextBaseLine;

    /**
     * Text的基线
     */
    protected float mWeekTextBaseLine;

    /**
     * 当前日历卡年份
     */
    protected int mYear;

    /**
     * 当前日历卡月份
     */
    protected int mMonth;

    /**
     * 下个月偏移的数量
     */
    protected int mNextDiff;

    /**
     * 周起始
     */
    protected int mWeekStart;

    /**
     * 日历的行数
     */
    protected int mLineCount;

    public YearView(Context context) {
        this(context, null);
    }

    public YearView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }


    /**
     * 初始化配置
     */
    private void initPaint() {
        mCurMonthTextPaint.setAntiAlias(true);
        mCurMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurMonthTextPaint.setColor(0xFF111111);
        mCurMonthTextPaint.setFakeBoldText(true);

        mOtherMonthTextPaint.setAntiAlias(true);
        mOtherMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthTextPaint.setFakeBoldText(true);

        mCurMonthLunarTextPaint.setAntiAlias(true);
        mCurMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSelectedLunarTextPaint.setAntiAlias(true);
        mSelectedLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mOtherMonthLunarTextPaint.setAntiAlias(true);
        mOtherMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mMonthTextPaint.setAntiAlias(true);
        mMonthTextPaint.setFakeBoldText(true);

        mWeekTextPaint.setAntiAlias(true);
        mWeekTextPaint.setFakeBoldText(true);
        mWeekTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeLunarTextPaint.setAntiAlias(true);
        mSchemeLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeTextPaint.setAntiAlias(true);
        mSchemeTextPaint.setStyle(Paint.Style.FILL);
        mSchemeTextPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeTextPaint.setColor(0xffed5353);
        mSchemeTextPaint.setFakeBoldText(true);

        mSelectTextPaint.setAntiAlias(true);
        mSelectTextPaint.setStyle(Paint.Style.FILL);
        mSelectTextPaint.setTextAlign(Paint.Align.CENTER);
        mSelectTextPaint.setColor(0xffed5353);
        mSelectTextPaint.setFakeBoldText(true);

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setStrokeWidth(2);
        mSchemePaint.setColor(0xffefefef);

        mCurDayTextPaint.setAntiAlias(true);
        mCurDayTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayTextPaint.setColor(Color.RED);
        mCurDayTextPaint.setFakeBoldText(true);

        mCurDayLunarTextPaint.setAntiAlias(true);
        mCurDayLunarTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayLunarTextPaint.setColor(Color.RED);
        mCurDayLunarTextPaint.setFakeBoldText(true);

        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(2);
    }

    /**
     * 设置
     *
     * @param delegate delegate
     */
    final void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        updateStyle();
    }

    final void updateStyle(){
        if(mDelegate == null){
            return;
        }
        this.mCurMonthTextPaint.setTextSize(mDelegate.getYearViewDayTextSize());
        this.mSchemeTextPaint.setTextSize(mDelegate.getYearViewDayTextSize());
        this.mOtherMonthTextPaint.setTextSize(mDelegate.getYearViewDayTextSize());
        this.mCurDayTextPaint.setTextSize(mDelegate.getYearViewDayTextSize());
        this.mSelectTextPaint.setTextSize(mDelegate.getYearViewDayTextSize());

        this.mSchemeTextPaint.setColor(mDelegate.getYearViewSchemeTextColor());
        this.mCurMonthTextPaint.setColor(mDelegate.getYearViewDayTextColor());
        this.mOtherMonthTextPaint.setColor(mDelegate.getYearViewDayTextColor());
        this.mCurDayTextPaint.setColor(mDelegate.getYearViewCurDayTextColor());
        this.mSelectTextPaint.setColor(mDelegate.getYearViewSelectTextColor());
        this.mMonthTextPaint.setTextSize(mDelegate.getYearViewMonthTextSize());
        this.mMonthTextPaint.setColor(mDelegate.getYearViewMonthTextColor());
        this.mWeekTextPaint.setColor(mDelegate.getYearViewWeekTextColor());
        this.mWeekTextPaint.setTextSize(mDelegate.getYearViewWeekTextSize());
    }

    /**
     * 初始化年视图
     *
     * @param year  year
     * @param month month
     */
    final void init(int year, int month) {
        mYear = year;
        mMonth = month;
        mNextDiff = CalendarUtil.getMonthEndDiff(mYear, mMonth, mDelegate.getWeekStart());
        int preDiff = CalendarUtil.getMonthViewStartDiff(mYear, mMonth, mDelegate.getWeekStart());

        mItems = CalendarUtil.initCalendarForMonthView(mYear, mMonth, mDelegate.getCurrentDay(), mDelegate.getWeekStart());

        mLineCount = 6;
        addSchemesFromMap();

    }

    /**
     * 测量大小
     *
     * @param width  width
     * @param height height
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    final void measureSize(int width, int height) {

        Rect rect = new Rect();
        mCurMonthTextPaint.getTextBounds("1", 0, 1, rect);
        int textHeight = rect.height();
        int mMinHeight = 12 * textHeight + getMonthViewTop();

        int h = height >= mMinHeight ? height : mMinHeight;

        getLayoutParams().width = width;
        getLayoutParams().height = h;
        mItemHeight = (h - getMonthViewTop()) / 6;

        Paint.FontMetrics metrics = mCurMonthTextPaint.getFontMetrics();
        mTextBaseLine = mItemHeight / 2 - metrics.descent + (metrics.bottom - metrics.top) / 2;

        Paint.FontMetrics monthMetrics = mMonthTextPaint.getFontMetrics();
        mMonthTextBaseLine = mDelegate.getYearViewMonthHeight() / 2 - monthMetrics.descent +
                (monthMetrics.bottom - monthMetrics.top) / 2;

        Paint.FontMetrics weekMetrics = mWeekTextPaint.getFontMetrics();
        mWeekTextBaseLine = mDelegate.getYearViewWeekHeight() / 2 - weekMetrics.descent +
                (weekMetrics.bottom - weekMetrics.top) / 2;

        invalidate();
    }

    /**
     * 添加事件标记，来自Map
     */
    private void addSchemesFromMap() {
        if (mDelegate.mSchemeDatesMap == null || mDelegate.mSchemeDatesMap.size() == 0) {
            return;
        }
        for (Calendar a : mItems) {
            if (mDelegate.mSchemeDatesMap.containsKey(a.toString())) {
                Calendar d = mDelegate.mSchemeDatesMap.get(a.toString());
                if(d == null){
                    continue;
                }
                a.setScheme(TextUtils.isEmpty(d.getScheme()) ? mDelegate.getSchemeText() : d.getScheme());
                a.setSchemeColor(d.getSchemeColor());
                a.setSchemes(d.getSchemes());
            } else {
                a.setScheme("");
                a.setSchemeColor(0);
                a.setSchemes(null);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mItemWidth = (getWidth() -
                mDelegate.getYearViewMonthPaddingLeft() -
                mDelegate.getYearViewMonthPaddingRight()) / 7;
        onPreviewHook();
        onDrawMonth(canvas);
        onDrawWeek(canvas);
        onDrawMonthView(canvas);
    }

    /**
     * 绘制
     *
     * @param canvas canvas
     */
    private void onDrawMonth(Canvas canvas) {
        onDrawMonth(canvas,
                mYear, mMonth,
                mDelegate.getYearViewMonthPaddingLeft(),
                mDelegate.getYearViewMonthPaddingTop(),
                getWidth() - 2 * mDelegate.getYearViewMonthPaddingRight(),
                mDelegate.getYearViewMonthHeight() +
                        mDelegate.getYearViewMonthPaddingTop());
    }

    private int getMonthViewTop() {
        return mDelegate.getYearViewMonthPaddingTop() +
                mDelegate.getYearViewMonthHeight() +
                mDelegate.getYearViewMonthPaddingBottom() +
                mDelegate.getYearViewWeekHeight();
    }

    /**
     * 绘制
     *
     * @param canvas canvas
     */
    private void onDrawWeek(Canvas canvas) {
        if (mDelegate.getYearViewWeekHeight() <= 0) {
            return;
        }
        int week = mDelegate.getWeekStart();
        if (week > 0) {
            week -= 1;
        }
        int width = (getWidth() -
                mDelegate.getYearViewMonthPaddingLeft() -
                mDelegate.getYearViewMonthPaddingRight()) / 7;
        for (int i = 0; i < 7; i++) {
            onDrawWeek(canvas,
                    week,
                    mDelegate.getYearViewMonthPaddingLeft() + i * width,
                    mDelegate.getYearViewMonthHeight() +
                            mDelegate.getYearViewMonthPaddingTop() +
                            mDelegate.getYearViewMonthPaddingBottom(),
                    width,
                    mDelegate.getYearViewWeekHeight());
            week += 1;
            if (week >= 7) {
                week = 0;
            }

        }
    }

    /**
     * 绘制月份数据
     *
     * @param canvas canvas
     */
    private void onDrawMonthView(Canvas canvas) {

        int count = mLineCount * 7;
        int d = 0;
        for (int i = 0; i < mLineCount; i++) {
            for (int j = 0; j < 7; j++) {
                Calendar calendar = mItems.get(d);
                if (d > mItems.size() - mNextDiff) {
                    return;
                }
                if (!calendar.isCurrentMonth()) {
                    ++d;
                    continue;
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
        int x = j * mItemWidth + mDelegate.getYearViewMonthPaddingLeft();
        int y = i * mItemHeight + getMonthViewTop();

        boolean isSelected = calendar.equals(mDelegate.mSelectedCalendar);
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
     * 绘制月份
     *
     * @param canvas canvas
     * @param year   year
     * @param month  month
     * @param x      x
     * @param y      y
     * @param width  width
     * @param height height
     */
    protected abstract void onDrawMonth(Canvas canvas, int year, int month, int x, int y, int width, int height);


    /**
     * 绘制年视图的周栏
     *
     * @param canvas canvas
     * @param week   week
     * @param x      x
     * @param y      y
     * @param width  width
     * @param height height
     */
    protected abstract void onDrawWeek(Canvas canvas, int week, int x, int y, int width, int height);


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
