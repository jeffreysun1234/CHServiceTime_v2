package com.mycompany.chservicetime.usecases

import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.Arrays
import java.util.Calendar

class TimeslotRulesTest {

    companion object {
        val work_activated = TimeslotEntity(
            "1", "Work",
            9, 30, 17, 0,
            false, true, true, true, true, true, false,
            true, true
        )

        val work_1_activated = TimeslotEntity(
            "2", "Work",
            9, 0, 12, 0,
            false, true, true, true, true, true, false,
            true, true
        )

        val work_2_activated = TimeslotEntity(
            "3", "Work",
            13, 0, 17, 0,
            false, true, true, true, true, true, false,
            true, true
        )

        val sleep_activated = TimeslotEntity(
            "4", "Sleep",
            22, 30, 7, 30,
            true, true, true, true, true, false, false,
            true, true
        )

        val data_1_activated = TimeslotEntity(
            "5", "Data 1",
            10, 30, 14, 30,
            false, true, true, true, true, true, false,
            true, true
        )
        val data_2_activated = TimeslotEntity(
            "6", "Data 2",
            13, 0, 18, 0,
            false, true, true, true, true, true, false,
            true, true
        )
        val data_3_activated = TimeslotEntity(
            "7", "Data 3",
            18, 30, 20, 30,
            false, true, true, true, true, true, false,
            true, true
        )
        val data_4_activated = TimeslotEntity(
            "8", "Data 4",
            17, 0, 20, 0,
            false, true, true, true, true, true, false,
            true, true
        )
        val data_5_overnight_activated = TimeslotEntity(
            "9", "Data 5",
            17, 0, 2, 0,
            false, true, true, true, true, true, false,
            true, true
        )
        val data_6_overnight_activated = TimeslotEntity(
            "10", "Data 6",
            20, 0, 5, 0,
            false, true, true, true, true, true, false,
            true, true
        )
        val data_7_overnight_activated = TimeslotEntity(
            "11", "Data 7",
            17, 30, 5, 0,
            false, true, true, true, true, true, false,
            true, true
        )
    }

    @ExperimentalStdlibApi
    @Test
    @Throws(Exception::class)
    fun testGetRequiredTimeSlots() {
        var inputData = mutableListOf<TimeslotEntity>()
        var resultData = listOf<Pair<Int, Int>>()

        // Empty input data
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        /****** One Input Data ******/

        // Only one input data with selected and activated
        inputData = mutableListOf(work_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals("[(930, 1700)]", Arrays.deepToString(resultData.toTypedArray()))

        // Only one input data with selected and unactivated
        inputData = mutableListOf(work_activated.copy(isActivated = false))
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        // Only one input data with unselected and activated
        inputData = mutableListOf(work_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.SATURDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        // Only one input data with unselected and unactivated
        inputData = mutableListOf(work_activated.copy(isActivated = false))
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.SATURDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        /****** Overnight Data ******/

        // Only one overnight input data with selected and activated
        inputData = mutableListOf(sleep_activated)
        // the period day is selected
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals(
            "[(0, 730), (2230, 2359)]",
            Arrays.deepToString(resultData.toTypedArray())
        )
        // the period day is unselected
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.SUNDAY)
        assertEquals("[(2230, 2359)]", Arrays.deepToString(resultData.toTypedArray()))

        // Only one overnight input data with selected and unactivated
        inputData = mutableListOf(sleep_activated.copy(isActivated = false))
        // the period day is selected
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))
        // the period day is unselected
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.MONDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        // Only one overnight input data with unselected and activated
        inputData = mutableListOf(sleep_activated)
        // the period day is selected
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.FRIDAY)
        assertEquals("[(0, 730)]", Arrays.deepToString(resultData.toTypedArray()))
        // the period day is unselected
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.SATURDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        // Only one overnight input data with unselected and unactivated
        inputData = mutableListOf(sleep_activated.copy(isActivated = false))
        // the period day is selected
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.FRIDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))
        // the period day is unselected
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.SATURDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        /****** Two Input Data ******/

        // Two input data with selected and activated
        inputData = mutableListOf(work_1_activated, work_2_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals(
            "[(900, 1200), (1300, 1700)]",
            Arrays.deepToString(resultData.toTypedArray())
        )

        // Two input data with selected and unactivated
        inputData = mutableListOf(
            work_1_activated.copy(isActivated = false),
            work_2_activated.copy(isActivated = false)
        )
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        // Two input data with only one selected and activated
        inputData = mutableListOf(work_1_activated, work_2_activated.copy(isTueSelected = false))
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals("[(900, 1200)]", Arrays.deepToString(resultData.toTypedArray()))

        // Two input data with unselected and activated
        inputData = mutableListOf(work_1_activated, work_2_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.SATURDAY)
        assertEquals("[]", Arrays.deepToString(resultData.toTypedArray()))

        // Two overnight input data with selected and activated
        inputData = mutableListOf(data_5_overnight_activated, data_6_overnight_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.SATURDAY)
        assertEquals("[(0, 500)]", Arrays.deepToString(resultData.toTypedArray()))

        /******* Cross Input Data ******/
        // Two cross input data with selected and activated
        inputData = mutableListOf(data_1_activated, data_2_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals("[(1030, 1800)]", Arrays.deepToString(resultData.toTypedArray()))

        // Cross input data with selected and activated, two segment
        inputData = mutableListOf(data_1_activated, data_2_activated, data_3_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals(
            "[(1030, 1800), (1830, 2030)]",
            Arrays.deepToString(resultData.toTypedArray())
        )

        // Cross input data with selected and activated, 1 segment
        inputData = mutableListOf(data_1_activated, data_2_activated, data_4_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals(
            "[(1030, 2000)]",
            Arrays.deepToString(resultData.toTypedArray())
        )

        // one input data with selected and activated, plus a overnight input data
        inputData = mutableListOf(data_1_activated, data_7_overnight_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals(
            "[(0, 500), (1030, 1430), (1730, 2359)]",
            Arrays.deepToString(resultData.toTypedArray())
        )

        // Cross input data with selected and activated, plus a overnight input data
        inputData = mutableListOf(data_1_activated, data_2_activated, data_7_overnight_activated)
        resultData = TimeslotRules.getRequiredTimeslots(inputData, Calendar.TUESDAY)
        assertEquals(
            "[(0, 500), (1030, 2359)]",
            Arrays.deepToString(resultData.toTypedArray())
        )
    }

    @Test
    @Throws(Exception::class)
    fun testGetNextAlarmTimePoint() {
        val timeslots1 = listOf<Pair<Int, Int>>()
        val timeslots2 =
            listOf(Pair(0, 730), Pair(1000, 1320), Pair(2100, 2200))
        val timeslots3 = listOf(Pair(1000, 1320), Pair(2100, 2200))

        var result = TimeslotRules.getNextAlarmTimePoint(timeslots2, 830)
        assertEquals("(false, 1000)", result.toString())

        result = TimeslotRules.getNextAlarmTimePoint(timeslots2, 630)
        assertEquals("(true, 730)", result.toString())

        result = TimeslotRules.getNextAlarmTimePoint(timeslots2, 2230)
        assertEquals("(false, 2359)", result.toString())

        // empty list
        result = TimeslotRules.getNextAlarmTimePoint(timeslots1, 2230)
        assertEquals("(false, 2359)", result.toString())

        // test the first timepoint
        result = TimeslotRules.getNextAlarmTimePoint(timeslots3, 930)
        assertEquals("(false, 1000)", result.toString())
    }
}