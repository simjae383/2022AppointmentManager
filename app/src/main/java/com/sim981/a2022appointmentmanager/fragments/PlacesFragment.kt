package com.sim981.a2022appointmentmanager.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.PlaceRecyclerAdapter
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
    lateinit var binding : FragmentPlacesBinding

    lateinit var addBtn : ImageView
    lateinit var myLocationBtn : ImageView

    lateinit var mPlaceRecyclerAdapter : PlaceRecyclerAdapter
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
        addBtn.visibility = View.VISIBLE
        myLocationBtn.visibility = View.VISIBLE

        addBtn.setImageResource(R.drawable.baseline_add_black_24dp)
        myLocationBtn.setImageResource(R.drawable.baseline_my_location_black_24dp)
        getMyPlaceListFromServer()
    }

    override fun onPause() {
        super.onPause()
        addBtn.visibility = View.GONE
        myLocationBtn.visibility = View.GONE
    }
    override fun setupEvents() {
        addBtn.setOnClickListener {
            Log.d("버튼", "editmyplace")
            val myIntent = Intent(mContext, EditMyPlaceActivity::class.java)
            startActivity(myIntent)
        }
        myLocationBtn.setOnClickListener {
            Log.d("버튼", "mylocation")
            val myIntent = Intent(mContext, MyLocationActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mPlaceRecyclerAdapter = PlaceRecyclerAdapter(mContext, mPlaceList)
        binding.myPlacesRecyclerView.adapter = mPlaceRecyclerAdapter
        binding.myPlacesRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun getMyPlaceListFromServer(){
        apiList.getRequestMyPlace().enqueue(object : Callback<BasicResponse> {
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!

                    mPlaceList.clear()
                    mPlaceList.addAll(br.data.places)

                    mPlaceRecyclerAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}