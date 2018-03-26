package com.haibin.calendarviewproject;

import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 全部假设calendars1 ！= null, 传入calendars1，合并calendars2，返回calendars1，有多少个集合则调用多少次
     */
    private List<Calendar> merge(List<Calendar> calendars1, List<Calendar> calendars2) {
        if (calendars2 == null || calendars2.size() == 0)
            return calendars1;
        for (Calendar calendar : calendars2) {
            if (calendars1.contains(calendar)) {
                Calendar mergeCalendar = calendars1.get(calendars1.indexOf(calendar));
                mergeCalendar(mergeCalendar, calendar);
            } else {
                calendars1.add(calendar);
            }
        }
        return calendars1;
    }


    private void mergeCalendar(Calendar calendar1, Calendar calendar2) {
        if (calendar2.getSchemes() == null || calendar2.getSchemes().size() == 0)
            return;
        if (calendar1.getSchemes() == null) {
            calendar1.setSchemes(new ArrayList<Calendar.Scheme>());
        }
        calendar1.getSchemes().addAll(calendar2.getSchemes());
    }


    public void onClick(View view) {
        mCalendarView.showSelectLayout(mCalendarView.getCurYear());
    }
}
