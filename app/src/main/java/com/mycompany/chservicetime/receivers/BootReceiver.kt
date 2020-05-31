package com.mycompany.chservicetime.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mycompany.chservicetime.services.AlarmJobIntentService

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            AlarmJobIntentService.startActionFoo(context, "Hello", "")
        }
    }
}
