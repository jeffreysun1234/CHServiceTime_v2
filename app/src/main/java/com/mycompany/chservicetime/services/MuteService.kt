package com.mycompany.chservicetime.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.mycompany.chservicetime.utilities.timestampFormat
import timber.log.Timber
import java.util.Calendar

private const val ACTION_SET_SOUND_MODEL = "com.mycompany.chservicetime.services.action.ALARM"

private const val EXTRA_OPERATION_FLAG = "com.mycompany.chservicetime.services.extra.PARAM1"

class MuteService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        when (intent?.action) {
            ACTION_SET_SOUND_MODEL -> {
                if (intent.getBooleanExtra(EXTRA_OPERATION_FLAG, false)) {
                    Timber.d("Set Mute mode at ${timestampFormat(Calendar.getInstance().timeInMillis)}")
                    DNDController.turnOnDND()
                } else {
                    Timber.d("Set Normal mode at ${timestampFormat(Calendar.getInstance().timeInMillis)}")
                    DNDController.turnOffDND()
                }
            }
        }
    }

    companion object {

        /**
         * Unique job ID for this service.
         */
        private const val JOB_ID = 1010

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MuteService::class.java, JOB_ID, work)
        }

        fun startActionSetSoundModel(context: Context, operationFlag: Boolean) {
            val intent = Intent(context, MuteService::class.java).apply {
                action = ACTION_SET_SOUND_MODEL
                putExtra(EXTRA_OPERATION_FLAG, operationFlag)
            }
            enqueueWork(context, intent)
        }
    }
}
