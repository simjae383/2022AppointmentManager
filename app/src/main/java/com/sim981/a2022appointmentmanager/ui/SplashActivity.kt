package com.sim981.a2022appointmentmanager.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        getKeyHash()
        addAppointmentBtn.visibility = View.GONE

        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        apiList.getRequestMyInfo(ContextUtil.getLoginToken(mContext)).enqueue(object : Callback<BasicResponse>{
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful){
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

            if(isTokenOk && ContextUtil.getAutoLogin(mContext)){
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