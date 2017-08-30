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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 月份选择布局
 * ViewPager + RecyclerView
 */
public class MonthSelectLayout extends ViewPager {
    private boolean isInit;
    private int mSchemeColor;
    private MonthRecyclerView.OnMonthSelectedListener mListener;
    private List<Calendar> mSchemes;
    private int mMinYear, mMaxYear;

    public MonthSelectLayout(Context context) {
        this(context, null);
    }

    public MonthSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setYearSpan(int minYear, int maxYear) {
        this.mMinYear = minYear;
        this.mMaxYear = maxYear;
    }

    void init(int year) {
        if (isInit) {
            setCurrentItem(year - mMinYear);
            return;
        }
        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mMaxYear-mMinYear;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                MonthRecyclerView view = new MonthRecyclerView(getContext());
                container.addView(view);
                view.setOnMonthSelectedListener(mListener);
                view.setSchemeColor(mSchemeColor);
                view.init(position + mMinYear);
                view.setSchemes(mSchemes);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if (object instanceof MonthRecyclerView)
                    container.removeView((MonthRecyclerView) object);
            }
        });
        isInit = true;
        setCurrentItem(year - mMinYear);
    }

    void setSchemes(List<Calendar> mSchemes) {
        this.mSchemes = mSchemes;
    }

    void setSchemeColor(int schemeColor) {
        this.mSchemeColor = schemeColor;
    }

    void update() {
        for (int i = 0; i < getChildCount(); i++) {
            MonthRecyclerView view = (MonthRecyclerView) getChildAt(i);
            view.setSchemeColor(mSchemeColor);
            view.getAdapter().notifyDataSetChanged();
        }
    }

    public void setOnMonthSelectedListener(MonthRecyclerView.OnMonthSelectedListener listener) {
        this.mListener = listener;
    }
}
