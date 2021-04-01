package com.mmfinfotech.streameApp.dashBoard.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterEventRecruiting
import com.mmfinfotech.streameApp.dashBoard.profile.activity.RankingCharringProfileActivity
import com.mmfinfotech.streameApp.model.Rank
import com.mmfinfotech.streameApp.util.getIntFromJson
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.fragment_ranking_total.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class RankingTotalFragment : Fragment() {
    private val TAG: String? = RankingTotalFragment::class.java.simpleName
    private var arrEventRecruiting: ArrayList<Rank?>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ranking_total, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrEventRecruiting = ArrayList()

        recyclerRanking.adapter = AdapterEventRecruiting(context, arrEventRecruiting)

        callDataApi()
    }

    private fun callDataApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"

        val apiService: MyApiEndpointInterface? = ApiClient(context).getClient()?.create(MyApiEndpointInterface::class.java)
        val fetch: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "1")

        val callUserLeaderBoard: Call<JsonObject?>? = apiService?.callUserLeaderBoard(headers, fetch)

        (context as RankingCharringProfileActivity).callApi(true, callUserLeaderBoard, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, "record", JsonObject())
                        val rankes = getJsonArrayFromJson(record, "rankes", JsonArray())
                        for (i in 0 until (rankes?.size() ?: 0)) {
                            val item = getJsonObjFromJson(rankes, i, JsonObject())
                            arrEventRecruiting?.add(
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
                        recyclerRanking.adapter?.notifyDataSetChanged()
                    }
                    else -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if ((context as RankingCharringProfileActivity).dialog?.isShowing == true)
                    (context as RankingCharringProfileActivity).dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }
}