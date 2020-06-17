package com.mycompany.servicetime.usecases

import BEGIN_HOUR_DAY
import BEGIN_MINUTE_DAY
import END_HOUR_DAY
import END_MINUTE_DAY
import END_TIME_DAY_INT
import com.mycompany.servicetime.data.source.local.TimeslotEntity
import com.mycompany.servicetime.utilities.beginTimeInt
import com.mycompany.servicetime.utilities.endTimeInt
import java.util.Calendar

class TimeslotRules {

    companion object {

        /**
         * Get all TimeSlots of a specific day.
         *
         * @param dayInWeek     Calendar.Sunday, Calendar.Monday, ... , Calendar.Saturday
         *                      Based on Calendar.java
         *
         * @return a Pair list of begin time (first) and end time (second).
         *
         * Time is in 24 hour format.
         */
        fun getRequiredTimeslots(
            timeslots: List<TimeslotEntity>,
            dayOfWeek: Int
        ): List<Pair<Int, Int>> = timeslots.filter {
            // select all activated timeslots
            it.isActivated
        }.flatMap {
            // Get overnight TimeSlots
            if (it.beginTimeInt() <= it.endTimeInt()) {
                listOf(it.copy())
            } else {
                listOf(
                    it.copy(endTimeHour = END_HOUR_DAY, endTimeMinute = END_MINUTE_DAY),
                    it.copy(
                        beginTimeHour = BEGIN_HOUR_DAY,
                        beginTimeMinute = BEGIN_MINUTE_DAY,
                        isMonSelected = it.isSunSelected,
                        isTueSelected = it.isMonSelected,
                        isWedSelected = it.isTueSelected,
                        isThuSelected = it.isWedSelected,
                        isFriSelected = it.isThuSelected,
                        isSatSelected = it.isFriSelected,
                        isSunSelected = it.isSatSelected
                    )
                )
            }
        }.filter {
            // select all timeslots in the special day
            when (dayOfWeek) {
                Calendar.SUNDAY -> it.isSunSelected
                Calendar.MONDAY -> it.isMonSelected
                Calendar.TUESDAY -> it.isTueSelected
                Calendar.WEDNESDAY -> it.isWedSelected
                Calendar.THURSDAY -> it.isThuSelected
                Calendar.FRIDAY -> it.isFriSelected
                Calendar.SATURDAY -> it.isSatSelected
                else -> false
            }
        }.sortedWith(
            // Sort by begin time and end time
            compareBy({ it.beginTimeInt() }, { it.endTimeInt() })
        ).map {
            Pair(it.beginTimeInt(), it.endTimeInt())
        }.fold(mutableListOf()) {
            // union two crossed timeslots
                acc, timeslot ->
            if (acc.isEmpty())
                acc.add(timeslot)
            else {
                val lastOne = acc.last()
                if (timeslot.first <= lastOne.second) {
                    if (timeslot.second > lastOne.second) {
                        // acc.removeLast()
                        acc.removeAt(acc.lastIndex)
                        acc.add(Pair(lastOne.first, timeslot.second))
                    }
                } else {
                    acc.add(timeslot)
                }
            }
            acc
        }

        /**
         * Get the most recent time point in the TimeSlot list based on the current time.
         *
         * @param currentTimeslotList a sorted list by begintime and endtime asc
         * @param currentTimeString The format is HH:mm
         * @return The first describes if the currentTime is in a timeslot.
         *          The second describes the next alarm time.
         */
        fun getNextAlarmTimePoint(
            currentTimeslotList: List<Pair<Int, Int>>,
            currentTimeString: Int
        ): Pair<Boolean, Int> {
            if (currentTimeslotList.isEmpty()) return Pair(false, END_TIME_DAY_INT)

            for (timeslot in currentTimeslotList) {
                if (currentTimeString < timeslot.first) return Pair(false, timeslot.first)
                if (timeslot.first <= currentTimeString && currentTimeString < timeslot.second) {
                    return Pair(true, timeslot.second)
                }
            }

            return Pair(false, END_TIME_DAY_INT)
        }
    }
}