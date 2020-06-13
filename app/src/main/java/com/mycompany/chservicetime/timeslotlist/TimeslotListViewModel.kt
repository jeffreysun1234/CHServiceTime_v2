package com.mycompany.chservicetime.timeslotlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mycompany.chservicetime.R
import com.mycompany.chservicetime.data.source.DataRepository
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import com.mycompany.chservicetime.services.MuteOperator
import com.mycompany.chservicetime.utilities.CHEvent
import com.mycompany.chservicetime.utilities.DNDControllerHelper
import com.mycompany.chservicetime.utilities.putPreferenceIntValue
import kotlinx.coroutines.launch
import timber.log.Timber

class TimeslotListViewModel internal constructor(
    private val app: Application,
    private val dataRepository: DataRepository
) : AndroidViewModel(app) {

    val timeslotList: LiveData<List<TimeslotEntity>> = dataRepository.getTimeslotListLiveData()

    val _nextAlarmTime = MutableLiveData<String>()
    val nextAlarmTime: LiveData<String> = _nextAlarmTime

    private val _currentViewEvent = MutableLiveData<CHEvent<TimeslotListResult>>()
    val currentViewEvent: LiveData<CHEvent<TimeslotListResult>> = _currentViewEvent

    fun doActivateTimeslot(timeslot: TimeslotEntity) =
        viewModelScope.launch {
            dataRepository.saveTimeslot(timeslot.copy(isActivated = !timeslot.isActivated))
        }

    fun triggerMuteService() {
        if (DNDControllerHelper.checkDndPermissionCompat(app, false)) {
            Timber.d("UI triggers an operation")
            MuteOperator(app.applicationContext).execute()
            // MuteService.startActionSetSoundMode(app.applicationContext)
        } else {
            putPreferenceIntValue(
                app.applicationContext,
                R.string.pref_key_next_alarm_timepoint,
                ""
            )

            _currentViewEvent.value = CHEvent(TimeslotListResult.NeedDNDPermission)
        }
    }
}