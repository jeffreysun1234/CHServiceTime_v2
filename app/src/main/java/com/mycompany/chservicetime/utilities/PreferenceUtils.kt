package com.mycompany.chservicetime.utilities

import android.content.Context
import android.preference.PreferenceManager

fun putPreferenceIntValue(context: Context, key: Int, value: String) {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    with(sharedPref.edit()) {
        putString(context.resources.getString(key), value)
        apply()
    }
}

fun getPreferenceIntValue(context: Context, key: Int): String? {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPref.getString(context.resources.getString(key), "")
}