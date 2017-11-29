package com.haibin.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.Date;

/**
 * Google规范化的属性委托
 */
class CustomCalendarViewDelegate implements CalendarView.CalendarViewDelegate {

    /**
     * 各种字体颜色，看名字知道对应的地方
     */
    private int mCurDayTextColor, mWeekTextColor,
            mSchemeTextColor,
            mSchemeLunarTextColor,
            mOtherMonthTextColor,
            mCurrentMonthTextColor,
            mSelectedTextColor,
            mSelectedLunarTextColor,
            mCurMonthLunarTextColor,
            mOtherMonthLunarTextColor;

    /**
     * 标记的主题色和选中的主题色
     */
    private int mSchemeThemeColor, mSelectedThemeColor;

    /**
     * 星期栏的背景
     */
    private int mWeekBackground;

    /**
     * 自定义的日历路径
     */
    private String mCalendarCardViewClass;

    /**
     * 自定义周视图路径
     */
    private String mWeekViewClass;

    /**
     * 最小年份和最大年份
     */
    private int mMinYear, mMaxYear;

    /**
     * 日期和农历文本大小
     */
    private int mDayTextSize, mLunarTextSize;

    /**
     * 日历卡的项高度
     */
    private int mCalendarItemHeight;

    /**
     * 今天的日子
     */
    private Calendar mCurrentDate;


    CustomCalendarViewDelegate(Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCurDayTextColor = array.getColor(R.styleable.CalendarView_current_day_text_color, Color.RED);
        mSchemeTextColor = array.getColor(R.styleable.CalendarView_scheme_text_color, 0xFFFFFFFF);
        mSchemeLunarTextColor = array.getColor(R.styleable.CalendarView_scheme_lunar_text_color, 0xFFe1e1e1);
        mSchemeThemeColor = array.getColor(R.styleable.CalendarView_scheme_theme_color, 0x50CFCFCF);
        mCalendarCardViewClass = array.getString(R.styleable.CalendarView_calendar_card_view);
        mWeekViewClass = array.getString(R.styleable.CalendarView_week_view);

        mWeekBackground = array.getColor(R.styleable.CalendarView_week_background, Color.WHITE);
        mWeekTextColor = array.getColor(R.styleable.CalendarView_week_text_color, Color.BLACK);

        mSelectedThemeColor = array.getColor(R.styleable.CalendarView_selected_theme_color, 0x50CFCFCF);
        mSelectedTextColor = array.getColor(R.styleable.CalendarView_selected_text_color, 0xFF111111);
        mSelectedLunarTextColor = array.getColor(R.styleable.CalendarView_selected_lunar_text_color, 0xFF111111);
        mCurrentMonthTextColor = array.getColor(R.styleable.CalendarView_current_month_text_color, 0xFF111111);
        mOtherMonthTextColor = array.getColor(R.styleable.CalendarView_other_month_text_color, 0xFFe1e1e1);

        mCurMonthLunarTextColor = array.getColor(R.styleable.CalendarView_current_month_lunar_text_color, Color.GRAY);
        mOtherMonthLunarTextColor = array.getColor(R.styleable.CalendarView_other_month_lunar_text_color, Color.GRAY);
        mMinYear = array.getInt(R.styleable.CalendarView_min_year, 2010);
        mMaxYear = array.getInt(R.styleable.CalendarView_max_year, 2050);

        mDayTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_day_text_size, Util.dipToPx(context, 16));
        mLunarTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_lunar_text_size, Util.dipToPx(context, 10));
        mCalendarItemHeight = (int) array.getDimension(R.styleable.CalendarView_calendar_height, Util.dipToPx(context, 56));
        if (mMinYear <= 1900) mMaxYear = 1900;
        if (mMaxYear >= 2099) mMaxYear = 2099;
        array.recycle();
        init();
    }

    private void init(){
        mCurrentDate = new Calendar();
        Date d = new Date();
        mCurrentDate.setYear(Util.getDate("yyyy", d));
        mCurrentDate.setMonth(Util.getDate("MM", d));
        mCurrentDate.setDay(Util.getDate("dd", d));
    }

    @Override
    public int getCurDayTextColor() {
        return mCurDayTextColor;
    }

    @Override
    public int getWeekTextColor() {
        return mWeekTextColor;
    }

    @Override
    public int getSchemeTextColor() {
        return mSchemeTextColor;
    }

    @Override
    public int getSchemeLunarTextColor() {
        return mSchemeLunarTextColor;
    }

    @Override
    public int getOtherMonthTextColor() {
        return mOtherMonthTextColor;
    }

    @Override
    public int getCurrentMonthTextColor() {
        return mCurrentMonthTextColor;
    }

    @Override
    public int getSelectedTextColor() {
        return mSelectedTextColor;
    }

    @Override
    public int getSelectedLunarTextColor() {
        return mSelectedLunarTextColor;
    }

    @Override
    public int getCurrentMonthLunarTextColor() {
        return mCurMonthLunarTextColor;
    }

    @Override
    public int getOtherMonthLunarTextColor() {
        return mOtherMonthLunarTextColor;
    }

    @Override
    public int getSchemeThemeColor() {
        return mSchemeThemeColor;
    }

    @Override
    public int getSelectedThemeColor() {
        return mSelectedThemeColor;
    }

    @Override
    public int getWeekBackground() {
        return mWeekBackground;
    }

    @Override
    public String getCalendarCardViewClass() {
        return mCalendarCardViewClass;
    }

    @Override
    public String getWeekViewClass() {
        return mWeekViewClass;
    }

    @Override
    public int getMinYear() {
        return mMinYear;
    }

    @Override
    public int getMaxYear() {
        return mMaxYear;
    }

    @Override
    public int getDayTextSize() {
        return mDayTextSize;
    }

    @Override
    public int getLunarTextSize() {
        return mLunarTextSize;
    }

    @Override
    public int getCalendarItemHeight() {
        return mCalendarItemHeight;
    }

    @Override
    public void setMinYear(int minYear) {
        this.mMinYear = minYear;
    }

    @Override
    public void setMaxYear(int maxYear) {
        this.mMaxYear = maxYear;
    }

    @Override
    public void setTextColor(int curDayTextColor, int curMonthTextColor, int otherMonthTextColor, int curMonthLunarTextColor, int otherMonthLunarTextColor) {
        mCurrentMonthTextColor = curMonthTextColor;
        mCurMonthLunarTextColor = curMonthLunarTextColor;
        mOtherMonthLunarTextColor = otherMonthLunarTextColor;
    }

    @Override
    public void setSchemeColor(int schemeColor, int schemeTextColor, int schemeLunarTextColor) {
        this.mSchemeThemeColor = schemeColor;
        this.mSchemeTextColor = schemeTextColor;
        this.mSchemeLunarTextColor = schemeLunarTextColor;
    }

    @Override
    public void setSelectColor(int selectedColor, int selectedTextColor, int selectedLunarTextColor) {
        this.mSelectedThemeColor = selectedColor;
        this.mSelectedTextColor = selectedTextColor;
        this.mSelectedLunarTextColor = selectedLunarTextColor;
    }

    @Override
    public void setDayTextSize(float calendarTextSize, float lunarTextSize) {
        mDayTextSize = (int) calendarTextSize;
        mLunarTextSize = (int) lunarTextSize;
    }

    @Override
    public Calendar getCurrentDay() {
        return mCurrentDate;
    }
}
