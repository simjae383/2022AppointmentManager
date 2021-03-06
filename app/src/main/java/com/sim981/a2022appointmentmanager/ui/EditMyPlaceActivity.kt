package com.sim981.a2022appointmentmanager.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityEditMyPlaceBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMyPlaceActivity : BaseActivity() {
    lateinit var binding: ActivityEditMyPlaceBinding

    //    기본 선택 위도 경도
    var mSelectedLatitude = 37.5779235853308
    var mSelectedLongitude = 127.033553463647

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_my_place)
        titleTxt.text = "장소 추가하기" //커스텀 액션바에 제목 넣기
        addAppointmentBtn.visibility = View.GONE //다른 기능의 버튼 비활성화
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun setupEvents() {
//        저장하기 버튼
        binding.placeSaveBtn.setOnClickListener {
            val inputName = binding.placeTitleEdt.text.toString()
            if (inputName.isBlank()) {
                Toast.makeText(mContext, "장소명을 기입해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//          장소 이름과 위도 경도, 기본 장소 설정 여부를 체크
            apiList.postRequestAddMyPlace(inputName, mSelectedLatitude, mSelectedLongitude, false)
                .enqueue(object : Callback<BasicResponse> {
                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(mContext, "장소가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val errorBodyStr = response.errorBody()!!.string()
                            val jsonObj = JSONObject(errorBodyStr)
                            val code = jsonObj.getInt("code")
                            val message = jsonObj.getString("message")

                            if (code == 400) {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("장소 추가 서버 에러 ", message)
                            }
                        }
                    }
                })
        }
    }

    override fun setValues() {
//        네이버 지도 설정
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.placeEditmap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.placeEditmap, it).commit()
            }
        mapFragment.getMapAsync {
            val naverMap = it

            val coord = LatLng(37.5779235853308, 127.033553463647)

            val cameraUpdate = CameraUpdate.scrollTo(coord)
            naverMap.moveCamera(cameraUpdate)

            val marker = Marker()
            marker.position = coord
            marker.map = naverMap

            naverMap.setOnMapClickListener { pointF, latLng ->
                marker.position = latLng
                marker.map = naverMap

                mSelectedLatitude = latLng.latitude
                mSelectedLongitude = latLng.longitude
            }
        }
    }
}