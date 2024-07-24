package com.dis.ornetcalling.ui.activity

import com.dis.ornetcalling.architecture.MainViewModel
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dis.ornetcalling.ui.adapter.MessagesAdapter
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.databinding.ActivityGroupMessagesBinding
import com.dis.ornetcalling.socket.GroupChatWebSocket
import com.dis.ornetcalling.ui.fragment.AddMembersBottomSheet
import com.dis.ornetcalling.ui.fragment.GroupMembersBottomSheet

class GroupMessagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupMessagesBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var chatWebSocket: GroupChatWebSocket

    private var groupId: String = "0"
    private lateinit var groupName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        groupId = intent.getStringExtra("groupId") ?: ""
        groupName = intent.getStringExtra("groupName") ?: "Group"

        binding.tvGroupName.text = groupName
        chatWebSocket = GroupChatWebSocket(groupId, SessionManager(this).getUserToken() ?: "", viewModel)
        chatWebSocket.connect()

        if (groupId.isEmpty()) {
            Toast.makeText(this, "Invalid group ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        binding.tvGroupName.setOnClickListener {
            showGroupMembersBottomSheet()
        }
        binding.btnAddMember.setOnClickListener {
            val bottomSheet = AddMembersBottomSheet(groupId)
            bottomSheet.show(supportFragmentManager, "GroupMembersBottomSheet")        }
        binding.toolbar.setOnClickListener {
            showGroupMembersBottomSheet()
        }
        binding.btnSendMessage.setOnClickListener {
            sendMessage()
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        supportActionBar?.title = groupName
        setupRecyclerView()
        loadMessages()
        observeViewModel()
    }

    private fun showGroupMembersBottomSheet() {
        val bottomSheet = GroupMembersBottomSheet(groupId)
        bottomSheet.show(supportFragmentManager, "GroupMembersBottomSheet")
    }

    private fun setupRecyclerView() {
        messagesAdapter = MessagesAdapter(emptyList(), SessionManager(this).getUserEmail() ?: "")
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@GroupMessagesActivity).apply {
                stackFromEnd = true
            }
            adapter = messagesAdapter
        }
    }

    private fun loadMessages() {
        showLoading(true)
        val token = getToken()
        viewModel.getGroupMessages(token, groupId)
    }

    private fun observeViewModel() {
        viewModel.messages.observe(this) { result ->
            showLoading(false)
            result.onSuccess { messages ->
                messagesAdapter.updateMessages(messages)
                binding.rvMessages.scrollToPosition(messages.size - 1)
            }.onFailure { error ->
                Toast.makeText(
                    this,
                    "Failed to load messages: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendMessage() {
        val messageContent = binding.etMessage.text.toString().trim()
        if (messageContent.isNotEmpty()) {
            val token = getToken()
            //  viewModel.sendMessage(token, groupId, messageContent)
            chatWebSocket.sendMessage(messageContent)
            binding.etMessage.text.clear()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvMessages.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun getToken(): String {
        val token = SessionManager(this).getUserToken()
        return token ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        chatWebSocket.disconnect()
    }
}