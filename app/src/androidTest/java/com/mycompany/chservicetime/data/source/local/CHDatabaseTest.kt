package com.mycompany.chservicetime.data.source.local

import InitData
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CHDatabaseTest {

    private lateinit var timeslotDao: TimeslotDao
    private lateinit var db: CHDatabase

    // @get:Rule
    // var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, CHDatabase::class.java
        ).build()
        timeslotDao = db.timeslotDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_timeslotDao() = runBlocking {
        timeslotDao.insertTimeslot(InitData.timeslotEntity_1)
        val initialTimeslots = timeslotDao.getTimeslotList()
        // assertThat(initialTimeslots.size, equalTo(1))
    }
}