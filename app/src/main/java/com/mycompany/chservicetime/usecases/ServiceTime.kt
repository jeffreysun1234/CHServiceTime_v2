package com.mycompany.chservicetime.usecases

data class ServiceTime(
    var currentTime: Int = 0,
    var currentOperation: Int = INVALID,
    // Timestamp
    var nextAlarmTime: Long? = null,
    // HHmm in 24 hour format
    var nextAlarmTimeInt: Int = 0
) {
    companion object {
        val INVALID = -2
        val Normal = 0
        val Vibrate = 1
        val Mute = 2
        val NO_OPERATION = -1
    }
}
