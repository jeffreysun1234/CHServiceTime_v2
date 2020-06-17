package com.mycompany.servicetime.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService

private const val ACTION_SET_SOUND_MODEL = "com.mycompany.servicetime.services.action.ALARM"

class MuteService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        when (intent?.action) {
            ACTION_SET_SOUND_MODEL -> {
                MuteOperator(this).execute()
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

        fun startActionSetSoundMode(context: Context) {
            val intent = Intent(context, MuteService::class.java).apply {
                action = ACTION_SET_SOUND_MODEL
            }
            enqueueWork(context, intent)
        }
    }
}
