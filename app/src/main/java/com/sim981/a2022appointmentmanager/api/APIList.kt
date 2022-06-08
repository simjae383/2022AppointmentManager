package com.sim981.a2022appointmentmanager.api

import com.sim981.a2022appointmentmanager.models.BasicResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface APIList {
    //    search
    @GET("/search/user")
    fun getRequestSearchUser(@Query("nickname") nickname: String) : Call<BasicResponse>

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

    @FormUrlEncoded
    @PATCH("/user")
    fun patchRequestUserEdit (
        @Field("field") field : String,
        @Field("value") value: String,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @PATCH("/user/password")
    fun patchRequestPwEdit(
        @Field("current_password") currentPw : String,
        @Field("new_password") newPw : String,
    ) : Call<BasicResponse>

    @GET("/user/check")
    fun getRequestUserCheck (
        @Query("type") type : String,
        @Query("value") value : String,
    ) : Call<BasicResponse>

    @DELETE("/user")
    fun deleteRequestUserSecession (
        @Query("text") text : String,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/social")
    fun postRequestSocialLogin(
        @Field("provider") provider : String,
        @Field("uid") uid : String,
        @Field("nick_name") nickname : String,
    ) : Call<BasicResponse>

    @Multipart
    @PUT("/user/image")
    fun putRequestUserImage(@Part profileImg : MultipartBody.Part) : Call<BasicResponse>

    //    friend
    @GET("/user/friend")
    fun getRequestMyFriendsList(@Query("type") type: String): Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/friend")
    fun postRequestAddFriend(@Field("user_id")userId : Int) : Call<BasicResponse>

    @FormUrlEncoded
    @PUT("/user/friend")
    fun putRequestAnswerRequest(@Field("user_id")userId : Int,
                                @Field("type")type: String) : Call<BasicResponse>

    @DELETE("/user/friend")
    fun deleteRequestDeleteFriend(@Query("user_id")userId : Int) : Call<BasicResponse>

    //    user/place
    @GET("/user/place")
    fun getRequestMyPlace () : Call<BasicResponse>
}