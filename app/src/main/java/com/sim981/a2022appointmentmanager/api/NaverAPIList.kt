package com.sim981.a2022appointmentmanager.api

import com.naver.maps.geometry.LatLng
import com.sim981.a2022appointmentmanager.navermodels.NaverBasicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverAPIList {
//    ReverseGeocoding
    @GET("map-reversegeocode/v2/gc")
    fun getRequestNaverAddress(@Query("coords") coords : LatLng,
                               @Query("output") output : String) : Call<NaverBasicResponse>
}