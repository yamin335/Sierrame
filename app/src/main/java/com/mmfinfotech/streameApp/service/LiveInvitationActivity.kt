package com.mmfinfotech.streameApp.service

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.agora.base.BaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.activity.LiveActivity
import com.mmfinfotech.streameApp.models.LiversProfile
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_authentication_process.*
import kotlinx.android.synthetic.main.activity_live_invitation.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * Created By Somil Rawal on Fri-12-03-2021-March-2021.
 */
class LiveInvitationActivity : BaseActivity() {
    private val TAG: String? = LiveInvitationActivity::class.java.simpleName
    private var referenceId: String? = null

    companion object {
        private val Message: String? by lazy { "extra_message" }
        private val ReferenceId: String? by lazy { "extra_referenceId" }
        fun getInstance(context: Context?, referenceId: String?, message: String?) =
            Intent(context, LiveInvitationActivity::class.java).apply {
                putExtra(Message, message)
                putExtra(ReferenceId, referenceId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_invitation)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        intent?.getStringExtra(Message).let { textViewLiveInvitationSubTitle.text = it }
        intent?.getStringExtra(ReferenceId).let { referenceId = it }

        buttonLiveInvitationCancel.setOnClickListener { onRejectInvitation() }
        buttonLiveInvitationSave.setOnClickListener { onAcceptInvitation() }

    }

    private fun onRejectInvitation() {
        val headers = HashMap<String, String>()
        headers["Authorization"] =
            "Bearer ${AppPreferences().getAuthToken(this@LiveInvitationActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveInvitationActivity).getClient()
            ?.create(MyApiEndpointInterface::class.java)

        val fetch: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), referenceId ?: "")

        val callUserLeaderBoard: Call<JsonObject?>? =
            apiService?.callRejectInvitation(headers, fetch)

        callApi(true, callUserLeaderBoard, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                finish()
            }

            override fun onFailure() {
                finish()
            }
        })
    }

    private fun onAcceptInvitation() {
        val headers = HashMap<String, String>()
        headers["Authorization"] =
            "Bearer ${AppPreferences().getAuthToken(this@LiveInvitationActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveInvitationActivity).getClient()
            ?.create(MyApiEndpointInterface::class.java)

        val fetch: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), referenceId ?: "")

        val callUserLeaderBoard: Call<JsonObject?>? =
            apiService?.callAcceptInvitation(headers, fetch)

        callApi(true, callUserLeaderBoard, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                Log.i(TAG, "onSuccess: $mainObject")
//                    "status":"200",
//                    "message":"Invitee accepted invitation",
//                    "record": {
//                        "id":"394", "channel_id":"4765a25e-1734-4a87-8ba6-986dc79a458e", "user_id":"8",
//                        "title":"bsu", "discription":"", "hashtags":"#iOS", "category":"2",
//                        "start_time":"1615543800", "end_time":null, "status":1, "access_option":1,
//                        "password":"",
//                        "rtc_token":"0069641cde867ea4c1da045494e0f6ca77cIAC+yyiPIus+x68qrU4JSgxr6vQ1nA1aZsOWkxSCyKfRZr6MGEUAAAAAIgBOGAEAeI9MYAQAAQAITEtgAwAITEtgAgAITEtgBAAITEtg",
//                        "rtm_token": {
//                            "rtm":"0069641cde867ea4c1da045494e0f6ca77cIACRhuU2eu/cbICHoWZBxxX3OoyJ0m84iCE4HtLY1klRdXcVWtYAAAAAEABvLQAAWJFMYAEA6APoTUtg",
//                            "user_account":"11"
//                                     },
//                        "added_on":"1615543800", "update_on":"1615543800", "name":"sonam123",
//                        "profile":"https://staging-streame-bucket.s3.amazonaws.com/uploads/5fdc5a3f6fac6.jpg",
//                        "streamer_status":"hey🥰",
//                        "fcm_token":"euW5gKjsa00jtDAchPeBqb:APA91bEsjcSokUF9fyqzmg2Si9xcRvxVtxLmywb-eQw00huicP5rIp5obSe0Hr1KavNraC_NlIjTgkb4s9eEYUOiVnO1APGiGRJWPsbh08CIG5DkemVzECqcT2-V8VfiMMOOxO7vHof8",
//                        "streaming_user_id":"746"
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val stremerId = getStringFromJson(
                            record,
                            "id",
                            AppConstants.Defaults.string
                        )
                        val title = getStringFromJson(record, "title", AppConstants.Defaults.string)
                        val description = getStringFromJson(
                            record,
                            "discription",
                            AppConstants.Defaults.string
                        )
                        val categoryId = getStringFromJson(
                            record,
                            "category",
                            AppConstants.Defaults.string
                        )
                        val accessOption = getStringFromJson(
                            record,
                            "access_option",
                            AppConstants.Defaults.string
                        )
                        val password = getStringFromJson(
                            record,
                            "password",
                            AppConstants.Defaults.string
                        )
                        val hashTags = getStringFromJson(
                            record,
                            "hashtags",
                            AppConstants.Defaults.string
                        )
                        val channelId = getStringFromJson(
                            record,
                            "channel_id",
                            AppConstants.Defaults.string
                        )

                        apiGoLive(
                            channelId,
                            title,
                            description,
                            categoryId,
                            accessOption,
                            password,
                            hashTags
                        )
                    }
                    NotFound -> {
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
                        Toast.makeText(this@LiveInvitationActivity, msg, Toast.LENGTH_LONG).show()
                        if (dialog?.isShowing == true) dialog?.dismiss()
                    }
                    else -> {
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
                        Toast.makeText(this@LiveInvitationActivity, msg, Toast.LENGTH_LONG).show()
                        if (dialog?.isShowing == true) dialog?.dismiss()
                    }
                }

            }

            override fun onFailure() {
                finish()
            }
        })
    }

    private fun apiGoLive(
        channelId: String?,
        title: String?,
        description: String?,
        categoryId: String?,
        accessOption: String?,
        password: String?,
        hashTags: String?
    ) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LiveInvitationActivity)}"


        val requestChennelId: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            channelId ?: AppConstants.Defaults.string
        )
        val requestTitle: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), title ?: AppConstants.Defaults.string)
        val requestDiscription: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            description ?: AppConstants.Defaults.string
        )
        val requestCategory: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            categoryId ?: AppConstants.Defaults.string
        )
        val requestAccessOption: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            accessOption ?: AppConstants.Defaults.string
        )
        val requestPwd: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            password ?: AppConstants.Defaults.string
        )
        val array: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            hashTags ?: AppConstants.Defaults.string
        )

        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveInvitationActivity).getClient()
            ?.create(MyApiEndpointInterface::class.java)
        val callAddSchedule: Call<JsonObject?>? = apiService?.callStreamLive(
            headers, requestChennelId, requestTitle, requestDiscription,
            array, requestCategory, requestAccessOption, requestPwd
        )
        callApi(true, callAddSchedule, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val channel =
                            getStringFromJson(record, "channel", AppConstants.Defaults.string)
                        val rtc = getStringFromJson(record, "rtc", AppConstants.Defaults.string)
                        val rtm = getStringFromJson(record, "rtm", AppConstants.Defaults.string)
                        val userAccount =
                            getStringFromJson(record, "user_account", AppConstants.Defaults.string)

                        val streamingId =
                            getStringFromJson(record, "streaming_id", AppConstants.Defaults.string)

                        config()?.channelName = channel
                        config()?.rtcChannelToken = rtc
                        config()?.rtmChannelToken = rtm
                        config()?.streamingId = streamingId

                        val liversProfile: LiversProfile? = LiversProfile(
                            streamingId, "", "", "", "", "", "",
                            "", "", "", "", "", "",
                            "", "", "", "", "",
                            streamingId, "", "", rtc, rtm, userAccount,
                            ArrayList(), ArrayList(), ArrayList()
                        )

                        Log.i(TAG, "Invitation Channel Name $channel")
                        Log.i(TAG, "Invitation RTC $rtc")
                        Log.i(TAG, "Invitation RTM $rtm")
                        Log.i(TAG, "Invitation streamingId $streamingId")

                        startActivity(LiveActivity.getInstance(applicationContext, liversProfile, true))
//                        startActivity(LiveTwoActivity.getInstance(applicationContext, liversProfile, true))
//                        val intent = Intent(intent)
//                        intent.putExtra(AppConstants.KEY_CLIENT_ROLE, Constants.CLIENT_ROLE_BROADCASTER)
//                        intent.setClass(applicationContext, LiveActivityDemo::class.java)
//                        intent.putExtra(AppConstants.IntentExtras.liversProfile, liversProfile)
//                        startActivity(intent)
                        finish()
                    }
                    NotFound -> {
                        val msg =
                            getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@LiveInvitationActivity, msg, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }
}