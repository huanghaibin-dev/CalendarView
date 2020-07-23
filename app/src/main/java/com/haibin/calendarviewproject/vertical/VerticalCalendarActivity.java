package com.haibin.calendarviewproject.vertical;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.R;
import com.haibin.calendarviewproject.base.activity.BaseActivity;
import com.haibin.calendarviewproject.meizu.MeiZuActivity;

/**
 * <pre>
 *     author : alan.wu
 *     e-mail : alan.wu@chinaibex.com
 *     time   : 2020/07/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class VerticalCalendarActivity extends BaseActivity {

    private CalendarView mCalendarView;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MeiZuActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vertical_calendar;
    }

    @Override
    protected void initView() {
        mCalendarView = findViewById(R.id.calendarView);
        int cYear = mCalendarView.getCurYear();
        int cMonth = mCalendarView.getCurMonth();
        int cDay = mCalendarView.getCurDay();

//        mCalendarView.setRange(cYear,cMonth,cDay,cYear+100,12,31);
        mCalendarView.hideWeekBar();
        mCalendarView.hideMonthPager();
        mCalendarView.scrollToCalendar(cYear,cMonth,cDay);
    }

    @Override
    protected void initData() {

    }
}
