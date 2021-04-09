package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.mmfinfotech.streameApp.dashBoard.live.activity.UserMoreDetailActivity
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterCommonForThree
import com.mmfinfotech.streameApp.models.FollowUnFollowResponse
import com.mmfinfotech.streameApp.models.FollowerListResponse
import com.mmfinfotech.streameApp.models.Following
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.RecyclerTouchListener
import kotlinx.android.synthetic.main.activity_follow_n_like.*
import kotlinx.android.synthetic.main.activity_play_streaming.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class FollowNLikeActivity : DashBoardBaseActivity() {
    private val TAG: String? = FollowNLikeActivity::class.java.simpleName
    private var arrFollowNLike: ArrayList<Following?>? = null
    private var adapterCommon: AdapterCommonForThree? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var intentFrom: String? = AppConstants.Defaults.string
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var oldsize: Int? = null
    private var recyclerTouchListener: RecyclerTouchListener? = null

    companion object {
        const val ACTION_FOLLOWER = "action_Follower"
        const val ACTION_FOLLOWING = "action_Following"
        const val ACTION_LIKE = "action_Like"
        const val ACTION_MYCOLLECTION = "action_mycollection"
        const val ACTION_ANNONYMOUSCHARING = "action_annonymouscharing"
        fun getInstance(
                context: Context?,
                action: String?
        ) = Intent(context, FollowNLikeActivity::class.java).apply {
            setAction(action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_n_like)
        arrFollowNLike = ArrayList()
        intentFrom = intent?.action
        initView()
        setListners()
        setAdapter(arrFollowNLike)
    }

    private fun setListners() {
        findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<ImageView>(R.id.imageToolbarBack)?.setOnClickListener {
            onBackPressed()
        }
        recyclerview_follow_frag.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = mLayoutManager?.getItemCount()
                    lastVisibleItem = mLayoutManager?.findLastVisibleItemPosition()
                    if (arrFollowNLike != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) &&
                            (arrFollowNLike?.size ?: 0) >= 20) {
                        loadData = true
                        arrFollowNLike?.add(null)
                        adapterCommon?.notifyDataSetChanged()
//                        getDynDataFromServer(20, index - 1, true, true)
                        pageNo = pageNo?.inc()
                        oldsize = arrFollowNLike?.size
                        Log.v(TAG, "callServerForData from recyclerview Lisner")
                        Log.v(TAG, "Calllling with wrong page ${pageNo}")
                        when (intentFrom) {
                            ACTION_FOLLOWER -> {
                               callUserFollowerApi()
                            }
                            ACTION_FOLLOWING -> {
//                                callUserFollowingApi()
                            }
                            ACTION_LIKE -> {

                            }
                        }
                    }
                }
            }
        })
    }

    private fun initView() {
        when (intentFrom) {
            ACTION_FOLLOWER -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.follower)
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.visibility = View.GONE
                callUserFollowerApi()
            }
            ACTION_FOLLOWING -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.following)
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.visibility = View.GONE
                callUserFollowingApi()
            }

            ACTION_LIKE -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.like)
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.visibility = View.GONE

            }
            ACTION_MYCOLLECTION -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.my_collection)
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.visibility = View.GONE
            }
            ACTION_ANNONYMOUSCHARING -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.anonymous_cheering)
                findViewById<ConstraintLayout>(R.id.includeToolbarFollowLlikeAnnonyChering)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.setImageDrawable(getDrawable(R.drawable.ic_help_))
            }
        }
    }

    private fun setAdapter(arrFollowNLike: ArrayList<Following?>?) {
        mLayoutManager = recyclerview_follow_frag.layoutManager as LinearLayoutManager
        adapterCommon = AdapterCommonForThree(
                this@FollowNLikeActivity,
                arrFollowNLike, intentFrom,
                object : AdapterCommonForThree.OnFllowLikeListner {
                    override fun onFollower(position: Int, textFollowTitle: TextView) {
                                callFollowUnFollow(arrFollowNLike?.get(position)?.user_id, position,textFollowTitle)
                    }

                    override fun onFollowing(position: Int, textFollowTitle: TextView) {
//                        callFollowUnFollow(arrFollowNLike?.get(position)?.follow_id, position, textFollowTitle)
                    }

                    override fun onLike(position: Int) {
                    }

                    override fun onAnnonymouseClicking(position: Int) {
                        annonymouseDialog(this@FollowNLikeActivity)
                    }
                    override fun onProfileClicking(position: Int) {
                        startActivity(UserMoreDetailActivity.getInstance(this@FollowNLikeActivity, arrFollowNLike?.get(position)?.user_id))
//                        if (intentFrom == ACTION_FOLLOWER) {
//                            startActivity(UserMoreDetailActivity.getInstance(this@FollowNLikeActivity, arrFollowNLike?.get(position)?.user_id))
//                        } else {
//                            startActivity(UserMoreDetailActivity.getInstance(this@FollowNLikeActivity, arrFollowNLike?.get(position)?.follow_id))
//                        }
                    }
                })
        recyclerview_follow_frag.adapter = adapterCommon
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callUserFollowerApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@FollowNLikeActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callMyPageFollowers: Call<FollowerListResponse?>? = apiService?.callMypagFollower(headers)
        callRemoteApi(true, callMyPageFollowers, object : ApiClient.ApiCallbackListener<FollowerListResponse> {
            override fun onDataFetched(response: FollowerListResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@FollowNLikeActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<FollowerListResponse> {
                        override fun onSuccess(response: FollowerListResponse) {
                            val followers = response.record?.data ?: return
                            for (follower in followers) {
                                arrFollowNLike?.add(Following(follower.id?.toString(),
                                    follower.user_id,
                                    "",
                                    follower.added_on,
                                    follower.name,
                                    follower.profile,
                                    follower.profile_status))
                            }
                            setAdapter(arrFollowNLike)
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(this@FollowNLikeActivity, message, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        })
    }

    private fun callUserFollowingApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"

        val apiService: MyApiEndpointInterface? =
                ApiClient(this@FollowNLikeActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callMyPagFollowings: Call<FollowerListResponse?>? = apiService?.callMypagFollowing(headers)

        callRemoteApi(true, callMyPagFollowings, object : ApiClient.ApiCallbackListener<FollowerListResponse> {
            override fun onDataFetched(response: FollowerListResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@FollowNLikeActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<FollowerListResponse> {
                        override fun onSuccess(response: FollowerListResponse) {
                            val followers = response.record?.data ?: return
                            for (follower in followers) {
                                arrFollowNLike?.add(Following(follower.id?.toString(),
                                    follower.user_id,
                                    "",
                                    follower.added_on,
                                    follower.name,
                                    follower.profile,
                                    follower.profile_status))
                            }
                            setAdapter(arrFollowNLike)
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(this@FollowNLikeActivity, message, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        })
    }

    fun callFollowUnFollow(id: String?, position: Int?, textView: TextView?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@FollowNLikeActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@FollowNLikeActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callFollowUnFollow: Call<FollowUnFollowResponse?>? = apiService?.callFollowUnFollow(headers, id)

        callRemoteApi(true, callFollowUnFollow, object : ApiClient.ApiCallbackListener<FollowUnFollowResponse> {
            override fun onDataFetched(response: FollowUnFollowResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@FollowNLikeActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<FollowUnFollowResponse> {
                        override fun onSuccess(response: FollowUnFollowResponse) {
                            if (response.record?.follow_status == "0") {
                                arrFollowNLike?.remove(arrFollowNLike?.get(position!!))
                                adapterCommon?.notifyDataSetChanged()
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(this@FollowNLikeActivity, message, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        })
    }
}