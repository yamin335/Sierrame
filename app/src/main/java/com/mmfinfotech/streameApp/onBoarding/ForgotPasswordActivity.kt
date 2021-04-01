package com.mmfinfotech.streameApp.onBoarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.base.NetworkBaseActivity
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_credit_card.*
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*

class ForgotPasswordActivity : NetworkBaseActivity() {
    private val tag: String? = ForgotPasswordActivity::class.java.simpleName

    companion object {
        fun getInstance(context: Context?) = Intent(context, ForgotPasswordActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
    }

    fun onVerify(view: View) {
//        startActivity(ResetPasswordActivity.getInstance(this@ForgotPasswordActivity))
        if (validate()) resetPassword()
    }

    private fun validate(): Boolean = (validationEmptyField(this@ForgotPasswordActivity, editTextForgotPasswordEmailMobile) == true)
                && (validateEmail(this@ForgotPasswordActivity, editTextForgotPasswordEmailMobile) == true)

    fun onBack(view: View) = onBackPressed()

    private fun resetPassword() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@ForgotPasswordActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@ForgotPasswordActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val email: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            editTextForgotPasswordEmailMobile?.text?.toString() ?: AppConstants.Defaults.string
        )

        val callSubscriptionPlanBuy: Call<JsonObject?>? = apiService?.callResetPassword(headers, email)

        callApi(true, callSubscriptionPlanBuy, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@ForgotPasswordActivity, msg, object: View.OnClickListener{
                            override fun onClick(v: View?) {
                                finish()
                            }
                        })
                    }
                    else -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@ForgotPasswordActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true) dialog?.dismiss()
            }

            override fun onFailure() {

            }
        })
    }
}