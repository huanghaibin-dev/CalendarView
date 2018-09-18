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
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

final class LunarCalendar {


    static void init(Context context) {
        if (MONTH_STR != null) {
            return;
        }
        SolarTermUtil.init(context);
        MONTH_STR = context.getResources().getStringArray(R.array.lunar_first_of_month);
        TRADITION_FESTIVAL_STR = context.getResources().getStringArray(R.array.tradition_festival);
        DAY_STR = context.getResources().getStringArray(R.array.lunar_str);
        SPECIAL_FESTIVAL_STR = context.getResources().getStringArray(R.array.special_festivals);
        SOLAR_CALENDAR = context.getResources().getStringArray(R.array.solar_festival);
    }

    /**
     * 农历月份第一天转写
     */
    private static String[] MONTH_STR = null;

    /**
     * 传统农历节日
     */
    private static String[] TRADITION_FESTIVAL_STR = null;

    /**
     * 农历大写
     */
    private static String[] DAY_STR = null;

    /**
     * 特殊节日的数组
     */
    private static String[] SPECIAL_FESTIVAL_STR = null;

    /**
     * 特殊节日、母亲节和父亲节,感恩节等
     */
    @SuppressLint("UseSparseArrays")
    private static final Map<Integer, String[]> SPECIAL_FESTIVAL = new HashMap<>();

    /**
     * 公历节日
     */
    private static String[] SOLAR_CALENDAR = null;

    /**
     * 保存每年24节气
     */
    @SuppressLint("UseSparseArrays")
    private static final Map<Integer, String[]> SOLAR_TERMS = new HashMap<>();

    /**
     * 返回传统农历节日
     *
     * @param year  农历年
     * @param month 农历月
     * @param day   农历日
     * @return 返回传统农历节日
     */
    private static String getTraditionFestival(int year, int month, int day) {
        if (month == 12) {
            int count = daysInLunarMonth(year, month);
            if (day == count) {
                return TRADITION_FESTIVAL_STR[0];//除夕
            }
        }
        String text = getString(month, day);
        String festivalStr = "";
        for (String festival : TRADITION_FESTIVAL_STR) {
            if (festival.contains(text)) {
                festivalStr = festival.replace(text, "");
                break;
            }
        }
        return festivalStr;
    }


    /**
     * 数字转换为汉字月份
     *
     * @param month 月
     * @param leap  1==闰月
     * @return 数字转换为汉字月份
     */
    private static String numToChineseMonth(int month, int leap) {
        if (leap == 1) {
            return String.format("闰%s", MONTH_STR[month - 1]);
        }
        return MONTH_STR[month - 1];
    }

    /**
     * 数字转换为农历节日或者日期
     *
     * @param month 月
     * @param day   日
     * @param leap  1==闰月
     * @return 数字转换为汉字日
     */
    private static String numToChinese(int month, int day, int leap) {
        if (day == 1) {
            return numToChineseMonth(month, leap);
        }
        return DAY_STR[day - 1];
    }

    /**
     * 用来表示1900年到2099年间农历年份的相关信息，共24位bit的16进制表示，其中：
     * 1. 前4位表示该年闰哪个月；
     * 2. 5-17位表示农历年份13个月的大小月分布，0表示小，1表示大；
     * 3. 最后7位表示农历年首（正月初一）对应的公历日期。
     * <p/>
     * 以2014年的数据0x955ABF为例说明：
     * 1001 0101 0101 1010 1011 1111
     * 闰九月 农历正月初一对应公历1月31号
     */
    private static final int LUNAR_INFO[] = {
            0x84B6BF,/*1900*/
            0x04AE53, 0x0A5748, 0x5526BD, 0x0D2650, 0x0D9544, 0x46AAB9, 0x056A4D, 0x09AD42, 0x24AEB6, 0x04AE4A,/*1901-1910*/
            0x6A4DBE, 0x0A4D52, 0x0D2546, 0x5D52BA, 0x0B544E, 0x0D6A43, 0x296D37, 0x095B4B, 0x749BC1, 0x049754,/*1911-1920*/
            0x0A4B48, 0x5B25BC, 0x06A550, 0x06D445, 0x4ADAB8, 0x02B64D, 0x095742, 0x2497B7, 0x04974A, 0x664B3E,/*1921-1930*/
            0x0D4A51, 0x0EA546, 0x56D4BA, 0x05AD4E, 0x02B644, 0x393738, 0x092E4B, 0x7C96BF, 0x0C9553, 0x0D4A48,/*1931-1940*/
            0x6DA53B, 0x0B554F, 0x056A45, 0x4AADB9, 0x025D4D, 0x092D42, 0x2C95B6, 0x0A954A, 0x7B4ABD, 0x06CA51,/*1941-1950*/
            0x0B5546, 0x555ABB, 0x04DA4E, 0x0A5B43, 0x352BB8, 0x052B4C, 0x8A953F, 0x0E9552, 0x06AA48, 0x6AD53C,/*1951-1960*/
            0x0AB54F, 0x04B645, 0x4A5739, 0x0A574D, 0x052642, 0x3E9335, 0x0D9549, 0x75AABE, 0x056A51, 0x096D46,/*1961-1970*/
            0x54AEBB, 0x04AD4F, 0x0A4D43, 0x4D26B7, 0x0D254B, 0x8D52BF, 0x0B5452, 0x0B6A47, 0x696D3C, 0x095B50,/*1971-1980*/
            0x049B45, 0x4A4BB9, 0x0A4B4D, 0xAB25C2, 0x06A554, 0x06D449, 0x6ADA3D, 0x0AB651, 0x095746, 0x5497BB,/*1981-1990*/
            0x04974F, 0x064B44, 0x36A537, 0x0EA54A, 0x86B2BF, 0x05AC53, 0x0AB647, 0x5936BC, 0x092E50, 0x0C9645,/*1991-2000*/
            0x4D4AB8, 0x0D4A4C, 0x0DA541, 0x25AAB6, 0x056A49, 0x7AADBD, 0x025D52, 0x092D47, 0x5C95BA, 0x0A954E,/*2001-2010*/
            0x0B4A43, 0x4B5537, 0x0AD54A, 0x955ABF, 0x04BA53, 0x0A5B48, 0x652BBC, 0x052B50, 0x0A9345, 0x474AB9,/*2011-2020*/
            0x06AA4C, 0x0AD541, 0x24DAB6, 0x04B64A, 0x6a573D, 0x0A4E51, 0x0D2646, 0x5E933A, 0x0D534D, 0x05AA43,/*2021-2030*/
            0x36B537, 0x096D4B, 0xB4AEBF, 0x04AD53, 0x0A4D48, 0x6D25BC, 0x0D254F, 0x0D5244, 0x5DAA38, 0x0B5A4C,/*2031-2040*/
            0x056D41, 0x24ADB6, 0x049B4A, 0x7A4BBE, 0x0A4B51, 0x0AA546, 0x5B52BA, 0x06D24E, 0x0ADA42, 0x355B37,/*2041-2050*/
            0x09374B, 0x8497C1, 0x049753, 0x064B48, 0x66A53C, 0x0EA54F, 0x06AA44, 0x4AB638, 0x0AAE4C, 0x092E42,/*2051-2060*/
            0x3C9735, 0x0C9649, 0x7D4ABD, 0x0D4A51, 0x0DA545, 0x55AABA, 0x056A4E, 0x0A6D43, 0x452EB7, 0x052D4B,/*2061-2070*/
            0x8A95BF, 0x0A9553, 0x0B4A47, 0x6B553B, 0x0AD54F, 0x055A45, 0x4A5D38, 0x0A5B4C, 0x052B42, 0x3A93B6,/*2071-2080*/
            0x069349, 0x7729BD, 0x06AA51, 0x0AD546, 0x54DABA, 0x04B64E, 0x0A5743, 0x452738, 0x0D264A, 0x8E933E,/*2081-2090*/
            0x0D5252, 0x0DAA47, 0x66B53B, 0x056D4F, 0x04AE45, 0x4A4EB9, 0x0A4D4C, 0x0D1541, 0x2D92B5          /*2091-2099*/
    };


    /**
     * 传回农历 year年month月的总天数，总共有13个月包括闰月
     *
     * @param year  将要计算的年份
     * @param month 将要计算的月份
     * @return 传回农历 year年month月的总天数
     */
    private static int daysInLunarMonth(int year, int month) {
        if ((LUNAR_INFO[year - CalendarViewDelegate.MIN_YEAR] & (0x100000 >> month)) == 0)
            return 29;
        else
            return 30;
    }

    /**
     * 获取公历节日
     *
     * @param month 公历月份
     * @param day   公历日期
     * @return 公历节日
     */
    private static String gregorianFestival(int month, int day) {
        String text = getString(month, day);
        String solar = "";
        for (String aMSolarCalendar : SOLAR_CALENDAR) {
            if (aMSolarCalendar.contains(text)) {
                solar = aMSolarCalendar.replace(text, "");
                break;
            }
        }
        return solar;
    }

    private static String getString(int month, int day) {
        return (month >= 10 ? String.valueOf(month) : "0" + month) + (day >= 10 ? day : "0" + day);
    }


    /**
     * 返回24节气
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 返回24节气
     */
    private static String getSolarTerm(int year, int month, int day) {
        if (!SOLAR_TERMS.containsKey(year)) {
            SOLAR_TERMS.put(year, SolarTermUtil.getSolarTerms(year));
        }
        String[] solarTerm = SOLAR_TERMS.get(year);
        String text = String.format("%s%s", year, getString(month, day));
        String solar = "";
        for (String solarTermName : solarTerm) {
            if (solarTermName.contains(text)) {
                solar = solarTermName.replace(text, "");
                break;
            }
        }
        return solar;
    }


    /**
     * 获取农历节日
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 农历节日
     */
    private static String getLunarText(int year, int month, int day) {
        String termText = LunarCalendar.getSolarTerm(year, month, day);
        String solar = LunarCalendar.gregorianFestival(month, day);
        if (!TextUtils.isEmpty(solar))
            return solar;
        if (!TextUtils.isEmpty(termText))
            return termText;
        int[] lunar = LunarUtil.solarToLunar(year, month, day);
        String festival = getTraditionFestival(lunar[0], lunar[1], lunar[2]);
        if (!TextUtils.isEmpty(festival))
            return festival;
        return LunarCalendar.numToChinese(lunar[1], lunar[2], lunar[3]);
    }


    /**
     * 获取特殊计算方式的节日
     * 如：每年五月的第二个星期日为母亲节，六月的第三个星期日为父亲节
     * 每年11月第四个星期四定为"感恩节"
     *
     * @param year  year
     * @param month month
     * @param day   day
     * @return 获取西方节日
     */
    private static String getSpecialFestival(int year, int month, int day) {
        if (!SPECIAL_FESTIVAL.containsKey(year)) {
            SPECIAL_FESTIVAL.put(year, getSpecialFestivals(year));
        }
        String[] specialFestivals = SPECIAL_FESTIVAL.get(year);
        String text = String.format("%s%s", year, getString(month, day));
        String solar = "";
        for (String special : specialFestivals) {
            if (special.contains(text)) {
                solar = special.replace(text, "");
                break;
            }
        }
        return solar;
    }


    /**
     * 获取每年的母亲节和父亲节和感恩节
     * 特殊计算方式的节日
     *
     * @param year 年
     * @return 获取每年的母亲节和父亲节、感恩节
     */
    private static String[] getSpecialFestivals(int year) {
        String[] festivals = new String[3];
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, 4, 1);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        int startDiff = 7 - week + 1;
        if (startDiff == 7) {
            festivals[0] = dateToString(year, 5, startDiff + 1) + SPECIAL_FESTIVAL_STR[0];
        } else {
            festivals[0] = dateToString(year, 5, startDiff + 7 + 1) + SPECIAL_FESTIVAL_STR[0];
        }
        date.set(year, 5, 1);
        week = date.get(java.util.Calendar.DAY_OF_WEEK);
        startDiff = 7 - week + 1;
        if (startDiff == 7) {
            festivals[1] = dateToString(year, 6, startDiff + 7 + 1) + SPECIAL_FESTIVAL_STR[1];
        } else {
            festivals[1] = dateToString(year, 6, startDiff + 7 + 7 + 1) + SPECIAL_FESTIVAL_STR[1];
        }

        date.set(year, 10, 1);
        week = date.get(java.util.Calendar.DAY_OF_WEEK);
        startDiff = 7 - week + 1;
        if (startDiff <= 2) {
            festivals[2] = dateToString(year, 11, startDiff + 21 + 5) + SPECIAL_FESTIVAL_STR[2];
        } else {
            festivals[2] = dateToString(year, 11, startDiff + 14 + 5) + SPECIAL_FESTIVAL_STR[2];
        }
        return festivals;
    }


    private static String dateToString(int year, int month, int day) {
        return String.format("%s%s", year, getString(month, day));
    }

    /**
     * 初始化各种农历、节日
     *
     * @param calendar calendar
     */
    static void setupLunarCalendar(Calendar calendar) {
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        calendar.setWeekend(CalendarUtil.isWeekend(calendar));
        calendar.setWeek(CalendarUtil.getWeekFormCalendar(calendar));

        Calendar lunarCalendar = new Calendar();
        calendar.setLunarCalendar(lunarCalendar);
        int[] lunar = LunarUtil.solarToLunar(year, month, day);
        lunarCalendar.setYear(lunar[0]);
        lunarCalendar.setMonth(lunar[1]);
        lunarCalendar.setDay(lunar[2]);
        calendar.setLeapYear(CalendarUtil.isLeapYear(year));
        if (lunar[3] == 1) {//如果是闰月
            calendar.setLeapMonth(lunar[1]);
            lunarCalendar.setLeapMonth(lunar[1]);
        }
        String solarTerm = LunarCalendar.getSolarTerm(year, month, day);
        String gregorian = LunarCalendar.gregorianFestival(month, day);
        String festival = getTraditionFestival(lunar[0], lunar[1], lunar[2]);
        if (TextUtils.isEmpty(gregorian)) {
            gregorian = getSpecialFestival(year, month, day);
        }
        calendar.setSolarTerm(solarTerm);
        calendar.setGregorianFestival(gregorian);
        calendar.setTraditionFestival(festival);
        lunarCalendar.setTraditionFestival(festival);
        lunarCalendar.setSolarTerm(solarTerm);
        if (!TextUtils.isEmpty(solarTerm)) {
            calendar.setLunar(solarTerm);
        } else if (!TextUtils.isEmpty(gregorian)) {
            calendar.setLunar(gregorian);
        } else if (!TextUtils.isEmpty(festival)) {
            calendar.setLunar(festival);
        } else {
            calendar.setLunar(LunarCalendar.numToChinese(lunar[1], lunar[2], lunar[3]));
        }
        lunarCalendar.setLunar(calendar.getLunar());
    }

    /**
     * 获取农历节日
     *
     * @param calendar calendar
     * @return 获取农历节日
     */
    static String getLunarText(Calendar calendar) {
        return getLunarText(calendar.getYear(), calendar.getMonth(), calendar.getDay());
    }
}
