package com.mycompany.servicetime.data.source

import com.mycompany.servicetime.data.source.local.TimeslotDao
import com.mycompany.servicetime.data.source.local.TimeslotEntity

class TimeslotRepository(private val timeslotDao: TimeslotDao) : DataRepository {

    override fun getTimeslotListLiveData() = timeslotDao.getTimeslotListLiveData()

    override fun getTimeslotList() = timeslotDao.getTimeslotList()

    override fun getTimeslotById(timeslotId: String) = timeslotDao.getTimeslotById(timeslotId)

    override suspend fun deleteTimeslotById(timeslotId: String) =
        timeslotDao.deleteTimeslotById(timeslotId)

    override suspend fun deleteAllTimeslot() {
        timeslotDao.deleteAllTimeslot()
    }

    override suspend fun saveTimeslot(timeslot: TimeslotEntity) =
        timeslotDao.insertTimeslot(timeslot)

    override suspend fun activateTimeslot(timeslotId: String, activatedFlag: Boolean) =
        timeslotDao.updateActivated(timeslotId, activatedFlag)
}