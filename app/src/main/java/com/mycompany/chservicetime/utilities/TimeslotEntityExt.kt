package com.mycompany.chservicetime.utilities

import com.mycompany.chservicetime.data.source.local.TimeslotEntity

fun TimeslotEntity.getDays(): String {
    var daysTemp = ""
    if (isDay0Selected) daysTemp += "Sun "
    if (isDay1Selected) daysTemp += "Mon "
    if (isDay2Selected) daysTemp += "Tur "
    if (isDay3Selected) daysTemp += "Wed "
    if (isDay4Selected) daysTemp += "Thr "
    if (isDay5Selected) daysTemp += "Fri "
    if (isDay6Selected) daysTemp += "Sat "

    return daysTemp
}

fun TimeslotEntity.getTimeRange(): String {
    return "${beginTimeHour.toString().padStart(2, '0')}" + ":" +
        "${beginTimeMinute.toString().padStart(2, '0')}" + " - " +
        "${endTimeHour.toString().padStart(2, '0')}" + ":" +
        "${endTimeMinute.toString().padStart(2, '0')}"
}