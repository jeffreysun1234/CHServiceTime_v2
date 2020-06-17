package com.mycompany.servicetime.data.source

import androidx.lifecycle.LiveData
import com.mycompany.servicetime.data.source.local.TimeslotEntity

interface DataRepository {

    fun getTimeslotListLiveData(): LiveData<List<TimeslotEntity>>

    fun getTimeslotList(): List<TimeslotEntity>

    fun getTimeslotById(timeslotId: String): LiveData<TimeslotEntity>

    suspend fun deleteTimeslotById(timeslotId: String): Int

    suspend fun deleteAllTimeslot()

    suspend fun saveTimeslot(timeslot: TimeslotEntity)

    suspend fun activateTimeslot(timeslotId: String, activatedFlag: Boolean)
}