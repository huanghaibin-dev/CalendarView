package com.haibin.calendarviewproject;

import android.content.Context;
import android.graphics.Canvas;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarCardView;

/**
 * 完成自定义测试
 * Created by huanghaibin on 2017/10/12.
 */

public class CalendarCardViewDemo extends CalendarCardView {
    public CalendarCardViewDemo(Context context) {
        super(context);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, float cx, float baselineY, boolean hasScheme) {
        int top = y - mItemHeight / 6;
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mLunarTextPaint);

            canvas.drawText(calendar.getScheme(), cx + mItemWidth/4, mTextBaseLine + top, mSchemeTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mLunarTextPaint);
        }
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int width, int x, int y, int cx, int cy, int radius) {
        //canvas.drawRect(x + 20, y + 20, x + width - 20, y + mItemHeight - 20, mSchemePaint);
    }

    @Override
    protected void onDrawSelected(Canvas canvas, int width, int x, int y, int cx, int cy, int radius, boolean hasScheme) {
        canvas.drawRect(x + 20, y + 20, x + width - 20, y + mItemHeight - 20, mSelectedPaint);
    }
}
