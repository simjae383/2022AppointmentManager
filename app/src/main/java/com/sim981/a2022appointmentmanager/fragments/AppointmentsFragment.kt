package com.sim981.a2022appointmentmanager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.AppointmentsRecyclerAdapter
import com.sim981.a2022appointmentmanager.databinding.FragmentAppointmentsBinding
import com.sim981.a2022appointmentmanager.models.AppointmentData
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.ui.EditAppointmentActivity
import com.sim981.a2022appointmentmanager.ui.MainActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentsFragment : BaseFragment() {
    lateinit var binding: FragmentAppointmentsBinding

    lateinit var mAppointmentAdapter: AppointmentsRecyclerAdapter
    var mAppointmentsList = ArrayList<AppointmentData>()

    lateinit var addBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_appointments, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBtn = (mContext as MainActivity).addAppointmentBtn
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
        addBtn.setImageResource(R.drawable.baseline_add_black_24dp)
        mAppointmentAdapter.notifyDataSetChanged()
        getAppointmentListFromServer()
    }

    override fun setupEvents() {
        addBtn.setOnClickListener {
            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mAppointmentAdapter = AppointmentsRecyclerAdapter(mContext, mAppointmentsList)
        binding.AppointmentRecyclerView.adapter = mAppointmentAdapter
        binding.AppointmentRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun getAppointmentListFromServer() {
        apiList.getRequestMyAppointment().enqueue(object : Callback<BasicResponse> {
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val br = response.body()!!

                    mAppointmentsList.clear()
                    mAppointmentsList.addAll(br.data.appointments)
                    mAppointmentsList.addAll(br.data.invitedAppointments)

                    if (mAppointmentsList.isEmpty()) {
                        binding.emptyLayout.visibility = View.VISIBLE
                    } else {
                        binding.emptyLayout.visibility = View.GONE
                    }
                    mAppointmentAdapter.notifyDataSetChanged()
                } else {
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)
                    val message = jsonObj.getString("message")

                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}