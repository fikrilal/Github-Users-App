package com.fikrilal.githubuserapps.data.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fikrilal.githubuserapps.ui.UserListFragment

class TabsAdapter(fragment: Fragment, private val username: String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = UserListFragment.newInstance()
        fragment.arguments = Bundle().apply {
            putInt("section_number", position)
            putString("username", username)
        }
        return fragment
    }
}