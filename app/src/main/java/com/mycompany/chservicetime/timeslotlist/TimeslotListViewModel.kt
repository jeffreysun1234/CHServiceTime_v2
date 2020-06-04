package com.mycompany.chservicetime.timeslotlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mycompany.chservicetime.data.source.TimeslotRepository
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import com.mycompany.chservicetime.services.AlarmController
import com.mycompany.chservicetime.services.DNDController
import com.mycompany.chservicetime.usecases.TimeslotRules
import com.mycompany.chservicetime.utilities.CHEvent
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

    private val _currentViewEvent = MutableLiveData<CHEvent<TimeslotListResult>>()
    val currentViewEvent: LiveData<CHEvent<TimeslotListResult>> = _currentViewEvent

    fun doActivateTimeslot(timeslot: TimeslotEntity) =
        viewModelScope.launch {
            timeslotRepository.saveTimeslot(timeslot.copy(isActivated = !timeslot.isActivated))
        }

    fun triggerAlarmService(timeslots: List<TimeslotEntity>) {
        if (DNDController.checkDndPermission(false)) {
            val requiredTimeslots = TimeslotRules.getRequiredTimeslots(timeslots, getTodayOfWeek())
            val nextAlarmTimePoint =
                TimeslotRules.getNextAlarmTimePoint(requiredTimeslots, getCurrentHHmm())

            _nextAlarmTime.value = getFormatHourMinuteString(nextAlarmTimePoint.second)

            Timber.d("Get next alarm : $nextAlarmTimePoint")

            AlarmController.setNextAlarm(app.applicationContext, nextAlarmTimePoint)
        } else {
            _nextAlarmTime.value = ""
            _currentViewEvent.value = CHEvent(TimeslotListResult.NeedDNDPermission)
        }
    }
}