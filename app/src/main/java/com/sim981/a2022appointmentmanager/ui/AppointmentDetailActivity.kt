package com.sim981.a2022appointmentmanager.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityAppointmentDetailBinding
import com.sim981.a2022appointmentmanager.models.AppointmentData
import com.sim981.a2022appointmentmanager.utils.SizeUtil
import java.text.SimpleDateFormat

class AppointmentDetailActivity : BaseActivity() {
    lateinit var binding : ActivityAppointmentDetailBinding

    lateinit var appointmentDetail : AppointmentData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_detail)
        appointmentDetail = intent.getSerializableExtra("AppointmentData") as AppointmentData
        titleTxt.text = appointmentDetail.title
        addAppointmentBtn.visibility = View.GONE
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.detailTargetPlaceTxt.setOnClickListener {
            val myIntent = Intent(mContext, PlaceDetailActivity::class.java)
            myIntent.putExtra("myPlaceName", "도착 장소").putExtra("myPlaceLatitude", appointmentDetail.latitude)
                .putExtra("myPlaceLongitude", appointmentDetail.longitude).putExtra("myPlaceIsDeletableOk", false)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        val sdf = SimpleDateFormat("M/d a h:mm")
        binding.detailDateTimeTxt.text = "${sdf.format(appointmentDetail.datetime)}"
        binding.detailTargetPlaceTxt.text = appointmentDetail.place

        for(friends in appointmentDetail.invitedFriends){
            val textView = TextView(mContext)
            textView.setBackgroundResource(R.drawable.lightgray_rectangle_r6)

            textView.setPadding(SizeUtil.dpToPx(mContext, 5f).toInt())
            textView.text = friends.nickName
            if(friends.nickName == appointmentDetail.user.nickName){
                textView.setTypeface(null, Typeface.BOLD)
                textView.setTextSize(18F)
            }
            binding.detailfriendsListLayout.visibility = View.VISIBLE
            binding.detailfriendsListLayout.addView(textView)

        }
    }
}