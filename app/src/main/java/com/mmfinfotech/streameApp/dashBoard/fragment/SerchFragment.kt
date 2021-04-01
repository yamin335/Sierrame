package com.mmfinfotech.streameApp.dashBoard.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.activity.PlayStreamingActivity
import com.mmfinfotech.streameApp.dashBoard.adapter.AdapterLivers
import com.mmfinfotech.streameApp.dashBoard.adapter.AdapterSearch
import com.mmfinfotech.streameApp.dashBoard.live.activity.UserMoreDetailActivity
import com.mmfinfotech.streameApp.model.Clips
import com.mmfinfotech.streameApp.model.Liver
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.fragment_serch.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


class SerchFragment : Fragment() {
    private val TAG = SerchFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrSerch: ArrayList<Clips?>? = null
    private var adapterSearch: AdapterSearch? = null
    private var arrLivers: ArrayList<Liver?>? = ArrayList()
    private var adapterLivers: AdapterLivers? = null
    private var mGridManager: GridLayoutManager? = GridLayoutManager(activity, 3)
    private var mLayoutManager: LinearLayoutManager? = null
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var oldsize: Int? = null
    private var query: String? = null
    val handler: Handler? = Handler()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_serch, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val searchView = (mContext as DashBoardActivity).findViewById(R.id.searchView) as SearchView
        searchView?.findViewById<TextView>(androidx.appcompat.R.id.search_src_text).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

        arrSerch = ArrayList()
        apiLatestClip(pageNo)
        setAdapter(arrSerch)
        setListners()
    }

    override fun onResume() {
        super.onResume()
//        searchView.setQuery("", false)
        constraintViewLivers.visibility = View.GONE
        constraintSearchFrag.visibility = View.VISIBLE
        buttonCancel.visibility = View.GONE
    }

    private fun setListners() {
        buttonCancel.setOnClickListener {
            searchView.setQuery("", false)
            constraintViewLivers.visibility = View.GONE
            constraintSearchFrag.visibility = View.VISIBLE
            buttonCancel.visibility = View.GONE
        }
        searchView.setOnSearchClickListener {
            constraintViewLivers.visibility = View.VISIBLE
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                buttonCancel.visibility = View.VISIBLE
                recyclerview_search_fragLivers.visibility = View.VISIBLE
                constraintViewLivers.visibility = View.VISIBLE
                constraintSearchFrag.visibility = View.GONE

                handler?.removeCallbacksAndMessages(null)
                handler?.postDelayed(Runnable {
                    query = newText
                    if (!query.equals("")) callSearchApi(pageNo)
                }, 2000)
                return true
            }
        })
        recyclerview_search_frag.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = mLayoutManager?.getItemCount()
                    lastVisibleItem = mLayoutManager?.findLastVisibleItemPosition()
                    if (arrSerch != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) &&
                        (arrSerch?.size ?: 0) >= 20
                    ) {
                        loadData = true
                        arrSerch?.add(null)
                        adapterSearch?.notifyDataSetChanged()
                        pageNo = pageNo?.inc()
                        oldsize = arrSerch?.size
                    }
                }
            }
        })
        recyclerview_search_fragLivers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    totalItemCount = mLayoutManager?.getItemCount()
                    lastVisibleItem = mLayoutManager?.findLastVisibleItemPosition()
                    if (arrLivers != null && !loadData && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold) &&
                        (arrLivers?.size ?: 0) >= 20
                    ) {
                        loadData = true
                        arrLivers?.add(null)
                        adapterLivers?.notifyDataSetChanged()
//                        getDynDataFromServer(20, index - 1, true, true)
                        pageNo = pageNo?.inc()
                        oldsize = arrLivers?.size
                        callSearchApi(pageNo)
                    }
                }
            }
        })
    }

    private fun setAdapter(arrHomeLive: ArrayList<Clips?>?) {
        recyclerview_search_frag.layoutManager = mGridManager
        adapterSearch = AdapterSearch(
            mContext,
            arrHomeLive,
            object : AdapterSearch.OnSearchLisner {
                override fun onClick(position: Int) {
                    startActivity(
                        PlayStreamingActivity.getInstance(mContext, arrSerch?.get(position))
                    )
                }
            })

        recyclerview_search_frag.adapter = adapterSearch

        mLayoutManager = recyclerview_search_fragLivers.layoutManager as LinearLayoutManager
        adapterLivers = AdapterLivers(mContext, arrLivers, object : AdapterLivers.OnSearchLiversListner {
            override fun liveProfileListner(p: Int) {
                startActivity(UserMoreDetailActivity.getInstance(mContext, arrLivers?.get(p)?.id))
            }
        })
        recyclerview_search_fragLivers.adapter = adapterLivers

    }

    private fun callSearchApi(pageNo: Int?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("search_key", query)

        val apiService: MyApiEndpointInterface? = ApiClient(mContext).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callStreamPost: Call<JsonObject?>? = apiService?.callSearchLivers(headers, pageNo.toString(), sendParams)
        (mContext as DashBoardActivity).callApi(true, callStreamPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        if (pageNo == 1) {
                            arrLivers?.clear()
                        }
                        for (i in 0 until dataArray?.size()!!) {
                            val recordData = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id = getStringFromJson(recordData, "id", AppConstants.Defaults.string)
                            val username = getStringFromJson(recordData, "username", AppConstants.Defaults.string)
                            val name = getStringFromJson(recordData, "name", AppConstants.Defaults.string)
                            val profile = getStringFromJson(recordData, "profile", AppConstants.Defaults.string)
                            val my_status = getStringFromJson(recordData, "my_status", AppConstants.Defaults.string)

                            arrLivers?.add(
                                Liver(
                                    id,
                                    username,
                                    name,
                                    profile,
                                    my_status
                                )
                            )
                        }
                        adapterLivers?.notifyDataSetChanged()
                    }
                    NotFound -> {
                        arrLivers?.clear()
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(mContext, "${msg}", Toast.LENGTH_LONG).show()
                    }
                }
                if ((mContext as DashBoardActivity).dialog?.isShowing == true)
                    (mContext as DashBoardActivity).dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    private fun apiLatestClip(pageNo: Int?) {

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val apiService: MyApiEndpointInterface? = ApiClient((mContext as DashBoardActivity)).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callMyPost: Call<JsonObject?>? = apiService?.callLatestClips(headers, pageNo.toString())
        (mContext as DashBoardActivity).callApi(true, callMyPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        if (pageNo == 1) {
                            arrSerch?.clear()
                        }
                        for (i in 0 until dataArray?.size()!!) {
                            val recordData = getJsonObjFromJson(dataArray, i, JsonObject())

                            val id: String? = recordData?.get("id")?.asInt.toString()
                            val user_id: String? = getStringFromJson(recordData, "user_id", AppConstants.Defaults.string)
                            val title: String? = getStringFromJson(recordData, "title", AppConstants.Defaults.string)
                            val description: String? = getStringFromJson(recordData, "description", AppConstants.Defaults.string)
                            val file: String? = getStringFromJson(recordData, "file", AppConstants.Defaults.string)
                            val thumb: String? = getStringFromJson(recordData, "thumb", AppConstants.Defaults.string)
                            val file_type: String? = getStringFromJson(recordData, "file_type", AppConstants.Defaults.string)
                            val category: String? = getStringFromJson(recordData, "category", AppConstants.Defaults.string)
                            val status: String? = getStringFromJson(recordData, "status", AppConstants.Defaults.string)
                            val added_on: String? = getStringFromJson(recordData, "added_on", AppConstants.Defaults.string)
                            val update_on: String? = getStringFromJson(recordData, "update_on", AppConstants.Defaults.string)
                            val user_name: String? = getStringFromJson(recordData, "user_name", AppConstants.Defaults.string)
                            val user_profile: String? = getStringFromJson(recordData, "user_profile", AppConstants.Defaults.string)
                            val profile_status: String? = getStringFromJson(recordData, "profile_status", AppConstants.Defaults.string)
                            val like_count: String? = recordData?.get("like_count")?.asInt.toString()
                            val comment_count: String? = recordData?.get("comment_count")?.asInt.toString()
                            arrSerch?.add(
                                Clips(
                                    id,
                                    user_id,
                                    title,
                                    description,
                                    file,
                                    thumb,
                                    file_type,
                                    category,
                                    status,
                                    added_on,
                                    update_on,
                                    user_name,
                                    user_profile,
                                    profile_status,
                                    like_count,
                                    comment_count
                                )
                            )


                        }
                        adapterSearch?.notifyDataSetChanged()
                    }
                    NotFound -> {
                        arrSerch?.clear()
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