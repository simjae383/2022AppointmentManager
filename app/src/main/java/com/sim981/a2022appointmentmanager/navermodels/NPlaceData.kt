package com.sim981.a2022appointmentmanager.navermodels

import java.io.Serializable

data class NPlaceData(
    val title : String,
    val roadAddress : String,
    val mapx : Int,
    val mapy : Int,
) : Serializable{
}