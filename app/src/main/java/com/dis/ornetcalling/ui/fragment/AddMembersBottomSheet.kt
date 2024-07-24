package com.dis.ornetcalling.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dis.ornetcalling.architecture.MainViewModel
import com.dis.ornetcalling.databinding.BottomSheetAddMembersBinding
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.ui.adapter.MemberAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddMembersBottomSheet(val groupId: String) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddMembersBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MemberAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddMembersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        groupId = arguments?.getString("groupId") ?: ""
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Fetch non-group members
        viewModel.getNonGroupMember(getToken(), groupId)
    }

    private fun setupRecyclerView() {
        adapter = MemberAdapter()
        binding.rvMembers.adapter = adapter
        binding.rvMembers.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        viewModel.nonGroupMember.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                adapter.submitList(result.getOrThrow())
            }
            result.onFailure {
                // Handle error
                Toast.makeText(requireContext(), "Failed to fetch members", Toast.LENGTH_SHORT)
                    .show()
            }

            viewModel.addMemberResult.observe(viewLifecycleOwner) { result ->
                result.onSuccess {
                    Toast.makeText(
                        requireContext(),
                        "Members added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
                }
                result.onFailure {
                    // Handle error
                    Toast.makeText(requireContext(), "Failed to add members", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener {
            val selectedUserIds = adapter.getSelectedUserIds()
            if (selectedUserIds.isNotEmpty()) {
                viewModel.addGroupMember(getToken(), selectedUserIds, groupId)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please select at least one member",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getToken(): String {
        return SessionManager(requireContext()).getUserToken()?:""
    }
}