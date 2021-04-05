package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.base.NetworkBaseActivity
import com.mmfinfotech.streameApp.dashBoard.activity.CreditCardActivity
import com.mmfinfotech.streameApp.dashBoard.adapter.CoinAdapter
import com.mmfinfotech.streameApp.models.Coin
import com.mmfinfotech.streameApp.util.getIntFromJson
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.partial_layout_buy_coins_bottomsheet.*
import retrofit2.Call
import java.util.HashMap

class BuyCoinsActivity : NetworkBaseActivity() {
    private val tag: String? = BuyCoinsActivity::class.java.simpleName
    private var arrCoins: ArrayList<Coin?>? = null

    companion object {
        fun getInstance(context: Context?) = Intent(context, BuyCoinsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partial_layout_buy_coins_bottomsheet)

        arrCoins = ArrayList()

        val space = resources?.getDimensionPixelSize(R.dimen._10sdp)
        recyclerViewBuyCoins.adapter = CoinAdapter(this@BuyCoinsActivity, arrCoins)
        recyclerViewBuyCoins?.addItemDecoration(GridSpacingItemDecoration(3, space ?: 0, true))

        buttonBuyCoins?.setOnClickListener {
            startActivity(
                CreditCardActivity.getInstance(this@BuyCoinsActivity, arrCoins?.get((recyclerViewBuyCoins?.adapter as CoinAdapter).getSelectedCoin()))
            )
        }

        getBuyCoins()
    }

    private fun getBuyCoins() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@BuyCoinsActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@BuyCoinsActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callHotTheme: Call<JsonObject?>? = apiService?.callSubscriptionPlanList(headers)
        callApi(true, callHotTheme, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonArrayFromJson(mainObject, record, JsonArray())
                        for (i in 0 until (record?.size() ?: 0)) {
                            val item = getJsonObjFromJson(record, i, JsonObject())
                            arrCoins?.add(
                                Coin(
                                    id = getIntFromJson(item, "id", AppConstants.Defaults.integer),
                                    title = getStringFromJson(item, "title", AppConstants.Defaults.string),
                                    icon = getStringFromJson(item, "icon", AppConstants.Defaults.string),
                                    price = getIntFromJson(item, "price", AppConstants.Defaults.integer),
                                    currency = getStringFromJson(item, "currency", AppConstants.Defaults.string),
                                    coins = getIntFromJson(item, "coins", AppConstants.Defaults.integer),
                                    description = getStringFromJson(item, "description", AppConstants.Defaults.string),
                                    condition_description = getStringFromJson(item, "condition_description", AppConstants.Defaults.string),
                                    type = getIntFromJson(item, "type", AppConstants.Defaults.integer),
                                    status = getIntFromJson(item, "status", AppConstants.Defaults.integer),
                                    added_on = getStringFromJson(item, "added_on", AppConstants.Defaults.string),
                                    update_on = getStringFromJson(item, "update_on", AppConstants.Defaults.string),
                                )
                            )
                        }
                    }
                    else -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@BuyCoinsActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
                recyclerViewBuyCoins.adapter?.notifyDataSetChanged()
            }

            override fun onFailure() {

            }
        })
    }
}