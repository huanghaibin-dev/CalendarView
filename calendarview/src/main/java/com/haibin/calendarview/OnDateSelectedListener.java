package com.haibin.calendarview;

/**
 * Created by haibin
 * on 2017/3/8.
 */
@SuppressWarnings("all")
public interface OnDateSelectedListener {
    void onDateSelected(int year, int month, int day, String lunar, String scheme);
}
