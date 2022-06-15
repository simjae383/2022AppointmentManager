package com.sim981.a2022appointmentmanager.api

import com.sim981.a2022appointmentmanager.navermodels.GeoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverAPIList {
    @GET("map-reversegeocode/v2/gc")
    fun getRequestMapAddress(
        @Query("coords") coords: String,
        @Query("output") output: String,
        @Query("orders") orders: String
    ): Call<GeoResponse>
}