package com.sim981.a2022appointmentmanager.models

data class DataResponse(
    val user : UserData,
    val token : String,
    val users : List<UserData>,
    val friends : List<UserData>,
    val places : List<PlaceData>,
) {
}