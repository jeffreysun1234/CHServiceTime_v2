package com.mycompany.servicetime.timeslotdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mycompany.servicetime.MainCoroutineRule
import com.mycompany.servicetime.R
import com.mycompany.servicetime.data.source.DataRepository
import com.mycompany.servicetime.data.source.TestData
import com.mycompany.servicetime.testAppModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TimeslotDetailFragmentTest : KoinTest {

    private val dataRepository: DataRepository by inject()

    // Set the main coroutines dispatcher for unit testing.
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
    fun emptyTimeslot_isNotSaved() {
        // GIVEN - On the "New Timeslot" screen.
        val bundle = TimeslotDetailFragmentArgs(null).toBundle()
        launchFragmentInContainer<TimeslotDetailFragment>(bundle, R.style.AppTheme)

        // WHEN - Enter invalid title and click save
        onView(withId(R.id.timeslot_title)).perform(clearText())
        onView(withId(R.id.save_button)).perform(click())

        // THEN - Entered Timeslot is still displayed (a correct task would close it).
        onView(withId(R.id.timeslot_title)).check(matches(isDisplayed()))
        // show a error notice at the Title edittext
        onView(withId(R.id.timeslot_title)).check(matches(hasErrorText("Name can not be empty!")));
    }

    @Test
    fun validTimeslot_navigatesBack() {
        // Create a TestNavHostController
        val navController =
            TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
                setGraph(R.navigation.nav_graph)
                setCurrentDestination(R.id.timeslotDetailFragment)
            }

        // GIVEN - On the "New Timeslot" screen.
        val bundle = TimeslotDetailFragmentArgs(null).toBundle()
        // val scenario = launchFragmentInContainer(bundle, R.style.AppTheme){
        //     TimeslotDetailFragment().also { fragment ->
        //         // In addition to returning a new instance of our Fragment,
        //         // get a callback whenever the fragment’s view is created
        //         // or destroyed so that we can set the NavController
        //         fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
        //             if (viewLifecycleOwner != null) {
        //                 // The fragment’s view has just been created
        //                 Navigation.setViewNavController(fragment.requireView(), navController)
        //             }
        //         }
        //     }
        // }

        val scenario = launchFragmentInContainer<TimeslotDetailFragment>(bundle, R.style.AppTheme)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        // WHEN - Give some data and click save
        onView(withId(R.id.timeslot_title)).perform(replaceText(TestData.timeslotEntity_1.title))
        onView(withId(R.id.day_fri_button)).perform(click())
        onView(withId(R.id.save_button)).perform(click())

        // THEN - Verify that we navigated back to the timeslotList screen.
        assertThat(navController.currentDestination?.id, equalTo(R.id.timeslotListFragment))
    }
}