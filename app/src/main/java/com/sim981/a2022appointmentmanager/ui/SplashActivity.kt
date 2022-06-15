package com.sim981.a2022appointmentmanager.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.common.util.Utility
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivitySplashBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.utils.ContextUtil
import com.sim981.a2022appointmentmanager.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity() {
    var isTokenOk = false

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        getKeyHash()
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
//        로그인 토큰을 받아오기
        apiList.getRequestMyInfo(ContextUtil.getLoginToken(mContext))
            .enqueue(object : Callback<BasicResponse> {
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        val br = response.body()!!

                        isTokenOk = true
                        GlobalData.loginUser = br.data.user
                    }
                }
            })
    }

    override fun setValues() {
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed({
            val myIntent: Intent
//              로그인 토큰과 자동 로그인 여부가 맞을시 로그인 페이지를 건너뛰고 메인 화면으로 직행
            if (isTokenOk && ContextUtil.getAutoLogin(mContext)) {
                myIntent = Intent(mContext, MainActivity::class.java)
            } else {
                myIntent = Intent(mContext, LoginActivity::class.java)
            }
            startActivity(myIntent)
            finish()
        }, 2500)
    }

    fun getKeyHash() {
        var keyHash = Utility.getKeyHash(mContext)

        Log.d("kakao_keyHash", keyHash)
    }
}