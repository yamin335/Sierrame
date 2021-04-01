package com.mmfinfotech.streameApp

import android.app.AlertDialog
import android.app.Application
import android.content.*
import android.util.Log
import android.widget.Toast
import com.mmfinfotech.streameApp.agora.rtc.AgoraEventHandler
import com.mmfinfotech.streameApp.agora.rtc.EngineConfig
import com.mmfinfotech.streameApp.agora.rtc.EventHandler
import com.mmfinfotech.streameApp.agora.rtm.ChatManager
import com.mmfinfotech.streameApp.agora.stats.StatsManager
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.PrefManager
import io.agora.rtc.Constants
import io.agora.rtc.RtcEngine
import io.agora.rtm.RtmClient

class AppController : Application() {
    private val tag: String? = AppController::class.java.simpleName
    private var mRtcEngine: RtcEngine? = null
    private var mGlobalConfig: EngineConfig? = EngineConfig()
    private var mHandler: AgoraEventHandler? = AgoraEventHandler()
    private var mStatsManager: StatsManager? = StatsManager()

    /**
     * Chatting
     * */
    private var mRtmClient: RtmClient? = null

    //    private var  mMessageHandler: AgoraMessageEventHandler? = AgoraMessageEventHandler()
    private var mChatManager: ChatManager? = null

    override fun onCreate() {
        super.onCreate()
        try {
            mRtcEngine = RtcEngine.create(
                applicationContext,
                getString(R.string.private_app_id),
                mHandler
            )
            // Sets the channel profile of the Agora RtcEngine.
            // The Agora RtcEngine differentiates channel profiles and applies different optimization algorithms accordingly. For example, it prioritizes smoothness and low latency for a video call, and prioritizes video quality for a video broadcast.
            mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
            mRtcEngine?.enableVideo()
//            mRtcEngine?.setLogFile(FileUtil.initializeLogFile(this))

        } catch (e: Exception) {
            e.printStackTrace()
            Log.v(tag, "On catch exception : ${e.message}")
        }

        initConfig()

        mChatManager = ChatManager(this@AppController)
        mChatManager?.init()
//        registerReceiver(Receiver(), IntentFilter("InvitationToJoinLive"))
    }

    private fun initConfig() {
        val pref: SharedPreferences = PrefManager.getPreferences(applicationContext)
        mGlobalConfig?.videoDimenIndex = pref.getInt(
            AppConstants.PREF_RESOLUTION_IDX, AppConstants.DEFAULT_PROFILE_IDX
        )
        val showStats = pref.getBoolean(AppConstants.PREF_ENABLE_STATS, false)
        mGlobalConfig?.setIfShowVideoStats(showStats)
        mStatsManager?.enableStats(showStats)
        mGlobalConfig?.mirrorLocalIndex = pref.getInt(AppConstants.PREF_MIRROR_LOCAL, 0)
        mGlobalConfig?.mirrorRemoteIndex = pref.getInt(AppConstants.PREF_MIRROR_REMOTE, 0)
        mGlobalConfig?.mirrorEncodeIndex = pref.getInt(AppConstants.PREF_MIRROR_ENCODE, 0)
    }

    fun engineConfig(): EngineConfig? {
        return mGlobalConfig
    }

    fun rtcEngine(): RtcEngine? {
        return mRtcEngine
    }

    fun statsManager(): StatsManager? {
        return mStatsManager
    }

    fun registerEventHandler(handler: EventHandler?) {
        mHandler?.addHandler(handler)
    }

    fun removeEventHandler(handler: EventHandler?) {
        mHandler?.removeHandler(handler)
    }

    fun getChatManager(): ChatManager? {
        return mChatManager
    }

    override fun onTerminate() {
        super.onTerminate()
        RtcEngine.destroy()
    }

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.v("Tag", "onReceive broad cast ${intent?.action}")
            when (intent?.action) {
                "InvitationToJoinLive" -> {
                    val builder = AlertDialog.Builder(context, R.style.Theme_AppCompat)
                    builder.setTitle("Title")
                    builder.setMessage("dialogMessage")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("Yes") { dialogInterface, which ->
                        Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG)
                            .show()
                    }
                    builder.setNeutralButton("Cancel") { dialogInterface, which ->
                        Toast.makeText(
                            applicationContext,
                            "clicked cancel\n operation cancel",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    builder.setNegativeButton("No") { dialogInterface, which ->
                        Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG)
                            .show()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
        }
    }
}