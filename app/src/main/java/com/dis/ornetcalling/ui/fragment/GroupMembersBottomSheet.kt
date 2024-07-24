package com.dis.ornetcalling.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.dis.ornetcalling.R
import com.dis.ornetcalling.architecture.MainViewModel
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.ui.adapter.GroupMemberAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GroupMembersBottomSheet(val groupID:String) : BottomSheetDialogFragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: GroupMemberAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_group_members, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvGroupMembers)
        adapter = GroupMemberAdapter(emptyList())
        recyclerView.adapter = adapter

        observeGroupMembers()
    }

    private fun observeGroupMembers() {
        viewModel.getGroupMember(SessionManager(requireActivity()).getUserToken()?:"",groupID)
        viewModel.groupMember.observe(viewLifecycleOwner) { result ->

            result.onSuccess { res ->
                adapter = GroupMemberAdapter(res)
                view?.findViewById<RecyclerView>(R.id.rvGroupMembers)?.adapter = adapter
            }.onFailure { error ->
                Toast.makeText(context, "Failed to load group members", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }

}