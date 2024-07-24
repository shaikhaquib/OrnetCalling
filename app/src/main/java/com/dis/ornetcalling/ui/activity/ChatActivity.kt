package com.dis.ornetcalling.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dis.ornetcalling.architecture.MainViewModel
import com.dis.ornetcalling.databinding.ActivityChatBinding
import com.dis.ornetcalling.network.model.LastMessage
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.socket.ChatWebSocket
import com.dis.ornetcalling.socket.GroupChatWebSocket
import com.dis.ornetcalling.ui.adapter.ChatAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatWebSocket: ChatWebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSendButton()
        observeMessages()

        // Get chat ID from intent and load messages
        val chatId = intent.getStringExtra("chat_id") ?: return
        val chatName = intent.getStringExtra("chatName") ?: "Unknown"

        chatWebSocket =
            ChatWebSocket(chatId, SessionManager(this).getUserToken() ?: "", viewModel)
        chatWebSocket.connect()
        binding.tvGroupName.text = chatName
        viewModel.getOntoOneChatMessage(getToken(), chatId)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSendMessage.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(getCurrentUserId())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupSendButton() {
        binding.btnSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                // Send message logic here
                binding.etMessage.text?.clear()
            }
        }
    }

    private fun observeMessages() {
        viewModel.getOntoOneChatMessage.observe(this) { result ->
            result.onSuccess {
                chatAdapter.submitList(result.getOrThrow())
                binding.recyclerView.scrollToPosition(result.getOrThrow().size - 1)
                    viewModel.markAsRead(getToken(),extractIds(result.getOrThrow(),SessionManager(applicationContext).getUserId()?:""))
            }
            result.onFailure {
                // Handle error
                Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getCurrentUserId(): String {
        return SessionManager(this).getUserId() ?: ""
    }

    private fun getToken(): String {
        return SessionManager(this).getUserToken() ?: ""
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

    override fun onDestroy() {
        super.onDestroy()
        chatWebSocket.disconnect()
    }

    fun extractIds(lastMessages: List<LastMessage>, currentUserId: String): List<String> {
        return lastMessages
            .filter { it.sender.id != currentUserId }
            .map { it.id }
    }
}