package com.sim981.a2022appointmentmanager.models

data class BasicResponse(
    val code: Int,
    val message: String,
    val data: DataResponse,
) {
}