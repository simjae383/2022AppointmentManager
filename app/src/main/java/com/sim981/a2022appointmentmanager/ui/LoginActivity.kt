package com.sim981.a2022appointmentmanager.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.ActivityLoginBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.utils.ContextUtil
import com.sim981.a2022appointmentmanager.utils.GlobalData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    lateinit var binding : ActivityLoginBinding

    var TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        addAppointmentBtn.visibility = View.GONE
        titleTxt.text = "AppointmentManager"
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.loginBtn.setOnClickListener {
            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.passwordEdt.text.toString()

            apiList.postRequestLogin(inputEmail,inputPw).enqueue(object : Callback<BasicResponse>{
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful){
                        val br = response.body()!!

                        ContextUtil.setLoginToken(mContext, br.data.token)
                        ContextUtil.setAutoLogin(mContext, binding.autoLoginCb.isChecked)
                        GlobalData.loginUser = br.data.user

                        Toast.makeText(mContext, "${br.data.user.nickName}??? ???????????????.", Toast.LENGTH_SHORT).show()

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)
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
        binding.signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }

        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }
    }

    override fun setValues() {
        titleTxt.text = "AppointmentManager"
    }

    fun kakaoLogin(){
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "????????????????????? ????????? ??????", error)
            } else if (token != null) {
                Log.i(TAG, "????????????????????? ????????? ?????? ${token.accessToken}")
                getKakaoUserInfo()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            UserApiClient.instance.loginWithKakaoTalk(mContext) { token, error ->
                if (error != null) {
                    Log.e(TAG, "?????????????????? ????????? ??????", error)

                    // ???????????? ???????????? ?????? ??? ???????????? ?????? ?????? ???????????? ???????????? ????????? ??????,
                    // ???????????? ????????? ????????? ?????? ????????????????????? ????????? ?????? ?????? ????????? ????????? ?????? (???: ?????? ??????)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // ??????????????? ????????? ?????????????????? ?????? ??????, ????????????????????? ????????? ??????
                    UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "?????????????????? ????????? ?????? ${token.accessToken}")
                    getKakaoUserInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
        }
    }

    fun getKakaoUserInfo(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "????????? ?????? ?????? ??????", error)
            }
            else if (user != null) {
                Log.i(TAG, "????????? ?????? ?????? ??????" +
                        "\n????????????: ${user.id}" +
                        "\n?????????: ${user.kakaoAccount?.profile?.nickname}")
                Log.d("?????????", user.kakaoAccount?.profile?.nickname.toString())
                socialLogin("kakao", "${user.id}", "${user.kakaoAccount?.profile?.nickname}")
            }
        }
    }

    fun socialLogin(provider : String, uid : String, nickname : String) {
        Log.d("?????????2", uid)
        Log.d("?????????2", nickname)
        apiList.postRequestSocialLogin(provider, uid, nickname).enqueue(object : Callback<BasicResponse>{
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!

                    ContextUtil.setAutoLogin(mContext, binding.autoLoginCb.isChecked)
                    ContextUtil.setLoginToken(mContext, br.data.token)
                    GlobalData.loginUser = br.data.user

                    Toast.makeText(
                        mContext,
                        "${GlobalData.loginUser!!.nickName}??? ???????????????.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val myIntent = Intent(mContext, MainActivity::class.java)
                    startActivity(myIntent)
                    finish()
                }
            }
        })
    }
}