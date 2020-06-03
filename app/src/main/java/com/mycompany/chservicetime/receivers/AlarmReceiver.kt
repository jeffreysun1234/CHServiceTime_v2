package com.mycompany.chservicetime.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mycompany.chservicetime.services.AlarmService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AlarmService.startActionSetSoundModel(context, "Alarm Hello")
    }
}
