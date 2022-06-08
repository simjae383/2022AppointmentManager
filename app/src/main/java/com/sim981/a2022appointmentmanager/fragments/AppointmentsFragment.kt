package com.sim981.a2022appointmentmanager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.FragmentAppointmentsBinding
import com.sim981.a2022appointmentmanager.ui.EditAppointmentActivity
import com.sim981.a2022appointmentmanager.ui.MainActivity

class AppointmentsFragment : BaseFragment() {
    lateinit var binding : FragmentAppointmentsBinding

    lateinit var addBtn : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointments, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBtn = (mContext as MainActivity).addAppointmentBtn
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
        addBtn.visibility = View.VISIBLE

        addBtn.setImageResource(R.drawable.baseline_add_black_24dp)
    }

    override fun onPause() {
        super.onPause()
        addBtn.visibility = View.GONE
    }
    override fun setupEvents() {

        addBtn.setOnClickListener {
            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {

    }
}