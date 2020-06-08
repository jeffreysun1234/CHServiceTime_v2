package com.mycompany.chservicetime.timeslotlist

import PREFERENCE_FILE_NAME
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mycompany.chservicetime.R
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import com.mycompany.chservicetime.databinding.FragmentTimeslotListBinding
import com.mycompany.chservicetime.utilities.EventObserver
import com.mycompany.chservicetime.utilities.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimeslotListFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: FragmentTimeslotListBinding

    private val viewModel: TimeslotListViewModel by viewModel()

    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adapter = TimeslotListAdapter { timeslot: TimeslotEntity ->
            viewModel.doActivateTimeslot(timeslot)
        }

        binding = FragmentTimeslotListBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner

            timeslotList.adapter = adapter

            addTimeslot.setOnClickListener {
                navigateToTimeslotDetail()
            }
        }

        subscribeUi(adapter, binding)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        handleViewEvent()
    }

    override fun onResume() {
        super.onResume()
        viewModel._nextAlarmTime.value =
            sharedPreferences.getString(getString(R.string.pref_key_next_alarm_timepoint), "")
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_add_timeslot -> {
                navigateToTimeslotDetail()
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_timeslot_list, menu)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_key_next_alarm_timepoint)) {
            viewModel._nextAlarmTime.value = sharedPreferences.getString(key, "")
        }
    }

    private fun subscribeUi(adapter: TimeslotListAdapter, binding: FragmentTimeslotListBinding) {
        viewModel.timeslotList.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result)
            viewModel.triggerMuteService()
        })
    }

    private fun navigateToTimeslotDetail() {
        val direction = TimeslotListFragmentDirections
            .actionTimeslotListFragmentToTimeslotDetailFragment(null)
        findNavController().navigate(direction)
    }

    private fun handleViewEvent() {
        viewModel.currentViewEvent.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is TimeslotListResult.NeedDNDPermission -> {
                    binding.root.showSnackbar(R.string.need_dnd_permission, Snackbar.LENGTH_SHORT)
                }
            }

        })
    }
}
