package com.sim981.a2022appointmentmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityMyLocationBinding

class MyLocationActivity : BaseActivity() {
    lateinit var binding : ActivityMyLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_location)
        titleTxt.text = "현재 나의 위치"
        addAppointmentBtn.visibility = View.GONE
        setupEvents()
        setValues()
    }
    override fun onResume() {
        super.onResume()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
    }
}