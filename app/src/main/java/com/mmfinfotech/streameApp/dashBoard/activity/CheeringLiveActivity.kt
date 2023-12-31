package com.mmfinfotech.streameApp.dashBoard.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.base.NetworkBaseActivity
import com.mmfinfotech.streameApp.dashBoard.adapter.CheeringLiveAdapter
import com.mmfinfotech.streameApp.models.AnonymousCheeringResponse
import com.mmfinfotech.streameApp.models.Cheering
import com.mmfinfotech.streameApp.models.CheeringResponse
import com.mmfinfotech.streameApp.util.getIntFromJson
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_annonymous_cheering.*
import kotlinx.android.synthetic.main.activity_cheering_live.*
import kotlinx.android.synthetic.main.activity_leader_board.*
import kotlinx.android.synthetic.main.fragment_leader_board.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class CheeringLiveActivity : NetworkBaseActivity() {
    private val tag: String? = CheeringLiveActivity::class.java.simpleName
    private val tabTitleThisMonth: String by lazy {  getString(R.string.txt_this_month) }
    private val tabTitleTotal: String by lazy { getString(R.string.txt_total) }

    private var cheeringLiveAdapter: CheeringLiveAdapter? = null
    private var arrRank: ArrayList<Cheering?>? = null


    companion object {
        fun getInstance(context: Context?) = Intent(context, CheeringLiveActivity::class.java).apply {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheering_live)


        findViewById<ConstraintLayout>(R.id.includeToolbarCheeringLive)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.cheering_live)

        arrRank = ArrayList()

        setListener()

        tabLayoutCheeringLive.addTab(tabLayoutCheeringLive.newTab().setText(tabTitleThisMonth))
        tabLayoutCheeringLive.addTab(tabLayoutCheeringLive.newTab().setText(tabTitleTotal))

        callDataApi("2")

        tabLayoutCheeringLive?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> callDataApi("2")
                    1 -> callDataApi("1")
                    else -> callDataApi("")
                }
            }
        })

        val spaceHorizontal = resources?.getDimensionPixelSize(R.dimen._10sdp)
        val spaceVertical = resources?.getDimensionPixelSize(R.dimen._10sdp)
        recyclerViewCheeringLive?.addItemDecoration(SpaceItemDecoration(spaceHorizontal ?: 0, spaceVertical ?: 0))
        cheeringLiveAdapter = CheeringLiveAdapter((this@CheeringLiveActivity))
        recyclerViewCheeringLive?.adapter = cheeringLiveAdapter

    }

    private fun setListener() {
        findViewById<ConstraintLayout>(R.id.includeToolbarCheeringLive)?.findViewById<ImageView>(R.id.imageToolbarBack)
            ?.setOnClickListener { onBackPressed() }
    }

    private fun callDataApi(fetch: String) {
        arrRank?.clear()
//        recyclerViewCheeringLive?.adapter?.notifyDataSetChanged()
        cheeringLiveAdapter?.setData(arrRank)

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@CheeringLiveActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@CheeringLiveActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val _fetch: RequestBody = RequestBody.create(MediaType.parse("text/plain"), fetch)

        val callUserLeaderBoard: Call<CheeringResponse?>? = apiService?.callCheeringLive(headers, _fetch)

        callRemoteApi(true, callUserLeaderBoard, object : ApiClient.ApiCallbackListener<CheeringResponse> {
            override fun onDataFetched(response: CheeringResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@CheeringLiveActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<CheeringResponse> {
                        override fun onSuccess(response: CheeringResponse) {
                            val data = response.record?.data ?: return
                            for (item in data) {
                                arrRank?.add(item)
                            }
                            cheeringLiveAdapter?.setData(arrRank)
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(this@CheeringLiveActivity, message, Toast.LENGTH_LONG).show()
                        }
                    })
            }
        })
    }
}