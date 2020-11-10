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
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * 年份+月份选择布局
 * ViewPager + RecyclerView
 */
public final class YearViewPager extends ViewPager {
    private int mYearCount;
    private boolean isUpdateYearView;
    private CalendarViewDelegate mDelegate;
    private YearRecyclerView.OnMonthSelectedListener mListener;

    public YearViewPager(Context context) {
        this(context, null);
    }

    public YearViewPager(Context context, AttributeSet attrs) {
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
            public int getItemPosition(@NonNull Object object) {
                return isUpdateYearView ? POSITION_NONE : super.getItemPosition(object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                YearRecyclerView view = new YearRecyclerView(getContext());
                container.addView(view);
                view.setup(mDelegate);
                view.setOnMonthSelectedListener(mListener);
                view.init(position + mDelegate.getMinYear());
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        setCurrentItem(mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear());
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (Math.abs(getCurrentItem() - item) > 1) {
            super.setCurrentItem(item, false);
        } else {
            super.setCurrentItem(item, false);
        }
    }

    /**
     * 通知刷新
     */
    void notifyDataSetChanged() {
        this.mYearCount = mDelegate.getMaxYear() - mDelegate.getMinYear() + 1;
        if(getAdapter() != null){
            getAdapter().notifyDataSetChanged();
        }

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
            view.notifyAdapterDataSetChanged();
        }
    }


    /**
     * 更新周起始
     */
    final void updateWeekStart() {
        for (int i = 0; i < getChildCount(); i++) {
            YearRecyclerView view = (YearRecyclerView) getChildAt(i);
            view.updateWeekStart();
            view.notifyAdapterDataSetChanged();
        }
    }

    /**
     * 更新字体颜色大小
     */
    final void updateStyle(){
        for (int i = 0; i < getChildCount(); i++) {
            YearRecyclerView view = (YearRecyclerView) getChildAt(i);
            view.updateStyle();
        }
    }

    final void setOnMonthSelectedListener(YearRecyclerView.OnMonthSelectedListener listener) {
        this.mListener = listener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //heightMeasureSpec = MeasureSpec.makeMeasureSpec(getHeight(getContext(), this), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算相对高度
     *
     * @param context context
     * @param view    view
     * @return 年月视图选择器最适合的高度
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
