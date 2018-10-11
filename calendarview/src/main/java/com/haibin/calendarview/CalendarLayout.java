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
import android.support.v4.view.MotionEventCompat;
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
public class CalendarLayout extends LinearLayout {

    /**
     * 多点触控支持
     */
    private int mActivePointerId;

    private static final int ACTIVE_POINTER = 1;

    private static final int INVALID_POINTER = -1;

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
    YearViewSelectLayout mYearView;

    /**
     * ContentView
     */
    ViewGroup mContentView;

    /**
     * 默认手势
     */
    private static final int GESTURE_MODE_DEFAULT = 0;

//       /**
    //     * 仅日历有效
    //     */
//    private static final int GESTURE_MODE_ONLY_CALENDAR = 1;

    /**
     * 禁用手势
     */
    private static final int GESTURE_MODE_DISABLED = 2;

    /**
     * 手势模式
     */
    private int mGestureMode;


    private int mCalendarShowMode;

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

    private int mItemHeight;

    private CalendarViewDelegate mDelegate;

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarLayout);
        mContentViewId = array.getResourceId(R.styleable.CalendarLayout_calendar_content_view_id, 0);
        mDefaultStatus = array.getInt(R.styleable.CalendarLayout_default_status, STATUS_EXPAND);
        mCalendarShowMode = array.getInt(R.styleable.CalendarLayout_calendar_show_mode, CALENDAR_SHOW_MODE_BOTH_MONTH_WEEK_VIEW);
        mGestureMode = array.getInt(R.styleable.CalendarLayout_gesture_mode, GESTURE_MODE_DEFAULT);
        array.recycle();
        mVelocityTracker = VelocityTracker.obtain();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    /**
     * 初始化
     *
     * @param delegate delegate
     */
    final void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        mItemHeight = mDelegate.getCalendarItemHeight();
        initCalendarPosition(delegate.mSelectedCalendar.isAvailable() ?
                delegate.mSelectedCalendar :
                delegate.createCurrentDate());
        updateContentViewTranslateY();
    }

    /**
     * 初始化当前时间的位置
     *
     * @param cur 当前日期时间
     */
    private void initCalendarPosition(Calendar cur) {
        int diff = CalendarUtil.getMonthViewStartDiff(cur, mDelegate.getWeekStart());
        int size = diff + cur.getDay() - 1;
        updateSelectPosition(size);
    }

    /**
     * 当前第几项被选中，更新平移量
     *
     * @param selectPosition 月视图被点击的position
     */
    final void updateSelectPosition(int selectPosition) {
        int line = (selectPosition + 7) / 7;
        mViewPagerTranslateY = (line - 1) * mItemHeight;
    }

    /**
     * 设置选中的周，更新位置
     *
     * @param week week
     */
    final void updateSelectWeek(int week) {
        mViewPagerTranslateY = (week - 1) * mItemHeight;
    }


    /**
     * 更新内容ContentView可平移的最大距离
     */
    void updateContentViewTranslateY() {
        Calendar calendar = mDelegate.mIndexCalendar;
        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
            mContentViewTranslateY = 5 * mItemHeight;
        } else {
            mContentViewTranslateY = CalendarUtil.getMonthViewHeight(calendar.getYear(), calendar.getMonth(), mItemHeight, mDelegate.getWeekStart())
                    - mItemHeight;
        }
        //已经显示周视图，则需要动态平移contentView的高度
        if (mWeekPager.getVisibility() == VISIBLE) {
            if (mContentView == null)
                return;
            mContentView.setTranslationY(-mContentViewTranslateY);
        }
    }

    /**
     * 更新日历项高度
     */
    final void updateCalendarItemHeight() {
        mItemHeight = mDelegate.getCalendarItemHeight();
        if (mContentView == null)
            return;
        Calendar calendar = mDelegate.mIndexCalendar;
        updateSelectWeek(CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart()));
        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
            mContentViewTranslateY = 5 * mItemHeight;
        } else {
            mContentViewTranslateY = CalendarUtil.getMonthViewHeight(calendar.getYear(), calendar.getMonth(),
                    mItemHeight, mDelegate.getWeekStart()) - mItemHeight;
        }
        translationViewPager();
        if (mWeekPager.getVisibility() == VISIBLE) {
            mContentView.setTranslationY(-mContentViewTranslateY);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mDelegate.isShowYearSelectedLayout) {
            return false;
        }

        if (mContentView == null) {
            return false;
        }

        int action = event.getAction();
        float y = event.getY();
        mVelocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                int index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                mLastY = downY = y;
                return true;
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int indexx = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, indexx);
                if (mActivePointerId == 0) {
                    //核心代码：就是让下面的 dy = y- mLastY == 0，避免抖动
                    mLastY = MotionEventCompat.getY(event, mActivePointerId);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
                if (mGestureMode == GESTURE_MODE_DISABLED ||
                        mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_MONTH_VIEW ||
                        mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_WEEK_VIEW) {//禁用手势，或者只显示某种视图
                    return false;
                }


                getPointerIndex(event, mActivePointerId);
                if (mActivePointerId == INVALID_POINTER) {
                    //如果切换了手指，那把mLastY换到最新手指的y坐标即可，核心就是让下面的 dy== 0，避免抖动
                    mLastY = y;
                    mActivePointerId = ACTIVE_POINTER;
                }

                float dy = y - mLastY;
                //向上滑动，并且contentView平移到最大距离，显示周视图
                if (dy < 0 && mContentView.getTranslationY() == -mContentViewTranslateY) {
                    //mContentView.onTouchEvent(event);
                    showWeek();
                    mLastY = y;
                    return false;
                }
                hideWeek();

                //向下滑动，并且contentView已经完全平移到底部
                if (dy > 0 && mContentView.getTranslationY() + dy >= 0) {
                    mContentView.setTranslationY(0);
                    translationViewPager();
                    mLastY = y;
                    return super.onTouchEvent(event);
                }

                //向上滑动，并且contentView已经平移到最大距离，则contentView平移到最大的距离
                if (dy < 0 && mContentView.getTranslationY() + dy <= -mContentViewTranslateY) {
                    mContentView.setTranslationY(-mContentViewTranslateY);
                    translationViewPager();
                    mLastY = y;
                    return super.onTouchEvent(event);
                }
                //否则按比例平移
                mContentView.setTranslationY(mContentView.getTranslationY() + dy);
                translationViewPager();
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:

            case MotionEvent.ACTION_POINTER_UP:
                int pointerIndex = getPointerIndex(event, mActivePointerId);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                mLastY = MotionEventCompat.getY(event, pointerIndex);
                break;
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAnimating) {
            return true;
        }
        if (mGestureMode == GESTURE_MODE_DISABLED) {
            return false;
        }
        if (mYearView == null ||
                mContentView == null ||
                mContentView.getVisibility() != VISIBLE) {
            return super.onInterceptTouchEvent(ev);
        }

        if (mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_MONTH_VIEW ||
                mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_WEEK_VIEW) {
            return false;
        }

        if (mYearView.getVisibility() == VISIBLE || mDelegate.isShowYearSelectedLayout) {
            return super.onInterceptTouchEvent(ev);
        }
        final int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
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
                    if (!isScrollTop()) {
                        return false;
                    }
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
        }
        return super.onInterceptTouchEvent(ev);
    }


    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1) {
            mActivePointerId = INVALID_POINTER;
        }
        return activePointerIndex;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mContentView != null && mMonthView != null) {
            int year = mDelegate.mIndexCalendar.getYear();
            int month = mDelegate.mIndexCalendar.getMonth();

            int monthHeight = CalendarUtil.getMonthViewHeight(year, month,
                    mDelegate.getCalendarItemHeight(),
                    mDelegate.getWeekStart()) + CalendarUtil.dipToPx(getContext(),41);
            int height = getHeight();

            if(monthHeight >= height && mMonthView.getHeight() >0 ){
                height = monthHeight;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(monthHeight +
                        CalendarUtil.dipToPx(getContext(), 41) +
                        mDelegate.getWeekBarHeight() , MeasureSpec.EXACTLY);
            }else if(monthHeight < height && mMonthView.getHeight()>0){
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
            }

            int h = height - mItemHeight
                    - (mDelegate != null ? mDelegate.getWeekBarHeight() :
                    CalendarUtil.dipToPx(getContext(), 40))
                    - CalendarUtil.dipToPx(getContext(), 1);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int heightSpec = MeasureSpec.makeMeasureSpec(h,
                    MeasureSpec.EXACTLY);
            mContentView.measure(widthMeasureSpec, heightSpec);
            mContentView.layout(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(), mContentView.getBottom());
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMonthView = (MonthViewPager) findViewById(R.id.vp_month);
        mWeekPager = (WeekViewPager) findViewById(R.id.vp_week);
        mContentView = (ViewGroup) findViewById(mContentViewId);
        mYearView = (YearViewSelectLayout) findViewById(R.id.selectLayout);
        if (mContentView != null) {
            mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
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
    public final boolean isExpand() {
        return mContentView == null || mMonthView.getVisibility() == VISIBLE;
    }


    /**
     * 展开
     *
     * @return 展开是否成功
     */
    public boolean expand() {
        if (isAnimating ||
                mCalendarShowMode == CALENDAR_SHOW_MODE_ONLY_WEEK_VIEW ||
                mContentView == null)
            return false;
        if (mMonthView.getVisibility() != VISIBLE) {
            mWeekPager.setVisibility(GONE);
            onShowMonthView();
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
     *
     * @return 成功或者失败
     */
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
    final void initStatus() {
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
        } else {
            if (mDelegate.mViewChangeListener == null) {
                return;
            }
            post(new Runnable() {
                @Override
                public void run() {
                    mDelegate.mViewChangeListener.onViewChange(true);
                }
            });
        }
    }

    /**
     * 隐藏周视图
     */
    private void hideWeek() {
        onShowMonthView();
        mWeekPager.setVisibility(GONE);
        mMonthView.setVisibility(VISIBLE);
    }

    /**
     * 显示周视图
     */
    private void showWeek() {
        onShowWeekView();
        mWeekPager.getAdapter().notifyDataSetChanged();
        mWeekPager.setVisibility(VISIBLE);
        mMonthView.setVisibility(INVISIBLE);
    }

    /**
     * 周视图显示事件
     */
    private void onShowWeekView() {
        if (mWeekPager.getVisibility() == VISIBLE) {
            return;
        }
        if (mDelegate.mViewChangeListener != null) {
            mDelegate.mViewChangeListener.onViewChange(false);
        }
    }


    /**
     * 周视图显示事件
     */
    private void onShowMonthView() {
        if (mMonthView.getVisibility() == VISIBLE) {
            return;
        }
        if (mDelegate.mViewChangeListener != null) {
            mDelegate.mViewChangeListener.onViewChange(true);
        }
    }

    /**
     * ContentView是否滚动到顶部 如果完全不适合，就复写这个方法
     *
     * @return 是否滚动到顶部
     */
    protected boolean isScrollTop() {
        if (mContentView instanceof CalendarScrollView) {
            return ((CalendarScrollView) mContentView).isScrollToTop();
        }
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
    @SuppressLint("NewApi")
    final void hideContentView() {
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
    @SuppressLint("NewApi")
    final void showContentView() {
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


    @SuppressWarnings("unused")
    private int getCalendarViewHeight() {
        return mMonthView.getVisibility() == VISIBLE ? mDelegate.getWeekBarHeight() + mMonthView.getHeight() :
                mDelegate.getWeekBarHeight() + mDelegate.getCalendarItemHeight();
    }

    /**
     * 如果有十分特别的ContentView，可以自定义实现这个接口
     */
    public interface CalendarScrollView {
        /**
         * 是否滚动到顶部
         *
         * @return 是否滚动到顶部
         */
        boolean isScrollToTop();
    }
}
