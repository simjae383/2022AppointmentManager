package com.sim981.a2022appointmentmanager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sim981.a2022appointmentmanager.fragments.AppointmentsFragment
import com.sim981.a2022appointmentmanager.fragments.FriendsFragment
import com.sim981.a2022appointmentmanager.fragments.PlacesFragment
import com.sim981.a2022appointmentmanager.fragments.SettingsFragment

class MainViewPagerAdapter(fa : FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return  4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> AppointmentsFragment()
            1 -> FriendsFragment()
            2 -> PlacesFragment()
            else -> SettingsFragment()
        }
    }
}