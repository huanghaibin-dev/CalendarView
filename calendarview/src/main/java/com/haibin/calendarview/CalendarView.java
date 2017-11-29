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
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 日历布局
 * 各个类使用包权限，避免不必要的public
 */
@SuppressWarnings("unused")
public class CalendarView extends FrameLayout {

    private CalendarViewDelegate mDelegate;

    /**
     * 自定义自适应高度的ViewPager
     */
    private WrapViewPager mViewPager;

    /**
     * 日历周视图
     */
    private WeekViewPager mWeekPager;

    /**
     * 标记的日期
     */
    private List<Calendar> mSchemeDate;

    /**
     * 日期切换监听
     */
    private OnDateChangeListener mListener;

    /**
     * 日期被选中监听
     */
    private OnDateSelectedListener mDateSelectedListener;

    /**
     * 内部日期切换监听，用于内部更新计算
     */
    private OnInnerDateSelectedListener mInnerListener;

    /**
     * 月份快速选取
     */
    private MonthSelectLayout mSelectLayout;

    /**
     * 星期栏
     */
    private LinearLayout mLinearWeek;

    /**
     * 日历外部收缩布局
     */
    CalendarLayout mParentLayout;

    /**
     * 保存选中的日期
     */
    private Calendar mSelectedCalendar;


    public CalendarView(@NonNull Context context) {
        this(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDelegate = new CustomCalendarViewDelegate(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.cv_layout_calendar_view, this, true);

        this.mWeekPager = (WeekViewPager) findViewById(R.id.vp_week);
        this.mWeekPager.setup(mDelegate);

        this.mLinearWeek = (LinearLayout) findViewById(R.id.ll_week);
        mSelectLayout = (MonthSelectLayout) findViewById(R.id.selectLayout);
        mLinearWeek.setBackgroundColor(mDelegate.getWeekBackground());
        for (int i = 0; i < mLinearWeek.getChildCount(); i++) {
            ((TextView) mLinearWeek.getChildAt(i)).setTextColor(mDelegate.getWeekTextColor());
        }
        this.mViewPager = (WrapViewPager) findViewById(R.id.vp_calendar);
        this.mViewPager.setup(mDelegate);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar calendar = new Calendar();
                calendar.setYear(position / 12 + mDelegate.getMinYear());
                calendar.setMonth(position % 12 + 1);
                calendar.setDay(1);
                calendar.setCurrentMonth(calendar.getMonth() == mDelegate.getCurrentDay().getMonth());
                calendar.setLunar(LunarCalendar.numToChineseDay(LunarCalendar.solarToLunar(calendar.getYear(), calendar.getMonth(), 1)[2]));

                if (mListener != null) {
                    mListener.onDateChange(mSelectedCalendar);
                }

                if (mParentLayout == null || mViewPager.getVisibility() == INVISIBLE || mWeekPager.getVisibility() == VISIBLE) {
                    return;
                }
                if (!calendar.isCurrentMonth()) {
                    mSelectedCalendar = calendar;
                } else {
                    mSelectedCalendar = mDelegate.getCurrentDay();
                }

                BaseCalendarCardView view = (BaseCalendarCardView) mViewPager.findViewWithTag(position);
                if (view != null) {
                    int index = view.getSelectedIndex(mSelectedCalendar);
                    view.mCurrentItem = index;
                    if (index >= 0) {
                        mParentLayout.setSelectPosition(index);
                    }
                    view.invalidate();
                }
                mWeekPager.updateSelected(mSelectedCalendar);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSelectLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onYearChange(position + mDelegate.getMinYear());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mInnerListener = new OnInnerDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar) {
                if (calendar.getMonth() == mDelegate.getCurrentDay().getMonth() && mViewPager.getCurrentItem() != mCurrentViewItem) {
                    return;
                }
                mSelectedCalendar = calendar;
                mWeekPager.updateSelected(mSelectedCalendar);
                for (int i = 0; i < mViewPager.getChildCount(); i++) {
                    BaseCalendarCardView view = (BaseCalendarCardView) mViewPager.getChildAt(i);
                    view.setSelectedCalendar(mSelectedCalendar);
                    view.invalidate();
                }
            }

            @Override
            public void onWeekSelected(Calendar calendar) {
                mSelectedCalendar = calendar;
                mWeekPager.mSelectedCalendar = calendar;
                int y = calendar.getYear() - mDelegate.getMinYear();
                int position = 12 * y + mSelectedCalendar.getMonth() - 1;
                mViewPager.setCurrentItem(position);
                for (int i = 0; i < mViewPager.getChildCount(); i++) {
                    BaseCalendarCardView view = (BaseCalendarCardView) mViewPager.getChildAt(i);
                    view.setSelectedCalendar(mSelectedCalendar);
                    view.invalidate();
                }
            }
        };

        int position;
        mSelectedCalendar = mDelegate.getCurrentDay();

        int mCurYear = mSelectedCalendar.getYear();
        if (mDelegate.getMinYear() >= mCurYear) mDelegate.setMinYear(mCurYear);
        if (mDelegate.getMaxYear() <= mCurYear) mDelegate.setMaxYear(mCurYear + 2);
        mSelectLayout.setYearSpan(mDelegate.getMinYear(), mDelegate.getMaxYear());
        int mCurMonth = mSelectedCalendar.getMonth();
        int mCurDay = mSelectedCalendar.getDay();
        int y = mSelectedCalendar.getYear() - mDelegate.getMinYear();
        mCurrentViewItem = 12 * y + mSelectedCalendar.getMonth() - 1;
        CalendarViewPagerAdapter adapter = new CalendarViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mCurrentViewItem);
        mSelectLayout.setOnMonthSelectedListener(new MonthRecyclerView.OnMonthSelectedListener() {
            @Override
            public void onMonthSelected(int year, int month) {
                int position = 12 * (year - mDelegate.getMinYear()) + month - 1;
                closeSelectLayout(position);
            }
        });
        mSelectLayout.setSchemeColor(mDelegate.getSchemeThemeColor());
        mWeekPager.updateSelected(mSelectedCalendar);

    }

    private int mCurrentViewItem;

    /**
     * 获取当天
     *
     * @return 返回今天
     */
    public int getCurDay() {
        return mDelegate.getCurrentDay().getDay();
    }

    /**
     * 获取本月
     *
     * @return 返回本月
     */
    public int getCurMonth() {
        return mDelegate.getCurrentDay().getMonth();
    }

    /**
     * 获取本年
     *
     * @return 返回本年
     */
    public int getCurYear() {
        return mDelegate.getCurrentDay().getYear();
    }

    /**
     * 打开日历月份快速选择
     *
     * @param year 年
     */
    public void showSelectLayout(final int year) {
        if (mParentLayout != null && mParentLayout.mContentView != null) {
            mParentLayout.expand();
            mParentLayout.mContentView.setVisibility(GONE);
        }
        mWeekPager.setVisibility(GONE);

        mLinearWeek.animate()
                .translationY(-mLinearWeek.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLinearWeek.setVisibility(GONE);
                        mSelectLayout.setVisibility(VISIBLE);
                        mSelectLayout.init(year);
                    }
                });

        mViewPager.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(180)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mViewPager.setVisibility(GONE);
                    }
                });
    }

    /**
     * 滚动到当前
     */
    public void scrollToCurrent() {
        if (mWeekPager.getVisibility() == VISIBLE) {
            mWeekPager.scrollToCurrent(mDelegate.getCurrentDay());
            return;
        }
        mViewPager.setCurrentItem(12 * (mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear()) + mDelegate.getCurrentDay().getMonth() - 1);
    }

    /**
     * 滚动到某一年
     *
     * @param year 快速滚动的年份
     */
    public void scrollToYear(int year) {
        mViewPager.setCurrentItem(12 * (year - mDelegate.getMinYear()) + mDelegate.getCurrentDay().getMonth() - 1);
    }

    /**
     * 关闭日历布局，同时会滚动到指定的位置
     *
     * @param position 某一年
     */
    public void closeSelectLayout(final int position) {
        mSelectLayout.setVisibility(GONE);
        mLinearWeek.setVisibility(VISIBLE);
        mViewPager.setVisibility(VISIBLE);
        if (position == mViewPager.getCurrentItem()) {
            if (mListener != null) {
                Calendar calendar = new Calendar();
                calendar.setYear(position / 12 + mDelegate.getMinYear());
                calendar.setMonth(position % 12 + 1);
                calendar.setDay(1);
                calendar.setLunar(LunarCalendar.numToChineseDay(LunarCalendar.solarToLunar(calendar.getYear(), calendar.getMonth(), 1)[2]));
                mListener.onDateChange(calendar);
            }
        } else {
            mViewPager.setCurrentItem(position, true);
        }
        mLinearWeek.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLinearWeek.setVisibility(VISIBLE);
                        if (mParentLayout != null && mParentLayout.mContentView != null) {
                            mParentLayout.mContentView.setVisibility(VISIBLE);
                        }
                    }
                });
        mViewPager.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(180)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mViewPager.setVisibility(VISIBLE);

                    }
                });
    }

    /**
     * 日期改变监听器
     *
     * @param listener 监听
     */
    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置日期选中事件
     *
     * @param listener 日期选中事件
     */
    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mDateSelectedListener = listener;
    }


    /**
     * 日历卡Adapter
     */
    private class CalendarViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear());
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int year = position / 12 + mDelegate.getMinYear();
            int month = position % 12 + 1;
            BaseCalendarCardView view;
            if (TextUtils.isEmpty(mDelegate.getCalendarCardViewClass())) {
                view = new SimpleCalendarCardView(getContext());
            } else {
                try {
                    Class cls = Class.forName(mDelegate.getCalendarCardViewClass());
                    @SuppressWarnings("unchecked")
                    Constructor constructor = cls.getConstructor(Context.class);
                    view = (BaseCalendarCardView) constructor.newInstance(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            view.mParentLayout = mParentLayout;
            view.mSchemes = mSchemeDate;
            view.mListener = mListener;
            view.mDateSelectedListener = mDateSelectedListener;
            view.mInnerListener = mInnerListener;
            view.setTag(position);
            view.setCurrentDate(year, month);
            view.setSelectedCalendar(mSelectedCalendar);
            view.setup(mDelegate);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    /**
     * 初始化时初始化日历卡默认选择位置
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() != null && getParent() instanceof CalendarLayout) {
            mParentLayout = (CalendarLayout) getParent();
            mParentLayout.mItemHeight = mDelegate.getCalendarItemHeight();
            mViewPager.mParentLayout = mParentLayout;
            mWeekPager.mParentLayout = mParentLayout;
            mWeekPager.mListener = mListener;
            mWeekPager.mDateSelectedListener = mDateSelectedListener;
            mWeekPager.mInnerListener = mInnerListener;
            mParentLayout.initCalendarPosition(mSelectedCalendar);
        }
    }


    /**
     * 标记哪些日期有事件
     *
     * @param mSchemeDate mSchemeDate 通过自己的需求转换即可
     */
    public void setSchemeDate(List<Calendar> mSchemeDate) {
        this.mSchemeDate = mSchemeDate;
        mSelectLayout.setSchemes(mSchemeDate);
        mWeekPager.mSchemeDate = mSchemeDate;
        for (int i = 0; i < mViewPager.getChildCount(); i++) {
            BaseCalendarCardView view = (BaseCalendarCardView) mViewPager.getChildAt(i);
            view.mSchemes = mSchemeDate;
            view.update();
        }
        mWeekPager.update();
    }

    /**
     * 设置背景色
     *
     * @param monthLayoutBackground 月份卡片的背景色
     * @param weekBackground        星期栏背景色
     * @param lineBg                线的颜色
     */
    public void setBackground(int monthLayoutBackground, int weekBackground, int lineBg) {
        mLinearWeek.setBackgroundColor(weekBackground);
        mSelectLayout.setBackgroundColor(monthLayoutBackground);
        findViewById(R.id.line).setBackgroundColor(lineBg);
    }


    /**
     * 设置文本颜色
     *
     * @param curMonthTextColor        当前月份字体颜色
     * @param otherMonthColor          其它月份字体颜色
     * @param curMonthLunarTextColor   当前月份农历字体颜色
     * @param otherMonthLunarTextColor 其它农历字体颜色
     */
    @Deprecated
    public void setTextColor(int curMonthTextColor,
                             int otherMonthColor,
                             int curMonthLunarTextColor,
                             int otherMonthLunarTextColor) {
        mDelegate.setTextColor(mDelegate.getCurDayTextColor(), curMonthTextColor, otherMonthColor, curMonthLunarTextColor, otherMonthLunarTextColor);
    }


    /**
     * 设置文本颜色
     *
     * @param currentDayTextColor      今天字体颜色
     * @param curMonthTextColor        当前月份字体颜色
     * @param otherMonthColor          其它月份字体颜色
     * @param curMonthLunarTextColor   当前月份农历字体颜色
     * @param otherMonthLunarTextColor 其它农历字体颜色
     */
    public void setTextColor(
            int currentDayTextColor,
            int curMonthTextColor,
            int otherMonthColor,
            int curMonthLunarTextColor,
            int otherMonthLunarTextColor) {
        mDelegate.setTextColor(currentDayTextColor, curMonthTextColor, otherMonthColor, curMonthLunarTextColor, otherMonthLunarTextColor);
    }

    /**
     * 设置选择的效果
     *
     * @param selectedThemeColor 选中的标记颜色
     * @param selectedTextColor  选中的字体颜色
     */
    @Deprecated
    public void setSelectedColor(int selectedThemeColor, int selectedTextColor) {
        mDelegate.setSelectColor(selectedThemeColor, selectedTextColor, mDelegate.getSelectedLunarTextColor());
    }


    /**
     * 设置选择的效果
     *
     * @param selectedThemeColor     选中的标记颜色
     * @param selectedTextColor      选中的字体颜色
     * @param selectedLunarTextColor 选中的农历字体颜色
     */
    @Deprecated
    public void setSelectedColor(int selectedThemeColor, int selectedTextColor, int selectedLunarTextColor) {
        mDelegate.setSelectColor(selectedThemeColor, selectedTextColor, selectedLunarTextColor);
    }


    /**
     * 设置标记的色
     *
     * @param schemeColor     标记背景色
     * @param schemeTextColor 标记字体颜色
     */
    @Deprecated
    public void setSchemeColor(int schemeColor, int schemeTextColor) {
        mDelegate.setSchemeColor(schemeColor, schemeTextColor, mDelegate.getSchemeLunarTextColor());
        mSelectLayout.setSchemeColor(schemeColor);
    }

    /**
     * 设置标记的色
     *
     * @param schemeColor     标记背景色
     * @param schemeTextColor 标记字体颜色
     */
    @Deprecated
    public void setSchemeColor(int schemeColor, int schemeTextColor, int schemeLunarTextColor) {
        mDelegate.setSchemeColor(schemeColor, schemeTextColor, schemeLunarTextColor);
        mSelectLayout.setSchemeColor(schemeColor);
    }

    /**
     * 设置星期栏的背景和字体颜色
     *
     * @param weekBackground 背景色
     * @param weekTextColor  字体颜色
     */
    public void setWeeColor(int weekBackground, int weekTextColor) {
        mLinearWeek.setBackgroundColor(weekBackground);
        for (int i = 0; i < mLinearWeek.getChildCount(); i++) {
            ((TextView) mLinearWeek.getChildAt(i)).setTextColor(weekTextColor);
        }
    }


    /**
     * 更新界面，
     * 重新设置颜色等都需要调用该方法
     */
    public void update() {
        mSelectLayout.update();
        for (int i = 0; i < mViewPager.getChildCount(); i++) {
            BaseCalendarCardView view = (BaseCalendarCardView) mViewPager.getChildAt(i);
            view.setup(mDelegate);
            view.update();
        }
    }

    /**
     * 获取选择的日期
     */
    public Calendar getSelectedCalendar() {
        return mSelectedCalendar;
    }


    /**
     * 日期改变、左右切换、快速年份、月份切换
     */
    public interface OnDateChangeListener {
        void onDateChange(Calendar calendar);

        void onYearChange(int year);
    }


    /**
     * 内部日期选择，不暴露外部使用
     */
    interface OnInnerDateSelectedListener {
        void onDateSelected(Calendar calendar);

        void onWeekSelected(Calendar calendar);
    }

    /**
     * 外部日期选择事件
     */
    public interface OnDateSelectedListener {
        void onDateSelected(Calendar calendar);
    }

    /**
     * 新版本将按照google控件规范整理代码
     */
    interface CalendarViewDelegate {

        /**
         * 当天日期文本颜色
         */
        int getCurDayTextColor();

        /**
         * 星期栏文本颜色
         */
        int getWeekTextColor();

        /**
         * 标记日期文本颜色
         */
        int getSchemeTextColor();

        /**
         * 标记农历文本颜色
         */
        int getSchemeLunarTextColor();

        /**
         * 其它月份日期文本颜色
         */
        int getOtherMonthTextColor();

        /**
         * 当前月份日期文本颜色
         */
        int getCurrentMonthTextColor();

        /**
         * 选中日期文本颜色
         */
        int getSelectedTextColor();

        /**
         * 选中农历文本颜色
         */
        int getSelectedLunarTextColor();

        /**
         * 当前月份农历文本颜色
         */
        int getCurrentMonthLunarTextColor();

        /**
         * 其它月份农历文本颜色
         */
        int getOtherMonthLunarTextColor();

        /**
         * 标记的主题色
         */
        int getSchemeThemeColor();

        /**
         * 选中的主题色
         */
        int getSelectedThemeColor();


        /**
         * 星期栏的背景
         */
        int getWeekBackground();


        /**
         * 自定义的日历路径
         */
        String getCalendarCardViewClass();

        /**
         * 自定义周视图路径
         */
        String getWeekViewClass();


        /**
         * 最小年份
         */
        int getMinYear();

        void setMinYear(int minYear);

        /**
         * 最大年份
         */
        int getMaxYear();

        void setMaxYear(int maxYear);

        /**
         * 日期文本大小
         */
        int getDayTextSize();

        /**
         * 农历文本大小
         */
        int getLunarTextSize();


        /**
         * 日历卡的项高度
         */
        int getCalendarItemHeight();


        /**
         * 设置文本的颜色
         *
         * @param curDayTextColor          今天的日期文本颜色
         * @param curMonthTextColor        当前月份的日期颜色
         * @param otherMonthTextColor      其它月份的日期颜色
         * @param curMonthLunarTextColor   当前月份农历字体颜色
         * @param otherMonthLunarTextColor 其它月份农历字体颜色
         */
        void setTextColor(int curDayTextColor,
                          int curMonthTextColor,
                          int otherMonthTextColor,
                          int curMonthLunarTextColor,
                          int otherMonthLunarTextColor);

        /**
         * 设置事务标记
         *
         * @param schemeColor     标记的颜色
         * @param schemeTextColor 标记的文本颜色
         */
        void setSchemeColor(int schemeColor, int schemeTextColor, int schemeLunarTextColor);


        /**
         * 设置标记的style
         */
        void setSelectColor(int selectedColor, int selectedTextColor, int selectedLunarTextColor);


        /**
         * 设置字体大小
         *
         * @param calendarTextSize 日期大小
         * @param lunarTextSize    农历大小
         */
        void setDayTextSize(float calendarTextSize, float lunarTextSize);


        /**
         * 获取当前日期
         *
         * @return 获取当前日期
         */
        Calendar getCurrentDay();
    }
}
