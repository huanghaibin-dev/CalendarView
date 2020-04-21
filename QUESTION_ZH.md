# CalendarView使用详细文档

### 日历控件定制是移动开发平台上比较常见的而且比较难的需求，一般会遇到以下问题：
- **性能差，加载速度慢**，原因是各种基于GridView或RecyclerView等ViewGroup实现的日历，控件数太多，假设一个月视图界面有42个item，每个item里面分别就有2个子TextView：天数、农历数和本身3个控件，这样一个月视图就有42 \* 3+1（RecyclerView or GridView），清楚ViewPager特性的开发者就会明白，一般ViewPager持有3个item，那么一个日历控件持有的View控件数的数量将达到 1（ViewPager）+ 3(RecyclerView or GridView) + 3 \* 42 \* 3 = **382**，如果用1个View来代替RecyclerView等，用Canvas来代替各种TextView，那View的数量瞬间将下降360+，内存和性能优势将相当明显了
- **难定制** 一般日历框架发布的同时也将UI风格确定下来了，假如人人都使用这个日历框架，那么将会千篇一律，难以突出自己的风格，要么就得改源码，成本太大，不太实际
- **功能性不足** 例如无法自定义周起始、无法更改选择模式、动态设置UI等等
- **无法满足产品经理提出的变态需求** 今天产品经历说我们要这样的实现、明天跟你说这里得改、后天说我们得限制一些日期...

### 但现在有了全新的 **CalendarView** 控件，它解锁了各种姿势，而且你可以任意定制，直到你满足为止...

### 插拔式设计

插拔式设计：好比插座一样，插上灯泡就会亮，插上风扇就会转，看用户需求什么而不是看插座有什么，只要是电器即可。此框架使用插拔式，既可以在编译时指定年月日视图，如：app:month_view="xxx.xxx.MonthView.class"，也可在运行时动态更换年月日视图，如：CalendarView.setMonthViewClass(MonthView.Class)，从而达到UI即插即用的效果，相当于框架不提供UI实现，让UI都由客户端实现，不至于日历UI都千篇一律，只需遵守插拔式接口即可随意定制，自由化程度非常高。


### **CalendarView** 的特性

- 基于Canvas绘制，极速性能
- 热插拔思想，任意定制周视图、月视图，即插即用！
- 支持单选、多选、范围选择、国内手机日历默认自动选择等选择模式
- 支持静态、动态设置周起始，一行代码搞定
- 支持静态、动态设置日历项高度、日历填充模式
- 支持设置任意日期范围、任意拦截日期
- 支持多点触控、手指平滑切换过渡，拒绝界面抖动
- 类NestedScrolling特性，嵌套滚动
- 既然这么多支持，那一定支持英语、繁体、简体，任意定制实现

### **注意：** 框架本身只是实现各自逻辑，不实现UI，UI如同一张白纸，任凭客户端自行通过Canvas绘制实现，如果不熟悉Canvas的，请自行了解各自Canvas.drawXXX方法，UI都靠Canvas实现，坐标都已经计算好了，因此怎么隐藏农历，怎么换某些日期的字，这些都不属于框架范畴，只要你想换，都能随便换。

### **再次注意：** app Demo只是Demo，只是示例如何使用，与框架本身没有关联，不属于框架一部分

### 接下来请看**CalendarView**操作，前方高能

- 你这样继承自己的月视图和周视图，只需要依次实现绘制选中：**onDrawSelected**、绘制事务：**onDrawScheme**、绘制文本：**onDrawText** 这三个回调即可，参数和坐标都已经在回调函数上实现好，周视图也是一样的逻辑，只是不需要y参数

```java
/**
 * 定制高仿魅族日历界面，按你的想象力绘制出各种各样的界面
 *
 */
public class MeiZuMonthView extends MonthView {

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        //这里绘制选中的日子样式，看需求需不需要继续调用onDrawScheme
        return true;
    }

    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
       //这里绘制标记的日期样式，想怎么操作就怎么操作
    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        //这里绘制文本，不要再问我怎么隐藏农历了，不要再问我怎么把某个日期换成特殊字符串了，要怎么显示你就在这里怎么画，你不画就不显示，是看你想怎么显示日历的，而不是看框架
    }
}
```

- 当你实现好之后，直接在xml界面上添加特性，编译后可以即时预览效果：

```xml

<attr name="month_view" format="string" /><!--自定义月视图路径-->
<attr name="week_view" format="string" /> <!--自定义周视图路径-->

app:month_view="com.haibin.calendarviewproject.MeiZuMonthView"
app:week_view="com.haibin.calendarviewproject.MeiZuWeekView"

```

- 视图有多种模式可供选择，几乎涵盖了各种需求，看各自的需求自行继承

```xml

如果继承这2个，MonthView、WeekView，即select_mode="default_mode"，这是默认的手机自带的日历模式，会自动选择月的第一天，不支持拦截器，
也可以设置select_mode="single_mode"，即单选模式，支持拦截器

如果继承这2个，RangeMonthView、RangeWeekView，即select_mode="range_mode"，这是范围选择模式，支持拦截器

如果继承这2个，MultiMonthView、MultiWeekView，即select_mode="multi_mode"，这是多选模式，支持拦截器

```


- 如果静态模式无法满足你的需求，你可能需要动态变换定制的视图界面，你可以使用热插拔特性，即插即用，不爽就换：

```java

mCalendarView.setWeekView(MeiZuWeekView.class);

mCalendarView.setMonthView(MeiZuMonthView.class);

```

- 如果你需要可收缩的日历，你可以在 **CalendarView** 父布局添加 **CalendarLayout**，当然你不需要周视图也可以不用，例如原生日历，使用如下：

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
             app:month_view="com.haibin.calendarviewproject.simple.SimpleMonthView"
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
            android:background="#ffffff" />
    </com.haibin.calendarview.CalendarLayout>
```

- 使用可收缩的日历你可以使用监听器，监听视图变换

```java

public void setOnViewChangeListener(OnViewChangeListener listener);

```

- 当然 **CalendarLayout** 有很多特性可提供周月视图无缝切换，而且，平滑手势不抖动！使用 **CalendarLayout**，你需要指定 **calendar_content_view_id**，用他来平移收缩月视图，更多特性如下：

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

<attr name="calendar_content_view_id" format="integer" /><!--内容布局id，用于提供月视图平移过渡-->

```

- **CalendarView** 可以设置全屏，只需设置 app:calendar_match_parent="true"即可，全屏**CalendarView**是不需要周视图的，不必嵌套CalendarLayout

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/full_calendar.png" width="400"/>


- **CalendarView** 也提供了高效便利的年视图，可以快速切换年份、月份，十分便利

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/year_view.png" width="400"/>


- 但年视图也不一定就适合你的胃口，如果你希望像弹出 **DatePickerView**，通过它来跳转日期，你可以使用以下的API来让日历与其它控件联动
```

CalendarView.scrollToCalendar();

CalendarView.scrollToNext();

CalendarView.scrollToPre();

CalendarView.scrollToXXX();

```

- 你也许需要像魅族日历一样，可以静态、动态更换周起始
```

app:week_start_with="mon、sun、sat"

CalendarView.setWeekStarWithSun();

CalendarView.setWeekStarWithMon();

CalendarView.setWeekStarWithSat();

```

- 假如你是做酒店、旅游等应用场景的APP的，那么需要可选范围的日历，你可以这样继承，和普通视图实现完全一样

```java
public class CustomRangeMonthView extends RangeMonthView{

}

public class CustomRangeWeekView extends RangeWeekView{

}

```

- 然后你需要设置选择模式为范围模式：**select\_mode="range\_mode"**


- 酒店式日历场景当然是不能从昨天开始订房的，也不能无限期订房，所以你需要静态或动态设置日历范围、精确到具体某一天！！！

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

- 当然还有更特殊的日子也是不能选择的，例如：某月某号起这N天时间内因为超强台风来袭，酒店需停止营业N天，这段期间不可订房，这时日期拦截器就排上用场了
```java
//设置日期拦截事件
mCalendarView.setOnCalendarInterceptListener(new CalendarView.OnCalendarInterceptListener() {
     @Override
     public boolean onCalendarIntercept(Calendar calendar) {
         //这里写拦截条件，返回true代表拦截，尽量以最高效的代码执行
         return calendar.isWeekend();
     }

     @Override
     public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
         //todo 点击拦截的日期回调
     }
});
```

- 添加日期拦截器和范围设置后，你可以在周月视图按需求获得他们的结果
```java

boolean isInRange = isInRange(calendar);//日期是否在范围内，超出范围的可以置灰

boolean isEnable = !onCalendarIntercept(calendar);//日期是否可用，没有被拦截，被拦截的可以置灰

```

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/range.png" height="650"/>


- 假如你是做清单类、任务类APP的，可能会有这样的需求：标记某天事务的进度，这也很简单，因为：日历界面长什么样，你自己说了算！！！

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/progress_expand.png" width="400"/>


- 也许你只需要像原生日历那样就够了，但原生日历那奇怪且十分不友好的style，受到theme的影响，各种头疼，使用此控件，你只需要简简单单定制月视图就够了，**CalendarView** 能非常简单就高仿各种日历UI

- **CalendarView** 提供了 **setSchemeDate(Map<String, Calendar> mSchemeDates)** 这个十分高效的API用来动态标记事务，即时你的数据量达到数千、数万、数十万，都不会对UI渲染造成影响

- 日历类 **Calendar** 提供了许多十分有用的API

```java
boolean isWeekend();//判断是不是周末，可以用不同的画笔绘制周末的样式

int getWeek();//获取星期

String getSolarTerm();//获取24节气，可以用不同颜色标记不同节日

String getGregorianFestival();//获取公历节日,自由判断，把节日换上喜欢的颜色

String getTraditionFestival();//获取传统节日

boolean isLeapYear();//是否是闰年

int getLeapMonth();//获取闰月

boolean isSameMonth(Calendar calendar);//是否相同月

int compareTo(Calendar calendar);//比较日期大小 -1 0 1

long getTimeInMillis();//获取时间戳

int differ(Calendar calendar);//日期运算，相差多少天
```

### **CalendarView** 的全部xml特性如下：

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
              <enum name="multi_mode" value="3" />
        </attr>

        <!-- when select_mode = multi_mode -->
        <attr name="max_multi_select_size" format="integer" />

        <!-- 当 select_mode=range_mode -->
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

### 写在最后，其它各种场景姿势就不多说了，看自己需求去实现。**再次注意：Demo只是Demo，只是示例如何使用，与框架本身没有关联，不属于框架一部分**
### 框架本身是为了解决各种各样的场景而设计的，UI本身是靠自己绘制的，非常简单，如果还不懂的请优先看Demo，你可以自由发挥想象力定制最喜欢的日历，只有你想不到，Demo基本给出了各种场景的实现思路。觉得可以的请给个star或者留下你宝贵的意见。