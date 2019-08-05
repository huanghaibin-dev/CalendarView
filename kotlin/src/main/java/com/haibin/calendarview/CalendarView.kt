package com.haibin.calendarview

import android.app.Activity
import android.view.View

/**
 *@Author xuyang
 *@Email xuyang@prudencemed.com
 *@Date 2019/8/5.
 *@Description
 */
inline fun findCalendarView(activity: Activity,id : Int,action : CalendarView.() -> Unit) : CalendarView{
    val calendarView = activity.findViewById<CalendarView>(id)
    action(calendarView)
    return calendarView
}

inline fun findCalendarView(view: View, id : Int, action : CalendarView.() -> Unit) : CalendarView{
    val calendarView = view.findViewById<CalendarView>(id)
    action(calendarView)
    return calendarView
}



