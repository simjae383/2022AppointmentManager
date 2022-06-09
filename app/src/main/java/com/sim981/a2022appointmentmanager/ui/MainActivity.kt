package com.sim981.a2022appointmentmanager.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.sim981.a2022appointmentmanager.R
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
//                    titleTxt.text = when (position) {
//                        0 -> "약속 확인하기"
//                        1 -> "친구 관리"
//                        2 -> "장소 관리"
//                        else -> "설정"
//                    }
                    when (position) {
                        0 -> { titleTxt.text = "약속 확인하기"
                            addAppointmentBtn.visibility = View.VISIBLE
                            addPlaceBtn.visibility = View.GONE
                            addFriendBtn.visibility = View.GONE
                            requestFriendBtn.visibility = View.GONE
                            myLocationBtn.visibility = View.GONE
                        }
                        1 -> {
                            titleTxt.text = "친구 관리"
                            addAppointmentBtn.visibility = View.GONE
                            addPlaceBtn.visibility = View.GONE
                            addFriendBtn.visibility = View.VISIBLE
                            requestFriendBtn.visibility = View.VISIBLE
                            myLocationBtn.visibility = View.GONE
                        }
                        2 -> {
                            titleTxt.text = "장소 관리"
                            addAppointmentBtn.visibility = View.GONE
                            addPlaceBtn.visibility = View.VISIBLE
                            addFriendBtn.visibility = View.GONE
                            requestFriendBtn.visibility = View.GONE
                            myLocationBtn.visibility = View.VISIBLE
                        }
                        else -> {
                            titleTxt.text = "설정"
                            addAppointmentBtn.visibility = View.GONE
                            addPlaceBtn.visibility = View.GONE
                            addFriendBtn.visibility = View.GONE
                            requestFriendBtn.visibility = View.GONE
                            myLocationBtn.visibility = View.GONE
                        }
                    }
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