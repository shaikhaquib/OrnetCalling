package com.dis.ornetcalling.network.model

data class ModelCreateMemebers(
    val errors: List<Any>,
    val successful_users: List<SuccessfulUser>
) {
    data class SuccessfulUser(
        val group: String,
        val id: String,
        val is_admin: Boolean,
        val user: String
    )
}