package com.dis.ornetcalling.ui.activity

// noinspection SuspiciousImport
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dis.ornetcalling.R
import com.dis.ornetcalling.architecture.MainViewModel
import com.dis.ornetcalling.databinding.ActivityMainBinding
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.ui.fragment.ChatFragment
import com.dis.ornetcalling.ui.fragment.GroupsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm :MainViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Chat"
                    tab.setIcon(R.drawable.baseline_chat_24)
                }
                1 -> {
                    tab.text = "Group Chat"
                    tab.setIcon(R.drawable.baseline_group_24)
                }
            }
        }.attach()

        vm.updateUserModel(sessionManager.getUserToken()?:"",sessionManager.getUserId()?:"",true)
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.updateUserModel(sessionManager.getUserToken()?:"",sessionManager.getUserId()?:"",false)

    }
}

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatFragment()
            1 -> GroupsFragment()
            else -> ChatFragment()
        }
    }
}

