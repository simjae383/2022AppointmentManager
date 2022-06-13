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
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.api.APIList
import com.sim981.a2022appointmentmanager.api.NaverAPIList
import com.sim981.a2022appointmentmanager.api.NaverGeocodingAPI
import com.sim981.a2022appointmentmanager.databinding.ActivityPlaceDetailBinding
import com.sim981.a2022appointmentmanager.dialogs.CustomAlertDialog
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.PlaceData
import com.sim981.a2022appointmentmanager.navermodels.NaverBasicResponse
import org.json.JSONObject
import retrofit2.*

class PlaceDetailActivity : BaseActivity() {
    lateinit var binding : ActivityPlaceDetailBinding

    var detailId = 0
    var detailName = ""
    var detailLatitude = 0.0
    var detailLongitude = 0.0
    var isDeletableOk = false

    var mNaverMap: NaverMap? = null

    lateinit var naverRetrofit: Retrofit
    lateinit var naverApiList : NaverAPIList

    var thisCoord : LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
        detailId = intent.getIntExtra("myPlaceId", 0)
        detailName = intent.getStringExtra("myPlaceName").toString()
        detailLatitude = intent.getDoubleExtra("myPlaceLatitude", 0.0)
        detailLongitude = intent.getDoubleExtra("myPlaceLongitude", 0.0)
        isDeletableOk = intent.getBooleanExtra("myPlaceIsDeletableOk", false)
        titleTxt.text = detailName
        addAppointmentBtn.visibility = View.GONE
        naverRetrofit = NaverGeocodingAPI.getRetrofit(mContext)
        naverApiList = retrofit.create(NaverAPIList::class.java)
        thisCoord = LatLng(detailLongitude, detailLatitude)


        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        if(isDeletableOk){
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
            val coord = LatLng(detailLatitude, detailLongitude)
            val cameraUpdate = CameraUpdate.scrollTo(coord)
            mNaverMap!!.moveCamera(cameraUpdate)

            val marker = Marker()
            marker.position = coord
            marker.map = mNaverMap
        }
        getCoordToAddress()
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

    fun getCoordToAddress(){
        naverApiList.getRequestNaverAddress(thisCoord!!, "json").enqueue(object : Callback<NaverBasicResponse>{
            override fun onFailure(call: Call<NaverBasicResponse>, t: Throwable) {
                Log.d("주소", "실패1")
            }

            override fun onResponse(
                call: Call<NaverBasicResponse>,
                response: Response<NaverBasicResponse>
            ) {
                if(response.isSuccessful){
                    val br = response.body()!!

                    Log.d("주소", br.toString())
                } else {
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)
                    val message = jsonObj.getString("message")
                    Log.d("주소1", message)
                }
            }
        })
    }
}