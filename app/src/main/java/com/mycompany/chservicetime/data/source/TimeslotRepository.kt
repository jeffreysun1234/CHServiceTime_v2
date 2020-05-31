package com.mycompany.chservicetime.data.source

import com.mycompany.chservicetime.data.source.local.TimeslotDao
import com.mycompany.chservicetime.data.source.local.TimeslotEntity

class TimeslotRepository(private val timeslotDao: TimeslotDao) {

    fun getTimeslotList() = timeslotDao.getTimeslotList()

    fun getTimeslotById(timeslotId: String) = timeslotDao.getTimeslotById(timeslotId)

    suspend fun deleteTimeslotById(timeslotId: String) = timeslotDao.deleteTimeslotById(timeslotId)

    suspend fun saveTimeslot(timeslot: TimeslotEntity) = timeslotDao.insertTimeslot(timeslot)

    suspend fun activateTimeslot(timeslotId: String, activatedFlag: Boolean) =
        timeslotDao.updateActivated(timeslotId, activatedFlag)
}