package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.api.APIList
import com.sim981.a2022appointmentmanager.api.ServerAPI
import com.sim981.a2022appointmentmanager.fragments.PlacesFragment
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.PlaceData
import com.sim981.a2022appointmentmanager.ui.MainActivity
import com.sim981.a2022appointmentmanager.ui.PlaceDetailActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacesRecyclerAdapter(
    val mContext : Context,
    val mList : List<PlaceData>
) : RecyclerView.Adapter<PlacesRecyclerAdapter.ItemViewHolder>(){
    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val placeNameTxt = itemView.findViewById<TextView>(R.id.placeNameTxt)
        val isPrimaryTxt = itemView.findViewById<TextView>(R.id.placeIsPrimaryTxt)
        val viewPlaceMapImg = itemView.findViewById<ImageView>(R.id.viewPlaceMapImg)

        fun bind (item : PlaceData) {
            val apiList = ServerAPI.getRetrofit(mContext).create(APIList::class.java)

            placeNameTxt.text = item.name
            if(!item.isPrimary) {
                isPrimaryTxt.visibility = View.GONE
            }
            viewPlaceMapImg.setOnClickListener {

            }

            itemView.setOnClickListener { 
                val myIntent = Intent(mContext, PlaceDetailActivity::class.java)
                myIntent.putExtra("myPlaceId", item.id).putExtra("myPlaceName", item.name)
                    .putExtra("myPlaceLatitude", item.latitude).putExtra("myPlaceLongitude", item.longitude)
                    .putExtra("myPlaceIsDeletableOk", true)
                mContext.startActivity(myIntent)
            }            
            
            itemView.setOnLongClickListener {
                apiList.patchRequestDefaultPlace(item.id).enqueue(object : Callback<BasicResponse>{
                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful){
                            val br = response.body()!!
                            Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                            ((mContext as MainActivity)
                                .supportFragmentManager
                                .findFragmentByTag("f2") as PlacesFragment)
                                .getMyPlaceListFromServer()
                        } else {
                            val errorBodyStr = response.errorBody()!!.string()
                            val jsonObj = JSONObject(errorBodyStr)
                            val message = jsonObj.getString("message")

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })

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