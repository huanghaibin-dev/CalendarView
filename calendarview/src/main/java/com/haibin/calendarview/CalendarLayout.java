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


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;


/**
 * 日历布局
 */
public class CalendarLayout extends LinearLayout {

    /**自定义ViewPager*/
    WrapViewPager mViewPager;
    /**ContentView*/
    ViewGroup mContentView;

    private int mTouchSlop;
    private int mContentViewTranslateY; //ContentView  可滑动的最大距离距离 , 固定
    private int mViewPagerTranslateY = 0;// ViewPager可以平移的距离

    private float downY;
    private float mLastY;
    private boolean isAnimating = false;

    /**内容布局id*/
    private int mContentViewId;

    /**手速判断*/
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;

    int mItemHeight;

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarLayout);
        mContentViewId = array.getResourceId(R.styleable.CalendarLayout_calendar_content_view_id, 0);
        array.recycle();
        //setSelectPosition(6);
        mVelocityTracker = VelocityTracker.obtain();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    /**
     * 初始化当前时间的位置
     * @param cur 当前日期时间
     */
    void initCalendarPosition(Calendar cur) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(cur.getYear(), cur.getMonth() - 1, 1);
        int diff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0,则偏移几天
        int size = diff + cur.getDay() -1;
        setSelectPosition(size);
    }

    /**
     * 当前第几项被选中
     */
    void setSelectPosition(int selectPosition) {
        int line = (selectPosition + 7) / 7;
        mViewPagerTranslateY = (line - 1) * mItemHeight;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mContentView != null && mViewPager != null) {
            int h = getHeight() - mItemHeight
                    - Util.dipToPx(getContext(), 41);
            int heightSpec = MeasureSpec.makeMeasureSpec(h,
                    MeasureSpec.EXACTLY);
            mContentView.measure(widthMeasureSpec, heightSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentViewTranslateY = mViewPager.getMeasuredHeight() - mItemHeight;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewPager = (WrapViewPager) findViewById(R.id.vp_calendar).findViewById(R.id.vp_calendar);
        mContentView = (ViewGroup) findViewById(mContentViewId);
        if (mContentView != null) {
            mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAnimating) {
            return true;
        }
        if (mContentView == null || mContentView.getVisibility() != VISIBLE) {
            return super.onInterceptTouchEvent(ev);
        }
        final int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                 /*
                   如果向上滚动，且ViewPager已经收缩，不拦截事件
                 */
                if (dy < 0 && mContentView.getTranslationY() == -mContentViewTranslateY) {
                    return false;
                }
                /*
                  * 如果向下滚动，有 2 种情况处理 且y在ViewPager下方
                  * 1、RecyclerView 或者其它滚动的View，当mContentView滚动到顶部时，拦截事件
                  * 2、非滚动控件，直接拦截事件
                */
                if (dy > 0 && mContentView.getTranslationY() == -mContentViewTranslateY
                        && y >= Util.dipToPx(getContext(), 98)) {
                    if (!isScrollTop())
                        return false;
                }

                if (dy > 0 && mContentView.getTranslationY() == 0 && y >= Util.dipToPx(getContext(), 98)) {
                    return false;
                }

                if (Math.abs(dy) > mTouchSlop) {//大于mTouchSlop开始拦截事件，ContentView和ViewPager得到CANCEL事件
                    if ((dy > 0 && mContentView.getTranslationY() <= 0)
                            || (dy < 0 && mContentView.getTranslationY() >= -mContentViewTranslateY)) {
                        mLastY = y;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mContentView == null)
            return false;
        int action = event.getAction();
        float y = event.getY();
        mVelocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = downY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                if (dy < 0 && mContentView.getTranslationY() == -mContentViewTranslateY) {
                    mContentView.onTouchEvent(event);
                    return false;
                }
                if (dy > 0 && mContentView.getTranslationY() + dy >= 0) {
                    mContentView.setTranslationY(0);
                    translationViewPager();
                    return super.onTouchEvent(event);
                }
                if (dy < 0 && mContentView.getTranslationY() + dy <= -mContentViewTranslateY) {
                    mContentView.setTranslationY(-mContentViewTranslateY);
                    translationViewPager();
                    return super.onTouchEvent(event);
                }
                mContentView.setTranslationY(mContentView.getTranslationY() + dy);
                translationViewPager();
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                float mYVelocity = velocityTracker.getYVelocity();
                if (mContentView.getTranslationY() == 0
                        || mContentView.getTranslationY() == mContentViewTranslateY) {
                    break;
                }
                if (Math.abs(mYVelocity) >= 800) {
                    if (mYVelocity < 0) {
                        shrink();
                    } else {
                        expand();
                    }
                    return super.onTouchEvent(event);
                }
                if (event.getY() - downY > 0) {
                    expand();
                } else {
                    shrink();
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private void translationViewPager() {
        float percent = mContentView.getTranslationY() * 1.0f / mContentViewTranslateY;
        mViewPager.setTranslationY(mViewPagerTranslateY * percent);
    }


    /**
     * 展开
     */
    public void expand() {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView,
                "translationY", mContentView.getTranslationY(), 0f);
        objectAnimator.setDuration(240);
        objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                float percent = currentValue * 1.0f / mContentViewTranslateY;
                mViewPager.setTranslationY(mViewPagerTranslateY * percent);
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }
        });
        objectAnimator.start();
    }


    /**
     * 收缩
     */
    public void shrink() {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView,
                "translationY", mContentView.getTranslationY(), -mContentViewTranslateY);
        objectAnimator.setDuration(240);
        objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                float percent = currentValue * 1.0f / mContentViewTranslateY;
                mViewPager.setTranslationY(mViewPagerTranslateY * percent);
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }
        });
        objectAnimator.start();
    }

    /**
     * ContentView是否滚动到顶部
     */
    private boolean isScrollTop() {
        if (mContentView instanceof RecyclerView)
            return ((RecyclerView) mContentView).computeVerticalScrollOffset() == 0;
        if (mContentView instanceof AbsListView) {
            boolean result = false;
            AbsListView listView = (AbsListView) mContentView;
            if (listView.getFirstVisiblePosition() == 0) {
                final View topChildView = listView.getChildAt(0);
                result = topChildView.getTop() == 0;
            }
            return result;
        }
        return mContentView.getScrollY() == 0;
    }
}
