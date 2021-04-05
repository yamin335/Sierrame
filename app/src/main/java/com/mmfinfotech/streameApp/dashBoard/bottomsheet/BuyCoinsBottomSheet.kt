package com.mmfinfotech.streameApp.dashBoard.bottomsheet

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.activity.CreditCardActivity
import com.mmfinfotech.streameApp.dashBoard.adapter.CoinAdapter
import com.mmfinfotech.streameApp.dashBoard.live.activity.LiveActivity
import com.mmfinfotech.streameApp.models.Coin
import com.mmfinfotech.streameApp.models.Gift
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


/**
 * Created By Somil Rawal on Tue-29-December-2020.
 */
class BuyCoinsBottomSheet : BottomSheetDialogFragment() {

    private var listners: Listeners? = null
    private var arrCoins: ArrayList<Coin?>? = null

    companion object {
        val TAG: String? = BuyCoinsBottomSheet::class.java.simpleName
        fun getInstance() = BuyCoinsBottomSheet().apply { arguments = Bundle().apply { } }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val space = context?.resources?.getDimensionPixelSize(R.dimen._100sdp)
        val windowHeight = getWindowHeight() - (space ?: 0)
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is Listeners) listners = context
//        else throw RuntimeException("$context must implement Buy Coins Bottom Listener")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val arr = arguments?.getParcelableArrayList<Gift?>("Gifts")
        arrCoins = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.partial_layout_buy_coins_bottomsheet, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val space = context?.resources?.getDimensionPixelSize(R.dimen._10sdp)
        recyclerViewBuyCoins.adapter = CoinAdapter(context, arrCoins)
        recyclerViewBuyCoins?.addItemDecoration(GridSpacingItemDecoration(3, space ?: 0, true))

//        recyclerViewBuyCoins?.addOnItemTouchListener(RecyclerTouchListener(context, recyclerViewBuyCoins, object : RecyclerTouchListener.ClickListener {
//            override fun onClick(view: View, position: Int) {
////                listners?.onItemClick(arrGifts?.get(position))
//                arrCoins?.get(position)?.isSelected = arrCoins?.get(position)?.isSelected?.not()
//                recyclerViewBuyCoins?.adapter?.notifyItemChanged(position)
//            }
//
//            override fun onLongClick(view: View, position: Int) {}
//        }))
        getBuyCoins()

        buttonBuyCoins?.setOnClickListener {
            dismiss()
            startActivity(CreditCardActivity.getInstance(context, arrCoins?.get((recyclerViewBuyCoins?.adapter as CoinAdapter).getSelectedCoin())))
        }

    }

    private fun getBuyCoins() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"

        val apiService: MyApiEndpointInterface? = ApiClient(context).getClient()?.create(MyApiEndpointInterface::class.java)
        val callHotTheme: Call<JsonObject?>? = apiService?.callSubscriptionPlanList(headers)
        (context as LiveActivity).callApi(true, callHotTheme, object : OnApiResponse {
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
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if ((context as LiveActivity).dialog?.isShowing == true)
                    (context as LiveActivity).dialog?.dismiss()
                recyclerViewBuyCoins.adapter?.notifyDataSetChanged()
            }

            override fun onFailure() {

            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        listners = null
    }

    interface Listeners {
        fun onItemClick(gift: Gift?)
    }
}