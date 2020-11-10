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

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 日历对象、
 */
@SuppressWarnings("all")
public final class Calendar implements Serializable, Comparable<Calendar> {
    private static final long serialVersionUID = 141315161718191143L;


    /**
     * 年
     */
    private int year;

    /**
     * 月1-12
     */
    private int month;

    /**
     * 如果是闰月，则返回闰月
     */
    private int leapMonth;

    /**
     * 日1-31
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
     * 计划，可以用来标记当天是否有任务,这里是默认的，如果使用多标记，请使用下面API
     * using addScheme(int schemeColor,String scheme); multi scheme
     */
    private String scheme;

    /**
     * 各种自定义标记颜色、没有则选择默认颜色，如果使用多标记，请使用下面API
     * using addScheme(int schemeColor,String scheme); multi scheme
     */
    private int schemeColor;


    /**
     * 多标记
     * multi scheme,using addScheme();
     */
    private List<Scheme> schemes;

    /**
     * 是否是周末
     */
    private boolean isWeekend;

    /**
     * 星期,0-6 对应周日到周一
     */
    private int week;

    /**
     * 获取完整的农历日期
     */
    private Calendar lunarCalendar;


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


    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }


    public void addScheme(Scheme scheme) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(scheme);
    }

    public void addScheme(int schemeColor, String scheme) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(new Scheme(schemeColor, scheme));
    }

    public void addScheme(int type, int schemeColor, String scheme) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(new Scheme(type, schemeColor, scheme));
    }

    public void addScheme(int type, int schemeColor, String scheme, String other) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(new Scheme(type, schemeColor, scheme, other));
    }

    public void addScheme(int schemeColor, String scheme, String other) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(new Scheme(schemeColor, scheme, other));
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

    public Calendar getLunarCalendar() {
        return lunarCalendar;
    }

    public void setLunarCalendar(Calendar lunarCakendar) {
        this.lunarCalendar = lunarCakendar;
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

    public boolean hasScheme() {
        if (schemes != null && schemes.size() != 0) {
            return true;
        }
        if (!TextUtils.isEmpty(scheme)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是相同月份
     *
     * @param calendar 日期
     * @return 是否是相同月份
     */
    public boolean isSameMonth(Calendar calendar) {
        return year == calendar.getYear() && month == calendar.getMonth();
    }

    /**
     * 比较日期
     *
     * @param calendar 日期
     * @return <0 0 >0
     */
    public int compareTo(Calendar calendar) {
        if (calendar == null) {
            return 1;
        }
        return toString().compareTo(calendar.toString());
    }

    /**
     * 运算差距多少天
     *
     * @param calendar calendar
     * @return 运算差距多少天
     */
    public final int differ(Calendar calendar) {
        return CalendarUtil.differ(this, calendar);
    }

    /**
     * 日期是否可用
     *
     * @return 日期是否可用
     */
    public boolean isAvailable() {
        return year > 0 & month > 0 & day > 0 & day <=31 & month <= 12 & year >= 1900 & year <= 2099;
    }

    /**
     * 获取当前日历对应时间戳
     *
     * @return getTimeInMillis
     */
    public long getTimeInMillis() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis();
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

//    @Override
//    public int compare(Calendar lhs, Calendar rhs) {
//        if (lhs == null || rhs == null) {
//            return 0;
//        }
//        int result = lhs.compareTo(rhs);
//        return result;
//    }

    final void mergeScheme(Calendar calendar, String defaultScheme) {
        if (calendar == null)
            return;
        setScheme(TextUtils.isEmpty(calendar.getScheme()) ?
                defaultScheme : calendar.getScheme());
        setSchemeColor(calendar.getSchemeColor());
        setSchemes(calendar.getSchemes());
    }

    final void clearScheme() {
        setScheme("");
        setSchemeColor(0);
        setSchemes(null);
    }

    /**
     * 事件标记服务，现在多类型的事务标记建议使用这个
     */
    public final static class Scheme implements Serializable {
        private int type;
        private int shcemeColor;
        private String scheme;
        private String other;
        private Object obj;

        public Scheme() {
        }

        public Scheme(int type, int shcemeColor, String scheme, String other) {
            this.type = type;
            this.shcemeColor = shcemeColor;
            this.scheme = scheme;
            this.other = other;
        }

        public Scheme(int type, int shcemeColor, String scheme) {
            this.type = type;
            this.shcemeColor = shcemeColor;
            this.scheme = scheme;
        }

        public Scheme(int shcemeColor, String scheme) {
            this.shcemeColor = shcemeColor;
            this.scheme = scheme;
        }

        public Scheme(int shcemeColor, String scheme, String other) {
            this.shcemeColor = shcemeColor;
            this.scheme = scheme;
            this.other = other;
        }

        public int getShcemeColor() {
            return shcemeColor;
        }

        public void setShcemeColor(int shcemeColor) {
            this.shcemeColor = shcemeColor;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }
    }
}
