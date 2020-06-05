package com.mycompany.chservicetime.services

import END_TIME_DAY_INT
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mycompany.chservicetime.R
import com.mycompany.chservicetime.receivers.AlarmReceiver
import com.mycompany.chservicetime.utilities.getCalendarByHHmm

object AlarmController {

    private const val REQUEST_CODE = 0

    fun setNextAlarm(context: Context, nextTimePoint: Pair<Boolean, Int>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.action = context.resources.getString(R.string.receiver_action_mute_operation)
            intent.component = ComponentName(
                context.applicationContext.packageName,
                "com.mycompany.chservicetime.receivers.AlarmReceiver"
            )
            PendingIntent.getBroadcast(
                context, REQUEST_CODE, intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

        var nextAlarmTimestamp = getCalendarByHHmm(nextTimePoint.second).timeInMillis
        // make sure the alarm time at next day
        if (nextTimePoint.second == END_TIME_DAY_INT) nextAlarmTimestamp += 90 * 1000L

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTimestamp,
                alarmIntent
            )
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTimestamp,
                alarmIntent
            )
        }
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
}