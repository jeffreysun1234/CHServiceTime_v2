package com.mycompany.chservicetime.utilities

import junit.framework.Assert.assertEquals
import org.junit.Test

class DateTimeUtilsKtTest {

    @Test
    fun getFormatHourMinuteString() {
        assertEquals("09:23", getFormatHourMinuteString(923))
    }
}