package com.haibin.calendarviewproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekBar;

/**
 * 自定义英文栏
 * Created by huanghaibin on 2017/11/30.
 */

public class EnglishWeekBar extends WeekBar {

    private int mPreSelectedIndex;

    public EnglishWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.english_week_bar, this, true);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDateSelected(Calendar calendar, boolean isClick) {
        getChildAt(mPreSelectedIndex).setSelected(false);
        getChildAt(calendar.getWeek()).setSelected(true);
        mPreSelectedIndex = calendar.getWeek();
    }
}
