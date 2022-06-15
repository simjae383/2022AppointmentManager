package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.models.UserData

class MyFriendsSpinnerAdapter(
    val mContext: Context,
    val resId: Int,
    val mList: List<UserData>
) : ArrayAdapter<UserData>(mContext, resId, mList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(resId, null)
        }
        row!!

        val data = mList[position]

        val profileImg = row.findViewById<ImageView>(R.id.userProfileImg)
        val nicknameTxt = row.findViewById<TextView>(R.id.userNickNameTxt)
        val socialLoginImg = row.findViewById<ImageView>(R.id.userSocialLoginImg)

        nicknameTxt.text = data.nickName

        when (data.provider) {
            "kakao" -> socialLoginImg.setImageResource(R.drawable.kakao_login_icon)
            else -> socialLoginImg.visibility = View.GONE
        }
        Glide.with(mContext).load(data.profileImg).into(profileImg)

        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}