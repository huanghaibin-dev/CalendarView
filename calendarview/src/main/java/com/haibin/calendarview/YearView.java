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
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 年份视图
 * Created by haibin on 2017/3/6.
 */

public class YearView extends View {

    private int mDiff;//第一天偏离周日多少天
    private int mCount;//总数
    private int mLastCount;//最后一行的天数
    private int mLine;//多少行
    private Paint mPaint = new Paint();
    private Paint mSchemePaint = new Paint();
    private Calendar mCalendar;
    private int mMinHeight;//最小高度
    private CalendarViewDelegate mDelegate;

    public YearView(Context context) {
        this(context, null);
    }

    public YearView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setTextAlign(Paint.Align.CENTER);
        measureLine();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int pLeft = getPaddingLeft();
        int w = (width - getPaddingLeft() - getPaddingRight()) / 7;
        int h = (height - getPaddingTop() - getPaddingBottom()) / 6;
        int d = 0;
        for (int i = 0; i < mLine; i++) {
            if (i == 0) {//第一行
                for (int j = 0; j < (7 - mDiff); j++) {
                    ++d;
                    canvas.drawText(String.valueOf(j + 1), mDiff * w + j * w + pLeft + w / 2, h, isScheme(d) ? mSchemePaint : mPaint);
                }
            } else if (i == mLine - 1 && mLastCount != 0) {
                int first = mCount - mLastCount + 1;
                for (int j = 0; j < mLastCount; j++) {
                    ++d;
                    canvas.drawText(String.valueOf(first), j * w + pLeft + w / 2, (i + 1) * h, isScheme(d) ? mSchemePaint : mPaint);
                    ++first;
                }
            } else {
                int first = i * 7 - mDiff + 1;
                for (int j = 0; j < 7; j++) {
                    ++d;
                    canvas.drawText(String.valueOf(first), j * w + pLeft + w / 2, (i + 1) * h, isScheme(d) ? mSchemePaint : mPaint);
                    ++first;
                }
            }
        }
    }

    /**
     * 计算行数
     */
    private void measureLine() {
        int offset = mCount - (7 - mDiff);
        mLine = 1 + (offset % 7 == 0 ? 0 : 1) + offset / 7;
        mLastCount = offset % 7;
    }

    /**
     * 初始化月份卡
     *
     * @param mDiff  偏离天数
     * @param mCount 当月总天数
     * @param mYear  哪一年
     * @param mMonth 哪一月
     */
    void init(int mDiff, int mCount, int mYear, int mMonth) {
        this.mDiff = mDiff;
        this.mCount = mCount;
        mCalendar = new Calendar();
        mCalendar.setYear(mYear);
        mCalendar.setMonth(mMonth);
        measureLine();
        invalidate();
    }

    /**
     * 初始化
     *
     * @param delegate delegate
     */
    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        mSchemePaint.setColor(delegate.getYearViewSchemeTextColor());
        mSchemePaint.setTextSize(delegate.getYearViewDayTextSize());
        mPaint.setTextSize(delegate.getYearViewDayTextSize());
        mPaint.setColor(delegate.getYearViewDayTextColor());
        Rect rect = new Rect();
        mPaint.getTextBounds("1", 0, 1, rect);
        int textHeight = rect.height();
        mMinHeight = 12 * textHeight;
    }


    /**
     * 设置标记颜色
     *
     * @param schemeColor schemeColor
     */
    void setSchemeColor(int schemeColor) {
        if (schemeColor != 0)
            mSchemePaint.setColor(schemeColor);
        if (schemeColor == 0xff30393E)
            mSchemePaint.setColor(Color.RED);
    }

    /**
     * 设置字体
     *
     * @param textSize  textSize
     * @param textColor textColor
     */
    void setTextStyle(int textSize, int textColor) {
        mSchemePaint.setTextSize(textSize);
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
    }

    /**
     * 测量高度
     *
     * @param height height
     */
    void measureHeight(int height) {
        if (height <= mMinHeight) {
            getLayoutParams().height = mMinHeight;
        } else {
            getLayoutParams().height = height;
        }
    }

    /**
     * 是否包含事件
     *
     * @param day day
     * @return return
     */
    private boolean isScheme(int day) {
        if (mDelegate.getSchemeType() == CalendarViewDelegate.SCHEME_TYPE_LIST) {
            if (mDelegate.mSchemeDate == null || mDelegate.mSchemeDate.size() == 0) {
                return false;
            }
            mCalendar.setDay(day);
            return mDelegate.mSchemeDate.contains(mCalendar);
        }

        if (mDelegate.mSchemeDatesMap == null || mDelegate.mSchemeDatesMap.size() == 0) {
            return false;
        }
        mCalendar.setDay(day);
        return mDelegate.mSchemeDatesMap.containsKey(mCalendar.toString());
    }
}
