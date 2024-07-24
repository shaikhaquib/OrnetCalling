package com.dis.ornetcalling.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dis.ornetcalling.architecture.MainViewModel
import com.dis.ornetcalling.databinding.FragmentChatBinding
import com.dis.ornetcalling.network.model.UserModel
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.ui.activity.ChatActivity
import com.dis.ornetcalling.ui.adapter.ChatListAdapter

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var chatAdapter: ChatListAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChat()
        setupRecyclerView()
        observeChatList()
        setupAddChatButton()
        startRepeatingTaskWithCoroutines()
    }

    // Method 2: Using Coroutines
    private fun startRepeatingTaskWithCoroutines() {

        runnable = object : Runnable {
            override fun run() {
                Log.d("ChatFragment", "Fetching one-to-one chat")
                viewModel.getOnetoOneChat(SessionManager(requireContext()).getUserToken() ?: "")
                runnable?.let { handler.postDelayed(it, 20000) }
            }
        }
        runnable?.let { handler.post(it) }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatListAdapter { chatItem ->
            val intent = Intent(requireContext(),ChatActivity::class.java)
            intent.putExtra("chat_id",chatItem.id)
            intent.putExtra("chatName",chatItem.opposite_user.full_name)
           startActivity(intent)
        }
        binding.recyclerViewChats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun observeChatList() {
        viewModel.oneToOneChat.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = false
            result.onSuccess { chatList ->
                Log.d("ChatFragment", "Fetching one-to-one chat Live data")

                chatAdapter.setData(chatList)
                binding.recyclerViewChats.isVisible = true
                binding.textViewNoChatsFallback.isVisible = chatList.isEmpty()
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Failed to load chats: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Load chat list
    }

    private fun setupAddChatButton() {
        binding.fabAddChat.setOnClickListener {
            val userListBottomSheet = UserListBottomSheetFragment()
            userListBottomSheet.setUserSelectionListener(object : UserListBottomSheetFragment.UserSelectionListener {
                override fun onUserSelected(user: UserModel) {
                    viewModel.createChat(SessionManager(requireActivity()).getUserToken() ?: "", user.id)
                }
            })
            userListBottomSheet.show(requireActivity().supportFragmentManager, "UserListBottomSheet")
        }
    }

    private fun observeChat() {
        viewModel.createChat.observe(viewLifecycleOwner) { result ->

            result.onSuccess { res ->
                viewModel.getOnetoOneChat(SessionManager(requireContext()).getUserToken() ?: "")
            }.onFailure { error ->
                Toast.makeText(context, "Failed to load group members", Toast.LENGTH_SHORT).show()
               // dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runnable?.let { handler.removeCallbacks(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOnetoOneChat(SessionManager(requireContext()).getUserToken() ?: "")
    }
}