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
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * 基本的日历View，派生出MonthView 和 WeekView
 * Created by huanghaibin on 2018/1/23.
 */

public abstract class BaseView extends View implements View.OnClickListener, View.OnLongClickListener {

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
     * 日历布局，需要在日历下方放自己的布局
     */
    CalendarLayout mParentLayout;

    /**
     * 日历项
     */
    protected List<Calendar> mItems;

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
    protected float mX, mY;

    /**
     * 是否点击
     */
    boolean isClick = true;

    /**
     * 字体大小
     */
    static final int TEXT_SIZE = 14;

    /**
     * 当前点击项
     */
    int mCurrentItem = -1;

    /**
     * 周起始
     */
    int mWeekStartWidth;

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
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
        mCurMonthTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mOtherMonthTextPaint.setAntiAlias(true);
        mOtherMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthTextPaint.setFakeBoldText(true);
        mOtherMonthTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mCurMonthLunarTextPaint.setAntiAlias(true);
        mCurMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSelectedLunarTextPaint.setAntiAlias(true);
        mSelectedLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mOtherMonthLunarTextPaint.setAntiAlias(true);
        mOtherMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);


        mSchemeLunarTextPaint.setAntiAlias(true);
        mSchemeLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeTextPaint.setAntiAlias(true);
        mSchemeTextPaint.setStyle(Paint.Style.FILL);
        mSchemeTextPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeTextPaint.setColor(0xffed5353);
        mSchemeTextPaint.setFakeBoldText(true);
        mSchemeTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mSelectTextPaint.setAntiAlias(true);
        mSelectTextPaint.setStyle(Paint.Style.FILL);
        mSelectTextPaint.setTextAlign(Paint.Align.CENTER);
        mSelectTextPaint.setColor(0xffed5353);
        mSelectTextPaint.setFakeBoldText(true);
        mSelectTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setStrokeWidth(2);
        mSchemePaint.setColor(0xffefefef);

        mCurDayTextPaint.setAntiAlias(true);
        mCurDayTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayTextPaint.setColor(Color.RED);
        mCurDayTextPaint.setFakeBoldText(true);
        mCurDayTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mCurDayLunarTextPaint.setAntiAlias(true);
        mCurDayLunarTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayLunarTextPaint.setColor(Color.RED);
        mCurDayLunarTextPaint.setFakeBoldText(true);
        mCurDayLunarTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(2);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    /**
     * 初始化所有UI配置
     *
     * @param delegate delegate
     */
    final void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        mWeekStartWidth = mDelegate.getWeekStart();
        updateStyle();
        updateItemHeight();

        initPaint();
    }


    final void updateStyle() {
        if (mDelegate == null) {
            return;
        }
        this.mCurDayTextPaint.setColor(mDelegate.getCurDayTextColor());
        this.mCurDayLunarTextPaint.setColor(mDelegate.getCurDayLunarTextColor());
        this.mCurMonthTextPaint.setColor(mDelegate.getCurrentMonthTextColor());
        this.mOtherMonthTextPaint.setColor(mDelegate.getOtherMonthTextColor());
        this.mCurMonthLunarTextPaint.setColor(mDelegate.getCurrentMonthLunarTextColor());
        this.mSelectedLunarTextPaint.setColor(mDelegate.getSelectedLunarTextColor());
        this.mSelectTextPaint.setColor(mDelegate.getSelectedTextColor());
        this.mOtherMonthLunarTextPaint.setColor(mDelegate.getOtherMonthLunarTextColor());
        this.mSchemeLunarTextPaint.setColor(mDelegate.getSchemeLunarTextColor());
        this.mSchemePaint.setColor(mDelegate.getSchemeThemeColor());
        this.mSchemeTextPaint.setColor(mDelegate.getSchemeTextColor());
        this.mCurMonthTextPaint.setTextSize(mDelegate.getDayTextSize());
        this.mOtherMonthTextPaint.setTextSize(mDelegate.getDayTextSize());
        this.mCurDayTextPaint.setTextSize(mDelegate.getDayTextSize());
        this.mSchemeTextPaint.setTextSize(mDelegate.getDayTextSize());
        this.mSelectTextPaint.setTextSize(mDelegate.getDayTextSize());

        this.mCurMonthLunarTextPaint.setTextSize(mDelegate.getLunarTextSize());
        this.mSelectedLunarTextPaint.setTextSize(mDelegate.getLunarTextSize());
        this.mCurDayLunarTextPaint.setTextSize(mDelegate.getLunarTextSize());
        this.mOtherMonthLunarTextPaint.setTextSize(mDelegate.getLunarTextSize());
        this.mSchemeLunarTextPaint.setTextSize(mDelegate.getLunarTextSize());

        this.mSelectedPaint.setStyle(Paint.Style.FILL);
        this.mSelectedPaint.setColor(mDelegate.getSelectedThemeColor());
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    void updateItemHeight() {
        this.mItemHeight = mDelegate.getCalendarItemHeight();
        Paint.FontMetrics metrics = mCurMonthTextPaint.getFontMetrics();
        mTextBaseLine = mItemHeight / 2 - metrics.descent + (metrics.bottom - metrics.top) / 2;
    }


    /**
     * 移除事件
     */
    final void removeSchemes() {
        for (Calendar a : mItems) {
            a.setScheme("");
            a.setSchemeColor(0);
            a.setSchemes(null);
        }
    }

    /**
     * 添加事件标记，来自Map
     */
    final void addSchemesFromMap() {
        if (mDelegate.mSchemeDatesMap == null || mDelegate.mSchemeDatesMap.size() == 0) {
            return;
        }
        for (Calendar a : mItems) {
            if (mDelegate.mSchemeDatesMap.containsKey(a.toString())) {
                Calendar d = mDelegate.mSchemeDatesMap.get(a.toString());
                if (d == null) {
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
     * 是否是选中的
     *
     * @param calendar calendar
     * @return true or false
     */
    protected boolean isSelected(Calendar calendar) {
        return mItems != null && mItems.indexOf(calendar) == mCurrentItem;
    }

    /**
     * 更新事件
     */
    final void update() {
        if (mDelegate.mSchemeDatesMap == null || mDelegate.mSchemeDatesMap.size() == 0) {//清空操作
            removeSchemes();
            invalidate();
            return;
        }
        addSchemesFromMap();
        invalidate();
    }


    /**
     * 是否拦截日期，此设置续设置mCalendarInterceptListener
     *
     * @param calendar calendar
     * @return 是否拦截日期
     */
    protected final boolean onCalendarIntercept(Calendar calendar) {
        return mDelegate.mCalendarInterceptListener != null &&
                mDelegate.mCalendarInterceptListener.onCalendarIntercept(calendar);
    }

    /**
     * 是否在日期范围内
     *
     * @param calendar calendar
     * @return 是否在日期范围内
     */
    protected final boolean isInRange(Calendar calendar) {
        return mDelegate != null && CalendarUtil.isCalendarInRange(calendar, mDelegate);
    }

    /**
     * 跟新当前日期
     */
    abstract void updateCurrentDate();

    /**
     * 销毁
     */
    protected abstract void onDestroy();

    protected int getWeekStartWith() {
        return mDelegate != null ? mDelegate.getWeekStart() : CalendarViewDelegate.WEEK_START_WITH_SUN;
    }


    protected int getCalendarPaddingLeft() {
        return mDelegate != null ? mDelegate.getCalendarPaddingLeft() : 0;
    }


    protected int getCalendarPaddingRight() {
        return mDelegate != null ? mDelegate.getCalendarPaddingRight() : 0;
    }


    /**
     * 初始化画笔相关
     */
    protected void initPaint() {

    }
}
