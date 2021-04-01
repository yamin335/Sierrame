package com.mmfinfotech.streameApp.util.retrofit

import android.os.Build
import com.google.gson.JsonObject

interface MyRequestInterface {

    fun requestSignIn(
        userName: String?,
        password: String?,
        deviceId: String?,
        fcmToken: String?
    ) : JsonObject? {
        return JsonObject().apply {
            addProperty("username", userName)
            addProperty("password", password)
            addProperty("device_id", deviceId)
            addProperty("os_type", "AN")
            addProperty("device_name", Build.BRAND)
            addProperty("device_modal", Build.BOARD)
            addProperty("time_zone", "")
            addProperty("fcm_token", fcmToken)
        }
    }
}