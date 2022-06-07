package com.sim981.a2022appointmentmanager

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.sim981.a2022appointmentmanager.api.APIList
import com.sim981.a2022appointmentmanager.api.ServerAPI
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {
    lateinit var mContext : Context

    lateinit var retrofit: Retrofit
    lateinit var apiList : APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()

    abstract fun setValues()
}