# CalenderView
Android上一个优雅、高度自定义、性能高效的日历控件，完美支持周视图，支持标记、自定义颜色、农历等，任意控制月视图显示、任意日期拦截条件、自定义周起始等。Canvas绘制，极速性能、占用内存低，，支持简单定制即可实现任意自定义布局、自定义UI，支持收缩展开、性能非常高效，
这个控件内存和效率优势相当明显，而且真正做到收缩+展开，适配多种场景，支持同时多种颜色标记日历事务，支持多点触控，你真的想不到日历还可以如此优雅！更多参考用法请移步Demo，Demo实现了一些精美的自定义效果，用法仅供参考。

### 插拔式设计

插拔式设计：好比插座一样，插上灯泡就会亮，插上风扇就会转，看用户需求什么而不是看插座有什么，只要是电器即可。此框架使用插拔式，既可以在编译时指定年月日视图，如：app:month_view="xxx.xxx.MonthView.class"，也可在运行时动态更换年月日视图，如：CalendarView.setMonthViewClass(MonthView.Class)，从而达到UI即插即用的效果，相当于框架不提供UI实现，让UI都由客户端实现，不至于日历UI都千篇一律，只需遵守插拔式接口即可随意定制，自由化程度非常高。

[**English Version**](https://github.com/huanghaibin-dev/CalendarView/blob/master/QUESTION.md)

[**详细介绍**](https://github.com/huanghaibin-dev/CalendarView/blob/master/QUESTION_ZH.md)

### AndroidStudio v3.5+

### support版本使用
```
implementation 'com.haibin:calendarview:3.6.8
```

### Androidx版本使用
```
implementation 'com.haibin:calendarview:3.6.9'
```

```
<dependency>
  <groupId>com.haibin</groupId>
  <artifactId>calendarview</artifactId>
  <version>3.6.6</version>
  <type>pom</type>
</dependency>
```


### 混淆proguard-rules
```java
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
```

### 或者针对性的使用混淆，请自行配置测试！
``` java
-keep class your project path.MonthView {
    public <init>(android.content.Context);
}
-keep class your project path.WeekBar {
    public <init>(android.content.Context);
}
-keep class your project path.WeekView {
    public <init>(android.content.Context);
}
-keep class your project path.YearView {
    public <init>(android.content.Context);
}
```

### 效果预览
### 功能性展示
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/main_zh_func.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/main_zh_list.png" height="650"/>
### 年视图和范围选择风格
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/year_view.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/range.png" height="650"/>
### 中国式变态需求风格
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/custom_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/custom_shrink.png" height="650"/>
### 收缩展开的魅族风格效果a
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu_shrink.png" height="650"/>
### 全屏和多彩风格
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/full_calendar.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/color_expand.png" height="650"/>
### 进度条风格
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/progress_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/progress_shrink.png" height="650"/>
### 星系图风格
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/solar_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/solar_shrink.png" height="650"/>

### 特别的，请注意不要复制这三个路径，自行替换您自己的自定义路径

```xml
app:month_view="com.haibin.calendarviewproject.simple.SimpleMonthView"
app:week_view="com.haibin.calendarviewproject.simple.SimpleWeekView"
app:week_bar_view="com.haibin.calendarviewproject.EnglishWeekBar"
```

### 使用方法
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
             app:week_start_with="mon"
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

        <attr name="calendar_padding" format="dimension" /><!--日历内部左右padding-->

        <attr name="month_view" format="color" /> <!--自定义类日历月视图路径-->
        <attr name="week_view" format="string" /> <!--自定义类周视图路径-->
        <attr name="week_bar_height" format="dimension" /> <!--星期栏的高度-->
        <attr name="week_bar_view" format="color" /> <!--自定义类周栏路径，通过自定义则 week_text_color week_background xml设置无效，当仍可java api设置-->
        <attr name="week_line_margin" format="dimension" /><!--线条margin-->

        <attr name="week_line_background" format="color" /><!--线条颜色-->
        <attr name="week_background" format="color" /> <!--星期栏的背景-->
        <attr name="week_text_color" format="color" /> <!--星期栏文本颜色-->
        <attr name="week_text_size" format="dimension" /><!--星期栏文本大小-->

        <attr name="current_day_text_color" format="color" /> <!--今天的文本颜色-->
        <attr name="current_day_lunar_text_color" format="color" /><!--今天的农历文本颜色-->

        <attr name="calendar_height" format="string" /> <!--日历每项的高度，56dp-->
        <attr name="day_text_size" format="string" /> <!--天数文本大小-->
        <attr name="lunar_text_size" format="string" /> <!--农历文本大小-->

        <attr name="scheme_text" format="string" /> <!--标记文本-->
        <attr name="scheme_text_color" format="color" /> <!--标记文本颜色-->
        <attr name="scheme_month_text_color" format="color" /> <!--标记天数文本颜色-->
        <attr name="scheme_lunar_text_color" format="color" /> <!--标记农历文本颜色-->

        <attr name="scheme_theme_color" format="color" /> <!--标记的颜色-->

        <attr name="selected_theme_color" format="color" /> <!--选中颜色-->
        <attr name="selected_text_color" format="color" /> <!--选中文本颜色-->
        <attr name="selected_lunar_text_color" format="color" /> <!--选中农历文本颜色-->

        <attr name="current_month_text_color" format="color" /> <!--当前月份的字体颜色-->
        <attr name="other_month_text_color" format="color" /> <!--其它月份的字体颜色-->

        <attr name="current_month_lunar_text_color" format="color" /> <!--当前月份农历节假日颜色-->
        <attr name="other_month_lunar_text_color" format="color" /> <!--其它月份农历节假日颜色-->

        <!-- 年视图相关 -->
        <attr name="year_view_month_text_size" format="dimension" /> <!-- 年视图月份字体大小 -->
        <attr name="year_view_day_text_size" format="dimension" /> <!-- 年视图月份日期字体大小 -->
        <attr name="year_view_month_text_color" format="color" /> <!-- 年视图月份字体颜色 -->
        <attr name="year_view_day_text_color" format="color" /> <!-- 年视图日期字体颜色 -->
        <attr name="year_view_scheme_color" format="color" /> <!-- 年视图标记颜色 -->

        <attr name="min_year" format="integer" />  <!--最小年份1900-->
        <attr name="max_year" format="integer" />  <!--最大年份2099-->
        <attr name="min_year_month" format="integer" /> <!--最小年份对应月份-->
        <attr name="max_year_month" format="integer" /> <!--最大年份对应月份-->

        <!--月视图是否可滚动-->
        <attr name="month_view_scrollable" format="boolean" />
        <!--周视图是否可滚动-->
        <attr name="week_view_scrollable" format="boolean" />
        <!--年视图是否可滚动-->
        <attr name="year_view_scrollable" format="boolean" />
        
        <!--配置你喜欢的月视图显示模式模式-->
        <attr name="month_view_show_mode">
             <enum name="mode_all" value="0" /> <!--全部显示-->
             <enum name="mode_only_current" value="1" /> <!--仅显示当前月份-->
             <enum name="mode_fix" value="2" /> <!--自适应显示，不会多出一行，但是会自动填充-->
        </attr>

        <!-- 自定义周起始 -->
        <attr name="week_start_with">
             <enum name="sun" value="1" />
             <enum name="mon" value="2" />
             <enum name="sat" value="7" />
        </attr>

        <!-- 自定义选择模式 -->
        <attr name="select_mode">
              <enum name="default_mode" value="0" />
              <enum name="single_mode" value="1" />
              <enum name="range_mode" value="2" />
        </attr>

        <!-- 当 select_mode=range_mode -->
        <attr name="min_select_range" format="integer" />
        <attr name="max_select_range" format="integer" />
</declare-styleable>
```
### CalendarView api
```java

public void setRange(int minYear, int minYearMonth, int minYearDay,
                     int maxYear, int maxYearMonth, int maxYearDay) ;//置日期范围

public int getCurDay(); //今天
public int getCurMonth(); //当前的月份
public int getCurYear(); //今年

public boolean isYearSelectLayoutVisible();//年月份选择视图是否打开
public void closeYearSelectLayout();//关闭年月视图选择布局
public void showYearSelectLayout(final int year); //快速弹出年份选择月份

public void setOnMonthChangeListener(OnMonthChangeListener listener);//月份改变事件

public void setOnYearChangeListener(OnYearChangeListener listener);//年份切换事件

public void setOnCalendarSelectListener(OnCalendarSelectListener listener)//日期选择事件

public void setOnCalendarLongClickListener(OnCalendarLongClickListener listener);//日期长按事件

public void setOnCalendarLongClickListener(OnCalendarLongClickListener listener, boolean preventLongPressedSelect);//日期长按事件

public void setOnCalendarInterceptListener(OnCalendarInterceptListener listener);//日期拦截和日期有效性绘制

public void setSchemeDate(Map<String, Calendar> mSchemeDates);//标记日期

public void update();//动态更新

public Calendar getSelectedCalendar(); //获取选择的日期

/**
 * 特别的，如果你需要自定义或者使用其它选择器，可以用以下方法进行和日历联动
 */
public void scrollToCurrent();//滚动到当前日期

public void scrollToCurrent(boolean smoothScroll);//滚动到当前日期

public void scrollToYear(int year);//滚动到某一年

public void scrollToPre();//滚动到上一个月

public void scrollToNext();//滚动到下一个月

public void scrollToCalendar(int year, int month, int day);//滚动到指定日期

public Calendar getMinRangeCalendar();//获得最小范围日期

public Calendar getMaxRangeCalendar();//获得最大范围日期

/**
  * 设置背景色
  *
  * @param monthLayoutBackground 月份卡片的背景色
  * @param weekBackground        星期栏背景色
  * @param lineBg                线的颜色
 */
public void setBackground(int monthLayoutBackground, int weekBackground, int lineBg)

/**
  * 设置文本颜色
  *
  * @param curMonthTextColor 当前月份字体颜色
  * @param otherMonthColor   其它月份字体颜色
  * @param lunarTextColor    农历字体颜色
 */
public void setTextColor(int curMonthTextColor,int otherMonthColor,int lunarTextColor)

/**
  * 设置选择的效果
  *
  * @param style              选中的style CalendarCardView.STYLE_FILL or CalendarCardView.STYLE_STROKE
  * @param selectedThemeColor 选中的标记颜色
  * @param selectedTextColor  选中的字体颜色
 */
public void setSelectedColor(int style, int selectedThemeColor, int selectedTextColor)

/**
  * 设置标记的色
  *
  * @param style           标记的style CalendarCardView.STYLE_FILL or CalendarCardView.STYLE_STROKE
  * @param schemeColor     标记背景色
  * @param schemeTextColor 标记字体颜色
 */
public void setSchemeColor(int style, int schemeColor, int schemeTextColor)


/**
  * 设置星期栏的背景和字体颜色
  *
  * @param weekBackground 背景色
  * @param weekTextColor  字体颜色
 */
public void setWeeColor(int weekBackground, int weekTextColor)
```
### CalendarLayout api

```java
public void expand(); //展开

public void shrink(); //收缩

public boolean isExpand();//是否展开了
```

### CalendarLayout attrs

```xml

<!-- 日历显示模式 -->
<attr name="calendar_show_mode">
      <enum name="both_month_week_view" value="0" /><!-- 默认都有 -->
      <enum name="only_week_view" value="1" /><!-- 仅周视图 -->
      <enum name="only_month_view" value="2" /><!-- 仅月视图 -->
</attr>

<attr name="default_status">
      <enum name="expand" value="0" /> <!--默认展开-->
      <enum name="shrink" value="1" /><!--默认搜索-->
</attr>

<attr name="calendar_content_view_id" format="integer" /><!--内容布局id-->
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
