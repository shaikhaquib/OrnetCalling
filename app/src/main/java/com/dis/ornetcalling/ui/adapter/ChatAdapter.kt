package com.dis.ornetcalling.ui.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dis.ornetcalling.R
import com.dis.ornetcalling.databinding.ItemChatReceiverBinding
import com.dis.ornetcalling.databinding.ItemChatSenderBinding
import com.dis.ornetcalling.network.model.LastMessage
import com.dis.ornetcalling.ui.activity.ImageViewerActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChatAdapter(private val currentUserId: String) :
    ListAdapter<LastMessage, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_RECEIVER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER -> SenderViewHolder(
                ItemChatSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            VIEW_TYPE_RECEIVER -> ReceiverViewHolder(
                ItemChatReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is SenderViewHolder -> holder.bind(message)
            is ReceiverViewHolder -> holder.bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.sender.id == currentUserId) VIEW_TYPE_SENDER else VIEW_TYPE_RECEIVER
    }

    inner class SenderViewHolder(private val binding: ItemChatSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: LastMessage) {
            binding.messageTextView.text = message.content
            binding.timestampTextView.text = formatTimestamp(message.timestamp)
            binding.seenImageView.setImageResource(if (message.is_read) R.drawable.ic_seen else R.drawable.baseline_check_24)
            setupImageRecyclerView(binding.imageRecyclerView, message.media_links)
        }
    }

    inner class ReceiverViewHolder(private val binding: ItemChatReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: LastMessage) {
            binding.messageTextView.text = message.content
            binding.timestampTextView.text = formatTimestamp(message.timestamp)

            setupImageRecyclerView(binding.imageRecyclerView, message.media_links)
        }
    }

    private fun setupImageRecyclerView(recyclerView: RecyclerView, mediaLinks: List<String>) {
        if (mediaLinks.isNotEmpty()) {
            recyclerView.isVisible = true
            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 3)
            recyclerView.adapter = ImageAdapter(mediaLinks) { imageUrls, position ->
                // Open image viewer
                ImageViewerActivity.start(recyclerView.context, imageUrls, position)
            }
        } else {
            recyclerView.isVisible = false
        }
    }

    private fun formatTimestamp(timestamp: String): String {
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

    private class MessageDiffCallback : DiffUtil.ItemCallback<LastMessage>() {
        override fun areItemsTheSame(oldItem: LastMessage, newItem: LastMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LastMessage, newItem: LastMessage): Boolean {
            return oldItem == newItem
        }
    }
}