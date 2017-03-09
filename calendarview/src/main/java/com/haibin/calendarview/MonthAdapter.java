package com.haibin.calendarview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by haibin
 * on 2017/3/6.
 */

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
        return new MonthViewHolder(mInflater.inflate(R.layout.item_list_month, parent, false));
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
