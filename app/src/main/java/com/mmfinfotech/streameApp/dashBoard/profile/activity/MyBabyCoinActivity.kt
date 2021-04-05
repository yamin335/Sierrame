package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.base.NetworkBaseActivity
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.MyBabyCoinAdapter
import com.mmfinfotech.streameApp.models.BabyCoin
import com.mmfinfotech.streameApp.util.getIntFromJson
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_my_baby_coin.*
import kotlinx.android.synthetic.main.fragment_leader_board.*
import kotlinx.android.synthetic.main.partial_layout_buy_coins_bottomsheet.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class MyBabyCoinActivity : NetworkBaseActivity() {
    private val tag: String? = MyBabyCoinActivity::class.java.simpleName
    var arrPurchase: ArrayList<BabyCoin?>? = null
    var arrUsage: ArrayList<BabyCoin?>? = null
    private var myBabyCoinAdapter: MyBabyCoinAdapter? = null

    companion object {
        fun getInstance(context: Context?) = Intent(context, MyBabyCoinActivity::class.java).apply {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_baby_coin)

        arrPurchase = ArrayList()
        arrUsage = ArrayList()

        textViewPurchaseRecord?.isSelected = true
        textViewUsageRecord?.isSelected = false
        textViewPurchaseRecord?.setOnClickListener { toggleButtonView(it) }
        textViewUsageRecord?.setOnClickListener { toggleButtonView(it) }
        findViewById<ConstraintLayout>(R.id.includeToolbarBabyCoin)?.findViewById<ImageView>(R.id.imageToolbarBack)
            ?.setOnClickListener {
                onBackPressed()
            }
        findViewById<ConstraintLayout>(R.id.includeToolbarBabyCoin)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.my_baby_coin)

        val spaceHorizontal = resources?.getDimensionPixelSize(R.dimen._10sdp)
        val spaceVertical = resources?.getDimensionPixelSize(R.dimen._10sdp)
        recyclerViewBabyCoin?.addItemDecoration(SpaceItemDecoration(spaceHorizontal ?: 0, spaceVertical ?: 0))

        val divider = DividerItemDecoration(this@MyBabyCoinActivity, LinearLayoutManager.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this@MyBabyCoinActivity, R.drawable.divider_leader_board)!!)
        recyclerViewBabyCoin?.addItemDecoration(divider)

        myBabyCoinAdapter =  MyBabyCoinAdapter(this@MyBabyCoinActivity)
        myBabyCoinAdapter?.setProfileVisibility(false)
        recyclerViewBabyCoin.adapter = myBabyCoinAdapter

        textViewBuy?.setOnClickListener { startActivity(BuyCoinsActivity.getInstance(this@MyBabyCoinActivity)) }

        callData()
    }

    private fun toggleButtonView(view: View) {
        textViewPurchaseRecord?.isSelected = view.id == R.id.textViewPurchaseRecord
        textViewUsageRecord?.isSelected = view.id == R.id.textViewUsageRecord
        myBabyCoinAdapter?.setProfileVisibility(view.id == R.id.textViewUsageRecord)
        myBabyCoinAdapter?.setData( if (view.id == R.id.textViewPurchaseRecord) arrPurchase else arrUsage)
    }

    private fun callData() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@MyBabyCoinActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@MyBabyCoinActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callMyCoins: Call<JsonObject?>? = apiService?.callMyCoins(headers)
        callApi(true, callMyCoins, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val coins = getJsonObjFromJson(record, "coins", JsonObject())
                        textViewPaidCoin.text = getStringFromJson(coins, "paid_coins", AppConstants.Defaults.string)
                        textViewPresentCoin.text = getStringFromJson(coins, "present_coins", AppConstants.Defaults.string)

                        val purchaseRecords = getJsonArrayFromJson(record, "purchase_records", JsonArray())
                        val usageRecords = getJsonArrayFromJson(record, "usage_records", JsonArray())
                        for (i in 0 until (purchaseRecords?.size() ?: 0)) {
                            val item = getJsonObjFromJson(purchaseRecords, i, JsonObject())
                            arrPurchase?.add(
                                BabyCoin(
                                    id = getIntFromJson(item, "id", AppConstants.Defaults.integer),
                                    coins = getIntFromJson(item, "coins", AppConstants.Defaults.integer),
                                    giftedBy = getIntFromJson(item, "gifted_by", AppConstants.Defaults.integer),
                                    planId = getIntFromJson(item, "plan_id", AppConstants.Defaults.integer),
                                    status = getIntFromJson(item, "status", AppConstants.Defaults.integer),
                                    addedOn = getStringFromJson(item, "added_on", AppConstants.Defaults.string),
                                    senderName = getStringFromJson(item, "sender_name", AppConstants.Defaults.string),
                                    senderImage = getStringFromJson(item, "sender_image", AppConstants.Defaults.string),
                                    planName = getStringFromJson(item, "plan_name", AppConstants.Defaults.string)
                                )
                            )
                        }
                        for (i in 0 until (usageRecords?.size() ?: 0)) {
                            val item = getJsonObjFromJson(usageRecords, i, JsonObject())
                            arrUsage?.add(
                                BabyCoin(
                                    id = getIntFromJson(item, "id", AppConstants.Defaults.integer),
                                    coins = getIntFromJson(item, "coins", AppConstants.Defaults.integer),
                                    streamerId = getIntFromJson(item, "streamer_id", AppConstants.Defaults.integer),
                                    planId = getIntFromJson(item, "plan_id", AppConstants.Defaults.integer),
                                    status = getIntFromJson(item, "status", AppConstants.Defaults.integer),
                                    addedOn = getStringFromJson(item, "added_on", AppConstants.Defaults.string),
                                    senderName = getStringFromJson(item, "streamer_name", AppConstants.Defaults.string),
                                    senderImage = getStringFromJson(item, "streamer_image", AppConstants.Defaults.string)
                                )
                            )
                        }

                        myBabyCoinAdapter?.setData(arrPurchase)
                    }
                    else -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@MyBabyCoinActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
                recyclerViewBabyCoin.adapter?.notifyDataSetChanged()
            }

            override fun onFailure() {

            }
        })
    }

}