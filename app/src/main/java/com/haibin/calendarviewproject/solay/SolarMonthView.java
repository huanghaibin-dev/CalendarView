package com.haibin.calendarviewproject.solay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

import java.util.List;

/**
 * 精致星系月视图，利用三角函数计算坐标
 * solar MonthView ,using trigonometric function
 * Created by huanghaibin on 2018/2/8.
 */

public class SolarMonthView extends MonthView {


    private Paint mPointPaint = new Paint();

    private int mRadius;
    private int mPointRadius;

    public SolarMonthView(Context context) {
        super(context);


        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setStyle(Paint.Style.STROKE);
        mSchemePaint.setStrokeWidth(dipToPx(context, 1.2f));
        mSchemePaint.setColor(0xFFFFFFFF);
        mPointRadius = dipToPx(context, 3.6f);
        mPointPaint.setColor(Color.RED);

    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);

        List<Calendar.Scheme> schemes = calendar.getSchemes();

        mPointPaint.setColor(schemes.get(0).getShcemeColor());//You can also use three fixed Paint 你也可以使用三个Paint对象
        int rightTopX = (int) (cx + mRadius * Math.cos(-10 * Math.PI / 180));
        int rightTopY = (int) (cy + mRadius * Math.sin(-10 * Math.PI / 180));
        canvas.drawCircle(rightTopX, rightTopY, mPointRadius, mPointPaint);

        mPointPaint.setColor(schemes.get(1).getShcemeColor());
        int leftTopX = (int) (cx + mRadius * Math.cos(-140 * Math.PI / 180));
        int leftTopY = (int) (cy + mRadius * Math.sin(-140 * Math.PI / 180));
        canvas.drawCircle(leftTopX, leftTopY, mPointRadius, mPointPaint);

        mPointPaint.setColor(schemes.get(2).getShcemeColor());
        int bottomX = (int) (cx + mRadius * Math.cos(100 * Math.PI / 180));
        int bottomY = (int) (cy + mRadius * Math.sin(100 * Math.PI / 180));
        canvas.drawCircle(bottomX, bottomY, mPointRadius, mPointPaint);

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
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
