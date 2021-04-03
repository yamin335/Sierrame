package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.live.activity.LiveActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterHotTheme
import com.mmfinfotech.streameApp.models.*
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.listners.OnDialogPasswordListner
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.fragment_hot_theme.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


class HotThemeFragment : Fragment() {
    private val TAG = HotThemeFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrHotTheme: ArrayList<HotTheme?>? = null
    private var arrHotThemeFilters: ArrayList<HotThemeFilter?>? = null
    private var adapterHotTheme: AdapterHotTheme? = null
    private val gridLayoutManager = GridLayoutManager(activity, 2)
    private var arrAuto_msgsObj: java.util.ArrayList<AutoMessage?>? = null
    private var categoryId: String? = ""

    //    private var mLayoutManager: LinearLayoutManager? = null
    private var intentFrom: String? = AppConstants.Defaults.string
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var oldsize: Int? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hot_theme, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrHotTheme = ArrayList()
        arrHotThemeFilters = ArrayList()
        apiHotTheme(true, pageNo, "")
        setAdapter(arrHotTheme)
        setChipsData()
        setListners()
    }

    private fun setChipsData() {

        for (i in 0 until arrHotThemeFilters?.size!!) {
//          val chip = Chip(mContext, null, R.style.Widget_MaterialComponents_Chip_Choice)
            val chip = this.layoutInflater.inflate(R.layout.dynamic_chip_item, null, false) as Chip
            chip.text = arrHotThemeFilters?.get(i)?.name
//            chip.isClickable = true
//            chip.isCheckable = true

            chip.isSelected = arrHotThemeFilters?.get(i)?.id == categoryId

//            if(arrHotThemeFilters?.get(i)?.id == categoryId){
//                chip.isSelected =true
//            }
//            else{
////                arrHotThemeFilters?.get(i)?.id==""
////                arrHotThemeFilters.get(0).id=
//            }

            chip.setOnClickListener {
                categoryId = arrHotThemeFilters?.get(i)?.id
                Log.d("kak ", "hdfk ${categoryId}")
                apiHotTheme(true, pageNo, categoryId)
            }
            group_data.addView(chip)
        }
    }

    private fun setListners() {
        recyclerviewHotTheme.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = gridLayoutManager?.itemCount
                    lastVisibleItem = gridLayoutManager?.findLastVisibleItemPosition()
                    if (arrHotTheme != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) && (arrHotTheme?.size
                            ?: 0) >= 20
                    ) {
                        loadData = true
                        arrHotTheme?.add(null)
                        adapterHotTheme?.notifyDataSetChanged()
                        pageNo = pageNo?.inc()
                        oldsize = arrHotTheme?.size
                        apiHotTheme(false, pageNo, categoryId)
                    }
                }
            }
        })
    }

    private fun apiHotTheme(showDialog: Boolean?, pageNo: Int?, CategoryFilterId: String?) {
        Log.v(tag, "calling apiHomeTheme -> $pageNo")
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"
        val sendParameter = JsonObject()
        sendParameter.addProperty("category", CategoryFilterId)

        val apiService: MyApiEndpointInterface? = ApiClient(mContext).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callHotTheme: Call<JsonObject?>? = apiService?.callHotTheme(headers, pageNo.toString(), sendParameter)
        (mContext as DashBoardActivity).callApi(true, callHotTheme, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, "record", JsonObject())
                        val bannerArray = getJsonArrayFromJson(record, "banner", JsonArray())
                        if (bannerArray?.size() == 0) {
                        } else {
                            val banner = bannerArray?.get(0)?.asString
                            imageViewHotThemeBanner?.let { it ->
                                Glide.with(mContext as DashBoardActivity)
                                    .load(banner)
                                    .into(it)
                            }
                        }
                        val dataCategoryArray = getJsonArrayFromJson(record, "category", JsonArray())
                        if (pageNo == 1) {
                            arrHotThemeFilters?.clear()
                            arrHotThemeFilters?.add(HotThemeFilter("", "Liver"))
                            for (i in 0 until dataCategoryArray?.size()!!) {
                                val dataCategory: JsonObject? = getJsonObjFromJson(dataCategoryArray, i, JsonObject())
                                val id = getStringFromJson(dataCategory, "id", AppConstants.Defaults.string)
                                val name = getStringFromJson(dataCategory, "name", AppConstants.Defaults.string)
                                arrHotThemeFilters?.add(HotThemeFilter(id, name))
                            }
                            arrHotTheme?.clear()
                            val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                            for (i in 0 until dataArray?.size()!!) {
                                val data: JsonObject? = getJsonObjFromJson(dataArray, i, JsonObject())
                                val id: String? = getStringFromJson(data, "id", AppConstants.Defaults.string)
                                val user_id: String? = getStringFromJson(data, "user_id", AppConstants.Defaults.string)
                                val title: String? = getStringFromJson(data, "title", AppConstants.Defaults.string)
                                val accessOption: String? = getStringFromJson(data, "access_option", AppConstants.Defaults.string)
                                val channel_id: String? = getStringFromJson(data, "channel_id", AppConstants.Defaults.string)
                                val hashtags: String? = getStringFromJson(data, "hashtags", AppConstants.Defaults.string)
                                val status: String? = getStringFromJson(data, "status", AppConstants.Defaults.string)
                                val start_time: String? = getStringFromJson(data, "start_time", AppConstants.Defaults.string)
                                val user_name: String? = getStringFromJson(data, "user_name", AppConstants.Defaults.string)
                                val user_score: Int? = getIntFromJson(data, "user_score", AppConstants.Defaults.integer)
                                val user_profile: String? = getStringFromJson(data, "user_profile", AppConstants.Defaults.string)
                                val rtcToken: String? = getStringFromJson(data, "rtc_token", AppConstants.Defaults.string)
                                val rtmToken: String? = getStringFromJson(data, "rtm_token", AppConstants.Defaults.string)
                                arrHotTheme?.add(
                                    HotTheme(
                                        id, user_id, title, channel_id, hashtags, status, start_time,
                                        user_name, user_score, user_profile, rtcToken, rtmToken, accessOption
                                    )
                                )
                            }
                        } else {
                            val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                            for (i in 0 until dataArray?.size()!!) {
                                val data: JsonObject? = getJsonObjFromJson(dataArray, i, JsonObject())
                                val id: String? = getStringFromJson(data, "id", AppConstants.Defaults.string)
                                val user_id: String? = getStringFromJson(data, "user_id", AppConstants.Defaults.string)
                                val title: String? = getStringFromJson(data, "title", AppConstants.Defaults.string)
                                val channel_id: String? = getStringFromJson(data, "channel_id", AppConstants.Defaults.string)
                                val hashtags: String? = getStringFromJson(data, "hashtags", AppConstants.Defaults.string)
                                val status: String? = getStringFromJson(data, "status", AppConstants.Defaults.string)
                                val start_time: String? = getStringFromJson(data, "start_time", AppConstants.Defaults.string)
                                val user_name: String? = getStringFromJson(data, "user_name", AppConstants.Defaults.string)
                                val user_score: Int? = getIntFromJson(data, "user_score", AppConstants.Defaults.integer)
                                val user_profile: String? = getStringFromJson(data, "user_profile", AppConstants.Defaults.string)
                                val rtcToken: String? = getStringFromJson(data, "rtc_token", AppConstants.Defaults.string)
                                val rtmToken: String? = getStringFromJson(data, "rtm_token", AppConstants.Defaults.string)
                                val collaborateCommunity: HotTheme? = HotTheme(
                                    id,
                                    user_id,
                                    title,
                                    channel_id,
                                    hashtags,
                                    status,
                                    start_time,
                                    user_name,
                                    user_score,
                                    user_profile,
                                    rtcToken,
                                    rtmToken
                                )
                                arrHotTheme?.add(collaborateCommunity)
                            }
                        }
                        if (arrHotTheme?.contains(null) == true) {
                            arrHotTheme?.remove(null)
                        }

                        if (pageNo == 1) {
                            adapterHotTheme?.notifyDataSetChanged()
                            group_data?.removeAllViews()
                            setChipsData()
                        } else {
//                            if (oldsize != arrHome?.size!! )
//                                adapterHome?.notifyItemRangeInserted(oldsize!!, arrHome?.size!! - 1)
                            adapterHotTheme?.notifyDataSetChanged()
                        }
                        if (pageNo!! > 1) {
                            loadData = false
                        }
                        if ((mContext as DashBoardActivity).dialog?.isShowing == true)
                            (mContext as DashBoardActivity).dialog?.dismiss()
                    }
                }
            }

            override fun onFailure() {

            }
        })
    }

    private fun setAdapter(arrHotTheme: ArrayList<HotTheme?>?) {
        recyclerviewHotTheme.layoutManager = gridLayoutManager
        adapterHotTheme = AdapterHotTheme(
            mContext,
            arrHotTheme,
            object : AdapterHotTheme.OnHotThemeLisner {
                override fun onClick(position: Int) {
                    val data: HotTheme? = arrHotTheme?.get(position)
//                        (mContext as DashBoardActivity).setConfig(data?.channel_id, data?.rtcToken, data?.rtmToken, data?.id)
//                        startActivity(LiveActivity.getInstance(mContext, data?.id, false))
//                    apiConnectLive(data?.id)
                    if (data?.accessOption == AppConstants.BroadCastAccessOption.Private){
                        createPrivateBroadCasting(mContext, getString(R.string.dialogDescriptionPassUserLiveJoin), false,
                            object : OnDialogPasswordListner {
                                override fun onButtonClick(view: View?, s: String?) {
//                                    apiGoLive(AppConstants.BroadCastAccessOption.Private, s)
                                    apiConnectLive(data.id, s)
                                }

                                override fun onShareButtonClick(view: View?, s: String?) {
//
                                }
                            })
                    }else {
//                        AppToast.showToast(mContext, "Public Broadcast")
                        apiConnectLive(data?.id, "")
                    }
                }
            })
        recyclerviewHotTheme.adapter = adapterHotTheme
    }

    fun apiConnectLive(stremeId: String?, password: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val apiService: MyApiEndpointInterface? = ApiClient(mContext).getClient()?.create(MyApiEndpointInterface::class.java)
        val requestPassword: RequestBody = RequestBody.create(MediaType.parse("text/plain"), password ?: AppConstants.Defaults.string)
        val callConnectlive: Call<JsonObject?>? = apiService?.callConnectLive(headers, stremeId, requestPassword )

        (mContext as DashBoardActivity).callApi(true, callConnectlive, object : OnApiResponse {
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
                        val streamerRanking: String? = getStringFromJson(record, "streamer_ranking", AppConstants.Defaults.string)
                        val streamerStatus: String? = getStringFromJson(record, "streamer_status", AppConstants.Defaults.string)
                        val followStatus: String? = getStringFromJson(record, "follow_status", AppConstants.Defaults.string)
                        val profileNotes: String? = getStringFromJson(record, "profile_notes", AppConstants.Defaults.string)
                        val totalGifts: String? = getStringFromJson(record, "total_gifts", AppConstants.Defaults.string)
                        val streamerId: String? = getStringFromJson(record, "streamer_id", AppConstants.Defaults.string)
                        val streamerName: String? = getStringFromJson(record, "streamer_name", AppConstants.Defaults.string)
                        val streamerProfile: String? = getStringFromJson(record, "streamer_profile", AppConstants.Defaults.string)
                        val like_status: String? = getStringFromJson(record, "like_status", AppConstants.Defaults.string)
                        val connectedUser: JsonArray? = getJsonArrayFromJson(record, "connected_user", JsonArray())
                        val autoMsgs: JsonArray? = getJsonArrayFromJson(record, "auto_msgs", JsonArray())
                        val schedules: JsonArray? = getJsonArrayFromJson(record, "schedules", JsonArray())
                        if (followStatus.equals("1")) {
                            //true
                        } else {

                        }

                        val arrLiveUserConnected: ArrayList<LiveUserConnected?>? = ArrayList()
                        for (i in 0 until connectedUser?.size()!!) {
                            val userConnectedObj = getJsonObjFromJson(connectedUser, i, JsonObject())
                            val loopId = getStringFromJson(userConnectedObj, "id", AppConstants.Defaults.string)
                            val loopUserId = getStringFromJson(userConnectedObj, "user_id", AppConstants.Defaults.string)
                            val loopUserName = getStringFromJson(userConnectedObj, "user_name", AppConstants.Defaults.string)
                            val loopUserProfile = getStringFromJson(userConnectedObj, "user_profile", AppConstants.Defaults.string)
                            arrLiveUserConnected?.add(LiveUserConnected(loopId, loopUserId, loopUserName, loopUserProfile))
                        }

                        val arrAutoMessage: ArrayList<AutoMessage?>? = ArrayList()
                        for (i in 0 until autoMsgs?.size()!!) {
                            val autoMsgsObj = getJsonObjFromJson(autoMsgs, i, JsonObject())
                            val loopId = getStringFromJson(autoMsgsObj, "id", AppConstants.Defaults.string)
                            val loopMsg = getStringFromJson(autoMsgsObj, "msg", AppConstants.Defaults.string)
                            arrAutoMessage?.add(AutoMessage(loopId, loopMsg))
                        }

                        val arrLiveScheduleHotTheme: ArrayList<LiveHotThemeSchedule?>? = ArrayList()
                        for (i in 0 until schedules?.size()!!) {
                            val schedulesObj = getJsonObjFromJson(schedules, i, JsonObject())
                            val loopId = getStringFromJson(schedulesObj, "id", AppConstants.Defaults.string)
                            val loopDateTime = getStringFromJson(schedulesObj, "date_time", AppConstants.Defaults.string)
                            val loopDay = getStringFromJson(schedulesObj, "day", AppConstants.Defaults.string)
                            val loopDate = getStringFromJson(schedulesObj, "date", AppConstants.Defaults.string)
                            val loopTime = getStringFromJson(schedulesObj, "time", AppConstants.Defaults.string)
                            arrLiveScheduleHotTheme?.add(
                                LiveHotThemeSchedule(
                                    loopId,
                                    loopDateTime,
                                    loopDay,
                                    loopDate,
                                    loopTime
                                )
                            )
                        }

                        val rtcToken: String? = getStringFromJson(record, "rtc_token", AppConstants.Defaults.string)
                        val rtmObject: JsonObject? = getJsonObjFromJson(record, "rtm", JsonObject())
                        val rtmToken: String? = getStringFromJson(rtmObject, "rtm", AppConstants.Defaults.string)
                        val userAccount: String? = getStringFromJson(rtmObject, "user_account", AppConstants.Defaults.string)

                        val liversProfile: LiversProfile? = LiversProfile(
                            id, channelId, userId, title, description, hashTags, category,
                            startTime, endTime, status, addedOn, updateOn, streamerRanking,
                            streamerStatus, followStatus,like_status, profileNotes, totalGifts,
                            streamerId, streamerName, streamerProfile, rtcToken, rtmToken, userAccount,
                            arrLiveUserConnected,
                            arrAutoMessage, arrLiveScheduleHotTheme
                        )
                        (mContext as DashBoardActivity).setConfig(channelId, rtcToken, rtmToken, streamerId)
                        startActivity(LiveActivity.getInstance(activity?.applicationContext!!, liversProfile, false))
//                        startActivity(LiveTwoActivity.getInstance(activity?.applicationContext!!,  liversProfile,false))
//                        val intent = Intent(activity?.intent)
//                        intent.putExtra(AppConstants.KEY_CLIENT_ROLE, Constants.CLIENT_ROLE_BROADCASTER)
//                        intent.setClass(activity?.applicationContext!!, LiveActivityDemo::class.java)
//                        startActivity(intent)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if ((mContext as DashBoardActivity).dialog?.isShowing == true)
                    (mContext as DashBoardActivity).dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

}