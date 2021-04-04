package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.base.NetworkBaseActivity
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.BlockedUserAdapter
import com.mmfinfotech.streameApp.model.BlockedUser
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import kotlinx.android.synthetic.main.activity_blocked_user.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class BlockedUserActivity : NetworkBaseActivity() {
    private val TAG: String? = BlockedUserActivity::class.java.simpleName
    private var arrayBlockedUser: ArrayList<BlockedUser?>? = null
    private var adapter: BlockedUserAdapter? = null

    private var mLayoutManager: LinearLayoutManager? = null
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var oldsize: Int? = null

    companion object {
        fun getInstance(context: Context?) = Intent(context, BlockedUserActivity::class.java).apply { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_user)

        findViewById<ConstraintLayout>(R.id.includeToolbarBlockedUser)?.apply {
            findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.txt_block_user)
            findViewById<ImageView>(R.id.imageToolbarBack)?.setOnClickListener { onBackPressed() }
        }

        arrayBlockedUser = ArrayList()
        adapter = BlockedUserAdapter(this@BlockedUserActivity, arrayBlockedUser, object : BlockedUserAdapter.OnBlockedUserListener {
            override fun onUnBlockUser(p: Int) {
                blockUser(p)
            }
        })
        recyclerViewBlockedUser.adapter = adapter

        callSearchApi()
    }

    private fun callSearchApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${appPreferences?.getAuthToken(this)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(MyApiEndpointInterface::class.java)

        val callBlockedUser: Call<JsonObject?>? = apiService?.callBlockUserList(headers)
        callApi(true, callBlockedUser, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        if (pageNo == 1) {
                            arrayBlockedUser?.clear()
                        }
                        for (i in 0 until dataArray?.size()!!) {
                            val recordData = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id = getStringFromJson(recordData, "id", AppConstants.Defaults.string)
                            val blockedUserId = getStringFromJson(recordData, "blocked_user_id", AppConstants.Defaults.string)
                            val blockedBy = getStringFromJson(recordData, "blocked_by", AppConstants.Defaults.string)
                            val _status = getStringFromJson(recordData, "status", AppConstants.Defaults.string)
                            val blockedUserProfile = getStringFromJson(recordData, "blocked_user_profile", AppConstants.Defaults.string)
                            val blockedUserName = getStringFromJson(recordData, "blocked_user_name", AppConstants.Defaults.string)
                            val blockedUserProfileStatus = getStringFromJson(recordData, "blockedUserProfile_status", AppConstants.Defaults.string)

                            arrayBlockedUser?.add(
                                BlockedUser(id, blockedUserId, blockedBy, _status, blockedUserProfile, blockedUserName, blockedUserProfileStatus)
                            )
                        }
                        adapter?.notifyDataSetChanged()
//                        if (pageNo!! > 1) {
//                            loadData = false
//                        }
                    }
                    NotFound -> {
                        arrayBlockedUser?.clear()
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@BlockedUserActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    private fun blockUser(p: Int) {
        val userId = arrayBlockedUser?.get(p)?.blockedUserId

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${appPreferences?.getAuthToken(this@BlockedUserActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@BlockedUserActivity).getClient()?.create(MyApiEndpointInterface::class.java)

        val callBlockUser: Call<JsonObject?>? = apiService?.callBlockUser(headers, userId)
        callApi(true, callBlockUser, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val message: String = getStringFromJson(mainObject, "message", AppConstants.Defaults.string)
                        Toast.makeText(this@BlockedUserActivity, message, Toast.LENGTH_SHORT).show()
                        arrayBlockedUser?.removeAt(p)
                        adapter?.notifyItemRemoved(p)
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }
}