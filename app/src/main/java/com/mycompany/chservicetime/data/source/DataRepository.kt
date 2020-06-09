package com.mycompany.chservicetime.data.source

import androidx.lifecycle.LiveData
import com.mycompany.chservicetime.data.source.local.TimeslotEntity

interface DataRepository {

    fun getTimeslotListLiveData(): LiveData<List<TimeslotEntity>>

    fun getTimeslotList(): List<TimeslotEntity>

    fun getTimeslotById(timeslotId: String): LiveData<TimeslotEntity>

    suspend fun deleteTimeslotById(timeslotId: String): Int

    suspend fun saveTimeslot(timeslot: TimeslotEntity)

    suspend fun activateTimeslot(timeslotId: String, activatedFlag: Boolean)
}