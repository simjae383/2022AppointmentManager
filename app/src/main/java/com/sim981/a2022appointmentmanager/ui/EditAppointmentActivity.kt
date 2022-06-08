package com.sim981.a2022appointmentmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityEditAppointmentBinding

class EditAppointmentActivity : BaseActivity() {
    lateinit var binding : ActivityEditAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}