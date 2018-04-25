package com.haibin.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 默认高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class DefaultMonthView extends MonthView {

    private Paint mTextPaint = new Paint();
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;

    public DefaultMonthView(Context context) {
        super(context);

        mTextPaint.setTextSize(CalendarUtil.dipToPx(context, 8));
        mTextPaint.setColor(0xffFFFFFF);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setColor(0xffed5353);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = CalendarUtil.dipToPx(getContext(), 7);
        mPadding = CalendarUtil.dipToPx(getContext(), 4);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + CalendarUtil.dipToPx(getContext(), 1);

    }

    /**
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());

        canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, y + mPadding + mRadio, mRadio, mSchemeBasicPaint);

        canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mRadio, y + mPadding + mSchemeBaseLine, mTextPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :mSchemeLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }
    }
}
