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
    @ColumnInfo(name = "sun_selected")
    var isSunSelected: Boolean = false,
    @ColumnInfo(name = "mon_selected")
    var isMonSelected: Boolean = false,
    @ColumnInfo(name = "tue_selected")
    var isTueSelected: Boolean = false,
    @ColumnInfo(name = "wed_selected")
    var isWedSelected: Boolean = false,
    @ColumnInfo(name = "thu_selected")
    var isThuSelected: Boolean = false,
    @ColumnInfo(name = "fri_selected")
    var isFriSelected: Boolean = false,
    @ColumnInfo(name = "sat_selected")
    var isSatSelected: Boolean = false,
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