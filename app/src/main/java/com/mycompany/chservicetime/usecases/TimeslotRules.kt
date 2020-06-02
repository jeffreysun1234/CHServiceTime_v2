package com.mycompany.chservicetime.usecases

import BEGIN_HOUR_DAY
import BEGIN_MINUTE_DAY
import END_HOUR_DAY
import END_MINUTE_DAY
import END_TIME_DAY_INT
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import com.mycompany.chservicetime.utilities.beginTimeInt
import com.mycompany.chservicetime.utilities.endTimeInt
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
        @ExperimentalStdlibApi
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
                        isDay1Selected = it.isDay0Selected,
                        isDay2Selected = it.isDay1Selected,
                        isDay3Selected = it.isDay2Selected,
                        isDay4Selected = it.isDay3Selected,
                        isDay5Selected = it.isDay4Selected,
                        isDay6Selected = it.isDay5Selected,
                        isDay0Selected = it.isDay6Selected
                    )
                )
            }
        }.filter {
            // select all timeslots in the special day
            when (dayOfWeek) {
                Calendar.SUNDAY -> it.isDay0Selected
                Calendar.MONDAY -> it.isDay1Selected
                Calendar.TUESDAY -> it.isDay2Selected
                Calendar.WEDNESDAY -> it.isDay3Selected
                Calendar.THURSDAY -> it.isDay4Selected
                Calendar.FRIDAY -> it.isDay5Selected
                Calendar.SATURDAY -> it.isDay6Selected
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
                        acc.removeLast()
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