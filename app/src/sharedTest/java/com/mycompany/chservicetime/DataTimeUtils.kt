package com.mycompany.chservicetime

import java.util.Calendar

fun getEndTimeForTest(): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MINUTE, 2)
    return Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
}