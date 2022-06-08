package com.sim981.a2022appointmentmanager.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceData(
    val id : Int,
    val name : String,
    val latitude : Double,
    val longitude : Double,
    @SerializedName("is_primary")
    val isPrimary : Boolean,
) : Serializable {
}