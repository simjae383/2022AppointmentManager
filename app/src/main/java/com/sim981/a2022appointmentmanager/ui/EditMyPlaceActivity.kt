package com.sim981.a2022appointmentmanager.ui

import android.os.Bundle
import android.util.Log
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

    var mSelectedLatitude = 37.5779235853308
    var mSelectedLongitude = 127.033553463647

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
        binding.placeSaveBtn.setOnClickListener {
            val inputName = binding.placeTitleEdt.text.toString()
            if (inputName.isBlank()) {
                Toast.makeText(mContext, "약속명을 기입해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.postRequestAddMyPlace(inputName, mSelectedLatitude, mSelectedLatitude, false)
                .enqueue(object : Callback<BasicResponse>{
                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(mContext, "장소가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else {
                            val errorBodyStr = response.errorBody()!!.string()
                            val jsonObj = JSONObject(errorBodyStr)
                            val code = jsonObj.getInt("code")
                            val message = jsonObj.getString("message")

                            if (code == 400) {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }else {
                                Log.e("장소 추가 서버 에러 ", message)
                            }
                        }
                    }
                })
        }
    }

    override fun setValues() {
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