package com.mmfinfotech.streameApp.dashBoard.live.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterRequestToAddLivers
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterSelectedLivers
import com.mmfinfotech.streameApp.models.Liver
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.listners.OnMemoAddListners
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.AppToast
import kotlinx.android.synthetic.main.activity_search_brodcaster.*
import kotlinx.android.synthetic.main.fragment_serch.*
import kotlinx.android.synthetic.main.fragment_serch.searchView
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class SearchBrodcasterActivity : DashBoardBaseActivity() {
    private var arrLivers: ArrayList<Liver?>? = ArrayList()
    private var adapterRequestAddLivers: AdapterRequestToAddLivers? = null
    private var adapterSelectedLivers: AdapterSelectedLivers? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var loadData: Boolean = false
    private val visibleThreshold = 1
    private var pageNo: Int? = 1
    private var oldsize: Int? = null
    private var query: String? = ""
    private var arrSelectedLivers: ArrayList<Liver?>? = ArrayList()
    private var StreameId: String? = null
    val handler: Handler? = Handler()

    companion object {
        fun getInstance(
            context: Context?, id: String?
        ) = Intent(context, SearchBrodcasterActivity::class.java).apply {
            putExtra(AppConstants.IntentExtras.LiveId, id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_brodcaster)
        StreameId = intent.getStringExtra(AppConstants.IntentExtras.LiveId)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView?.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
            ?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

        initView()
        callSearchApi(pageNo)
        setAdapter()
        setListners()
    }

    private fun callSearchApi(pageNo: Int?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("search_key", query)
        sendParams?.addProperty("streaming_id", StreameId)

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callStreamPost: Call<JsonObject?>? =
            apiService?.callSearchInviteLivers(headers, pageNo.toString(), sendParams)
        callApi(true, callStreamPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val dataArray = getJsonArrayFromJson(record, "data", JsonArray())
                        if (pageNo == 1) {
                            arrLivers?.clear()
                        }
                        for (i in 0 until dataArray?.size()!!) {
                            val recordData = getJsonObjFromJson(dataArray, i, JsonObject())
                            val id =
                                getStringFromJson(recordData, "id", AppConstants.Defaults.string)
                            val username = getStringFromJson(
                                recordData,
                                "username",
                                AppConstants.Defaults.string
                            )
                            val name =
                                getStringFromJson(recordData, "name", AppConstants.Defaults.string)
                            val profile = getStringFromJson(
                                recordData,
                                "profile",
                                AppConstants.Defaults.string
                            )
                            val my_status = getStringFromJson(
                                recordData,
                                "my_status",
                                AppConstants.Defaults.string
                            )
//                            var isSelected :Boolean?=null
//                            for (positn in 0 until arrSelectedLivers?.size!!) {
//                                isSelected = arrSelectedLivers?.get(positn)?.id == id
//                            }

//                            arrSelectedLivers?.any{ it?.id== id}

                            val isSelected =  arrSelectedLivers?.any{ it?.id== id}
//                            val isSelected = arrSelectedLivers?.contains(id)
                            arrLivers?.add(
                                Liver(
                                    id,
                                    username,
                                    name,
                                    profile,
                                    my_status, isSelected
                                )
                            )
                        }
                        adapterRequestAddLivers?.notifyDataSetChanged()
                        if (pageNo!! > 1) {
                            loadData = false
                        }
                    }
                    NotFound -> {
                        arrLivers?.clear()
                        val msg =
                            getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@SearchBrodcasterActivity, "${msg}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    private fun initView() {
        findViewById<ConstraintLayout>(R.id.toolbarLiversDetail)?.findViewById<TextView>(R.id.textViewTitle)?.text =
            getString(R.string.txt_title_sendto)
        findViewById<ConstraintLayout>(R.id.toolbarLiversDetail)?.findViewById<ImageView>(R.id.imageToolbarMenu)
            ?.setImageDrawable(getDrawable(R.drawable.ic_cross_white))
    }

    private fun setListners() {
        findViewById<ConstraintLayout>(R.id.toolbarLiversDetail)?.findViewById<ImageView>(R.id.imageToolbarMenu)
            ?.setOnClickListener {
                onBackPressed()
            }
        findViewById<ConstraintLayout>(R.id.toolbarLiversDetail)?.findViewById<ImageView>(R.id.imageToolbarBack)
            ?.setOnClickListener {
                onBackPressed()
            }
        searchView.setOnSearchClickListener {
            constraintViewLivers.visibility = View.VISIBLE
        }
        buttonDone.setOnClickListener {
            arrSelectedLivers?.removeAll(Collections.singleton(null));
            if(arrSelectedLivers?.size ==0 && arrSelectedLivers?.isEmpty()==true)
            {
                AppToast.showToast(this@SearchBrodcasterActivity, getString(R.string.please_select_members))
            }else{
                showRequestSendDialog(
                    this@SearchBrodcasterActivity,
                    arrSelectedLivers,
                    adapterSelectedLivers,
                    object : OnMemoAddListners {
                        override fun saveStringListners(view: View, s: String) {
                            if (arrSelectedLivers?.isNotEmpty()!! && arrSelectedLivers != null) apiSendInvitation(
                                s
                            )
                        }

                        override fun cancelViewListners(view: View) {

                        }
                    })
            }

        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler?.removeCallbacksAndMessages(null)
                handler?.postDelayed(Runnable {
                    query = newText
                    pageNo=1
                    callSearchApi(pageNo)

                }, 2000)
                return true
            }
        })
        recyclerviewLivers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        adapterRequestAddLivers?.notifyDataSetChanged()
//                        getDynDataFromServer(20, index - 1, true, true)
                        pageNo = pageNo?.inc()
                        oldsize = arrLivers?.size
                        callSearchApi(pageNo)
                    }
                }
            }
        })
    }

    private fun setAdapter() {
        mLayoutManager = recyclerviewLivers.layoutManager as LinearLayoutManager
        adapterRequestAddLivers = AdapterRequestToAddLivers(
            this@SearchBrodcasterActivity,
            arrLivers,
            arrSelectedLivers,
            object : AdapterRequestToAddLivers.OnReqstLiversListner {
                override fun onReqstLiveListner(p: Int) {
//                startActivity(UserMoreDetailActivity.getInstance(mContext, arrLivers?.get(p)?.id))

                }

                override fun oncheckedClicking(position: Int, checkbox: CheckBox) {
//                arrLivers?.get(position)?.isSeleted = arrLivers?.get(position)?.isSeleted != true
                    val liveObj = arrLivers?.get(position)
                    if (arrSelectedLivers?.contains(liveObj) == true) {
                        arrLivers?.get(position)?.isSeleted = false
                        arrSelectedLivers?.remove(liveObj)

                        !checkbox.isChecked
                    } else {
                        if (arrSelectedLivers?.size ?: 0 < 4) {
                            arrSelectedLivers?.add(liveObj)
                            arrLivers?.get(position)?.isSeleted =
                                arrLivers?.get(position)?.isSeleted != true
                        } else {
                            !checkbox.isChecked
                            !checkbox.isClickable
                            ShowAlertInformation(
                                this@SearchBrodcasterActivity,
                                getString(R.string.only_user_at_time)
                            )
                        }
                    }
                    adapterRequestAddLivers?.notifyDataSetChanged()
                    adapterSelectedLivers?.notifyDataSetChanged()
                    Log.d("selected Member ", "log arrSelectedLivers ${arrSelectedLivers?.size}")
                    Log.d("Member ", "log  ${arrLivers?.size}")
                }
            })
        recyclerviewLivers.adapter = adapterRequestAddLivers

        mLayoutManager = recyclerSelectedLivers.layoutManager as LinearLayoutManager
        adapterSelectedLivers = AdapterSelectedLivers(
            this@SearchBrodcasterActivity,
            arrSelectedLivers,
            object : AdapterSelectedLivers.OnSelectedReqstLiversListner {
                override fun onReqstLiveListner(p: Int) {
//                startActivity(UserMoreDetailActivity.getInstance(mContext, arrLivers?.get(p)?.id))
                }

                override fun onCancelClicking(p: Int) {
                    val selectedliveObj = arrSelectedLivers?.get(p)
                    if (arrLivers?.contains(selectedliveObj) == true) {
//                    arrLivers?.get(p)?.isSeleted = false
                        for (i in 0 until arrLivers?.size!!) {
                            if (arrSelectedLivers?.get(p)?.id == arrLivers?.get(i)?.id) {
                                arrLivers?.get(i)?.isSeleted = false
                            } else {
                            }
                        }
                    }
                    arrSelectedLivers?.removeAt(p)

                    adapterSelectedLivers?.notifyDataSetChanged()
                    adapterRequestAddLivers?.notifyDataSetChanged()
                }
            })
        recyclerSelectedLivers.adapter = adapterSelectedLivers
    }

    private fun apiSendInvitation(msg: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val list: ArrayList<String?>? = ArrayList()
        for (i in 0 until arrSelectedLivers?.size!!) list?.add(arrSelectedLivers?.get(i)?.id)
        list?.joinToString()
        val id: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            StreameId ?: AppConstants.Defaults.string
        )
        val msg: RequestBody = RequestBody.create(MediaType.parse("text/plain"), msg)
        val invite_id: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            list?.joinToString() ?: AppConstants.Defaults.string
        )
        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callStreamPost: Call<JsonObject?>? =
            apiService?.callStreamersInvitation(headers, id, msg, invite_id)
        callApi(true, callStreamPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val message = getStringFromJson(mainObject, "message", "")
                        CompleteActionDialog(
                            this@SearchBrodcasterActivity,
                            message.toString(),
                            View.OnClickListener { v ->
                                when (v?.id) {
                                    R.id.doneTask -> {
                                        finish()
                                    }
                                }
                            })
                    }
                    NotFound -> {
                        arrLivers?.clear()
                        val msg =
                            getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@SearchBrodcasterActivity, "${msg}", Toast.LENGTH_LONG)
                            .show()
                    }
                    ValidationError -> {
                        val message = getStringFromJson(mainObject, "message", "")
                        CompleteActionDialog(
                            this@SearchBrodcasterActivity,
                            message.toString(),
                            View.OnClickListener { v ->
                                when (v?.id) {
                                    R.id.doneTask -> {
                                        finish()
                                    }
                                }
                            })
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }
}