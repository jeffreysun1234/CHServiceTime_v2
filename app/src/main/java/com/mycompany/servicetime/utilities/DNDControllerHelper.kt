package com.mycompany.servicetime.utilities

import android.content.Context
import android.os.Build
import com.mycompany.servicetime.services.DNDController

class DNDControllerHelper {

    companion object {
        fun checkDndPermissionCompat(context: Context, showSettingDialog: Boolean) =
            if (Build.VERSION.SDK_INT >= 23) {
                DNDController(context).checkDndPermission(showSettingDialog)
            } else {
                true
            }
    }
}