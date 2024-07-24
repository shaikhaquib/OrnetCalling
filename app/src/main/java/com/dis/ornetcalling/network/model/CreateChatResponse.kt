package com.dis.ornetcalling.network.model

data class CreateChatResponse(
    val created_at: String,
    val id: String,
    val is_active: Boolean,
    val is_archived: Boolean,
    val last_message: Any,
    val updated_at: String,
    val user1: User1,
    val user2: User2
) {
    data class User1(
        val email: String,
        val full_name: String,
        val id: String,
        val is_online: Boolean
    )

    data class User2(
        val email: String,
        val full_name: Any,
        val id: String,
        val is_online: Boolean
    )
}