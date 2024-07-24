package com.dis.ornetcalling.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dis.ornetcalling.R
import com.dis.ornetcalling.network.model.GroupMemberResponse

class GroupMemberAdapter(private val members: List<GroupMemberResponse.GroupMemberResponseItem>) :
    RecyclerView.Adapter<GroupMemberAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val ivAdmin: ImageView = view.findViewById(R.id.ivAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = members[position]
        holder.tvName.text = member.user.full_name
        holder.tvEmail.text = member.user.email
        holder.ivAdmin.visibility = if (member.is_admin) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = members.size
}