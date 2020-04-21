package com.haibin.calendarview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 新版月视图算法测试
 * Created by huanghaibin on 2018/2/8.
 */
@SuppressWarnings("all")
public class CalendarUtilTest {
    @Test
    public void getMonthViewHeight() throws Exception {
        //周一起始
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 4, 1, 1));
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 5, 1, 1));
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 6, 1, 1));
        assertEquals(6, CalendarUtil.getMonthViewHeight(2018, 9, 1, 1));

        //周一起始
        assertEquals(6, CalendarUtil.getMonthViewHeight(2018, 4, 1, 2));
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 5, 1, 2));
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 6, 1, 2));
        assertEquals(6, CalendarUtil.getMonthViewHeight(2018, 7, 1, 2));
    }

    /**
     * 根据星期数和最小日期推算出该星期的第一天
     */
    @Test
    public void getFirstCalendarStartWithMinCalendar() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(8);
        calendar.setDay(13);

        Calendar firstCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                1,
                2);
        assertEquals(calendar, firstCalendar);

        calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(8);
        calendar.setDay(20);

        firstCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                2,
                2);
        assertEquals(calendar, firstCalendar);

        calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(8);
        calendar.setDay(12);

        firstCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                1,
                1);
        assertEquals(calendar, firstCalendar);
    }

    /**
     * 获取两个日期之间一共有多少周，
     * 注意周起始周一、周日、周六
     *
     * @return 周数用于WeekViewPager itemCount
     */
    @Test
    public void getWeekCountBetweenBothCalendar() throws Exception {
        int count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 5,
                2018, 8, 12, 1);
        assertEquals(2, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 1,
                2018, 8, 12, 2);

        assertEquals(2, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 7, 29,
                2018, 8, 12, 2);

        assertEquals(3, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2017, 12, 1,
                2017, 12, 3, 2);

        assertEquals(1, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 13,
                2018, 12, 12, 2);

        assertEquals(18, count);
    }

    /**
     * 是否在日期范围內
     */
    @Test
    public void isCalendarInRange() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2017);
        calendar.setMonth(12);
        calendar.setDay(30);

        boolean isInRange = CalendarUtil.isCalendarInRange(calendar,
                2017, 3, 12,
                2018, 12, 31);

        assertEquals(true, isInRange);

        calendar = new Calendar();
        calendar.setYear(2017);
        calendar.setMonth(3);
        calendar.setDay(11);

        isInRange = CalendarUtil.isCalendarInRange(calendar,
                2017, 3, 12,
                2018, 12, 31);

        assertEquals(false, isInRange);

        calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(12);
        calendar.setDay(31);

        isInRange = CalendarUtil.isCalendarInRange(calendar,
                2017, 3, 12,
                2018, 12, 31);

        assertEquals(true, isInRange);

        calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(12);
        calendar.setDay(31);

        isInRange = CalendarUtil.isCalendarInRange(calendar,
                2019, 1, 31,
                2020, 11, 30);

        assertEquals(false, isInRange);

    }

    /**
     * 根据日期获取距离最小年份是第几周
     */
    @Test
    public void getWeekFromCalendarStartWithMinCalendar() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(8);
        calendar.setDay(1);

        int week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendar,
                2018, 8, 1, 1);

        assertEquals(1, week);

        calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(9);
        calendar.setDay(1);

        week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendar,
                2018, 8, 13, 2);

        assertEquals(3, week);

    }

    @Test
    public void getWeekCountDiff() throws Exception {
        int diff = CalendarUtil.getWeekViewEndDiff(2020,4,2,1);
        assertEquals(2,diff);
    }


    @Test
    public void getWeekViewStartDiff() throws Exception {

    }


    @Test
    public void getWeekViewEndDiff() throws Exception {

    }

    @Test
    public void getWeekFromDayInMonth() throws Exception {

        Calendar calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(4);
        calendar.setDay(24);

        CalendarUtil.initCalendarForMonthView(2018, 4, calendar, 1);

    }

    @Test
    public void getMonthEndDiff() throws Exception {
        assertEquals(5, CalendarUtil.getMonthEndDiff(2018, 4, 1));
        assertEquals(2, CalendarUtil.getMonthEndDiff(2018, 5, 1));
        assertEquals(0, CalendarUtil.getMonthEndDiff(2018, 6, 1));
        assertEquals(4, CalendarUtil.getMonthEndDiff(2018, 7, 1));

        assertEquals(4, CalendarUtil.getMonthEndDiff(2018, 4, 7));
        assertEquals(1, CalendarUtil.getMonthEndDiff(2018, 5, 7));
        assertEquals(6, CalendarUtil.getMonthEndDiff(2018, 6, 7));
        assertEquals(3, CalendarUtil.getMonthEndDiff(2018, 7, 7));

        assertEquals(6, CalendarUtil.getMonthEndDiff(2018, 4, 2));
        assertEquals(3, CalendarUtil.getMonthEndDiff(2018, 5, 2));
        assertEquals(1, CalendarUtil.getMonthEndDiff(2018, 6, 2));
        assertEquals(5, CalendarUtil.getMonthEndDiff(2018, 7, 2));
    }

    @Test
    public void getMonthViewStartDiff() throws Exception {

        Calendar calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(4);
        calendar.setDay(1);

        assertEquals(1, CalendarUtil.getMonthViewStartDiff(calendar, 7));

        assertEquals(0, CalendarUtil.getMonthViewStartDiff(calendar, 1));

        assertEquals(6, CalendarUtil.getMonthViewStartDiff(calendar, 2));

        calendar.setMonth(5);


        assertEquals(3, CalendarUtil.getMonthViewStartDiff(calendar, 7));

        assertEquals(2, CalendarUtil.getMonthViewStartDiff(calendar, 1));

        assertEquals(1, CalendarUtil.getMonthViewStartDiff(calendar, 2));
    }

    /**
     * 根据日期获取两个年份中第几周,用来设置 WeekView currentItem
     *
     * @throws Exception Exception
     */
    @Test
    public void differ() throws Exception {
        Calendar calendar1 = new Calendar();
        calendar1.setYear(2018);
        calendar1.setMonth(4);
        calendar1.setDay(1);

        Calendar calendar2 = new Calendar();
        calendar2.setYear(2018);
        calendar2.setMonth(4);
        calendar2.setDay(3);

        assertEquals(-2,CalendarUtil.differ(calendar1,calendar2));

        calendar1.setYear(2018);
        calendar1.setMonth(9);
        calendar1.setDay(30);

        calendar2.setYear(2018);
        calendar2.setMonth(9);
        calendar2.setDay(1);

        assertEquals(29,CalendarUtil.differ(calendar1,calendar2));

        calendar1.setYear(2018);
        calendar1.setMonth(9);
        calendar1.setDay(12);

        calendar2.setYear(2018);
        calendar2.setMonth(9);
        calendar2.setDay(5);

        assertEquals(7,CalendarUtil.differ(calendar1,calendar2));
    }
}