package com.mmfinfotech.streameApp.dashBoard.live.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.agora.base.RtcBaseActivity
import com.mmfinfotech.streameApp.agora.stats.LocalStatsData
import com.mmfinfotech.streameApp.agora.stats.RemoteStatsData
import com.mmfinfotech.streameApp.agora.ui.VideoGridContainer
import com.mmfinfotech.streameApp.dashBoard.bottomsheet.BuyCoinsBottomSheet
import com.mmfinfotech.streameApp.dashBoard.bottomsheet.GiftBottomSheet
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AutoMessageAdapter
import com.mmfinfotech.streameApp.dashBoard.live.adapter.MessageAdapter
import com.mmfinfotech.streameApp.models.*
import com.mmfinfotech.streameApp.ui.OnMessageInputLayoutListener
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.listners.OnAutoMessageSelectListner
import com.mmfinfotech.streameApp.util.listners.OnMemoAddListners
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.AppToast
import com.mmfinfotech.streameApp.utils.GoogleTranslate
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.video.VideoEncoderConfiguration.VideoDimensions
import io.agora.rtm.*
import kotlinx.android.synthetic.main.activity_live.*
import kotlinx.android.synthetic.main.partial_message_input.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class LiveActivity : RtcBaseActivity(), OnMessageInputLayoutListener, GiftBottomSheet.Listeners {
    private val tag: String? = LiveActivity::class.java.simpleName
    private var arrLiveUserConnected: ArrayList<LiveUserConnected?>? = null
    private var arrAutoMessage: ArrayList<AutoMessage?>? = null
    private var arrLivescheduleHotTheme: ArrayList<LiveHotThemeSchedule?>? = null

    private var mVideoGridContainer: VideoGridContainer? = null
    private var mVideoDimension: VideoDimensions? = null
    private var mMuteAudioBtn: ImageView? = null
    private var mMuteVideoBtn: ImageView? = null

    private var mRtmChannel: RtmChannel? = null
    var role: Int = Constants.CLIENT_ROLE_AUDIENCE
    private var arrMesages: ArrayList<MessageBean?>? = ArrayList()
    private var mRtmClient: RtmClient? = null
    private var liversProfile: LiversProfile? = null

    private val arrAnimator: ArrayList<Animator?>? = ArrayList()
    private val arrObject: ArrayList<ImageView?>? = ArrayList()
    private val arrGifImageView: ArrayList<ImageView?>? = ArrayList()
    private var arrGifs: ArrayList<String?>? = null
    private var translateIntoLanguage: String? = ""

    private var arrGifts: ArrayList<Gift?>? = null
    private var giftBottomSheet: GiftBottomSheet? = null
    private var availableCoins: String? = AppConstants.Defaults.string

    var countDownTimer : CountDownTimer? = null

    private var buyCoinsBottomSheet: BuyCoinsBottomSheet? = null

    /**
     * id live Strimer id from Hot theme
     * */
    companion object {
        fun getInstance(context: Context?, liversProfile: LiversProfile?, isBroadCaster: Boolean?) =
            Intent(context, LiveActivity::class.java).apply {
                putExtra(AppConstants.IntentExtras.liversProfile, liversProfile)
                putExtra(
                    AppConstants.KEY_CLIENT_ROLE,
                    if (isBroadCaster == true) Constants.CLIENT_ROLE_BROADCASTER else Constants.CLIENT_ROLE_AUDIENCE
                )
            }
    }

    fun onFollowCLick(view: View) {
        callFollowUnFollow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live)
//      hotStremerId = intent.getStringExtra(AppConstants.IntentExtras.HotStremeId)
        liversProfile = intent.getParcelableExtra(AppConstants.IntentExtras.liversProfile)

        Log.d("Intentaction", "Intentaction ${intent.action}")
        arrLiveUserConnected = ArrayList()
        arrLivescheduleHotTheme = ArrayList()
        arrAutoMessage = ArrayList()
        arrGifs = ArrayList()
        arrGifts = ArrayList()

        mRtmClient = chatManager()?.rtmClient
//        mRtmClient?.login(config()?.rtmChannelToken, liversProfile?.userAccount,
//            object : ResultCallback<Void> {
//                override fun onSuccess(p0: Void?) {
//                    runOnUiThread {
//                        createAndJoinChannel()
//                    }
//                }
//
//                override fun onFailure(p0: ErrorInfo?) {
//                    runOnUiThread {
//                        AppToast.showToast(
//                            this@LiveActivity,
//                            "Login Failed ==> ${p0?.errorDescription}"
//                        )
//                        Log.e(tag, "on Login failure ==> ${p0?.errorDescription}")
//                    }
//                }
//            })
        initData()
        initUI()
        mVideoDimension = AppConstants.VIDEO_DIMENSIONS[config()?.videoDimenIndex ?: 0]
        setListners()
        setAdapter()
        getGiftsFromServer()

        recyclerViewAutoMessages?.adapter = AutoMessageAdapter(
            this@LiveActivity,
            liversProfile?.arrAutoMessage,
            object : OnAutoMessageSelectListner {
                override fun onClick(autoMessage: AutoMessage?) {
                    createAndSendMessage(
                        AppConstants.MessageType.normalMessage,
                        autoMessage?.message,
                        ""
                    )
                }
            }
        )

        recyclerViewLiveMessages?.adapter = MessageAdapter(this@LiveActivity, arrMesages)

//        TODO Full files
//        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f6340740b76a.gif")
//        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f63419b04df0.gif")
//        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f6341a1a07ab.gif")
//        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f6341a76b71e.gif")
//        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f6341ac99dbf.gif")
//        TODO Thumbnails
        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f6340740b76a.gif")
        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f63419b04df0.gif")
        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f6341a1a07ab.gif")
        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f6341a76b71e.gif")
        arrGifs?.add("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f6341ac99dbf.gif")
    }

    private fun getGiftsFromServer() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LiveActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveActivity).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callFetchGifts: Call<JsonObject?>? = apiService?.callFetchGifts(headers)
        callApi(true, callFetchGifts, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val data = getJsonArrayFromJson(record, "data", JsonArray())

                        for (i in 0 until (data?.size() ?: 0)) {
                            val item = getJsonObjFromJson(data, i, JsonObject())
                            val id =
                                getIntFromJson(item, "id", AppConstants.Defaults.integer)//"id": 1,
                            val title = getStringFromJson(
                                item,
                                "title",
                                AppConstants.Defaults.string
                            )//"title": "New_Gift-Icon-10p",
                            val img =
                                getStringFromJson(
                                    item,
                                    "img",
                                    AppConstants.Defaults.string
                                )//"img": "https://staging-streame-bucket.s3.amazonaws
                            val coinPrice = getIntFromJson(
                                item,
                                "coin_price",
                                AppConstants.Defaults.integer
                            )//"coin_price": 10,
                            val sendPrice = getIntFromJson(
                                item,
                                "send_price",
                                AppConstants.Defaults.integer
                            )//"send_price": 10,
                            val description = getStringFromJson(
                                item,
                                "description",
                                AppConstants.Defaults.string
                            )//"description": "gift worth 10p",
                            val category = getIntFromJson(
                                item,
                                "category",
                                AppConstants.Defaults.integer
                            )//"category": "1",
                            val duration = getStringFromJson(
                                item,
                                "duration",
                                AppConstants.Defaults.string
                            )//"duration": null,
                            val _status = getIntFromJson(
                                item,
                                "status",
                                AppConstants.Defaults.integer
                            )//"status": 1,
                            val addedOn = getStringFromJson(
                                item,
                                "added_on",
                                AppConstants.Defaults.string
                            )//"added_on": "2020-12-16 09:37:11",
                            val updateOn = getStringFromJson(
                                item,
                                "update_on",
                                AppConstants.Defaults.string
                            )//"update_on": "2020-12-16 09:37:11",
                            val categoryName = getStringFromJson(
                                item,
                                "category_name",
                                AppConstants.Defaults.string
                            )//"category_name": "funny"
                            arrGifts?.add(
                                Gift(
                                    id = id,
                                    title = title,
                                    image = img,
                                    coinPrice = coinPrice,
                                    sendPrice = sendPrice,
                                    description = description,
                                    category = category,
                                    duration = duration,
                                    status = _status,
                                    addedOn = addedOn,
                                    updateOn = updateOn,
                                    categoryName = categoryName
                                )
                            )
                        }

                        availableCoins = getStringFromJson(
                            record,
                            "available_coins",
                            AppConstants.Defaults.string
                        )
                    }
                    NotFound -> {
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
                        Toast.makeText(this@LiveActivity, msg, Toast.LENGTH_LONG).show()
                    }
                    NotVerify -> {

                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }


    private fun initData() {
        if (liversProfile?.like_status.equals("1")) {
            imageButtonLiveHeart.colorFilter = null
        } else {
            imageButtonLiveHeart.setColorFilter(resources.getColor(R.color.unselectedDrawables))
        }
    }

    private fun scrollToBottom() = recyclerViewLiveMessages.scrollToPosition(
        arrMesages?.size?.minus(1)
            ?: 0
    )

    override fun onResume() {
        super.onResume()
        messageInputLayout?.visibility =
            if (imageButtonLiveToggleMessage.isActivated) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() = exitLiveBroadcast()

    private fun initUI() {
        val roomName: TextView? = findViewById(R.id.live_room_name)
        roomName?.text = config()?.channelName

        role = intent.getIntExtra(
            AppConstants.KEY_CLIENT_ROLE,
            Constants.CLIENT_ROLE_AUDIENCE
        )

        val isBroadcaster = role == Constants.CLIENT_ROLE_BROADCASTER
//        mMuteVideoBtn = findViewById(R.id.live_btn_mute_video)
        mMuteVideoBtn?.isActivated = isBroadcaster
//        mMuteAudioBtn = findViewById(R.id.live_btn_mute_audio)
        mMuteAudioBtn?.isActivated = isBroadcaster

//        val beautyBtn: ImageView = findViewById(R.id.live_btn_beautification)
//        beautyBtn.isActivated = true
//        rtcEngine()?.setBeautyEffectOptions(
//            beautyBtn.isActivated,
//            AppConstants.DEFAULT_BEAUTY_OPTIONS
//        )
        mVideoGridContainer = findViewById(R.id.live_video_grid_layout)
        mVideoGridContainer?.setStatsManager(statsManager())

        if (isBroadcaster) {
            containerLiversProfile?.visibility = View.GONE
            imageButtonLiveToggleMessage?.visibility = View.GONE
            imageButtonLiveGift?.visibility = View.GONE
            imageButtonLiveHeart?.visibility = View.GONE
            containerTranslator?.visibility = View.GONE
        } else {
            imageButtonRequestBrodcaster.visibility = View.GONE
            textViewLiveUserName?.text = liversProfile?.streamerName
            textViewLiversFollowStatus?.text =
                if (liversProfile?.followStatus.equals("1"))
                    resources.getString(R.string.following) else resources.getString(R.string.txt_follow)
            textViewLiversRanking.text = liversProfile?.streamerRanking

            Glide.with(this@LiveActivity)
                .load(liversProfile?.streamerProfile)
                .circleCrop()
                .into(imageViewLiversProfile)
        }

        rtcEngine()?.setClientRole(role)
        if (isBroadcaster) startBroadcast()
    }

    private fun startBroadcast() {
        rtcEngine()?.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        val surface = prepareRtcVideo(0, true)
        mVideoGridContainer?.addUserVideoSurface(0, surface, true)
        mMuteAudioBtn?.isActivated = true
    }

    private fun stopBroadcast() {
        rtcEngine()?.setClientRole(Constants.CLIENT_ROLE_AUDIENCE)
        removeRtcVideo(0, true)
        mVideoGridContainer?.removeUserVideo(0, true)
        mMuteAudioBtn?.isActivated = false
    }

    fun onLeaveClicked(view: View?) {
        finish()
    }

    fun onSwitchCameraClicked(view: View?) {
        rtcEngine()?.switchCamera()
    }

    fun onSwitchMessageClicked(view: View) {
        view.isActivated = !view.isActivated

        if (view.isActivated) {
            messageInputLayout?.visibility = View.VISIBLE
            messageInputLayout?.foucs()
        } else {
            messageInputLayout?.visibility = View.GONE
            messageInputLayout?.removefocus()
        }
    }

    fun onBeautyClicked(view: View) {
        view.isActivated = !view.isActivated
        rtcEngine()?.setBeautyEffectOptions(
            view.isActivated,
            AppConstants.DEFAULT_BEAUTY_OPTIONS
        )
    }

    private fun setAdapter() {
//        mLayoutManager = recyclerUsers.layoutManager as LinearLayoutManager
//        adapterLiveUsersConnected =
//                AdapterLiveUsersConnected(
//                    this@LiveActivity,
//                    arrLiveUserConnected,
//                    object : AdapterLiveUsersConnected.OnLiveUsersLisner {
//                        override fun onClick(position: Int) {
//
//                        }
//                    })
//
//        recyclerUsers.adapter = adapterLiveUsersConnected
    }

    private fun setListners() {
        imageViewClose.setOnClickListener {
//            onBackPressed()
            exitLiveBroadcast()
        }
        imageViewLiversProfile.setOnClickListener {
            showLiveUser(this@LiveActivity, liversProfile) { v ->
                when (v?.id) {
                    R.id.buttonRespose -> { }
                    R.id.buttonRecording -> { }
                    R.id.imageViewMemo -> {
                        showMemoliveStremer(
                            this@LiveActivity,
                            liversProfile?.profileNotes,
                            liversProfile?.streamerName,
                            object : OnMemoAddListners {
                                override fun saveStringListners(view: View, s: String) {
                                    ApiAddMemo(s)
                                }

                                override fun cancelViewListners(view: View) {}
                            })
                    }
                    R.id.imageViewSchedule -> {
                        dialogScheduleListliveStremer(
                            this@LiveActivity,
                            liversProfile?.arrLiveScheduleHotTheme, //arrLivescheduleHotTheme,
                            liversProfile?.streamerName
                        )
                    }
                    R.id.textViewFollowDialog -> { callFollowUnFollow() }
                    R.id.textViewMoreDetail -> {
                        startActivity(
                            UserMoreDetailActivity.getInstance(
                                this@LiveActivity,
                                liversProfile?.streamerId
                            )
                        )
                    }
                }
            }
        }

        messageInputLayout?.setOnMessageInputLayoutListener(this@LiveActivity)

        radioGroupLanguage.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.language_en -> {
                        translateIntoLanguage = "en"
                    }
                    R.id.language_ja -> {
                        translateIntoLanguage = "ja"
                    }
                    R.id.language_zh -> {
                        translateIntoLanguage = "zh"
                    }
                }
            }
        })

        appCompactButtonTranslate.setOnClickListener {
            if (translateIntoLanguage.equals("")) {
                ShowAlertInformation(this@LiveActivity, getString(R.string.please_select_language))
            } else {
                if (translateIntoLanguage.equals("en")) {
//                    messageInputLayout?.setOnMessageInputLayoutListener(this@LiveActivity,true)
                } else {
                    val googleTranslate = GoogleTranslate()
                    val result = googleTranslate.execute(
                        messageInputLayout_Message.text.toString(),
                        "en",
                        translateIntoLanguage
                    ).get()
                    messageInputLayout?.setOnMessageInputLayoutListener(
                        this@LiveActivity,
                        true,
                        result
                    )
                }
            }
        }
    }

    private fun ApiAddMemo(Memo: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LiveActivity)}"
        val jsonobject: JsonObject? = JsonObject()
        jsonobject?.addProperty("streamer_id", liversProfile?.streamerId)
        jsonobject?.addProperty("notes", Memo)

        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveActivity).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callMemo: Call<JsonObject?>? = apiService?.callMemoforStremer(headers, jsonobject)
        callApi(true, callMemo, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
//                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        liversProfile?.profileNotes = Memo

                    }
                    NotFound -> {
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
                        Toast.makeText(this@LiveActivity, msg, Toast.LENGTH_LONG).show()
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
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

    fun callFollowUnFollow() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LiveActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveActivity).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callFollowUnfollow: Call<JsonObject?>? = apiService?.callFollowUnFollow(
            headers,
            liversProfile?.streamerId
        )

        callApi(true, callFollowUnfollow, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        liversProfile?.followStatus =
                            getStringFromJson(record, "follow_status", AppConstants.Defaults.string)

                        textViewLiversFollowStatus?.text =
                            if (liversProfile?.followStatus.equals("1"))
                                resources.getString(R.string.following) else resources.getString(R.string.txt_follow)

                    }
                    NotFound -> {
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
                        Toast.makeText(this@LiveActivity, msg, Toast.LENGTH_LONG).show()
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
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

    private fun callApiLikeDisLike(view: View?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? =
            ApiClient(this@LiveActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callLogout: Call<JsonObject?>? = apiService?.callLikeDisslike(
            headers,
            liversProfile?.id,
            AppConstants.LikeTypes.TypeLiveStream
        )
        callApi(true, callLogout, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val status_changed: String? = getStringFromJson(
                            mainObject,
                            "status_changed",
                            AppConstants.Defaults.string
                        )
                        if (status_changed.equals("1")) {
                            createHeartsAnimation(view)
                            imageButtonLiveHeart.setColorFilter(null)
                        } else {
                            imageButtonLiveHeart.setColorFilter(resources.getColor(R.color.unselectedDrawables))
//                            ImageButtonHeart.setImageResource(R.drawable.ic_baseline_emptyheart);
                        }

                    }

                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveAndReleaseChannel()
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        // Do nothing at the moment
        runOnUiThread { AppToast.showToast(this@LiveActivity, "onJoinChannelSuccess") }
        Log.v(tag, "onJoinChannelSuccess")
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
        // Do nothing at the moment
//        runOnUiThread{AppToast.showToast(this@LiveActivity,  "onUserJoined")}
        Log.v(tag, "onUserJoined uid = $uid")
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        runOnUiThread { removeRemoteUser(uid) }
//        runOnUiThread{AppToast.showToast(this@LiveActivity,  "onuserOffline")}
        Log.v(tag, "onUserOffline")
    }

    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        runOnUiThread { renderRemoteUser(uid) }
//        runOnUiThread{AppToast.showToast(this@LiveActivity,  "onFirstRemoteVideoDecoded")}
        Log.v(tag, "onFirstRemoteVideoDecoded")
    }

    override fun onLocalVideoStats(stats: IRtcEngineEventHandler.LocalVideoStats?) {
        Log.v(tag, "onLocalVideoStates")
        if (statsManager()?.isEnabled == false) return

        val data: LocalStatsData = statsManager()?.getStatsData(0) as LocalStatsData ?: return

        data.width = mVideoDimension?.width ?: 0
        data.height = mVideoDimension?.height ?: 0
        data.framerate = stats?.sentFrameRate ?: 0
    }

    override fun onRtcStats(stats: IRtcEngineEventHandler.RtcStats?) {
        Log.v(tag, "onRtcStates $stats")
        if (statsManager()?.isEnabled == false) return

        val data = statsManager()?.getStatsData(0) as LocalStatsData

        data.lastMileDelay = stats?.lastmileDelay ?: 0
        data.videoSendBitrate = stats?.txVideoKBitRate ?: 0
        data.videoRecvBitrate = stats?.rxVideoKBitRate ?: 0
        data.audioSendBitrate = stats?.txAudioKBitRate ?: 0
        data.audioRecvBitrate = stats?.rxAudioKBitRate ?: 0
        data.cpuApp = stats?.cpuAppUsage ?: 0.0
        data.cpuTotal = stats?.cpuAppUsage ?: 0.0
        data.sendLoss = stats?.txPacketLossRate ?: 0
        data.recvLoss = stats?.rxPacketLossRate ?: 0
        Log.v(tag, "onRtcStates End")
    }

    override fun onNetworkQuality(uid: Int, txQuality: Int, rxQuality: Int) {
        Log.v(tag, "onNetworkQuality")
        if (statsManager()?.isEnabled == false) return
        val data = statsManager()?.getStatsData(uid) ?: return
        data.sendQuality = statsManager()?.qualityToString(txQuality)
        data.recvQuality = statsManager()?.qualityToString(rxQuality)
        Log.v(tag, "onNetworkQuality end")
    }

    override fun onRemoteVideoStats(stats: IRtcEngineEventHandler.RemoteVideoStats?) {
        Log.v(tag, "onRemoteVideoStats")
        if (statsManager()?.isEnabled == false) return

        val data: RemoteStatsData? =
            statsManager()?.getStatsData(stats?.uid ?: 0) as RemoteStatsData?

        data?.width = stats?.width ?: 0
        data?.height = stats?.height ?: 0
        data?.framerate = stats?.rendererOutputFrameRate ?: 0
        data?.videoDelay = stats?.delay ?: 0
    }

    override fun onRemoteAudioStats(stats: IRtcEngineEventHandler.RemoteAudioStats?) {
        Log.v(tag, "onRemoteAudioStats")
        if (statsManager()?.isEnabled == false) return

        val data: RemoteStatsData? =
            statsManager()?.getStatsData(stats?.uid ?: 0) as RemoteStatsData? ?: return

        data?.audioNetDelay = stats?.networkTransportDelay ?: 0
        data?.audioNetJitter = stats?.jitterBufferDelay ?: 0
        data?.audioLoss = stats?.audioLossRate ?: 0
        data?.audioQuality = statsManager()?.qualityToString(stats?.quality ?: 0)
    }

    override fun finish() {
        super.finish()
        statsManager()?.clearAllData()
        leaveAndReleaseChannel()
    }

    private fun renderRemoteUser(uid: Int) {
        Log.i(tag, "renderRemoteUser: $uid")
        val surface = prepareRtcVideo(uid, false)
        mVideoGridContainer?.addUserVideoSurface(uid, surface, false)
    }

    private fun removeRemoteUser(uid: Int) {
        removeRtcVideo(uid, false)
        mVideoGridContainer?.removeUserVideo(uid, false)
        Log.v(
            tag,
            "removeRemoteUser ==> uid: $uid  layout count: ${mVideoGridContainer?.childCount}"
        )
        if (mVideoGridContainer?.childCount == 0) {
            AlertDialog.Builder(this@LiveActivity).apply {
                setMessage("Session End!")
                setPositiveButton("Ok") { _, _ -> exitLiveBroadcast() }
                show()
            }
        }
    }

    /**
     * API CALL: create and join channel
     */
    private fun createAndJoinChannel() {
        // step 1: create a channel instance
        mRtmChannel = mRtmClient?.createChannel(
            config()?.channelName,
            MyChannelListener()
        )
        if (mRtmChannel == null) {
            AppToast.showToast(this@LiveActivity, "mRtmChannel is null")
            finish()
            return
        }
        Log.e(tag, "channel" + mRtmChannel.toString() + "")

        // step 2: join the channel
        mRtmChannel?.join(object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {
                Log.i(tag, "join channel success")
                getChannelMemberList()
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.e(
                    tag,
                    "join channel failed ==> ${errorInfo.errorCode} ${errorInfo.errorDescription}"
                )
                runOnUiThread {
                    AppToast.showToast(
                        this@LiveActivity,
                        "Join channel failed ==> ${errorInfo.errorDescription}"
                    )
                    finish()
                }
            }
        })
    }

    inner class MyChannelListener : RtmChannelListener {
        override fun onMemberCountUpdated(p0: Int) {
            Log.v(tag, "onMemberCountUpdated $p0")
        }

        override fun onAttributesUpdated(p0: MutableList<RtmChannelAttribute>?) {
            Log.v(tag, "onAttributesUpdated")
        }

        override fun onMessageReceived(message: RtmMessage?, fromMember: RtmChannelMember?) {
            Log.v(tag, "onMessageReceived")
            runOnUiThread {
                val account: String? = fromMember?.userId
//                #LIKE
//                if (message?.text == "##LIKE##") {
//                    createHeartsAnimation(imageButtonLiveHeart)
//                } else {
//                    val messageBean = MessageBean(account, message, false, AppConstants.MessageType.)
//                    addNewMessage(messageBean)
//                }
                message?.text?.let { _text ->
                    val jsonObject: JsonObject? = Gson().fromJson(_text, JsonObject::class.java)
                    jsonObject?.let { _json ->
                        val _messageType: Int? = getIntFromJson(
                            _json,
                            JsonKey.messageType,
                            AppConstants.Defaults.integer
                        )
                        val _image: String? = getStringFromJson(
                            _json,
                            JsonKey.profileImage,
                            AppConstants.Defaults.string
                        )
                        val _name: String? = getStringFromJson(
                            _json,
                            JsonKey.name,
                            AppConstants.Defaults.string
                        )
                        val _message: String? = getStringFromJson(
                            _json,
                            JsonKey.message,
                            AppConstants.Defaults.string
                        )
                        val _gift: String? = getStringFromJson(
                            _json,
                            JsonKey.giftUrl,
                            AppConstants.Defaults.string
                        )
                        when (_messageType) {
                            AppConstants.MessageType.normalMessage -> addNewMessage(
                                MessageBean(
                                    account,
                                    message,
                                    false,
                                    _messageType,
                                    _message,
                                    _image,
                                    _name
                                )
                            )
                            AppConstants.MessageType.heartMessage -> createHeartsAnimation(
                                imageButtonLiveHeart
                            )
                            AppConstants.MessageType.GiftMessage -> _gift?.let { displayGif(_gift) }
                            else -> {
                            }
                        }
                    }
                }
            }
        }

        override fun onMemberJoined(p0: RtmChannelMember?) {
            Log.v(tag, "onMemberJoined")


        }

        override fun onMemberLeft(p0: RtmChannelMember?) {
            Log.v(
                tag,
                "onMemberLeft => ${p0?.userId}  ${p0?.channelId} ${liversProfile?.streamerId} ${liversProfile?.userId} ${liversProfile?.id}"
            )
        }

    }


    /**
     * API CALL: leave and release channel
     */
    private fun leaveAndReleaseChannel() {
        mRtmChannel?.leave(null)
        mRtmChannel?.release()
        mRtmChannel = null
    }

    /**
     * API CALL: send message to a channel
     */
    private fun sendChannelMessage(message: RtmMessage?) {
        mRtmChannel?.sendMessage(message, object : ResultCallback<Void?> {
            override fun onSuccess(aVoid: Void?) {
//                runOnUiThread { AppToast.showToast(this@LiveActivity, "messages Success") }
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                // refer to RtmStatusCode.ChannelMessageState for the message state
                Log.e(
                    tag,
                    "OnFailure for send message => ${errorInfo.errorCode} ${errorInfo.errorDescription}"
                )
                val errorCode = errorInfo.errorCode
                runOnUiThread {
                    when (errorCode) {
                        RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT, RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE -> AppToast.showToast(
                            this@LiveActivity,
                            "send_msg_failed"
                        )
                    }
                }
            }
        })
    }

    /**
     * API CALL: get channel member list
     */
    private fun getChannelMemberList() {
        mRtmChannel?.getMembers(object : ResultCallback<List<RtmChannelMember?>> {
            override fun onSuccess(responseInfo: List<RtmChannelMember?>) {
                runOnUiThread {
//                    mChannelMemberCount = responseInfo.size
//                    refreshChannelTitle()
                }
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.e(tag, "failed to get channel members, err: " + errorInfo.errorCode)
            }
        })
    }

    private fun exitLiveBroadcast() {
        val isBroadcaster = role == Constants.CLIENT_ROLE_BROADCASTER
        if (!isBroadcaster) {
            mRtmClient?.logout(null)
            finish()
            return
        }

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LiveActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveActivity).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callConnectlive: Call<JsonObject?>? = apiService?.callEndBroadcasting(
            headers,
            config()?.streamingId
        )

        callApi(true, callConnectlive, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
//                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        mRtmClient?.logout(null)
                        finish()
                    }
                    NotFound -> {
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
                        Toast.makeText(this@LiveActivity, msg, Toast.LENGTH_LONG).show()
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    override fun onAttachmentClick() {
        AppToast.showToast(this@LiveActivity, "onAttachmentClick")
    }

    override fun onCameraClick() {
        AppToast.showToast(this@LiveActivity, "onCameraClick")
    }

    override fun onEmojiKeyboardClick() {
        AppToast.showToast(this@LiveActivity, "onEmojiClick")
    }

    override fun onSendMessageClick(message: String?) {
        createAndSendMessage(AppConstants.MessageType.normalMessage, message, "")
    }

    private fun createAndSendMessage(messageType: Int?, message: String?, giftUrl: String?) {
        val image = appPreferences?.getProfileImage(this@LiveActivity)
        val name = appPreferences?.getStremeId(this@LiveActivity)
        val sendMessage = mRtmClient?.createMessage()?.apply {
            text = JsonObject().apply {
                addProperty(JsonKey.messageType, messageType)
                addProperty(JsonKey.message, message)
                addProperty(JsonKey.name, name)
                addProperty(JsonKey.profileImage, image)
                addProperty(JsonKey.giftUrl, giftUrl)
            }.toString()
        }

        if (messageType == AppConstants.MessageType.normalMessage)
            addNewMessage(
                MessageBean(
                    liversProfile?.userAccount,
                    sendMessage,
                    true,
                    messageType,
                    message,
                    image,
                    name
                )
            )
        sendChannelMessage(sendMessage)
    }

    private fun addNewMessage(messageBean: MessageBean?) {
        arrMesages?.add(messageBean)
        recyclerViewLiveMessages?.adapter?.notifyItemRangeChanged(arrMesages?.size ?: 0, 1)
        scrollToBottom()
    }

    fun onHeartButtonClick(view: View?) {
        createAndSendMessage(AppConstants.MessageType.heartMessage, "##LIKE##", "")
        callApiLikeDisLike(view)
//        createHeartsAnimation(view)
    }

    fun onGiftButtonClick(view: View?) {
        val index = Random.nextInt(0, arrGifs?.size?.minus(1) ?: 0)
//        createAndSendMessage(AppConstants.MessageType.GiftMessage, "##GIFT##", arrGifs?.get(index))
//        val animator : ObjectAnimator? = ObjectAnimator.ofFloat(
//            ConstraintLayoutTest, "translationY", - (ConstraintLayoutTest?.height?.toFloat() ?: 500f ))
//        .apply {
//            duration = 500
//        }
//        animator?.start()
        /*showGIFSheet(this@LiveActivity, arrGifs, object : OnGifListners {
            override fun onGifClicking(selectedGif: String?) {
                displayGif(selectedGif)
            }
        })*/
        /*giftBottomSheet = GiftBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("Gifts", arrGifts)
                putString("availableCoins", availableCoins)
            }
        }*/
        giftBottomSheet = GiftBottomSheet.getInstance(arrGifts, availableCoins)
        giftBottomSheet?.show(supportFragmentManager, GiftBottomSheet.TAG)
    }

    fun onCloseGiftView(view: View?) {
        val animator: ObjectAnimator? = ObjectAnimator.ofFloat(
            ConstraintLayoutTest, "translationY", (ConstraintLayoutTest?.height?.toFloat() ?: 500f)
        )
            .apply {
                duration = 500
            }
        animator?.start()
    }


    private fun createHeartsAnimation(view: View?) {
        Handler().postDelayed({
            for (i in 0 until 10) {
                val outLocation = IntArray(2)
                view?.getLocationOnScreen(outLocation)

                val display: Display? = windowManager.defaultDisplay
                val size: Point? = Point()
                display?.getSize(size)
                val width: Int? = size?.x
//                val height: Int? = size?.y

                val heart: Drawable? = ContextCompat.getDrawable(
                    this@LiveActivity,
                    R.drawable.ic_heart
                )

                val imageView: ImageView? = ImageView(baseContext).apply {
                    setImageDrawable(heart)
                    val imageSizeMax = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        40F,
                        resources.displayMetrics
                    ).toInt()
                    val imageSizeMin = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        15F,
                        resources.displayMetrics
                    ).toInt()
                    val paramsImage =
                        RelativeLayout.LayoutParams(
                            Random.nextInt(imageSizeMin, imageSizeMax), Random.nextInt(
                                imageSizeMin,
                                imageSizeMax
                            )
                        )
                    x = (outLocation[0].plus(
                        view?.width?.div(2)?.minus(paramsImage.width.div(2)) ?: 0
                    )).toFloat()
                    y = (outLocation[1].minus(
                        view?.height?.div(2)?.minus(paramsImage.height.div(2)) ?: 0
                    )).toFloat()
                    layoutParams = paramsImage
                }

                runOnUiThread { relativeLayoutRender?.addView(imageView) }

                val animationY: ObjectAnimator? =
                    ObjectAnimator.ofFloat(imageView, "translationY", 50f).apply {
                        duration = Random.nextLong(2000, 6000)
                        startDelay = Random.nextLong(10, 30)
                    }

//                val max = width?.div(4) ?: 0
//                val min = width?.div(12) ?: 0
//            val randomX = (Random.nextInt(max.plus(min)).minus(min)) * 10
                val randomX = Random.nextInt(width?.div(1.5)?.toInt() ?: 0, width ?: 0)
                val animationX: ObjectAnimator? =
                    ObjectAnimator.ofFloat(imageView, "translationX", randomX.toFloat()).apply {
                        duration = 1250
//                duration = Random.nextLong(2000, 6000)
//                repeatCount = ValueAnimator.INFINITE
//                repeatMode = ValueAnimator.REVERSE
                        startDelay = Random.nextLong(10, 30)
                    }

                val fadeOut: ObjectAnimator? =
                    ObjectAnimator.ofFloat(imageView, View.ALPHA, 1f, 0f).apply {
                        duration = animationY?.duration?.div(1.5)?.toLong()
                            ?: 0//Random.nextLong(1000, animationY?.duration ?: 2000)
                        interpolator = LinearInterpolator()
                        startDelay = animationY?.duration?.div(4) ?: 0
                    }

                val animationListener: Animator.AnimatorListener =
                    object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                            Log.v(tag, "onAnimationEnd ==> index : start")
                            arrAnimator?.add(animation)
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            Log.v(
                                tag,
                                "onAnimationEnd ==> index : ${arrAnimator?.indexOf(animation)}"
                            )
                            arrAnimator?.indexOf(animation)?.let {
                                arrObject?.get(it).let { _imageView ->
                                    relativeLayoutRender?.removeView(_imageView)
                                    arrObject?.remove(_imageView)
                                }
                                arrAnimator.removeAt(it)
                            }
//                    relativeLayoutRender?.removeView(arrObject?.get(arrAnimator?.indexOf(animation) ?: 0))
//                    arrAnimator?.remove(animation)
//                    arrObject?.removeAt(arrAnimator?.indexOf(animation) ?: 0)
                        }

                        override fun onAnimationCancel(animation: Animator?) {}
                        override fun onAnimationRepeat(animation: Animator?) {}
                    }

                val animatorSet: AnimatorSet? = AnimatorSet()
                animatorSet?.playTogether(animationX, animationY, fadeOut)
                animatorSet?.addListener(animationListener)
                animatorSet?.start()
                arrObject?.add(imageView)
            }
        }, 100)
    }

    private fun displayGif(url: String?) {
//        Thread {
            /*val gifDrawable: GifDrawable? = GifDrawable(BufferedInputStream(URL(url).openStream())).apply {
                addAnimationListener {
                    try {
                        runOnUiThread {
                            if (arrGifImageView?.size != 0) {
                                relativeLayoutRender?.removeView(arrGifImageView?.get(0))
                                arrGifImageView?.removeAt(0)
                                if (arrGifImageView?.size != 0) {
                                    relativeLayoutRender.addView(arrGifImageView?.get(0))
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.v(tag, "displayGif ==> ${e.message}")
                    }
                }
            }*/
            if (countDownTimer == null)
                countDownTimer = object : CountDownTimer(1500, 1000) {
                    // 5000 = 5 sec
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        if (arrGifImageView?.size != 0) {
                            relativeLayoutRender?.removeView(arrGifImageView?.get(0))
                            arrGifImageView?.removeAt(0)
                            if (arrGifImageView?.size != 0) {
                                relativeLayoutRender.addView(arrGifImageView?.get(0))
                            }
                        }
                        countDownTimer?.start()
                    }
                }.start()

        val space = resources?.getDimensionPixelSize(R.dimen._100sdp)
            val imageParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                space ?: RelativeLayout.LayoutParams.WRAP_CONTENT,
                space ?: RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            }

            val imageView: ImageView = ImageView(this@LiveActivity).apply {
                layoutParams = imageParams
//                setImageDrawable(gifDrawable)

                Glide.with(this@LiveActivity)
                    .load(url)
                    .placeholder(R.drawable.image_placeholder)
                    .into(this)

                id = View.generateViewId()
            }

            arrGifImageView?.add(imageView)

            if (arrGifImageView?.size == 1)
                runOnUiThread { relativeLayoutRender.addView(arrGifImageView[0]) }
//        }.start()
    }

    fun onRequestBrodcaster(view: View) {
        showRequestBrodcaterDialog(this@LiveActivity, object : View.OnClickListener {
            override fun onClick(v: View?) {
                when (v?.id) {
                    R.id.textViewAddContacts -> {
                        startActivity(
                            SearchBrodcasterActivity.getInstance(
                                this@LiveActivity,
                                liversProfile?.id
                            )
                        )
                    }
                }
            }
        })
    }

    override fun onItemClick(gift: Gift?) {
        giftBottomSheet?.dismiss()
//        displayGif(gift?.image)
//        createAndSendMessage(AppConstants.MessageType.GiftMessage, "##GIFT##", gift?.image)

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LiveActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveActivity).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val streamingId: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            liversProfile?.id ?: AppConstants.Defaults.string
        )
        val giftId: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            gift?.id?.toString() ?: AppConstants.Defaults.string
        )
        val callSendGift: Call<JsonObject?>? = apiService?.callSendGift(
            headers,
            streamingId,
            giftId
        )

        callApi(true, callSendGift, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                Log.v(tag, "send gift response == $mainObject")
                when (status) {
                    Success -> {
                        getGiftsFromServer()
                        displayGif(gift?.image)
                        createAndSendMessage(
                            AppConstants.MessageType.GiftMessage,
                            "##GIFT##",
                            gift?.image
                        )
                    }
                    "400" -> {
                        buyCoinsBottomSheet = BuyCoinsBottomSheet.getInstance()
                        buyCoinsBottomSheet?.show(supportFragmentManager, BuyCoinsBottomSheet.TAG)
                    }
                    else -> {
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
                        Toast.makeText(this@LiveActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }
}