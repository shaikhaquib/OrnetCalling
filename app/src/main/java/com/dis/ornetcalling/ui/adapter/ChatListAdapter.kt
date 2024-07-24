package com.dis.ornetcalling.ui.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dis.ornetcalling.R
import com.dis.ornetcalling.databinding.ItemChatBinding
import com.dis.ornetcalling.network.model.OnetoOneChatList
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChatListAdapter(private val onChatClicked: (OnetoOneChatList) -> Unit) :
    RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    private val items = mutableListOf<OnetoOneChatList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<OnetoOneChatList>) {
        items.clear()
        items.addAll(newItems.sortedByDescending { it.last_message?.timestamp })
        notifyDataSetChanged()
    }

    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: OnetoOneChatList) {
            binding.apply {
                textViewName.text = chat.opposite_user.full_name ?: "Unknown"
                if (chat.last_message == null) {
                    textViewTime.visibility = View.GONE
                    textViewLastMessage.text = "No messages yet"
                } else {
                    textViewTime.text = formatTime(chat.last_message.timestamp)
                    textViewLastMessage.text = chat.last_message.content
                }

                textViewUnreadCount.apply {
                    isVisible = chat.unread_count > 0
                    text = chat.unread_count.toString()
                }
                imageViewOnlineStatus.isVisible = chat.opposite_user.is_online

                // Set user avatar (you might want to use a library like Glide or Coil for image loading)
                imageViewAvatar.setImageResource(R.drawable.default_avatar)

                root.setOnClickListener { onChatClicked(chat) }
            }
        }
    }

    private fun formatTime(timestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date = sdf.parse(timestamp)
            val now = System.currentTimeMillis()
            val diff = now - date.time

            return when {
                diff < DateUtils.MINUTE_IN_MILLIS -> "just now"
                diff < DateUtils.HOUR_IN_MILLIS -> "${diff / DateUtils.MINUTE_IN_MILLIS} minutes ago"
                diff < 12 * DateUtils.HOUR_IN_MILLIS -> "${diff / DateUtils.HOUR_IN_MILLIS} hours ago"
                else -> {
                    val formatter = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US)
                    formatter.format(date)
                }
            }
        } catch (e: Exception) {
            return "Invalid date"
        }
    }
}