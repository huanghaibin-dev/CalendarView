package com.haibin.calendarview;

/**
 * Created by haibin
 * on 2017/3/5.
 */
@SuppressWarnings("all")
public interface OnDateChangeListener {
    void onDateChange(int year, int month, int day, String lunar, String scheme);

    void onYearChange(int year);
}
