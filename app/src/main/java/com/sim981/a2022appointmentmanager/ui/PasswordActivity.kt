package com.sim981.a2022appointmentmanager.ui

import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityPasswordBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.utils.ContextUtil
import com.sim981.a2022appointmentmanager.utils.GlobalData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : BaseActivity() {
    lateinit var binding: ActivityPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        binding.newPwEdt.addTextChangedListener {
            val newPw = binding.newPwEdt.text.toString()
            if (newPw.length >= 8) {
                binding.newPwCheckTxt.text = "정상적인 비밀번호 입니다."
                binding.changePwBtn.isEnabled = true
            } else {
                binding.newPwCheckTxt.text = "8자리 이상 입력해주세요."
                binding.changePwBtn.isEnabled = false
            }
        }
        binding.changePwBtn.setOnClickListener {
            val currentPw = binding.currentPwEdt.text.toString()
            val newPw = binding.newPwEdt.text.toString()
            if (newPw.isBlank()) {
                Toast.makeText(mContext, "새 비밀번호를 입력하시오", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            apiList.patchRequestPwEdit(currentPw, newPw).enqueue(object : Callback<BasicResponse> {
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        val br = response.body()!!


                        ContextUtil.setLoginToken(mContext, br.data.token)
                        GlobalData.loginUser = br.data.user
                        Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                        finish()
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

    override fun setValues() {

    }
}