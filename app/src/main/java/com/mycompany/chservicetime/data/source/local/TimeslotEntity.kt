package com.mycompany.chservicetime.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.UUID

@Entity(tableName = "timeslots")
data class TimeslotEntity(
    @PrimaryKey @ColumnInfo(name = "entry_id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "begin_time_hour")
    var beginTimeHour: Int = 0,
    @ColumnInfo(name = "begin_time_minute")
    var beginTimeMinute: Int = 0,
    @ColumnInfo(name = "end_time_hour")
    var endTimeHour: Int = 0,
    @ColumnInfo(name = "end_time_minute")
    var endTimeMinute: Int = 0,
    // Sunday is Day0, Monday is Day1, ... , Saturday is Day6
    @ColumnInfo(name = "day0_selected")
    var isDay0Selected: Boolean = false,
    @ColumnInfo(name = "day1_selected")
    var isDay1Selected: Boolean = false,
    @ColumnInfo(name = "day2_selected")
    var isDay2Selected: Boolean = false,
    @ColumnInfo(name = "day3_selected")
    var isDay3Selected: Boolean = false,
    @ColumnInfo(name = "day4_selected")
    var isDay4Selected: Boolean = false,
    @ColumnInfo(name = "day5_selected")
    var isDay5Selected: Boolean = false,
    @ColumnInfo(name = "day6_selected")
    var isDay6Selected: Boolean = false,
    @ColumnInfo(name = "repeated")
    var isRepeated: Boolean = false,
    @ColumnInfo(name = "activated")
    var isActivated: Boolean = false,
    @ColumnInfo(name = "update_timestamp")
    var updateTimestamp: Long = Calendar.getInstance().timeInMillis
) {

    // TODO : Delete the code if it is not used.
    val photoFileName
        get() = "IMG_$id.jpg"
}