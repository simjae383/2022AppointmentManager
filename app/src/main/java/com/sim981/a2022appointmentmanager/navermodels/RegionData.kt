package com.sim981.a2022appointmentmanager.navermodels

import java.io.Serializable

data class RegionData(
    var area0: AreaData,
    var area1: AreaData,
    var area2: AreaData,
    var area3: AreaData,
    var area4: AreaData
) : Serializable {
}