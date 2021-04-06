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
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.activity.PostDescriptionActivity
import com.mmfinfotech.streameApp.dashBoard.live.activity.UserMoreDetailActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterStremePost
import com.mmfinfotech.streameApp.models.Post
import com.mmfinfotech.streameApp.models.PostResponse
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
                        startActivity(PostDescriptionActivity.getInstance(mContext, arrayMyPost?.get(position)?.id?.toString()))

                    }
                })
        recyclerStremerPost.adapter = adapterStremePost
    }

    private fun apiStremePost(pageno: Int?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val apiService: MyApiEndpointInterface? = ApiClient(mContext).getClient()?.create(
                MyApiEndpointInterface::class.java)
        val callStreamPost: Call<PostResponse?>? = apiService?.callStreamPost(headers, StreamerId.toString(), pageno.toString())
        (mContext as UserMoreDetailActivity).callRemoteApi(true, callStreamPost, object : ApiClient.ApiCallbackListener<PostResponse> {
            override fun onDataFetched(response: PostResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(requireActivity(), response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<PostResponse> {
                        override fun onSuccess(response: PostResponse) {
                            val posts = response.record?.data ?: return
                            if (pageNo == 1) {
                                arrayMyPost?.clear()
                            }
                            for (post in posts) {
                                arrayMyPost?.add(post)
                            }
                            adapterStremePost?.notifyDataSetChanged()
                            if (pageNo!! > 1) {
                                loadData = false
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                            when (status) {
                                NotFound -> {
                                    arrayMyPost?.clear()
                                }
                            }
                        }
                    })
            }
        })
    }

}