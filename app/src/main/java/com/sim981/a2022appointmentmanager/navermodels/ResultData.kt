package com.sim981.a2022appointmentmanager.navermodels

import java.io.Serializable

data class ResultData(
    var name : String,
    var region : RegionData,
    var land : LandData
) : Serializable{
}