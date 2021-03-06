package com.sim981.a2022appointmentmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.FriendsListRecyclerAdapter
import com.sim981.a2022appointmentmanager.databinding.ActivityRequestFriendsBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.UserData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestFriendsActivity : BaseActivity() {
    lateinit var binding : ActivityRequestFriendsBinding

    lateinit var mFriendsAdapter : FriendsListRecyclerAdapter

    var mFriendsList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_friends)
        titleTxt.text = "친구 요청 수락/거절하기"
        addAppointmentBtn.visibility = View.GONE
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
        getRequestedFriendsListFromServer()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        mFriendsAdapter = FriendsListRecyclerAdapter(mContext, mFriendsList, "requested")
        binding.requestFriendsRecyclerView.adapter = mFriendsAdapter
        binding.requestFriendsRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
//      다른 친구가 보낸 친구 요청을 열람하는 기능 - 어댑터에서 수락, 거절 버튼을 통해 기능
    fun getRequestedFriendsListFromServer(){
        apiList.getRequestMyFriendsList("requested").enqueue(object : Callback<BasicResponse>{
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!

                    mFriendsList.clear()
                    mFriendsList.addAll(br.data.friends)
                    mFriendsAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}