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
@SuppressWarnings("unused")
final class Util {
    @SuppressLint("SimpleDateFormat")
    static int getDate(String formatStr, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return Integer.parseInt(format.format(date));
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
    @SuppressWarnings("unused")
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
     * 获取某年第几周起始第一天，如遇到前一个月，则为1号
     *
     * @param year       年
     * @param weekInYear 某年第几周
     * @return Calendar
     */
    static Calendar getFirstCalendarFormWeekInYear(int year, int weekInYear) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, 0, 1);
        int diff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//1月第一天为星期几,星期天 == 0，也就是偏移量
        int count = 0;
        int diy = (weekInYear - 1) * 7 - diff + 1;
        int month = 0;
        int day = diy;
        for (int i = 1; i <= 12; i++) {
            int monthDayCount = getMonthDaysCount(year, i);
            count += monthDayCount;
            if (diy <= count) {
                month = i;
                break;
            }else {
                day -= monthDayCount;
            }

        }
        if(day <= 0){
            day = 1;
        }
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setLunar(LunarCalendar.getLunarText(calendar.getYear(), calendar.getMonth(), calendar.getDay()));
        return calendar;
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
