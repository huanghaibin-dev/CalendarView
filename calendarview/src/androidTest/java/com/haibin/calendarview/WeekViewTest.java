package com.haibin.calendarview;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 周视图控件测试
 * Created by huanghaibin on 2017/11/22.
 */
public class WeekViewTest {
    /**
     * 测试传入某个星期某一天，计算该星期天数正确与否,主要比较起始正确
     *
     * @throws Exception 失败则抛出异常
     */
    @Test
    public void setup() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2004);
        calendar.setMonth(5);
        calendar.setDay(16);

        Calendar last = new Calendar();
        last.setYear(2004);
        last.setMonth(5);
        last.setDay(22);

        Calendar first = new Calendar();
        first.setYear(2004);
        first.setMonth(5);
        first.setDay(16);

        assertEquals(last, getLast(calendar));
        assertEquals(first, getFirst(calendar));
    }


    private static Calendar getFirst(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        int week = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//星期几,星期天 == 0，也就是前面偏差多少天
        int dayCount = Util.getMonthDaysCount(calendar.getYear(), calendar.getMonth());//获取某个月有多少天

        int preDiff = 0, nextDiff = 0;
        int preMonthDaysCount = 0;
        int preYear = 0, preMonth = 0;
        int nextYear = 0, nextMonth = 0;

        if (calendar.getDay() - week <= 0) {//如果某月某天-星期<0，则说明前面需要上个月的补数
            date.set(calendar.getYear(), calendar.getMonth() - 1, 1);
            preDiff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0，补数量就是偏差量diff;
            if (calendar.getMonth() == 1) {//取上一年的12月份
                preMonthDaysCount = 31;
                preYear = calendar.getYear() - 1;
                preMonth = 12;
            } else {//否则取上一个月份天数
                preMonthDaysCount = Util.getMonthDaysCount(calendar.getYear(), calendar.getMonth() - 1);
                preYear = calendar.getYear();
                preMonth = calendar.getMonth() - 1;
            }
        } else if (calendar.getDay() + 6 - week > dayCount) {//往后偏移多少天，即当前月份的最后一天不是星期6，则需要往后取补数
            nextDiff = calendar.getDay() + 6 - week - dayCount;//往后偏移多少天，补差diff
            if (calendar.getMonth() == 12) {
                nextMonth = 1;
                nextYear = calendar.getYear() + 1;
            } else {
                nextMonth = calendar.getMonth() + 1;
                nextYear = calendar.getYear();
            }
        }
        List<Calendar> items = new ArrayList<>();
        int nextDay = 1;
        int day = calendar.getDay() - week;
        for (int i = 0; i < 7; i++) {
            Calendar calendarDate = new Calendar();
            if (i < preDiff) {//如果前面有补数
                calendarDate.setYear(preYear);
                calendarDate.setMonth(preMonth);
                calendarDate.setDay(preMonthDaysCount - preDiff + i + 1);
                day += 1;
            } else if (nextDiff > 0 && i >= (7 - nextDiff)) {
                calendarDate.setYear(nextYear);
                calendarDate.setMonth(nextMonth);
                calendarDate.setDay(nextDay);
                nextDay += 1;
            } else {
                calendarDate.setYear(calendar.getYear());
                calendarDate.setMonth(calendar.getMonth());
                calendarDate.setDay(day);
                day += 1;
            }
            calendarDate.setLunar(LunarCalendar.getLunarText(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDay()));
            items.add(calendarDate);
        }
        return items.get(0);
    }


    private static Calendar getLast(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        int week = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//星期几,星期天 == 0，也就是前面偏差多少天
        int dayCount = Util.getMonthDaysCount(calendar.getYear(), calendar.getMonth());//获取某个月有多少天

        int preDiff = 0, nextDiff = 0;
        int preMonthDaysCount = 0;
        int preYear = 0, preMonth = 0;
        int nextYear = 0, nextMonth = 0;

        if (calendar.getDay() - week <= 0) {//如果某月某天-星期<0，则说明前面需要上个月的补数
            date.set(calendar.getYear(), calendar.getMonth() - 1, 1);
            preDiff = date.get(java.util.Calendar.DAY_OF_WEEK) - 1;//月第一天为星期几,星期天 == 0，补数量就是偏差量diff;
            if (calendar.getMonth() == 1) {//取上一年的12月份
                preMonthDaysCount = 31;
                preYear = calendar.getYear() - 1;
                preMonth = 12;
            } else {//否则取上一个月份天数
                preMonthDaysCount = Util.getMonthDaysCount(calendar.getYear(), calendar.getMonth() - 1);
                preYear = calendar.getYear();
                preMonth = calendar.getMonth() - 1;
            }
        } else if (calendar.getDay() + 6 - week > dayCount) {//往后偏移多少天，即当前月份的最后一天不是星期6，则需要往后取补数
            nextDiff = calendar.getDay() + 6 - week - dayCount;//往后偏移多少天，补差diff
            if (calendar.getMonth() == 12) {
                nextMonth = 1;
                nextYear = calendar.getYear() + 1;
            } else {
                nextMonth = calendar.getMonth() + 1;
                nextYear = calendar.getYear();
            }
        }
        List<Calendar> items = new ArrayList<>();
        int nextDay = 1;
        int day = calendar.getDay() - week;
        for (int i = 0; i < 7; i++) {
            Calendar calendarDate = new Calendar();
            if (i < preDiff) {//如果前面有补数
                calendarDate.setYear(preYear);
                calendarDate.setMonth(preMonth);
                calendarDate.setDay(preMonthDaysCount - preDiff + i + 1);
                day += 1;
            } else if (nextDiff > 0 && i >= (7 - nextDiff)) {
                calendarDate.setYear(nextYear);
                calendarDate.setMonth(nextMonth);
                calendarDate.setDay(nextDay);
                nextDay += 1;
            } else {
                calendarDate.setYear(calendar.getYear());
                calendarDate.setMonth(calendar.getMonth());
                calendarDate.setDay(day);
                day += 1;
            }
            calendarDate.setLunar(LunarCalendar.getLunarText(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDay()));
            items.add(calendarDate);
        }
        return items.get(6);
    }
}