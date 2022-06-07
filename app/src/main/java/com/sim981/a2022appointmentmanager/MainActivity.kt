package com.sim981.a2022appointmentmanager

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.sim981.a2022appointmentmanager.adapters.MainViewPagerAdapter
import com.sim981.a2022appointmentmanager.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    lateinit var binding : ActivityMainBinding

    lateinit var mPagerAdapter : MainViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        mPagerAdapter = MainViewPagerAdapter(this)
        binding.mainViewPager.adapter = mPagerAdapter

        binding.mainViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.mainBottomNav.menu.getItem(position).isChecked = true
                }
            }
        )
        binding.mainBottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.Appointments -> binding.mainViewPager.currentItem = 0
                R.id.Friends -> binding.mainViewPager.currentItem = 1
                R.id.Places -> binding.mainViewPager.currentItem = 2
                R.id.Settings -> binding.mainViewPager.currentItem = 3
            }
            return@setOnItemSelectedListener true
        }
    }
}