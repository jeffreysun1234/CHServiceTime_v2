package com.mycompany.chservicetime.services

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.JobIntentService

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.mycompany.chservicetime.services.action.FOO"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.mycompany.chservicetime.services.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.mycompany.chservicetime.services.extra.PARAM2"

class BootService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        Log.i("AlarmJobIntentService", "Executing work: $intent")
        when (intent?.action) {
            ACTION_FOO -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo(param1, param2)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String, param2: String) {
        for (i in 0..4) {
            Log.i(
                "AlarmJobIntentService", "Running service " + (i + 1)
                    + "/5 @ " + SystemClock.elapsedRealtime()
            )
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
            }
        }
        Log.i(
            "AlarmJobIntentService",
            "Completed service @ " + SystemClock.elapsedRealtime()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("AlarmJobIntentService", "All work complete")
    }

    companion object {
        /**
         * Unique job ID for this service.
         */
        private const val JOB_ID = 1000

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, BootService::class.java, JOB_ID, work)
        }

        /**
         * Starts this service to perform action Foo with the given parameters.
         */
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, BootService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            enqueueWork(context, intent)
        }
    }
}
