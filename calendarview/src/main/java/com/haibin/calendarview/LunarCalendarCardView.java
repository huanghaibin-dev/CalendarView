package com.haibin.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 带农历的
 */

public class LunarCalendarCardView extends CalendarCardView {
    public LunarCalendarCardView(Context context) {
        super(context);
    }

    public LunarCalendarCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void onDrawScheme(Canvas canvas, Calendar calendar, int cx, int cy, int radius) {
        super.onDrawScheme(canvas, calendar, cx, cy, radius);
    }

    /**
     * y = 1/2高度
     */
    @Override
    void onDrawText(Canvas canvas, Calendar calendar, float x, float y, int h, boolean hasScheme) {
        int top = h - mItemHeight / 8;
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), x, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), x, mTextBaseLine + h +  mItemHeight / 10, mLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), x,mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), x, mTextBaseLine + h +  mItemHeight / 10, mLunarTextPaint);
        }
    }
}
