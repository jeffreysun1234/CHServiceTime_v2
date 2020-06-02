package com.mycompany.chservicetime.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.JobIntentService
import com.mycompany.chservicetime.receivers.AlarmReceiver
import timber.log.Timber

private const val ACTION_SET_SOUND_MODEL = "com.mycompany.chservicetime.services.action.ALARM"

private const val EXTRA_PARAM1 = "com.mycompany.chservicetime.services.extra.PARAM1"

private const val REQUEST_CODE = 0

class AlarmService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        when (intent?.action) {
            ACTION_SET_SOUND_MODEL -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                Timber.v("******* $param1 ********")
            }
        }
    }

    companion object {
        fun setNextAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, REQUEST_CODE, intent, FLAG_CANCEL_CURRENT)
            }

            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10 * 1000,
                alarmIntent
            )
        }

        fun cancelAlarm(context: Context) {
            val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0)
            }
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            if (alarmIntent != null && alarmManager != null) {
                alarmManager.cancel(alarmIntent)
            }
        }

        /**
         * Unique job ID for this service.
         */
        private const val JOB_ID = 1010

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, AlarmService::class.java, JOB_ID, work)
        }

        fun startActionSetSoundModel(context: Context, param1: String) {
            val intent = Intent(context, AlarmService::class.java).apply {
                action = ACTION_SET_SOUND_MODEL
                putExtra(EXTRA_PARAM1, param1)
            }
            enqueueWork(context, intent)
        }
    }
}
