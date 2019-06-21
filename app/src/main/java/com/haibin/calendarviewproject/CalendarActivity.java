package com.haibin.calendarviewproject;

import android.content.Context;
import android.content.Intent;

import com.haibin.calendarviewproject.base.activity.BaseActivity;

/**
 * Only calendar
 * Created by haibin on 2019/6/12.
 */

public class CalendarActivity extends BaseActivity {

    public static void show(Context context) {
        context.startActivity(new Intent(context, CalendarActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
    }

    @Override
    protected void initData() {

    }
}
