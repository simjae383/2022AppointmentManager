package com.sim981.a2022appointmentmanager.navermodels

import java.io.Serializable

data class LandData(
    val type : String,
    val number1 : String,
    var number2 : String,
    var name : String
) : Serializable{
}