package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.models.PlaceData

class PlaceRecyclerAdapter(
    val mContext : Context,
    val mList : List<PlaceData>
) : RecyclerView.Adapter<PlaceRecyclerAdapter.ItemViewHolder>(){
    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val placeNameTxt = itemView.findViewById<TextView>(R.id.placeNameTxt)
        val isPrimaryTxt = itemView.findViewById<TextView>(R.id.placeIsPrimaryTxt)
        val viewPlaceMapImg = itemView.findViewById<ImageView>(R.id.viewPlaceMapImg)

        fun bind (item : PlaceData) {
            placeNameTxt.text = item.name
            if(!item.isPrimary) {
                isPrimaryTxt.visibility = View.GONE
            }
            viewPlaceMapImg.setOnClickListener {

            }

            itemView.setOnLongClickListener {




                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.list_place_item, parent, false)
        return ItemViewHolder(row)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}