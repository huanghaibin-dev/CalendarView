package com.haibin.calendarviewproject;

import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.base.activity.BaseActivity;

/**
 * 测试
 * Created by huanghaibin on 2018/1/28.
 */

public class TestActivity extends BaseActivity {

    private NestedScrollView mScrollView;
    private CalendarView mCalendarView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void onClick(View view) {
        mCalendarView.showSelectLayout(mCalendarView.getCurYear());
    }
}
