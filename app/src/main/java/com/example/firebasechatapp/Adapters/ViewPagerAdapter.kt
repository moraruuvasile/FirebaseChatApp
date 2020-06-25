package com.example.firebasechatapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.firebasechatapp.Fragments.ChatsFragment
import com.example.firebasechatapp.Fragments.SearchFragment
import com.example.firebasechatapp.Fragments.SettingsFragment

class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return ChatsFragment()
            2 -> return SearchFragment()
            else -> return SettingsFragment()
        }
    }

}