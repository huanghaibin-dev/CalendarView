package com.haibin.calendarview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by haibin
 * on 2017/2/7.
 */

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
