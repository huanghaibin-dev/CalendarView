package com.haibin.calendarview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试
 * Created by huanghaibin on 2017/11/22.
 */
public class UtilTest {


    /**
     * 测试获取某年第几个星期第一天：即星期一在第几个月，如2004年第6周刚刚是2月1日
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getMonthFromWeekFirstDayInYear() throws Exception {
        assertEquals(1,Util.getMonthFromWeekFirstDayInYear(2005,6));
    }

    /**
     * 测试获取某年某天在第几个月，如2004年第60天是2月29，第61天是3月1号
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getMonthFromDayInYear() throws Exception {
        assertEquals(2,Util.getMonthFromDayInYear(2005,60));
    }

    /**
     * 获取某年第几周起始第一天，如遇到前一个月，则为1号
     * @throws Exception 如果测试失败则异常 AssertionError
     */
    @Test
    public void getFirstCalendarFormWeekInYear() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2005);
        calendar.setMonth(1);
        calendar.setDay(1);
        assertEquals(calendar,Util.getFirstCalendarFormWeekInYear(2005,1));
    }
}