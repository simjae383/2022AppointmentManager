package com.sim981.a2022appointmentmanager

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}