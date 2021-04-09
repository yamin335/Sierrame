package com.mmfinfotech.streameApp.dashBoard.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.activity.CommentsActivity
import com.mmfinfotech.streameApp.dashBoard.activity.PostDescriptionActivity
import com.mmfinfotech.streameApp.dashBoard.adapter.AdapterTimeLine
import com.mmfinfotech.streameApp.models.LikeDisLikeResponse
import com.mmfinfotech.streameApp.models.Post
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_play_streaming.*
import kotlinx.android.synthetic.main.fragment_time_line.*
import retrofit2.Call
import java.util.HashMap

class TimeLineFragment : Fragment() {
    private var mContext: Context? = null
    private var arrTimeLInes: ArrayList<Post?>? = ArrayList()
    private var adapterTimeline : AdapterTimeLine?= null
    private var mLayoutManager : LinearLayoutManager? = null
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var oldsize: Int? = null
    val handler: Handler?= Handler()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_time_line, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrTimeLInes = ArrayList()
        callTimeLineApi(true,pageNo)
        setListners()
        setAdapter()
    }

    private fun setListners() {
        swipe_refresh_CollaborationComments.setOnRefreshListener {
            pageNo = 1
            callTimeLineApi(true,  pageNo)
        }

        recyclerview_TimeLine_frag.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = mLayoutManager?.getItemCount()
                    lastVisibleItem = mLayoutManager?.findLastVisibleItemPosition()
                    if (arrTimeLInes != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) &&
                        (arrTimeLInes?.size ?: 0) >= 20) {
                        loadData = true
                        arrTimeLInes?.add(null)
                        adapterTimeline?.notifyDataSetChanged()
//                        getDynDataFromServer(20, index - 1, true, true)
                        pageNo = pageNo?.inc()
                        oldsize = arrTimeLInes?.size
                        callTimeLineApi(false,pageNo)

                    }
                }
            }
        })

    }

    private fun setAdapter() {
        recyclerview_TimeLine_frag.adapter = adapterTimeline
        mLayoutManager = recyclerview_TimeLine_frag.layoutManager as LinearLayoutManager
        adapterTimeline = AdapterTimeLine(mContext, arrTimeLInes, object : AdapterTimeLine.OnTimeLineListner {
            override fun onClickThreeDotsListner(p: Int) {
                showDotOptionsList(p)
            }

            override fun onMoreClickingListner(p: Int) {
                startActivity(PostDescriptionActivity.getInstance(mContext, arrTimeLInes?.get(p)?.id?.toString()))
            }

            override fun onCommentClickingListner(p: Int) {
                startActivity(CommentsActivity.getInstance(mContext, arrTimeLInes?.get(p)?.id?.toString()))
            }

            override fun onLikeClickingListner(p: Int, ImageButtonHeart: ImageView) {
                callApiLikeDisLike(arrTimeLInes?.get(p)?.id?.toString(),ImageButtonHeart)
            }

            override fun onShareClickingListner(p: Int) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "hi")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        })
        recyclerview_TimeLine_frag.adapter = adapterTimeline
    }

     fun callTimeLineApi( isRefresh: Boolean?,pageNo: Int?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"

        val apiService: MyApiEndpointInterface? = ApiClient(mContext).getClient()?.create(
            MyApiEndpointInterface::class.java)

        val callTimeLinePost: Call<JsonObject?>? = apiService?.callTimeLines(headers, pageNo.toString())
        (mContext as DashBoardActivity).callApi(true, callTimeLinePost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        if (isRefresh == true) arrTimeLInes?.clear()
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        if (pageNo == 1) {
                            arrTimeLInes?.clear()
                        }
                        for (i in 0 until dataArray?.size()!!) {
                            val recordData = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id = recordData?.get("id")?.asInt.toString()
                            val user_id= getStringFromJson(recordData,"user_id",AppConstants.Defaults.string)
                            val title= getStringFromJson(recordData,"title",AppConstants.Defaults.string)
                            val description= getStringFromJson(recordData,"description",AppConstants.Defaults.string)
                            val file= getStringFromJson(recordData,"file",AppConstants.Defaults.string)
                            val thumb= getStringFromJson(recordData,"thumb",AppConstants.Defaults.string)
                            val file_type= getStringFromJson(recordData,"file_type",AppConstants.Defaults.string)
                            val status= recordData?.get("status")?.asInt.toString()
                            val added_on= getStringFromJson(recordData,"added_on",AppConstants.Defaults.string)
                            val update_on= getStringFromJson(recordData,"update_on",AppConstants.Defaults.string)
                            val user_name= getStringFromJson(recordData,"user_name",AppConstants.Defaults.string)
                            val user_profile= getStringFromJson(recordData,"user_profile",AppConstants.Defaults.string)
                            val profile_status= getStringFromJson(recordData,"profile_status",AppConstants.Defaults.string)
                            val like_status= getStringFromJson(recordData,"like_status",AppConstants.Defaults.string)
                            val like_count= getStringFromJson(recordData,"like_count",AppConstants.Defaults.string)
                            val comment_count= getStringFromJson(recordData,"comment_count",AppConstants.Defaults.string)

                            arrTimeLInes?.add(Post(id.toInt(),
                                user_id,
                                title,
                                description,
                                file,
                                thumb,
                                file_type,
                                status.toInt(),
                                added_on,
                                update_on+"000",
                                user_name,
                                user_profile,
                                profile_status,
                                like_status,
                                like_count.toInt(),
                                comment_count.toInt()))
                        }
                        adapterTimeline?.notifyDataSetChanged()
                        if (pageNo!! > 1) {
                            loadData = false
                        }
                        if (swipe_refresh_CollaborationComments?.isRefreshing == true)
                            swipe_refresh_CollaborationComments?.isRefreshing = false
                        if ((mContext as DashBoardActivity).dialog?.isShowing == true)
                                (mContext as DashBoardActivity).dialog?.dismiss()
                    }
                    NotFound -> {
                        arrTimeLInes?.clear()
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
//                        Toast.makeText((mContext as DashBoardActivity), "${msg}", Toast.LENGTH_LONG).show()
                    }

                    else -> {
                    }
                }
                if ((mContext as DashBoardActivity).dialog?.isShowing == true)
                    (mContext as DashBoardActivity).dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    private fun callApiLikeDisLike(id: String?, ImageButtonHeart: ImageView) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val apiService: MyApiEndpointInterface? =
            ApiClient(mContext).getClient()?.create(MyApiEndpointInterface::class.java)
        val callLogout: Call<LikeDisLikeResponse?>? = apiService?.callLikeDisslike(headers, id, AppConstants.LikeTypes.TypePost)
        (mContext as DashBoardActivity).callRemoteApi(true, callLogout, object : ApiClient.ApiCallbackListener<LikeDisLikeResponse> {
            override fun onDataFetched(response: LikeDisLikeResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create((mContext as DashBoardActivity), response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<LikeDisLikeResponse> {
                        override fun onSuccess(response: LikeDisLikeResponse) {
                            if (response.status_changed.equals("1")) {
                                ImageButtonHeart.setImageResource(R.drawable.ic__fillheart_24);
                            } else {
                                ImageButtonHeart.setImageResource(R.drawable.ic_baseline_emptyheart);
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                        }
                    })
            }
        })
    }

    fun showDotOptionsList(p: Int) {
        context?.let { context ->
            AlertDialog.Builder(context, R.style.MyDialog).create().apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_three_dots, null).apply {
                    findViewById<TextView>(R.id.textViewSpamReport).setOnClickListener {  }
                    findViewById<TextView>(R.id.textViewBlock).setOnClickListener {
                        dismiss()
                        blockUser(p)
                    }
                    findViewById<TextView>(R.id.textViewCancel).setOnClickListener { dismiss() }
                }
                setView(view, 0, 0, 0, 0)
                window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                window?.setGravity(Gravity.BOTTOM)
            }.run { show() }
        }
    }

    private fun blockUser(p: Int) {
        val userId = arrTimeLInes?.get(p)?.user_id

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val apiService: MyApiEndpointInterface? = ApiClient(mContext).getClient()?.create(MyApiEndpointInterface::class.java)

        val callBlockUser: Call<JsonObject?>? = apiService?.callBlockUser(headers, userId)
        (mContext as DashBoardActivity).callApi(true, callBlockUser, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val message: String? = getStringFromJson(mainObject, "message", AppConstants.Defaults.string)
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        pageNo = 1
                        callTimeLineApi(true,  pageNo)
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (  (mContext as DashBoardActivity).dialog?.isShowing == true)
                    (mContext as DashBoardActivity).dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }


}