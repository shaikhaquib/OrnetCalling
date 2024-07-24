package com.dis.ornetcalling.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dis.ornetcalling.databinding.ItemMemberBinding
import com.dis.ornetcalling.network.model.GroupMemberResponse

class MemberAdapter : ListAdapter<GroupMemberResponse.GroupMemberResponseItem.User, MemberAdapter.MemberViewHolder>(MemberDiffCallback()) {

    private val selectedUsers = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MemberViewHolder(private val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GroupMemberResponse.GroupMemberResponseItem.User) {
            binding.tvName.text = user.full_name
            // Load avatar image using a library like Glide or Coil
            // Glide.with(binding.root).load(user.avatar).into(binding.ivAvatar)

            binding.cbSelect.isChecked = selectedUsers.contains(user.id)
            binding.root.setOnClickListener {
                binding.cbSelect.isChecked = !binding.cbSelect.isChecked
            }

            binding.cbSelect.setOnCheckedChangeListener { compoundButton, b ->
                if (binding.cbSelect.isChecked) {
                    selectedUsers.add(user.id)
                } else {
                    selectedUsers.remove(user.id)
                }
            }
        }
    }

    fun getSelectedUserIds(): List<String> {
        return selectedUsers.toList()
    }
}

class MemberDiffCallback : DiffUtil.ItemCallback<GroupMemberResponse.GroupMemberResponseItem.User>() {
    override fun areItemsTheSame(oldItem: GroupMemberResponse.GroupMemberResponseItem.User, newItem: GroupMemberResponse.GroupMemberResponseItem.User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupMemberResponse.GroupMemberResponseItem.User, newItem: GroupMemberResponse.GroupMemberResponseItem.User): Boolean {
        return oldItem == newItem
    }
}