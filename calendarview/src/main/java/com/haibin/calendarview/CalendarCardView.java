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
import java.util.Date;
import java.util.List;

/**
 * 第二版本，废弃RecyclerView
 * Created by huanghaibin on 2017/6/8.
 */
@SuppressWarnings("unused")
public class CalendarCardView extends View implements View.OnClickListener {

    static int ITEM_HEIGHT = 46;

    static final int STYLE_FILL = 1;
    static final int STYLE_STROKE = 2;

    Paint mCurMonthTextPaint = new Paint(); //当前月份日期的笔
    Paint mOtherMonthTextPaint = new Paint(); //其它月份日期颜色

    Paint mLunarTextPaint = new Paint(); //农历文本颜色
    Paint mSchemePaint = new Paint(); // 标记的日期背景颜色

    Paint mSchemeTextPaint = new Paint();//标记的文本颜色

    Paint mCurDayTextPaint = new Paint(); //当前日期背景颜色

    Paint mSelectedPaint = new Paint(); //被选择的日期背景色

    CalendarView.OnDateSelectedListener mDateSelectedListener;
    CalendarView.OnInnerDateSelectedListener mInnerListener;
    CalendarView.OnDateChangeListener mListener;

    Calendar mCurrent;
    private int mYear;
    private int mMonth;
    CalendarLayout mParentLayout;
    private List<Calendar> mItems;
    List<Calendar> mSchemes;

    private int mLineCount;
    private int mDiff;//当前月的第一天为星期几，星期天为0，实际就是当前月份的偏移量
    static int mItemHeight; //每一项的高度
    float mTextBaseLine; //Text的基线
    private boolean isShowAll;

    private float mX, mY;
    private boolean isClick = true;
    boolean isShowLunar;
    static final int TEXT_SIZE = 14;

    private int mPaddingLeft, mPaddingRight;

    int mCurrentItem = -1;
    private int mTextSchemeColor;
    private int mTextCurMonthColor;
    private int mTextSelectedColor;
    private int mSchemeColor;
    private int mLunarTextColor;

    public CalendarCardView(Context context) {
        this(context, null);
    }

    public CalendarCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

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

        mLunarTextPaint.setAntiAlias(true);
        mLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeTextPaint.setAntiAlias(true);
        mSchemeTextPaint.setStyle(Paint.Style.FILL);
        mSchemeTextPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeTextPaint.setColor(0xffed5353);
        mSchemeTextPaint.setFakeBoldText(true);
        mSchemeTextPaint.setTextSize(Util.dipToPx(context, TEXT_SIZE));

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setStyle(Paint.Style.STROKE);
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

        mItemHeight = Util.dipToPx(context, CalendarCardView.ITEM_HEIGHT);

        Paint.FontMetrics metrics = mCurMonthTextPaint.getFontMetrics();
        mTextBaseLine = mItemHeight / 2 - metrics.descent + (metrics.bottom - metrics.top) / 2;
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isClick) {
            Calendar calendar = getIndex();
            if (calendar != null && calendar.isCurrentMonth()) {
                if (mInnerListener != null) {
                    mInnerListener.onDateSelected(calendar);
                }
                if (mParentLayout != null)
                    mParentLayout.setSelectPosition(mItems.indexOf(calendar));
                if (mDateSelectedListener != null) {
                    mDateSelectedListener.onDateSelected(calendar);
                }
                if (mListener != null) {
                    mListener.onDateChange(calendar);
                }
                invalidate();
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
                float mDX;
                float mDY;
                if (isClick) {
                    mDX = event.getX() - mX;
                    mDY = event.getY() - mY;
                    isClick = Math.abs(mDY) <= 50;

                }
                break;
            case MotionEvent.ACTION_UP:
                mX = event.getX();
                mY = event.getY();
                mDX = 0;
                mDY = 0;
                break;
        }
        return super.onTouchEvent(event);
    }

    private Calendar getIndex() {
        int width = (getWidth() - mPaddingLeft - mPaddingRight) / 7;
        int indexX = (int) mX / width;
        int indexY = (int) mY / mItemHeight;
        mCurrentItem = indexY * 7 + indexX;// 选择项
        if (mCurrentItem >= mDiff && mCurrentItem < mItems.size())
            return mItems.get(mCurrentItem);
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLineCount == 0)
            return;
        int width = (getWidth() - mPaddingLeft - mPaddingRight) / 7;
        int height = mItemHeight * mLineCount;
        int d = 0;
        int radius = isShowLunar ? Math.min(width, mItemHeight) / 7 * 3 :
                Math.min(width, mItemHeight) / 5 * 2;
        for (int i = 0; i < mLineCount; i++) {
            for (int j = 0; j < 7; j++) {
                Calendar calendar = mItems.get(d);
                if (mSchemes != null && mSchemes.contains(calendar)) {
                    //标记的日子
                    Calendar scheme = mSchemes.get(mSchemes.indexOf(calendar));
                    int x = j * width + width / 2 + mPaddingLeft;
                    int y = i * mItemHeight + mItemHeight / 2;

                    if (d == mCurrentItem) {//绘制选择效果
                        onDrawSelected(canvas, x, y, radius, true);
                    } else {
                        onDrawScheme(canvas, scheme, x, y, radius);
                    }

                    onDrawText(canvas, calendar, j * width + width / 2 + mPaddingLeft,
                            mTextBaseLine + i * mItemHeight, i * mItemHeight, true);

                } else {
                    int x = j * width + width / 2 + mPaddingLeft;
                    int y = i * mItemHeight + mItemHeight / 2;
                    if (d == mCurrentItem) {//绘制选择效果
                        onDrawSelected(canvas, x, y, radius, false);
                    } else {
                        mCurMonthTextPaint.setColor(mTextCurMonthColor);
                        mLunarTextPaint.setColor(calendar.isCurrentMonth() ? mLunarTextColor : mOtherMonthTextPaint.getColor());
                    }

                    onDrawText(canvas, calendar, j * width + width / 2 + mPaddingLeft,
                            mTextBaseLine + i * mItemHeight, i * mItemHeight, false);
                }
                ++d;
            }
        }
    }

    /**
     * 绘制选中的日期
     *
     * @param canvas    canvas
     * @param cx        cx
     * @param cy        cy
     * @param radius    radius
     * @param hasScheme hasScheme 非标记的日期
     */
    void onDrawSelected(Canvas canvas, int cx, int cy, int radius, boolean hasScheme) {
        canvas.drawCircle(cx, cy, radius, mSelectedPaint);
        mLunarTextPaint.setColor(mTextSelectedColor);
        if (hasScheme) {
            mSchemeTextPaint.setColor(mTextSelectedColor);
        } else {
            mCurMonthTextPaint.setColor(mTextSelectedColor);
        }
    }

    /**
     * 绘制标记的日期
     *
     * @param canvas   canvas
     * @param calendar calendar
     * @param cx       cx
     * @param cy       cy
     * @param radius   radius
     */
    void onDrawScheme(Canvas canvas, Calendar calendar, int cx, int cy, int radius) {
        mSchemePaint.setColor(calendar.getSchemeColor() != 0 ? calendar.getSchemeColor() : mSchemeColor);
        canvas.drawCircle(cx, cy, radius, mSchemePaint);
        mSchemeTextPaint.setColor(mTextSchemeColor);
        mLunarTextPaint.setColor(calendar.isCurrentMonth() ? mLunarTextColor : mOtherMonthTextPaint.getColor());
    }

    /**
     * 绘制文本
     *
     * @param canvas    canvas
     * @param calendar  calendar
     * @param x         x坐标
     * @param y         y坐标 baseline
     * @param h         view的top
     * @param hasScheme 是否是标记的日期
     */
    void onDrawText(Canvas canvas, Calendar calendar, float x, float y, int h, boolean hasScheme) {
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    x,
                    y,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), x, y,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
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
        mCurrent = new Calendar();
        Date d = new Date();
        mCurrent.setYear(Util.getDate("yyyy", d));
        mCurrent.setMonth(Util.getDate("MM", d));
        mCurrent.setDay(Util.getDate("dd", d));
        initCalendar();
    }

    @SuppressLint("WrongConstant")
    private void initCalendar() {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(mYear, mMonth - 1, 1);
        int firstDayOfWeek = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0
        mDiff = firstDayOfWeek;
        //mCurrentItem = mDiff;
        int mDaysCount = Util.getMonthDaysCount(mYear, mMonth);
        date.set(mYear, mMonth - 1, mDaysCount);
        int mLastCount = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;

        int nextMonthDaysOffset = 6 - mLastCount;//下个月的日偏移天数

        int preYear, preMonth;
        int nextYear, nextMonth;

        int size = 42;

        int preMonthDaysCount;
        if (mMonth == 1) {//如果是1月
            preYear = mYear - 1;
            preMonth = 12;
            nextYear = mYear;
            nextMonth = mMonth + 1;
            preMonthDaysCount = firstDayOfWeek == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
        } else if (mMonth == 12) {//如果是12月
            preYear = mYear;
            preMonth = mMonth - 1;
            nextYear = mYear + 1;
            nextMonth = 1;
            preMonthDaysCount = firstDayOfWeek == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
        } else {//平常
            preYear = mYear;
            preMonth = mMonth - 1;
            nextYear = mYear;
            nextMonth = mMonth + 1;
            preMonthDaysCount = firstDayOfWeek == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
        }
        int nextDay = 1;
        if (mItems == null)
            mItems = new ArrayList<>();
        mItems.clear();
        for (int i = 0; i < size; i++) {
            Calendar calendarDate = new Calendar();
            if (i < firstDayOfWeek) {
                calendarDate.setYear(preYear);
                calendarDate.setMonth(preMonth);
                calendarDate.setDay(preMonthDaysCount - firstDayOfWeek + i + 1);
            } else if (i >= mDaysCount + firstDayOfWeek) {
                calendarDate.setYear(nextYear);
                calendarDate.setMonth(nextMonth);
                calendarDate.setDay(nextDay);
                ++nextDay;
            } else {
                calendarDate.setYear(mYear);
                calendarDate.setMonth(mMonth);
                calendarDate.setCurrentMonth(true);
                calendarDate.setDay(i - firstDayOfWeek + 1);
            }
            if (calendarDate.equals(mCurrent)) {
                calendarDate.setCurrentDay(true);
                mCurrentItem = i;
            }
            calendarDate.setLunar(LunarCalendar.getLunarText(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDay()));
            mItems.add(calendarDate);
        }
        mLineCount = mItems.size() / 7;
        if (mSchemes != null) {
            for (Calendar a : mItems) {
                for (Calendar d : mSchemes) {
                    if (d.equals(a)) {
                        a.setScheme(d.getScheme());
                    }
                }
            }
        }
        invalidate();
    }

    void update() {
        if (mSchemes != null) {
            for (Calendar a : mItems) {
                a.setScheme("");
                for (Calendar d : mSchemes) {
                    if (d.equals(a)) {
                        a.setScheme(d.getScheme());
                    }
                }
            }
            invalidate();
        }
    }

    /**
     * 设置文本的颜色
     *
     * @param curDayTextColor     今天的日期文本颜色
     * @param curMonthTextColor   当前月份的日期颜色
     * @param otherMonthTextColor 其它月份的日期颜色
     * @param lunarTextColor      农历字体颜色
     */
    void setTextColor(int curDayTextColor,
                      int curMonthTextColor,
                      int otherMonthTextColor,
                      int lunarTextColor) {
        mTextCurMonthColor = curMonthTextColor;
        mLunarTextColor = lunarTextColor;
        mCurDayTextPaint.setColor(curDayTextColor);
        mCurMonthTextPaint.setColor(curMonthTextColor);
        mOtherMonthTextPaint.setColor(otherMonthTextColor);
        mLunarTextPaint.setColor(lunarTextColor);
    }


    /**
     * 设置事务标记
     *
     * @param style           标记的style CalendarCardView.STYLE_FILL or CalendarCardView.STYLE_STROKE
     * @param schemeColor     标记的颜色
     * @param schemeTextColor 标记的文本颜色
     */
    void setSchemeColor(int style, int schemeColor, int schemeTextColor) {
        if (style == STYLE_STROKE) {
            mSchemePaint.setStyle(Paint.Style.STROKE);
        } else {
            mSchemePaint.setStyle(Paint.Style.FILL);
        }
        this.mSchemeColor = schemeColor;
        this.mSchemePaint.setColor(schemeColor);
        this.mTextSchemeColor = schemeTextColor;
        this.mSchemeTextPaint.setColor(schemeTextColor);
    }

    /**
     * 设置标记的style
     *
     * @param style 选中的style CalendarCardView.STYLE_FILL or CalendarCardView.STYLE_STROKE
     */
    void setSelectColor(int style, int selectedColor, int selectedTextColor) {
        if (style == STYLE_STROKE) {
            mSelectedPaint.setStyle(Paint.Style.STROKE);
        } else {
            mSelectedPaint.setStyle(Paint.Style.FILL);
        }
        this.mSelectedPaint.setColor(selectedColor);
        this.mTextSelectedColor = selectedTextColor;
        this.mTextSelectedColor = selectedTextColor;
    }

    /**
     * 设置字体大小
     *
     * @param calendarTextSize 日期大小
     * @param lunarTextSize    农历大小
     */
    void setDayTextSize(float calendarTextSize, float lunarTextSize) {
        mCurMonthTextPaint.setTextSize(Util.dipToPx(getContext(), calendarTextSize));
        mOtherMonthTextPaint.setTextSize(mCurMonthTextPaint.getTextSize());
        mCurDayTextPaint.setTextSize(mCurMonthTextPaint.getTextSize());
        mSchemeTextPaint.setTextSize(mCurMonthTextPaint.getTextSize());
        mLunarTextPaint.setTextSize(Util.dipToPx(getContext(), lunarTextSize));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLineCount != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Util.dipToPx(getContext(), ITEM_HEIGHT * mLineCount), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
