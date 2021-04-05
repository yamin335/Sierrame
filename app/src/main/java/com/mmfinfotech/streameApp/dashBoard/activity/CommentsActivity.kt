package com.mmfinfotech.streameApp.dashBoard.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.adapter.AdapterComments
import com.mmfinfotech.streameApp.models.CommentResponse
import com.mmfinfotech.streameApp.models.CommentsChet
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_comments.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class CommentsActivity : DashBoardBaseActivity() {
    private var arrComments: ArrayList<CommentsChet?>? = ArrayList()
    private var adapterComments: AdapterComments? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var loadData: Boolean = false
    private var pageNo: Int? = 1
    private var refrenceId: String? = null

    val handler: Handler = Handler()

    companion object {
        fun getInstance(context: Context?, refrenceId: String?) = Intent(context, CommentsActivity::class.java).apply {
            putExtra(AppConstants.IntentExtras.selectCommentsRefrenceId, refrenceId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        refrenceId = intent.getStringExtra(AppConstants.IntentExtras.selectCommentsRefrenceId)

        callCommentsApi(refrenceId)
        setAdapter()
        setListners()
    }

    private fun setAdapter() {
        recyclerview_comments_streme.adapter = adapterComments
        mLayoutManager = recyclerview_comments_streme.layoutManager as LinearLayoutManager
        adapterComments = AdapterComments(this@CommentsActivity, arrComments, object : AdapterComments.OnCommentsListner {
            override fun onCommentsClick(p: Int) {}
            override fun onDeleteClick(p: Int) {
                deleteComment(p)
            }
        })
        recyclerview_comments_streme.adapter = adapterComments
        if (arrComments?.size != 0) recyclerview_comments_streme.smoothScrollToPosition(
            arrComments?.size?.minus(1)!!
        )
    }

    private fun setListners() {
        btn_send.setOnClickListener {
            if (validationEmptyField(this@CommentsActivity, edtTextComments) == true) {
                AddCommentsApi()
            } else {
            }
        }
        ImageButtonBack.setOnClickListener {
            onBackPressed()
            recyclerview_comments_streme.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lm: LinearLayoutManager? = recyclerView.layoutManager as LinearLayoutManager?
                    val firstVisibleItemPosition: Int? = lm?.findFirstVisibleItemPosition()

                    if (!loadData && arrComments?.isEmpty() == false && firstVisibleItemPosition!! <= 5) {
                        loadData = true
                    }
                }
            })
        }

//        recyclerview_comments_streme.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                    totalItemCount = mLayoutManager?.getItemCount()
//                    if (!recyclerView.canScrollVertically(1)) {
//                    lastVisibleItem = mLayoutManager?.findLastVisibleItemPosition()
//                    if (arrComments != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) &&
//                        (arrComments?.size ?: 0) >= 20) {
//                        loadData = true
//                        arrComments?.add(null)
//                        adapterComments?.notifyDataSetChanged()
////                        getDynDataFromServer(20, index - 1, true, true)
//                        pageNo = pageNo?.inc()
//                        oldsize = arrComments?.size
////                        callCommentsApi(pageNo)
//                    }
//                }
//            }
//        })
    }

    private fun AddCommentsApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("refrence_id", refrenceId)
        sendParams?.addProperty("type", AppConstants.LikeTypes.TypePost)
        sendParams?.addProperty("comment", edtTextComments.text.toString())

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callStreamPost: Call<CommentResponse?>? = apiService?.callAddComments(headers, sendParams)
        callRemoteApi(true, callStreamPost, object : ApiClient.ApiCallbackListener<CommentResponse> {
            override fun onDataFetched(response: CommentResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@CommentsActivity, response?.status, response?.message ?: message, response, object : ApiClient.ApiResponseListener<CommentResponse> {
                    override fun onSuccess(response: CommentResponse) {
                        edtTextComments.setText("")
                        callCommentsApi(refrenceId)
                    }

                    override fun onFailed(status: String, message: String) {
                        Toast.makeText(this@CommentsActivity, message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        })
    }

    fun callCommentsApi(RefrenceId: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@CommentsActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callComments: Call<JsonObject?>? = apiService?.callGetAllComments(headers, RefrenceId.toString(), AppConstants.LikeTypes.TypePost)
        callApi(true, callComments, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        if (pageNo == 1) {
                            arrComments?.clear()
                        }
                        val arrTemp: ArrayList<CommentsChet?> = ArrayList()
                        for (i in 0 until dataArray?.size()!!) {
                            val recordData = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id = recordData?.get("id")?.asInt.toString()
                            val user_id = getStringFromJson(recordData, "user_id", AppConstants.Defaults.string)
                            val refrence_id = getStringFromJson(recordData, "refrence_id", AppConstants.Defaults.string)
                            val owner_id = getIntFromJson(recordData, "owner_id", AppConstants.Defaults.integer)
                            val comment = getStringFromJson(recordData, "comment", AppConstants.Defaults.string)
                            val type = getStringFromJson(recordData, "type", AppConstants.Defaults.string)
                            val status = getStringFromJson(recordData, "status", AppConstants.Defaults.string)
                            val added_on = getStringFromJson(recordData, "added_on", AppConstants.Defaults.string)
                            val update_on = recordData?.get("update_on")?.asInt.toString()
                            val user_name = getStringFromJson(recordData, "user_name", AppConstants.Defaults.string)
                            val user_profile = getStringFromJson(recordData, "user_profile", AppConstants.Defaults.string)
                            val amazonaws = getStringFromJson(recordData, "amazonaws", AppConstants.Defaults.string)
                            val profile_status = getStringFromJson(recordData, "profile_status", AppConstants.Defaults.string)

                            arrTemp.add(
                                CommentsChet(
                                    id, user_id, refrence_id, comment, type, status, added_on, "${update_on}000", user_name,
                                    user_profile, amazonaws, profile_status, owner_id
                                )
                            )
                        }
                        arrTemp.reverse()
                        arrComments?.addAll(0, arrTemp)
//                        recyclerview_comments_streme.smoothScrollToPosition(
////                            arrComments?.size?.minus(1)!!
////                        )
                        if (arrComments?.contains(null) == true) arrComments?.remove(null)

                        if (pageNo == 1) adapterComments?.notifyDataSetChanged()
                        else {
//                            if (oldsize != arrHome?.size!! )
//                                adapterHome?.notifyItemRangeInserted(oldsize!!, arrHome?.size!! - 1)
                            adapterComments?.notifyDataSetChanged()
                        }

                        if (pageNo!! > 1 && dataArray.size() != 0) loadData = false

                        adapterComments?.notifyDataSetChanged()
                        if ((pageNo ?: 0) > 1) {
                            loadData = false
                        }
                    }
                    NotFound -> {
                        arrComments?.clear()
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
//                        Toast.makeText((mContext as DashBoardActivity), "${msg}", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    private fun deleteComment(position: Int) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@CommentsActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(MyApiEndpointInterface::class.java)

        val callDeleteComment: Call<JsonObject?>? = apiService?.callPostCommentDelete(headers, refrenceId, arrComments?.get(position)?.id)
        callApi(true, callDeleteComment, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        arrComments?.removeAt(position)
                        adapterComments?.notifyItemRemoved(position)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@CommentsActivity, msg, Toast.LENGTH_LONG).show()
                    }
                    else -> Toast.makeText(this@CommentsActivity, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }
}