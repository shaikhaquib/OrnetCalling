package com.dis.ornetcalling.network

import com.dis.ornetcalling.network.model.CreateChatResponse
import com.dis.ornetcalling.network.model.Group
import com.dis.ornetcalling.network.model.GroupMemberResponse
import com.dis.ornetcalling.network.model.LastMessage
import com.dis.ornetcalling.network.model.LoginResponse
import com.dis.ornetcalling.network.model.Message
import com.dis.ornetcalling.network.model.ModelCreateMemebers
import com.dis.ornetcalling.network.model.OnetoOneChatList
import com.dis.ornetcalling.network.model.ReadReciptModel
import com.dis.ornetcalling.network.model.SignUpResponse
import com.dis.ornetcalling.network.model.UpdateUserModel
import com.dis.ornetcalling.network.model.UserModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("accounts/signup/")
    suspend fun signUp(@Body signUpRequest: Map<String, String>): SignUpResponse

    @POST("accounts/login/")
    suspend fun login(@Body loginRequest: Map<String, String>): LoginResponse

    @GET("groupchat/user/groups/")
    suspend fun getGroups(@Header("Authorization") token: String): List<Group>

    @POST("groupchat/groups/")
    suspend fun createGroup(
        @Header("Authorization") token: String,
        @Body createGroupRequest: Map<String, String>
    ): Group

    @GET("groupchat/group/group-messages/{groupId}/")
    suspend fun getGroupMessages(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String
    ): List<Message>

    @GET("groupchat/group/group-members/{groupId}/")
    suspend fun getGroupMember(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String
    ): GroupMemberResponse

    @GET("groupchat/group/non-group-members/{groupId}/")
    suspend fun getNonGroupMember(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String
    ): List<GroupMemberResponse.GroupMemberResponseItem.User>

    @POST("groupchat/group-members/")
    suspend fun addGroupMember(
        @Header("Authorization") token: String,
        @Body addMemberRequest: AddMemberRequest
    ): ModelCreateMemebers

    @GET("accounts/users/")
    suspend fun getAllUser(
        @Header("Authorization") token: String,
    ): List<UserModel>

    @POST("normalchat/chats/create/")
    suspend fun createChat(
        @Header("Authorization") token: String,
        @Body createGroupRequest: Map<String, String>
    ): CreateChatResponse

    @GET("normalchat/chats/")
    suspend fun getOntoOneChat(
        @Header("Authorization") token: String,
    ): List<OnetoOneChatList>

    @GET("normalchat/chats/{chatId}/messages/")
    suspend fun getOntoOneChatMessage(
        @Header("Authorization") token: String,
        @Path("chatId") chatId: String
    ): List<LastMessage>

    @PUT("/accounts/users/{userId}/update/")
    suspend fun updateOnlineStatus(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body data: Map<String, Any>
    ): UpdateUserModel

    @POST("normalchat/messages/mark-as-read/")
    suspend fun markAsRead(
        @Header("Authorization") token: String,
        @Body data: MarkAsReadRequest
    ): ReadReciptModel
}

data class AddMemberRequest(
    val users: List<String>,
    val group: String
)

data class MarkAsReadRequest(
    val message_ids: List<String>,
)
