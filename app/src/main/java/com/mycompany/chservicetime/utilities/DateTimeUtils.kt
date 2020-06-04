package com.mycompany.chservicetime.utilities

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val date_format = "yyyyMMdd:HHmm"
private val threadLocal = ThreadLocal<DateFormat>()

var CURRENT_TIMESTAMP_FLAG = -1

val dateFormat: DateFormat
    get() {
        var df = threadLocal.get()
        if (df == null) {
            df = SimpleDateFormat(date_format, Locale.US)
            threadLocal.set(df)
        }
        return df
    }

fun timestampFormat(timestamp: Long): String {
    return dateFormat.format(timestamp)
}

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

fun getFormatHourMinuteString(hhmm: Int) = getFormatHourMinuteString(hhmm / 100, hhmm % 100)

fun getCalendarByHHmm(hhmm: Int) = Calendar.getInstance().apply {
    timeInMillis = System.currentTimeMillis()
    set(Calendar.HOUR_OF_DAY, hhmm / 100)
    set(Calendar.MINUTE, hhmm % 100)
    set(Calendar.SECOND, 0)
}
