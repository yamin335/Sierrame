package com.mmfinfotech.streameApp.agora.rtc

import com.mmfinfotech.streameApp.utils.AppConstants


class EngineConfig {
    // private static final int DEFAULT_UID = 0;
    // private int mUid = DEFAULT_UID;
    var channelName: String? = null
    var rtmChannelToken: String? = null
    var rtcChannelToken: String? = null
    var streamingId: String? = null
    private var mShowVideoStats = false
    var videoDimenIndex = AppConstants.DEFAULT_PROFILE_IDX
    var mirrorLocalIndex = 0
    var mirrorRemoteIndex = 0
    var mirrorEncodeIndex = 0

    fun ifShowVideoStats(): Boolean {
        return mShowVideoStats
    }

    fun setIfShowVideoStats(show: Boolean) {
        mShowVideoStats = show
    }
}