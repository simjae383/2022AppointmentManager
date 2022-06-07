package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.models.UserData

class FriendsListRecyclerAdapter(
    val mContext : Context,
    val mList : List<UserData>,
//    val type : String
) : RecyclerView.Adapter<FriendsListRecyclerAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind (item : UserData){

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