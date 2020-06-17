package com.mycompany.servicetime.timeslotlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.servicetime.R
import com.mycompany.servicetime.data.source.local.TimeslotEntity
import com.mycompany.servicetime.databinding.ItemTimeslotListBinding

class TimeslotListAdapter(private val onActiviteTimeslot: (TimeslotEntity) -> Unit) :
    ListAdapter<TimeslotEntity, TimeslotListAdapter.ViewHolder>(
        TimeslotDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_timeslot_list, parent, false
            ),
            onActiviteTimeslot
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vItem: TimeslotEntity = getItem(position)
        holder.bind(vItem)
    }

    class ViewHolder(
        val binding: ItemTimeslotListBinding,
        val onActiviteTimeslot: (TimeslotEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setItemClickListener { view ->
                binding.timeslot?.let { timeslot ->
                    navigateToTimeslotDetail(timeslot.id, view)
                }
            }
            binding.setActiviteCheckBoxClickListener {
                binding.timeslot?.let { timeslot ->
                    onActiviteTimeslot(timeslot)
                }
            }
        }

        fun bind(item: TimeslotEntity) {
            with(binding) {
                timeslot = item
                executePendingBindings()
            }
        }

        private fun navigateToTimeslotDetail(timeslotId: String, view: View) {
            val direction = TimeslotListFragmentDirections
                .actionTimeslotListFragmentToTimeslotDetailFragment(timeslotId)
            view.findNavController().navigate(direction)
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