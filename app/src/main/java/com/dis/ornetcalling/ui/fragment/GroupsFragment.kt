package com.dis.ornetcalling.ui.fragment

import com.dis.ornetcalling.architecture.MainViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dis.ornetcalling.databinding.ActivityGroupsBinding
import com.dis.ornetcalling.ui.adapter.GroupsAdapter
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.ui.activity.GroupMessagesActivity

class GroupsFragment : Fragment() {
    private lateinit var binding: ActivityGroupsBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var groupsAdapter: GroupsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupRecyclerView()

        binding.btnCreateGroup.setOnClickListener {
            val createGroupBottomSheet = CreateGroupBottomSheetFragment()
            createGroupBottomSheet.show(parentFragmentManager, "CreateGroupBottomSheet")
        }

        loadGroups()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        groupsAdapter = GroupsAdapter(emptyList()) { group ->
            val intent = Intent(requireContext(), GroupMessagesActivity::class.java).apply {
                putExtra("groupId", group.id)
                putExtra("groupName", group.name)
            }
            startActivity(intent)
        }
        binding.rvGroups.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupsAdapter
        }
    }

    private fun loadGroups() {
        showLoading(true)
        val token = getToken()
        viewModel.getGroups(token)
    }

    private fun observeViewModel() {
        viewModel.groups.observe(viewLifecycleOwner) { result ->
            showLoading(false)
            result.onSuccess { groups ->
                groupsAdapter.updateGroups(groups)
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Failed to load groups: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvGroups.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun getToken(): String {
        val token = SessionManager(requireContext()).getUserToken()
        return token ?: ""
    }
}
