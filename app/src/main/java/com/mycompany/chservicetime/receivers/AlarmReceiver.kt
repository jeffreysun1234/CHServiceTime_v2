package com.mycompany.chservicetime.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mycompany.chservicetime.services.MuteService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        MuteService.startActionSetSoundModel(
            context,
            intent.getBooleanExtra(EXTRA_OPERATION_FLAG, false)
        )
    }

    companion object {
        const val EXTRA_OPERATION_FLAG = "com.mycompany.chservicetime.alarmReceiver.operationFlag"
    }
}
