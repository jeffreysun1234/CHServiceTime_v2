package com.mycompany.chservicetime.utilities

import com.mycompany.chservicetime.data.source.local.TimeslotEntity

fun TimeslotEntity.getDays(): String {
    var daysTemp = ""
    if (isSunSelected) daysTemp += "Sun "
    if (isMonSelected) daysTemp += "Mon "
    if (isTueSelected) daysTemp += "Tue "
    if (isWedSelected) daysTemp += "Wed "
    if (isThuSelected) daysTemp += "Thr "
    if (isFriSelected) daysTemp += "Fri "
    if (isSatSelected) daysTemp += "Sat "

    return daysTemp
}

fun TimeslotEntity.getTimeRange(): String {
    return getFormatHourMinuteString(beginTimeHour, beginTimeMinute) +
        " - " + getFormatHourMinuteString(endTimeHour, endTimeMinute)
}

// fun TimeslotEntity.beginTimeString() =
//     "${beginTimeHour.toString().padStart(2, '0')}:${beginTimeMinute.toString().padStart(2, '0')}"
//
// fun TimeslotEntity.endTimeString() =
//     "${endTimeHour.toString().padStart(2, '0')}:${endTimeMinute.toString().padStart(2, '0')}"

fun TimeslotEntity.beginTimeInt() = getFormatHourMinuteInt(beginTimeHour, beginTimeMinute)

fun TimeslotEntity.endTimeInt() = getFormatHourMinuteInt(endTimeHour, endTimeMinute)