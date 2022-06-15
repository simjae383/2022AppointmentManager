package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.models.PlaceData

class StartPlaceSpinnerAdapter(
    val mContext: Context,
    val resId: Int,
    val mList: List<PlaceData>
) : ArrayAdapter<PlaceData>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(resId, null)
        }
        row!!

        val data = mList[position]

        val placeNameTxt = row.findViewById<TextView>(R.id.placeNameTxt)
        val isPrimaryTxt = row.findViewById<TextView>(R.id.placeIsPrimaryTxt)
        val viewPlaceMapImg = row.findViewById<ImageView>(R.id.viewPlaceMapImg)

        placeNameTxt.text = data.name
        if (!data.isPrimary) {
            isPrimaryTxt.visibility = View.GONE
        }
        viewPlaceMapImg.visibility = View.GONE

        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}