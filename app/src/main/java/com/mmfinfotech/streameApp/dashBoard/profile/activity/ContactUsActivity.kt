package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import java.util.HashMap

class ContactUsActivity : DashBoardBaseActivity() {
    companion object {
        fun getInstance(
                context: Context?
        ) = Intent(context, ContactUsActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        initView()
        setListner()
    }

    private fun setListner() {
        btn_SentContact.setOnClickListener {
            if(validation()){
                callContactUs()
            }
            else
            {}
        }
    }

    private fun initView() {
        findViewById<ConstraintLayout>(R.id.includeToolbarContact)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.titleToolbarContact)
        findViewById<ConstraintLayout>(R.id.includeToolbarContact)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.setImageDrawable(getDrawable(R.drawable.ic_help_))
    }
    private fun validation(): Boolean {
        return (validateEmail(this@ContactUsActivity, editTextemail) == true)
                && (validationEmptyField(this@ContactUsActivity, editTextId) == true)
                && (validationEmptyField(this@ContactUsActivity, editTextContactUsCat) == true)
                && (validationEmptyField(this@ContactUsActivity, editTextCaseName) == true)
                && (validationEmptyField(this@ContactUsActivity, editTextExplaination) == true)
                && (validationEmptyField(this@ContactUsActivity, editTextProblemTime) == true)
                && (validationEmptyField(this@ContactUsActivity, editTextDevice) == true)
                && (validationEmptyField(this@ContactUsActivity, editTextDeviceOs) == true)
                && (validationEmptyField(this@ContactUsActivity, editTextAppVersion) == true)
    }
    private fun callContactUs() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("email", editTextemail?.text.toString())
        sendParams?.addProperty("contact_us_category", editTextContactUsCat?.text.toString())
        sendParams?.addProperty("case_name", editTextCaseName?.text.toString())
        sendParams?.addProperty("explanation", editTextExplaination?.text.toString())
        sendParams?.addProperty("problem_occurs_time", editTextProblemTime?.text.toString())
        sendParams?.addProperty("device", editTextDevice?.text.toString())
        sendParams?.addProperty("os", editTextDeviceOs?.text.toString())
        sendParams?.addProperty("app_version", editTextAppVersion?.text.toString())

        val apiService: MyApiEndpointInterface? =
                ApiClient(this@ContactUsActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callContactUs: Call<JsonObject?>? = apiService?.callsettingContactUs(headers,sendParams)
        callApi(true, callContactUs, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
//                        ShowAlertInformation(this@ContactUsActivity, msg)
                   finish()
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
}