package com.haibin.calendarview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by haibin
 * on 2017/2/7.
 */
@SuppressWarnings("unused")
public class CalendarView extends FrameLayout {
    private ViewPager mViewPager;
    private List<Calendar> mSchemeDate;
    private OnDateChangeListener mListener;
    private OnDateSelectedListener mDateSelectedListener;
    private SelectLayout mSelectLayout;
    private LinearLayout mLinearWeek;
    private int mCurYear, mCurMonth, mCurDay;
    private int mCurDayColor, mSchemeThemeColor, mWeekBackground, mWeekTextColor;

    public CalendarView(@NonNull Context context) {
        super(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCurDayColor = array.getColor(R.styleable.CalendarView_current_day_color, Color.RED);
        mSchemeThemeColor = array.getColor(R.styleable.CalendarView_scheme_theme_color, Color.RED);
        mWeekBackground = array.getColor(R.styleable.CalendarView_week_background, Color.WHITE);
        mWeekTextColor = array.getColor(R.styleable.CalendarView_week_text_color, Color.RED);
        array.recycle();
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_calendar_view_mvp, this, true);
        this.mViewPager = (ViewPager) findViewById(R.id.vp_calendar);
        this.mLinearWeek = (LinearLayout) findViewById(R.id.ll_week);
        mSelectLayout = (SelectLayout) findViewById(R.id.selectLayout);
        mLinearWeek.setBackgroundColor(mWeekBackground);
        for (int i = 0; i < mLinearWeek.getChildCount(); i++) {
            ((TextView) mLinearWeek.getChildAt(i)).setTextColor(mWeekTextColor);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    int year = position / 12 + 2010;
                    int month = position % 12 + 1;
                    int[] lunar = LunarCalendar.solarToLunar(year, month, 1);
                    mListener.onDateChange(year, month, 1, LunarCalendar.numToChineseDay(lunar[2]), "");
                }
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
                    mListener.onYearChange(position + 2010);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int position;
        Calendar calendar = new Calendar();

        Date d = new Date();
        calendar.setYear(Util.getDate("yyyy", d));
        calendar.setMonth(Util.getDate("MM", d));
        calendar.setDay(Util.getDate("dd", d));

        mCurYear = calendar.getYear();
        mCurMonth = calendar.getMonth();
        mCurDay = calendar.getDay();
        int y = calendar.getYear() - 2010;
        position = 12 * y + calendar.getMonth() - 1;
        CalendarViewPagerAdapter adapter = new CalendarViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);

        mSelectLayout.setOnMonthSelectedListener(new MonthRecyclerView.OnMonthSelectedListener() {
            @Override
            public void onMonthSelected(int year, int month) {
                int position = 12 * (year - 2010) + month - 1;
                closeSelectLayout(position);
            }
        });
    }

    public int getCurDay() {
        return mCurDay;
    }

    public int getCurMonth() {
        return mCurMonth;
    }

    public int getCurYear() {
        return mCurYear;
    }

    public void showSelectLayout(final int year) {
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

    public void closeSelectLayout(final int position) {
        mSelectLayout.setVisibility(GONE);
        mLinearWeek.setVisibility(VISIBLE);
        mViewPager.setVisibility(VISIBLE);
        mViewPager.setCurrentItem(position, true);
        mLinearWeek.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLinearWeek.setVisibility(VISIBLE);
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

    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.mListener = listener;
    }

    private  class CalendarViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 480;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int year = position / 12 + 2010;
            int month = position % 12 + 1;
            CalendarCardView view = new CalendarCardView(getContext(), null);
            view.setScheme(mSchemeDate);
            view.setOnDateSelectedListener(mDateSelectedListener);
            view.setCurrentDate(year, month);
            view.setOnDateChangeListener(mListener);
            view.setStyle(mSchemeThemeColor, mCurDayColor);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((CalendarCardView) object);
        }

    }

    public void setSchemeDate(List<Calendar> mSchemeDate) {
        this.mSchemeDate = mSchemeDate;
        mSelectLayout.setSchemes(mSchemeDate);
        for (int i = 0; i < mViewPager.getChildCount(); i++) {
            CalendarCardView view = (CalendarCardView) mViewPager.getChildAt(i);
            view.setScheme(mSchemeDate);
        }
    }

    public void setStyle(int schemeThemeColor, int selectLayoutBackground, int lineBg) {
        this.mSchemeThemeColor = schemeThemeColor;
        mSelectLayout.setSchemeColor(mSchemeThemeColor);
        mSelectLayout.setBackgroundColor(selectLayoutBackground);
        findViewById(R.id.line).setBackgroundColor(lineBg);
        mSelectLayout.update();
    }

    public void update() {
        for (int i = 0; i < mViewPager.getChildCount(); i++) {
            CalendarCardView view = (CalendarCardView) mViewPager.getChildAt(i);
            view.setStyle(mSchemeThemeColor, mCurDayColor);
            view.getAdapter().notifyDataSetChanged();
        }
    }

    public void setWeekStyle(int mWeekBackground, int mWeekTextColor) {
        this.mWeekBackground = mWeekBackground;
        this.mWeekTextColor = mWeekTextColor;
        mLinearWeek.setBackgroundColor(mWeekBackground);
        for (int i = 0; i < mLinearWeek.getChildCount(); i++) {
            ((TextView) mLinearWeek.getChildAt(i)).setTextColor(mWeekTextColor);
        }
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mDateSelectedListener = listener;
    }
}
