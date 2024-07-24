package com.dis.ornetcalling.ui.fragment

import com.dis.ornetcalling.architecture.MainViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.databinding.BottomSheetCreateGroupBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateGroupBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCreateGroupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        observeViewModel()
        binding.btnCreateGroup.setOnClickListener {
            val groupName = binding.etGroupName.text.toString().trim()
            if (groupName.isNotEmpty()) {
                viewModel.createGroup(sessionManager.getUserToken() ?: "", groupName, "")
            } else {
                binding.etGroupName.error = "Group name cannot be empty"
            }
        }
    }

    private fun observeViewModel() {
        viewModel.createGroupResult.observe(this) { result ->
            //  showLoading(false)
            result.onSuccess { res ->
                viewModel.getGroups(sessionManager.getUserToken() ?: "")
                dismiss()
            }.onFailure { error ->
                Toast.makeText(activity, "failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CreateGroupBottomSheetFragment {
            return CreateGroupBottomSheetFragment()
        }
    }
}