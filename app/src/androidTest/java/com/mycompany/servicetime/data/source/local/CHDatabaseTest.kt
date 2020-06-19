package com.mycompany.servicetime.data.source.local

import InitData
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mycompany.servicetime.LiveDataTestUtil
import com.mycompany.servicetime.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CHDatabaseTest {

    private lateinit var timeslotDao: TimeslotDao
    private lateinit var db: CHDatabase

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // using an in-memory database because the information stored here disappears when the
        // process is killed
        db = Room.inMemoryDatabaseBuilder(context, CHDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        timeslotDao = db.timeslotDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_timeslotDao() = runBlockingTest {
        timeslotDao.insertTimeslot(InitData.timeslotEntity_1)
        val initialTimeslots = timeslotDao.getTimeslotListLiveData()
        assertThat(LiveDataTestUtil.getValue(initialTimeslots).size, equalTo(1))
    }
}