package com.sim981.a2022appointmentmanager.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityMyLocationBinding

class MyLocationActivity : BaseActivity() {
    lateinit var binding: ActivityMyLocationBinding

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationManager: LocationManager
    var latitude: Double = 0.0
    var longitude: Double = 0.0

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
        binding.locationBtn.setOnClickListener {
            val pl = object : PermissionListener {
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }

                override fun onPermissionGranted() {
//                지도 표시
                    requestNewLocationData()
                }
            }
            TedPermission.create().setPermissionListener(pl)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .setDeniedMessage("[설정] > [권한]에서 좌표 권한을 열어주세요.")
                .check()
        }
    }

    override fun setValues() {
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData(){
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }
    private val mLocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation : Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            showMapView()
        }
    }

    fun showMapView(){
        binding.currentLatLngTxt.text = "위도 : ${latitude}, 경도 : $longitude"
    }
}