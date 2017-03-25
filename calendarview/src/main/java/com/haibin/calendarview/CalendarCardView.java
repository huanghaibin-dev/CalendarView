/*
 * Copyright (C) 2016 huanghaibin_dev <huanghaibin_dev@163.com>
 * WebSite https://github.com/MiracleTimes-Dev
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haibin.calendarview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarCardView extends RecyclerView {
    private CalendarAdapter mAdapter;
    private Calendar mCurrent;
    private int mYear;
    private int mMonth;
    private OnDateSelectedListener mDateSelectedListener;
    private CalendarView.OnInnerDateSelectedListener mInnerListener;
    private OnDateChangeListener mListener;
    private List<Calendar> mItems;
    private List<Calendar> mSchemes;
    private String mScheme;

    public CalendarCardView(Context context) {
        super(context, null);
    }

    public CalendarCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setLayoutManager(new GridLayoutManager(context, 7));
        mAdapter = new CalendarAdapter(context);
        setAdapter(mAdapter);
        mCurrent = new Calendar();
        Date d = new Date();
        mCurrent.setYear(Util.getDate("yyyy", d));
        mCurrent.setMonth(Util.getDate("MM", d));
        mCurrent.setDay(Util.getDate("dd", d));
        setOverScrollMode(OVER_SCROLL_NEVER);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                mAdapter.update(position);
                Calendar date = mAdapter.getItem(position);
                if (date == null) return;
                mInnerListener.onDateSelected(date);

                if (mListener != null) {
                    mListener.onDateChange(date.getYear(), date.getMonth(), date.getDay(),
                            date.getLunar(), date.getScheme());
                }

                if (mDateSelectedListener != null) {
                    mDateSelectedListener.onDateSelected(date.getYear(), date.getMonth(), date.getDay(),
                            date.getLunar(), date.getScheme());
                }
            }
        });
    }

    void setScheme(String scheme) {
        this.mScheme = TextUtils.isEmpty(scheme) ? scheme : scheme.substring(0, 1);
    }

    void setCurrentDate(int year, int month) {
        mYear = year;
        mMonth = month;
        initCalendar();
    }

    void setSelectedColor(int color, int textColor) {
        mAdapter.setSelectedColor(color, textColor);
    }

    void setStyle(int mThemeColor, int mCurColor) {
        mAdapter.setStyle(mThemeColor, mCurColor);
    }

    void setOnDateChangeListener(OnDateChangeListener listener) {
        this.mListener = listener;
    }

    void setInnerListener(CalendarView.OnInnerDateSelectedListener listener) {
        this.mInnerListener = listener;
    }

    void setSelectedCalendar(Calendar calendar) {
        mAdapter.setSelectedCalendar(calendar);
    }

    private void initCalendar() {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(mYear, mMonth - 1, 1);
        int firstDayOfWeek = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0
        int mDaysCount = Util.getMonthDaysCount(mYear, mMonth);
        date.set(mYear, mMonth - 1, mDaysCount);
        int lastDayOfWeek = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月最后一天为星期几,星期天 == 0

        int nextMonthDaysOffest = 6 - lastDayOfWeek;//下个月的日偏移天数

        int preYear, preMonth;
        int nextYear, nextMonth;

        int mSize = firstDayOfWeek + mDaysCount + nextMonthDaysOffest;//日历卡要显示的总格数

        int preMonthDaysCount;
        if (mMonth == 1) {//如果是1月
            preYear = mYear - 1;
            preMonth = 12;
            nextYear = mYear;
            nextMonth = mMonth + 1;
            preMonthDaysCount = firstDayOfWeek == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
        } else if (mMonth == 12) {//如果是12月
            preYear = mYear;
            preMonth = mMonth - 1;
            nextYear = mYear + 1;
            nextMonth = 1;
            preMonthDaysCount = firstDayOfWeek == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
        } else {//平常
            preYear = mYear;
            preMonth = mMonth - 1;
            nextYear = mYear;
            nextMonth = mMonth + 1;
            preMonthDaysCount = firstDayOfWeek == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
        }
        int nextDay = 1;
        if (mItems == null)
            mItems = new ArrayList<>();
        mItems.clear();
        for (int i = 0; i < mSize; i++) {
            Calendar calendarDate = new Calendar();
            if (i < firstDayOfWeek) {
                calendarDate.setYear(preYear);
                calendarDate.setMonth(preMonth);
                calendarDate.setDay(preMonthDaysCount - firstDayOfWeek + i + 1);
            } else if (i >= mDaysCount + firstDayOfWeek) {
                calendarDate.setYear(nextYear);
                calendarDate.setMonth(nextMonth);
                calendarDate.setDay(nextDay);
                ++nextDay;
            } else {
                calendarDate.setYear(mYear);
                calendarDate.setMonth(mMonth);
                calendarDate.setCurrentMonth(true);
                calendarDate.setDay(i - firstDayOfWeek + 1);
            }
            if (calendarDate.equals(mCurrent))
                calendarDate.setCurrentDay(true);
            int[] lunar = LunarCalendar.solarToLunar(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDay());
            calendarDate.setLunar(LunarCalendar.numToChineseDay(lunar[2]));
            mItems.add(calendarDate);
        }
        mAdapter.addAll(mItems);
        if (mSchemes != null) {
            for (Calendar a : mAdapter.getItems()) {
                for (Calendar d : mSchemes) {
                    if (d.equals(a)) {
                        a.setScheme(mScheme);
                    }
                }
            }
        }
    }

    public void setSchemes(List<Calendar> mScheme) {
        this.mSchemes = mScheme;
        update();
    }

    void update() {
        if (mSchemes != null) {
            for (Calendar a : mAdapter.getItems()) {
                for (Calendar d : mSchemes) {
                    if (d.equals(a)) {
                        a.setScheme("记");
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mDateSelectedListener = listener;
    }
}
