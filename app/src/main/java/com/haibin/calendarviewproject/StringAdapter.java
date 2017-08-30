package com.haibin.calendarviewproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huanghaibin
 * on 2017/8/30.
 */

class StringAdapter extends BaseRecyclerAdapter<String> {
    StringAdapter(Context context) {
        super(context);
        for (int i = 0; i < 20; i++) {
            addItem(String.valueOf(i + 1));
        }
    }

    @Override
    RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new StringHolder(mInflater.inflate(R.layout.item_list_string, parent, false));
    }

    @Override
    void onBindViewHolder(RecyclerView.ViewHolder holder, String item, int position) {

    }

    private static class StringHolder extends RecyclerView.ViewHolder {
        StringHolder(View itemView) {
            super(itemView);
        }
    }
}
