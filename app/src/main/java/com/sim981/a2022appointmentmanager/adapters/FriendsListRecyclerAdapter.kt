package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.api.APIList
import com.sim981.a2022appointmentmanager.api.ServerAPI
import com.sim981.a2022appointmentmanager.databinding.ListUserItemBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.UserData
import com.sim981.a2022appointmentmanager.ui.RequestFriendsActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsListRecyclerAdapter(
    val mContext : Context,
    val mList : List<UserData>,
    val type : String
) : RecyclerView.Adapter<FriendsListRecyclerAdapter.ItemViewHolder>() {
    lateinit var binding : ListUserItemBinding

    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind (item : UserData){
            val apiList = ServerAPI.getRetrofit(mContext).create(APIList::class.java)

            Glide.with(mContext).load(item.profileImg).into(binding.userProfileImg)
            binding.userNickNameTxt.text = item.nickName

            when (type) {
                "add" -> {
                    binding.addFriendBtn.visibility = View.VISIBLE
                    binding.requestBtnLayout.visibility = View.GONE
                }
                "requested" -> {
                    binding.addFriendBtn.visibility = View.GONE
                    binding.requestBtnLayout.visibility = View.VISIBLE
                }
                "my" -> {
                    binding.addFriendBtn.visibility = View.GONE
                    binding.requestBtnLayout.visibility = View.GONE
                }
            }

            val ocl = object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val okOrNo = p0!!.tag.toString()

                    apiList.putRequestAnswerRequest(item.id, okOrNo).enqueue(object : Callback<BasicResponse>{
                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(!response.isSuccessful){
                                val errorBodyStr = response.errorBody()!!.string()
                                val jsonObj = JSONObject(errorBodyStr)
                                val message = jsonObj.getString("message")

                                Log.e("승인_거절 실패", message)
                            }

                            (mContext as RequestFriendsActivity).getRequestedFriendsListFromServer()
                        }
                    })
                }
            }

            binding.acceptBtn.setOnClickListener(ocl)
            binding.denyBtn.setOnClickListener(ocl)

            binding.addFriendBtn.setOnClickListener {
                apiList.postRequestAddFriend(item.id).enqueue(object : Callback<BasicResponse>{
                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(mContext, "${item.nickName}님에게 친구요청을 보냈습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.list_user_item,parent,false)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}