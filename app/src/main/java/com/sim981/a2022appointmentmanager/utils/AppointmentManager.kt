package com.sim981.a2022appointmentmanager.utils

import android.app.Application
import com.kakao.sdk.common.KakaoSdk


class AppointmentManager : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "88c66ead7d59229e0a3ae3741624b441")
    }
}