package com.mmfinfotech.streameApp.dashBoard.streme.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.CountryListActivity
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.getCountryCodes
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.models.Country
import com.mmfinfotech.streameApp.model.Category
import com.mmfinfotech.streameApp.model.Hashtags
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

            }

        }
        buttonComplete.setOnClickListener {
            sendCompleteVerification()
        }

    }

    private fun sendCompleteVerification() {
        val hasTagArray: ArrayList<Hashtags?>? = ArrayList()
        val arrcategoryArray: ArrayList<Category?>? = ArrayList()

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("verify_code", editTextPhoneVarification.text.toString())
        val apiService: MyApiEndpointInterface? = ApiClient(this@AuthenticationActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callSignup: Call<JsonObject?>? = apiService?.callVerifyCode(headers, sendParams)
        callApi(true, callSignup, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val recordObj = getJsonObjFromJson(mainObject, record, JsonObject())
                        val uuid = getStringFromJson(recordObj, "uuid", "")
                        val hashArray = getJsonArrayFromJson(recordObj, "hashtags", JsonArray())

                        for (i in 0 until hashArray!!.size()) {
                            val hashTagsObjects = getJsonObjFromJson(hashArray, i, JsonObject())
                            val id = getStringFromJson(hashTagsObjects, "id", AppConstants.Defaults.string)
                            val tag_name = getStringFromJson(hashTagsObjects, "tag_name", AppConstants.Defaults.string)
                            hasTagArray?.add(Hashtags(id, tag_name))
                        }
                        val categoryArray = getJsonArrayFromJson(recordObj, "category", JsonArray())
                        for (i in 0 until categoryArray!!.size()) {
                            val categoryObjects = getJsonObjFromJson(categoryArray, i, JsonObject())
                            val id = getStringFromJson(categoryObjects, "id", AppConstants.Defaults.string)
                            val name = getStringFromJson(categoryObjects, "name", AppConstants.Defaults.string)
                            arrcategoryArray?.add(Category(id, name))
                        }

                        if (intent?.action == ActionLive)
                            startActivity(AuthenticatLive.getInstance(this@AuthenticationActivity, uuid, hasTagArray, arrcategoryArray))
//                        else if (intent?.action == ActionSettings)
//                            startActivity(AuthenticationActivity.getInstance(this@AuthenticationActivity, AuthenticationActivity.ActionLive))
                        finish()
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
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
        val callSignup: Call<JsonObject?>? = apiService?.callSendVarification(headers, sendParams)
        callApi(true, callSignup, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
//                        Toast.makeText(this@AuthenticationActivity, "${msg}", Toast.LENGTH_LONG).show()
                        ShowAlertInformation(this@AuthenticationActivity,msg)
                    }
                    NotVerify -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@AuthenticationActivity,msg)
//                        Toast.makeText(this@AuthenticationActivity, "${msg}", Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
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