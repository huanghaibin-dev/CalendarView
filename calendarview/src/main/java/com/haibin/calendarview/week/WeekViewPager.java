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
package com.haibin.calendarview.week;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 周视图滑动ViewPager，需要动态固定高度
 * 我们知道一年有365或366天，都不是7的整数倍，所以很容易确定每年都有53个星期，是个定值，
 * 所以Adapter的count数量就是: (maxYear-minYear)*53
 */
@SuppressWarnings("unused")
public class WeekViewPager extends ViewPager {

    /**
     * 周视图高度
     */
    private int mItemHeight;

    public WeekViewPager(Context context) {
        this(context, null);
    }

    public WeekViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置周视图高度
     *
     * @param mItemHeight 周视图高度
     */
    public void setItemHeight(int mItemHeight) {
        this.mItemHeight = mItemHeight;
    }

    /**
     * 周视图的高度应该与日历项的高度一致
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
