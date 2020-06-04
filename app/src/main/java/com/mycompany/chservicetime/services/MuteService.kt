package com.mycompany.chservicetime.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.mycompany.chservicetime.R
import com.mycompany.chservicetime.data.source.TimeslotRepository
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import com.mycompany.chservicetime.usecases.TimeslotRules
import com.mycompany.chservicetime.utilities.getCurrentHHmm
import com.mycompany.chservicetime.utilities.getFormatHourMinuteString
import com.mycompany.chservicetime.utilities.getTodayOfWeek
import com.mycompany.chservicetime.utilities.putPreferenceIntValue
import com.mycompany.chservicetime.utilities.timestampFormat
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import timber.log.Timber
import java.util.Calendar

private const val ACTION_SET_SOUND_MODEL = "com.mycompany.chservicetime.services.action.ALARM"

private const val EXTRA_OPERATION_FLAG = "com.mycompany.chservicetime.services.extra.PARAM1"

class MuteService : JobIntentService(), KoinComponent {

    private val timeslotRepository: TimeslotRepository by inject()

    override fun onHandleWork(intent: Intent) {
        when (intent?.action) {
            ACTION_SET_SOUND_MODEL -> {
                val currentData = timeslotRepository.getTimeslotList()
                getCurrentAlarmTimePoint(currentData).let {
                    setMuteMode(it)
                    triggerAlarmService(it)
                }
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
            DNDController.turnOnDND()
        } else {
            Timber.d("Set Normal mode at ${timestampFormat(Calendar.getInstance().timeInMillis)}")
            DNDController.turnOffDND()
        }

        putPreferenceIntValue(
            this.applicationContext,
            R.string.pref_key_next_alarm_timepoint,
            getFormatHourMinuteString(nextAlarmTimePoint.second)
        )
    }

    private fun triggerAlarmService(nextAlarmTimePoint: Pair<Boolean, Int>) {
        AlarmController.setNextAlarm(this.applicationContext, nextAlarmTimePoint)
    }

    companion object {

        /**
         * Unique job ID for this service.
         */
        private const val JOB_ID = 1010

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MuteService::class.java, JOB_ID, work)
        }

        fun startActionSetSoundMode(context: Context, operationFlag: Boolean) {
            val intent = Intent(context, MuteService::class.java).apply {
                action = ACTION_SET_SOUND_MODEL
                putExtra(EXTRA_OPERATION_FLAG, operationFlag)
            }
            enqueueWork(context, intent)
        }

        fun startActionSetSoundMode(context: Context) {
            val intent = Intent(context, MuteService::class.java).apply {
                action = ACTION_SET_SOUND_MODEL
            }
            enqueueWork(context, intent)
        }
    }
}
