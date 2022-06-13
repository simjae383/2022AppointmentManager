package com.sim981.a2022appointmentmanager.navermodels

import com.google.gson.annotations.SerializedName

class NaverAreaResponse(
    @SerializedName("name")
    val areaName : String,
    @SerializedName("coords")
    val areaCoords : NaverCoordsResponse
) {
}