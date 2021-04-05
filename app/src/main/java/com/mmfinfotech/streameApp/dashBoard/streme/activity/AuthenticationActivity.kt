package com.mmfinfotech.streameApp.dashBoard.streme.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.CountryListActivity
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.getCountryCodes
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.models.Country
import com.mmfinfotech.streameApp.models.CommonResponse
import com.mmfinfotech.streameApp.models.LiveStreamCategory
import com.mmfinfotech.streameApp.models.LiveStreamHashTag
import com.mmfinfotech.streameApp.models.LiveStreamHashTagCategoryResponse
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_athentication.*
import retrofit2.Call
import java.util.*

class AuthenticationActivity : DashBoardBaseActivity() {
    private var country: Country? = null

    companion object {
        const val ActionLive = "action_live"
        const val ActionSettings = "action_setting"
        fun getInstance(context: Context?, action: String?) = Intent(context, AuthenticationActivity::class.java).apply {
            setAction(action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athentication)
        setListners()
        setCountryCode()
    }

    private fun setListners() {
        imageBack.setOnClickListener {
            onBackPressed()
        }
        textViewCountryCode.setOnClickListener {
            startActivityForResult(
                    CountryListActivity.getInstance(
                            this@AuthenticationActivity), AppConstants.RequestCode.activityCountryList)
        }
        imageViewSend.setOnClickListener {
            if ((validationEmptyField(this@AuthenticationActivity, editTextPhone) == true)) {
                sendVerification()
            } else {
                Toast.makeText(this, "Please enter phone number!", Toast.LENGTH_LONG).show()
            }
        }
        buttonComplete.setOnClickListener {
            sendCompleteVerification()
        }

    }

    private fun sendCompleteVerification() {
        val hasTagArray: ArrayList<LiveStreamHashTag?> = ArrayList()
        val arrcategoryArray: ArrayList<LiveStreamCategory?> = ArrayList()

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("verify_code", editTextPhoneVarification.text.toString())
        val apiService: MyApiEndpointInterface? = ApiClient(this@AuthenticationActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callSignUp: Call<LiveStreamHashTagCategoryResponse?>? = apiService?.callVerifyCode(headers, sendParams)
        callRemoteApi(true, callSignUp, object : ApiClient.ApiCallbackListener<LiveStreamHashTagCategoryResponse> {
            override fun onDataFetched(response: LiveStreamHashTagCategoryResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@AuthenticationActivity, response?.status, response?.message ?: message, response, object : ApiClient.ApiResponseListener<LiveStreamHashTagCategoryResponse> {
                    override fun onSuccess(response: LiveStreamHashTagCategoryResponse) {
                        val uuid = response?.record?.uuid
                        val hashArray = response?.record?.hashtags ?: return
                        val categoryArray = response.record.category ?: return

                        for (tag in hashArray) {
                            hasTagArray.add(tag)
                        }

                        for (category in categoryArray) {
                            arrcategoryArray.add(category)
                        }

                        if (intent?.action == ActionLive)
                            startActivity(AuthenticatLive.getInstance(this@AuthenticationActivity, uuid, hasTagArray, arrcategoryArray))
//                        else if (intent?.action == ActionSettings)
//                            startActivity(AuthenticationActivity.getInstance(this@AuthenticationActivity, AuthenticationActivity.ActionLive))
                        finish()
                    }

                    override fun onFailed(status: String, message: String) {
                        ShowAlertInformation(this@AuthenticationActivity, message)
                    }
                })
            }
        })
    }

    private fun sendVerification() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"

        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("phone", editTextPhone.text.toString())
        sendParams?.addProperty("country_code", textViewCountryCode.text.toString().trim())

        val apiService: MyApiEndpointInterface? =
                ApiClient(this@AuthenticationActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callSignUp: Call<CommonResponse?>? = apiService?.callSendVarification(headers, sendParams)
        callRemoteApi(true, callSignUp, object : ApiClient.ApiCallbackListener<CommonResponse> {
            override fun onDataFetched(response: CommonResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@AuthenticationActivity, response?.status, response?.message ?: message, response, object : ApiClient.ApiResponseListener<CommonResponse> {
                    override fun onSuccess(response: CommonResponse) {
                        ShowAlertInformation(this@AuthenticationActivity, response.message)
                    }

                    override fun onFailed(status: String, message: String) {
                        ShowAlertInformation(this@AuthenticationActivity, message)
                    }
                })
            }
        })
    }

    private fun setCountryCode() {
        if (country == null) {
            val locale: String? = getIsoCountryCode(this)
            val arr: ArrayList<Country?>? =
                    getCountryCodes(this@AuthenticationActivity, AppConstants.AssertName.assertCountryCode)
            arr?.forEach { country ->
                if (country?.isoCode?.equals(locale, ignoreCase = true) == true) this.country =
                        country
            }
        }
        val showCode = "+${country?.countryCode}"
        textViewCountryCode.text = showCode
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.RequestCode.activityCountryList -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        country = data?.getParcelableExtra(AppConstants.IntentExtras.country)
                        setCountryCode()
                    }

                }
            }
        }
    }
}