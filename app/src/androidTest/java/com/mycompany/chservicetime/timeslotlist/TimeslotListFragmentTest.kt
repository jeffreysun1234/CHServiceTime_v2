package com.mycompany.chservicetime.timeslotlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mycompany.chservicetime.testAppModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class TimeslotListFragmentTest : KoinTest {

    // @get:Rule
    // val mockProvider = MockProviderRule.create { clazz ->
    //     mockkClass(clazz.java.kotlin)
    // }

    // // Set the main coroutines dispatcher for unit testing.
    // @ExperimentalCoroutinesApi
    // @get:Rule
    // var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        loadKoinModules(testAppModules)
        // repository = rule.component.tasksRepository
        // repository.deleteAllTasksBlocking()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testEventFragment() {
        val scenario = launchFragmentInContainer<TimeslotListFragment>()
        // scenario.recreate()
    }
}