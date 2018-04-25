package com.haibin.calendarviewproject.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekBar;
import com.haibin.calendarviewproject.R;

/**
 * 自定义英文栏
 * Created by huanghaibin on 2017/11/30.
 */

public class CustomWeekBar extends WeekBar {

    private int mPreSelectedIndex;

    public CustomWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.english_week_bar, this, true);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDateSelected(Calendar calendar, int weekStart, boolean isClick) {
        getChildAt(mPreSelectedIndex).setSelected(false);
        int viewIndex = getViewIndexByCalendar(calendar, weekStart);
        getChildAt(viewIndex).setSelected(true);
        mPreSelectedIndex = viewIndex;
    }
}
