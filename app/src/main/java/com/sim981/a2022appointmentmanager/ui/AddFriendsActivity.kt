package com.sim981.a2022appointmentmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityAddFriendsBinding

class AddFriendsActivity : BaseActivity() {
    lateinit var binding : ActivityAddFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friends)
        titleTxt.text = "친구 추가 요청하기"
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}