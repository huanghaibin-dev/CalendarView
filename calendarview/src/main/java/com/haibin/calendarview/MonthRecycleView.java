package com.haibin.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 *     author : alan.wu
 *     e-mail : alan.wu@chinaibex.com
 *     time   : 2020/06/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MonthRecycleView extends RecyclerView {

    private boolean isUpdateMonthView;

    private int mMonthCount;

    private CalendarViewDelegate mDelegate;

    private int mNextViewHeight, mPreViewHeight, mCurrentViewHeight;

    CalendarLayout mParentLayout;

    WeekViewPager mWeekPager;

    WeekBar mWeekBar;

    private MonthRecycleViewAdapter mAdapter;

    private int mCurrentPosition = -1;


    /**
     * 是否使用滚动到某一天
     */
    private boolean isUsingScrollToCalendar = false;

    private Context mContext;

    private StickyDecoration mStickyDecoration;


    public MonthRecycleView(@NonNull Context context) {
        super(context,null);
        setLayoutManager(new LinearLayoutManager(context));
    }

    public MonthRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setLayoutManager(new LinearLayoutManager(context));
    }

    public MonthRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setLayoutManager(new LinearLayoutManager(context));
    }

    /**
     * 初始化
     *
     * @param delegate delegate
     */
    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = -1;

        setLayoutParams(params);
        init();
    }

    private void init() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
        mAdapter = new MonthRecycleViewAdapter();
        setAdapter(mAdapter);
        mStickyDecoration = new StickyDecoration(mContext, new DecorationCallback() {
            @Override
            public String getData(int position) {
                int year = (position + mDelegate.getMinYearMonth() - 1) / 12 + mDelegate.getMinYear();
                int month = (position + mDelegate.getMinYearMonth() - 1) % 12 + 1;
                return monthFormat(month)+", "+year;
            }
        });
        addItemDecoration(mStickyDecoration);

    }

    private String monthFormat(int i){
        if(i < 1 || i > 12){
            return "";
        }else{
            String allMonth = "Jan,Feb,Mar,Apr,May,June,July,Aug,Sep,Oct,Nov,Dec";
            String[] months = allMonth.split(",");
            return months[i-1];
        }
    }

    /**
     * 更新当前日期，夜间过度的时候调用这个函数，一般不需要调用
     */
    void updateCurrentDate() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateCurrentDate();
        }
    }

    /**
     * 更新显示模式
     */
    void updateShowMode() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateShowMode();
            view.requestLayout();
        }
        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
            mCurrentViewHeight = 6 * mDelegate.getCalendarItemHeight();
            mNextViewHeight = mCurrentViewHeight;
            mPreViewHeight = mCurrentViewHeight;
        } else {
//            updateMonthViewHeight(mDelegate.mSelectedCalendar.getYear(), mDelegate.mSelectedCalendar.getMonth());
        }
//        ViewGroup.LayoutParams params = getLayoutParams();
//        params.height = mCurrentViewHeight;
//        setLayoutParams(params);
        if (mParentLayout != null) {
            mParentLayout.updateContentViewTranslateY();
        }
    }

    /**
     * 更新周起始
     */
    void updateWeekStart() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateWeekStart();
            view.requestLayout();
        }

        updateMonthViewHeight(mDelegate.mSelectedCalendar.getYear(), mDelegate.mSelectedCalendar.getMonth());
//        ViewGroup.LayoutParams params = getLayoutParams();
//        params.height = mCurrentViewHeight;
//        setLayoutParams(params);
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(mDelegate.mSelectedCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }
        updateSelected();
    }

    /**
     * 更新为默认选择模式
     */
    void updateDefaultSelect() {
        BaseMonthView view = findViewWithTag(getCurrentItem());
        if (view != null) {
            int index = view.getSelectedIndex(mDelegate.mSelectedCalendar);
            view.mCurrentItem = index;
            if (index >= 0 && mParentLayout != null) {
                mParentLayout.updateSelectPosition(index);
            }
            view.invalidate();
        }
    }

    public int getCurrentItem() {
        return mCurrentPosition;
    }

    /**
     * 更新字体颜色大小
     */
    final void updateStyle() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateStyle();
            view.invalidate();
        }
    }

    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.update();
        }
    }

    /**
     * 更新月视图Class
     */
    void updateMonthViewClass() {
        isUpdateMonthView = true;
        dataChangAndUpdateUI();
        isUpdateMonthView = false;
    }

    /**
     * 清除选择范围
     */
    final void clearSelectRange() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.invalidate();
        }
    }

    /**
     * 清除单选选择
     */
    final void clearSingleSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.mCurrentItem = -1;
            view.invalidate();
        }
    }

    /**
     * 清除单选选择
     */
    final void clearMultiSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.mCurrentItem = -1;
            view.invalidate();
        }
    }

    /**
     * 滚动到指定日期
     *
     * @param year           年
     * @param month          月
     * @param day            日
     * @param invokeListener 调用日期事件
     */
    void scrollToCalendar(int year, int month, int day, boolean smoothScroll, boolean invokeListener) {
        isUsingScrollToCalendar = true;
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setCurrentDay(calendar.equals(mDelegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendar);
        mDelegate.mIndexCalendar = calendar;
        mDelegate.mSelectedCalendar = calendar;
        mDelegate.updateSelectCalendarScheme();
        int y = calendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendar.getMonth() - mDelegate.getMinYearMonth();

        scrollToPosition(position);

        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }

        if (mDelegate.mCalendarSelectListener != null && invokeListener) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }
        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendar, false);
        }

        updateSelected();
    }

    /**
     * 滚动到当前日期
     */
    void scrollToCurrent() {
        isUsingScrollToCalendar = true;
        int position = 12 * (mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear()) +
                mDelegate.getCurrentDay().getMonth() - mDelegate.getMinYearMonth();

        smoothScrollToPosition(position);

        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.getCurrentDay());
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.getCurrentDay()));
            }
        }

        if (mDelegate.mCalendarSelectListener != null && getVisibility() == VISIBLE) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(mDelegate.mSelectedCalendar, false);
        }
    }

    /**
     * 更新选择效果
     */
    void updateSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }


    /**
     * 更新日期范围
     */
    final void updateRange() {
        isUpdateMonthView = true;
        notifyDataSetChanged();
        isUpdateMonthView = false;
        if (getVisibility() != VISIBLE) {
            return;
        }
        isUsingScrollToCalendar = false;
        Calendar calendar = mDelegate.mSelectedCalendar;
        int y = calendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendar.getMonth() - mDelegate.getMinYearMonth();
        smoothScrollToPosition(position);
        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }


        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendar, false);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }
        updateSelected();
    }

    /**
     * 刷新
     */
    void notifyDataSetChanged() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
        dataChangAndUpdateUI();
    }
    void dataChangAndUpdateUI(){
        if(null != mAdapter) mAdapter.notifyDataSetChanged();
    }

    /**
     * 更新月视图的高度
     *
     * @param year  year
     * @param month month
     */
    private void updateMonthViewHeight(int year, int month) {
        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {//非动态高度就不需要了
            mCurrentViewHeight = 6 * mDelegate.getCalendarItemHeight();
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = mCurrentViewHeight;
            return;
        }

        mCurrentViewHeight = CalendarUtil.getMonthViewHeight(year, month,
                mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                mDelegate.getMonthViewShowMode());
        if (month == 1) {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year - 1, 12,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
            mNextViewHeight = CalendarUtil.getMonthViewHeight(year, 2,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
        } else {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year, month - 1,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());
            if (month == 12) {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year + 1, 1,
                        mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                        mDelegate.getMonthViewShowMode());
            } else {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year, month + 1,
                        mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                        mDelegate.getMonthViewShowMode());
            }
        }
    }

    public interface DecorationCallback {
        String getData(int position);
    }


    public class StickyDecoration extends ItemDecoration {
        private DecorationCallback callback;
        private TextPaint textPaint;
        private Paint paint;
        private int topHead;

        public StickyDecoration(Context context, DecorationCallback decorationCallback) {
            this.callback = decorationCallback;
            paint = new Paint();
            paint.setColor(Color.WHITE);
            textPaint = new TextPaint();
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            textPaint.setFakeBoldText(false);
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(50);
            textPaint.setColor(Color.BLACK);
            textPaint.setTextAlign(Paint.Align.LEFT);
            topHead = 80;
            this.callback = decorationCallback;
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            String data = callback.getData(pos);
            if (TextUtils.isEmpty(data)) {
                return;
            }
            //同组的第一个才添加padding
            if (pos == 0 || isHeader(pos)) {
                outRect.left = topHead/2;
            }
            outRect.top = topHead;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            super.onDrawOver(c, parent, state);
            //获取当前可见的item的数量，不包括分组项，注意区分下面的
            int childCount = parent.getChildCount();
            //获取所有的的item个数,不建议使用Adapter中获取
            int itemCount = state.getItemCount();
            int left = parent.getLeft() + parent.getPaddingLeft();
            int right = parent.getRight() - parent.getPaddingRight();
            String preDate;
            String currentDate = null;
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(view);
                String textLine = callback.getData(position);
                preDate = currentDate;
                currentDate = callback.getData(position);
                if (TextUtils.isEmpty(currentDate) || TextUtils.equals(currentDate, preDate)) {
                    continue;
                }
                if (TextUtils.isEmpty(textLine)) {
                    continue;
                }
                int viewBottom = view.getBottom();
                float textY = Math.max(topHead, view.getTop());
                //下一个和当前不一样移动当前
                if (position + 1 < itemCount) {
                    String nextData = callback.getData(position + 1);
                    if (!currentDate.equals(nextData) && viewBottom < textY) {//组内最后一个view进入了header
                        textY = viewBottom;
                    }
                }
                Rect rect = new Rect(left, (int) textY - topHead, right, (int) textY);
                c.drawRect(rect, paint);
                //绘制文字基线，文字的的绘制是从绘制的矩形底部开始的
                Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
                float baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                textPaint.setTextAlign(Paint.Align.LEFT);
                textPaint.setColor(mDelegate.getCurDayTextColor());
                //绘制文本
                int itemWidth = (MonthRecycleView.this.getWidth() - 2 * mDelegate.getCalendarPadding()) / 7;
                int x = itemWidth/2 + mDelegate.getCalendarPadding();
                c.drawText(textLine,x , baseline, textPaint);
            }
        }

        private boolean isHeader(int pos) {
            if (pos == 0) {
                return true;
            } else {
                String preData = callback.getData(pos - 1);
                String data = callback.getData(pos);
                return !preData.equals(data);
            }
        }

    }

    class MonthRecycleViewAdapter extends Adapter<MonthRecycleViewAdapter.ItemCalendarViewHolder>{

        private Calendar mCurrentDate;

        MonthRecycleViewAdapter(){
            mCurrentDate = new Calendar();
            Date d = new Date();
            mCurrentDate.setYear(CalendarUtil.getDate("yyyy", d));
            mCurrentDate.setMonth(CalendarUtil.getDate("MM", d));
            mCurrentDate.setDay(CalendarUtil.getDate("dd", d));
            mCurrentDate.setCurrentDay(true);
        }

        @NonNull
        @Override
        public ItemCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            BaseMonthView view;
            try {
                Constructor constructor = mDelegate.getMonthViewClass().getConstructor(Context.class);
                view = (BaseMonthView) constructor.newInstance(getContext());
            } catch (Exception e) {
                e.printStackTrace();
                view = new DefaultMonthView(getContext());
            }
            view.setup(mDelegate);
            view.initMonthWithDate(mCurrentDate.getYear(), mCurrentDate.getMonth());
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);


            ItemCalendarViewHolder viewHolder = new ItemCalendarViewHolder(view);
            viewHolder.setItemView(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemCalendarViewHolder holder, int position) {
            int year = (position + mDelegate.getMinYearMonth() - 1) / 12 + mDelegate.getMinYear();
            int month = (position + mDelegate.getMinYearMonth() - 1) % 12 + 1;
            mCurrentPosition = position;
            holder.itemView.initMonthWithDate(year, month);
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            int mCurrentViewHeight = CalendarUtil.getMonthViewHeight(year, month,
                    mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart(),
                    mDelegate.getMonthViewShowMode());

            layoutParams.height = mCurrentViewHeight;
            holder.itemView.setLayoutParams(layoutParams);
            holder.itemView.setTag(position);
        }


        @Override
        public int getItemCount() {
            return mMonthCount;
        }


        class ItemCalendarViewHolder extends ViewHolder{

            private BaseMonthView itemView;

            public ItemCalendarViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public BaseMonthView getItemView() {
                return itemView;
            }

            public void setItemView(BaseMonthView itemView) {
                this.itemView = itemView;
            }
        }
    }
}
