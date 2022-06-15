package com.sim981.a2022appointmentmanager.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.api.NaverAPIList
import com.sim981.a2022appointmentmanager.api.NaverMapServerAPI
import com.sim981.a2022appointmentmanager.databinding.ActivityMyLocationBinding
import com.sim981.a2022appointmentmanager.navermodels.GeoResponse
import com.sim981.a2022appointmentmanager.navermodels.ResultData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MyLocationActivity : BaseActivity() {
    lateinit var binding: ActivityMyLocationBinding

    lateinit var mFusedLocationClient: FusedLocationProviderClient
//  현재 위치 좌표
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    lateinit var naverRetrofit: Retrofit
    lateinit var naverApiList: NaverAPIList
//    네이버 맵에 넘겨줄 좌표
    var coord = LatLng(37.5779235853308, 127.033553463647)

    var mNaverMap: NaverMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_location)
        titleTxt.text = "현재 나의 위치"
        addAppointmentBtn.visibility = View.GONE
        naverRetrofit = NaverMapServerAPI.getRetrofit()
        naverApiList = naverRetrofit.create(NaverAPIList::class.java)
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun setupEvents() {
//        현재 위치 확인 버튼, 권한 요청 확인
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
//        네이버 지도 설정
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.currentLocationMap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.placeEditmap, it).commit()
            }
        mapFragment.getMapAsync {
            mNaverMap = it
        }
    }

//    현재 좌표를 찾는 기능
    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {
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

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            coord = LatLng(latitude, longitude)
            val cameraUpdate = CameraUpdate.scrollTo(coord)
            mNaverMap!!.moveCamera(cameraUpdate)
            getCoordToAddress()
        }
    }

//    현재 좌표를 기준으로 네이버 리버스 지오코딩을 하는 메소드
    fun getCoordToAddress() {
        var address = ""

        naverApiList.getRequestMapAddress(
            "${longitude}, ${latitude}",
            "json", "legalcode,admcode,addr,roadaddr"
        ).enqueue(object : Callback<GeoResponse> {
            override fun onFailure(call: Call<GeoResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<GeoResponse>, response: Response<GeoResponse>) {
                if (response.isSuccessful) {

                    var roadaddr: ResultData? = null
                    var addr: ResultData? = null

                    val results = response.body()!!.results
                    if (results.isEmpty()) {
                        address = "출력할 수 없는 지역입니다."
                    } else {
                        for (result in results) {
                            if (result.name == "roadaddr") {
                                roadaddr = result
                            } else if (result.name == "addr") {
                                addr = result
                            }
                        }

                        if (roadaddr == null) {
                            address =
                                "${addr!!.region.area1.name} ${addr!!.region.area2.name} ${addr!!.region.area3.name} ${addr.region.area4.name} 일대"
                        } else if (roadaddr!!.land.number2 == "") {
                            address =
                                "${roadaddr!!.region.area1.name} ${roadaddr!!.region.area2.name} ${roadaddr!!.land.name} ${roadaddr.land.number1}"
                        } else if (roadaddr!!.land.name.isNotBlank()) {
                            address =
                                "${roadaddr!!.region.area1.name} ${roadaddr!!.region.area2.name} ${roadaddr!!.land.name} ${roadaddr.land.number1}-${roadaddr.land.number2}"
                        }
                    }
                    binding.currentLatLngTxt.text = "현재 위치 : ${address}"
                    val marker = Marker()
                    marker.position = coord
                    marker.map = mNaverMap
                    marker.icon =
                        OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_blue)
                } else {
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)
                    val message = jsonObj.getString("message")

                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}