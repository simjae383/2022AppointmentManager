package com.sim981.a2022appointmentmanager.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.sim981.a2022appointmentmanager.utils.ContextUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class NaverGeocodingAPI {
    companion object {
        //        서버주소
        private val baseUrl = "https://naveropenapi.apigw.ntruss.com/"

        private var naverRetrofit : Retrofit? = null

        private val ClientId = "aq1kgvo17i"
        private val ClientSecret = "EVtgKSCikq7gSeFE7g02iWnn82Cn2tD8JNywiryV"

        fun getRetrofit(context : Context) : Retrofit {

            if (naverRetrofit == null) {

                val interceptor = Interceptor {
                    with(it) {
                        val newRequest = request().newBuilder()
                            .addHeader("X-NCP-APIGW-API-KEY-ID", ClientId)
                            .addHeader("X-NCP-APIGW-API-KEY", ClientSecret)
                            .build()
                        proceed(newRequest)
                    }
                }

                val myClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

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