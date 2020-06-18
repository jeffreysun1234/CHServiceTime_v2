package com.mycompany.servicetime.timeslotdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.servicetime.R
import com.mycompany.servicetime.data.source.DataRepository
import com.mycompany.servicetime.data.source.local.TimeslotEntity
import com.mycompany.servicetime.utilities.CHEvent
import com.mycompany.servicetime.utilities.getCurrentHourAndMinute
import kotlinx.coroutines.launch

/**
 * ViewModel for the Add/Edit screen.
 */
class TimeslotDetailViewModel internal constructor(
    private val timeslotId: String? = null,
    private val dataRepository: DataRepository
) : ViewModel() {

    // Two-way databinding, exposing MutableLiveData
    val title = MutableLiveData<String>()
    val day0Selected = MutableLiveData<Boolean>()
    val day1Selected = MutableLiveData<Boolean>()
    val day2Selected = MutableLiveData<Boolean>()
    val day3Selected = MutableLiveData<Boolean>()
    val day4Selected = MutableLiveData<Boolean>()
    val day5Selected = MutableLiveData<Boolean>()
    val day6Selected = MutableLiveData<Boolean>()
    val beginTimeHour = MutableLiveData<Int>()
    val beginTimeMunite = MutableLiveData<Int>()
    val endTimeHour = MutableLiveData<Int>()
    val endTimeMunite = MutableLiveData<Int>()
    val repeated = MutableLiveData<Boolean>()

    val titleError = MutableLiveData<String>()

    private var currentData: LiveData<TimeslotEntity>? = null

    private val _currentViewEvent = MutableLiveData<CHEvent<TimeslotDetailResult>>()
    val currentViewEvent: LiveData<CHEvent<TimeslotDetailResult>> = _currentViewEvent

    var isNewTimeslot: Boolean = false

    var customObserver: Observer<TimeslotEntity> = Observer { result ->
        result ?: return@Observer
        onTimeslotLoaded(result)
    }

    override fun onCleared() {
        currentData?.removeObserver(customObserver)
        super.onCleared()
    }

    /**
     * Initial data for adding a new timeslot or load data from data source for editing a timeslot
     */
    fun start() {
        if (timeslotId == null) {
            // No need to populate, it's a new timeslot
            isNewTimeslot = true

            val (currentHour, currentMinute) = getCurrentHourAndMinute()
            beginTimeHour.value = currentHour
            beginTimeMunite.value = currentMinute
            endTimeHour.value = currentHour
            endTimeMunite.value = currentMinute
            day0Selected.value = false
            day1Selected.value = false
            day2Selected.value = false
            day3Selected.value = false
            day4Selected.value = false
            day5Selected.value = false
            day6Selected.value = false
            return
        }

        isNewTimeslot = false

        currentData = dataRepository.getTimeslotById(timeslotId)
        currentData?.observeForever(customObserver)
    }

    private fun onTimeslotLoaded(timeslot: TimeslotEntity?) {
        timeslot?.let {
            title.value = timeslot.title
            beginTimeHour.value = timeslot.beginTimeHour
            beginTimeMunite.value = timeslot.beginTimeMinute
            endTimeHour.value = timeslot.endTimeHour
            endTimeMunite.value = timeslot.endTimeMinute
            day0Selected.value = timeslot.isSunSelected
            day1Selected.value = timeslot.isMonSelected
            day2Selected.value = timeslot.isTueSelected
            day3Selected.value = timeslot.isWedSelected
            day4Selected.value = timeslot.isThuSelected
            day5Selected.value = timeslot.isFriSelected
            day6Selected.value = timeslot.isSatSelected
            repeated.value = timeslot.isRepeated

            titleError.value = null
        }
    }

    // Called when clicking on the Save button
    fun saveTimeslot() {
        if (verifyInputData()) {
            if (isNewTimeslot) {
                createTimeslot(setTimeslotValue(TimeslotEntity()))
            } else {
                val timeslot = currentData?.value?.copy() ?: return
                updateTimeslot(setTimeslotValue(timeslot))
            }
        }
    }

    private fun verifyInputData(): Boolean {
        var result = true
        // Title can not be empty
        if (title.value.isNullOrEmpty()) {
            titleError.value = "Name can not be empty!"
            result = false
        } else {
            titleError.value = null
        }

        // At least one day need to be chosen.
        if (!(day0Selected.value!! || day1Selected.value!! || day2Selected.value!!
                || day3Selected.value!! || day4Selected.value!! || day5Selected.value!!
                || day6Selected.value!!)
        ) {
            result = false
            _currentViewEvent.value = CHEvent(TimeslotDetailResult.Title(R.string.input_error_days))
        }
        return result
    }

    private fun setTimeslotValue(timeslot: TimeslotEntity): TimeslotEntity {
        title.value?.let { timeslot.title = it }
        beginTimeHour.value?.let { timeslot.beginTimeHour = it }
        beginTimeMunite.value?.let { timeslot.beginTimeMinute = it }
        endTimeHour.value?.let { timeslot.endTimeHour = it }
        endTimeMunite.value?.let { timeslot.endTimeMinute = it }
        day0Selected.value?.let { timeslot.isSunSelected = it }
        day1Selected.value?.let { timeslot.isMonSelected = it }
        day2Selected.value?.let { timeslot.isTueSelected = it }
        day3Selected.value?.let { timeslot.isWedSelected = it }
        day4Selected.value?.let { timeslot.isThuSelected = it }
        day5Selected.value?.let { timeslot.isFriSelected = it }
        day6Selected.value?.let { timeslot.isSatSelected = it }
        repeated.value?.let { timeslot.isRepeated = it }
        return timeslot
    }

    fun deleteTimeslot() {
        viewModelScope.launch {
            timeslotId?.let { dataRepository.deleteTimeslotById(it) }
            _currentViewEvent.value = CHEvent(TimeslotDetailResult.DeletedSuccess)
        }
    }

    private fun createTimeslot(newTimeslot: TimeslotEntity) = viewModelScope.launch {
        dataRepository.saveTimeslot(newTimeslot)
        _currentViewEvent.value = CHEvent(TimeslotDetailResult.CreatedSuccess)
    }

    private fun updateTimeslot(timeslot: TimeslotEntity) {
        if (isNewTimeslot) {
            throw RuntimeException("updateTimeslot() was called but timeslot is new.")
        }
        viewModelScope.launch {
            dataRepository.saveTimeslot(timeslot)
            _currentViewEvent.value = CHEvent(TimeslotDetailResult.UpdatedSuccess)
        }
    }
}
