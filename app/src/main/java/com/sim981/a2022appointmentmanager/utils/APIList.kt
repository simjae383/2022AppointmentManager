package com.sim981.a2022appointmentmanager.utils

import com.sim981.a2022appointmentmanager.models.BasicResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIList {
//    user
    @FormUrlEncoded
    @POST("/user")
    fun postRequestLogin (
        @Field("email") email: String,
        @Field("password") password : String,
    ) : Call<BasicResponse>
}