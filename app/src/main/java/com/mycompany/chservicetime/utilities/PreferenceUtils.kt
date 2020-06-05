package com.mycompany.chservicetime.utilities

import PREFERENCE_FILE_NAME
import android.content.Context

fun putPreferenceIntValue(context: Context, key: Int, value: String) {
    val sharedPref = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(context.resources.getString(key), value)
        apply()
    }
}

fun getPreferenceIntValue(context: Context, key: Int): String? {
    val sharedPref = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
    return sharedPref.getString(context.resources.getString(key), "")
}