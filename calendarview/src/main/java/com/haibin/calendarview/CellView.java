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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class CellView extends View {

    private int mDay = 20;
    private String mLunar;
    private String mScheme;
    private Paint mDayPaint = new Paint();
    private Paint mLunarPaint = new Paint();
    private Paint mSchemePaint = new Paint();
    private Paint mCirclePaint = new Paint();
    private Paint mSelectedPaint = new Paint();
    private int mRadius;
    private int mCirclePadding;
    private boolean isSelectedDay;

    public CellView(Context context) {
        this(context, null);
    }

    public CellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mDayPaint.setAntiAlias(true);
        mDayPaint.setColor(Color.BLACK);
        mDayPaint.setFakeBoldText(true);
        mDayPaint.setTextAlign(Paint.Align.CENTER);

        mLunarPaint.setAntiAlias(true);
        mLunarPaint.setColor(Color.GRAY);
        mLunarPaint.setTextAlign(Paint.Align.CENTER);

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setColor(Color.WHITE);
        mSchemePaint.setFakeBoldText(true);
        mSchemePaint.setTextAlign(Paint.Align.CENTER);

        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setColor(0x50CFCFCF);
        mSelectedPaint.setStyle(Paint.Style.FILL);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CellView);
        mDayPaint.setTextSize(array.getDimensionPixelSize(R.styleable.CellView_cell_day_text_size, 18));
        mLunarPaint.setTextSize(array.getDimensionPixelSize(R.styleable.CellView_cell_lunar_text_size, 12));
        mRadius = (int) array.getDimension(R.styleable.CellView_cell_scheme_radius, 8);
        mSchemePaint.setTextSize(array.getDimensionPixelSize(R.styleable.CellView_cell_scheme_text_size, 6));
        mCirclePadding = array.getDimensionPixelSize(R.styleable.CellView_cell_circle_padding, 4);
        mCirclePaint.setColor(array.getColor(R.styleable.CellView_cell_circle_color, 0xff16BB7F));
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (isSelectedDay) {
            canvas.drawCircle(width / 2, height / 2, width / 2, mSelectedPaint);
        }

        int w = (width - getPaddingLeft() - getPaddingRight());
        int h = (height - getPaddingTop() - getPaddingBottom()) / 4;
        canvas.drawText(String.valueOf(mDay), w / 2, 2 * h + getPaddingTop(), mDayPaint);
        canvas.drawText(mLunar, w / 2, 4 * h + getPaddingTop(), mLunarPaint);
        if (!TextUtils.isEmpty(mScheme)) {
            canvas.drawCircle(w / 2 + mCirclePadding + mDayPaint.getTextSize(), getPaddingTop() + h, mRadius, mCirclePaint);
            canvas.drawText(mScheme, w / 2 + mCirclePadding + mDayPaint.getTextSize(), getPaddingTop() + mRadius / 2 + h, mSchemePaint);
        }
    }

    /**
     * 初始化日历
     *
     * @param day    天
     * @param lunar  农历
     * @param scheme 事件标记
     */
    void init(int day, String lunar, String scheme) {
        this.mDay = day;
        this.mLunar = lunar;
        this.mScheme = scheme;
    }

    void setTextColor(int textColor) {
        mDayPaint.setColor(textColor);
        mLunarPaint.setColor(textColor);
    }

    /**
     * 设置选中颜色
     *
     * @param color color
     */
    void setSelectedColor(int color) {
        mSelectedPaint.setColor(color);
    }

    /**
     * 设置是否被选中
     *
     * @param selectedDay selectedDay
     */
    void setSelectedDay(boolean selectedDay) {
        isSelectedDay = selectedDay;
    }

    void setCircleColor(int circleColor) {
        mCirclePaint.setColor(circleColor);
        invalidate();
    }
}
