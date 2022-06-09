package com.sim981.a2022appointmentmanager.models

import com.google.gson.annotations.SerializedName

data class DataResponse(
    val user : UserData,
    val token : String,
    val users : List<UserData>,
    val friends : List<UserData>,
    val place : PlaceData,
    val places : List<PlaceData>,
    val appointment : AppointmentData,
    val appointments : List<AppointmentData>,
    @SerializedName("invited_appointments")
val invitedAppointments : List<AppointmentData>
) {
}