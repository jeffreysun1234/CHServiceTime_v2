package com.mycompany.chservicetime.utilities

import java.util.Calendar

fun getCurrentHourAndMinute(): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    return Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
}

fun getTodayOfWeek(): Int = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

fun getCurrentHHmm(): Int {
    val calendar = Calendar.getInstance()
    return getFormatHourMinuteInt(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
}

fun getFormatHourMinuteInt(hour: Int, minute: Int) = hour * 100 + minute

fun getFormatHourMinuteString(hour: Int, minute: Int) =
    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"