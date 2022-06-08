package com.sim981.a2022appointmentmanager.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sim981.a2022appointmentmanager.databinding.CustomAlertDialogBinding

class CustomAlertDialog (val mContext : Context) {
    val dialog = Dialog(mContext)
    lateinit var binding : CustomAlertDialogBinding

    fun myDialog(){
//        컨텍스트가 가지고 있는 LayoutInflater를 가져오면, 굳이 activty를 들고 올 필요가 없다
        binding = CustomAlertDialogBinding.inflate(LayoutInflater.from(mContext))
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.setCancelable(true)

        dialog.show()
    }
}