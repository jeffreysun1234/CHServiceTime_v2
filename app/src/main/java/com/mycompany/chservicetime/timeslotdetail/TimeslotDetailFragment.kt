package com.mycompany.chservicetime.timeslotdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mycompany.chservicetime.R
import com.mycompany.chservicetime.databinding.FragmentTimeslotDetailBinding
import com.mycompany.chservicetime.utilities.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
class TimeslotDetailFragment : Fragment() {

    private val args: TimeslotDetailFragmentArgs by navArgs()

    private val viewModel: TimeslotDetailViewModel by viewModel { parametersOf(args.timeslotId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewDataBinding = DataBindingUtil.inflate<FragmentTimeslotDetailBinding>(
            inflater, R.layout.fragment_timeslot_detail, container, false
        ).apply {
            timeslotBeginTime.setIs24HourView(true)
            timeslotEndTime.setIs24HourView(true)
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setHasOptionsMenu(true)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        handleViewEvent()
        viewModel.start()
    }

    // private fun setupSnackbar() {
    //     view?.setupSnackbar(this, viewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    // }

    private fun handleViewEvent() {
        viewModel.currentViewEvent.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is TimeslotDetailResult.CreatedSuccess -> {
                    val action =
                        TimeslotDetailFragmentDirections.actionTimeslotDetailFragmentToTimeslotListFragment()
                    findNavController().navigate(action)
                }
                is TimeslotDetailResult.UpdatedSuccess -> {
                    val action =
                        TimeslotDetailFragmentDirections.actionTimeslotDetailFragmentToTimeslotListFragment()
                    findNavController().navigate(action)
                }
                is TimeslotDetailResult.DeletedSuccess -> {
                    val action = TimeslotDetailFragmentDirections
                        .actionTimeslotDetailFragmentToTimeslotListFragment()
                    findNavController().navigate(action)
                }
            }

        })
    }
}
