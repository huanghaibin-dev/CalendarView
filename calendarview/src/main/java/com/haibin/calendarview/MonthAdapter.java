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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

class MonthAdapter extends BaseRecyclerAdapter<Month> {
    private List<Calendar> mSchemes;
    private int mSchemeColor;

    MonthAdapter(Context context) {
        super(context);
    }

    void setSchemes(List<Calendar> mSchemes) {
        this.mSchemes = mSchemes;
    }

    void setSchemeColor(int mSchemeColor) {
        this.mSchemeColor = mSchemeColor;
    }

    @Override
    RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new MonthViewHolder(mInflater.inflate(R.layout.cv_item_list_month, parent, false));
    }

    @Override
    void onBindViewHolder(RecyclerView.ViewHolder holder, Month item, int position) {
        MonthViewHolder h = (MonthViewHolder) holder;
        MonthView view = h.mMonthView;
        view.setSchemes(mSchemes);
        view.setSchemeColor(mSchemeColor);
        view.init(item.getDiff(), item.getCount(), item.getYear(), item.getMonth());
        h.mTextMonth.setText(String.format("%s月", item.getMonth()));
    }

    private static class MonthViewHolder extends RecyclerView.ViewHolder {
        MonthView mMonthView;
        TextView mTextMonth;

        MonthViewHolder(View itemView) {
            super(itemView);
            mMonthView = (MonthView) itemView.findViewById(R.id.selectView);
            mTextMonth = (TextView) itemView.findViewById(R.id.tv_month);
        }
    }
}
