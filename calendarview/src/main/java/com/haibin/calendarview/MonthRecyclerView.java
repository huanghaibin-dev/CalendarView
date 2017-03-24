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


import java.util.List;

@SuppressWarnings("all")
public class MonthRecyclerView extends RecyclerView {
    private MonthAdapter mAdapter;
    private OnMonthSelectedListener mListener;

    public MonthRecyclerView(Context context) {
        this(context, null);
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAdapter = new MonthAdapter(context);
        setLayoutManager(new GridLayoutManager(context, 3));
        setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                if (mListener != null) {
                    Month month = mAdapter.getItem(position);
                    mListener.onMonthSelected(month.getYear(), month.getMonth());
                }
            }
        });
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

    void setSchemes(List<Calendar> mSchemes) {
        mAdapter.setSchemes(mSchemes);
    }

    void setSchemeColor(int schemeColor) {
        mAdapter.setSchemeColor(schemeColor);
    }

    void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.mListener = listener;
    }

    public interface OnMonthSelectedListener {
        void onMonthSelected(int year, int month);
    }
}
