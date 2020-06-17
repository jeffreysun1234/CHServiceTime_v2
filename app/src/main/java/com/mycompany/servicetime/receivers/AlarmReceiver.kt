package com.mycompany.servicetime.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mycompany.servicetime.services.MuteOperator
import timber.log.Timber

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Received an alarm.")

        MuteOperator(context).execute()
    }
}
