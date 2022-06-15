package com.sim981.a2022appointmentmanager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.PlacesRecyclerAdapter
import com.sim981.a2022appointmentmanager.databinding.FragmentPlacesBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.PlaceData
import com.sim981.a2022appointmentmanager.ui.EditMyPlaceActivity
import com.sim981.a2022appointmentmanager.ui.MainActivity
import com.sim981.a2022appointmentmanager.ui.MyLocationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacesFragment : BaseFragment() {
    lateinit var binding: FragmentPlacesBinding

    lateinit var addBtn: ImageView
    lateinit var myLocationBtn: ImageView

//    장소 목록을 저장하는 리스트와 어댑터
    lateinit var mPlacesRecyclerAdapter: PlacesRecyclerAdapter
    var mPlaceList = ArrayList<PlaceData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_places, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBtn = (mContext as MainActivity).addPlaceBtn
        myLocationBtn = (mContext as MainActivity).myLocationBtn
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()

        addBtn.setImageResource(R.drawable.baseline_add_black_24dp)
        myLocationBtn.setImageResource(R.drawable.baseline_my_location_black_24dp)
        getMyPlaceListFromServer()
    }

    override fun setupEvents() {
//        장소 등록 화면으로 넘어가는 버튼
        addBtn.setOnClickListener {
            val myIntent = Intent(mContext, EditMyPlaceActivity::class.java)
            startActivity(myIntent)
        }
//        현재 위치 화면으로 넘어가는 버튼
        myLocationBtn.setOnClickListener {
            val myIntent = Intent(mContext, MyLocationActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mPlacesRecyclerAdapter = PlacesRecyclerAdapter(mContext, mPlaceList)
        binding.myPlacesRecyclerView.adapter = mPlacesRecyclerAdapter
        binding.myPlacesRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
//      장소 목록을 불러와 리스트에 저장하는 메소드
    fun getMyPlaceListFromServer() {
        apiList.getRequestMyPlace().enqueue(object : Callback<BasicResponse> {
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val br = response.body()!!

                    mPlaceList.clear()
                    mPlaceList.addAll(br.data.places)

                    mPlacesRecyclerAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}