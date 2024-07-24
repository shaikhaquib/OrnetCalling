package com.dis.ornetcalling.network.model

data class UserModel(
    val email: String,
    val full_name: String?,
    val id: String,
    val is_online: Boolean
)