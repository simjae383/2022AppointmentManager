package com.sim981.a2022appointmentmanager.ui

import android.content.Context
import android.os.Bundle
import android.view.View
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
    lateinit var addFriendBtn : ImageView
    lateinit var addPlaceBtn : ImageView
    lateinit var addAppointmentBtn : ImageView
    lateinit var requestFriendBtn : ImageView
    lateinit var myLocationBtn : ImageView
    lateinit var zoomOutMapBtn : ImageView
    lateinit var deletePlaceBtn : ImageView
    lateinit var editAppointmentBtn : ImageView

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
        addFriendBtn = defActionBar.customView.findViewById(R.id.addFriendBtn)
        addPlaceBtn = defActionBar.customView.findViewById(R.id.addPlaceBtn)
        addAppointmentBtn = defActionBar.customView.findViewById(R.id.addAppointmentBtn)
        requestFriendBtn = defActionBar.customView.findViewById(R.id.requestFriendBtn)
        myLocationBtn = defActionBar.customView.findViewById(R.id.myLocationBtn)
        zoomOutMapBtn = defActionBar.customView.findViewById(R.id.zoomOutMapBtn)
        deletePlaceBtn = defActionBar.customView.findViewById(R.id.deletePlaceBtn)
        editAppointmentBtn = defActionBar.customView.findViewById(R.id.editAppointmentBtn)
        addFriendBtn.visibility = View.GONE
        addPlaceBtn.visibility = View.GONE
        requestFriendBtn.visibility = View.GONE
        myLocationBtn.visibility = View.GONE
        zoomOutMapBtn.visibility = View.GONE
        deletePlaceBtn.visibility = View.GONE
        editAppointmentBtn.visibility = View.GONE
    }
}