package com.sim981.a2022appointmentmanager.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class AppointmentData(
    val id : Int = 0,
    var title : String = "",
    val datetime : Date = Calendar.getInstance().time,
    @SerializedName("start_place")
    val startPlace : String = "",
    @SerializedName("start_latitude")
    val startLatitude : Double = 0.0,
    @SerializedName("start_longitude")
    val startLongitude : Double = 0.0,
    val place : String = "",
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    val user : UserData = UserData(),
    @SerializedName("invited_friends")
    val invitedFriends : List<UserData> = ArrayList<UserData>()
) : Serializable {
}