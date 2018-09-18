# CalenderView 详细介绍，使用请自行参考Demo！！！

## 介绍
```
此框架采用组合的方式，各个模块互相独立，可自由采用各种提供的控件组合，完全自定义自己需要的UI，
周视图和月视图可通过简单自定义任意自由绘制，不怕美工提需求！！！
```

## 特点
```
1、热插拔设计，根据不同的UI需求完全自定义UI，简单几步即可实现，自定义事件日历标记、颜色、农历等
2、完全Canvas绘制，极速性能，相比大多数基于GridView或RecyclerView实现的占用内存更低，启动速度更快
3、支持收缩、展开、快速年月份选择等
4、支持多种选择模式，场景化需求应有尽有
5、支持简体、繁体、英文等。
6、支持多点触控，多点触控界面不抖动
7、简洁易懂的源码，易学习。
```

## 此框架的设计思想

该框架采用插拔式设计（类似自定义Behavior），框架本身做好了各种逻辑，不对客户端UI做限制，因此界面显示均由客户端自行控制。
如果你需要完全定制UI，参考demo，简单几步即可绘制你需要的效果，一般只需要实现三个回调函数绘制你需要的特效即可，
自定义日历UI需要同时自定义月视图和周视图，真正做到热插拔效果，方便大众定制各种UI需求
插拔式接口如下：

```
<attr name="month_view" format="string" /><!--自定义月视图路径-->
<attr name="week_view" format="string" /> <!--自定义周视图路径-->
<attr name="week_bar_view" format="string" /> <!--自定义星期栏路径-->

```
你可以按自己的需求自定义自己的视图

``` 普通视图
public class CustomMonthView extends MonthView

public class CustomWeekView extends WeekView

public class CustomWeekBar extends WeekBar

```

``` 范围选择视图
public class CustomRangeMonthView extends RangeMonthView

public class CustomRangeWeekView extends RangeWeekView

```
当定义好各种视图之后，可以自行在xml指定路径或者使用java API

```
 app:month_view="com.haibin.calendarviewproject.custom.CustomMonthView"
 app:week_view="com.haibin.calendarviewproject.custom.CustomWeekView"

 mCalendarView.setWeekView(CustomWeekView.class);
 mCalendarView.setMonthView(CustomMonthView.class);
 mCalendarView.setWeekBar(CustomWeekBar.class);

```

## 框架常见问题
1. 为什么仅显示周视图需要指定calendar_content_view_id？
```
月视图收缩后才是周视图，但月视图未收缩部分需要ContentView视图来遮挡，如果还不明白的请将ContentView背景设置为透明就明白了，
同样的，很多场景需要不断变化视图，有时仅显示周视图、有时仅月视图、有时两者都要，该框架可以充分支持这种动态变换。
```
2. 是否支持周末、节日等颜色单独？

```
假如你喜欢小米的日历日期快速跳转，那么你也可以使用类似的控件通过调用各种 CalendarView.scrollToCalendar() API
```

3. 是否可隐藏农历？
```
收到很多这样的issue，框架本身没有这种说法，该显示什么、要不要显示什么，你自己说了算，自行绘制即可，看demo！！！
```

4. 限制日期？拦截日期?
```
<attr name="min_year" format="integer" />
<attr name="max_year" format="integer" />
<attr name="min_year_month" format="integer" />
<attr name="max_year_month" format="integer" />
<attr name="min_year_day" format="integer" />
<attr name="max_year_day" format="integer" />

mCalendarView.setRange();//设置范围

mCalendarView.setOnCalendarInterceptListener(this);//按你的条件来拦截任意日期

```

5. 是否支持自定义周起始？
```
你可以定制周一、周日、周六为视图起始时间，使用 week_start_with attr 或者 mCalendarView.setWeekStarWithXXX()

```

6. 关于UI的一系列问题
```
UI本身是靠自己绘制的，非常简单，不懂的请优先看Demo，看的懂Java自然就能看的懂怎么绘制界面
```

## 高级用法、高级需求

### 场景1. 酒店式日历，这类需求通常是选择日期范围的，当前是不能选择昨天的，一般都是从今天开始、往后几天

步骤1：
```
需要设置select_mode="range_mode"
```

步骤2：
```
分别继承RangeMonthView，RangeWeekView，实现界面绘制即可，参数都非常简单
```

步骤3：
```
你可以动态或静态设置选择范围：
CalendarView.setSelectRange(int minRange, int maxRange);当参数为-1则不限制，其它类推即可
<attr name="min_select_range" format="integer" />
<attr name="max_select_range" format="integer" />
```

步骤4：
```你可以限制一下日期范围如：从今天开始往后多少天
mCalendarView.setRange(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), mCalendarView.getCurDay(),
                mCalendarView.getCurYear() + 2, 12, 31);
```

步骤5：
```当你限制了选择范围为7天之后，因为你的业余需要，你还需要屏蔽其中2天无效
例如范围为：本周三 —— 下周三，周末不可选择，那么你可以实现如下接口：

mCalendarView.setOnCalendarInterceptListener(new CalendarView.OnCalendarInterceptListener() {
      @Override
      public boolean onCalendarIntercept(Calendar calendar) {
            //根据你的场景，拦截任意日期
            return calendar.isWeekend();
      }

      @Override
      public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
           //点击拦截的日期回调
      }
});
你可以在周视图和月视图，得到对应的API：
isInRange(calendar);
bonCalendarIntercept(calendar);
```

### 场景2. 动态变换日历，如更换周起始、设置范围、更换选择模式

#### 需求1：动态变换日历，如你所知，框架支持热插拔，API如下，你需要什么日历，自己决定，即插即用

```
 mCalendarView.setWeekView(CustomWeekView.class);
 mCalendarView.setMonthView(CustomMonthView.class);
```

#### 需求2：根据不同用户爱好、不同地区更换周起始，按需调用即可，日历视图会自动调整

```
mCalendarView.setWeekStarWithSun();
mCalendarView.setWeekStarWithMon();
mCalendarView.setWeekStarWithSat();

```

#### 需求3：更换选择模式

```
mCalendarView.setSelectDefaultMode();
mCalendarView.setSelectSingleMode();
mCalendarView.setSelectRangeMode();
```

#### 需求4：更换月视图显示效果

```
mCalendarView.setAllMode();
mCalendarView.setOnlyCurrentMode();
mCalendarView.setFixMode();

```

### 场景3.超级定制,你可以在月视图和周视图以及Calendar类里面得到很多实用的API

#### Calendar API
```
isWeekend();//判断是不是周末，可以用不同的画笔绘制周末的样式

getWeek();//获取星期

getSolarTerm();//获取24节气，可以用不同颜色标记不同节日

String getGregorianFestival();//获取公历节日

getTraditionFestival();获取传统节日

isLeapYear();//是否是闰年

getLeapMonth();//获取闰月

isSameMonth(Calendar calendar);//是否相同月

compareTo(Calendar calendar);//毕竟日期大小 -1 0 1

getTimeInMillis();//获取时间戳

differ(Calendar calendar);//日期运算，相差多少天

```

#### MonthView、WeekView API
```
onCalendarIntercept(Calendar calendar);//日期是否被拦截不可用，可以用灰色画笔

isInRange(Calendar calendar);//日期是否在范围内，可以用灰色画笔

```

#### RangeMonthView、RangeWeekView API
```
isSelectPreCalendar(Calendar calendar);//上一个日期是否被选中
isSelectNextCalendar(Calendar calendar);//下一个日期是否被选中

```

## 写在最后

框架本身是为了解决各种各样的场景而设计的，不是仅仅针对一个需求的

你可以自由发挥想象力定制最喜欢的日历，只有你想不到

此框架能解决绝大多数场景，但不是100%，有任何疑问可提issue或者直接参考Demo，Demo目前给出了多数场景的解决思路。



