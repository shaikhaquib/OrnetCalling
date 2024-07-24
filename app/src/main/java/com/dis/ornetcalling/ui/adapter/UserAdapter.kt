package com.dis.ornetcalling.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dis.ornetcalling.databinding.ItemUserBinding
import com.dis.ornetcalling.network.model.UserModel

class UserAdapter(
    private var filteredUsers: List<UserModel>,
    private val onUserClick: (UserModel) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var users: List<UserModel> = filteredUsers

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredUsers[position])
    }

    fun setUsers(newUsers: List<UserModel>) {
        users = newUsers
        filteredUsers = newUsers
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredUsers = if (query.isEmpty()) {
            users
        } else {
            users.filter { user ->
              (  user.full_name.toString()?:"").contains(query, ignoreCase = true) ||
                        user.email.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel) {
            binding.apply {
                userName.text = user.full_name.toString()
                userEmail.text = user.email
                userOnlineStatus.isVisible = user.is_online
                root.setOnClickListener { onUserClick(user) }
            }
        }
    }
}
