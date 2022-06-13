package com.sim981.a2022appointmentmanager.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityAppointmentDetailBinding
import com.sim981.a2022appointmentmanager.models.AppointmentData
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.utils.GlobalData
import com.sim981.a2022appointmentmanager.utils.SizeUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import kotlin.math.pow
import kotlin.math.sqrt

class AppointmentDetailActivity : BaseActivity() {
    lateinit var binding: ActivityAppointmentDetailBinding

    //    리싸이클러뷰에서 아이템 선택시 해당 값을 처음 받아오는 약속 데이터
    lateinit var receivedApponintment: AppointmentData

    //    약속 수정 완료시 그 값을 받아오는 약속 데이터
    lateinit var apponintmentDetail: AppointmentData

    //    약속 값을 저장해 약속 검색 API에 넘겨줄 Id값
    var requestId = 0

    var mNaverMap: NaverMap? = null

//    들어온 좌표정보
    var coord: LatLng? = null
    var startPosition: LatLng? = null
    var endPosition: LatLng? = null

    var zoomValue = 0.0

    var mStartPlaceMarker = Marker()
    var mEndPlaceMarker = Marker()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_detail)
        receivedApponintment = intent.getSerializableExtra("appointmentPackage") as AppointmentData
        requestId = receivedApponintment.id
        addAppointmentBtn.visibility = View.GONE
        setupEvents()
        setValues()

    }

    override fun onResume() {
        super.onResume()
        firstDetailValues()
        getRequestAppointmentDetail()
    }

    override fun setupEvents() {
//        도착 장소 이름을 누르면 해당 장소의 정보 열람
        binding.detailTargetPlaceTxt.setOnClickListener {
            val myIntent = Intent(mContext, PlaceDetailActivity::class.java)
            myIntent.putExtra("myPlaceName", "도착 장소")
                .putExtra("myPlaceLatitude", receivedApponintment.latitude)
                .putExtra("myPlaceLongitude", receivedApponintment.longitude)
                .putExtra("myPlaceIsDeletableOk", false)
            startActivity(myIntent)
        }
//        약속 수정 버튼
        binding.detailEditBtn.setOnClickListener {
            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            myIntent.putExtra("currentAppointmentData", apponintmentDetail)
                .putExtra("isEditData", true)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.detailAppointmentMap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.detailAppointmentMap, it).commit()
            }
        mapFragment.getMapAsync {
            mNaverMap = it

            var cameraUpdate = CameraUpdate.scrollTo(coord!!)
            it.moveCamera(cameraUpdate)
            //시작 지점, 끝지점의 비교 연산자 확인
            var subLatitude = 0.0
            var subLongitude = 0.0
//
//            if(startPosition!!.latitude > endPosition!!.latitude){
//                subLatitude = startPosition!!.latitude - endPosition!!.latitude
//            } else {
//                subLatitude = endPosition!!.latitude - startPosition!!.latitude
//            }
//
//            if(startPosition!!.longitude > endPosition!!.longitude){
//                subLongitude = startPosition!!.longitude - endPosition!!.longitude
//            } else {
//                subLongitude = endPosition!!.longitude - startPosition!!.longitude
//            }
//
//            zoomValue = (sqrt(subLatitude.pow(2) + subLongitude.pow(2))*100)/10
//            Log.d("값", zoomValue.toString())
//
//            cameraUpdate = CameraUpdate.zoomBy(zoomValue)

            mNaverMap!!.moveCamera(cameraUpdate)

            it.moveCamera(cameraUpdate)

            mStartPlaceMarker.position = startPosition!!
            mStartPlaceMarker.map = mNaverMap

            mEndPlaceMarker.position = endPosition!!
            mEndPlaceMarker.map = mNaverMap
            mEndPlaceMarker.icon =
                OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_red)


        }
    }

    //      약속 세부정보 검색 API
    fun getRequestAppointmentDetail() {
        apiList.getRequestAppointmentDetail(requestId).enqueue(object : Callback<BasicResponse> {
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    apponintmentDetail = response.body()!!.data.appointment
                    Log.d("수정 확인", apponintmentDetail.toString())
                    newDetailValues()
                }
            }
        })
    }

    //    상세 정보 처음 로드시 넣어줄 데이터
    fun firstDetailValues() {
        titleTxt.text = receivedApponintment.title
        val sdf = SimpleDateFormat("M/d a h:mm")
        binding.detailDateTimeTxt.text = "${sdf.format(receivedApponintment.datetime)}"
        binding.detailTargetPlaceTxt.text = receivedApponintment.place
        if (receivedApponintment.user.nickName == GlobalData.loginUser!!.nickName) {
            binding.detailEditBtn.isEnabled = true
        }
        startPosition =
            LatLng(receivedApponintment.startLatitude, receivedApponintment.startLongitude)
        Log.d("시작 좌표", startPosition.toString())
        endPosition = LatLng(receivedApponintment.latitude, receivedApponintment.longitude)
        Log.d("끝 좌표", endPosition.toString())
        coord = LatLng(
            (startPosition!!.latitude + endPosition!!.latitude) / 2,
            (startPosition!!.longitude + endPosition!!.longitude) / 2
        )
        Log.d("중간 좌표", coord.toString())
        Log.d("값", zoomValue.toString())
        Log.d("좌표", "-")
    }

    //    약속 수정 완료 이후 새로 넣어줄 데이터
    fun newDetailValues() {
        titleTxt.text = apponintmentDetail.title
        val sdf = SimpleDateFormat("M/d a h:mm")
        binding.detailDateTimeTxt.text = "${sdf.format(apponintmentDetail.datetime)}"
        binding.detailTargetPlaceTxt.text = apponintmentDetail.place
        binding.detailfriendsListLayout.removeAllViewsInLayout()
        for (friends in apponintmentDetail.invitedFriends) {
            val textView = TextView(mContext)
            textView.setBackgroundResource(R.drawable.lightgray_rectangle_r6)

            textView.setPadding(SizeUtil.dpToPx(mContext, 5f).toInt())
            textView.text = friends.nickName
            if (friends.nickName == apponintmentDetail.user.nickName) {
                textView.setTypeface(null, Typeface.BOLD)
                textView.setTextSize(18F)
            }
            binding.detailfriendsListLayout.visibility = View.VISIBLE
            binding.detailfriendsListLayout.addView(textView)
        }
        startPosition = LatLng(apponintmentDetail.startLatitude, apponintmentDetail.startLongitude)
        endPosition = LatLng(apponintmentDetail.latitude, apponintmentDetail.longitude)
        coord = LatLng(
            (startPosition!!.latitude + endPosition!!.latitude) / 2,
            (startPosition!!.longitude + endPosition!!.longitude) / 2
        )
    }
}