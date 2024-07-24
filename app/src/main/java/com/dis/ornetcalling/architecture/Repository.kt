package com.dis.ornetcalling.architecture

import com.dis.ornetcalling.network.AddMemberRequest
import com.dis.ornetcalling.network.ApiService
import com.dis.ornetcalling.network.MarkAsReadRequest
import com.dis.ornetcalling.network.model.CreateChatResponse
import com.dis.ornetcalling.network.model.Group
import com.dis.ornetcalling.network.model.GroupMember
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
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path

class Repository {
    private val apiService: ApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.234.119.125/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun signUp(signUpRequest: Map<String, String>): SignUpResponse {
        return apiService.signUp(signUpRequest)
    }

    suspend fun login(loginRequest: Map<String, String>): LoginResponse {
        return apiService.login(loginRequest)
    }

    suspend fun getGroups(token: String): List<Group> {
        return apiService.getGroups("Token $token")
    }

    suspend fun createGroup(token: String, createGroupRequest: Map<String, String>): Group {
        return apiService.createGroup("Token $token", createGroupRequest)
    }

    suspend fun createChat(
        token: String,
        createGroupRequest: Map<String, String>
    ): CreateChatResponse {
        return apiService.createChat("Token $token", createGroupRequest)
    }

    suspend fun getGroupMessages(token: String, groupId: String): List<Message> {
        return apiService.getGroupMessages("Token $token", groupId)
    }

    suspend fun getGroupMember(token: String, groupId: String): GroupMemberResponse {
        return apiService.getGroupMember("Token $token", groupId)
    }

    suspend fun getNonGroupMember(
        token: String,
        groupId: String
    ): List<GroupMemberResponse.GroupMemberResponseItem.User> {
        return apiService.getNonGroupMember("Token $token", groupId)
    }

    suspend fun addGroupMember(
        token: String,
        addMemberRequest: AddMemberRequest
    ): ModelCreateMemebers {
        return apiService.addGroupMember("Token $token", addMemberRequest)
    }

    suspend fun getAllUser(
        token: String,
    ): List<UserModel> {
        return apiService.getAllUser("Token $token")
    }

    suspend fun getOnetoOneChat(
        token: String,
    ): List<OnetoOneChatList> {
        return apiService.getOntoOneChat("Token $token")
    }

    suspend fun getOntoOneChatMessage(
        token: String,
        chatId: String
    ): List<LastMessage> {
        return apiService.getOntoOneChatMessage("Token $token", chatId)
    }

    suspend fun updateOnlineStatus(
        token: String,
        userId: String,
        data: Map<String, Any>
    ): UpdateUserModel {
        return apiService.updateOnlineStatus("Token $token", userId,data)
    }
    suspend fun markAsRead(
        token: String,
        data: MarkAsReadRequest
    ): ReadReciptModel {
        return apiService.markAsRead("Token $token",data)
    }

}