package com.mycompany.chservicetime.utilities

import java.util.Calendar

fun getCurrentHourAndMinute(): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    return Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
}