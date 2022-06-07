package com.sim981.a2022appointmentmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.databinding.ActivityLoginBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.utils.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
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

                        Toast.makeText(mContext, "${br.data.user.nick_name}님 환영합니다.", Toast.LENGTH_SHORT).show()

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)
                        finish()
                    } else {
                        val errorBodyStr = response.errorBody()!!.string()
                        val jsonObj = JSONObject(errorBodyStr)
                        val code = jsonObj.getInt("code")
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
    }

    override fun setValues() {

    }
}