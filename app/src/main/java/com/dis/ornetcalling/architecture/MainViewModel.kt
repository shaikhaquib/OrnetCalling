package com.dis.ornetcalling.architecture

import android.graphics.ColorSpace.Model
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dis.ornetcalling.network.AddMemberRequest
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
import com.dis.ornetcalling.network.model.SignUpResponse
import com.dis.ornetcalling.network.model.UpdateUserModel
import com.dis.ornetcalling.network.model.UserModel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = Repository()

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _signUpResult = MutableLiveData<Result<SignUpResponse>>()
    val signUpResult: LiveData<Result<SignUpResponse>> = _signUpResult

    private val _groups = MutableLiveData<Result<List<Group>>>()
    val groups: LiveData<Result<List<Group>>> = _groups

    private val _messages = MutableLiveData<Result<List<Message>>>()
    val messages: LiveData<Result<List<Message>>> = _messages

    private val _groupMember = MutableLiveData<Result<GroupMemberResponse>>()
    val groupMember: LiveData<Result<GroupMemberResponse>> = _groupMember

    private val _nonGroupMember =
        MutableLiveData<Result<List<GroupMemberResponse.GroupMemberResponseItem.User>>>()
    val nonGroupMember: LiveData<Result<List<GroupMemberResponse.GroupMemberResponseItem.User>>> =
        _nonGroupMember


    private val _createGroupResult = MutableLiveData<Result<Group>>()
    val createGroupResult: LiveData<Result<Group>> = _createGroupResult

    private val _addMemberResult = MutableLiveData<Result<ModelCreateMemebers>>()
    val addMemberResult: LiveData<Result<ModelCreateMemebers>> = _addMemberResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val loginRequest = mapOf(
                    "email" to email,
                    "password" to password
                )
                val response = repository.login(loginRequest)
                _loginResult.value = Result.success(response)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun signUp(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val signUpRequest = mapOf(
                    "full_name" to fullName,
                    "email" to email,
                    "password" to password
                )
                val response = repository.signUp(signUpRequest)
                _signUpResult.value = Result.success(response)
            } catch (e: Exception) {
                _signUpResult.value = Result.failure(e)
            }
        }
    }

    fun getGroups(token: String) {
        viewModelScope.launch {
            try {
                val groupsList = repository.getGroups(token)
                _groups.value = Result.success(groupsList)
            } catch (e: Exception) {
                _groups.value = Result.failure(e)
            }
        }
    }

    fun getGroupMessages(token: String, groupId: String) {
        viewModelScope.launch {
            try {
                val messagesList = repository.getGroupMessages(token, groupId)
                _messages.value = Result.success(messagesList)
            } catch (e: Exception) {
                _messages.value = Result.failure(e)
            }
        }
    }

    fun getGroupMember(token: String, groupId: String) {
        viewModelScope.launch {
            try {
                val messagesList = repository.getGroupMember(token, groupId)
                _groupMember.value = Result.success(messagesList)
            } catch (e: Exception) {
                _groupMember.value = Result.failure(e)
            }
        }
    }

    fun getNonGroupMember(token: String, groupId: String) {
        viewModelScope.launch {
            try {
                val messagesList = repository.getNonGroupMember(token, groupId)
                _nonGroupMember.value = Result.success(messagesList)
            } catch (e: Exception) {
                _nonGroupMember.value = Result.failure(e)
            }
        }
    }

    fun addMessage(newMessage: Message) {
        viewModelScope.launch {
            val currentMessages = _messages.value
            if (currentMessages != null) {
                currentMessages.fold(
                    onSuccess = { messageList ->
                        val updatedList = messageList.toMutableList()
                        updatedList.add(newMessage)
                        _messages.value = Result.success(updatedList)
                    },
                    onFailure = {
                        // If the current value is a failure, we'll create a new list with just the new message
                        _messages.value = Result.success(listOf(newMessage))
                    }
                )
            } else {
                // If there's no current value, we'll create a new list with just the new message
                _messages.value = Result.success(listOf(newMessage))
            }
        }
    }

    fun addMessage1on1(newMessage: LastMessage) {
        viewModelScope.launch {
            val currentMessages = _getOntoOneChatMessage.value
            if (currentMessages != null) {
                currentMessages.fold(
                    onSuccess = { messageList ->
                        val updatedList = messageList.toMutableList()
                        updatedList.add(newMessage)
                        _getOntoOneChatMessage.value = Result.success(updatedList)
                    },
                    onFailure = {
                        // If the current value is a failure, we'll create a new list with just the new message
                        _getOntoOneChatMessage.value = Result.success(listOf(newMessage))
                    }
                )
            } else {
                // If there's no current value, we'll create a new list with just the new message
                _getOntoOneChatMessage.value = Result.success(listOf(newMessage))
            }
        }
    }

    fun createGroup(token: String, name: String, description: String) {
        viewModelScope.launch {
            try {
                val createGroupRequest = mapOf(
                    "name" to name,
                    "description" to description
                )
                val createdGroup = repository.createGroup(token, createGroupRequest)
                _createGroupResult.value = Result.success(createdGroup)
            } catch (e: Exception) {
                _createGroupResult.value = Result.failure(e)
            }
        }
    }

    fun addGroupMember(token: String, userIds: List<String>, groupId: String) {
        viewModelScope.launch {
            try {
                val addMemberRequest = AddMemberRequest(users = userIds, group = groupId)
                val addedMember = repository.addGroupMember(token, addMemberRequest)
                _addMemberResult.value = Result.success(addedMember)
            } catch (e: Exception) {
                _addMemberResult.value = Result.failure(e)
            }
        }
    }

    private val _listAllUser = MutableLiveData<Result<List<UserModel>>>()
    val listAllUser: LiveData<Result<List<UserModel>>> = _listAllUser

    fun getAllUser(token: String, userIds: List<String>, groupId: String) {
        viewModelScope.launch {
            try {
                val addedMember = repository.getAllUser(token)
                _listAllUser.value = Result.success(addedMember)
            } catch (e: Exception) {
                _listAllUser.value = Result.failure(e)
            }
        }
    }

    private val _oneToOneChat = MutableLiveData<Result<List<OnetoOneChatList>>>()
    val oneToOneChat: LiveData<Result<List<OnetoOneChatList>>> = _oneToOneChat

    fun getOnetoOneChat(token: String) {
        viewModelScope.launch {
            try {
                val chatList = repository.getOnetoOneChat(token)
                _oneToOneChat.value = Result.success(chatList)
            } catch (e: Exception) {
                _oneToOneChat.value = Result.failure(e)
            }
        }
    }

    private val _createChat = MutableLiveData<Result<CreateChatResponse>>()
    val createChat: LiveData<Result<CreateChatResponse>> = _createChat

    fun createChat(token: String, chatId: String) {
        viewModelScope.launch {
            try {
                val createChatRequest = mapOf(
                    "user2_id" to chatId,
                )
                val addedMember = repository.createChat(token, createChatRequest)
                _createChat.value = Result.success(addedMember)
            } catch (e: Exception) {
                _createChat.value = Result.failure(e)
            }
        }
    }

    private val _getOntoOneChatMessage = MutableLiveData<Result<List<LastMessage>>>()
    val getOntoOneChatMessage: LiveData<Result<List<LastMessage>>> = _getOntoOneChatMessage

    fun getOntoOneChatMessage(token: String, chatId: String) {
        viewModelScope.launch {
            try {
                val addedMember = repository.getOntoOneChatMessage(token, chatId)
                _getOntoOneChatMessage.value = Result.success(addedMember)
            } catch (e: Exception) {
                _getOntoOneChatMessage.value = Result.failure(e)
            }
        }
    }

    private val _updateOnlineStatus = MutableLiveData<Result<UpdateUserModel>>()
    val updateOnlineStatus: LiveData<Result<UpdateUserModel>> = _updateOnlineStatus

    fun updateUserModel(token: String, chatId: String, isOnline: Boolean) {
        viewModelScope.launch {
            try {
                val map = mapOf(
                    "is_online" to isOnline,
                )
                val request = repository.updateOnlineStatus(token, chatId, map)
                _updateOnlineStatus.value = Result.success(request)
            } catch (e: Exception) {
                _updateOnlineStatus.value = Result.failure(e)
            }
        }
    }

    fun markAsRead(token: String, chatIds: List<String>) {
        viewModelScope.launch {
            try {
                val request = repository.markAsRead(token, MarkAsReadRequest(chatIds))
                Result.success(request)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
