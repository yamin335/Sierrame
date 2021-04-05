package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.activity.UserMoreDetailActivity
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterLiveSchadule
import com.mmfinfotech.streameApp.models.LiveSChedule
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_follow_n_like.*
import kotlinx.android.synthetic.main.activity_live_scadule.*
import kotlinx.android.synthetic.main.fragment_profile_page.*
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LiveScaduleActivity : DashBoardBaseActivity() {
    val Tag: String? = LiveScaduleActivity::class.java.simpleName

    private var mLayoutManager: LinearLayoutManager? = null
    private var arrLiveScadule: ArrayList<LiveSChedule?>? = null
    private var adapterLiveSchadule: AdapterLiveSchadule? = null
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var oldsize: Int? = null
    private var query: String? = null

    var currentDate: String? = null

    companion object {
        fun getInstance(context: Context?) = Intent(context, LiveScaduleActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_scadule)
        arrLiveScadule = ArrayList()
        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        initView()
        apiLiveSchedule(currentDate)
        setScheduleListners()
        setListners()
        setAdapter(arrLiveScadule)
    }

    private fun setListners() {
        findViewById<ConstraintLayout>(R.id.includeToolbarLiveSceduling)?.findViewById<ImageView>(R.id.imageToolbarBack)?.setOnClickListener {
            onBackPressed()
        }
        recyclerLivescaduling.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = mLayoutManager?.getItemCount()
                    lastVisibleItem = mLayoutManager?.findLastVisibleItemPosition()
                    if (arrLiveScadule != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) &&
                            (arrLiveScadule?.size ?: 0) >= 20) {
                        loadData = true
                        arrLiveScadule?.add(null)
                        adapterLiveSchadule?.notifyDataSetChanged()
//                        getDynDataFromServer(20, index - 1, true, true)
                        pageNo = pageNo?.inc()
                        oldsize = arrLiveScadule?.size
//                        callApi()
                    }
                }
            }
        })

    }

    private fun setScheduleListners() {
        txtViewToday.setOnClickListener {
            toggleView(it)
            apiLiveSchedule(currentDate)
        }
        txtViewDate1.setOnClickListener {
            toggleView(it)
            apiLiveSchedule(getSendDate(1))
        }
        txtViewDate2.setOnClickListener {
            toggleView(it)
            apiLiveSchedule(getSendDate(2))
        }
        txtViewDate3.setOnClickListener {
            toggleView(it)
            apiLiveSchedule(getSendDate(3))
        }
        txtViewDate4.setOnClickListener {
            toggleView(it)
            apiLiveSchedule(getSendDate(4))
        }
        txtViewDate5.setOnClickListener {
            toggleView(it)
            apiLiveSchedule(getSendDate(5))
        }
        txtViewDate6.setOnClickListener {
            toggleView(it)
            apiLiveSchedule(getSendDate(6))
        }
    }

    private fun toggleView(v: View) {
        txtViewToday.isSelected = txtViewToday.id == v.id
        txtViewDate1.isSelected = txtViewDate1.id == v.id
        txtViewDate2.isSelected = txtViewDate2.id == v.id
        txtViewDate3.isSelected = txtViewDate3.id == v.id
        txtViewDate4.isSelected = txtViewDate4.id == v.id
        txtViewDate5.isSelected = txtViewDate5.id == v.id
        txtViewDate6.isSelected = txtViewDate6.id == v.id

        txtViewWeek.isSelected = txtViewToday.id == v.id
        txtViewWeek1.isSelected = txtViewDate1.id == v.id
        txtViewWeek2.isSelected = txtViewDate2.id == v.id
        txtViewWeek3.isSelected = txtViewDate3.id == v.id
        txtViewWeek4.isSelected = txtViewDate4.id == v.id
        txtViewWeek5.isSelected = txtViewDate5.id == v.id
        txtViewWeek6.isSelected = txtViewDate6.id == v.id
    }

    private fun apiLiveSchedule(date: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LiveScaduleActivity)}"
        val sendProperty: JsonObject? = JsonObject()
        sendProperty?.addProperty("date", date)
        val apiService: MyApiEndpointInterface? = ApiClient(this@LiveScaduleActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callLiveSchedul: Call<JsonObject?>? = apiService?.callGetLiveSchedul(headers, sendProperty)
        callApi(true, callLiveSchedul, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        arrLiveScadule = ArrayList()
                        val record = getJsonArrayFromJson(mainObject, record, JsonArray())
                        for (i in 0 until record?.size()!!) {
                            val liveScheduleObj = getJsonObjFromJson(record, i, JsonObject())
                            val id = getStringFromJson(liveScheduleObj, "id", AppConstants.Defaults.string)
                            val user_id = getStringFromJson(liveScheduleObj, "user_id", AppConstants.Defaults.string)
                            val title = getStringFromJson(liveScheduleObj, "title", AppConstants.Defaults.string)
                            val description = getStringFromJson(liveScheduleObj, "description", AppConstants.Defaults.string)
                            val date = getStringFromJson(liveScheduleObj, "date", AppConstants.Defaults.string)
                            val date_time = getStringFromJson(liveScheduleObj, "date_time", AppConstants.Defaults.string)
                            val status = getStringFromJson(liveScheduleObj, "status", AppConstants.Defaults.string)
                            val added_on = getStringFromJson(liveScheduleObj, "added_on", AppConstants.Defaults.string)
                            val update_on = getStringFromJson(liveScheduleObj, "update_on", AppConstants.Defaults.string)
                            val name = getStringFromJson(liveScheduleObj, "name", AppConstants.Defaults.string)
                            val profile = getStringFromJson(liveScheduleObj, "profile", AppConstants.Defaults.string)
                            val time = getStringFromJson(liveScheduleObj, "time", AppConstants.Defaults.string)
                            arrLiveScadule?.add(LiveSChedule(id,
                                    user_id,
                                    title,
                                    description,
                                    date,
                                    date_time,
                                    status,
                                    added_on,
                                    update_on,
                                    name,
                                    profile,
                                    time))
                        }
                        setAdapter(arrLiveScadule)
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    private fun initView() {
        findViewById<ConstraintLayout>(R.id.includeToolbarLiveSceduling)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.live_schedule)
        txtViewDate1.text = getDateAndMonthe(1)
        txtViewDate2.text = getDateAndMonthe(2)
        txtViewDate3.text = getDateAndMonthe(3)
        txtViewDate4.text = getDateAndMonthe(4)
        txtViewDate5.text = getDateAndMonthe(5)
        txtViewDate6.text = getDateAndMonthe(6)

        txtViewWeek.text = getweek(this@LiveScaduleActivity,0)
        txtViewWeek1.text = getweek(this@LiveScaduleActivity,1)
        txtViewWeek2.text = getweek(this@LiveScaduleActivity,2)
        txtViewWeek3.text = getweek(this@LiveScaduleActivity,3)
        txtViewWeek4.text = getweek(this@LiveScaduleActivity,4)
        txtViewWeek5.text = getweek(this@LiveScaduleActivity,5)
        txtViewWeek6.text = getweek(this@LiveScaduleActivity,6)
        txtViewToday.isSelected = true
        txtViewWeek.isSelected = true
    }

    private fun setAdapter(arrLiveScadule: ArrayList<LiveSChedule?>?) {
        mLayoutManager = recyclerLivescaduling.layoutManager as LinearLayoutManager
        adapterLiveSchadule = AdapterLiveSchadule(
                this@LiveScaduleActivity,
                arrLiveScadule,
                object : AdapterLiveSchadule.OnLiveSchaduleListner {
                    override fun liveProfileListner(position: Int) {
//                        ApiCallingProfile(arrLiveScadule?.get(position)?.user_id)
                        startActivity(UserMoreDetailActivity.getInstance(
                                this@LiveScaduleActivity, arrLiveScadule?.get(position)?.user_id))
                    }
                })

        recyclerLivescaduling.adapter = adapterLiveSchadule
    }

}