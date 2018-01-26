package com.haibin.calendarview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试
 * Created by huanghaibin on 2017/11/22.
 */
public class UtilTest {
    @Test
    public void getMonthViewHeight() throws Exception {
        assertEquals(50,Util.getMonthViewHeight(2018,2,10));
    }

    @Test
    public void getFirstCalendarFromWeekCount() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2017);
        calendar.setMonth(1);
        calendar.setDay(1);

        Calendar calendar1 = new Calendar();
        calendar1.setYear(2017);
        calendar1.setMonth(7);
        calendar1.setDay(31);
        assertEquals(calendar, Util.getFirstCalendarFromWeekCount(2017, 7,1));
        //assertEquals(calendar1, Util.getFirstCalendarFromWeekCount(2017, 8,1));
    }

    @Test
    public void getWeekCountDiff() throws Exception {
        //assertEquals(4, Util.getWeekCountDiff(2017, 2));
        //assertEquals(8, Util.getWeekCountDiff(2018, 3));
        assertEquals(0, Util.getWeekCountDiff(2017, 9));
    }

    @Test
    public void getWeekCountBetweenYearAndYear() throws Exception {
        assertEquals(53, Util.getWeekCountBetweenYearAndYear(2017, 1, 2017, 12));
        //assertEquals(732, Util.getWeekCountBetweenYearAndYear(2018, 10, 2018, 11));
    }

    /**
     * 获取两个年份之间一共有多少周
     *
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getWeekCountBetweenYearByYear() throws Exception {
        assertEquals(53, Util.getWeekCountBetweenYearAndYear(2017, 2018));
        assertEquals(732, Util.getWeekCountBetweenYearAndYear(2004, 2017));
    }


    /**
     * 测试获取某年第几个星期第一天：即星期一在第几个月，如2004年第6周刚刚是2月1日
     *
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getMonthFromWeekFirstDayInYear() throws Exception {
        assertEquals(1, Util.getMonthFromWeekFirstDayInYear(2005, 6));
        assertEquals(1, Util.getMonthFromWeekFirstDayInYear(2006, 7));
    }

    /**
     * 测试获取某年某天在第几个月，如2004年第60天是2月29，第61天是3月1号
     *
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getMonthFromDayInYear() throws Exception {
        assertEquals(2, Util.getMonthFromDayInYear(2005, 61));
        assertEquals(2, Util.getMonthFromDayInYear(2006, 60));
    }

    /**
     * 获取某年第几周起始第一天，如遇到前一个月，则为1号
     *
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getFirstCalendarFormWeekInYear() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2005);
        calendar.setMonth(1);
        calendar.setDay(1);
        //assertEquals(calendar, Util.getFirstCalendarFormWeekInYear(2005, 1));
    }

    /**
     * 从一个日期Calendar中获取所处在一年中的第几个星期
     *
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getWeekFromCalendarInYear() throws Exception {

        Calendar calendar1 = new Calendar();
        calendar1.setYear(2004);
        calendar1.setMonth(2);
        calendar1.setDay(29);

        Calendar calendar2 = new Calendar();
        calendar2.setYear(2004);
        calendar2.setMonth(1);
        calendar2.setDay(31);

        assertEquals(10, Util.getWeekFromCalendarInYear(calendar1));
        assertEquals(5, Util.getWeekFromCalendarInYear(calendar2));
    }

    /**
     * 获取某天在该月的第几周
     *
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getWeekFromDayInMonth() throws Exception {
        Calendar calendar1 = new Calendar();
        calendar1.setYear(2018);
        calendar1.setMonth(3);
        calendar1.setDay(31);

        Calendar calendar2 = new Calendar();
        calendar2.setYear(2018);
        calendar2.setMonth(4);
        calendar2.setDay(7);

        assertEquals(5, Util.getWeekFromDayInMonth(calendar1));
        assertEquals(1, Util.getWeekFromDayInMonth(calendar2));
    }


    /**
     * 根据日期获取两个年份中第几周
     *
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getWeekFromCalendarBetweenYearAndYear() throws Exception {
        Calendar calendar1 = new Calendar();
        calendar1.setYear(2004);
        calendar1.setMonth(1);
        calendar1.setDay(10);

        Calendar calendar2 = new Calendar();
        calendar2.setYear(2017);
        calendar2.setMonth(11);
        calendar2.setDay(27);
        //assertEquals(2, Util.getWeekFromCalendarBetweenYearAndYear(calendar1, 2004));
        //assertEquals(727, Util.getWeekFromCalendarBetweenYearAndYear(calendar2, 2004));

        //assertEquals(2,Util.getWeekFromCalendarBetweenYearAndYear(calendar1,2004,1));
        assertEquals(727,Util.getWeekFromCalendarBetweenYearAndYear(calendar2,2004,1));
    }

}