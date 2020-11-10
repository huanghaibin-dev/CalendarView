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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一些日期辅助计算工具
 */
@SuppressWarnings("all")
public final class CalendarUtil {

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
    public static boolean isWeekend(Calendar calendar) {
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
    public static int getMonthDaysCount(int year, int month) {
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
     * @return 是否是闰年
     */
    public static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }


    public static int getMonthViewLineCount(int year, int month, int weekStartWith, int mode) {
        if (mode == CalendarViewDelegate.MODE_ALL_MONTH) {
            return 6;
        }
        int nextDiff = CalendarUtil.getMonthEndDiff(year, month, weekStartWith);
        int preDiff = CalendarUtil.getMonthViewStartDiff(year, month, weekStartWith);
        int monthDayCount = CalendarUtil.getMonthDaysCount(year, month);
        return (preDiff + monthDayCount + nextDiff) / 7;
    }

    /**
     * 获取月视图的确切高度
     * Test pass
     *
     * @param year       年
     * @param month      月
     * @param itemHeight 每项的高度
     * @return 不需要多余行的高度
     */
    public static int getMonthViewHeight(int year, int month, int itemHeight, int weekStartWith) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1, 12, 0, 0);
        int preDiff = getMonthViewStartDiff(year, month, weekStartWith);
        int monthDaysCount = getMonthDaysCount(year, month);
        int nextDiff = getMonthEndDiff(year, month, monthDaysCount, weekStartWith);
        return (preDiff + monthDaysCount + nextDiff) / 7 * itemHeight;
    }

    /**
     * 获取月视图的确切高度
     * Test pass
     *
     * @param year       年
     * @param month      月
     * @param itemHeight 每项的高度
     * @param weekStartWith weekStartWith
     * @param mode  mode
     * @return 不需要多余行的高度
     */
    public static int getMonthViewHeight(int year, int month, int itemHeight, int weekStartWith, int mode) {
        if (mode == CalendarViewDelegate.MODE_ALL_MONTH) {
            return itemHeight * 6;
        }
        return getMonthViewHeight(year, month, itemHeight, weekStartWith);
    }

    /**
     * 获取某天在该月的第几周,换言之就是获取这一天在该月视图的第几行,第几周，根据周起始动态获取
     * Test pass，单元测试通过
     *
     * @param calendar  calendar
     * @param weekStart 其实星期是哪一天？
     * @return 获取某天在该月的第几周 the week line in MonthView
     */
    public static int getWeekFromDayInMonth(Calendar calendar, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, 1, 12, 0, 0);
        //该月第一天为星期几,星期天 == 0
        int diff = getMonthViewStartDiff(calendar, weekStart);
        return (calendar.getDay() + diff - 1) / 7 + 1;
    }

    /**
     * 获取上一个日子
     *
     * @param calendar calendar
     * @return 获取上一个日子
     */
    public static Calendar getPreCalendar(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay(), 12, 0, 0);//

        long timeMills = date.getTimeInMillis();//获得起始时间戳

        date.setTimeInMillis(timeMills - ONE_DAY);

        Calendar preCalendar = new Calendar();
        preCalendar.setYear(date.get(java.util.Calendar.YEAR));
        preCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        preCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return preCalendar;
    }

    public static Calendar getNextCalendar(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay(), 12, 0, 0);//

        long timeMills = date.getTimeInMillis();//获得起始时间戳

        date.setTimeInMillis(timeMills + ONE_DAY);

        Calendar nextCalendar = new Calendar();
        nextCalendar.setYear(date.get(java.util.Calendar.YEAR));
        nextCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        nextCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return nextCalendar;
    }

    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期所在月视图对应的起始偏移量
     * Test pass
     *
     * @param calendar  calendar
     * @param weekStart weekStart 星期的起始
     * @return 获取日期所在月视图对应的起始偏移量 the start diff with MonthView
     */
    static int getMonthViewStartDiff(Calendar calendar, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, 1, 12, 0, 0);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return week - 1;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == CalendarViewDelegate.WEEK_START_WITH_SAT ? 0 : week;
    }


    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期所在月视图对应的起始偏移量
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期所在月视图对应的起始偏移量 the start diff with MonthView
     */
    static int getMonthViewStartDiff(int year, int month, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1, 12, 0, 0);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return week - 1;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == CalendarViewDelegate.WEEK_START_WITH_SAT ? 0 : week;
    }


    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期月份对应的结束偏移量,用于计算两个年份之间总共有多少周，不用于MonthView
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期月份对应的结束偏移量 the end diff in Month not MonthView
     */
    static int getMonthEndDiff(int year, int month, int weekStart) {
        return getMonthEndDiff(year, month, getMonthDaysCount(year, month), weekStart);
    }


    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期月份对应的结束偏移量,用于计算两个年份之间总共有多少周，不用于MonthView
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期月份对应的结束偏移量 the end diff in Month not MonthView
     */
    private static int getMonthEndDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return 7 - week;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 0 : 7 - week + 1;
        }
        return week == 7 ? 6 : 7 - week - 1;
    }

    /**
     * 获取某个日期是星期几
     * 测试通过
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
     * 获取周视图的切换默认选项位置 WeekView index
     * 测试通过 test pass
     *
     * @param calendar  calendar
     * @param weekStart weekStart
     * @return 获取周视图的切换默认选项位置
     */
    static int getWeekViewIndexFromCalendar(Calendar calendar, int weekStart) {
        return getWeekViewStartDiff(calendar.getYear(), calendar.getMonth(), calendar.getDay(), weekStart);
    }

    /**
     * 是否在日期范围內
     * 测试通过 test pass
     *
     * @param calendar     calendar
     * @param minYear      minYear
     * @param minYearDay   最小年份天
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @param maxYearDay   最大年份天
     * @return 是否在日期范围內
     */
    static boolean isCalendarInRange(Calendar calendar,
                                     int minYear, int minYearMonth, int minYearDay,
                                     int maxYear, int maxYearMonth, int maxYearDay) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(minYear, minYearMonth - 1, minYearDay);
        long minTime = c.getTimeInMillis();
        c.set(maxYear, maxYearMonth - 1, maxYearDay);
        long maxTime = c.getTimeInMillis();
        c.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime >= minTime && curTime <= maxTime;
    }

    /**
     * 获取两个日期之间一共有多少周，
     * 注意周起始周一、周日、周六
     * 测试通过 test pass
     *
     * @param minYear      minYear 最小年份
     * @param minYearMonth maxYear 最小年份月份
     * @param minYearDay   最小年份天
     * @param maxYear      maxYear 最大年份
     * @param maxYearMonth maxYear 最大年份月份
     * @param maxYearDay   最大年份天
     * @param weekStart    周起始
     * @return 周数用于WeekViewPager itemCount
     */
    public static int getWeekCountBetweenBothCalendar(int minYear, int minYearMonth, int minYearDay,
                                                      int maxYear, int maxYearMonth, int maxYearDay,
                                                      int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, minYearDay);
        long minTimeMills = date.getTimeInMillis();//给定时间戳
        int preDiff = getWeekViewStartDiff(minYear, minYearMonth, minYearDay, weekStart);

        date.set(maxYear, maxYearMonth - 1, maxYearDay);

        long maxTimeMills = date.getTimeInMillis();//给定时间戳

        int nextDiff = getWeekViewEndDiff(maxYear, maxYearMonth, maxYearDay, weekStart);

        int count = preDiff + nextDiff;

        int c = (int) ((maxTimeMills - minTimeMills) / ONE_DAY) + 1;
        count += c;
        return count / 7;
    }


    /**
     * 根据日期获取距离最小日期在第几周
     * 用来设置 WeekView currentItem
     * 测试通过 test pass
     *
     * @param calendar     calendar
     * @param minYear      minYear 最小年份
     * @param minYearMonth maxYear 最小年份月份
     * @param minYearDay   最小年份天
     * @param weekStart    周起始
     * @return 返回两个年份中第几周 the WeekView currentItem
     */
    public static int getWeekFromCalendarStartWithMinCalendar(Calendar calendar,
                                                              int minYear, int minYearMonth, int minYearDay,
                                                              int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, minYearDay);//起始日期
        long firstTimeMill = date.getTimeInMillis();//获得范围起始时间戳

        int preDiff = getWeekViewStartDiff(minYear, minYearMonth, minYearDay, weekStart);//范围起始的周偏移量

        int weekStartDiff = getWeekViewStartDiff(calendar.getYear(),
                calendar.getMonth(),
                calendar.getDay(),
                weekStart);//获取点击的日子在周视图的起始，为了兼容全球时区，最大日差为一天，如果周起始偏差weekStartDiff=0，则日期加1

        date.set(calendar.getYear(),
                calendar.getMonth() - 1,
                weekStartDiff == 0 ? calendar.getDay() + 1 : calendar.getDay());

        long curTimeMills = date.getTimeInMillis();//给定时间戳

        int c = (int) ((curTimeMills - firstTimeMill) / ONE_DAY);

        int count = preDiff + c;

        return count / 7 + 1;
    }

    /**
     * 根据星期数和最小日期推算出该星期的第一天，
     * 为了防止夏令时，导致的时间提前和延后1-2小时，导致日期出现误差1天，因此吧hourOfDay = 12
     * //测试通过 Test pass
     *
     * @param minYear      最小年份如2017
     * @param minYearMonth maxYear 最小年份月份，like : 2017-07
     * @param minYearDay   最小年份天
     * @param week         从最小年份minYear月minYearMonth 日1 开始的第几周 week > 0
     * @return 该星期的第一天日期
     */
    public static Calendar getFirstCalendarStartWithMinCalendar(int minYear, int minYearMonth, int minYearDay, int week, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(minYear, minYearMonth - 1, minYearDay, 12, 0);//

        long firstTimeMills = date.getTimeInMillis();//获得起始时间戳


        long weekTimeMills = (week - 1) * 7 * ONE_DAY;

        long timeCountMills = weekTimeMills + firstTimeMills;

        date.setTimeInMillis(timeCountMills);

        int startDiff = getWeekViewStartDiff(date.get(java.util.Calendar.YEAR),
                date.get(java.util.Calendar.MONTH) + 1,
                date.get(java.util.Calendar.DAY_OF_MONTH), weekStart);

        timeCountMills -= startDiff * ONE_DAY;
        date.setTimeInMillis(timeCountMills);

        Calendar calendar = new Calendar();
        calendar.setYear(date.get(java.util.Calendar.YEAR));
        calendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        calendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return calendar;
    }


    /**
     * 是否在日期范围内
     *
     * @param calendar calendar
     * @param delegate delegate
     * @return 是否在日期范围内
     */
    static boolean isCalendarInRange(Calendar calendar, CalendarViewDelegate delegate) {
        return isCalendarInRange(calendar,
                delegate.getMinYear(), delegate.getMinYearMonth(), delegate.getMinYearDay(),
                delegate.getMaxYear(), delegate.getMaxYearMonth(), delegate.getMaxYearDay());
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
     * 运算 calendar1 - calendar2
     * test Pass
     *
     * @param calendar1 calendar1
     * @param calendar2 calendar2
     * @return calendar1 - calendar2
     */
    public static int differ(Calendar calendar1, Calendar calendar2) {
        if (calendar1 == null) {
            return Integer.MIN_VALUE;
        }
        if (calendar2 == null) {
            return Integer.MAX_VALUE;
        }
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendar1.getYear(), calendar1.getMonth() - 1, calendar1.getDay(), 12, 0, 0);//

        long startTimeMills = date.getTimeInMillis();//获得起始时间戳

        date.set(calendar2.getYear(), calendar2.getMonth() - 1, calendar2.getDay(), 12, 0, 0);//

        long endTimeMills = date.getTimeInMillis();//获得结束时间戳

        return (int) ((startTimeMills - endTimeMills) / ONE_DAY);
    }

    /**
     * 比较日期大小
     *
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @param minYearDay   minYearDay
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @param maxYearDay   maxYearDay
     * @return <0 0 >0
     */
    public static int compareTo(int minYear, int minYearMonth, int minYearDay,
                                int maxYear, int maxYearMonth, int maxYearDay) {
        Calendar first = new Calendar();
        first.setYear(minYear);
        first.setMonth(minYearMonth);
        first.setDay(minYearDay);

        Calendar second = new Calendar();
        second.setYear(maxYear);
        second.setMonth(maxYearMonth);
        second.setDay(maxYearDay);
        return first.compareTo(second);
    }

    /**
     * 为月视图初始化日历
     *
     * @param year        year
     * @param month       month
     * @param currentDate currentDate
     * @param weekStar    weekStar
     * @return 为月视图初始化日历项
     */
    static List<Calendar> initCalendarForMonthView(int year, int month, Calendar currentDate, int weekStar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(year, month - 1, 1);

        int mPreDiff = getMonthViewStartDiff(year, month, weekStar);//获取月视图其实偏移量

        int monthDayCount = getMonthDaysCount(year, month);//获取月份真实天数

        int preYear, preMonth;
        int nextYear, nextMonth;

        int size = 42;

        List<Calendar> mItems = new ArrayList<>();

        int preMonthDaysCount;
        if (month == 1) {//如果是1月
            preYear = year - 1;
            preMonth = 12;
            nextYear = year;
            nextMonth = month + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        } else if (month == 12) {//如果是12月
            preYear = year;
            preMonth = month - 1;
            nextYear = year + 1;
            nextMonth = 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        } else {//平常
            preYear = year;
            preMonth = month - 1;
            nextYear = year;
            nextMonth = month + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        }
        int nextDay = 1;
        for (int i = 0; i < size; i++) {
            Calendar calendarDate = new Calendar();
            if (i < mPreDiff) {
                calendarDate.setYear(preYear);
                calendarDate.setMonth(preMonth);
                calendarDate.setDay(preMonthDaysCount - mPreDiff + i + 1);
            } else if (i >= monthDayCount + mPreDiff) {
                calendarDate.setYear(nextYear);
                calendarDate.setMonth(nextMonth);
                calendarDate.setDay(nextDay);
                ++nextDay;
            } else {
                calendarDate.setYear(year);
                calendarDate.setMonth(month);
                calendarDate.setCurrentMonth(true);
                calendarDate.setDay(i - mPreDiff + 1);
            }
            if (calendarDate.equals(currentDate)) {
                calendarDate.setCurrentDay(true);
            }
            LunarCalendar.setupLunarCalendar(calendarDate);
            mItems.add(calendarDate);
        }
        return mItems;
    }

    static List<Calendar> getWeekCalendars(Calendar calendar, CalendarViewDelegate mDelegate) {
        long curTime = calendar.getTimeInMillis();

        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(),
                calendar.getMonth() - 1,
                calendar.getDay(), 12, 0);//
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        int startDiff;
        if (mDelegate.getWeekStart() == 1) {
            startDiff = week - 1;
        } else if (mDelegate.getWeekStart() == 2) {
            startDiff = week == 1 ? 6 : week - mDelegate.getWeekStart();
        } else {
            startDiff = week == 7 ? 0 : week;
        }

        curTime -= startDiff * ONE_DAY;
        java.util.Calendar minCalendar = java.util.Calendar.getInstance();
        minCalendar.setTimeInMillis(curTime);
        Calendar startCalendar = new Calendar();
        startCalendar.setYear(minCalendar.get(java.util.Calendar.YEAR));
        startCalendar.setMonth(minCalendar.get(java.util.Calendar.MONTH) + 1);
        startCalendar.setDay(minCalendar.get(java.util.Calendar.DAY_OF_MONTH));
        return initCalendarForWeekView(startCalendar, mDelegate, mDelegate.getWeekStart());
    }

    /**
     * 生成周视图的7个item
     *
     * @param calendar  周视图的第一个日子calendar，所以往后推迟6天，生成周视图
     * @param mDelegate mDelegate
     * @param weekStart weekStart
     * @return 生成周视图的7个item
     */
    @SuppressWarnings("unused")
    static List<Calendar> initCalendarForWeekView(Calendar calendar, CalendarViewDelegate mDelegate, int weekStart) {

        java.util.Calendar date = java.util.Calendar.getInstance();//当天时间
        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay(), 12, 0);
        long curDateMills = date.getTimeInMillis();//生成选择的日期时间戳

        //int weekEndDiff = getWeekViewEndDiff(calendar.getYear(), calendar.getMonth(), calendar.getDay(), weekStart);
        //weekEndDiff 例如周起始为周日1，当前为2020-04-01，周三，则weekEndDiff为本周结束相差今天三天，weekEndDiff=3
        int weekEndDiff = 6;
        List<Calendar> mItems = new ArrayList<>();

        date.setTimeInMillis(curDateMills);
        Calendar selectCalendar = new Calendar();
        selectCalendar.setYear(calendar.getYear());
        selectCalendar.setMonth(calendar.getMonth());
        selectCalendar.setDay(calendar.getDay());
        if (selectCalendar.equals(mDelegate.getCurrentDay())) {
            selectCalendar.setCurrentDay(true);
        }
        LunarCalendar.setupLunarCalendar(selectCalendar);
        selectCalendar.setCurrentMonth(true);
        mItems.add(selectCalendar);


        for (int i = 1; i <= weekEndDiff; i++) {
            date.setTimeInMillis(curDateMills + i * ONE_DAY);
            Calendar calendarDate = new Calendar();
            calendarDate.setYear(date.get(java.util.Calendar.YEAR));
            calendarDate.setMonth(date.get(java.util.Calendar.MONTH) + 1);
            calendarDate.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));
            if (calendarDate.equals(mDelegate.getCurrentDay())) {
                calendarDate.setCurrentDay(true);
            }
            LunarCalendar.setupLunarCalendar(calendarDate);
            calendarDate.setCurrentMonth(true);
            mItems.add(calendarDate);
        }
        return mItems;
    }

    /**
     * 单元测试通过
     * 从选定的日期，获取周视图起始偏移量，用来生成周视图布局
     *
     * @param year      year
     * @param month     month
     * @param day       day
     * @param weekStart 周起始，1，2，7 日 一 六
     * @return 获取周视图起始偏移量，用来生成周视图布局
     */
    private static int getWeekViewStartDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day, 12, 0);//
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == 1) {
            return week - 1;
        }
        if (weekStart == 2) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == 7 ? 0 : week;
    }


    /**
     * 单元测试通过
     * 从选定的日期，获取周视图结束偏移量，用来生成周视图布局
     * 为了兼容DST，DST时区可能出现时间偏移1-2小时，从而导致凌晨时候实际获得的日期往前或者往后推移了一天，
     * 日历没有时和分的概念，因此把日期的时间强制在12:00，可以避免DST兼容问题
     *
     * @param year      year
     * @param month     month
     * @param day       day
     * @param weekStart 周起始，1，2，7 日 一 六
     * @return 获取周视图结束偏移量，用来生成周视图布局
     */
    public static int getWeekViewEndDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day, 12, 0);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == 1) {
            return 7 - week;
        }
        if (weekStart == 2) {
            return week == 1 ? 0 : 7 - week + 1;
        }
        return week == 7 ? 6 : 7 - week - 1;
    }


    /**
     * 从月视图切换获得第一天的日期
     * Test Pass 它是100%正确的
     *
     * @param position position
     * @param delegate position
     * @return 从月视图切换获得第一天的日期
     */
    static Calendar getFirstCalendarFromMonthViewPager(int position, CalendarViewDelegate delegate) {
        Calendar calendar = new Calendar();
        calendar.setYear((position + delegate.getMinYearMonth() - 1) / 12 + delegate.getMinYear());
        calendar.setMonth((position + delegate.getMinYearMonth() - 1) % 12 + 1);
        if (delegate.getDefaultCalendarSelectDay() != CalendarViewDelegate.FIRST_DAY_OF_MONTH) {
            int monthDays = getMonthDaysCount(calendar.getYear(), calendar.getMonth());
            Calendar indexCalendar = delegate.mIndexCalendar;
            calendar.setDay(indexCalendar == null || indexCalendar.getDay() == 0 ? 1 :
                    monthDays < indexCalendar.getDay() ? monthDays : indexCalendar.getDay());
        } else {
            calendar.setDay(1);
        }
        if (!isCalendarInRange(calendar, delegate)) {
            if (isMinRangeEdge(calendar, delegate)) {
                calendar = delegate.getMinRangeCalendar();
            } else {
                calendar = delegate.getMaxRangeCalendar();
            }
        }
        calendar.setCurrentMonth(calendar.getYear() == delegate.getCurrentDay().getYear() &&
                calendar.getMonth() == delegate.getCurrentDay().getMonth());
        calendar.setCurrentDay(calendar.equals(delegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendar);
        return calendar;
    }


    /**
     * 根据传入的日期获取边界访问日期，要么最大，要么最小
     *
     * @param calendar calendar
     * @param delegate delegate
     * @return 获取边界访问日期
     */
    static Calendar getRangeEdgeCalendar(Calendar calendar, CalendarViewDelegate delegate) {
        if (CalendarUtil.isCalendarInRange(delegate.getCurrentDay(), delegate)
                && delegate.getDefaultCalendarSelectDay() != CalendarViewDelegate.LAST_MONTH_VIEW_SELECT_DAY_IGNORE_CURRENT) {
            return delegate.createCurrentDate();
        }
        if (isCalendarInRange(calendar, delegate)) {
            return calendar;
        }
        Calendar minRangeCalendar = delegate.getMinRangeCalendar();
        if (minRangeCalendar.isSameMonth(calendar)) {
            return delegate.getMinRangeCalendar();
        }
        return delegate.getMaxRangeCalendar();
    }

    /**
     * 是否是最小访问边界了
     *
     * @param calendar calendar
     * @return 是否是最小访问边界了
     */
    private static boolean isMinRangeEdge(Calendar calendar, CalendarViewDelegate delegate) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(delegate.getMinYear(), delegate.getMinYearMonth() - 1, delegate.getMinYearDay(), 12, 0);
        long minTime = c.getTimeInMillis();
        c.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay(), 12, 0);
        long curTime = c.getTimeInMillis();
        return curTime < minTime;
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
