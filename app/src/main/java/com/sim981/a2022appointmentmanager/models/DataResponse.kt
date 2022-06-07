package com.sim981.a2022appointmentmanager.models

class DataResponse(
    val user : UserData,
    val token : String,
    val users : List<UserData>
) {
}