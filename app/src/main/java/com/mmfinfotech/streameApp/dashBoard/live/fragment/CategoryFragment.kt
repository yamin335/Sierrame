package com.mmfinfotech.streameApp.dashBoard.live.fragment

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
import com.mmfinfotech.streameApp.model.*
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.listners.OnDialogPasswordListner
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_hot_theme.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

private const val ARG_CATEGORY = "param1"

class CategoryFragment : Fragment() {
    private val TAG: String? = CategoryFragment::class.java.simpleName
    private var category: Int? = null
    private var subCategory: Int? = null
    private var pageNo: Int? = 1
    private var arrFilter: ArrayList<HotThemeFilter?>? = null
    private var arrData: ArrayList<HotTheme?>? = null
    private var adapterHotTheme: AdapterHotTheme? = null

    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var oldsize: Int? = null

    companion object {
        @JvmStatic
        fun newInstance(category: Int?) = CategoryFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CATEGORY, category ?: AppConstants.Defaults.integer)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getInt(ARG_CATEGORY, AppConstants.Defaults.integer)
        }
        arrFilter = ArrayList()
        arrData = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterHotTheme = AdapterHotTheme(context, arrData, object : AdapterHotTheme.OnHotThemeLisner {
            override fun onClick(position: Int) {
                val data: HotTheme? = arrData?.get(position)
                if (data?.accessOption == AppConstants.BroadCastAccessOption.Private) {
                    createPrivateBroadCasting(context, getString(R.string.dialogDescriptionPassUserLiveJoin), false,
                        object : OnDialogPasswordListner {
                            override fun onButtonClick(view: View?, s: String?) {
                                apiConnectLive(data.id, s)
                            }

                            override fun onShareButtonClick(view: View?, s: String?) {

                            }
                        })
                } else {
                    apiConnectLive(data?.id, "")
                }
            }
        })
        recyclerviewCategory?.adapter = adapterHotTheme
        (recyclerviewCategory?.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (recyclerviewCategory?.adapter?.getItemViewType(position)) {
                    0 -> 1
                    else -> 2
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        api(true)
        recyclerviewCategory?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = (recyclerviewCategory?.layoutManager as GridLayoutManager).itemCount
                    lastVisibleItem = (recyclerviewCategory?.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    if (arrData != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) && (arrData?.size ?: 0) >= 20 ) {
                        loadData = true
                        arrData?.add(null)
                        adapterHotTheme?.notifyDataSetChanged()
                        pageNo = pageNo?.inc()
                        oldsize = arrData?.size
                        api(false)
                    }
                }
            }
        })
    }

    private fun api(progress: Boolean) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"

        val requestUserCategory: RequestBody = RequestBody.create(MediaType.parse("text/plain"), category?.toString() ?: AppConstants.Defaults.string)
        val requestCategory: RequestBody = RequestBody.create(MediaType.parse("text/plain"), subCategory?.toString() ?: "0")

        val apiService: MyApiEndpointInterface? = ApiClient(context).getClient()?.create(MyApiEndpointInterface::class.java)
        val callHotTheme: Call<JsonObject?>? = apiService?.callHotTheme(headers, pageNo.toString(), requestUserCategory, requestCategory)
        (context as DashBoardActivity).callApi(progress, callHotTheme, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, "record", JsonObject())
                        val bannerArray = getJsonArrayFromJson(record, "banner", JsonArray())
                        if (bannerArray?.size() ?: 0 > 0)
                            context?.let { context ->
                                imageViewCategoryBanner?.let { imageView ->
                                    Glide.with(context)
                                        .load(bannerArray?.get(0)?.asString)
                                        .into(imageView)
                                }
                            }

                        val dataCategoryArray = getJsonArrayFromJson(record, "category", JsonArray())

                        if (pageNo == 1) {
                            arrData?.clear()
                            arrFilter?.clear()
                            groupChip?.removeAllViews()
                            for (i in 0 until (dataCategoryArray?.size() ?: 0)) {
                                val dataCategory: JsonObject? = getJsonObjFromJson(dataCategoryArray, i, JsonObject())
                                val id = getIntFromJson(dataCategory, "id", AppConstants.Defaults.integer)
                                val name = getStringFromJson(dataCategory, "name", AppConstants.Defaults.string)
                                arrFilter?.add(HotThemeFilter(id, name))
                                if (subCategory == null) subCategory = id
                                val chip = this@CategoryFragment.layoutInflater.inflate(R.layout.dynamic_chip_item, null, false) as Chip
                                chip.id = id
                                chip.text = name
                                chip.isSelected = id == subCategory
                                chip.setOnClickListener {
                                    subCategory = arrFilter?.get(i)?.id
                                    api(true)
                                }
                                groupChip.addView(chip)
                            }
                        }

                        if (arrData?.contains(null) == true) arrData?.remove(null)

                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        for (i in 0 until dataArray?.size()!!) {
                            val data: JsonObject? = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id: String = getStringFromJson(data, "id", AppConstants.Defaults.string)
                            val userId: String = getStringFromJson(data, "user_id", AppConstants.Defaults.string)
                            val title: String = getStringFromJson(data, "title", AppConstants.Defaults.string)
                            val accessOption: String = getStringFromJson(data, "access_option", AppConstants.Defaults.string)
                            val channelId: String = getStringFromJson(data, "channel_id", AppConstants.Defaults.string)
                            val hashtags: String = getStringFromJson(data, "hashtags", AppConstants.Defaults.string)
                            val status: String = getStringFromJson(data, "status", AppConstants.Defaults.string)
                            val startTime: String = getStringFromJson(data, "start_time", AppConstants.Defaults.string)
                            val userName: String = getStringFromJson(data, "user_name", AppConstants.Defaults.string)
                            val userScore: Int = getIntFromJson(data, "user_score", AppConstants.Defaults.integer)
                            val userProfile: String = getStringFromJson(data, "user_profile", AppConstants.Defaults.string)
                            val rtcToken: String = getStringFromJson(data, "rtc_token", AppConstants.Defaults.string)
                            val rtmToken: String = getStringFromJson(data, "rtm_token", AppConstants.Defaults.string)
                            arrData?.add(
                                HotTheme(
                                    id, userId, title, channelId, hashtags, status, startTime,
                                    userName, userScore, userProfile, rtcToken, rtmToken, accessOption
                                )
                            )
                        }

                        adapterHotTheme?.notifyDataSetChanged()
//                        if (pageNo == 1) {
//                            adapterHotTheme?.notifyDataSetChanged()
//                            group_data?.removeAllViews()
//                            setChipsData()
//                        } else {
////                            if (oldsize != arrHome?.size!! )
////                                adapterHome?.notifyItemRangeInserted(oldsize!!, arrHome?.size!! - 1)
//                            adapterHotTheme?.notifyDataSetChanged()
//                        }
                        if ((pageNo ?: 0) > 1) loadData = false

                        if ((context as DashBoardActivity).dialog?.isShowing == true) (context as DashBoardActivity).dialog?.dismiss()
                    }
                }
            }

            override fun onFailure() {

            }
        })
    }

    fun apiConnectLive(stremeId: String?, password: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"
        val apiService: MyApiEndpointInterface? = ApiClient(context).getClient()?.create(MyApiEndpointInterface::class.java)
        val requestPassword: RequestBody = RequestBody.create(MediaType.parse("text/plain"), password ?: AppConstants.Defaults.string)
        val callConnectlive: Call<JsonObject?>? = apiService?.callConnectLive(headers, stremeId, requestPassword)

        (context as DashBoardActivity).callApi(true, callConnectlive, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val id: String = getStringFromJson(record, "id", AppConstants.Defaults.string)
                        val channelId: String = getStringFromJson(record, "channel_id", AppConstants.Defaults.string)
                        val userId: String = getStringFromJson(record, "user_id", AppConstants.Defaults.string)
                        val title: String = getStringFromJson(record, "title", AppConstants.Defaults.string)
                        val description: String = getStringFromJson(record, "discription", AppConstants.Defaults.string)
                        val hashTags: String = getStringFromJson(record, "hashtags", AppConstants.Defaults.string)
                        val category: String = getStringFromJson(record, "category", AppConstants.Defaults.string)
                        val startTime: String = getStringFromJson(record, "start_time", AppConstants.Defaults.string)
                        val endTime: String = getStringFromJson(record, "end_time", AppConstants.Defaults.string)
                        val status: String = getStringFromJson(record, "status", AppConstants.Defaults.string)
                        val addedOn: String = getStringFromJson(record, "added_on", AppConstants.Defaults.string)
                        val updateOn: String = getStringFromJson(record, "update_on", AppConstants.Defaults.string)
                        val streamerRanking: String = getStringFromJson(record, "streamer_ranking", AppConstants.Defaults.string)
                        val streamerStatus: String = getStringFromJson(record, "streamer_status", AppConstants.Defaults.string)
                        val followStatus: String = getStringFromJson(record, "follow_status", AppConstants.Defaults.string)
                        val profileNotes: String = getStringFromJson(record, "profile_notes", AppConstants.Defaults.string)
                        val totalGifts: String = getStringFromJson(record, "total_gifts", AppConstants.Defaults.string)
                        val streamerId: String = getStringFromJson(record, "streamer_id", AppConstants.Defaults.string)
                        val streamerName: String = getStringFromJson(record, "streamer_name", AppConstants.Defaults.string)
                        val streamerProfile: String = getStringFromJson(record, "streamer_profile", AppConstants.Defaults.string)
                        val like_status: String = getStringFromJson(record, "like_status", AppConstants.Defaults.string)
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
                            streamerStatus, followStatus, like_status, profileNotes, totalGifts,
                            streamerId, streamerName, streamerProfile, rtcToken, rtmToken, userAccount,
                            arrLiveUserConnected,
                            arrAutoMessage, arrLiveScheduleHotTheme
                        )
                        (context as DashBoardActivity).setConfig(channelId, rtcToken, rtmToken, streamerId)
                        startActivity(LiveActivity.getInstance(activity?.applicationContext!!, liversProfile, false))
//                        startActivity(LiveTwoActivity.getInstance(activity?.applicationContext!!,  liversProfile,false))
//                        val intent = Intent(activity?.intent)
//                        intent.putExtra(AppConstants.KEY_CLIENT_ROLE, Constants.CLIENT_ROLE_BROADCASTER)
//                        intent.setClass(activity?.applicationContext!!, LiveActivityDemo::class.java)
//                        startActivity(intent)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if ((context as DashBoardActivity).dialog?.isShowing == true)
                    (context as DashBoardActivity).dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }


}