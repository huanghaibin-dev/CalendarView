# CalenderView
Android上一个优雅、高度自定义、性能高效的日历控件，支持标记、自定义颜色、农历等。Canvas绘制，速度快、占用内存低，2.0.0版本全新重构、支持收缩展开、性能更加高效，
简单的说老版本是使用RecyclerView + Canvas 的、这种方式对于自定义控件的复杂度下降了很多，但是相反内存占用却上升了，如果使用一个View替换RecyclerView，则控件数相比会下降130+，每个页面就有43个控件，
所以2.0.0版本内存和效率优势提升就相当明显了，而且真正做到收缩+展开，适配多种场景，支持同时多种颜色标记日历事务，更多参考用法请移步MainActivity

### Gradle
```
compile 'com.haibin:calendarview:2.0.1'
```
```
<dependency>
  <groupId>com.haibin</groupId>
  <artifactId>calendarview</artifactId>
  <version>2.0.1</version>
  <type>pom</type>
</dependency>
```
### 使用方法
```xml
 <com.haibin.calendarview.CalendarLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        app:calendar_content_view_id="@+id/recyclerView">

        <com.haibin.calendarview.CalendarView
            android:id="@+id/calendarView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#fff"
            app:current_month_text_color="#333333"
            app:lunar_text_color="#CFCFCF"
            app:min_year="2004"
            app:other_month_text_color="#e1e1e1"
            app:scheme_text="假"
            app:scheme_text_color="#fff"
            app:scheme_theme_color="#2eb654"
            app:scheme_theme_style="fill"
            app:selected_text_color="#333"
            app:selected_theme_color="#108cd4"
            app:show_lunar="true"
            app:selected_theme_style="stroke"
            app:week_background="#fff"
            app:week_text_color="#111" />

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#d4d4d4" />
    </com.haibin.calendarview.CalendarLayout>
```

### attrs
```xml
<declare-styleable name="CalendarView">

        <attr name="week_background" format="color" /> <!--星期栏的背景-->
        <attr name="week_text_color" format="color" /> <!--星期栏文本颜色-->

        <attr name="current_day_text_color" format="color" /> <!--今天的文本颜色-->

        <attr name="scheme_text" format="string" /> <!--标记文本-->

        <attr name="scheme_text_color" format="color" /> <!--标记文本颜色-->

        <attr name="scheme_theme_color" format="color" /> <!--标记的颜色-->

        <attr name="selected_theme_color" format="color" /> <!--选中颜色-->

        <attr name="selected_text_color" format="color" /> <!--选中文本颜色-->

        <attr name="current_month_text_color" format="color" /> <!--当前月份的字体颜色-->
        <attr name="other_month_text_color" format="color" /> <!--其它月份的字体颜色-->

        <attr name="lunar_text_color" format="color" /> <!--农历节假日颜色-->
        <attr name="show_lunar" format="boolean" /> <!--是否显示农历-->


        <attr name="min_year" format="integer" />  <!--最小年份1900-->
        <attr name="max_year" format="integer" /> <!--最大年份2099-->

        <attr name="scheme_theme_style" format="integer"> <!--标记style-->
            <enum name="fill" value="1" />
            <enum name="stroke" value="2" />
        </attr>

        <attr name="selected_theme_style" format="integer"> <!--选择style-->
            <enum name="fill" value="1" />
            <enum name="stroke" value="2" />
        </attr>
        
</declare-styleable>
```
### api
```java
public int getCurDay(); //今天
public int getCurMonth(); //当前的月份
public int getCurYear(); //今年
public void showSelectLayout(final int year); //快速弹出年份选择月份
public void closeSelectLayout(final int position); //关闭选择年份并跳转日期
public void setOnDateChangeListener(OnDateChangeListener listener);//添加事件
public void setOnDateSelectedListener(OnDateSelectedListener listener);//日期选择事件
public void setSchemeDate(List<Calendar> mSchemeDate);//标记日期
public void update();//动态更新
public Calendar getSelectedCalendar(); //获取选择的日期

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

### 效果预览
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/Screenshot_2017-08-30-18-14-09-184_com.haibin.cal.png" height="650"/><img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/Screenshot_2017-08-30-18-14-14-217_com.haibin.cal.png" height="650"/> <img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/Screenshot_2017-08-30-18-14-46-468_com.haibin.cal.png" height="650"/><img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/Screenshot_2017-08-30-18-15-00-435_com.haibin.cal.png" height="650"/><img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/Screenshot_2017-08-30-18-39-16-806_com.haibin.cal.png" height="650"/><img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/Screenshot_2017-08-30-18-39-45-843_com.haibin.cal.png" height="650"/>

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
