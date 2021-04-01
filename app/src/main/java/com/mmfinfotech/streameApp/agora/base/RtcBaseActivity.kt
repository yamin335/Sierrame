package com.mmfinfotech.streameApp.agora.base

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceView
import com.mmfinfotech.streameApp.agora.rtc.EventHandler
import com.mmfinfotech.streameApp.utils.AppConstants
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration

abstract class RtcBaseActivity : BaseActivity(), EventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerRtcEventHandler(this)
        configVideo()
        joinChannel()
    }

    private fun configVideo() {
        val configuration = VideoEncoderConfiguration(
            AppConstants.VIDEO_DIMENSIONS[config()?.videoDimenIndex ?: 0],
            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        )
        configuration.mirrorMode = AppConstants.VIDEO_MIRROR_MODES[config()?.mirrorEncodeIndex ?: 0]
        rtcEngine()?.setVideoEncoderConfiguration(configuration)
    }

    private fun joinChannel() {
        // Initialize token, extra info here before joining channel
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name and uid that
        // you use to generate this token.
        var token: String? = config()?.rtcChannelToken // getString(R.string.agora_access_token)
        if (token == null || TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null // default, no token
        }
        rtcEngine()?.joinChannel(token, config()?.channelName, "", 0)
    }

    protected fun prepareRtcVideo(uid: Int, local: Boolean): SurfaceView {
        // Render local/remote video on a SurfaceView
        val surface = RtcEngine.CreateRendererView(applicationContext)
        if (local) {
            rtcEngine()?.setupLocalVideo(
                VideoCanvas(
                    surface,
                    VideoCanvas.RENDER_MODE_HIDDEN,
                    0,
                    AppConstants.VIDEO_MIRROR_MODES[config()?.mirrorLocalIndex ?: 0]
                )
            )
        } else {
            rtcEngine()?.setupRemoteVideo(
                VideoCanvas(
                    surface,
                    VideoCanvas.RENDER_MODE_HIDDEN,
                    uid,
                    AppConstants.VIDEO_MIRROR_MODES[config()?.mirrorRemoteIndex ?: 0]
                )
            )
        }
        return surface
    }

    protected fun removeRtcVideo(uid: Int, local: Boolean) {
        if (local) {
            rtcEngine()?.setupLocalVideo(null)
        } else {
            rtcEngine()?.setupRemoteVideo(VideoCanvas(null, VideoCanvas.RENDER_MODE_HIDDEN, uid))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("TAG", "onDestroy Rtc base Activity")
        removeRtcEventHandler(this)
        rtcEngine()?.leaveChannel()
    }
}