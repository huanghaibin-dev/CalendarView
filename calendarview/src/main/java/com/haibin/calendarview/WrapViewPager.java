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
 */package com.haibin.calendarview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;


/**
 * 这是一个自适应高度的View
 */
@SuppressWarnings("unused")
public class WrapViewPager extends ViewPager {
    private CalendarView.CalendarViewDelegate mDelegate;
    private int mNextViewHeight, mPreViewHeight, mCurrentViewHeight;
    private boolean isFirstInit = true;
    CalendarLayout mParentLayout;

    public WrapViewPager(Context context) {
        this(context, null);
    }

    public WrapViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(new OnPageChangeListener() {
            /**
             *
             * @param position position如果小于当前position，则代表往前一页滑动，否则为下一页
             * @param positionOffset 滑动比例
             * @param positionOffsetPixels 滑动像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (getTranslationY() != 0) {
                    return;
                }
                int height;
                if (position < getCurrentItem()) {//右滑-1
                    height = (int) ((mPreViewHeight)
                            * (1 - positionOffset) +
                            mCurrentViewHeight
                                    * positionOffset);
                } else {//左滑+！
                    height = (int) ((mCurrentViewHeight)
                            * (1 - positionOffset) +
                            (mNextViewHeight)
                                    * positionOffset);
                }
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = height;
                setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                int year = position / 12 + mDelegate.getMinYear();
                int month = position % 12 + 1;
                mCurrentViewHeight = getCardHeight();
                if (month == 1) {
                    mNextViewHeight = getCardHeight();
                    mPreViewHeight = getCardHeight();
                } else {
                    mPreViewHeight = getCardHeight();
                    if (month == 12) {
                        mNextViewHeight = getCardHeight();
                    } else {
                        mNextViewHeight = getCardHeight();
                    }
                }
                if (isFirstInit) {
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.height = mCurrentViewHeight;
                    setLayoutParams(params);
                    isFirstInit = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void setup(CalendarView.CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getCardHeight(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取日历卡高度
     *
     * @return 获取日历卡高度
     */
    private int getCardHeight() {
        return 6 * mDelegate.getCalendarItemHeight();
    }

}
