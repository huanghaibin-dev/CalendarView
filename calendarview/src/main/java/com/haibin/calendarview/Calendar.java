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


import java.io.Serializable;

/**
 * 日历对象、
 */
@SuppressWarnings("all")
public class Calendar implements Serializable {
    private static final long serialVersionUID = 141315161718191143L;

    /**
     * 年
     */
    private int year;

    /**
     * 月
     */
    private int month;

    /**
     * 如果是闰月，则返回闰月
     */
    private int leapMonth;

    /**
     * 日
     */
    private int day;

    /**
     * 是否是闰年
     */
    private boolean isLeapYear;

    /**
     * 是否是本月,这里对应的是月视图的本月，而非当前月份，请注意
     */
    private boolean isCurrentMonth;

    /**
     * 是否是今天
     */
    private boolean isCurrentDay;

    /**
     * 农历字符串，没有特别大的意义，用来做简单的农历或者节日标记
     * 建议通过lunarCakendar获取完整的农历日期
     */
    private String lunar;


    /**
     * 24节气
     */
    private String solarTerm;


    /**
     * 公历节日
     */
    private String gregorianFestival;

    /**
     * 传统农历节日
     */
    private String traditionFestival;

    /**
     * 计划，可以用来标记当天是否有任务
     */
    private String scheme;

    /**
     * 各种自定义标记颜色、没有则选择默认颜色
     */
    private int schemeColor;

    /**
     * 是否是周末
     */
    private boolean isWeekend;

    /**
     * 星期
     */
    private int week;

    /**
     * 获取完整的农历日期
     */
    private Calendar lunarCakendar;


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }


    public void setCurrentMonth(boolean currentMonth) {
        this.isCurrentMonth = currentMonth;
    }

    public boolean isCurrentDay() {
        return isCurrentDay;
    }

    public void setCurrentDay(boolean currentDay) {
        isCurrentDay = currentDay;
    }


    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public int getSchemeColor() {
        return schemeColor;
    }

    public void setSchemeColor(int schemeColor) {
        this.schemeColor = schemeColor;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public Calendar getLunarCakendar() {
        return lunarCakendar;
    }

    public void setLunarCakendar(Calendar lunarCakendar) {
        this.lunarCakendar = lunarCakendar;
    }

    public String getSolarTerm() {
        return solarTerm;
    }

    public void setSolarTerm(String solarTerm) {
        this.solarTerm = solarTerm;
    }

    public String getGregorianFestival() {
        return gregorianFestival;
    }

    public void setGregorianFestival(String gregorianFestival) {
        this.gregorianFestival = gregorianFestival;
    }


    public int getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(int leapMonth) {
        this.leapMonth = leapMonth;
    }

    public boolean isLeapYear() {
        return isLeapYear;
    }

    public void setLeapYear(boolean leapYear) {
        isLeapYear = leapYear;
    }

    public String getTraditionFestival() {
        return traditionFestival;
    }

    public void setTraditionFestival(String traditionFestival) {
        this.traditionFestival = traditionFestival;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Calendar) {
            if (((Calendar) o).getYear() == year && ((Calendar) o).getMonth() == month && ((Calendar) o).getDay() == day)
                return true;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return year + "" + (month < 10 ? "0" + month : month) + "" + (day < 10 ? "0" + day : day);
    }
}
