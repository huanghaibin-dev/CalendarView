# CalenderView
Android上一个优雅、高度自定义、性能高效的日历控件，完美支持周视图，支持标记、自定义颜色、农历等。Canvas绘制，速度快、占用内存低，3.0.0版本全新重构，支持简单重新即可实现任意自定义布局、自定义UI，支持收缩展开、性能非常高效，
这个控件内存和效率优势相当明显，而且真正做到收缩+展开，适配多种场景，支持同时多种颜色标记日历事务，更多参考用法请移步Demo，Demo实现了4个精美的自定义效果。

### Gradle
```
compile 'com.haibin:calendarview:3.1.4'
```
```
<dependency>
  <groupId>com.haibin</groupId>
  <artifactId>calendarview</artifactId>
  <version>3.1.4</version>
  <type>pom</type>
</dependency>
```

### 完整用法教程请参照博客教程
[**https://juejin.im/post/5a6743836fb9a01caa20aefc**](https://juejin.im/post/5a6743836fb9a01caa20aefc)

### 如果你需要完全定制UI，参考demo，简单几步即可绘制你需要的效果，一般只需要实现三个回调函数绘制你需要的特效即可，自定义日历UI需要同时自定义周视图，真正做到热插拔效果，方便大众定制UI需求

### 效果预览
### 收缩展开的魅族风格效果
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu_shrink.png" height="650"/>
### 下标和多彩风格
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/index_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/color_expand.png" height="650"/>
### 快速年份月份切换
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/year_view.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/simple_expand.png" height="650"/>


### 使用方法
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
             android:background="#fff"
             app:calendar_card_view="com.haibin.calendarviewproject.simple.SimpleCalendarCardView"
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

        <attr name="calendar_card_view" format="color" /> <!--热插拔自定义类日历月视图路径-->
        <attr name="week_view" format="color" /> <!--热插拔自定义类周视图路径-->
        <attr name="week_bar_view" format="color" /> <!--自定义类周栏路径-->

        <attr name="week_background" format="color" /> <!--星期栏的背景-->
        <attr name="week_text_color" format="color" /> <!--星期栏文本颜色-->

        <attr name="current_day_text_color" format="color" /> <!--今天的文本颜色-->
  
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
        <attr name="max_year" format="integer" /> <!--最大年份2099-->
        <attr name="min_year_month" format="integer" /> <!--最小年份对应月份-->
        <attr name="max_year_month" format="integer" /> <!--最大年份对应月份-->
        
</declare-styleable>
```
### CalendarView api
```java
public int getCurDay(); //今天
public int getCurMonth(); //当前的月份
public int getCurYear(); //今年
public void showSelectLayout(final int year); //快速弹出年份选择月份
public void closeSelectLayout(final int position); //关闭选择年份并跳转日期
@Deprecated
public void setOnDateChangeListener(OnDateChangeListener listener);//添加事件

public void setOnYearChangeListener(OnYearChangeListener listener);//年份切换事件

public void setOnDateSelectedListener(OnDateSelectedListener listener);//日期选择事件
public void setSchemeDate(List<Calendar> mSchemeDate);//标记日期
public void update();//动态更新
public Calendar getSelectedCalendar(); //获取选择的日期

public void scrollToPre();//滚动到上一个月

public void scrollToNext();//滚动到下一个月

public void scrollToCalendar(int year, int month, int day);//滚动到指定日期

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

### 混淆proguard-rules
```java
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
```


### 如果你需要完全定制UI，参考demo，简单几步即可绘制你需要的效果，月视图和周视图需要同时更换，达到UI一致
#### 首先绘制月视图
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

#### 其次绘制周视图，周视图回调几乎一样，唯一的区别是不需要y，只有一行，所以可以直接拷贝代码，把y=0即可
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
