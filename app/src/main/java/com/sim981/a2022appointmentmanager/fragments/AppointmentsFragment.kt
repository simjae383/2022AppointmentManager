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

//    약속 목록을 저장하는 리스트와 어댑터
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
//        추가 버튼을 클릭시 약속 등록 액티비티로 넘어감
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

//    약속 리스트를 서버에서 가져와 리스트에 할당
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

//                    약속이 비어있을 경우 빈공간을 표시하는 레이아웃 활성화
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