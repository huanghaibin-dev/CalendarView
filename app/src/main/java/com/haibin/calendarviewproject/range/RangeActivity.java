package com.haibin.calendarviewproject.range;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.R;
import com.haibin.calendarviewproject.base.activity.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RangeActivity extends BaseActivity implements
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnCalendarRangeSelectListener,
        CalendarView.OnMonthChangeListener,
        View.OnClickListener {

    TextView mTextLeftDate;
    TextView mTextLeftWeek;

    TextView mTextRightDate;
    TextView mTextRightWeek;

    TextView mTextMinRange;
    TextView mTextMaxRange;

    CalendarView mCalendarView;

    private int mCalendarHeight;

    public static void show(Context context) {
        context.startActivity(new Intent(context, RangeActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_range;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextLeftDate = findViewById(R.id.tv_left_date);
        mTextLeftWeek = findViewById(R.id.tv_left_week);
        mTextRightDate = findViewById(R.id.tv_right_date);
        mTextRightWeek = findViewById(R.id.tv_right_week);

        mTextMinRange = findViewById(R.id.tv_min_range);
        mTextMaxRange = findViewById(R.id.tv_max_range);

        mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setOnCalendarRangeSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        //设置日期拦截事件，当前有效
        mCalendarView.setOnCalendarInterceptListener(this);

        findViewById(R.id.iv_clear).setOnClickListener(this);
        findViewById(R.id.iv_reduce).setOnClickListener(this);
        findViewById(R.id.iv_increase).setOnClickListener(this);
        findViewById(R.id.tv_commit).setOnClickListener(this);

        mCalendarHeight = dipToPx(this, 46);

        mCalendarView.setRange(2000, 1, 1,

                mCalendarView.getCurYear(), mCalendarView.getCurMonth(), mCalendarView.getCurDay()

                );
        mCalendarView.post(new Runnable() {
            @Override
            public void run() {
                mCalendarView.scrollToCurrent();
            }
        });
    }

    @Override
    protected void initData() {

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);

        mTextMinRange.setText(String.format("min range = %s", mCalendarView.getMinSelectRange()));
        mTextMaxRange.setText(String.format("max range = %s", mCalendarView.getMaxSelectRange()));
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                mCalendarView.clearSelectRange();
                mTextLeftWeek.setText("开始日期");
                mTextRightWeek.setText("结束日期");
                mTextLeftDate.setText("");
                mTextRightDate.setText("");
                //mCalendarView.setSelectCalendarRange(2018,10,13,2018,10,13);
                break;
            case R.id.iv_reduce:

                mCalendarHeight -= dipToPx(this, 8);
                if (mCalendarHeight <= dipToPx(this, 46)) {
                    mCalendarHeight = dipToPx(this, 46);
                }
                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                break;
            case R.id.iv_increase:
                mCalendarHeight += dipToPx(this, 8);
                if (mCalendarHeight >= dipToPx(this, 90)) {
                    mCalendarHeight = dipToPx(this, 90);
                }
                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                break;
            case R.id.tv_commit:
                List<Calendar> calendars = mCalendarView.getSelectCalendarRange();
                if (calendars == null || calendars.size() == 0) {
                    return;
                }
                for (Calendar c : calendars) {
                    Log.e("SelectCalendarRange", c.toString()
                            + " -- " + c.getScheme()
                            + "  --  " + c.getLunar());
                }
                Toast.makeText(this, String.format("选择了%s个日期: %s —— %s", calendars.size(),
                        calendars.get(0).toString(), calendars.get(calendars.size() - 1).toString()),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 屏蔽某些不可点击的日期，可根据自己的业务自行修改
     * 如 calendar > 2018年1月1日 && calendar <= 2020年12月31日
     *
     * @param calendar calendar
     * @return 是否屏蔽某些不可点击的日期，MonthView和WeekView有类似的API可调用
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return false;
        //return calendar.getTimeInMillis()<getCurrentDayMill() ;
    }


    private long getCurrentDayMill(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR,0);
        calendar.set(java.util.Calendar.MINUTE,0);
        calendar.set(java.util.Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this,
                calendar.toString() + (isClick ? "拦截不可点击" : "拦截设定为无效日期"),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + "  --  " + month);
    }

    @Override
    public void onCalendarSelectOutOfRange(Calendar calendar) {
        // TODO: 2018/9/13 超出范围提示
    }

    @Override
    public void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange) {
        Toast.makeText(this,
                calendar.toString() + (isOutOfMinRange ? "小于最小选择范围" : "超过最大选择范围"),
                Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarRangeSelect(Calendar calendar, boolean isEnd) {
        if (!isEnd) {
            mTextLeftDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextLeftWeek.setText(WEEK[calendar.getWeek()]);
            mTextRightWeek.setText("结束日期");
            mTextRightDate.setText("");
        } else {
            mTextRightDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextRightWeek.setText(WEEK[calendar.getWeek()]);
        }
    }

    private static final String[] WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
}
