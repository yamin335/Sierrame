package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.streme.activity.AuthenticationActivity
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.CountryListActivity
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.getCountryCodes
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.models.Country
import com.mmfinfotech.streameApp.onBoarding.WelComeActivity
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_advance_setting.*
import retrofit2.Call
import java.util.*


class AdvanceSettingActivity : DashBoardBaseActivity() {
    private var country: Country? = null
    private val TAG: String? = AdvanceSettingActivity::class.java.simpleName

    companion object {
        fun getInstance(
                context: Context?
        ) = Intent(context, AdvanceSettingActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advance_setting)
        initView()
        setListners()
    }

    private fun initView() {
        findViewById<ConstraintLayout>(R.id.includeToolbarAdvanceSetting)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.advance_setting)

        switchSecretMode?.isChecked = appPreferences?.getSecretMode(this@AdvanceSettingActivity) == "1"
        switchViewPrivateAllowlist?.isChecked = appPreferences?.getPrivateFollowList(this@AdvanceSettingActivity) == "1"
        if (appPreferences?.getPhoneAuthentication(this@AdvanceSettingActivity) == "1")
            textViewPhoneAuthenticationNumber?.text = appPreferences?.getPhone(this@AdvanceSettingActivity)
        else {
            textViewPhoneAuthentication?.setOnClickListener {
                startActivity(AuthenticationActivity.getInstance(this@AdvanceSettingActivity, AuthenticationActivity.ActionSettings))
            }
        }
    }

    private fun setListners() {
        findViewById<ConstraintLayout>(R.id.includeToolbarAdvanceSetting)?.findViewById<ImageView>(R.id.imageToolbarBack)?.setOnClickListener {
            onBackPressed()
        }

        textViewLogout.setOnClickListener {
            exitDialog(this@AdvanceSettingActivity, getString(R.string.txt_logout), getString(R.string.txt_are_you_sure_you_want_to_logout), object : View.OnClickListener {
                override fun onClick(v: View?) {
                    when (v?.id) {
                        R.id.buttonSave -> {
                            callApiLogout()
                        }
                        R.id.buttonCancel -> {
                        }
                    }
                }
            })
        }
        textViewConactUs.setOnClickListener {
            startActivity(ContactUsActivity.getInstance(this@AdvanceSettingActivity))
        }
        textViewSelectRegion.setOnClickListener {
            startActivityForResult(
                    CountryListActivity.getInstance(
                            this@AdvanceSettingActivity), AppConstants.RequestCode.activityCountryList)
        }
        textViewEditText.setOnClickListener {
            startActivity(ContactUsActivity.getInstance(this@AdvanceSettingActivity))

        }
        switchSaveTheOrignalPhoto.setOnClickListener {
            callSettingOriginalPhoto()
        }
        switchViewPrivateAllowlist.setOnClickListener {
            callApiPrivateFollowerList()
        }
        switchViewPrivateAccount.setOnClickListener {
            callPrivateAccount()
        }

        switchSecretMode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            callApiSecret()
        })
        textViewBlockUser?.setOnClickListener { startActivity(BlockedUserActivity.getInstance(this@AdvanceSettingActivity)) }
    }

    private fun callApiLogout() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@AdvanceSettingActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callLogout: Call<JsonObject?>? = apiService?.callLogout(headers)
        callApi(true, callLogout, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        appPreferences?.clearPreferences(this@AdvanceSettingActivity)
                        appPreferences?.setAuthToken(this@AdvanceSettingActivity, AppConstants.Defaults.string)
                        startActivity(WelComeActivity.getInstance(this@AdvanceSettingActivity))
                        finishAffinity()
                    }

                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }

    private fun callApiSecret() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? =
                ApiClient(this@AdvanceSettingActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callSecretMode: Call<JsonObject?>? = apiService?.callsettingSecret(headers)
        callApi(true, callSecretMode, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        val secretMode = getStringFromJson(record, "secret_mode", AppConstants.Defaults.string)
                        ShowAlertInformation(this@AdvanceSettingActivity, msg)
                        appPreferences?.setSecretMode(this@AdvanceSettingActivity, secretMode)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@AdvanceSettingActivity, msg)
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }

    private fun callSettingOriginalPhoto() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? =
                ApiClient(this@AdvanceSettingActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callOriginalPhoto: Call<JsonObject?>? = apiService?.callsettingOriginalPhoto(headers)
        callApi(true, callOriginalPhoto, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@AdvanceSettingActivity, msg)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@AdvanceSettingActivity, msg)
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }

    private fun callApiPrivateFollowerList() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? =
                ApiClient(this@AdvanceSettingActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callprivateFollow: Call<JsonObject?>? = apiService?.callsettingPrivateFollowerList(headers)
        callApi(true, callprivateFollow, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@AdvanceSettingActivity, msg)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@AdvanceSettingActivity, msg)
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }

    private fun callPrivateAccount() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? =
                ApiClient(this@AdvanceSettingActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callPrivateAccount: Call<JsonObject?>? = apiService?.callsettingPrivateAccount(headers)
        callApi(true, callPrivateAccount, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@AdvanceSettingActivity, msg)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@AdvanceSettingActivity, msg)
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }

    private fun setCountryCode() {
        if (country == null) {
            val locale: String? = getIsoCountryCode(this)
            val arr: ArrayList<Country?>? =
                    getCountryCodes(this@AdvanceSettingActivity, AppConstants.AssertName.assertCountryCode)
            arr?.forEach { country ->
                if (country?.isoCode?.equals(locale, ignoreCase = true) == true) this.country =
                        country
            }
        }
        val showCode = "+${country?.name}"
        textViewRegion.text = showCode



//        val tz: TimeZone = TimeZone.getTimeZone(textViewRegion.text.toString())
//        Log.d("riti", "time Zone tz ${tz}")

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