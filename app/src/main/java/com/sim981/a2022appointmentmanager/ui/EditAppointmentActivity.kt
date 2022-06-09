package com.sim981.a2022appointmentmanager.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityEditAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class EditAppointmentActivity : BaseActivity() {
    lateinit var binding : ActivityEditAppointmentBinding

//    선택한 약속 일시를 저장할 멤버변수
    val mSelectedDateTime = Calendar.getInstance() //기본값 : 현재시간


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        titleTxt.text = "약속 등록하기"
        addAppointmentBtn.visibility = View.GONE
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setupEvents() {
//      날짜선택
        binding.dateTxt.setOnClickListener {
//            날짜를 선택하고 할 일(인터페이스) 작성
            val dl = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    mSelectedDateTime.set(year, month, day)

                    val sdf = SimpleDateFormat("yyyy. M. d")
                    Log.d("선택된 시간", sdf.format(mSelectedDateTime.time))

                    binding.dateTxt.text = sdf.format(mSelectedDateTime.time)
                }
            }
//            DatePickerDialog 팝업
            DatePickerDialog(mContext, dl, mSelectedDateTime.get(Calendar.YEAR),
                mSelectedDateTime.get(Calendar.MONTH),mSelectedDateTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

//        시간 선택
        binding.timeTxt.setOnClickListener {
            val tsl = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
                    mSelectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                    mSelectedDateTime.set(Calendar.MINUTE, minute)

                    val sdf = SimpleDateFormat("a h:mm")
                    binding.timeTxt.text = sdf.format(mSelectedDateTime.time)
                }
            }
            TimePickerDialog(mContext, tsl, mSelectedDateTime.get(Calendar. HOUR_OF_DAY),
            mSelectedDateTime.get(Calendar.MINUTE), false).show()

        }
    }

    override fun setValues() {

    }
}