package com.haibin.calendarview;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 测试
 * Created by haibin on 2017/4/10.
 */
public class LunarCalendarTest {

    /**
     * 测试当年的清明节偏移日期为0、1、2， 4或5号6号
     *
     * @throws Exception Exception
     */
    @Test
    public void getTermsOffset() throws Exception {
        assertEquals(1, LunarCalendar.getTermsOffset(1911));
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