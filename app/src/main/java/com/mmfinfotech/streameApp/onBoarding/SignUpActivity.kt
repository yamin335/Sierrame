package com.mmfinfotech.streameApp.onBoarding


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.RadioButton
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.OnBoardingBaseActivity
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.model.SocialLogin
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call

class SignUpActivity : OnBoardingBaseActivity() {
    private val tag: String? = SignUpActivity::class.java.simpleName
    private var socialLogin: SocialLogin? = null

    companion object {
        fun getInstance(context: Context?, socials: SocialLogin? = null) =
            Intent(context, SignUpActivity::class.java).apply {
                putExtra("So", socials)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        socialLogin = intent.getParcelableExtra("So") as? SocialLogin
        Log.d(tag, "socialLogin = ${socialLogin}")

        setListners()
        SocialLoginCheck()
    }

    private fun SocialLoginCheck() {
        if (socialLogin == null) {
            edittextSignUpName.setText("")
            edittextSignUpEmail.setText("")
        } else {
            edittextSignUpName.setText("${socialLogin?.socialFirstName}")
            edittextSignUpEmail.setText("${socialLogin?.socialemail}")
        }

    }

    private fun setListners() {
        textViewSignUpLogin.setOnClickListener {
            startActivity(SignInActivity.getInstance(this@SignUpActivity))
        }

        buttonSignUp.setOnClickListener {
            if (validate()) {
                if (radioGroupSignUpGender.checkedRadioButtonId != -1) {
                    signupApi("${findViewById<RadioButton>(radioGroupSignUpGender.checkedRadioButtonId).text}")
                } else {
                    ShowAlertInformation(this@SignUpActivity,getString(R.string.msg_please_selectgender))
                }
            } else {
//                Toast.makeText(this@SignUpActivity, "please check", Toast.LENGTH_LONG).show()
            }
        }

        imgButtonBack?.setOnClickListener { onBackPressed() }
    }

    private fun signupApi(gender: String?) {
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("username", edittextSignUpName.text.toString())
        sendParams?.addProperty("email", edittextSignUpEmail.text.toString().trim())
        sendParams?.addProperty("password", edittextSignUpPassword.text.toString().trim())
        sendParams?.addProperty("gender", gender.toString())
//        sendParams?.addProperty("device_id", getDeviceId(this))
        sendParams?.addProperty("device_id", "d3269c9f69162a30")
        sendParams?.addProperty("fcm_token",appPreferences?.getFcmToken(this@SignUpActivity))
//        sendParams?.addProperty("os_type", Build.VERSION.SDK_INT.toString())
        sendParams?.addProperty("os_type", "28")
//        sendParams?.addProperty("device_name", Build.BRAND)
        sendParams?.addProperty("device_name", "1")
//        sendParams?.addProperty("device_modal", Build.BOARD)
        sendParams?.addProperty("device_modal", "1")

        Log.d(tag, "signup sendparams request : $sendParams")

        val apiService: MyApiEndpointInterface? =
            ApiClient(this@SignUpActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callSignup: Call<JsonObject?>? = apiService?.callSignup(sendParams)
        callApi(true, callSignup, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val token = getStringFromJson(getJsonObjFromJson(mainObject, record, JsonObject()),"token","")
                        if (token != null && !TextUtils.isEmpty(token))
                            appPreferences?.setAuthToken(this@SignUpActivity, token)
//                      Toast.makeText(this@SignUpActivity, "$status", Toast.LENGTH_LONG).show()
                        val email = getStringFromJson(getJsonObjFromJson(mainObject, record, JsonObject()),"email","")
                        val userNameId = getStringFromJson(getJsonObjFromJson(mainObject, record, JsonObject()),"username","")
                        val gender = getStringFromJson(getJsonObjFromJson(mainObject, record, JsonObject()),"gender","")
                        if (token != null && !TextUtils.isEmpty(token))
                        appPreferences?.setEmail(this@SignUpActivity, email)
                        appPreferences?.setFullName(this@SignUpActivity, AppConstants.Defaults.string)
                        appPreferences?.setGander(this@SignUpActivity, gender)
                        appPreferences?.setStremeID(this@SignUpActivity, userNameId)
                        startActivity(DashBoardActivity.getInstance(this@SignUpActivity))
                        finishAffinity()
                    }
                    ValidationError->{
                        var str:String?=null
                        val errorArray= mainObject?.get("error")?.asJsonArray
                        for (i in 0 until errorArray?.size()!!){
                          str = errorArray.get(i).asString
                        }
                        ShowAlertFailed(this@SignUpActivity, str)
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    private fun validate(): Boolean {
        return (validationEmptyField(this@SignUpActivity, edittextSignUpName) == true)
                && (validateEmail(this@SignUpActivity, edittextSignUpEmail) == true)
                && (validatePassword(this@SignUpActivity, edittextSignUpPassword) == true)
    }
}