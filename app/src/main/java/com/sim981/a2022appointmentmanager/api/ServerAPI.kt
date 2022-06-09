package com.sim981.a2022appointmentmanager.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.sim981.a2022appointmentmanager.utils.ContextUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ServerAPI {

    companion object {
        //        서버주소
        private val baseUrl = "https://keepthetime.xyz"

        private var retrofit : Retrofit? = null

        fun getRetrofit(context : Context) : Retrofit {

            if (retrofit == null) {

                val interceptor = Interceptor {
                    with(it) {
                        val newRequest = request().newBuilder()
                            .addHeader("X-Http-Token", ContextUtil.getLoginToken(context))
                            .build()
                        proceed(newRequest)
                    }
                }

                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapter(Date::class.java, DateDeserializer())
                    .create()

                val myClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(myClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            }

            return retrofit!!
        }

    }
}