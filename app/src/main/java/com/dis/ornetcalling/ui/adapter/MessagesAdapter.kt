package com.dis.ornetcalling.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dis.ornetcalling.R
import com.dis.ornetcalling.network.model.Message
import com.dis.ornetcalling.databinding.ItemMessageBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MessagesAdapter(
    private var messages: List<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        with(holder.binding) {
            tvSender.text = message.user
            tvContent.text = message.message
            tvTimestamp.text = formatTimestamp(message.timestamp)

            val isCurrentUser = message.user == currentUserId
            val params = messageContainer.layoutParams as RelativeLayout.LayoutParams

            if (isCurrentUser) {
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                params.removeRule(RelativeLayout.ALIGN_PARENT_START)
                messageContainer.background = ContextCompat.getDrawable(root.context,
                    R.drawable.chat_bubble_right
                )
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_START)
                params.removeRule(RelativeLayout.ALIGN_PARENT_END)
                messageContainer.background = ContextCompat.getDrawable(root.context,
                    R.drawable.chat_bubble_left
                )
            }

            messageContainer.layoutParams = params

            val timestampParams = tvTimestamp.layoutParams as RelativeLayout.LayoutParams
            if (isCurrentUser) {
                timestampParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                timestampParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
            } else {
                timestampParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                timestampParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
            }
            tvTimestamp.layoutParams = timestampParams
        }
    }

    override fun getItemCount() = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    private fun formatTimestamp(timestamp: String): String {
        // Assuming the timestamp is in ISO 8601 format
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(timestamp)

        val outputFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        return outputFormat.format(date ?: Date())
    }
}