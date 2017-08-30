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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


import java.util.List;

/**
 * 月度选择View
 * Created by haibin
 * on 2017/3/6.
 */

public class MonthView extends View {
    private int mDiff;//第一天偏离周日多少天
    private int mCount;//总数
    private int mLastCount;//最后一行的天数
    private int mLine;//多少行
    private Paint mPaint = new Paint();
    private Paint mSchemePaint = new Paint();
    private List<Calendar> mSchemes;
    private Calendar mCalendar;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setTextAlign(Paint.Align.CENTER);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MonthView);
        mPaint.setTextSize(array.getDimensionPixelSize(R.styleable.MonthView_month_view_text_size, 12));
        mSchemePaint.setTextSize(array.getDimensionPixelSize(R.styleable.MonthView_month_view_text_size, 12));
        mPaint.setColor(array.getColor(R.styleable.MonthView_month_view_text_color, Color.BLACK));
        mSchemePaint.setColor(array.getColor(R.styleable.MonthView_month_view_remark_color, Color.RED));
        array.recycle();
        measureLine();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
     * @param mDiff 偏离天数
     * @param mCount 当月总天数
     * @param mYear 哪一年
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

    void setSchemes(List<Calendar> mSchemes) {
        this.mSchemes = mSchemes;
    }

    void setSchemeColor(int schemeColor) {
        if (schemeColor != 0)
            mSchemePaint.setColor(schemeColor);
        if(schemeColor == 0xff30393E)
            mSchemePaint.setColor(Color.RED);
    }

    private boolean isScheme(int day) {
        if (mSchemes == null || mSchemes.size() == 0)
            return false;
        mCalendar.setDay(day);
        return mSchemes.contains(mCalendar);
    }
}
