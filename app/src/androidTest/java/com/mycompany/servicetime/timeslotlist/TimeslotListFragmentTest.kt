package com.mycompany.servicetime.timeslotlist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mycompany.servicetime.MainActivity
import com.mycompany.servicetime.MainCoroutineRule
import com.mycompany.servicetime.R
import com.mycompany.servicetime.data.source.DataRepository
import com.mycompany.servicetime.data.source.TestData
import com.mycompany.servicetime.getEndTimeForTest
import com.mycompany.servicetime.testAppModules
import com.mycompany.servicetime.timeslotdetail.TimeslotDetailFragment
import com.mycompany.servicetime.utilities.getFormatHourMinuteString
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TimeslotListFragmentTest : KoinTest {

    private val dataRepository: DataRepository by inject()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    init {
        loadKoinModules(testAppModules)
    }

    @Before
    fun setup() {
        runBlockingTest {
            dataRepository.deleteAllTimeslot()
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun displayEmpty_whenRepositoryIsEmpty() {
        runBlockingTest {
            val scenario = launchFragmentInContainer<TimeslotListFragment>()
            onView(withText(get<Context>().resources.getString(R.string.empty_timeslot_list)))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun displayTimeslotList_whenRepositoryHasData() {
        // GIVEN - One Timeslot already in the repository
        runBlockingTest {
            dataRepository.saveTimeslot(TestData.timeslotEntity_1)
        }
        // WHEN - On startup
        val scenario = launchFragmentInContainer<TimeslotListFragment>()
        onView(withText(TestData.timeslotEntity_1.title)).check(matches(isDisplayed()))
    }

    @Test
    fun showTimslotList() {
        runBlockingTest {
            dataRepository.saveTimeslot(TestData.timeslotEntity_1)
            dataRepository.saveTimeslot(TestData.timeslotEntity_2)
            dataRepository.saveTimeslot(TestData.timeslotEntity_3)
        }

        val scenario = launchFragmentInContainer<TimeslotListFragment>()

        onView(withText(TestData.timeslotEntity_1.title)).check(matches(isDisplayed()))
        onView(withText(TestData.timeslotEntity_2.title)).check(matches(isDisplayed()))
        onView(withText(TestData.timeslotEntity_3.title)).check(matches(isDisplayed()))
    }

    @Test
    fun markTimeslotAsActive() {
        val pair = getEndTimeForTest()
        val formatEndTimeString = getFormatHourMinuteString(pair.first, pair.second)

        runBlockingTest {
            dataRepository.saveTimeslot(
                TestData.timeslotEntity_all_week.copy(
                    beginTimeHour = pair.first,
                    beginTimeMinute = pair.second
                )
            )
        }

        val scenario = launchFragmentInContainer<TimeslotListFragment>()

        // Mark the task as active
        onView(checkboxWithText(TestData.timeslotEntity_all_week.title)).perform(click())

        // Verify task is shown and next alarm show the time
        // TODO: fix the error for multiple match
        // onView(withText(TestData.timeslotEntity_all_week.title)).check(matches(isDisplayed()))
        onView(
            withText(
                get<Context>().resources.getString(
                    R.string.next_alarm_time_label,
                    formatEndTimeString
                )
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun clickAddTimeslotButton_atEmptyList_navigateToDetailFragment() {
        // Create a TestNavHostController
        val testNavController = TestNavHostController(ApplicationProvider.getApplicationContext())
        testNavController.setGraph(R.navigation.nav_graph)

        // On the home screen
        val scenario = launchFragmentInContainer<TimeslotListFragment>(Bundle(), R.style.AppTheme)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), testNavController)
        }

        // WHEN - Click on the add button
        onView(withId(R.id.add_timeslot)).perform(click())

        // THEN - Verify that we navigate to the add screen
        assertThat(testNavController.currentDestination?.id, equalTo(R.id.timeslotDetailFragment))
    }

    @Test
    fun clickAddTimeslotMenuItem_navigateToDetailFragment() {
        val activityScenario = launchActivity<MainActivity>()

        // WHEN - Click on the add button
        onView(withId(R.id.action_add_timeslot)).perform(click())

        activityScenario.onActivity { activity ->
            val currentFragment =
                activity.nav_host?.childFragmentManager?.primaryNavigationFragment

            // THEN - Verify that we navigate to the add screen
            assertThat(currentFragment, IsInstanceOf(TimeslotDetailFragment::class.java))
        }

        // ActivityScenario does’t clean up device state automatically
        // and may leave the activity keep running after the test finishes.
        activityScenario.close()
    }

    private fun checkboxWithText(text: String): Matcher<View> {
        return allOf(withId(R.id.activate), hasSibling(withText(text)))
    }
}