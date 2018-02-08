package com.haibin.calendarviewproject.solay;

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

public class SolarWeekBar extends WeekBar {

    public SolarWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.solar_week_bar, this, true);
        setBackgroundColor(context.getResources().getColor(R.color.solar_background));
    }
}
