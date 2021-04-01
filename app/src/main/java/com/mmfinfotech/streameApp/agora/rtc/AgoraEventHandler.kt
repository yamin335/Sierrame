package com.mmfinfotech.streameApp.agora.rtc

import io.agora.rtc.IRtcEngineEventHandler

class AgoraEventHandler : IRtcEngineEventHandler() {
    private val mHandler: ArrayList<EventHandler?>? = ArrayList()

    fun addHandler(handler: EventHandler?) {
        mHandler?.add(handler)
    }

    fun removeHandler(handler: EventHandler?) {
        mHandler?.remove(handler)
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        mHandler?.forEach { handler ->
            handler?.onJoinChannelSuccess(channel, uid, elapsed)
        }
    }

    override fun onLeaveChannel(stats: RtcStats?) {
        mHandler?.forEach { handler ->
            handler?.onLeaveChannel(stats)
        }
    }

    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        mHandler?.forEach { handler ->
            handler?.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
        }
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
        mHandler?.forEach { handler ->
            handler?.onUserJoined(uid, elapsed)
        }
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        mHandler?.forEach { handler ->
            handler?.onUserOffline(uid, reason)
        }
    }

    override fun onLocalVideoStats(stats: LocalVideoStats?) {
        mHandler?.forEach { handler ->
            handler?.onLocalVideoStats(stats)
        }
    }

    override fun onRtcStats(stats: RtcStats?) {
        mHandler?.forEach { handler ->
            handler?.onRtcStats(stats)
        }
    }

    override fun onNetworkQuality(uid: Int, txQuality: Int, rxQuality: Int) {
        mHandler?.forEach { handler ->
            handler?.onNetworkQuality(uid, txQuality, rxQuality)
        }
    }

    override fun onRemoteVideoStats(stats: RemoteVideoStats?) {
        mHandler?.forEach { handler ->
            handler?.onRemoteVideoStats(stats)
        }
    }

    override fun onRemoteAudioStats(stats: RemoteAudioStats?) {
        mHandler?.forEach { handler ->
            handler?.onRemoteAudioStats(stats)
        }
    }

    override fun onLastmileQuality(quality: Int) {
        mHandler?.forEach { handler ->
            handler?.onLastmileQuality(quality)
        }
    }

    override fun onLastmileProbeResult(result: LastmileProbeResult?) {
        mHandler?.forEach { handler ->
            handler?.onLastmileProbeResult(result)
        }
    }
}