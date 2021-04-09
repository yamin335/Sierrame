package com.mmfinfotech.streameApp.dashBoard.live.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterStreamerFolloweNLike
import com.mmfinfotech.streameApp.models.FollowUnFollowResponse
import com.mmfinfotech.streameApp.models.Following
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.RecyclerTouchListener
import kotlinx.android.synthetic.main.activity_live.*
import kotlinx.android.synthetic.main.activity_stremer_follow_n_like.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class StremerFollowNLikeActivity : DashBoardBaseActivity() {
    private val TAG: String? = StremerFollowNLikeActivity::class.java.simpleName
    private var arrFollowNLike: ArrayList<Following?>? = null
    private var adapterFollowLike: AdapterStreamerFolloweNLike? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var intentFrom: String? = AppConstants.Defaults.string
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var StreamerId: String? = null
    private var oldsize: Int? = null
    private var recyclerTouchListener: RecyclerTouchListener? = null

    companion object {
        const val ACTION_Stremer_FOLLOWER = "action_Stremer_Follower"
        const val ACTION_Stremer_FOLLOWING = "action_Stremer_Following"
        const val ACTION_Stremer_LIKE = "action_Stremer_Like"

        fun getInstance(
                context: Context?,
                action: String?,
                StremerUserId: String?
        ) = Intent(context, StremerFollowNLikeActivity::class.java).apply {
            setAction(action)
            putExtra(AppConstants.IntentExtras.HotStremeId, StremerUserId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stremer_follow_n_like)
        arrFollowNLike = ArrayList()
        intentFrom = intent?.action
        StreamerId = intent.getStringExtra(AppConstants.IntentExtras.HotStremeId)

        initView()
        setListners()
        setAdapter(arrFollowNLike)
    }

    private fun setListners() {
        findViewById<ConstraintLayout>(R.id.includeToolbarStremerFollowing)?.findViewById<ImageView>(R.id.imageToolbarBack)?.setOnClickListener {
            onBackPressed()
        }
        recyclerview_follow_frag_streme.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = mLayoutManager?.getItemCount()
                    lastVisibleItem = mLayoutManager?.findLastVisibleItemPosition()
                    if (arrFollowNLike != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) &&
                            (arrFollowNLike?.size ?: 0) >= 20) {
                        loadData = true
                        arrFollowNLike?.add(null)
                        adapterFollowLike?.notifyDataSetChanged()
                        pageNo = pageNo?.inc()
                        oldsize = arrFollowNLike?.size
                         when (intentFrom) {
                            ACTION_Stremer_FOLLOWER -> {

                            }
                            ACTION_Stremer_FOLLOWING -> {

                            }
                            ACTION_Stremer_LIKE -> {
                            }
                        }
                    }
                }
            }
        })
    }

    private fun initView() {
        when (intentFrom) {
            ACTION_Stremer_FOLLOWER -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarStremerFollowing)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.follower)
                findViewById<ConstraintLayout>(R.id.includeToolbarStremerFollowing)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.visibility = View.GONE
                callStremerFollowerApi()
            }
            ACTION_Stremer_FOLLOWING -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarStremerFollowing)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.following)
                findViewById<ConstraintLayout>(R.id.includeToolbarStremerFollowing)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.visibility = View.GONE
                callStreamerFollowingApi()
            }

            ACTION_Stremer_LIKE -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarStremerFollowing)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.like)
                findViewById<ConstraintLayout>(R.id.includeToolbarStremerFollowing)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.visibility = View.GONE
            }
        }

    }

    private fun setAdapter(arrFollowNLike: ArrayList<Following?>?) {
        mLayoutManager = recyclerview_follow_frag_streme.layoutManager as LinearLayoutManager
        adapterFollowLike =
                AdapterStreamerFolloweNLike(
                        this@StremerFollowNLikeActivity,
                        arrFollowNLike, intentFrom,
                        object : AdapterStreamerFolloweNLike.OnFllowLikeListner {
                            override fun onFollower(position: Int, textFollowTitle: TextView) {
//                                callFollowUnFollow(arrFollowNLike?.get(position)?.user_id, position, textFollowTitle)
                            }

                            override fun onFollowing(position: Int, textFollowTitle: TextView) {
                          }


                            override fun onLike(position: Int) {

                            }

                            override fun onProfileClick(position: Int) {
                                startActivity(UserMoreDetailActivity.getInstance(this@StremerFollowNLikeActivity, arrFollowNLike?.get(position)?.user_id))
//
//                                if (intentFrom == FollowNLikeActivity.ACTION_FOLLOWER) {
//                                    startActivity(UserMoreDetailActivity.getInstance(this@StremerFollowNLikeActivity, arrFollowNLike?.get(position)?.user_id))
//                                } else {
//                                    startActivity(UserMoreDetailActivity.getInstance(this@StremerFollowNLikeActivity, arrFollowNLike?.get(position)?.follow_id))
//                                }
                            }
                        }
                )

        recyclerview_follow_frag_streme.adapter = adapterFollowLike
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callStremerFollowerApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? =
                ApiClient(this@StremerFollowNLikeActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callstremerFollower: Call<JsonObject?>? = apiService?.callstremerFollower(headers, StreamerId.toString())
        callApi(true, callstremerFollower, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
//                        Toast.makeText(this@FollowNLikeActivity, "${msg}", Toast.LENGTH_LONG).show()
                        for (i in 0 until dataArray!!.size()) {
                            val FollowingObject = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id = getStringFromJson(FollowingObject, "id", AppConstants.Defaults.string)
                            val user_id = getStringFromJson(FollowingObject, "user_id", AppConstants.Defaults.string)
                            val follow_id = getStringFromJson(FollowingObject, "follow_id", AppConstants.Defaults.string)
                            val added_on = getStringFromJson(FollowingObject, "added_on", AppConstants.Defaults.string)
                            val name = getStringFromJson(FollowingObject, "name", AppConstants.Defaults.string)
                            val profile = getStringFromJson(FollowingObject, "profile", AppConstants.Defaults.string)
                            val profile_status = getStringFromJson(FollowingObject, "profile_status", AppConstants.Defaults.string)

                            arrFollowNLike?.add(Following(id,
                                    user_id,
                                    follow_id,
                                    added_on,
                                    name,
                                    profile,
                                    profile_status))

                        }
                        setAdapter(arrFollowNLike)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@StremerFollowNLikeActivity, msg)
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

    private fun callStreamerFollowingApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"

        val apiService: MyApiEndpointInterface? =
                ApiClient(this@StremerFollowNLikeActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callStreamerFollowing: Call<JsonObject?>? = apiService?.callStremerFollowing(headers, StreamerId.toString())
        callApi(true, callStreamerFollowing, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        for (i in 0 until dataArray!!.size()) {
                            val FollowingObject = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id = getStringFromJson(FollowingObject, "id", AppConstants.Defaults.string)
                            val user_id = getStringFromJson(FollowingObject, "user_id", AppConstants.Defaults.string)
                            val follow_id = getStringFromJson(FollowingObject, "follow_id", AppConstants.Defaults.string)
                            val added_on = getStringFromJson(FollowingObject, "added_on", AppConstants.Defaults.string)
                            val name = getStringFromJson(FollowingObject, "name", AppConstants.Defaults.string)
                            val profile = getStringFromJson(FollowingObject, "profile", AppConstants.Defaults.string)
                            val profile_status = getStringFromJson(FollowingObject, "profile_status", AppConstants.Defaults.string)

                            arrFollowNLike?.add(Following(id,
                                    user_id,
                                    follow_id,
                                    added_on,
                                    name,
                                    profile,
                                    profile_status))
                        }

                        setAdapter(arrFollowNLike)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@StremerFollowNLikeActivity, "${msg}", Toast.LENGTH_LONG).show()
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

    fun callFollowUnFollow(id: String?, position: Int?, textView: TextView?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@StremerFollowNLikeActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@StremerFollowNLikeActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callFollowUnFollow: Call<FollowUnFollowResponse?>? = apiService?.callFollowUnFollow(headers, id)

        callRemoteApi(true, callFollowUnFollow, object : ApiClient.ApiCallbackListener<FollowUnFollowResponse> {
            override fun onDataFetched(response: FollowUnFollowResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@StremerFollowNLikeActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<FollowUnFollowResponse> {
                        override fun onSuccess(response: FollowUnFollowResponse) {
                            if (response.record?.follow_status == "0") {
                                arrFollowNLike?.remove(arrFollowNLike?.get(position!!))
                                adapterFollowLike?.notifyDataSetChanged()
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(this@StremerFollowNLikeActivity, message, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        })
    }
}