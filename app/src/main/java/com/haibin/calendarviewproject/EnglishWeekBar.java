package com.haibin.calendarviewproject;

import android.content.Context;
import android.view.LayoutInflater;

import com.haibin.calendarview.WeekBar;

/**
 * 自定义英文栏
 * Created by huanghaibin on 2017/11/30.
 */

public class EnglishWeekBar extends WeekBar {
    public EnglishWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.english_week_bar, this, true);
    }
}
