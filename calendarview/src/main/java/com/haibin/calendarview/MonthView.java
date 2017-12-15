package com.haibin.calendarview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 月视图切换
 * Created by huanghaibin on 2017/12/15.
 */

public abstract class MonthView extends BaseCalendarCardView{
    public MonthView(Context context) {
        super(context);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
