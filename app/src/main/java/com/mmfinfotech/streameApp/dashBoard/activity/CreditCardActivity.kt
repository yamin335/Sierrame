package com.mmfinfotech.streameApp.dashBoard.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.base.NetworkBaseActivity
import com.mmfinfotech.streameApp.model.Coin
import com.mmfinfotech.streameApp.ui.creditCard.CreditCardEditText
import com.mmfinfotech.streameApp.ui.creditCard.CreditCardExpireEditText
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_credit_card.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class CreditCardActivity : NetworkBaseActivity() {
    private val tag: String? = CreditCardActivity::class.java.simpleName
    private var coinPlan: Coin? = null
    val VISA_PREFIX = "4"
    val MASTERCARD_PREFIX = "51,52,53,54,55,"
    val DISCOVER_PREFIX = "6011"
    val AMEX_PREFIX = "34,37,"

    companion object {
        fun getInstance(context: Context?, coin: Coin?) = Intent(context, CreditCardActivity::class.java).apply {
            putExtra("plainCoin", coin)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card)

        coinPlan = intent?.getParcelableExtra<Coin>("plainCoin")

        textViewCreditCardPlanPrice?.text = coinPlan?.price?.toString()

        editTextCreditCardHolderName?.addTextChangedListener { text: Editable? ->
            if (TextUtils.isEmpty(text))
                textViewCardCardHolderName?.text = getString(R.string.card_holder)
            else textViewCardCardHolderName?.text = text
        }

        editTextCreditCardNumber?.setListener(object : CreditCardEditText.Listener {
            override fun onTextChange(text: String?) {
                if (TextUtils.isEmpty(text)) textViewCardCardNumber?.text = "XXXX XXXX XXXX XXXX"
                else textViewCardCardNumber?.text = text
            }
        })

        editTextCreditCardExpire?.setListener(object : CreditCardExpireEditText.Listener {
            override fun onTextChange(text: String?) {
                if (TextUtils.isEmpty(text))
                    textViewCardCardExpire?.text = "XX/XX"
                else
                    textViewCardCardExpire?.text = text
            }
        })

        editTextCreditCardExpire?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        buttonCreditCardPay?.setOnClickListener {
            if (validate())
                BuyPlan()
            else
                Log.v(tag, "button Credit card Pay not okay")
        }
    }

//    fun getCardType(cardNumber: String): Int {
//        if (cardNumber.substring(0, 1) == VISA_PREFIX)
//            return VISA
//        else if (MASTERCARD_PREFIX.contains(cardNumber.substring(0, 2) + ","))
//            return MASTERCARD
//        else if (AMEX_PREFIX.contains(cardNumber.substring(0, 2) + ","))
//            return AMEX
//        else if (cardNumber.substring(0, 4) == DISCOVER_PREFIX)
//            return DISCOVER
//        return NONE
//    }

    private fun validate(): Boolean {
        editTextCreditCardHolderName?.error = null

        if (TextUtils.isEmpty(editTextCreditCardHolderName?.text)) {
            editTextCreditCardHolderName?.error = getString(R.string.txt_cant_be_empty)
            return false
        }

        val regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
                "(?<mastercard>5[1-5][0-9]{14})|" +
                "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
                "(?<amex>3[47][0-9]{13})|" +
                "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
                "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$"
        val cardNumber = editTextCreditCardNumber?.getCardNumber()
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(cardNumber ?: "")
        if(!matcher.matches()) {
            editTextCreditCardNumber?.error = getString(R.string.txt_card_no_incorrect)
            return false
        }

        if (editTextCreditCardExpire?.text?.matches("(?:0[1-9]|1[0-2])/[0-9]{2}".toRegex()) == false) {
            editTextCreditCardExpire?.error = getString(R.string.txt_invalid_date)
            return false
        }

        return true
    }

    private fun BuyPlan() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@CreditCardActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@CreditCardActivity).getClient()?.create(MyApiEndpointInterface::class.java)

        val plainId: String = coinPlan?.id?.toString() ?: AppConstants.Defaults.string
        val price: RequestBody = RequestBody.create(MediaType.parse("text/plain"), coinPlan?.price?.toString() ?: AppConstants.Defaults.string)
        val currency: RequestBody = RequestBody.create(MediaType.parse("text/plain"), coinPlan?.currency ?: AppConstants.Defaults.string)
        val coins: RequestBody = RequestBody.create(MediaType.parse("text/plain"), coinPlan?.coins?.toString() ?: AppConstants.Defaults.string)
        val cardNumber: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            editTextCreditCardNumber?.getCardNumber() ?: AppConstants.Defaults.string
        )
        val expire: RequestBody = RequestBody.create(MediaType.parse("text/plain"), editTextCreditCardExpire?.getExpireDate() ?: AppConstants
            .Defaults.string)
        val cardHolderName: RequestBody = RequestBody.create(MediaType.parse("text/plain"), editTextCreditCardHolderName?.text?.toString() ?: AppConstants
            .Defaults.string)
        val email: RequestBody = RequestBody.create(MediaType.parse("text/plain"), appPreferences?.getEmail(this@CreditCardActivity) ?: AppConstants.Defaults.string)


        val callSubscriptionPlanBuy: Call<JsonObject?>? = apiService?.callSubscriptionPlanBuy(
            headers, plainId, price, currency, coins, cardNumber,
            expire, cardHolderName, email
        )

        callApi(true, callSubscriptionPlanBuy, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                Log.v(tag, "Buy plan onSuccess $mainObject")
                when (status) {
                    Sccess -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    else -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@CreditCardActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }
}