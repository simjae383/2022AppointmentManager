package com.sim981.a2022appointmentmanager.ui

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.api.APIList
import com.sim981.a2022appointmentmanager.api.ServerAPI
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {
    lateinit var mContext : Context

    lateinit var retrofit: Retrofit
    lateinit var apiList : APIList

    lateinit var titleTxt : TextView
    lateinit var firstBtn : ImageView
    lateinit var secondBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        supportActionBar?.let {
            setCustomActionBar()
        }

        retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()

    abstract fun setValues()

    fun setCustomActionBar () {
        val defActionBar = supportActionBar!!

        defActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defActionBar.setCustomView(R.layout.my_custom_action_bar)

        val toolbar = defActionBar.customView.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0,0)

        titleTxt = defActionBar.customView.findViewById(R.id.actionTitleTxt)
        firstBtn = defActionBar.customView.findViewById(R.id.actionfirstBtn)
        secondBtn = defActionBar.customView.findViewById(R.id.actionSecondBtn)
    }
}