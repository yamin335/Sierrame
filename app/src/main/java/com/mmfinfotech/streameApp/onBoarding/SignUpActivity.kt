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
import com.mmfinfotech.streameApp.models.SignUpResponse
import com.mmfinfotech.streameApp.models.SocialLogin
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
                    signUpApi("${findViewById<RadioButton>(radioGroupSignUpGender.checkedRadioButtonId).text}")
                } else {
                    ShowAlertInformation(this@SignUpActivity,getString(R.string.msg_please_selectgender))
                }
            } else {
//                Toast.makeText(this@SignUpActivity, "please check", Toast.LENGTH_LONG).show()
            }
        }

        imgButtonBack?.setOnClickListener { onBackPressed() }
    }

    private fun signUpApi(gender: String?) {
        val sendParams: JsonObject = JsonObject().apply {
            addProperty("username", edittextSignUpName.text.toString())
            addProperty("email", edittextSignUpEmail.text.toString().trim())
            addProperty("password", edittextSignUpPassword.text.toString().trim())
            addProperty("gender", gender.toString())
            addProperty("fcm_token",appPreferences?.getFcmToken(this@SignUpActivity))
            addProperty("device_id", "d3269c9f69162a30")
            addProperty("os_type", "28")
            addProperty("device_name", "1")
            addProperty("device_modal", "1")
        }


        val apiService: MyApiEndpointInterface? =
            ApiClient(this@SignUpActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callSignUp: Call<SignUpResponse?>? = apiService?.callSignUp(sendParams)
        callRemoteApi(true, callSignUp, object : ApiClient.ApiCallbackListener<SignUpResponse> {
            override fun onDataFetched(response: SignUpResponse?) {
                val token = response?.record?.token
                if (token.isNullOrBlank()) {
                    ShowAlertFailed(this@SignUpActivity, response?.message)
                    return
                }
                appPreferences?.setAuthToken(this@SignUpActivity, token)
                appPreferences?.setEmail(this@SignUpActivity, response.record.email)
                appPreferences?.setFullName(this@SignUpActivity, response.record.name)
                appPreferences?.setGander(this@SignUpActivity, response.record.gender)
                appPreferences?.setStremeID(this@SignUpActivity, response.record.username)
                startActivity(DashBoardActivity.getInstance(this@SignUpActivity))
                finishAffinity()
            }

            override fun onFailed(status: String, message: String?) {
                ShowAlertFailed(this@SignUpActivity, message)
            }
        })
    }

    private fun validate(): Boolean {
        return (validationEmptyField(this@SignUpActivity, edittextSignUpName) == true)
                && (validateEmail(this@SignUpActivity, edittextSignUpEmail) == true)
                && (validatePassword(this@SignUpActivity, edittextSignUpPassword) == true)
    }
}