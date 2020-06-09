package com.mycompany.chservicetime.data.source

import com.mycompany.chservicetime.data.source.local.TimeslotEntity

object TestData {

    val timeslotEntity_1 = TimeslotEntity(
        "Timeslot Entity 1",
        "Work Time",
        9,
        0,
        17,
        0,
        false,
        true,
        true,
        true,
        true,
        true,
        false
    )

    val timeslotEntity_2 = TimeslotEntity(
        "Timeslot Entity 2",
        "School First Phrase",
        8,
        30,
        10,
        30,
        false,
        true,
        true,
        false,
        true,
        false,
        false
    )

    val timeslotEntity_3 = TimeslotEntity(
        "Timeslot Entity 3",
        "School Second Phrase",
        13,
        0,
        17,
        30,
        false,
        true,
        true,
        false,
        true,
        false,
        false
    )
}