package com.haibin.calendarview;


import java.io.Serializable;

/**
 * Created by haibin
 * on 2017/2/7.
 */
@SuppressWarnings("unused")
class Calendar implements Serializable {
    private int year;
    private int month;
    private int day;
    private boolean currentMonth;
    private boolean isCurrentDay;
    private boolean selected;
    private String lunar;//农历
    private String scheme;//计划，可以用来标记当天是否有任务

    int getYear() {
        return year;
    }

    void setYear(int year) {
        this.year = year;
    }

    int getMonth() {
        return month;
    }

    void setMonth(int month) {
        this.month = month;
    }

    int getDay() {
        return day;
    }

    void setDay(int day) {
        this.day = day;
    }

    boolean isCurrentMonth() {
        return currentMonth;
    }

    void setCurrentMonth(boolean currentMonth) {
        this.currentMonth = currentMonth;
    }

    boolean isCurrentDay() {
        return isCurrentDay;
    }

    void setCurrentDay(boolean currentDay) {
        isCurrentDay = currentDay;
    }

    boolean isSelected() {
        return selected;
    }

    void setSelected(boolean selected) {
        this.selected = selected;
    }

    String getLunar() {
        return lunar;
    }

    void setLunar(String lunar) {
        this.lunar = lunar;
    }

    String getScheme() {
        return scheme;
    }

    void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Calendar) {
            if (((Calendar) o).getYear() == year && ((Calendar) o).getMonth() == month && ((Calendar) o).getDay() == day)
                return true;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return year + "" + (month < 10 ? "0" + month : month) + "" + (day < 10 ? "0" + day : day);
    }
}
