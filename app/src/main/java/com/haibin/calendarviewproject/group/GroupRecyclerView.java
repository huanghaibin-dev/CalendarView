package com.haibin.calendarviewproject.group;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.haibin.calendarviewproject.R;


/**
 * 带分组浮动的RecyclerView
 * Created by haibin on 2017/5/15.
 */
@SuppressWarnings("all")
public class GroupRecyclerView extends RecyclerView {
    private GroupItemDecoration mItemDecoration;
    private int mGroupHeight;
    private int mGroutBackground, mTextColor;
    private int mTextSize;
    private int mPaddingLeft, mPaddingRight;
    private boolean isCenter;
    protected int mChildItemOffset;
    private boolean isHasHeader;
    private OnGroupChangeListener mListener;

    public GroupRecyclerView(Context context) {
        super(context);
    }

    public GroupRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GroupRecyclerView);
        mTextSize = array.getDimensionPixelSize(R.styleable.GroupRecyclerView_group_text_size, 16);
        mGroupHeight = (int) array.getDimension(R.styleable.GroupRecyclerView_group_height, 52);
        mChildItemOffset = (int) array.getDimension(R.styleable.GroupRecyclerView_group_child_offset, 20);
        mTextColor = array.getColor(R.styleable.GroupRecyclerView_group_text_color, 0xFFFFFFFF);
        mGroutBackground = array.getColor(R.styleable.GroupRecyclerView_group_background, 0x80000000);
        isCenter = array.getBoolean(R.styleable.GroupRecyclerView_group_center, false);
        isHasHeader = array.getBoolean(R.styleable.GroupRecyclerView_group_has_header, true);
        mPaddingLeft = (int) array.getDimension(R.styleable.GroupRecyclerView_group_padding_left, 16);
        mPaddingRight = (int) array.getDimension(R.styleable.GroupRecyclerView_group_padding_right, 16);
        array.recycle();
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof GroupRecyclerAdapter) {
            super.setAdapter(adapter);
        } else {
            throw new IllegalStateException("Adapter must instanceof " +
                    "GroupRecyclerAdapter or extends GroupRecyclerAdapter");
        }
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        if (decor instanceof GroupItemDecoration)
            super.addItemDecoration(decor);
        else
            throw new IllegalStateException("ItemDecoration must instanceof " +
                    "GroupItemDecoration or extends GroupItemDecoration");
        mItemDecoration = (GroupItemDecoration) decor;
        mItemDecoration.setTextSize(mTextSize);
        mItemDecoration.setBackground(mGroutBackground);
        mItemDecoration.setTextColor(mTextColor);
        mItemDecoration.setGroupHeight(mGroupHeight);
        mItemDecoration.setPadding(mPaddingLeft, mPaddingRight);
        mItemDecoration.setCenter(isCenter);
        mItemDecoration.setHasHeader(isHasHeader);
        mItemDecoration.setChildItemOffset(mChildItemOffset);
        //mItemDecoration.notifyDataSetChanged((GroupRecyclerAdapter) getAdapter());
    }

    public void notifyDataSetChanged() {
        mItemDecoration.notifyDataSetChanged((GroupRecyclerAdapter) getAdapter());
    }

    public void setOnGroupChangeListener(OnGroupChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 分组最上面改变通知
     */
    public interface OnGroupChangeListener {
        void onGroupChange(int groupPosition, String group);
    }
}
