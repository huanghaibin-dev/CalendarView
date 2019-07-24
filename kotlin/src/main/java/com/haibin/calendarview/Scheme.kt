package com.haibin.calendarview

/**
 *@Author xuyang
 *@Email youtouchyang@sina.com
 *@Date 2019/7/24.
 *@Description
 */
import com.haibin.calendarview.Calendar.Scheme

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun<reified T : Any> Scheme.getObj():T = obj as T