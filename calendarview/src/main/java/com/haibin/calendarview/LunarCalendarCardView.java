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

    /**
     * y = 1/2高度
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar,
                              int x, int y, float cx, float baselineY,
                              boolean hasScheme) {
        int top = y - mItemHeight / 8;
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mLunarTextPaint);
        }
    }
}
