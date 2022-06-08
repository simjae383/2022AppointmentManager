package com.sim981.a2022appointmentmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.PlaceRecyclerAdapter
import com.sim981.a2022appointmentmanager.databinding.ActivityEditMyPlaceBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMyPlaceActivity : BaseActivity() {
    lateinit var binding : ActivityEditMyPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_my_place)
        titleTxt.text = "장소 추가하기"
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