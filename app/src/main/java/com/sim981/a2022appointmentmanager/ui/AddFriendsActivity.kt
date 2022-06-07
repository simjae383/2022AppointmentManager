package com.sim981.a2022appointmentmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.FriendsListRecyclerAdapter
import com.sim981.a2022appointmentmanager.databinding.ActivityAddFriendsBinding
import com.sim981.a2022appointmentmanager.models.UserData

class AddFriendsActivity : BaseActivity() {
    lateinit var binding : ActivityAddFriendsBinding
    lateinit var mFriendAdapter : FriendsListRecyclerAdapter

    var mFriendsList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friends)
        titleTxt.text = "친구 추가 요청하기"
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        mFriendAdapter = FriendsListRecyclerAdapter(mContext, mFriendsList)
        binding.findUserRecyclerView.adapter = mFriendAdapter
        binding.findUserRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
}