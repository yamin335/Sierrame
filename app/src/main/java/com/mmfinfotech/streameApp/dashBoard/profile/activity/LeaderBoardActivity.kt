package com.mmfinfotech.streameApp.dashBoard.profile.activity

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
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterEventRecruiting
import com.mmfinfotech.streameApp.models.Rank
import com.mmfinfotech.streameApp.util.getIntFromJson
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_leader_board.*
import kotlinx.android.synthetic.main.fragment_ranking_total.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class LeaderBoardActivity : NetworkBaseActivity() {
    private val tag: String? = LeaderBoardActivity::class.java.simpleName
    private var arrRank: ArrayList<Rank?>? = null
    private val tabTitleTotal: String = getString(R.string.txt_total) // "Total"
    private val tabTitleMonth: String = getString(R.string.txt_month) // "Month"
    private val tabTitleDay: String = getString(R.string.txt_day) // "Day"

    companion object {
        private const val userName = "userName"
        fun getInstance(context: Context?, username: String?) = Intent(context, LeaderBoardActivity::class.java).apply {
            putExtra(userName, username)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)

        intent.let {
            findViewById<ConstraintLayout>(R.id.includeToolbarLeaderBoard)?.findViewById<TextView>(R.id.textViewTitle)?.text = it.getStringExtra(userName)
        }


        arrRank = ArrayList()

        setListener()

        tabLayoutLeaderBoard.addTab(tabLayoutLeaderBoard.newTab().setText(tabTitleTotal))
        tabLayoutLeaderBoard.addTab(tabLayoutLeaderBoard.newTab().setText(tabTitleMonth))
        tabLayoutLeaderBoard.addTab(tabLayoutLeaderBoard.newTab().setText(tabTitleDay))

        callDataApi("1")

        tabLayoutLeaderBoard?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> callDataApi("1")
                    1 -> callDataApi("2")
                    2 -> callDataApi("3")
                    else -> callDataApi("")
                }
            }
        })

        recyclerViewLeaderBoard?.adapter = AdapterEventRecruiting(this@LeaderBoardActivity, arrRank)
    }

    private fun setListener() {
        findViewById<ConstraintLayout>(R.id.includeToolbarLeaderBoard)?.findViewById<ImageView>(R.id.imageToolbarBack)
            ?.setOnClickListener { onBackPressed() }
    }

    private fun callDataApi(fetch: String) {
        arrRank?.clear()
        recyclerViewLeaderBoard?.adapter?.notifyDataSetChanged()

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@LeaderBoardActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@LeaderBoardActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val _fetch: RequestBody = RequestBody.create(MediaType.parse("text/plain"), fetch)

        val callUserLeaderBoard: Call<JsonObject?>? = apiService?.callUserLeaderBoard(headers, _fetch)

        callApi(true, callUserLeaderBoard, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, "record", JsonObject())
                        val rankes = getJsonArrayFromJson(record, "rankes", JsonArray())
                        for (i in 0 until (rankes?.size() ?: 0)) {
                            val item = getJsonObjFromJson(rankes, i, JsonObject())
                            arrRank?.add(
                                Rank(
                                    id = getIntFromJson(item, "id", AppConstants.Defaults.integer),
                                    spentCoins = getStringFromJson(item, "spent_coins", AppConstants.Defaults.string),
                                    username = getStringFromJson(item, "username", AppConstants.Defaults.string),
                                    score = getIntFromJson(item, "score", AppConstants.Defaults.integer),
                                    userProfile = getStringFromJson(item, "user_profile", AppConstants.Defaults.string),
                                    updatedAt = getStringFromJson(item, "updated_at", AppConstants.Defaults.string),
                                    rank = getIntFromJson(item, "rank", AppConstants.Defaults.integer)
                                )
                            )
                        }
                        recyclerViewLeaderBoard.adapter?.notifyDataSetChanged()
                    }
                    else -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@LeaderBoardActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }
}