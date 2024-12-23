# CalenderView

### RecyclerView? ListView or ViewGroup? No, it's Canvas! Ultra performance and free customization interface! Fast rendering and extremely low memory.

[**Download CalendarViewDemo.apk**](https://github.com/huanghaibin-dev/CalendarView/blob/master/CalendarViewDemo.apk)

# 温馨提醒 Warm tips

Github代码全部开源无限制使用，免费开源最终版本为3.7.1，垂直、水平切换日历、自定义动画高仿iOS日历等源码不再开源。如需全部源码和使用支持，请微信联系，源码200元，无限制使用。

The final version of the free and open source part is 3.7.1, the vertical and horizontal switching calendar liked iOS calendar are no longer open source.

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/WechatIMG65.jpeg" height="330"/>

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/screen_main.gif" height="650"/>&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/new_ios.gif" height="650"/>&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/dark_list.jpg" height="650"/>&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/screen_recorder_flip.gif" height="650"/>

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/simple.jpg" height="650"/>&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/range_select.jpg" height="650"/>&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/full_calendar.jpg" height="650"/>

### AndroidStudio v3.5+

### support version if using support package
```
implementation 'com.haibin:calendarview:3.6.8'
```

### Androidx version if using Androidx
```
implementation 'com.haibin:calendarview:3.7.1'
```

```
<dependency>
  <groupId>com.haibin</groupId>
  <artifactId>calendarview</artifactId>
  <version>3.7.1</version>
  <type>pom</type>
</dependency>
```

## How to use?

[**English Doc**](https://github.com/huanghaibin-dev/CalendarView/blob/master/QUESTION.md)

[**中文使用文档**](https://github.com/huanghaibin-dev/CalendarView/blob/master/QUESTION_ZH.md)

### proguard-rules
```java
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
```

### or using this proguard-rules
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

### Effect Preview

### func
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/main_zh_func.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/main_zh_list.png" height="650"/>
### YearView and Range Style
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/year_view.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/range.png" height="650"/>
### Beautiful Chinese style
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/custom_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/custom_shrink.png" height="650"/>
### Meizu mobile phone calendar
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/meizu_shrink.png" height="650"/>
### Colorful and Full style
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/full_calendar.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/color_expand.png" height="650"/>
### Progress bar style
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/progress_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/progress_shrink.png" height="650"/>
### Galaxy style
<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/solar_expand.png" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/solar_shrink.png" height="650"/>



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
