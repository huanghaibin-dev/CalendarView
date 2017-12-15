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
import android.util.AttributeSet;

/**
 * 年份布局选择View
 */
public class YearRecyclerView extends RecyclerView {
    private CustomCalendarViewDelegate mDelegate;
    private YearAdapter mAdapter;
    private OnMonthSelectedListener mListener;

    public YearRecyclerView(Context context) {
        this(context, null);
    }

    public YearRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAdapter = new YearAdapter(context);
        setLayoutManager(new GridLayoutManager(context, 3));
        setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                if (mListener != null && mDelegate != null) {
                    Month month = mAdapter.getItem(position);
                    if (month == null) {
                        return;
                    }
                    if (!Util.isMonthInRange(month.getYear(), month.getMonth(),
                            mDelegate.getMinYear(), mDelegate.getMinYearMonth(),
                            mDelegate.getMaxYear(), mDelegate.getMaxYearMonth())) {
                        return;
                    }
                    mListener.onMonthSelected(month.getYear(), month.getMonth());
                }
            }
        });
    }

    void setup(CustomCalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        this.mAdapter.setup(delegate);
    }

    void init(int year) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        for (int i = 1; i <= 12; i++) {
            date.set(year, i - 1, 1);
            int firstDayOfWeek = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0
            int mDaysCount = Util.getMonthDaysCount(year, i);
            Month month = new Month();
            month.setDiff(firstDayOfWeek);
            month.setCount(mDaysCount);
            month.setMonth(i);
            month.setYear(year);
            mAdapter.addItem(month);
        }
    }

    void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.mListener = listener;
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int h = MeasureSpec.getSize(heightSpec);
        mAdapter.setItemHeight(h / 4);
    }

    interface OnMonthSelectedListener {
        void onMonthSelected(int year, int month);
    }
}
