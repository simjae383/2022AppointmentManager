package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.api.APIList
import com.sim981.a2022appointmentmanager.api.ServerAPI
import com.sim981.a2022appointmentmanager.databinding.ListUserItemBinding
import com.sim981.a2022appointmentmanager.dialogs.CustomAlertDialog
import com.sim981.a2022appointmentmanager.fragments.MyFriendsFragment
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.UserData
import com.sim981.a2022appointmentmanager.ui.MainActivity
import com.sim981.a2022appointmentmanager.ui.RequestFriendsActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsListRecyclerAdapter(
    val mContext: Context,
    val mList: List<UserData>,
    val type: String
) : RecyclerView.Adapter<FriendsListRecyclerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImg = view.findViewById<ImageView>(R.id.userProfileImg)
        val nicknameTxt = view.findViewById<TextView>(R.id.userNickNameTxt)
        val addFriendBtn = view.findViewById<Button>(R.id.addFriendBtn)
        val acceptBtn = view.findViewById<Button>(R.id.acceptBtn)
        val denyBtn = view.findViewById<Button>(R.id.denyBtn)
        val requestBtnLayout = view.findViewById<LinearLayout>(R.id.requestBtnLayout)
        val socialLoginImg = view.findViewById<ImageView>(R.id.userSocialLoginImg)

        fun bind(item: UserData) {
            val apiList = ServerAPI.getRetrofit(mContext).create(APIList::class.java)

            Glide.with(mContext).load(item.profileImg).into(profileImg)
            nicknameTxt.text = item.nickName
//          어댑터 기능을 요구한 뷰에 따라 아이템 형식 변환
            when (type) {
                "add" -> {
                    addFriendBtn.visibility = View.VISIBLE
                    requestBtnLayout.visibility = View.GONE
                }
                "requested" -> {
                    addFriendBtn.visibility = View.GONE
                    requestBtnLayout.visibility = View.VISIBLE
                }
                "my" -> {
                    addFriendBtn.visibility = View.GONE
                    requestBtnLayout.visibility = View.GONE
                }
            }
//            로그인 제공자에 따른 이미지 추가
            when (item.provider) {
                "kakao" -> {
                    socialLoginImg.setImageResource(R.drawable.kakao_login_icon)
                }
                "facebook" -> {
//                    socialLoginImg.setImageResource(R.drawable.facebook_login_icon)
                }
                else -> {socialLoginImg.visibility = View.GONE}
            }
//          친구 요청 수락/거절 이벤트
            val ocl = object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val okOrNo = p0!!.tag.toString()

                    apiList.putRequestAnswerRequest(item.id, okOrNo)
                        .enqueue(object : Callback<BasicResponse> {
                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                            }

                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if (response.isSuccessful) {
                                    (mContext as RequestFriendsActivity).getRequestedFriendsListFromServer()
                                } else {
                                    val errorBodyStr = response.errorBody()!!.string()
                                    val jsonObj = JSONObject(errorBodyStr)
                                    val message = jsonObj.getString("message")

                                    Log.e("승인_거절 실패", message)
                                }

                            }
                        })
                }
            }
            acceptBtn.setOnClickListener(ocl)
            denyBtn.setOnClickListener(ocl)
//          친구 요청 버튼 이벤트
            addFriendBtn.setOnClickListener {
                apiList.postRequestAddFriend(item.id).enqueue(object : Callback<BasicResponse> {
                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                mContext,
                                "${item.nickName}님에게 친구요청을 보냈습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
//          친구 목록 삭제 이벤트
            itemView.setOnLongClickListener {
                if (type != "my") {
                    return@setOnLongClickListener true
                }
                val alert = CustomAlertDialog(mContext)
                alert.myDialog()

                alert.binding.dialogTitleTxt.text = "친구 삭제"
                alert.binding.dialogBodyTxt.text = "정말 친구 목록에서 삭제하시겠습니까?"
                alert.binding.dialogContentEdt.visibility = View.GONE
                alert.binding.dialogPositiveBtn.setOnClickListener {
                    apiList.deleteRequestDeleteFriend(item.id)
                        .enqueue(object : Callback<BasicResponse> {
                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                            }

                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val br = response.body()!!
                                    Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                                    ((mContext as MainActivity)
                                        .supportFragmentManager
                                        .findFragmentByTag("f1") as MyFriendsFragment)
                                        .getMyFriendsListFromServer()
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
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.list_user_item, parent, false)
        return ItemViewHolder(row)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}