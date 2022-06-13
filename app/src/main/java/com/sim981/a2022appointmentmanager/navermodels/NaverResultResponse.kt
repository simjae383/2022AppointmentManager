package com.sim981.a2022appointmentmanager.navermodels

import com.google.gson.annotations.SerializedName

class NaverResultResponse(
    @SerializedName("name")
    val resultName : String,
    @SerializedName("code")
    val resultCode : NaverResultCodeResponse,
    val region : NaverRegionResponse
) {
}