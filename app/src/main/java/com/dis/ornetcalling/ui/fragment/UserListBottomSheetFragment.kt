package com.dis.ornetcalling.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dis.ornetcalling.architecture.MainViewModel
import com.dis.ornetcalling.databinding.BottomSheetUserListBinding
import com.dis.ornetcalling.network.model.UserModel
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.ui.adapter.UserAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserListBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetUserListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var userAdapter: UserAdapter

    interface UserSelectionListener {
        fun onUserSelected(user: UserModel)
    }

    private var userSelectionListener: UserSelectionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        observeViewModel()
        loadUsers()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList()) { user ->
            userSelectionListener?.onUserSelected(user)
            dismiss()
        }
        binding.userRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.userRecyclerView.adapter = userAdapter
    }

    private fun observeViewModel() {
        viewModel.listAllUser.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess { users ->
                if (users.isNullOrEmpty()) {
                    binding.userRecyclerView.visibility = View.GONE
                    Toast.makeText(requireContext(), "No users found", Toast.LENGTH_SHORT).show()
                } else {
                    binding.userRecyclerView.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed(
                        { userAdapter.setUsers(users) },
                        100
                    )

                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed to load users", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener { text ->
            userAdapter.filter(text.toString())
        }
    }

    private fun loadUsers() {
        binding.progressBar.visibility = View.VISIBLE
        binding.userRecyclerView.visibility = View.GONE
        val token = SessionManager(requireContext()).getUserToken() ?: ""
        viewModel.getAllUser(token, emptyList(), "")
    }

    fun setUserSelectionListener(listener: UserSelectionListener) {
        userSelectionListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
