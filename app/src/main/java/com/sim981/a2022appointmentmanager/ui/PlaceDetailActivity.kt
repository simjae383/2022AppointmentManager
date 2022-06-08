package com.sim981.a2022appointmentmanager.ui

import androidx.appcompat.app.AppCompatActivity
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
import com.sim981.a2022appointmentmanager.databinding.ActivityPlaceDetailBinding
import com.sim981.a2022appointmentmanager.dialogs.CustomAlertDialog
import com.sim981.a2022appointmentmanager.fragments.PlacesFragment
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.PlaceData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceDetailActivity : BaseActivity() {
    lateinit var binding : ActivityPlaceDetailBinding

    lateinit var placeDetail : PlaceData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
        placeDetail = intent.getSerializableExtra("myPlaceData") as PlaceData
        titleTxt.text = placeDetail.name
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.deletePlaceBtn.setOnClickListener {
            deleteThisPlace()
        }
    }

    override fun setValues() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.detailMap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.detailMap, it).commit()
            }
        mapFragment.getMapAsync {
            val naverMap = it
            val coord = LatLng(placeDetail.latitude, placeDetail.longitude)
            val cameraUpdate = CameraUpdate.scrollTo(coord)
            naverMap.moveCamera(cameraUpdate)

            val marker = Marker()
            marker.position = coord
            marker.map = naverMap
        }
    }

    fun deleteThisPlace(){
        val alert = CustomAlertDialog(mContext)
        alert.myDialog()

        alert.binding.dialogTitleTxt.text = "장소 삭제"
        alert.binding.dialogBodyTxt.text = "정말 장소 목록에서 삭제하시겠습니까?"
        alert.binding.dialogContentEdt.visibility = View.GONE
        alert.binding.dialogPositiveBtn.setOnClickListener {
            apiList.deleteRequestDeletePlace(placeDetail.id).enqueue(object : Callback<BasicResponse> {
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
}