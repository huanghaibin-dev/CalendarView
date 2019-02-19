package com.haibin.calendarview;

import android.content.Context;

/**
 * 干支纪年算法
 * Created by huanghaibin on 2019/2/12.
 */
@SuppressWarnings("unused")
public final class TrunkBranchAnnals {

    /**
     * 天干字符串
     */
    private static String[] TRUNK_STR = null;

    /**
     * 地支字符串
     */
    private static String[] BRANCH_STR = null;

    /**
     * 单独使用请先调用这个方法
     * @param context context
     */
    public static void init(Context context) {
        if (TRUNK_STR != null) {
            return;
        }
        TRUNK_STR = context.getResources().getStringArray(R.array.trunk_string_array);
        BRANCH_STR = context.getResources().getStringArray(R.array.branch_string_array);

    }

    /**
     * 获取某一年对应天干文字
     *
     * @param year 年份
     * @return 天干由甲到癸，每10一轮回
     */
    @SuppressWarnings("all")
    public static String getTrunkString(int year) {
        return TRUNK_STR[getTrunkInt(year)];
    }

    /**
     * 获取某一年对应天干，
     *
     * @param year 年份
     * @return 4 5 6 7 8 9 10 1 2 3
     */
    @SuppressWarnings("all")
    public static int getTrunkInt(int year) {
        int trunk = year % 10;
        return trunk == 0 ? 9 : trunk - 1;
    }

    /**
     * 获取某一年对应地支文字
     *
     * @param year 年份
     * @return 地支由子到亥，每12一轮回
     */
    @SuppressWarnings("all")
    public static String getBranchString(int year) {
        return BRANCH_STR[getBranchInt(year)];
    }

    /**
     * 获取某一年对应地支
     *
     * @param year 年份
     * @return 4 5 6 7 8 9 10 11 12 1 2 3
     */
    @SuppressWarnings("all")
    public static int getBranchInt(int year) {
        int branch = year % 12;
        return branch == 0 ? 11 : branch - 1;
    }

    /**
     * 获取干支纪年
     *
     * @param year 年份
     * @return 干支纪年
     */
    public static String getTrunkBranchYear(int year) {
        return String.format("%s%s", getTrunkString(year), getBranchString(year));
    }
}
