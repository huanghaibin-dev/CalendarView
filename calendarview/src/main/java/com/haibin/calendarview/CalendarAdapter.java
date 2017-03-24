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

class CalendarAdapter extends BaseRecyclerAdapter<Calendar> {
    private int mThemeColor, mCurColor;

    CalendarAdapter(Context context) {
        super(context);
    }

    void setStyle(int mThemeColor, int mCurColor) {
        this.mThemeColor = mThemeColor;
        this.mCurColor = mCurColor;
    }

    @Override
    RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new CalenderViewHolder(mInflater.inflate(R.layout.item_list_calendar_mvp, parent, false));
    }

    @Override
    void onBindViewHolder(RecyclerView.ViewHolder holder, Calendar item, int position) {
        CalenderViewHolder h = (CalenderViewHolder) holder;
        h.itemView.setVisibility(item.isCurrentMonth() ? View.VISIBLE : View.GONE);
        CellView view = h.mCellView;
        view.init(item.getDay(), item.getLunar(), item.getScheme());
        view.setCircleColor(mThemeColor);
        if (item.isCurrentDay()) {
            view.setTextColor(mCurColor);
        }
    }

    private static class CalenderViewHolder extends RecyclerView.ViewHolder {
        CellView mCellView;

        CalenderViewHolder(View itemView) {
            super(itemView);
            mCellView = (CellView) itemView.findViewById(R.id.cellView);
        }
    }
}
