package com.mycompany.chservicetime

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mycompany.chservicetime.data.source.DataRepository
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import java.util.LinkedHashMap

class FakeDataRepository : DataRepository {

    var timeslotDbData: LinkedHashMap<String, TimeslotEntity> = LinkedHashMap()

    override fun getTimeslotListLiveData(): LiveData<List<TimeslotEntity>> =
        MutableLiveData(timeslotDbData.values.toList())

    override fun getTimeslotList(): List<TimeslotEntity> = timeslotDbData.values.toList()

    override fun getTimeslotById(timeslotId: String): LiveData<TimeslotEntity> =
        timeslotDbData[timeslotId]?.let { MutableLiveData(it) } ?: MutableLiveData()

    override suspend fun deleteTimeslotById(timeslotId: String) =
        timeslotDbData.remove(timeslotId)?.let { 1 } ?: 0

    override suspend fun deleteAllTimeslot() {
        timeslotDbData.clear()
    }

    override suspend fun saveTimeslot(timeslot: TimeslotEntity) {
        if (timeslotDbData.containsKey(timeslot.id))
            timeslotDbData[timeslot.id] = timeslot
        else
            timeslotDbData.plus(timeslot.id to timeslot)
    }

    override suspend fun activateTimeslot(timeslotId: String, activatedFlag: Boolean) {
        TODO("Not yet implemented")
    }

    @VisibleForTesting
    fun addTimeslots(vararg timeslots: TimeslotEntity) {
        for (timeslot in timeslots) {
            timeslotDbData[timeslot.id] = timeslot
        }
    }
}