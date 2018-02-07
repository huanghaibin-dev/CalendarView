package com.haibin.calendarviewproject.my;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.haibin.calendarviewproject.base.type.SchemeType;

import java.util.List;

/**
 * 多层级日历布局
 * Created by wenhua on 2017/11/15.
 * https://github.com/peterforme 感谢 @peterforme 提供PR
 */

public class MultiMonthView extends MonthView {

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();

    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;

    public MultiMonthView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 0);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

    }

    /**
     * 绘制选中的日子
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

    /**
     * 绘制标记的事件日子
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        List<Calendar.Scheme> schemes = calendar.getSchemes();
        for(Calendar.Scheme scheme : schemes){
            if(scheme.getType() == SchemeType.TRIGLE.ordinal()){
                Log.e("pwh","画三角形");
                Path path=new Path();
                path.moveTo(x + mItemWidth  - 4 * mRadio,y);
                path.lineTo(x + mItemWidth ,y  + 4 * mRadio);
                path.lineTo(x + mItemWidth ,y  );
                path.moveTo(x + mItemWidth  - 4 * mRadio,y);
                path.close();
                mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                canvas.drawPath(path,mSchemeBasicPaint);
                canvas.drawText(scheme.getScheme(), x + mItemWidth - mPadding - 2 * mRadio, y + mPadding + mSchemeBaseLine , mTextPaint);
            }
            else if(scheme.getType() == SchemeType.INDEX.ordinal()){
                Log.e("pwh","画下标");
                mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                float radius = dipToPx(getContext(), 4);
                canvas.drawCircle(x + mItemWidth / 2,y + mItemHeight - radius  - mPadding,radius,mSchemeBasicPaint);
            }
            else if(scheme.getType() == SchemeType.BACKGROUND.ordinal()){
                Log.e("pwh","画背景色");
                mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                canvas.drawRect(x,y,x+mItemWidth,y+mItemHeight,mSchemePaint);
            }


        }

    }

    /**
     * 绘制文本
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
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
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mCurMonthLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
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
