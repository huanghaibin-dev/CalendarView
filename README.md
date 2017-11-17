# CalenderView
Android上一个优雅、高度自定义、性能高效的日历控件，支持标记、自定义颜色、农历等。Canvas绘制，速度快、占用内存低，3.0.0版本全新重构，支持简单重新即可实现任意自定义布局、自定义UI，支持收缩展开、性能非常高效，
简单的说老版本是使用RecyclerView + Canvas 的、这种方式对于自定义控件的复杂度下降了很多，但是相反内存占用却上升了，如果使用一个View替换RecyclerView，则控件数相比会下降130+，每个页面就有43个控件，
3.0.0版本内存和效率优势提升相当明显，而且真正做到收缩+展开，适配多种场景，支持同时多种颜色标记日历事务，更多参考用法请移步Demo。

### Gradle
```
compile 'com.haibin:calendarview:3.0.0'
```
```
<dependency>
  <groupId>com.haibin</groupId>
  <artifactId>calendarview</artifactId>
  <version>3.0.0</version>
  <type>pom</type>
</dependency>
```
### 如果你需要完全定制UI，参考demo，简单几步即可绘制你需要的效果，一般只需要实现三个回调函数绘制你需要的特效即可，真正做到热插拔效果，方便大众定制UI需求

### 效果预览
### 收缩展开的魅族风格效果
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu.jpeg" style="margin-right:20px;" height="650"/><img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/shrink.jpeg" height="650"/>
### 下标和多彩风格
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/index.jpeg" height="650"/><img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/colorful.jpeg" height="650"/>
### 快速年份月份切换
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/month.jpeg" height="650"/><img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/simple.jpeg" height="650"/>


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
             app:calendar_card_view="com.haibin.calendarviewproject.simple.SimpleCalendarCardView"
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

        <attr name="calendar_card_view" format="color" /> <!--热插拔自定义类路径-->

        <attr name="week_background" format="color" /> <!--星期栏的背景-->
        <attr name="week_text_color" format="color" /> <!--星期栏文本颜色-->

        <attr name="current_day_text_color" format="color" /> <!--今天的文本颜色-->

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

        <attr name="min_year" format="integer" />  <!--最小年份1900-->
        <attr name="max_year" format="integer" /> <!--最大年份2099-->
        
</declare-styleable>
```
### CalendarView api
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
### CalendarLayout api
```java
public void expand(); //展开

public void shrink(); //收缩
```

### 如果你需要完全定制UI，参考demo，简单几步即可绘制你需要的效果
```java
public class SimpleCalendarCardView extends BaseCalendarCardView {

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
    protected void onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
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
