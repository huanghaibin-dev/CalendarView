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

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 年份+月份选择布局
 * ViewPager + RecyclerView
 */
public final class YearViewSelectLayout extends ViewPager {
    private int mYearCount;
    private boolean isUpdateYearView;
    private CalendarViewDelegate mDelegate;
    private YearRecyclerView.OnMonthSelectedListener mListener;

    public YearViewSelectLayout(Context context) {
        this(context, null);
    }

    public YearViewSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        this.mYearCount = mDelegate.getMaxYear() - mDelegate.getMinYear() + 1;
        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mYearCount;
            }

            @Override
            public int getItemPosition(Object object) {
                return isUpdateYearView ? POSITION_NONE : super.getItemPosition(object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                YearRecyclerView view = new YearRecyclerView(getContext());
                container.addView(view);
                view.setup(mDelegate);
                view.setOnMonthSelectedListener(mListener);
                view.init(position + mDelegate.getMinYear());
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if (object instanceof YearRecyclerView)
                    container.removeView((YearRecyclerView) object);
            }
        });
        setCurrentItem(mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear());
    }

    /**
     * 通知刷新
     */
    void notifyDataSetChanged() {
        this.mYearCount = mDelegate.getMaxYear() - mDelegate.getMinYear() + 1;
        getAdapter().notifyDataSetChanged();
    }

    /**
     * 滚动到某年
     *
     * @param year         year
     * @param smoothScroll smoothScroll
     */
    void scrollToYear(int year, boolean smoothScroll) {
        setCurrentItem(year - mDelegate.getMinYear(), smoothScroll);
    }

    /**
     * 更新日期范围
     */
    final void updateRange() {
        isUpdateYearView = true;
        notifyDataSetChanged();
        isUpdateYearView = false;
    }

    /**
     * 更新界面
     */
    final void update() {
        for (int i = 0; i < getChildCount(); i++) {
            YearRecyclerView view = (YearRecyclerView) getChildAt(i);
            view.getAdapter().notifyDataSetChanged();
        }
    }


    /**
     * 更新周起始
     */
    final void updateWeekStart() {
        for (int i = 0; i < getChildCount(); i++) {
            YearRecyclerView view = (YearRecyclerView) getChildAt(i);
            view.updateWeekStart();
            view.getAdapter().notifyDataSetChanged();
        }
    }

    final void setOnMonthSelectedListener(YearRecyclerView.OnMonthSelectedListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getHeight(getContext(), this), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算相对高度
     *
     * @param context context
     * @param view    view
     * @return 月视图选择器最适合的高度
     */
    private static int getHeight(Context context, View view) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert manager != null;
        Display display = manager.getDefaultDisplay();
        int h = display.getHeight();
        int[] location = new int[2];
        view.getLocationInWindow(location);
        view.getLocationOnScreen(location);
        return h - location[1];
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDelegate.isYearViewScrollable() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDelegate.isYearViewScrollable() && super.onInterceptTouchEvent(ev);
    }
}
