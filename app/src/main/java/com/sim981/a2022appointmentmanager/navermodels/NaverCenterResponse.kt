package com.sim981.a2022appointmentmanager.navermodels

import com.google.gson.annotations.SerializedName

class NaverCenterResponse(
    val crs : String,
    @SerializedName("x")
    val coordX : Double,
    @SerializedName("y")
    val coordY : Double
) {
}