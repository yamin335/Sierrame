package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.LeaderBoardAdapter
import com.mmfinfotech.streameApp.models.Rank
import com.mmfinfotech.streameApp.util.getIntFromJson
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.ApiClient
import com.mmfinfotech.streameApp.util.retrofit.MyApiEndpointInterface
import com.mmfinfotech.streameApp.util.retrofit.OnApiResponse
import com.mmfinfotech.streameApp.util.retrofit.Success
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_hot_theme.*
import kotlinx.android.synthetic.main.fragment_leader_board.*
import kotlinx.android.synthetic.main.fragment_notification.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class LeaderBoardFragment : Fragment(), View.OnClickListener {

    private var arrRank: ArrayList<Rank?>? = null

    companion object {
        @JvmStatic
        fun getInstance() = LeaderBoardFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
        arrRank = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_leader_board, container, false).apply {
        findViewById<AppCompatTextView>(R.id.textViewLeaderBoardTotal).apply {
            isSelected = true
            setOnClickListener(this@LeaderBoardFragment)
        }
        findViewById<AppCompatTextView>(R.id.textViewLeaderBoardMonth).setOnClickListener(this@LeaderBoardFragment)
        findViewById<AppCompatTextView>(R.id.textViewLeaderBoardDay).setOnClickListener(this@LeaderBoardFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spaceHorizontal = context?.resources?.getDimensionPixelSize(R.dimen._10sdp)
        val spaceVertical = context?.resources?.getDimensionPixelSize(R.dimen._10sdp)
        recyclerviewLeaderBoard?.addItemDecoration(SpaceItemDecoration(spaceHorizontal ?: 0, spaceVertical ?: 0))

        val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        context?.let { divider.setDrawable(ContextCompat.getDrawable(it, R.drawable.divider_leader_board)!!) }
        recyclerviewLeaderBoard?.addItemDecoration(divider)

        recyclerviewLeaderBoard?.adapter = LeaderBoardAdapter(context, arrRank)
        callApiForData()
    }

    override fun onClick(v: View?) {
        textViewLeaderBoardTotal?.isSelected = v?.id == R.id.textViewLeaderBoardTotal
        textViewLeaderBoardMonth?.isSelected = v?.id == R.id.textViewLeaderBoardMonth
        textViewLeaderBoardDay?.isSelected = v?.id == R.id.textViewLeaderBoardDay
        callApiForData()
    }

    private fun callApiForData() {
        arrRank?.clear()
        recyclerviewLeaderBoard?.adapter?.notifyDataSetChanged()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"
        val fetch: RequestBody = RequestBody.create(MediaType.parse("text/plain"), when {
            textViewLeaderBoardTotal.isSelected -> "1"
            textViewLeaderBoardMonth?.isSelected == true -> "2"
            textViewLeaderBoardDay?.isSelected == true -> "3"
            else -> AppConstants.Defaults.string
        })

        val apiService: MyApiEndpointInterface? = ApiClient(context).getClient()?.create(MyApiEndpointInterface::class.java)
        val callHotTheme: Call<JsonObject?>? = apiService?.callMainLeaderBoard(headers, fetch)
        (context as DashBoardActivity).callApi(true, callHotTheme, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, "record", JsonObject())
                        val rankArray = getJsonArrayFromJson(record, "rankes", JsonArray())

                        for (i in 0 until (rankArray?.size() ?: 0)) {
                            val item = getJsonObjFromJson(rankArray, i, JsonObject())
                            val spentCoins = getStringFromJson(item, "spent_coins", AppConstants.Defaults.string)
                            val id = getIntFromJson(item, "id", AppConstants.Defaults.integer)
                            val username = getStringFromJson(item, "username", AppConstants.Defaults.string)
                            val score = getIntFromJson(item, "score", AppConstants.Defaults.integer)
                            val userProfile = getStringFromJson(item, "user_profile", AppConstants.Defaults.string)
                            val updatedAt = getStringFromJson(item, "updated_at", AppConstants.Defaults.string)
                            val rank = getIntFromJson(item, "rank", AppConstants.Defaults.integer)
                            arrRank?.add(
                                Rank(
                                    id = id, spentCoins = spentCoins, username = username, score = score,
                                    userProfile = userProfile, updatedAt = updatedAt, rank = rank,
                                )
                            )
                        }
                        recyclerviewLeaderBoard?.adapter?.notifyDataSetChanged()

                    }
                }
                if ((context as DashBoardActivity).dialog?.isShowing == true)
                    (context as DashBoardActivity).dialog?.dismiss()
            }

            override fun onFailure() {}
        })

    }
}