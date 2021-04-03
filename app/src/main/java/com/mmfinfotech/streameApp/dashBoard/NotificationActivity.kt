
package com.mmfinfotech.streameApp.dashBoard

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.models.LiversProfile
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class NotificationActivity : DashBoardBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

//        if (intent.action == AppConstants.IntentActions.actionNotification) {
//            val DataRemoteMsg = intent.getStringExtra(AppConstants.IntentExtras.NotifictnRemoteMsg)
//            val DataRefrenceId = intent.getStringExtra(AppConstants.IntentExtras.NotifictnRemoteRefrenceId)
//            Log.d("TAG", "Data $DataRefrenceId")
//            Log.d("TAG", "Data ${intent.action}")
//            showDialogAcceptReject(DataRefrenceId, DataRemoteMsg)
//        }
    }

    private fun showDialogAcceptReject(RefrenceId: String?, msg: String?) {
        val dialog = LayoutInflater.from(this@NotificationActivity).inflate(
            R.layout.accept_reject_invitatn,
            null,
            false
        )
        val builder = AlertDialog.Builder(this@NotificationActivity)
        builder.setView(dialog)
        dialog.setBackgroundColor(Color.WHITE)
        dialog.findViewById<TextView>(R.id.textviewSubTitle).text = msg
        val buttonOk = dialog.findViewById<TextView>(R.id.imagebuttonConfirm)
        val buttonCancel = dialog.findViewById<TextView>(R.id.imagebuttonCancel)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        val vibrator: Vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        vibrator.vibrate(pattern, 0)
        buttonOk.setOnClickListener {
            alertDialog.dismiss()
            vibrator.cancel()
            acceptInvitation(RefrenceId)
        }
        buttonCancel.setOnClickListener {
            RejectInvitation(RefrenceId)
            alertDialog.dismiss()
            vibrator.cancel()
        }
        Handler().postDelayed(Runnable {
            if (alertDialog.isShowing) {
                if (this.isDestroyed) {
                } else {
                    alertDialog.dismiss()
                    vibrator.cancel()
                }
            }
        }, 60000)
        alertDialog.show()
    }

    private fun acceptInvitation(id: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("streaming_id", id)

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callStreamPost: Call<JsonObject?>? = apiService?.callAcceptInvitation(headers, sendParams)
        callApi(true, callStreamPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val id: String? = getStringFromJson(record, "id", AppConstants.Defaults.string)
                        val channelId: String? = getStringFromJson(record, "channel_id", AppConstants.Defaults.string)
                        val userId: String? = getStringFromJson(record, "user_id", AppConstants.Defaults.string)
                        val title: String? = getStringFromJson(record, "title", AppConstants.Defaults.string)
                        val description: String? = getStringFromJson(record, "discription", AppConstants.Defaults.string)
                        val hashTags: String? = getStringFromJson(record, "hashtags", AppConstants.Defaults.string)
                        val category: String? = getStringFromJson(record, "category", AppConstants.Defaults.string)
                        val startTime: String? = getStringFromJson(record, "start_time", AppConstants.Defaults.string)
                        val endTime: String? = getStringFromJson(record, "end_time", AppConstants.Defaults.string)
                        val status: String? = getStringFromJson(record, "status", AppConstants.Defaults.string)
                        val addedOn: String? = getStringFromJson(record, "added_on", AppConstants.Defaults.string)
                        val updateOn: String? = getStringFromJson(record, "update_on", AppConstants.Defaults.string)
                        val streamerStatus: String? = getStringFromJson(record, "streamer_status", AppConstants.Defaults.string)
                        val streamerId: String? = getStringFromJson(record, "streamer_id", AppConstants.Defaults.string)
                        val streamerName: String? = getStringFromJson(record, "streamer_name", AppConstants.Defaults.string)
                        val streamerProfile: String? = getStringFromJson(record, "streamer_profile", AppConstants.Defaults.string)
                        val like_status: String? = getStringFromJson(record, "like_status", AppConstants.Defaults.string)
                        val rtcToken: String? = getStringFromJson(record, "rtc_token", AppConstants.Defaults.string)
                        val rtmObject: JsonObject? = getJsonObjFromJson(record, "rtm", JsonObject())
                        val rtmToken: String? = getStringFromJson(rtmObject, "rtm", AppConstants.Defaults.string)
                        val userAccount: String? = getStringFromJson(rtmObject, "user_account", AppConstants.Defaults.string)


                        val liversProfile: LiversProfile? = LiversProfile(
                            id, channelId, userId, title, description, hashTags, category,
                            startTime, endTime, status, addedOn, updateOn, "",
                            streamerStatus, "", like_status, "", "",
                            streamerId, streamerName, streamerProfile, rtcToken, rtmToken, userAccount, ArrayList(), ArrayList(), ArrayList()
                        )
                        config()?.channelName = channelId
                        config()?.rtcChannelToken = rtcToken
                        config()?.rtmChannelToken = rtmToken
                        config()?.streamingId = userId
//                        startActivity(LiveActivity.getInstance(this@NotificationActivity, liversProfile, true))
                        finishAffinity()
                    }
                    ValidationError -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@NotificationActivity, "${msg}", Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })


    }

    private fun RejectInvitation(id: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("streaming_id", id)

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callStreamPost: Call<JsonObject?>? = apiService?.callRejectStreame(headers, sendParams)
        callApi(true, callStreamPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        finish()
                    }
                    ValidationError -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@NotificationActivity, "${msg}", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()

//                    vibrator.cancel()
            }

            override fun onFailure() {}
        })


    }

}