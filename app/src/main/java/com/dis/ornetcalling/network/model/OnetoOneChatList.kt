package com.dis.ornetcalling.network.model

class OnetoOneChatList(
    val created_at: String,
    val id: String,
    val is_active: Boolean,
    val is_archived: Boolean,
    val last_message: LastMessage?,
    val opposite_user: OppositeUser,
    val unread_count: Int,
    val updated_at: String,
) {
    data class OppositeUser(
        val email: String,
        val full_name: String?,
        val id: String,
        val is_online: Boolean
    )
}