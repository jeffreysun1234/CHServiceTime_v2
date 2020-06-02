package com.mycompany.chservicetime.utilities

import java.util.Calendar

fun getCurrentHourAndMinute(): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    return Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
}

fun getFormatHourMinuteInt(hour: Int, minute: Int) = hour * 100 + minute

fun getFormatHourMinuteString(hour: Int, minute: Int) =
    "${hour.toString().padStart(2, '0')}:${hour.toString().padStart(2, '0')}"