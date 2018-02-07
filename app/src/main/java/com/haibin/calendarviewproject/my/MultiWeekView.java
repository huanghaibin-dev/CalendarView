package com.haibin.calendarviewproject.my;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;
import com.haibin.calendarviewproject.base.type.SchemeType;

import java.util.List;

/**
 * 多层次视图
 * Created by wenhua on 2017/11/29.
 * https://github.com/peterforme 感谢 @peterforme 提供PR
 */

public class MultiWeekView extends WeekView {
    private Paint mTextPaint = new Paint();
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;

    public MultiWeekView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setColor(0xffed5353);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 0);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

    }

    /**
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x + mPadding, mPadding, x + mItemWidth - mPadding, mItemHeight - mPadding, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        List<Calendar.Scheme> schemes = calendar.getSchemes();
        for (Calendar.Scheme scheme : schemes) {
            if (scheme.getType() == SchemeType.TRIGLE.ordinal()) {
                Log.e("pwh", "画三角形");
                Path path = new Path();
                path.moveTo(x + mItemWidth - 4 * mRadio, 0);
                path.lineTo(x + mItemWidth, 0 + 4 * mRadio);
                path.lineTo(x + mItemWidth, 0);
                path.moveTo(x + mItemWidth - 4 * mRadio, 0);
                path.close();
                mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                canvas.drawPath(path, mSchemeBasicPaint);
                canvas.drawText(scheme.getScheme(), x + mItemWidth - mPadding - 2 * mRadio, mPadding + mSchemeBaseLine, mTextPaint);
            } else if (scheme.getType() == SchemeType.INDEX.ordinal()) {
                Log.e("pwh", "画下标");
                mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                float radius = dipToPx(getContext(), 4);
                canvas.drawCircle(x + mItemWidth / 2, mItemHeight - radius - mPadding, radius, mSchemeBasicPaint);
            } else if (scheme.getType() == SchemeType.BACKGROUND.ordinal()) {
                Log.e("pwh", "画背景色");
                mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                canvas.drawRect(x, 0, x + mItemWidth, mItemHeight, mSchemePaint);
            }


        }

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = -mItemHeight / 6;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mCurMonthLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
