package com.sim981.a2022appointmentmanager.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.api.NaverAPIList
import com.sim981.a2022appointmentmanager.api.NaverMapServerAPI
import com.sim981.a2022appointmentmanager.databinding.ActivityPlaceDetailBinding
import com.sim981.a2022appointmentmanager.dialogs.CustomAlertDialog
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.navermodels.GeoResponse
import com.sim981.a2022appointmentmanager.navermodels.ResultData
import org.json.JSONObject
import retrofit2.*

class PlaceDetailActivity : BaseActivity() {
    lateinit var binding : ActivityPlaceDetailBinding

    var detailId = 0
    var detailName = ""
    var detailStartLatitude = 0.0
    var detailStartLongitude = 0.0
    var detailTargetLatitude = 0.0
    var detailTargetLongitude = 0.0
    var isAppointmentOk = false

    var mNaverMap: NaverMap? = null
    var startMarker = Marker()
    var endMarker = Marker()

    lateinit var naverRetrofit: Retrofit
    lateinit var naverApiList : NaverAPIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
        detailId = intent.getIntExtra("myPlaceId", 0)
        detailName = intent.getStringExtra("myPlaceName").toString()
        detailStartLatitude = intent.getDoubleExtra("myStartLatitude", 0.0)
        detailStartLongitude = intent.getDoubleExtra("myStartLongitude", 0.0)
        detailTargetLatitude = intent.getDoubleExtra("myTargetLatitude", 0.0)
        detailTargetLongitude = intent.getDoubleExtra("myTargetLongitude", 0.0)
        isAppointmentOk = intent.getBooleanExtra("IsThisAppointmentOk", false)
        titleTxt.text = detailName
        addAppointmentBtn.visibility = View.GONE
        naverRetrofit = NaverMapServerAPI.getRetrofit()
        naverApiList = naverRetrofit.create(NaverAPIList::class.java)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        if(isAppointmentOk){
            binding.deletePlaceBtn.visibility = View.VISIBLE
            binding.deletePlaceBtn.setOnClickListener {
                deleteThisPlace()
            }
        }

    }

    override fun setValues() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.detailMap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.detailMap, it).commit()
            }
        mapFragment.getMapAsync {
            mNaverMap = it

            val coord = LatLng(detailStartLatitude, detailStartLongitude)

            var cameraUpdate : CameraUpdate
            startMarker.position = coord
            startMarker.map = mNaverMap

            getCoordToAddress(detailStartLongitude, detailStartLatitude, true)

            if(isAppointmentOk){
                endMarker.position = LatLng(detailTargetLatitude,detailTargetLongitude)
                endMarker.map = mNaverMap
                endMarker.icon =
                    OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_red)
                cameraUpdate = CameraUpdate.scrollTo(LatLng((detailStartLatitude+ detailTargetLatitude)/2,
                    (detailStartLongitude + detailTargetLongitude)/2))
                binding.endAddressTxt.visibility = View.VISIBLE
                getCoordToAddress(detailTargetLongitude, detailTargetLatitude, false)
            } else {
                cameraUpdate = CameraUpdate.scrollTo(coord)
            }

            it.moveCamera(cameraUpdate)
            mNaverMap!!.moveCamera(cameraUpdate)
        }

    }

    fun deleteThisPlace(){
        val alert = CustomAlertDialog(mContext)
        alert.myDialog()

        alert.binding.dialogTitleTxt.text = "장소 삭제"
        alert.binding.dialogBodyTxt.text = "정말 장소 목록에서 삭제하시겠습니까?"
        alert.binding.dialogContentEdt.visibility = View.GONE
        alert.binding.dialogPositiveBtn.setOnClickListener {
            apiList.deleteRequestDeletePlace(detailId).enqueue(object : Callback<BasicResponse> {
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!
                        Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBodyStr = response.errorBody()!!.string()
                        val jsonObj = JSONObject(errorBodyStr)
                        val message = jsonObj.getString("message")

                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
            alert.dialog.dismiss()
        }
        alert.binding.dialogNegativeBtn.setOnClickListener {
            alert.dialog.dismiss()
        }
    }

    fun getCoordToAddress(inputLongitude : Double, inputLatitude : Double, isStartPlaceOk : Boolean){
        var address = ""
        naverApiList.getRequestMapAddress("${inputLongitude}, ${inputLatitude}",
            "json", "legalcode,admcode,addr,roadaddr").enqueue(object : Callback<GeoResponse>{
            override fun onFailure(call: Call<GeoResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<GeoResponse>, response: Response<GeoResponse>) {
                if (response.isSuccessful) {
                    Log.d("주소목록", response.body()!!.results.toString())

                    var roadaddr : ResultData? = null

                    val results = response.body()!!.results

                    for (result in results) {
                        if (result.name == "roadaddr") {
                            roadaddr = result
                        }
                    }
                    address = if (roadaddr!!.land.number2 == "") {
                        "${roadaddr!!.region.area1.name} ${roadaddr!!.region.area2.name} ${roadaddr!!.land.name} ${roadaddr.land.number1}"
                    } else {
                        "${roadaddr!!.region.area1.name} ${roadaddr!!.region.area2.name} ${roadaddr!!.land.name} ${roadaddr.land.number1}-${roadaddr.land.number2}"
                    }
                    if(isStartPlaceOk){
                        binding.startAddressTxt.text = "시작 장소 : ${address}"
                    } else {
                        binding.endAddressTxt.text = "도착 장소 : ${address}"
                    }
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