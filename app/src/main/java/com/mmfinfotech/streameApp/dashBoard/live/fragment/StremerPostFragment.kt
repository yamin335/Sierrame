package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.activity.PostDescriptionActivity
import com.mmfinfotech.streameApp.dashBoard.live.activity.UserMoreDetailActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterStremePost
import com.mmfinfotech.streameApp.models.Post
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.fragment_post.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


class StremerPostFragment : Fragment() {
    private val TAG: String? = StremerPostFragment::class.java.simpleName
    private var mContext: Context? = null
    private var adapterStremePost: AdapterStremePost? = null
    private var StreamerId: String? = null
    private val arrayMyPost: ArrayList<Post?>? = ArrayList()
    private val gridLayoutManager = GridLayoutManager(mContext, 3)
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var oldsize: Int? = null

    companion object {
        fun getInstance(stremerId: String? = "") =
                StremerPostFragment().apply {
                    arguments = Bundle().apply {
                        putString("StremerId", stremerId)
                    }
                }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        this.StreamerId = arguments?.getString("StremerId")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiStremePost(pageNo)
        setAdapter()
        setListners()
    }

    private fun setListners() {
        recyclerStremerPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = gridLayoutManager?.itemCount
                    lastVisibleItem = gridLayoutManager?.findLastVisibleItemPosition()
                    if (arrayMyPost != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) && (arrayMyPost?.size
                                    ?: 0) >= 20) {
                        loadData = true
                        arrayMyPost?.add(null)
                        adapterStremePost?.notifyDataSetChanged()
                        pageNo = pageNo?.inc()
                        oldsize = arrayMyPost?.size
                        apiStremePost(pageNo)
                    }
                }
            }
        })
    }

    private fun setAdapter() {
        recyclerStremerPost.layoutManager = gridLayoutManager
        adapterStremePost = AdapterStremePost(
                mContext,
                arrayMyPost,
                object : AdapterStremePost.OnPostLisner {
                    override fun onClick(position: Int) {
                        startActivity(PostDescriptionActivity.getInstance(mContext, arrayMyPost?.get(position)?.id))

                    }
                })
        recyclerStremerPost.adapter = adapterStremePost
    }

    private fun apiStremePost(pageno: Int?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val apiService: MyApiEndpointInterface? = ApiClient(mContext).getClient()?.create(
                MyApiEndpointInterface::class.java)
        val callStreamPost: Call<JsonObject?>? = apiService?.callStreamPost(headers, StreamerId.toString(), pageno.toString())
        (mContext as UserMoreDetailActivity).callApi(true, callStreamPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        if (pageNo == 1) {
                            arrayMyPost?.clear()
                        }
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        for (i in 0 until dataArray?.size()!!) {
                            val recordData = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id = recordData?.get("id")?.asInt.toString()
                            val user_id= getStringFromJson(recordData,"user_id",AppConstants.Defaults.string)
                            val title= getStringFromJson(recordData,"title",AppConstants.Defaults.string)
                            val description= getStringFromJson(recordData,"description",AppConstants.Defaults.string)
                            val file= getStringFromJson(recordData,"file",AppConstants.Defaults.string)
                            val thumb= getStringFromJson(recordData,"thumb",AppConstants.Defaults.string)
                            val file_type= getStringFromJson(recordData,"file_type",AppConstants.Defaults.string)
                            val status= getStringFromJson(recordData,"status",AppConstants.Defaults.string)
                            val added_on= getStringFromJson(recordData,"added_on",AppConstants.Defaults.string)
                            val update_on= getStringFromJson(recordData,"update_on",AppConstants.Defaults.string)
                            val user_name= getStringFromJson(recordData,"user_name",AppConstants.Defaults.string)
                            val user_profile= getStringFromJson(recordData,"user_profile",AppConstants.Defaults.string)
                            val profile_status= getStringFromJson(recordData,"profile_status",AppConstants.Defaults.string)
                            val like_status= getStringFromJson(recordData,"like_status",AppConstants.Defaults.string)
                            val like_count= getStringFromJson(recordData,"like_count",AppConstants.Defaults.string)
                            val comment_count= getStringFromJson(recordData,"comment_count",AppConstants.Defaults.string)

                            arrayMyPost?.add(Post(id,
                                user_id,
                                title,
                                description,
                                file,
                                thumb,
                                file_type,
                                status,
                                added_on,
                                update_on,
                                user_name,
                                user_profile,
                                profile_status,
                                like_status,
                                like_count,
                                comment_count))
                        }
                        adapterStremePost?.notifyDataSetChanged()
                    }
                    NotFound -> {
                        arrayMyPost?.clear()
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
//                        Toast.makeText((mContext as DashBoardActivity), "${msg}", Toast.LENGTH_LONG).show()
                    }

                    else -> {
                    }
                }
                if ((mContext as UserMoreDetailActivity).dialog?.isShowing == true)
                    (mContext as UserMoreDetailActivity).dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

}