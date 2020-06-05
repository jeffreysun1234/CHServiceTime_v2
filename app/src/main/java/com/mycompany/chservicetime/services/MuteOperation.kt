package com.mycompany.chservicetime.services

import android.content.Context
import com.mycompany.chservicetime.R
import com.mycompany.chservicetime.data.source.TimeslotRepository
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import com.mycompany.chservicetime.usecases.TimeslotRules
import com.mycompany.chservicetime.utilities.getCurrentHHmm
import com.mycompany.chservicetime.utilities.getFormatHourMinuteString
import com.mycompany.chservicetime.utilities.getTodayOfWeek
import com.mycompany.chservicetime.utilities.putPreferenceIntValue
import com.mycompany.chservicetime.utilities.timestampFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.Calendar

class MuteOperation(val context: Context) : KoinComponent {

    private val timeslotRepository: TimeslotRepository by inject()

    fun execute() {
        runBlocking<Unit> {
            async(Dispatchers.IO) {
                val currentData = timeslotRepository.getTimeslotList()
                getCurrentAlarmTimePoint(currentData).let {
                    setMuteMode(it)
                    triggerAlarmService(it)
                }

                // register the receiver
                // val filter = IntentFilter()
                // filter.addAction("com.journaldev.AN_INTENT")
                // filter.addCategory("android.intent.category.DEFAULT")
                // context.applicationContext.registerReceiver(AlarmReceiver(), filter)
            }
        }
    }

    private fun getCurrentAlarmTimePoint(timeslots: List<TimeslotEntity>): Pair<Boolean, Int> {
        val requiredTimeslots = TimeslotRules.getRequiredTimeslots(timeslots, getTodayOfWeek())
        return TimeslotRules.getNextAlarmTimePoint(requiredTimeslots, getCurrentHHmm())
    }

    private fun setMuteMode(nextAlarmTimePoint: Pair<Boolean, Int>) {
        Timber.d("Get next alarm : $nextAlarmTimePoint")

        if (nextAlarmTimePoint.first) {
            Timber.d("Set Mute mode at ${timestampFormat(Calendar.getInstance().timeInMillis)}")
            DNDController(context.applicationContext).turnOnDND()
        } else {
            Timber.d("Set Normal mode at ${timestampFormat(Calendar.getInstance().timeInMillis)}")
            DNDController(context.applicationContext).turnOffDND()
        }

        putPreferenceIntValue(
            context.applicationContext,
            R.string.pref_key_next_alarm_timepoint,
            getFormatHourMinuteString(nextAlarmTimePoint.second)
        )
    }

    private fun triggerAlarmService(nextAlarmTimePoint: Pair<Boolean, Int>) {
        AlarmController.setNextAlarm(context.applicationContext, nextAlarmTimePoint)
    }
}