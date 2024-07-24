package com.dis.ornetcalling.network.model

data class UpdateUserModel(
    val email: String,
    val full_name: String,
    val id: String,
    val is_active: Boolean,
    val is_online: Boolean,
    val is_staff: Boolean,
    val is_verified: Boolean,
    val last_login_time: Any,
    val last_logout_time: Any
)