package com.haibin.calendarview;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 测试
 * Created by haibin on 2017/4/10.
 */
public class LunarCalendarTest {
    @Test
    public void getLunarText() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2017);
        calendar.setMonth(6);
        calendar.setDay(24);

        Calendar calendar1 = new Calendar();
        calendar1.setYear(2017);
        calendar1.setMonth(6);
        calendar1.setDay(25);
        assertEquals("六月",LunarCalendar.getLunarText(calendar));
        assertEquals("六月",LunarCalendar.getLunarText(calendar1));
    }

    /**
     * 测试当年的清明节偏移日期为0、1、2， 4或5号6号
     *
     * @throws Exception Exception
     */
    @Test
    public void getTermsOffset() throws Exception {

    }


    /**
     * 测试每个月农历有多少天，也可适用于除夕算法
     *
     * @throws Exception Exception
     */
    @Test
    public void daysInLunarMonth() throws Exception {
        int d = LunarCalendar.daysInLunarMonth(2019,12);
        assertEquals(30,d);
    }

    /**
     * 测试每年冬至是具体几日
     *
     * @throws Exception 异常
     */
    @Test
    public void getWinterSolstice() throws Exception {
        assertEquals(22, 22);
    }

}