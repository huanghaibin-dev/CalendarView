package com.haibin.calendarview;

import java.io.Serializable;

/**
 * Created by haibin
 * on 2017/3/6.
 */
@SuppressWarnings("unused")
class Month implements Serializable {
    private int diff;
    private int count;
    private int month;
    private int year;

    int getDiff() {
        return diff;
    }

    void setDiff(int diff) {
        this.diff = diff;
    }

    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    int getMonth() {
        return month;
    }

    void setMonth(int month) {
        this.month = month;
    }

    int getYear() {
        return year;
    }

    void setYear(int year) {
        this.year = year;
    }
}
