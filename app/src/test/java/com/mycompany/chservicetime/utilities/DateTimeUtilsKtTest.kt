package com.mycompany.chservicetime.utilities

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DateTimeUtilsKtTest {

    @Test
    fun getFormatHourMinuteString() {
        assertThat(getFormatHourMinuteString(923)).isEqualTo("09:23")
    }
}