package com.sim981.a2022appointmentmanager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed({
            val myIntent: Intent

            myIntent = Intent(mContext, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        }, 2500)
    }
}