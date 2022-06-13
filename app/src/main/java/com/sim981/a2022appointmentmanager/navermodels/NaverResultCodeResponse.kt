package com.sim981.a2022appointmentmanager.navermodels

import com.google.gson.annotations.SerializedName

class NaverResultCodeResponse(
    @SerializedName("id")
    val resultCodeId : String,
    @SerializedName("type")
    val resultCodeType : String,
    @SerializedName("mappingId")
    val resultCodeMappingId : String
) {
}