package com.mycompany.chservicetime.timeslotlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mycompany.chservicetime.data.source.TimeslotRepository
import com.mycompany.chservicetime.data.source.local.TimeslotEntity

class TimeslotListViewModel internal constructor(
    private val timeslotRepository: TimeslotRepository
) : ViewModel() {

    val timeslotList: LiveData<List<TimeslotEntity>> = timeslotRepository.getTimeslotList()

    val nextAlarmTime: String = "TODO"
}