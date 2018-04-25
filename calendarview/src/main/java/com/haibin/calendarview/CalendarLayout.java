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
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;


/**
 * 日历布局
 */
public final class CalendarLayout extends LinearLayout {

    /**
     * 周月视图
     */
    private static final int CALENDAR_SHOW_MODE_BOTH_MONTH_WEEK_VIEW = 0;


    /**
     * 仅周视图
     */
    private static final int CALENDAR_SHOW_MODE_ONLY_WEEK_VIEW = 1;

    /**
     * 仅月视图
     */
    private static final int CALENDAR_SHOW_MODE_ONLY_MONTH_VIEW = 2;

    /**
     * 默认展开
     */
    private static final int STATUS_EXPAND = 0;

    /**
     * 默认收缩
     */
    private static final int STATUS_SHRINK = 1;

    /**
     * 默认状态
     */
    private int mDefaultStatus;

    /**
     * 星期栏
     */
    WeekBar mWeekBar;

    /**
     * 自定义ViewPager，月视图
     */
    MonthViewPager mMonthView;

    /**
     * 自定义的周视图
     */
    WeekViewPager mWeekPager;

    /**
     * 年视图
     */
    YearSelectLayout mYearView;

    /**
     * ContentView
     */
    ViewGroup mContentView;


    private int mCalendarShowMode = 0;

    private int mTouchSlop;
    private int mContentViewTranslateY; //ContentView  可滑动的最大距离距离 , 固定
    private int mViewPagerTranslateY = 0;// ViewPager可以平移的距离，不代表mMonthView的平移距离

    private float downY;
    private float mLastY;
    private boolean isAnimating = false;

    /**
     * 内容布局id
     */
    private int mContentViewId;

    /**
     * 手速判断
     */
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;

    int mItemHeight;

    private CustomCalendarViewDelegate mDelegate;

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarLayout);
        mContentViewId = array.getResourceId(R.styleable.CalendarLayout_calendar_content_view_id, 0);
        mDefaultStatus = array.getInt(R.styleable.CalendarLayout_default_status, STATUS_EXPAND);
        mCalendarShowMode = array.getInt(R.styleable.CalendarLayout_calendar_show_mode, CALENDAR_SHOW_MODE_BOTH_MONTH_WEEK_VIEW);

        array.recycle();
        mVelocityTracker = VelocityTracker.obtain();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    void setup(CustomCalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        initCalendarPosition(delegate.mSelectedCalendar);
        updateContentViewTranslateY();
    }

    /**
     * 初始化当前时间的位置
     *
     * @param cur 当前日期时间
     */
    private void initCalendarPosition(Calendar cur) {
        int diff = CalendarUtil.getMonthViewStartDiff(cur,mDelegate.getWeekStart());
        int size = diff + cur.getDay() - 1;
        setSelectPosition(size);
    }

    /**
     * 当前第几项被选中
     */
    void setSelectPosition(int selectPosition) {
        int line = (selectPosition + 7) / 7;
        mViewPagerTranslateY = (line - 1) * mItemHeight;
    }

    /**
     * 设置选中的周，更新位置
     *
     * @param line line
     */
    void setSelectWeek(int line) {
        mViewPagerTranslateY = (line - 1) * mItemHeight;
    }


    /**
     * 更新内容ContentView可平移的最大距离
     */
    void updateContentViewTranslateY() {
        if (mDelegate == null || mContentView == null)
            return;
        Calendar calendar = mDelegate.mSelectedCalendar;
        if (mDelegate.getMonthViewShowMode() == CustomCalendarViewDelegate.MODE_ALL_MONTH) {
            mContentViewTranslateY = 5 * mItemHeight;
        } else {
            mContentViewTranslateY = CalendarUtil.getMonthViewHeight(calendar.getYear(), calendar.getMonth(), mItemHeight,mDelegate.getWeekStart())
                    - mItemHeight;
        }
        //已经显示周视图，如果月视图高度是动态改变的，则需要动态平移contentView的高度
        if (mWeekPager.getVisibility() == VISIBLE && mDelegate.getMonthViewShowMode() != CustomCalendarViewDelegate.MODE_ALL_MONTH) {
            if (mContentView == null)
                return;
            mContentView.setTranslationY(-mContentViewTranslateY);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDelegate.isShowYearSelectedLayout) {
            return false;
        }
        if (mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_MONTH_VIEW ||
                mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_WEEK_VIEW)
            return false;
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
                //向上滑动，并且contentView平移到最大距离，显示周视图
                if (dy < 0 && mContentView.getTranslationY() == -mContentViewTranslateY) {
                    mContentView.onTouchEvent(event);
                    showWeek();
                    return false;
                }
                hideWeek();

                //向下滑动，并且contentView已经完全到底部
                if (dy > 0 && mContentView.getTranslationY() + dy >= 0) {
                    mContentView.setTranslationY(0);
                    translationViewPager();
                    return super.onTouchEvent(event);
                }
                //向上滑动，并且contentView已经平移到最大距离，则contentView平移到最大的距离
                if (dy < 0 && mContentView.getTranslationY() + dy <= -mContentViewTranslateY) {
                    mContentView.setTranslationY(-mContentViewTranslateY);
                    translationViewPager();
                    return super.onTouchEvent(event);
                }
                //否则按比例平移
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mContentView != null && mMonthView != null) {
            int h = getHeight() - mItemHeight
                    - (mDelegate != null ? mDelegate.getWeekBarHeight() :
                    CalendarUtil.dipToPx(getContext(), 40))
                    - CalendarUtil.dipToPx(getContext(), 1);
            int heightSpec = MeasureSpec.makeMeasureSpec(h,
                    MeasureSpec.EXACTLY);
            mContentView.measure(widthMeasureSpec, heightSpec);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMonthView = (MonthViewPager) findViewById(R.id.vp_calendar).findViewById(R.id.vp_calendar);
        mWeekPager = (WeekViewPager) findViewById(R.id.vp_week).findViewById(R.id.vp_week);
        mContentView = (ViewGroup) findViewById(mContentViewId);
        mYearView = (YearSelectLayout) findViewById(R.id.selectLayout);
        if (mContentView != null) {
            mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAnimating) {
            return true;
        }
        if (mYearView == null ||
                mContentView == null ||
                mContentView.getVisibility() != VISIBLE) {
            return super.onInterceptTouchEvent(ev);
        }
        if (mYearView.getVisibility() == VISIBLE || mDelegate.isShowYearSelectedLayout) {
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
                        && y >= CalendarUtil.dipToPx(getContext(), 98)) {
                    if (!isScrollTop())
                        return false;
                }

                if (dy > 0 && mContentView.getTranslationY() == 0 && y >= CalendarUtil.dipToPx(getContext(), 98)) {
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




    /**
     * 平移ViewPager月视图
     */
    private void translationViewPager() {
        float percent = mContentView.getTranslationY() * 1.0f / mContentViewTranslateY;
        mMonthView.setTranslationY(mViewPagerTranslateY * percent);
    }


    /**
     * 是否展开了
     *
     * @return isExpand
     */
    public boolean isExpand() {
        return mContentView == null || mMonthView.getVisibility() == VISIBLE;
    }


    /**
     * 展开
     * @return 展开是否成功
     */
    public boolean expand() {
        if (isAnimating ||
                mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_WEEK_VIEW ||
                mContentView == null)
            return false;
        if (mMonthView.getVisibility() != VISIBLE) {
            mWeekPager.setVisibility(GONE);
            mMonthView.setVisibility(VISIBLE);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView,
                "translationY", mContentView.getTranslationY(), 0f);
        objectAnimator.setDuration(240);
        objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                float percent = currentValue * 1.0f / mContentViewTranslateY;
                mMonthView.setTranslationY(mViewPagerTranslateY * percent);
                isAnimating = true;
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                hideWeek();

            }
        });
        objectAnimator.start();
        return true;
    }


    /**
     * 收缩
     * @return 成功或者失败
     */
    @SuppressWarnings("all")
    public boolean shrink() {
        if (isAnimating || mContentView == null) {
            return false;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView,
                "translationY", mContentView.getTranslationY(), -mContentViewTranslateY);
        objectAnimator.setDuration(240);
        objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                float percent = currentValue * 1.0f / mContentViewTranslateY;
                mMonthView.setTranslationY(mViewPagerTranslateY * percent);
                isAnimating = true;
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                showWeek();

            }
        });
        objectAnimator.start();
        return true;
    }

    /**
     * 初始化状态
     */
    void initStatus() {
        if (mContentView == null) {
            return;
        }
        if ((mDefaultStatus == STATUS_SHRINK ||
                mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_WEEK_VIEW) &&
                mCalendarShowMode != CALENDAR_SHOW_MODE_ONLY_MONTH_VIEW) {
            post(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView,
                            "translationY", mContentView.getTranslationY(), -mContentViewTranslateY);
                    objectAnimator.setDuration(0);
                    objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float currentValue = (Float) animation.getAnimatedValue();
                            float percent = currentValue * 1.0f / mContentViewTranslateY;
                            mMonthView.setTranslationY(mViewPagerTranslateY * percent);
                            isAnimating = true;
                        }
                    });
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            isAnimating = false;
                            showWeek();

                        }
                    });
                    objectAnimator.start();
                }
            });
        }
    }

    /**
     * 隐藏周视图
     */
    private void hideWeek() {
        mWeekPager.setVisibility(GONE);
        mMonthView.setVisibility(VISIBLE);
    }

    /**
     * 显示周视图
     */
    private void showWeek() {
        mWeekPager.getAdapter().notifyDataSetChanged();
        mWeekPager.setVisibility(VISIBLE);
        mMonthView.setVisibility(INVISIBLE);
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


    /**
     * 隐藏内容布局
     */
    void hideContentView() {
        if (mContentView == null)
            return;
        mContentView.animate()
                .translationY(getHeight() - mMonthView.getHeight())
                .setDuration(220)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mContentView.setVisibility(INVISIBLE);
                        mContentView.clearAnimation();
                    }
                });
    }

    /**
     * 显示内容布局
     */
    void showContentView() {
        if (mContentView == null)
            return;
        mContentView.setTranslationY(getHeight() - mMonthView.getHeight());
        mContentView.setVisibility(VISIBLE);
        mContentView.animate()
                .translationY(0)
                .setDuration(180)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
    }
}
