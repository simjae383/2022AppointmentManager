package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.models.AppointmentData
import java.text.SimpleDateFormat

class NearAppointmentsSpinnerAdapter(
    val mContext: Context,
    val resId: Int,
    val mList: List<AppointmentData>
) : ArrayAdapter<AppointmentData>(mContext, resId, mList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(resId, null)
        }
        row!!

        val data = mList[position]

        val appointmentName = row.findViewById<TextView>(R.id.appoinmentTitleTxt)
        val appointmentTime = row.findViewById<TextView>(R.id.timeTxt)

        appointmentName.text = data.title
        val sdf = SimpleDateFormat("M/d a h:mm")
        if (position == 0) {
            appointmentTime.text = ""
            appointmentName.text = "그 외의 약속 확인하기"
        } else {
            appointmentTime.text = "${sdf.format(data.datetime)}"
        }

        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

}