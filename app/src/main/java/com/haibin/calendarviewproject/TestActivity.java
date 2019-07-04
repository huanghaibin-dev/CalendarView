package com.haibin.calendarviewproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.base.activity.BaseActivity;

/**
 * 测试界面
 * Created by huanghaibin on 2018/8/7.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {

    private CalendarView mCalendarView;
    public static void show(Context context) {
        context.startActivity(new Intent(context, TestActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        findViewById(R.id.iv_next).setOnClickListener(this);
        findViewById(R.id.iv_pre).setOnClickListener(this);
        mCalendarView =  findViewById(R.id.calendar_view);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_next:
                mCalendarView.scrollToNext(false);
                break;
            case R.id.iv_pre:
                mCalendarView.scrollToPre(false);
                break;
        }
    }
}
