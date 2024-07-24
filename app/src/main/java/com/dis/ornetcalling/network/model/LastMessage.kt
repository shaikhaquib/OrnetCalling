package com.dis.ornetcalling.network.model

data class LastMessage(
        val chat: String,
        val content: String,
        val deleted_at: Any,
        val id: String,
        val is_active: Boolean,
        val is_read: Boolean,
        val media_links: List<String>,
        val read_at: Any,
        val `receiver`: Receiver,
        val sender: Sender,
        val timestamp: String
    ) {
        data class Receiver(
            val email: String,
            val full_name: String,
            val id: String,
            val is_online: Boolean
        )

        data class Sender(
            val email: String,
            val full_name: String,
            val id: String,
            val is_online: Boolean
        )
    }