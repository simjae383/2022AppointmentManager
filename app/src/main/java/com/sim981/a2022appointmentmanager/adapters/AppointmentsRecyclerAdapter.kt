package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.models.AppointmentData
import java.text.SimpleDateFormat

class AppointmentsRecyclerAdapter(
    val mContext : Context,
    val mList : List<AppointmentData>
) : RecyclerView.Adapter<AppointmentsRecyclerAdapter.ItemViewHolder>(){
    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val titleTxt = view.findViewById<TextView>(R.id.appoinmentTitleTxt)
        val placeNameTxt = view.findViewById<TextView>(R.id.placeNameTxt)
        val memberCountTxt = view.findViewById<TextView>(R.id.memberCountTxt)
        val timeTxt = view.findViewById<TextView>(R.id.timeTxt)
        val invitedFriendImg = view.findViewById<ImageView>(R.id.invitedFriendImg)
        val invitedFriendTxt = view.findViewById<TextView>(R.id.invitedFriendTxt)

        fun bind(item : AppointmentData){
            titleTxt.text = item.title
            invitedFriendTxt.text = item.user.nickName
            Glide.with(mContext).load(item.user.profileImg).into(invitedFriendImg)

            val sdf = SimpleDateFormat("M/d a h:mm")
            timeTxt.text = "${sdf.format(item.datetime)}"
            placeNameTxt.text = "약속 장소 : ${item.place}"
            memberCountTxt.text = "참여 인원 : ${item.invitedFriends.size}명"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.list_appointment_item, parent, false)
        return ItemViewHolder(row)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}