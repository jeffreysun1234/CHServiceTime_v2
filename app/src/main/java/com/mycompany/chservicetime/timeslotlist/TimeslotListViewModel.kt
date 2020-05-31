package com.mycompany.chservicetime.timeslotlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.chservicetime.data.source.TimeslotRepository
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import kotlinx.coroutines.launch

class TimeslotListViewModel internal constructor(
    private val timeslotRepository: TimeslotRepository
) : ViewModel() {

    val timeslotList: LiveData<List<TimeslotEntity>> = timeslotRepository.getTimeslotList()

    val nextAlarmTime: String = "TODO"

    fun doActivateTimeslot(timeslot: TimeslotEntity) =
        viewModelScope.launch {
            timeslotRepository.saveTimeslot(timeslot.copy(isActivated = !timeslot.isActivated))
            // showSnackbarMessage(if (activatedFlage) R.string.timeslot_marked_active else R.string.timeslot_marked_no_active)

            // Refresh list to show the new state
            // loadTimeslotList(false)
        }
}