package com.mycompany.chservicetime.timeslotlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.chservicetime.R
import com.mycompany.chservicetime.data.source.local.TimeslotEntity
import com.mycompany.chservicetime.databinding.ItemTimeslotListBinding

class TimeslotListAdapter :
    ListAdapter<TimeslotEntity, TimeslotListAdapter.ViewHolder>(
        TimeslotDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_timeslot_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTimeslotListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setItemClickListener { view ->
                binding.timeslot?.let { timeslot ->
                    navigateToTimeslotDetail(timeslot.id, view)
                }
            }
            binding.setActiviteCheckBoxClickListener { view ->
                binding.timeslot?.let { timeslot ->
                    //TODO
                }
            }
        }

        private fun navigateToTimeslotDetail(timeslotId: String, view: View) {
            val direction = TimeslotListFragmentDirections
                .actionTimeslotListFragmentToTimeslotDetailFragment(timeslotId)
            view.findNavController().navigate(direction)
        }

        fun bind(item: TimeslotEntity) {
            with(binding) {
                timeslot = item
                executePendingBindings()
            }
        }
    }
}

private class TimeslotDiffCallback : DiffUtil.ItemCallback<TimeslotEntity>() {

    override fun areItemsTheSame(
        oldItem: TimeslotEntity,
        newItem: TimeslotEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TimeslotEntity,
        newItem: TimeslotEntity
    ): Boolean {
        return oldItem == newItem
    }
}