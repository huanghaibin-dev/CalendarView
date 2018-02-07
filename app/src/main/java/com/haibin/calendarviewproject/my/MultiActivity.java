package com.haibin.calendarviewproject.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.Article;
import com.haibin.calendarviewproject.ArticleAdapter;
import com.haibin.calendarviewproject.R;
import com.haibin.calendarviewproject.base.activity.BaseActivity;
import com.haibin.calendarviewproject.base.type.SchemeType;
import com.haibin.calendarviewproject.colorful.ColorfulActivity;
import com.haibin.calendarviewproject.group.GroupItemDecoration;
import com.haibin.calendarviewproject.group.GroupRecyclerView;
import com.haibin.calendarviewproject.index.IndexActivity;
import com.haibin.calendarviewproject.simple.SimpleActivity;

import java.util.ArrayList;
import java.util.List;

public class MultiActivity extends BaseActivity implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener{

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MultiActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_multi;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    protected void initData() {
        List<Calendar> calendarList = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        //设置所有的scheme,请注意背景必须先设置，不然会出现覆盖的情况
        Calendar calendar = getCalendar(year, month, 5);
        setScheme(calendar,0xFFff0000,"",SchemeType.BACKGROUND);
        setScheme(calendar,0xFFe69138,"事",SchemeType.TRIGLE);
        setScheme(calendar,0xFFe69138,"",SchemeType.INDEX);
        calendarList.add(calendar);

        //设置今天
        java.util.Calendar c = java.util.Calendar.getInstance();//可以对每个时间域单独修改
        int yearNow = c.get(java.util.Calendar.YEAR);
        int monthNow = c.get(java.util.Calendar.MONTH) + 1;
        int dateNow = c.get(java.util.Calendar.DATE);
        int hourNow = c.get(java.util.Calendar.HOUR_OF_DAY);
        int minuteNow = c.get(java.util.Calendar.MINUTE);
        int secondNow = c.get(java.util.Calendar.SECOND);
        Log.e("pwh",yearNow + "/" + monthNow + "/" + dateNow + " " +hourNow + ":" +minuteNow + ":" + secondNow);
        calendar = getCalendar(yearNow, monthNow, dateNow);
        setScheme(calendar,0xFFff0000,"今",SchemeType.TRIGLE);
        calendarList.add(calendar);

        calendar = getCalendar(year, month, 19);
        setScheme(calendar,0xFFff0000,"",SchemeType.INDEX);
        calendarList.add(calendar);

        mCalendarView.setSchemeDate(calendarList);

        mRecyclerView = (GroupRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String,Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(this));
        mRecyclerView.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_flyme:
                MultiActivity.show(this);
                break;
            case R.id.ll_simple:
                SimpleActivity.show(this);
                break;
            case R.id.ll_colorful:
                ColorfulActivity.show(this);
                break;
            case R.id.ll_index:
                IndexActivity.show(this);
                break;
        }
    }

    private Calendar getCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);

        return calendar;
    }

    private Calendar setScheme(Calendar calendar,int color, String text, SchemeType schemeType){
        Calendar.Scheme scheme = new Calendar.Scheme(schemeType.ordinal(),color,text);
        calendar.addScheme(scheme);
        return calendar;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }


    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


}
