# CalendarView doc

### **CalendarView** Characteristics

- Based on Canvas, Extreme speed
- Hot plug UI customization
- Support month view、 week view、year view、lunar calendar and so on
- Support static or dynamic setting of week start
- Support for static or dynamic setting of calendar item height
- Support date interceptor and range
- Support multi-touch, smooth scrolling calendar
- Support like NestedScrolling
- All UI depends on your drawing


### 1、Create a class extends the **MonthView** or **RangeMonthView**、**MultiMonthView**.
### If you need WeekView,  also create a class extends the **WeekView** or **RangeWeekView**、**MultiWeekView**.

- Implement these three methods ：**onDrawSelected** **onDrawScheme** **onDrawText**, like this

```java
/**
 * CustomMonthView with canvas
 */
public class CustomMonthView extends MonthView {

    /**
     * draw select calendar
     *
     * @param canvas    canvas
     * @param calendar  select calendar
     * @param x         calendar item x start point
     * @param y         calendar item y start point
     * @param hasScheme is calendar has scheme?
     * @return if return true will call onDrawScheme again
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        canvas.drawRect(x , y , x + mItemWidth , y + mItemHeight, mSelectedPaint);
        return true;
    }

    /**
     * draw scheme if calendar has scheme
     *
     * @param canvas   canvas
     * @param calendar calendar has scheme
     * @param x        calendar item x start point
     * @param y        calendar item y start point
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
       canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    /**
     * draw text
     *
     * @param canvas     canvas
     * @param calendar   calendar
     * @param x          calendar item x start point
     * @param y          calendar item y start point
     * @param hasScheme  is calendar has scheme?
     * @param isSelected is calendar selected?
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;
        canvas.drawText(String.valueOf(calendar.getDay()),
                            cx,
                            baselineY,
                            calendar.isCurrentDay() ? mCurDayTextPaint :
                                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
    }
}
```

- Add the class path in xml

```xml

<attr name="month_view" format="string" /><!--you MonthView.class path-->
<attr name="week_view" format="string" /> <!--you WeekView.class path-->

<com.haibin.calendarview.CalendarView
            android:id="@+id/calendarView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:month_view="com.haibin.calendarviewproject.meizu.MeiZuMonthView"
            app:week_view="com.haibin.calendarviewproject.meizu.MeizuWeekView" />

```

- You can also use the hot plug feature, Quickly switch UI styles

```java

mCalendarView.setWeekView(MeiZuWeekView.class);

mCalendarView.setMonthView(MeiZuMonthView.class);

```

- Calendar has multiple view modes to choose depending on your needs

```xml

MonthView、WeekView，and select_mode="default_mode"
Same as mobile phone calendar,date interceptor is not supported

```

```xml

MonthView、WeekView，and select_mode="single_mode"
Single mode, support date interceptor

```

```xml

RangeMonthView、RangeWeekView，need set select_mode="range_mode"
support date intercept

```

```xml
MultiMonthView、MultiWeekView，need set select_mode="multi_mode"
support date intercept

```

- If you need WeekView，Add parent layout **CalendarLayout** in **CalendarView**

```xml
<com.haibin.calendarview.CalendarLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        app:default_status="shrink"
        app:calendar_content_view_id="@+id/recyclerView">

        <com.haibin.calendarview.CalendarView
             android:id="@+id/calendarView"
             android:layout_width="match_parent"
             android:layout_height="wrap_content"
             app:month_view="com.haibin.calendarviewproject.simple.SimpleMonthView"
             app:week_view="com.haibin.calendarviewproject.simple.SimpleWeekView"
             app:week_bar_view="com.haibin.calendarviewproject.EnglishWeekBar"
             app:month_view_show_mode="mode_only_current" />

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#ffffff" />
    </com.haibin.calendarview.CalendarLayout>
```

- You can use the listener to monitor the WeekView and MonthView switch

```java

public void setOnViewChangeListener(OnViewChangeListener listener);

```

- All off **CalendarLayout** attr，Smooth scrolling calendar. If used **CalendarLayout**，you need set **calendar_content_view_id**

```xml

<attr name="default_status">
      <enum name="expand" value="0" />
      <enum name="shrink" value="1" />
</attr>


<!-- gesture -->
<attr name="gesture_mode">
      <enum name="default_mode" value="0" />
      <enum name="disabled" value="2" />
</attr>

<attr name="calendar_show_mode">
      <enum name="both_month_week_view" value="0" />
      <enum name="only_week_view" value="1" />
      <enum name="only_month_view" value="2" />
</attr>

<attr name="calendar_content_view_id" format="integer" />
```

- If you need a full style **CalendarView**, make app:calendar_match_parent="true", No need to use CalendarLayout

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/full_calendar.png" width="400"/>


- The **CalendarView** also provides a YearView if you need it

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/year_view.png" width="400"/>


- If you need to quick jump date
```

CalendarView.scrollToCalendar();

CalendarView.scrollToNext();

CalendarView.scrollToPre();

CalendarView.scrollToXXX();

```

- Support static or dynamic setting of week start
```

app:week_start_with="mon、sun、sat"

mCalendarView.setWeekStarWithSun();

mCalendarView.setWeekStarWithMon();

mCalendarView.setWeekStarWithSat();

```

- Set calendar range

```

<attr name="min_year" format="integer" />
<attr name="max_year" format="integer" />
<attr name="min_year_month" format="integer" />
<attr name="max_year_month" format="integer" />
<attr name="min_year_day" format="integer" />
<attr name="max_year_day" format="integer" />

CalendarView.setRange(int minYear, int minYearMonth, int minYearDay,
         int maxYear, int maxYearMonth, int maxYearDay)

```

- Add OnCalendarInterceptListener to intercept calendar

```java
mCalendarView.setOnCalendarInterceptListener(new CalendarView.OnCalendarInterceptListener() {
     @Override
     public boolean onCalendarIntercept(Calendar calendar) {

         return calendar.isWeekend();
     }

     @Override
     public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
         //todo 点击拦截的日期回调
     }
});
```

- You can get interception and range results in month and week view

```java

boolean isInRange = isInRange(calendar);

boolean isEnable = !onCalendarIntercept(calendar);

```

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/range.png" height="650"/>


- The **CalendarView** provides scheme setting, **setSchemeDate(Map<String, Calendar> mSchemeDates)**

- class **Calendar**

```java
boolean isWeekend();

int getWeek();

String getSolarTerm();

String getGregorianFestival();

String getTraditionFestival();

boolean isLeapYear();

int getLeapMonth();

boolean isSameMonth(Calendar calendar);

int compareTo(Calendar calendar);

long getTimeInMillis();

int differ(Calendar calendar);
```

### All of **CalendarView** attr

```xml
<declare-styleable name="CalendarView">

        <attr name="calendar_padding" format="dimension" /><!--CalendarView left and right padding-->

        <attr name="month_view" format="color" /> <!--Custom class path-->
        <attr name="week_view" format="string" /> <!--Custom class path-->
        <attr name="week_bar_height" format="dimension" /> <!--WeekBar height-->
        <attr name="week_bar_view" format="string" /> <!--Custom WeekBar class path -->
        <attr name="week_line_margin" format="dimension" /><!--line margin-->

        <attr name="week_line_background" format="color" /><!--line color-->
        <attr name="week_background" format="color" /> <!--WeekBar background-->
        <attr name="week_text_color" format="color" /> <!--WeekBar text color-->
        <attr name="week_text_size" format="dimension" /><!--WeekBar text size-->

        <attr name="current_day_text_color" format="color" /> <!--current day text color-->
        <attr name="current_day_lunar_text_color" format="color" /><!--current day lunar text color-->

        <attr name="calendar_height" format="string" /> <!--calendar item height-->
        <attr name="day_text_size" format="string" /> <!--calendar day text size-->
        <attr name="lunar_text_size" format="string" /> <!--calendar lunar text size-->

        <attr name="scheme_text_color" format="color" /> <!--calendar scheme text color-->
        <attr name="scheme_month_text_color" format="color" /> <!--calendar has scheme month text color-->
        <attr name="scheme_lunar_text_color" format="color" /> <!--calendar has scheme lunar text color-->

        <attr name="scheme_theme_color" format="color" /> <!-- calendar scheme background theme color-->

        <attr name="selected_theme_color" format="color" /> <!--selected calendar theme color-->
        <attr name="selected_text_color" format="color" /> <!--selected calendar text color-->
        <attr name="selected_lunar_text_color" format="color" /> <!--selected calendar lunar text color-->

        <attr name="current_month_text_color" format="color" /> <!--current month text color-->
        <attr name="other_month_text_color" format="color" /> <!--other month text color-->

        <attr name="current_month_lunar_text_color" format="color" /> <!--current month lunar text color-->
        <attr name="other_month_lunar_text_color" format="color" /> <!--other month lunar text color-->

        <!-- YearView -->
        <attr name="year_view_month_text_size" format="dimension" /> <!-- year view month text size -->
        <attr name="year_view_day_text_size" format="dimension" /> <!-- year_view_day_text_size -->
        <attr name="year_view_month_text_color" format="color" /> <!-- year_view_month_text_color -->
        <attr name="year_view_day_text_color" format="color" /> <!-- year_view_day_text_color -->
        <attr name="year_view_scheme_color" format="color" /> <!-- year_view_scheme_color -->

        <attr name="min_year" format="integer" />  <!--min_year-->
        <attr name="max_year" format="integer" />  <!--max_year-->
        <attr name="min_year_month" format="integer" /> <!--min_year_month-->
        <attr name="max_year_month" format="integer" /> <!--max_year_month-->

        <!--MonthView can scrollable-->
        <attr name="month_view_scrollable" format="boolean" />
        <!--WeekView can scrollable-->
        <attr name="week_view_scrollable" format="boolean" />
        <!--YearView can scrollable-->
        <attr name="year_view_scrollable" format="boolean" />
        
        <!--MonthView show mode-->
        <attr name="month_view_show_mode">
             <enum name="mode_all" value="0" /> <!--show pre cur next month calendar-->
             <enum name="mode_only_current" value="1" /> <!--show only current month-->
             <enum name="mode_fix" value="2" /> <!--fix current month-->
        </attr>

        <!-- WeekStar -->
        <attr name="week_start_with">
             <enum name="sun" value="1" />
             <enum name="mon" value="2" />
             <enum name="sat" value="7" />
        </attr>

        <!-- select_mode -->
        <attr name="select_mode">
              <enum name="default_mode" value="0" />
              <enum name="single_mode" value="1" />
              <enum name="range_mode" value="2" />
              <enum name="multi_mode" value="3" />
        </attr>

        <!-- when select_mode = multi_mode -->
        <attr name="max_multi_select_size" format="integer" />

        <!-- when select_mode=range_mode -->
        <attr name="min_select_range" format="integer" />
        <attr name="max_select_range" format="integer" />
        
        <!-- auto select day -->
        <attr name="month_view_auto_select_day">
              <enum name="first_day_of_month" value="0" />
              <enum name="last_select_day" value="1" />
              <enum name="last_select_day_ignore_current" value="2" />
        </attr>
</declare-styleable>

```

### Better use with Demo when using.The calendar is very simple to customize