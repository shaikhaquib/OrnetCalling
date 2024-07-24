package com.dis.ornetcalling.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dis.ornetcalling.network.model.Group
import com.dis.ornetcalling.databinding.ItemGroupBinding

class GroupsAdapter(
    private var groups: List<Group>,
    private val onItemClick: (Group) -> Unit
) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        with(holder.binding) {
            tvGroupName.text = group.name
            tvGroupDescription.text = group.toString()
            root.setOnClickListener { onItemClick(group) }
        }
    }

    override fun getItemCount() = groups.size

    fun updateGroups(newGroups: List<Group>) {
        groups = newGroups
        notifyDataSetChanged()
    }
}