package com.dis.ornetcalling.network.model

class GroupMemberResponse : ArrayList<GroupMemberResponse.GroupMemberResponseItem>(){
    data class GroupMemberResponseItem(
        val group: String,
        val id: String,
        val is_admin: Boolean,
        val user: User
    ) {
        data class User(
            val email: String,
            val full_name: String,
            val id: String
        )
    }
}