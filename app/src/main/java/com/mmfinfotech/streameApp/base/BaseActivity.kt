package com.mmfinfotech.streameApp.base

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.SplashActivity
import com.mmfinfotech.streameApp.service.LiveInvitationActivity
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.networkWatcher.NetworkConnectionChecker
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.dismissSafely
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Somil Rawal on 10-07-2020.
 */
open class BaseActivity : AppCompatActivity() {
    private val tag = BaseActivity::class.java.simpleName
    var networkConnectionChecker: NetworkConnectionChecker? = null
    var appPreferences: AppPreferences? = null
    var dialog: Dialog? = null

    fun isNetworkAvailable(): Boolean? {
        return networkConnectionChecker?.isConnectedNow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (appPreferences == null) appPreferences = AppPreferences()
        if (dialog == null) dialog = progressDialog(this@BaseActivity)
        registerReceiver(Receiver(), IntentFilter("InvitationToJoinLive"))
//        ShowAlertInvitationToJoinLive(this@BaseActivity, "Message") { Toast.makeText(this, "click", Toast.LENGTH_SHORT).show() }
//        startActivity(Intent(this@BaseActivity, LiveInvitationActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        if (networkConnectionChecker == null) networkConnectionChecker =
            NetworkConnectionChecker(application)
        if (appPreferences == null) appPreferences = AppPreferences()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnectionChecker = null
        appPreferences = null
    }

    fun <T> callRemoteApi(
        showProgressDialog: Boolean?,
        call: Call<T?>?,
        onApiResponse: ApiClient.ApiCallbackListener<T>
    ) {
        if (showProgressDialog == true && dialog?.isShowing == false) dialog?.show()
        var msg = resources.getString(R.string.something_went_wrong)
        call?.clone()?.enqueue(object : Callback<T?> {
            override fun onFailure(call: Call<T?>, t: Throwable) {
                onApiResponse.onDataFetched(null, false, t.message ?: msg)
                dialog?.dismissSafely()
                ShowAlertRequestFailed(this@BaseActivity)
            }

            override fun onResponse(p0: Call<T?>, response: Response<T?>) {
                msg = response.message() ?: msg
                if (response.isSuccessful) {
                    onApiResponse.onDataFetched(response.body(), true, msg)
                } else {
                    onApiResponse.onDataFetched(response.body(), false, msg)
                }
                dialog?.dismissSafely()
            }
        })
    }

    fun callApi(
        showProgressDialog: Boolean?,
        call: Call<JsonObject?>?,
        onApiResponse: OnApiResponse?
    ) {
        if (showProgressDialog == true && dialog?.isShowing == false) dialog?.show()
        call?.clone()?.enqueue(object : Callback<JsonObject?> {
            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.e(tag, "onFailure ${call.request().url()}")
                Log.e(tag, "onFailure ${t.message}")
                onApiResponse?.onFailure()
                if (dialog?.isShowing == true) dialog?.dismiss()
                ShowAlertRequestFailed(this@BaseActivity)
            }

            override fun onResponse(p0: Call<JsonObject?>, response: Response<JsonObject?>) {
                Log.v(tag, "on Response $response")
                Log.v(tag, "on Response url ${p0.request().url()}")
                Log.v(tag, "on Response Body ${response.body()}")
                Log.v(tag, "on Response headers ${p0.request().headers()}")
                Log.v(tag, "on Response requestBody  ${p0.request().body()} ")
                if (response.isSuccessful) {
                    val jsonParse = JsonParser()
                    val mainObject: JsonObject =
                        jsonParse.parse(response.body().toString()).asJsonObject
                    val status = getStringFromJson(mainObject, status, AppConstants.Defaults.string)
                    var msg = resources.getString(R.string.something_went_wrong)
                    when (status) {
                        Success -> {
                            msg =
                                getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                            onApiResponse?.onSuccess(Success, mainObject)
                        }
                        AppConstants.Defaults.string -> {
                            ShowAlertFailed(
                                this@BaseActivity,
                                msg
                            )
                            if (dialog?.isShowing == true) dialog?.dismiss()
                        }
                        UnAuthorized -> {
                            appPreferences?.clearPreferences(this@BaseActivity)
                            startActivity(Intent(this@BaseActivity, SplashActivity::class.java))
                            if (dialog?.isShowing == true) dialog?.dismiss()
                            finishAffinity()
                        }
                        NotFound -> {
                            onApiResponse?.onSuccess(NotFound, mainObject)
                        }
//                        TokenExpire -> { refreshToken(call, onApiResponse) }
                        OtpExpire -> {
                            onApiResponse?.onSuccess(OtpExpire, mainObject)
                        }
                        NotVerify -> {
                            val authToken: String? = response.headers().get(authtoken)
                            if (authToken != null && !TextUtils.isEmpty(authToken))
                                appPreferences?.setAuthToken(this@BaseActivity, authToken)
                            onApiResponse?.onSuccess(NotVerify, mainObject)
                        }
                        AnotherDevice -> {
                            onApiResponse?.onSuccess(AnotherDevice, mainObject)
                        }
                        ValidationError -> {
                            onApiResponse?.onSuccess(ValidationError, mainObject)
                        }
                        ParameterNotProper -> {
                            onApiResponse?.onSuccess(ParameterNotProper, mainObject)
                        }
                        else -> {
                            msg =
                                getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                            ShowAlertFailed(this@BaseActivity, msg)
                            if (dialog?.isShowing == true) dialog?.dismiss()
                        }
                    }
                } else {
                    Log.v(tag, "on Response errorBody  ${response.errorBody()} ")
                    if (dialog?.isShowing == true) dialog?.dismiss()
                    ShowAlertFailed(this@BaseActivity, getString(R.string.something_went_wrong))
                }
            }
        })
    }

    private fun refreshToken(oldCall: Call<JsonObject?>?, onApiResponse: OnApiResponse?) {
        val sendparams: JsonObject? = JsonObject()
        sendparams?.addProperty("user_id", appPreferences?.getStremeId(this@BaseActivity))
        sendparams?.addProperty("oldToken", appPreferences?.getAuthToken(this@BaseActivity))
        Log.v(tag, "Refresh Token sendparams : ${sendparams.toString()}")
        val apiService: MyApiEndpointInterface? =
            ApiClient(this@BaseActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callTokenRefresh: Call<JsonObject>? = apiService?.callTokenRefresh(sendparams)
        callTokenRefresh?.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e(tag, "onFailure token refresh ${t.message}")
                if (dialog?.isShowing == true) dialog?.dismiss()
                ShowAlertRequestFailed(this@BaseActivity)
                onApiResponse?.onFailure()
            }

            override fun onResponse(p0: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonParse = JsonParser()
                    val mainObject: JsonObject =
                        jsonParse.parse(response.body().toString()).asJsonObject
                    val status = getStringFromJson(mainObject, status, AppConstants.Defaults.string)
                    var msg = resources.getString(R.string.something_went_wrong)
                    Log.v(tag, "Response Token refresh $mainObject")
                    when (status) {
                        Success -> {
                            val authToken: String? = response.headers().get(authtoken)
                            Log.v(tag, "auth token $authToken ")
                            if (authToken != null && !TextUtils.isEmpty(authToken))
                                appPreferences?.setAuthToken(this@BaseActivity, authToken)
                            callApi(true, oldCall, onApiResponse)
                        }
                        AppConstants.Defaults.string -> {
                            ShowAlertFailed(
                                this@BaseActivity,
                                msg
                            )
                        }
                        else -> {
                            msg =
                                getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                            ShowAlertFailed(
                                this@BaseActivity,
                                msg
                            )
                        }
                    }
                    if (dialog?.isShowing == true) dialog?.dismiss()
                } else {
                    if (dialog?.isShowing == true) dialog?.dismiss()
                    ShowAlertFailed(
                        this@BaseActivity,
                        getString(R.string.something_went_wrong)
                    )
                }
            }
        })
    }

    fun showDialogInvite(){
        ShowAlertInvitationToJoinLive(applicationContext, "Message") {
            Toast.makeText(this@BaseActivity, "click", Toast.LENGTH_SHORT).show()
        }
    }

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.v("Tag", "onReceive broad cast ${intent?.action}")
            when (intent?.action) {
                "InvitationToJoinLive" -> {
//                    Toast.makeText(context, "InvitationToJoinLive", Toast.LENGTH_SHORT).show()
//                    showDialogInvite()
//                    ShowAlertInvitationToJoinLive(applicationContext, "Message") {
//                        Toast.makeText(this@BaseActivity, "click", Toast.LENGTH_SHORT).show()
//                    }
                    val referenceId = intent.getStringExtra("referenceId") ?: ""
                    val message = intent.getStringExtra("message") ?: "N/A"
                    startActivity(LiveInvitationActivity.getInstance(applicationContext, referenceId, message))
                }
            }
        }

    }
}