package com.mycompany.chservicetime.timeslotlist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimeslotListFragmentTest {

    @Test
    fun testEventFragment() {
        val scenario = launchFragmentInContainer<TimeslotListFragment>()
        scenario.recreate()
    }
}