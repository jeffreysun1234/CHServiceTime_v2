package com.mycompany.servicetime.timeslotlist

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mycompany.servicetime.FakeDataRepository
import com.mycompany.servicetime.LiveDataTestUtil
import com.mycompany.servicetime.MainCoroutineRule
import com.mycompany.servicetime.data.source.TestData
import com.mycompany.servicetime.services.DNDController
import com.mycompany.servicetime.services.MuteOperator
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TimeslotListViewModelTest {

    // Subject under test
    private lateinit var viewModel: TimeslotListViewModel

    private val app: Application = mockk(relaxed = true)
    // private val app = mockkClass(Application::class, relaxed = true)

    // Use a fake repository to be injected into the viewmodel
    private lateinit var dataDepository: FakeDataRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    init {
        dataDepository = FakeDataRepository()
        dataDepository.addTimeslots(TestData.timeslotEntity_1, TestData.timeslotEntity_2)
    }

    @Before
    fun setUp() {
        clearMocks(app)
        viewModel = TimeslotListViewModel(app, dataDepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getTimeslotList() {
        // data correctly loaded
        assertThat(LiveDataTestUtil.getValue(viewModel.timeslotList)).hasSize(2)
    }

    @Test
    fun doActivateTimeslot() {
        viewModel.doActivateTimeslot(TestData.timeslotEntity_1)

        val updatedTimeslot = dataDepository.timeslotDbData[TestData.timeslotEntity_1.id]
        assertThat(updatedTimeslot!!.isActivated).isNotEqualTo(TestData.timeslotEntity_1.isActivated)
    }

    @Test
    fun triggerMuteService_permission_granted() {
        mockkConstructor(DNDController::class)
        mockkConstructor(MuteOperator::class)

        // permission granted
        every { anyConstructed<DNDController>().checkDndPermission(any()) } returns true
        every { anyConstructed<MuteOperator>().execute() } just Runs

        viewModel.triggerMuteService()

        // verify the call of the mute operation
        verify { anyConstructed<MuteOperator>().execute() }
    }

    @Test
    fun triggerMuteService_permission_refused() {
        mockkConstructor(DNDController::class)

        // permission refused
        every { anyConstructed<DNDController>().checkDndPermission(any()) } returns false

        viewModel.triggerMuteService()

        assertThat(LiveDataTestUtil.getValue(viewModel.currentViewEvent).getContentIfNotHandled())
            .isEqualTo(TimeslotListResult.NeedDNDPermission)
    }
}