package com.haibin.calendarview

import android.app.Activity
import android.view.View

/**
 *@Author xuyang
 *@Email xuyang@prudencemed.com
 *@Date 2019/8/5.
 *@Description
 */
inline fun findCalendarView(activity: Activity, id: Int, action: CalendarView.() -> Unit): CalendarView {
    val calendarView = activity.findViewById<CalendarView>(id)
    action(calendarView)
    return calendarView
}

inline fun findCalendarView(view: View, id: Int, action: CalendarView.() -> Unit): CalendarView {
    val calendarView = view.findViewById<CalendarView>(id)
    action(calendarView)
    return calendarView
}

inline fun CalendarView.doOnCalendarSelect(crossinline action: (calendar: Calendar?, isClick: Boolean) -> Unit) =
        setOnCalendarSelectListener(onCalendarSelect = action)

inline fun CalendarView.doOnCalendarOutOfRange(crossinline action: (calendar: Calendar?) -> Unit) =
        setOnCalendarSelectListener(onCalendarOutOfRange = action)

inline fun CalendarView.setOnCalendarSelectListener(
        crossinline onCalendarOutOfRange: (outCalendar: Calendar?) -> Unit = {},
        crossinline onCalendarSelect: (calendar: Calendar?, isClick: Boolean) -> Unit = { _, _ -> }
): CalendarView.OnCalendarSelectListener {
    val listener = object : CalendarView.OnCalendarSelectListener {
        override fun onCalendarOutOfRange(calendar: Calendar?) = onCalendarOutOfRange(calendar)
        override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) = onCalendarSelect(calendar, isClick)
    }
    setOnCalendarSelectListener(listener)
    return listener
}

inline fun CalendarView.doOnCalendarIntercept(crossinline action: (calendar: Calendar?) -> Boolean) =
        setOnCalendarInterceptListener(onCalendarIntercept = action)

inline fun CalendarView.doOnCalendarInterceptClick(crossinline action: (calendar: Calendar?, isClick: Boolean) -> Unit) =
        setOnCalendarInterceptListener(onCalendarInterceptClick = action)

inline fun CalendarView.setOnCalendarInterceptListener(
        crossinline onCalendarIntercept: (calendar: Calendar?) -> Boolean = { false },
        crossinline onCalendarInterceptClick: (calendar: Calendar?, isClick: Boolean) -> Unit = { _, _ -> }
): CalendarView.OnCalendarInterceptListener {
    val listener = object : CalendarView.OnCalendarInterceptListener {
        override fun onCalendarIntercept(calendar: Calendar?): Boolean = onCalendarIntercept(calendar)
        override fun onCalendarInterceptClick(calendar: Calendar?, isClick: Boolean) = onCalendarInterceptClick(calendar, isClick)
    }
    setOnCalendarInterceptListener(listener)
    return listener
}

inline fun CalendarView.doOnCalendarLongClickOutOfRange(crossinline action: (calendar: Calendar?) -> Unit) =
        setOnCalendarLongClickListener(onCalendarLongClickOutOfRange = action)

inline fun CalendarView.doOnCalendarLongClick(crossinline action: (calendar: Calendar?) -> Unit) =
        setOnCalendarLongClickListener(onCalendarLongClick = action)

inline fun CalendarView.setOnCalendarLongClickListener(
        crossinline onCalendarLongClickOutOfRange: (calendar: Calendar?) -> Unit = {},
        crossinline onCalendarLongClick: (calendar: Calendar?) -> Unit = {},
        preventLongPressedSelected: Boolean = false
): CalendarView.OnCalendarLongClickListener {
    val listener = object : CalendarView.OnCalendarLongClickListener {
        override fun onCalendarLongClickOutOfRange(calendar: Calendar?) = onCalendarLongClickOutOfRange(calendar)
        override fun onCalendarLongClick(calendar: Calendar?) = onCalendarLongClick(calendar)
    }
    setOnCalendarLongClickListener(listener, preventLongPressedSelected)
    return listener
}

inline fun CalendarView.doOnCalendarMultiSelectOutOfRange(crossinline action: (calendar: Calendar?) -> Unit) =
        setOnCalendarMultiSelectListener(onCalendarMultiSelectOutOfRange = action)

inline fun CalendarView.doOnMultiSelectOutOfSize(crossinline action: (calendar: Calendar?, maxSize: Int) -> Unit) =
        setOnCalendarMultiSelectListener(onMultiSelectOutOfSize = action)

inline fun CalendarView.doOnCalendarMultiSelect(crossinline action: (calendar: Calendar?, curSize: Int, maxSize: Int) -> Unit) =
        setOnCalendarMultiSelectListener(onCalendarMultiSelect = action)

inline fun CalendarView.setOnCalendarMultiSelectListener(
        crossinline onCalendarMultiSelectOutOfRange: (calendar: Calendar?) -> Unit = {},
        crossinline onMultiSelectOutOfSize: (calendar: Calendar?, maxSize: Int) -> Unit = { _, _ -> },
        crossinline onCalendarMultiSelect: (calendar: Calendar?, curSize: Int, maxSize: Int) -> Unit = { _, _, _ -> }
): CalendarView.OnCalendarMultiSelectListener {
    val listener = object : CalendarView.OnCalendarMultiSelectListener {
        override fun onCalendarMultiSelectOutOfRange(calendar: Calendar?) = onCalendarMultiSelectOutOfRange(calendar)
        override fun onMultiSelectOutOfSize(calendar: Calendar?, maxSize: Int) = onMultiSelectOutOfSize(calendar, maxSize)
        override fun onCalendarMultiSelect(calendar: Calendar?, curSize: Int, maxSize: Int) = onCalendarMultiSelect(calendar, curSize, maxSize)
    }
    setOnCalendarMultiSelectListener(listener)
    return listener
}

inline fun CalendarView.doOnCalendarSelectOutOfRange(crossinline action: (calendar: Calendar?) -> Unit) =
        setOnCalendarRangeSelectListener(onCalendarSelectOutOfRange = action)

inline fun CalendarView.doOnSelectOutOfRange(crossinline action: (calendar: Calendar?, isOutOfMinRange: Boolean) -> Unit) =
        setOnCalendarRangeSelectListener(onSelectOutOfRange = action)

inline fun CalendarView.doOnCalendarRangeSelect(crossinline action: (calendar: Calendar?, isEnd: Boolean) -> Unit) =
        setOnCalendarRangeSelectListener(onCalendarRangeSelect = action)

inline fun CalendarView.setOnCalendarRangeSelectListener(
        crossinline onCalendarSelectOutOfRange: (calendar: Calendar?) -> Unit = {},
        crossinline onSelectOutOfRange: (calendar: Calendar?, isOutOfMinRange: Boolean) -> Unit = { _, _ -> },
        crossinline onCalendarRangeSelect: (calendar: Calendar?, isEnd: Boolean) -> Unit = { _, _ -> }
): CalendarView.OnCalendarRangeSelectListener {
    val listener = object : CalendarView.OnCalendarRangeSelectListener {
        override fun onCalendarSelectOutOfRange(calendar: Calendar?) = onCalendarSelectOutOfRange(calendar)
        override fun onSelectOutOfRange(calendar: Calendar?, isOutOfMinRange: Boolean) = onSelectOutOfRange(calendar, isOutOfMinRange)
        override fun onCalendarRangeSelect(calendar: Calendar?, isEnd: Boolean) = onCalendarRangeSelect(calendar, isEnd)
    }
    setOnCalendarRangeSelectListener(listener)
    return listener
}

inline fun<reified T : BaseMonthView> CalendarView.setMonthView() = setMonthView(T::class.java)

inline fun<reified T : BaseWeekView> CalendarView.setWeekView() = setWeekView(T::class.java)