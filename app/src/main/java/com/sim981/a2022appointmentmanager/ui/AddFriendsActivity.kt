package com.sim981.a2022appointmentmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.FriendsListRecyclerAdapter
import com.sim981.a2022appointmentmanager.databinding.ActivityAddFriendsBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendsActivity : BaseActivity() {
    lateinit var binding: ActivityAddFriendsBinding
    lateinit var mFriendAdapter: FriendsListRecyclerAdapter

    var mFriendsList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friends)
        titleTxt.text = "친구 추가 요청하기"
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.searchBtn.setOnClickListener {
            val inputNick = binding.searchEdt.text.toString()

            if (inputNick.length < 2) {
                Toast.makeText(mContext, "검새어는 2자 이상 작성해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.getRequestSearchUser(inputNick).enqueue(object : Callback<BasicResponse>{
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!

                        mFriendsList.clear()
                        mFriendsList.addAll(br.data.users)

                        mFriendAdapter.notifyDataSetChanged()
                    }
                }
            })
        }


    }

    override fun setValues() {
        mFriendAdapter = FriendsListRecyclerAdapter(mContext, mFriendsList, "add")
        binding.findUserRecyclerView.adapter = mFriendAdapter
        binding.findUserRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
}