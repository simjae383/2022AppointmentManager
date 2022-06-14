package com.sim981.a2022appointmentmanager.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NaverMapServerAPI {
    companion object {

        private var naverRetrofit : Retrofit? = null
        private val baseUrl = "https://naveropenapi.apigw.ntruss.com/"

        fun getRetrofit() : Retrofit {
            val naverMapClientId = "aq1kgvo17i"
            val naverMapClientSecret = "GvVwIoObxc3YaxYjGhLstLjgLd44maU91YCtJfdR"

            val interceptor = Interceptor {
                with(it) {
                    val newRequest = request().newBuilder()
                        .addHeader("X-NCP-APIGW-API-KEY-ID", naverMapClientId)
                        .addHeader("X-NCP-APIGW-API-KEY", naverMapClientSecret)
                        .build()
                    proceed(newRequest)
                }
            }
            val myClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            if (naverRetrofit == null) {
                naverRetrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(myClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return naverRetrofit!!
        }
    }
}