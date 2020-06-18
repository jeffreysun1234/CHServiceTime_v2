package com.mycompany.servicetime.timeslotdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mycompany.servicetime.R
import com.mycompany.servicetime.databinding.FragmentTimeslotDetailBinding
import com.mycompany.servicetime.utilities.EventObserver
import com.mycompany.servicetime.utilities.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
class TimeslotDetailFragment : Fragment() {

    private val args: TimeslotDetailFragmentArgs by navArgs()

    private val viewModel: TimeslotDetailViewModel by viewModel { parametersOf(args.timeslotId) }

    private lateinit var binding: FragmentTimeslotDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentTimeslotDetailBinding>(
            inflater, R.layout.fragment_timeslot_detail, container, false
        ).apply {
            timeslotBeginTime.setIs24HourView(true)
            timeslotEndTime.setIs24HourView(true)
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCustomTitle()
        handleViewEvent()
        viewModel.start()
    }

    private fun setCustomTitle() {
        val resTitle = if (args.timeslotId == null) R.string.title_new else R.string.title_update
        requireActivity().title = resources.getText(resTitle)
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
                is TimeslotDetailResult.Title -> {
                    binding.root.showSnackbar(
                        (it as TimeslotDetailResult.Title).resTitle,
                        Snackbar.LENGTH_LONG
                    )
                }
            }

        })
    }
}
