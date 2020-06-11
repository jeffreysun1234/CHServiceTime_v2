package com.mycompany.chservicetime.services

import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.mycompany.chservicetime.R

class DNDController(val context: Context) {

    private fun getNotificationManager() =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    // NotificationManagerCompat.from(app.applicationContext)

    //Checks if app has access to Do not disturb
    //If no access then prompts user to give permission
    //Call every time before you access Do not disturb
    fun checkDndPermission(showSettingDialog: Boolean): Boolean {
        if (!getNotificationManager().isNotificationPolicyAccessGranted) {
            //Ask for permission
            if (showSettingDialog) dndPermissionDialog()
            return false
        } else {
            return true
        }
    }

    //Dialog box to prompt user for permission
    private fun dndPermissionDialog() = AlertDialog.Builder(context)
        .setMessage(R.string.dnd_dialog_message)
        .setTitle(R.string.dnd_dialog_title)
        .setPositiveButton(
            R.string.dnd_dialog_allow,
            DialogInterface.OnClickListener { dialog, id ->
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(context, intent, null)
            })
        .setNegativeButton(
            R.string.dnd_dialog_cancel,
            DialogInterface.OnClickListener { dialog, id ->
                //After user clicks 'Do Not Allow'
            })
        .create()
        .show()

    //Will turn ON Do Not Disturb
    fun turnOnDND(): Boolean {
        if (getNotificationManager().isNotificationPolicyAccessGranted()) {
            getNotificationManager().setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
            return true
        } else {
            return false
        }
    }

    //Will turn OFF Do Not Disturb
    fun turnOffDND(): Boolean {
        if (getNotificationManager().isNotificationPolicyAccessGranted()) {
            getNotificationManager().setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            return true
        } else {
            return false
        }
    }
}