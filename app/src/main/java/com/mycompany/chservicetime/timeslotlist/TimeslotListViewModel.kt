package com.mycompany.chservicetime.timeslotlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mycompany.chservicetime.data.source.TimeslotRepository
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import com.mycompany.chservicetime.services.AlarmService
import com.mycompany.chservicetime.usecases.TimeslotRules
import com.mycompany.chservicetime.utilities.getCurrentHHmm
import com.mycompany.chservicetime.utilities.getFormatHourMinuteString
import com.mycompany.chservicetime.utilities.getTodayOfWeek
import kotlinx.coroutines.launch
import timber.log.Timber

class TimeslotListViewModel internal constructor(
    private val app: Application,
    private val timeslotRepository: TimeslotRepository
) : AndroidViewModel(app) {

    val timeslotList: LiveData<List<TimeslotEntity>> = timeslotRepository.getTimeslotList()

    private val _nextAlarmTime = MutableLiveData<String>()
    val nextAlarmTime: LiveData<String> = _nextAlarmTime

    fun doActivateTimeslot(timeslot: TimeslotEntity) =
        viewModelScope.launch {
            timeslotRepository.saveTimeslot(timeslot.copy(isActivated = !timeslot.isActivated))
            // showSnackbarMessage(if (activatedFlage) R.string.timeslot_marked_active else R.string.timeslot_marked_no_active)

            // Refresh list to show the new state
            // loadTimeslotList(false)

        }

    fun triggerAlarmService(timeslots: List<TimeslotEntity>) {
        val requiredTimeslots = TimeslotRules.getRequiredTimeslots(timeslots, getTodayOfWeek())
        val nextAlarmTimePoint =
            TimeslotRules.getNextAlarmTimePoint(requiredTimeslots, getCurrentHHmm())

        _nextAlarmTime.value = getFormatHourMinuteString(nextAlarmTimePoint.second)

        Timber.d("***** ${nextAlarmTime.value}")
        Timber.d("Set next alarm: $nextAlarmTimePoint")

        AlarmService.setNextAlarm(app.applicationContext)
    }
}