package com.mycompany.servicetime.timeslotdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mycompany.servicetime.FakeDataRepository
import com.mycompany.servicetime.MainCoroutineRule
import com.mycompany.servicetime.data.source.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TimeslotDetailViewModelTest {

    // Subject under test
    private lateinit var viewModel: TimeslotDetailViewModel

    // private val app: Application = mockk(relaxed = true)

    // Use a fake repository to be injected into the viewmodel
    private var dataDepository = FakeDataRepository()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // clearMocks(app)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun saveNewTimeslotToRepository() {
        // Initial ViewMode
        viewModel = TimeslotDetailViewModel(null, dataDepository)
        viewModel.start()

        // fill the data
        (viewModel).apply {
            title.value = TestData.timeslotEntity_1.title
            beginTimeHour.value = TestData.timeslotEntity_1.beginTimeHour
            beginTimeMunite.value = TestData.timeslotEntity_1.beginTimeMinute
            endTimeHour.value = TestData.timeslotEntity_1.endTimeHour
            endTimeMunite.value = TestData.timeslotEntity_1.endTimeMinute
            day0Selected.value = TestData.timeslotEntity_1.isSunSelected
            day1Selected.value = TestData.timeslotEntity_1.isMonSelected
            day2Selected.value = TestData.timeslotEntity_1.isTueSelected
            day3Selected.value = TestData.timeslotEntity_1.isWedSelected
            day4Selected.value = TestData.timeslotEntity_1.isThuSelected
            day5Selected.value = TestData.timeslotEntity_1.isFriSelected
            day6Selected.value = TestData.timeslotEntity_1.isSatSelected
        }

        viewModel.saveTimeslot()

        val newTimeslot = dataDepository.timeslotDbData.values.first()

        // Then a timeslot is saved in the repository
        assertThat(newTimeslot.title).isEqualTo(TestData.timeslotEntity_1.title)
        assertThat(newTimeslot.beginTimeHour).isEqualTo(TestData.timeslotEntity_1.beginTimeHour)
        assertThat(newTimeslot.isFriSelected).isEqualTo(TestData.timeslotEntity_1.isFriSelected)
    }

    @Test
    fun saveModifiedTimeslotToRepository() {
        // initial viewmodel
        dataDepository.addTimeslots(TestData.timeslotEntity_1)
        viewModel = TimeslotDetailViewModel(TestData.timeslotEntity_1.id, dataDepository)
        viewModel.start()

        // modify a field
        val newTitle = "Test New Title"
        (viewModel).apply {
            title.value = newTitle
        }

        viewModel.saveTimeslot()

        val newTimeslot = dataDepository.timeslotDbData.values.first()

        // Then a timeslot is saved in the repository
        assertThat(newTimeslot.title).isNotEqualTo(TestData.timeslotEntity_1.title)
        assertThat(newTimeslot.title).isEqualTo(newTitle)
        assertThat(newTimeslot.beginTimeHour).isEqualTo(TestData.timeslotEntity_1.beginTimeHour)
        assertThat(newTimeslot.isFriSelected).isEqualTo(TestData.timeslotEntity_1.isFriSelected)
    }

    @Test
    fun saveTimeslotToRepository_emptyTitle_error() {
        // initial viewmodel
        viewModel = TimeslotDetailViewModel(null, dataDepository)
        viewModel.start()

        // set title to empty
        (viewModel).apply {
            title.value = ""
        }

        viewModel.saveTimeslot()

        val timeslots = dataDepository.timeslotDbData

        // Then a timeslot is NOT saved in the repository and show the error at UI
        assertThat(timeslots).isEmpty()
        assertThat(viewModel.titleError.value).isEqualTo("Name can not be empty!")
    }

    @Test
    fun saveTimeslotToRepository_notChooseDays_error() {
        // initial viewmodel
        viewModel = TimeslotDetailViewModel(null, dataDepository)
        viewModel.start()

        // do not choose any days
        (viewModel).apply {
            title.value = "Test"
            day0Selected.value = false
            day1Selected.value = false
            day2Selected.value = false
            day3Selected.value = false
            day4Selected.value = false
            day5Selected.value = false
            day6Selected.value = false
        }

        viewModel.saveTimeslot()

        val timeslots = dataDepository.timeslotDbData

        // Then a timeslot is NOT saved in the repository and send a error message event
        assertThat(timeslots).isEmpty()
        assertThat(viewModel.currentViewEvent.value).isNotNull()
    }

    @Test
    fun deleteTimeslotToRepository() {
        // initial viewmodel
        dataDepository.addTimeslots(TestData.timeslotEntity_1)
        viewModel = TimeslotDetailViewModel(TestData.timeslotEntity_1.id, dataDepository)
        viewModel.start()

        viewModel.deleteTimeslot()

        val timeslots = dataDepository.timeslotDbData

        // Then a timeslot is deleted from the repository
        assertThat(timeslots).isEmpty()
    }
}