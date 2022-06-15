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
import com.sim981.a2022appointmentmanager.adapters.FriendsListRecyclerAdapter
import com.sim981.a2022appointmentmanager.databinding.FragmentMyFriendsBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.UserData
import com.sim981.a2022appointmentmanager.ui.AddFriendsActivity
import com.sim981.a2022appointmentmanager.ui.MainActivity
import com.sim981.a2022appointmentmanager.ui.RequestFriendsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFriendsFragment : BaseFragment() {

    lateinit var binding: FragmentMyFriendsBinding

//    친구 목록을 저장하는 리스트와 어댑터
    lateinit var mFriendsAdapter: FriendsListRecyclerAdapter
    var mFriendsList = ArrayList<UserData>()

    lateinit var addBtn: ImageView
    lateinit var requestBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_friends, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBtn = (mContext as MainActivity).addFriendBtn
        requestBtn = (mContext as MainActivity).requestFriendBtn
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()

        addBtn.setImageResource(R.drawable.baseline_add_black_24dp)
        requestBtn.setImageResource(R.drawable.baseline_person_add_black_24dp)
        getMyFriendsListFromServer()
    }


    override fun setupEvents() {
//        친구요청을 수락 거절하는 화면으로 넘어가는 버튼
        addBtn.setOnClickListener {
            val myIntent = Intent(mContext, RequestFriendsActivity::class.java)
            startActivity(myIntent)
        }
//        친구 요청을 위한 화면으로 넘어가는 버튼
        requestBtn.setOnClickListener {
            val myIntent = Intent(mContext, AddFriendsActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mFriendsAdapter = FriendsListRecyclerAdapter(mContext, mFriendsList, "my")
        binding.myFriendsRecyclerView.adapter = mFriendsAdapter
        binding.myFriendsRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
//    친구 검색후 리스트에 저장하는 메소드
    fun getMyFriendsListFromServer() {
        apiList.getRequestMyFriendsList("my").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val br = response.body()!!

                    mFriendsList.clear()
                    mFriendsList.addAll(br.data.friends)
                    mFriendsAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }
}