package com.mmfinfotech.streameApp.dashBoard.profile.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterStremeClips
import com.mmfinfotech.streameApp.dashBoard.activity.PlayStreamingActivity
import com.mmfinfotech.streameApp.models.Clips
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.fragment_clips.*
import retrofit2.Call
import java.util.HashMap

class ClipsFragment : Fragment() {
    private val TAG: String? = ClipsFragment::class.java.simpleName
    private var mContext: Context? = null
    private var adapterStremeCLip: AdapterStremeClips? = null
    private val arrayMyClips: ArrayList<Clips?>? = ArrayList()
    private val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,1)
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clips, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiGetClip(pageNo)
        setAdapter()
        setListners()
    }

    private fun setListners() {
        recyclerStremerClips.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1)) {
                totalItemCount = staggeredGridLayoutManager?.itemCount


                var visibleChild: View = recyclerView.getChildAt(0)
                val firstChild: Int = recyclerView.getChildAdapterPosition(visibleChild)
                visibleChild = recyclerView.getChildAt(recyclerView.childCount - 1)
                lastVisibleItem = recyclerView.getChildAdapterPosition(visibleChild)
                if (arrayMyClips != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) && (arrayMyClips?.size
                        ?: 0) >= 20) {
                    loadData = true
                    arrayMyClips?.add(null)
                    adapterStremeCLip?.notifyDataSetChanged()
                    pageNo = pageNo?.inc()
                    oldsize = arrayMyClips?.size
                    apiGetClip(pageNo)
                }
            }

        })
    }

    private fun setAdapter() {
        recyclerStremerClips.layoutManager = staggeredGridLayoutManager
        adapterStremeCLip = AdapterStremeClips(
                mContext,
                arrayMyClips,
                object : AdapterStremeClips.OnPostLisner {
                    override fun onClick(position: Int) {
                        startActivity(PlayStreamingActivity.getInstance(mContext, arrayMyClips?.get(position)))
                    }
                })
        recyclerStremerClips.adapter = adapterStremeCLip
    }

    private fun apiGetClip(pageNo: Int?) {
        // Need_to_fix
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val apiService: MyApiEndpointInterface? = ApiClient((mContext as DashBoardActivity)).getClient()?.create(
                MyApiEndpointInterface::class.java)
        val callMyPost: Call<JsonObject?>? = apiService?.callMyClips(headers, pageNo.toString())
        (mContext as DashBoardActivity).callApi(true, callMyPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        if (pageNo == 1) {
                            arrayMyClips?.clear()
                        }
                        for (i in 0 until dataArray?.size()!!) {
                            val recordData = getJsonObjFromJson(dataArray, i, JsonObject())

                            val id:String?= recordData?.get("id")?.asInt.toString()
                            val user_id:String?= getStringFromJson(recordData,"user_id",AppConstants.Defaults.string)
                            val title:String?= getStringFromJson(recordData,"title",AppConstants.Defaults.string)
                            val description:String?= getStringFromJson(recordData,"description",AppConstants.Defaults.string)
                            val file:String?= getStringFromJson(recordData,"file",AppConstants.Defaults.string)
                            val thumb:String?= getStringFromJson(recordData,"thumb",AppConstants.Defaults.string)
                            val file_type:String?= getStringFromJson(recordData,"file_type",AppConstants.Defaults.string)
                            val category:String?= getStringFromJson(recordData,"category",AppConstants.Defaults.string)
                            val status:String?= getStringFromJson(recordData,"status",AppConstants.Defaults.string)
                            val added_on:String?= getStringFromJson(recordData,"added_on",AppConstants.Defaults.string)
                            val update_on:String?= getStringFromJson(recordData,"update_on",AppConstants.Defaults.string)
                            val user_name:String?= getStringFromJson(recordData,"user_name",AppConstants.Defaults.string)
                            val user_profile:String?= getStringFromJson(recordData,"user_profile",AppConstants.Defaults.string)
                            val profile_status:String?= getStringFromJson(recordData,"profile_status",AppConstants.Defaults.string)
                            val like_count:String?= recordData?.get("like_count")?.asInt.toString()
                            val comment_count:String?= recordData?.get("comment_count")?.asInt.toString()
//                            arrayMyClips?.add(
//                                Clips(
//                                    id,
//                                    user_id,
//                                    title,
//                                    description,
//                                    file,
//                                    thumb,
//                                    file_type,
//                                    category,
//                                    status,
//                                    added_on,
//                                    update_on,
//                                    user_name,
//                                    user_profile,
//                                    profile_status,
//                                    like_count,
//                                    comment_count
//                                )
//                            )


                    }
                        adapterStremeCLip?.notifyDataSetChanged()
                        if (pageNo!! > 1) {
                            loadData = false
                        }
                    }
                    NotFound -> {
                        arrayMyClips?.clear()
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
}