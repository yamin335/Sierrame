package com.mmfinfotech.streameApp.agora

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.agora.base.RtcBaseActivity
import com.mmfinfotech.streameApp.agora.stats.LocalStatsData
import com.mmfinfotech.streameApp.agora.stats.RemoteStatsData
import com.mmfinfotech.streameApp.agora.stats.StatsData
import com.mmfinfotech.streameApp.agora.ui.VideoGridContainer
import com.mmfinfotech.streameApp.dashBoard.live.activity.SearchBrodcasterActivity
import com.mmfinfotech.streameApp.model.LiversProfile
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.util.showRequestBrodcaterDialog
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlinx.android.synthetic.main.activity_live.*
import kotlinx.android.synthetic.main.activity_live_two.*
import retrofit2.Call
import java.util.HashMap

class LiveTwoActivity : RtcBaseActivity() {
    companion object {
        fun getInstance(context: Context?, liversProfile: LiversProfile?, isBroadCaster: Boolean?) = Intent(context, LiveTwoActivity::class.java)
            .apply {
                putExtra(AppConstants.IntentExtras.liversProfile, liversProfile)
                putExtra(
                    AppConstants.KEY_CLIENT_ROLE,
                    if (isBroadCaster == true) Constants.CLIENT_ROLE_BROADCASTER else Constants.CLIENT_ROLE_AUDIENCE
                )
            }
    }

    private val TAG: String? = LiveTwoActivity::class.java.simpleName
    private val mVideoGridContainer: VideoGridContainer? by lazy { findViewById(R.id.live_video_grid_layout) }
    private val mVideoDimension: VideoEncoderConfiguration.VideoDimensions? by lazy { AppConstants.VIDEO_DIMENSIONS[config()?.videoDimenIndex ?: 0] }
    private val liversProfile: LiversProfile? by lazy { intent.getParcelableExtra(AppConstants.IntentExtras.liversProfile) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live)
        initUI()

        imageViewClose?.setOnClickListener { exitLiveBroadcast() }
    }

    private fun initUI() {
//        live_room_name.text = config()?.channelName
        textViewLiveUserName.text = config()?.channelName

        val role = intent.getIntExtra(AppConstants.KEY_CLIENT_ROLE, Constants.CLIENT_ROLE_AUDIENCE)

        val isBroadcaster = role == Constants.CLIENT_ROLE_BROADCASTER

//        rtcEngine()?.setBeautyEffectOptions(live_btn_beautification.isActivated, AppConstants.DEFAULT_BEAUTY_OPTIONS)
        rtcEngine()?.setBeautyEffectOptions(false, AppConstants.DEFAULT_BEAUTY_OPTIONS)

        mVideoGridContainer?.setStatsManager(statsManager())

        rtcEngine()?.setClientRole(role)

        if (isBroadcaster) startBroadcast()
    }

    private fun startBroadcast() {
        rtcEngine()?.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        val surface: SurfaceView = prepareRtcVideo(0, true)
        mVideoGridContainer?.addUserVideoSurface(0, surface, true)
    }

    private fun stopBroadcast() {
        rtcEngine()?.setClientRole(Constants.CLIENT_ROLE_AUDIENCE)
        removeRtcVideo(0, true)
        mVideoGridContainer?.removeUserVideo(0, true)
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
//        super.onJoinChannelSuccess(channel, uid, elapsed)
        Log.i(TAG, "onJoinChannelSuccess: ")
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
//        super.onUserJoined(uid, elapsed)
        Log.i(TAG, "onUserJoined: ")
    }

    override fun onUserOffline(uid: Int, reason: Int) {
//        super.onUserOffline(uid, reason)
        runOnUiThread { removeRemoteUser(uid) }
    }

    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
//        super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
        runOnUiThread { renderRemoteUser(uid) }
    }

    private fun renderRemoteUser(uid: Int) {
        val surface: SurfaceView = prepareRtcVideo(uid, false)
        mVideoGridContainer?.addUserVideoSurface(uid, surface, false)
    }

    private fun removeRemoteUser(uid: Int) {
        removeRtcVideo(uid, false)
        mVideoGridContainer?.removeUserVideo(uid, false)
    }

    override fun onLocalVideoStats(stats: IRtcEngineEventHandler.LocalVideoStats?) {
//        super.onLocalVideoStats(stats)
        if (statsManager()?.isEnabled == false) return

        val data: LocalStatsData = statsManager()?.getStatsData(0) as LocalStatsData
        if (data == null) return

        data.width = mVideoDimension?.width ?: 100
        data.height = mVideoDimension?.height ?: 100
        data.framerate = stats?.sentFrameRate ?: 60
    }

    override fun onRtcStats(stats: IRtcEngineEventHandler.RtcStats?) {
//        super.onRtcStats(stats)
        if (statsManager()?.isEnabled == false) return

        val data: LocalStatsData = statsManager()?.getStatsData(0) as LocalStatsData
        if (data == null) return

        data.lastMileDelay = stats?.lastmileDelay ?: 0
        data.videoSendBitrate = stats?.txVideoKBitRate ?: 0
        data.videoRecvBitrate = stats?.rxVideoKBitRate ?: 0
        data.audioSendBitrate = stats?.txAudioKBitRate ?: 0
        data.audioRecvBitrate = stats?.rxAudioKBitRate ?: 0
        data.cpuApp = stats?.cpuAppUsage ?: 0.0
        data.cpuTotal = stats?.cpuAppUsage ?: 0.0
        data.sendLoss = stats?.txPacketLossRate ?: 0
        data.recvLoss = stats?.rxPacketLossRate ?: 0
    }

    override fun onNetworkQuality(uid: Int, txQuality: Int, rxQuality: Int) {
//        super.onNetworkQuality(uid, txQuality, rxQuality)
        if (statsManager()?.isEnabled == false) return

        val data: StatsData = statsManager()?.getStatsData(uid) as StatsData
        if (data == null) return

        data.sendQuality = statsManager()?.qualityToString(txQuality)
        data.recvQuality = statsManager()?.qualityToString(rxQuality)
    }

    override fun onRemoteVideoStats(stats: IRtcEngineEventHandler.RemoteVideoStats?) {
//        super.onRemoteVideoStats(stats)
        if (statsManager()?.isEnabled == false) return

        val data: RemoteStatsData = statsManager()?.getStatsData(stats?.uid ?: 0) as RemoteStatsData
        if (data == null) return

        data.width = stats?.width ?: 100
        data.height = stats?.height ?: 100
        data.framerate = stats?.rendererOutputFrameRate ?: 60
        data.videoDelay = stats?.delay ?: 100
    }

    override fun onRemoteAudioStats(stats: IRtcEngineEventHandler.RemoteAudioStats?) {
//        super.onRemoteAudioStats(stats)
        if (statsManager()?.isEnabled == false) return

        val data: RemoteStatsData = statsManager()?.getStatsData(stats?.uid ?: 0) as RemoteStatsData
        if (data == null) return

        data.audioNetDelay = stats?.networkTransportDelay ?: 0
        data.audioNetJitter = stats?.jitterBufferDelay ?: 0
        data.audioLoss = stats?.audioLossRate ?: 0
        data.audioQuality = statsManager()?.qualityToString(stats?.quality ?: 0)
    }

    override fun finish() {
        super.finish()
        statsManager()?.clearAllData()
    }

    /*
    * Custom Implementations
    * */
    fun onRequestBrodcaster(view: View) {
        showRequestBrodcaterDialog(this@LiveTwoActivity) { v ->
            when (v?.id) {
                R.id.textViewAddContacts -> startActivity(SearchBrodcasterActivity.getInstance(this@LiveTwoActivity, liversProfile?.id))
            }
        }
    }

    private fun exitLiveBroadcast() {
        val isBroadcaster = intent.getIntExtra(AppConstants.KEY_CLIENT_ROLE, Constants.CLIENT_ROLE_AUDIENCE) == Constants.CLIENT_ROLE_BROADCASTER
        if (!isBroadcaster) {
//            mRtmClient?.logout(null)
            finish()
            return
        }

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LiveTwoActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveTwoActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callConnectlive: Call<JsonObject?>? = apiService?.callEndBroadcasting(headers, config()?.streamingId)

        callApi(true, callConnectlive, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                if (dialog?.isShowing == true) dialog?.dismiss()
                when (status) {
                    Sccess -> {
//                        mRtmClient?.logout(null)
                        stopBroadcast()
                        finish()
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@LiveTwoActivity, msg, Toast.LENGTH_LONG).show()
                    }
                    else -> { }
                }

            }

            override fun onFailure() {}
        })
    }
}