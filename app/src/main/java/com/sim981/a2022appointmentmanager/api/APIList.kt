package com.sim981.a2022appointmentmanager.api

import com.sim981.a2022appointmentmanager.models.BasicResponse
import retrofit2.Call
import retrofit2.http.*

interface APIList {
    //    user
    @GET("/user")
    fun getRequestMyInfo(@Header("X-Http-Token") token : String) : Call<BasicResponse>


    @FormUrlEncoded
    @POST("/user")
    fun postRequestLogin (
        @Field("email") email: String,
        @Field("password") password : String,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignUp (
        @Field("email") email: String,
        @Field("password") pw : String,
        @Field("nick_name") nickName : String,
    ) : Call<BasicResponse>

    @GET("/user/check")
    fun getRequestUserCheck (
        @Query("type") type : String,
        @Query("value") value : String,
    ) : Call<BasicResponse>
}