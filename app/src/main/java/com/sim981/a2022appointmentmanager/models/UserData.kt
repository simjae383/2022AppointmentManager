package com.sim981.a2022appointmentmanager.models

import com.google.gson.annotations.SerializedName

class UserData(
    val id : Int,
    val email : String,
    @SerializedName("nick_name")
    val nickName : String,
    @SerializedName("profile_img")
    val profileImg : String,
    @SerializedName("ready_minute")
    val readyMinute : String,
) {
}