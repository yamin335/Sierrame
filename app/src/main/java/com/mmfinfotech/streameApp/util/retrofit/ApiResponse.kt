package com.mmfinfotech.streameApp.util.retrofit

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat.finishAffinity
import com.mmfinfotech.streameApp.SplashActivity
import com.mmfinfotech.streameApp.util.ShowAlertFailed
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences

object ApiResponse {
    fun <T> create(activity: Activity, status: String?, msg: String, response: T?, listener: ApiClient.ApiResponseListener<T>) {
        val appPreferences = AppPreferences()
        if (response == null) {
            listener.onFailed(status ?: UnAuthorized, msg)
        } else {
            when (status) {
                Success -> {
                    listener.onSuccess(response)
                }
                AppConstants.Defaults.string -> {
                    ShowAlertFailed(
                        activity,
                        msg
                    )
                }
                UnAuthorized -> {
                    appPreferences.clearPreferences(activity)
                    activity.startActivity(Intent(activity, SplashActivity::class.java))
                    finishAffinity(activity)
                }
                NotFound -> {
                    listener.onFailed(NotFound, msg)
                }
                OtpExpire -> {
                    listener.onFailed(OtpExpire, msg)
                }
                NotVerify -> {
//                    val authToken: String? = response.headers().get(authtoken)
//                    if (authToken != null && !TextUtils.isEmpty(authToken))
//                        appPreferences?.setAuthToken(context, authToken)
                    listener.onFailed(NotVerify, msg)
                }
                AnotherDevice -> {
                    listener.onFailed(AnotherDevice, msg)
                }
                ValidationError -> {
                    listener.onFailed(ValidationError, msg)
                }
                ParameterNotProper -> {
                    listener.onFailed(ParameterNotProper, msg)
                }
                else -> {
                    ShowAlertFailed(activity, msg)
                }
            }
        }
    }
}