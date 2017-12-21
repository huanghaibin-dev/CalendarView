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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一些辅助计算工具
 */
final class Util {

    private static final long ONE_DAY = 1000 * 3600 * 24;

    @SuppressLint("SimpleDateFormat")
    static int getDate(String formatStr, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return Integer.parseInt(format.format(date));
    }

    /**
     * 判断一个日期是否是周末，即周六日
     *
     * @param calendar calendar
     * @return 判断一个日期是否是周末，即周六日
     */
    static boolean isWeekend(Calendar calendar) {
        int week = getWeekFormCalendar(calendar);
        return week == 0 || week == 6;
    }

    /**
     * 获取某月的天数
     *
     * @param year  年
     * @param month 月
     * @return 某月的天数
     */
    static int getMonthDaysCount(int year, int month) {
        int count = 0;
        //判断大月份
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            count = 31;
        }

        //判断小月
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            count = 30;
        }

        //判断平年与闰年
        if (month == 2) {
            if (isLeapYear(year)) {
                count = 29;
            } else {
                count = 28;
            }
        }
        return count;
    }


    /**
     * 是否是闰年
     *
     * @param year year
     * @return return
     */
    private static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

    /**
     * 获取某年的天数
     *
     * @param year 某一年
     * @return 366 or 365
     */
    @SuppressWarnings("unused")
    private static int getYearCount(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

    @SuppressWarnings("unused")
    @SuppressLint("WrongConstant")
    @Deprecated
    static int getCardHeight(int year, int month) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1);
        int firstDayOfWeek = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0
        int mDaysCount = Util.getMonthDaysCount(year, month);
        date.set(year, month - 1, mDaysCount);
        int mLastCount = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月最后一天为星期几,星期天 == 0
        int nextMonthDaysOffset = 6 - mLastCount;//下个月的日偏移天数
        //return 6 * BaseCalendarCardView.mItemHeight;
        return 0;
        //return (firstDayOfWeek + mDaysCount + nextMonthDaysOffset) / 7 * CalendarCardViewV2.mItemHeight;
    }

    /**
     * 获取某个月有多少个星期
     */
    @SuppressWarnings("unused")
    static int getWeekCount(int year, int month) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1);
        int firstDayOfWeek = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0
        int mDaysCount = Util.getMonthDaysCount(year, month);
        date.set(year, month - 1, mDaysCount);
        int mLastCount = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月最后一天为星期几,星期天 == 0
        int nextMonthDaysOffset = 6 - mLastCount;//下个月的日偏移天数
        return (firstDayOfWeek + mDaysCount + nextMonthDaysOffset) / 7;
    }

    /**
     * 获取某年第几天是第几个月
     *
     * @param year      年
     * @param dayInYear 某年第几天
     * @return 第几个月
     */
    static int getMonthFromDayInYear(int year, int dayInYear) {
        int count = 0;
        for (int i = 1; i <= 12; i++) {
            count += getMonthDaysCount(year, i);
            if (dayInYear <= count)
                return i;
        }
        return 0;
    }


    /**
     * 获取某天在该月的第几周
     *
     * @param calendar calendar
     * @return 获取某天在该月的第几周
     */
    static int getWeekFromDayInMonth(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, 1);
        int diff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//该月第一天为星期几,星期天 == 0，也就是偏移量
        return (calendar.getDay() + diff - 1) / 7 + 1;
    }

    /**
     * 获取某个日期是星期几
     *
     * @param calendar 某个日期
     * @return 返回某个日期是星期几
     */
    static int getWeekFormCalendar(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        return date.get(java.util.Calendar.DAY_OF_WEEK) - 1;
    }


    /**
     * 获取某年第几周是在第几个月
     *
     * @param year       年
     * @param weekInYear 某年第几周
     * @return 第几个月
     */
    static int getMonthFromWeekFirstDayInYear(int year, int weekInYear) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, 0, 1);
        int diff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//1月第一天为星期几,星期天 == 0，也就是偏移量
        int count = 0;
        int diy = (weekInYear - 1) * 7 - diff + 1;
        for (int i = 1; i <= 12; i++) {
            count += getMonthDaysCount(year, i);
            if (diy <= count)
                return i;
        }
        return 0;
    }



    /**
     * 获取两个年份之间一共有多少周
     *
     * @param minYear minYear
     * @param maxYear maxYear
     * @return 周数
     */
    static int getWeekCountBetweenYearAndYear(int minYear, int maxYear) {
        if (minYear > maxYear)
            return 0;
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, 0, 1);//1月1日
        int preDiff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//1月第一天为星期几,星期天 == 0，也就是偏移量
        date.set(maxYear, 11, 31);//12月31日
        int nextDiff = 7 - date.get(java.util.Calendar.DAY_OF_WEEK);//1月第一天为星期几,星期天 == 0，也就是偏移量
        int count = preDiff + nextDiff;
        for (int i = minYear; i <= maxYear; i++) {
            count += getYearCount(i);
        }
        return count / 7;
    }


    /**
     * 获取两个年份之间一共有多少周
     *
     * @param minYear      minYear 最小年份
     * @param minYearMonth maxYear 最小年份月份
     * @param maxYear      maxYear 最大年份
     * @param maxYearMonth maxYear 最大年份月份
     * @return 周数
     */
    static int getWeekCountBetweenYearAndYear(int minYear, int minYearMonth, int maxYear, int maxYearMonth) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, 1);
        long minTime = date.getTimeInMillis();//给定时间戳
        int preDiff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//1月第一天为星期几,星期天 == 0，也就是偏移量
        date.set(maxYear, maxYearMonth - 1, getMonthDaysCount(maxYear, maxYearMonth));
        long maxTime = date.getTimeInMillis();//给定时间戳
        int nextDiff = 7 - date.get(java.util.Calendar.DAY_OF_WEEK);//1月第一天为星期几,星期天 == 0，也就是偏移量
        int count = preDiff + nextDiff;
        int c = (int) ((maxTime - minTime) / ONE_DAY) + 1;
        count += c;
        return count / 7;
    }

    /**
     * 根据日期获取两个年份中第几周
     *
     * @param calendar calendar
     * @param minYear  minYear
     * @return 返回两个年份中第几周
     */
    static int getWeekFromCalendarBetweenYearAndYear(Calendar calendar, int minYear, int minYearMonth) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, 0, 1);//1月1日
        long firstTime = date.getTimeInMillis();//获得起始时间戳
        int preDiff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//1月第一天为星期几,星期天 == 0，也就是偏移量
        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curTime = date.getTimeInMillis();//给定时间戳
        int c = (int) ((curTime - firstTime) / ONE_DAY);
        int count = preDiff + c;
        int weekDiff = getWeekCountDiff(minYear, minYearMonth);
        return count / 7 - weekDiff;
    }


    /**
     * 是否在日期范围內
     *
     * @param calendar     calendar
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @return 是否在日期范围內
     */
    static boolean isCalendarInRange(Calendar calendar, int minYear, int minYearMonth, int maxYear, int maxYearMonth) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(minYear, minYearMonth - 1, 1);
        long minTime = c.getTimeInMillis();
        c.set(maxYear, maxYearMonth - 1, getMonthDaysCount(maxYear, maxYearMonth));
        long maxTime = c.getTimeInMillis();
        c.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime >= minTime && curTime <= maxTime;
    }

    static boolean isCalendarInRange(Calendar calendar, CustomCalendarViewDelegate delegate) {
        return isCalendarInRange(calendar, delegate.getMinYear(), delegate.getMinYearMonth(),
                delegate.getMaxYear(), delegate.getMaxYearMonth());
    }

    /**
     * 是否在日期范围內
     *
     * @param year         year
     * @param month        month
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @return 是否在日期范围內
     */
    static boolean isMonthInRange(int year, int month, int minYear, int minYearMonth, int maxYear, int maxYearMonth) {
        return !(year < minYear || year > maxYear) &&
                !(year == minYear && month < minYearMonth) &&
                !(year == maxYear && month > maxYearMonth);
    }

    /**
     * 根据星期数和最小年份推算出该星期的第一天
     *
     * @param minYear      最小年份如2017
     * @param minYearMonth maxYear 最小年份月份，like : 2017-07
     * @param week         从最小年份1月1日开始的第几周
     * @return 该星期的第一天日期
     */
    static Calendar getFirstCalendarFromWeekCount(int minYear, int minYearMonth, int week) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, 0, 1);//1月1日
        long firstTime = date.getTimeInMillis();//获得起始时间戳
        int dayCount = (week + getWeekCountDiff(minYear, minYearMonth)) * 7 + 1;
        long timeCount = dayCount * ONE_DAY + firstTime;
        date.setTimeInMillis(timeCount);
        Calendar calendar = new Calendar();
        calendar.setYear(date.get(java.util.Calendar.YEAR));
        calendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        calendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));
        return calendar;
    }


    /**
     * 获取星期偏移了多少周
     *
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @return 获取星期偏移了多少周
     */
    static int getWeekCountDiff(int minYear, int minYearMonth) {
        if (minYearMonth == 1) {
            return -1;
        }
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, 0, 1);//1月1日
        long firstTime = date.getTimeInMillis();//获得起始时间戳
        int preDiff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//1月第一天为星期几,星期天 == 0，也就是偏移量
        date.set(minYear, minYearMonth - 1, 1);
        long minTime = date.getTimeInMillis();//获得时间戳
        int nextDiff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//minYearMonth月第一天为星期几,星期天 == 0，也就是偏移量
        int c = (int) ((minTime - firstTime) / ONE_DAY) - 1;
        int count = preDiff + c - nextDiff;
        return count / 7;
    }

    /**
     * 从一个日期Calendar中获取所处在一年中的第几个星期
     *
     * @param calendar 日期Calendar
     * @return 0 —— 53
     */
    static int getWeekFromCalendarInYear(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), 0, 1);
        int count = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//前补位
        for (int i = 1; i < calendar.getMonth(); i++) {
            count += getMonthDaysCount(calendar.getYear(), i);
        }
        count += calendar.getDay() - 1;
        return count / 7 + 1;
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
