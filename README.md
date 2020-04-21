# CalenderView

An elegant CalendarView on Android platform.
Freely draw UI with canvas, fast、efficient and low memory.
Support month view、 week view、year view、 custom week start、lunar calendar and so on.
Hot plug UI customization!
You can't think of the calendar can be so elegant!

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/screen_recorder.gif" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/screen_recorder_main.gif" height="650"/>

<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/screen_recorder_range.gif" height="650"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/huanghaibin-dev/CalendarView/blob/master/app/src/main/assets/screen_recorder_multi.gif" height="650"/>

### AndroidStudio v3.5+

### support version if using support package
```
implementation 'com.haibin:calendarview:3.6.8'
```

### Androidx version if using Androidx
```
implementation 'com.haibin:calendarview:3.6.9'
```

```
<dependency>
  <groupId>com.haibin</groupId>
  <artifactId>calendarview</artifactId>
  <version>3.6.9</version>
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
