package com.sim981.a2022appointmentmanager.ui

import android.Manifest
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle

import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
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
        val pl = object : PermissionListener{
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                finish()
            }

            override fun onPermissionGranted() {
//                지도 표시
            }
        }
        TedPermission.create().setPermissionListener(pl)
            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .setDeniedMessage("[설정] > [권한]에서 갤러리 권한을 열어주세요.")
            .check()
    }

    override fun setValues() {
    }
}