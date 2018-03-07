# CalenderView

An elegant, highly customized and high-performance Calendar Widget on Android is perfect for supporting WeekView, supporting tags, customizing colors, lunar calendar, etc., and multi-functional MonthView display. Canvas draw is fast, low in memory and simple in customization. It can achieve any custom layout and custom UI, and support shrink expansion. The performance is very efficient.
This widget has obvious advantages of memory and efficiency, adapt to many scenes, and support multi colored calendar business. You really can't imagine that calendars can be so elegant. Please see more reference usage Demo, Demo achieved 4 exquisite custom effects.

### Gradle
```
compile 'com.haibin:calendarview:3.2.7'
```
```
<dependency>
  <groupId>com.haibin</groupId>
  <artifactId>calendarview</artifactId>
  <version>3.2.7</version>
  <type>pom</type>
</dependency>
```

### List some of the commonly used questions to support
> * Does it support the weekend colors alone? Support, you need to customize MonthView, WeekView, using Calendar.isWeekend() API to judge whether or not to use the corresponding brush for the weekend.

> * Does it support linkage with other widget? Support, if you like the quick date skip of other widget, you can also use similar widget by invoking API CalendarView.scrollToCalendar(), scrollPre() and scrollNext() etc..

> * Does it support the length of the date? Support, long click to the callback you can do something like sound, vibration effects, this widget does not provide, is not conducive to decoupling.

> * Does it support the use of pictures as a click effect? Support, but you need to be able to use api Canvas.drawBitmap().

> * Does it support grid display? Of course, it's supported. It depends on how you draw it.

> * Does it support to callback WeekBar with date select? Support, you need to customize WeekBar, Override onDateSelected(Calendar calendar, Boolean isClick);

> * Therefore, as long as it is UI appearing in MonthView and WeekView, like pentagram, Bessel curve, picture and so on, are all supported, as long as you can draw, Weather it is beautiful or not, it depends on you.


### proguard-rules
```java
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
```

### If you want to use the English version, it means you don't need to use the lunar calendar, customize MonthView and WeekView, and refer to Demo.

### Effect preview

###
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/custom_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/custom_shrink.png" height="650"/>
###
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu_shrink.png" height="650"/>
###
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/index_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/color_expand.png" height="650"/>
###
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/progress_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/progress_shrink.png" height="650"/>
###
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/solar_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/solar_shrink.png" height="650"/>
###
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/year_view.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/simple_expand.png" height="650"/>



### usage
```xml
 <com.haibin.calendarview.CalendarLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        app:default_status="shrink"
        app:calendar_show_mode="only_week_view"
        app:calendar_content_view_id="@+id/recyclerView">

        <com.haibin.calendarview.CalendarView
             android:id="@+id/calendarView"
             android:layout_width="match_parent"
             android:layout_height="wrap_content"
             android:background="#fff"
             app:month_view="com.haibin.calendarviewproject.simple.SimpleCalendarCardView"
             app:week_view="com.haibin.calendarviewproject.simple.SimpleWeekView"
             app:week_bar_view="com.haibin.calendarviewproject.EnglishWeekBar"
             app:calendar_height="50dp"
             app:current_month_text_color="#333333"
             app:current_month_lunar_text_color="#CFCFCF"
             app:min_year="2004"
             app:other_month_text_color="#e1e1e1"
             app:scheme_text="假"
             app:scheme_text_color="#333"
             app:scheme_theme_color="#333"
             app:selected_text_color="#fff"
             app:selected_theme_color="#333"
             app:week_background="#fff"
             app:month_view_show_mode="mode_only_current"
             app:week_text_color="#111" />

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#d4d4d4" />
    </com.haibin.calendarview.CalendarLayout>
```

### CalendarView attrs
```xml
<declare-styleable name="CalendarView">

        <attr name="month_view" format="color" /> <!--Custom class MonthView path-->
        <attr name="week_view" format="color" /> <!--Custom WeekView path-->
        <attr name="week_bar_height" format="dimension" /> <!--WeekBar height-->
        <attr name="week_bar_view" format="color" /> <!--Custom class WeekBar path, invalid by custom week_text_color week_background XML settings, when still Java API settings can still be set-->

        <attr name="week_line_background" format="color" /><!--line color-->
        <attr name="week_background" format="color" /> <!--WeekBar background-->
        <attr name="week_text_color" format="color" /> <!--WeekBar textColor-->

        <attr name="current_day_text_color" format="color" /> <!--today textColor-->
        <attr name="current_day_lunar_text_color" format="color" /><!--today lunarTextColo-->
  
        <attr name="calendar_height" format="string" /> <!--MonthView item height default 56dp-->
        <attr name="day_text_size" format="string" /> <!--day textSize-->
        <attr name="lunar_text_size" format="string" /> <!--lunar textSize-->

        <attr name="scheme_text" format="string" /> <!--scheme text-->
        <attr name="scheme_text_color" format="color" /> <!--scheme Color-->
        <attr name="scheme_month_text_color" format="color" /> <!--scheme calendar textColor-->
        <attr name="scheme_lunar_text_color" format="color" /> <!--scheme calendar lunarTextColor-->

        <attr name="scheme_theme_color" format="color" /> <!--scheme theme color-->

        <attr name="selected_theme_color" format="color" /> <!--selected theme color-->
        <attr name="selected_text_color" format="color" /> <!--selected textColor-->
        <attr name="selected_lunar_text_color" format="color" /> <!--selected lunarTextColor-->

        <attr name="current_month_text_color" format="color" /> <!--current month textColor-->
        <attr name="other_month_text_color" format="color" /> <!--other month textColor-->

        <attr name="current_month_lunar_text_color" format="color" /> <!--current month lunar textColor-->
        <attr name="other_month_lunar_text_color" format="color" /> <!--other month lunar textColor-->

        <!-- YearView attr -->
        <attr name="year_view_month_text_size" format="dimension" /> <!-- YearView month textSize-->
        <attr name="year_view_day_text_size" format="dimension" /> <!-- YearView day textSize -->
        <attr name="year_view_month_text_color" format="color" /> <!-- YearView month textColor -->
        <attr name="year_view_day_text_color" format="color" /> <!-- YearView day textColor -->
        <attr name="year_view_scheme_color" format="color" /> <!-- YearView scheme textColor -->

        <attr name="min_year" format="integer" />  <!--min year as 1900-->
        <attr name="max_year" format="integer" /> <!--max year as 2099-->
        <attr name="min_year_month" format="integer" /> <!--min month of min year-->
        <attr name="max_year_month" format="integer" /> <!--max month of max year-->
        
        <!--month_view_show_mode-->
        <attr name="month_view_show_mode">
             <enum name="mode_all" value="0" /> <!--show all month-->
             <enum name="mode_only_current" value="1" /> <!--show only current month-->
             <enum name="mode_fix" value="2" /> <!--auto fix,like mode_only_current,but auto filling-->
        </attr>
</declare-styleable>
```
### CalendarView api
```java

public void setRange(int minYear, int minYearMonth, int maxYear, int maxYearMonth);//date range

public int getCurDay(); //get today
public int getCurMonth(); //get currentMonth
public int getCurYear(); //get currentYear

public boolean isYearSelectLayoutVisible();//is yearViewSelectedLayout visible?
public void closeYearSelectLayout();//close yearViewSelectedLayout
public void showYearSelectLayout(final int year); //show yearViewSelectedLayout

public void setOnMonthChangeListener(OnMonthChangeListener listener);//month change listener

public void setOnYearChangeListener(OnYearChangeListener listener);//year change listener

public void setOnDateSelectedListener(OnDateSelectedListener listener);//date selected listener

public void setOnDateLongClickListener(OnDateLongClickListener listener);//date long click listener

public void setSchemeDate(List<Calendar> mSchemeDate);//mark dates

public void update();//update change

public Calendar getSelectedCalendar(); //get current selected calendar

/**
 * In particular, if you need to customize or use other selectors, you can use the following methods to interact with the calendar.
 */
public void scrollToCurrent();

public void scrollToCurrent(boolean smoothScroll);

public void scrollToYear(int year);

public void scrollToYear(int year,boolean smoothScroll);

public void scrollToPre();

public void scrollToPre(boolean smoothScroll);

public void scrollToNext();

public void scrollToNext(boolean smoothScroll);

public void scrollToCalendar(int year, int month, int day);

public void scrollToCalendar(int year, int month, int day,boolean smoothScroll);

/**
  * setBackground
  *
  * @param yearLayoutBackground yearSelectLayout
  * @param weekBackground        WeekBar
  * @param lineBg                Line
 */
public void setBackground(int yearLayoutBackground, int weekBackground, int lineBg)

/**
  * setTextColor
  *
  * @param curMonthTextColor curMonthTextColor
  * @param otherMonthColor   otherMonthColor
  * @param lunarTextColor    lunarTextColor
 */
public void setTextColor(int curMonthTextColor,int otherMonthColor,int lunarTextColor)




/**
  * setWeeColor
  *
  * @param weekBackground 背景色
  * @param weekTextColor  字体颜色
 */
public void setWeeColor(int weekBackground, int weekTextColor)
```
### CalendarLayout api

```java
public void expand(); //expand

public void shrink(); //shrink

public boolean isExpand();//isExpand
```

### CalendarLayout attrs

```xml
<attr name="default_status">
      <enum name="expand" value="0" /> <!--expand-->
      <enum name="shrink" value="1" /><!--shrink-->
      <attr name="calendar_show_mode">
             <enum name="both_month_week_view" value="0" /><!-- default -->
             <enum name="only_week_view" value="1" /><!-- only week view -->
             <enum name="only_month_view" value="2" /><!-- only month view -->
      </attr>
      <attr name="calendar_content_view_id" format="integer" /><!-- content view id -->
</attr>
<attr name="only_week_view" format="boolean" /><!--only month-->
<attr name="calendar_content_view_id" format="integer" /><!--calendar_content_view_id-->
```

### If you need to fully customize the UI, refer to the demo, a few steps can be done to draw the effect you need. The MonthView and WeekView need to be replaced at the same time to achieve UI consistency.
#### First draw the MonthView
```java
public class SimpleCalendarCardView extends MonthView {

    private int mRadius;

    public SimpleCalendarCardView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
```

#### Second, draw the WeekView, the WeekView is almost the same, the only difference is that you do not need y, only one line, so you can copy the code directly, and then make y=0.
```java
public class SimpleWeekView extends WeekView{
    private int mRadius;


    public SimpleWeekView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy =  mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine ;
        int cx = x + mItemWidth / 2;
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mSchemeTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mCurMonthTextPaint);
        }
    }
}
```

## Licenses
- Copyright (C) 2013 huanghaibin_dev <huanghaibin_dev@163.com>
 
- Licensed under the Apache License, Version 2.0 (the "License");
- you may not use this file except in compliance with the License.
- You may obtain a copy of the License at
 
-         http://www.apache.org/licenses/LICENSE-2.0
 
- Unless required by applicable law or agreed to in writing, software
- distributed under the License is distributed on an "AS IS" BASIS,
- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
- See the License for the specific language governing permissions and
  limitations under the License.
