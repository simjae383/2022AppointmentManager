package com.sim981.a2022appointmentmanager.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.api.APIList
import com.sim981.a2022appointmentmanager.api.ServerAPI
import com.sim981.a2022appointmentmanager.dialogs.CustomAlertDialog
import com.sim981.a2022appointmentmanager.fragments.AppointmentsFragment
import com.sim981.a2022appointmentmanager.fragments.PlacesFragment
import com.sim981.a2022appointmentmanager.models.AppointmentData
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.ui.AppointmentDetailActivity
import com.sim981.a2022appointmentmanager.ui.MainActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class AppointmentsRecyclerAdapter(
    val mContext : Context,
    val mList : List<AppointmentData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    lateinit var mNearAppointmentSpinnerAdapter : NearAppointmentsSpinnerAdapter
    var mNearAppointmentList = ArrayList<AppointmentData>()

    inner class HeaderViewHolder(headerView : View) : RecyclerView.ViewHolder(headerView){
        val primaryTitleTxt = itemView.findViewById<TextView>(R.id.primaryAppointmentTitleTxt)
        val primaryTimeTxt = itemView.findViewById<TextView>(R.id.primaryAppointmentTimeTxt)
        val primaryLayoutBtn = itemView.findViewById<LinearLayout>(R.id.primaryLayoutBtn)
        val primarySpinner = itemView.findViewById<Spinner>(R.id.primaryAppointmentSpinner)

        fun bindHeader(){
            val apiList = ServerAPI.getRetrofit(mContext).create(APIList::class.java)

            primaryLayoutBtn.setOnClickListener {
                Toast.makeText(mContext, "선택되었습니다.", Toast.LENGTH_SHORT).show()
            }

            mNearAppointmentSpinnerAdapter = NearAppointmentsSpinnerAdapter(mContext, R.layout.list_appointment_item, mNearAppointmentList)
            primarySpinner.adapter = mNearAppointmentSpinnerAdapter

            apiList.getRequestMyAppointment().enqueue(object : Callback<BasicResponse>{
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!

                        mNearAppointmentList.clear()
                        mNearAppointmentList.addAll(br.data.appointments)
                        mNearAppointmentList.addAll(br.data.invitedAppointments)

                        mNearAppointmentSpinnerAdapter.notifyDataSetChanged()
                    } else {
                        val errorBodyStr = response.errorBody()!!.string()
                        val jsonObj = JSONObject(errorBodyStr)
                        val message = jsonObj.getString("message")

                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })

            primarySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position : Int, p3: Long) {
//                    val myIntent = Intent(mContext, AppointmentDetailActivity::class.java)
//                    myIntent.putExtra("appointmentPackage", mNearAppointmentList[position])
//                    mContext.startActivity(myIntent)
                }
            }
        }
    }


    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val titleTxt = itemView.findViewById<TextView>(R.id.appoinmentTitleTxt)
        val placeNameTxt = itemView.findViewById<TextView>(R.id.placeNameTxt)
        val memberCountTxt = itemView.findViewById<TextView>(R.id.memberCountTxt)
        val timeTxt = itemView.findViewById<TextView>(R.id.timeTxt)
        val invitedLayout = itemView.findViewById<LinearLayout>(R.id.invitedLayout)
        val invitedFriendImg = itemView.findViewById<ImageView>(R.id.invitedFriendImg)
        val invitedFriendTxt = itemView.findViewById<TextView>(R.id.invitedFriendTxt)

        fun bind(item : AppointmentData){
            val apiList = ServerAPI.getRetrofit(mContext).create(APIList::class.java)

            invitedLayout.visibility = View.VISIBLE
            placeNameTxt.visibility = View.VISIBLE
            memberCountTxt.visibility = View.VISIBLE

            titleTxt.text = item.title
            invitedFriendTxt.text = item.user.nickName
            Glide.with(mContext).load(item.user.profileImg).into(invitedFriendImg)

            val sdf = SimpleDateFormat("M/d a h:mm")
            timeTxt.text = "${sdf.format(item.datetime)}"
            placeNameTxt.text = "약속 장소 : ${item.place}"
            memberCountTxt.text = "참여 인원 : ${item.invitedFriends.size}명"

            itemView.setOnClickListener {
                val myIntent = Intent(mContext, AppointmentDetailActivity::class.java)
                myIntent.putExtra("appointmentPackage", item)
                mContext.startActivity(myIntent)
            }
            itemView.setOnLongClickListener {
                var resultMessage = ""
                val alert = CustomAlertDialog(mContext)
                alert.myDialog()

                alert.binding.dialogTitleTxt.text = "약속 삭제"
                alert.binding.dialogBodyTxt.text = "정말 삭제하시겠습니까?"
                alert.binding.dialogContentEdt.visibility = View.GONE
                alert.binding.dialogPositiveBtn.setOnClickListener{
                    apiList.deleteRequestDeleteAppointment(item.id).enqueue(object : Callback<BasicResponse>{
                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){
                                val br = response.body()!!
                                resultMessage = "약속을 삭제하였습니다."
                                ((mContext as MainActivity)
                                    .supportFragmentManager
                                    .findFragmentByTag("f0") as AppointmentsFragment)
                                    .getAppointmentListFromServer()
                            } else {
                                val errorBodyStr = response.errorBody()!!.string()
                                val jsonObj = JSONObject(errorBodyStr)
                                resultMessage = jsonObj.getString("message")

                            }
                            val alert2 = CustomAlertDialog(mContext)

                            alert2.myDialog()
                            alert2.binding.dialogTitleTxt.visibility = View.GONE
                            alert2.binding.dialogBodyTxt.text = resultMessage
                            alert2.binding.dialogContentEdt.visibility = View.GONE
                            alert2.binding.dialogPositiveBtn.setOnClickListener {
                                alert2.dialog.dismiss()
                            }
                            alert2.binding.dialogNegativeBtn.visibility = View.GONE
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            TYPE_HEADER -> {
                HeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_appointment_header, parent, false))
            }
            else -> {
                ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_appointment_item, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is HeaderViewHolder -> {
                holder.bindHeader()
            }
            is ItemViewHolder -> {
                holder.bind(mList[position-1])
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position){
            0 -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }
}