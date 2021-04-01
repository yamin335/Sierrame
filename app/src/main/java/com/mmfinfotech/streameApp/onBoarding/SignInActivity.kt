package com.mmfinfotech.streameApp.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.LoginDelegate
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.api.LineApiClient
import com.linecorp.linesdk.api.LineApiClientBuilder
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import com.linecorp.linesdk.auth.LineLoginResult
//import com.mmfinfotech.streameApp.BuildConfig
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.OnBoardingBaseActivity
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.model.SocialLogin
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*

import com.mmfinfotech.streameApp.utils.AppConstants
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject
import retrofit2.Call
import java.util.*

class SignInActivity : OnBoardingBaseActivity() {
    private val tag: String? = SignInActivity::class.java.simpleName
    
    private var callbackManager: CallbackManager? = null
    var mGoogleSignInClient: GoogleSignInClient? = null

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    private var lineApiClient: LineApiClient? = null
    private val loginDelegate: LoginDelegate? = LoginDelegate.Factory.create();

    companion object {
        fun getInstance(context: Context?) = Intent(context, SignInActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        FacebookSdk.setAutoLogAppEventsEnabled(false)
        LoginManager.getInstance().logOut()
        callbackManager = CallbackManager.Factory.create()

        val apiClientBuilder = LineApiClientBuilder(applicationContext, "1654427103")
        lineApiClient = apiClientBuilder.build()

        setListners()
    }

    private fun setListners() {
        textViewSignInForgotPassword.setOnClickListener {
            startActivity(ForgotPasswordActivity.getInstance(this@SignInActivity))
        }
        buttonSignInLogin.setOnClickListener {
            if (validation()) {
                loginApiCalling()
//                startActivity(DashBoardActivity.getInstance(this@SignInActivity))
            } else {
            }
//            startActivity(DashBoardActivity.getInstance(this@SignInActivity))
        }
        imageButtonFb.setOnClickListener {
            faceBookLogin()
        }
        imageButtonGoogle.setOnClickListener {
            googleLogin()
        }
        imgButtonBack.setOnClickListener {
            onBackPressed()
        }
        imageButtonLine.setOnClickListener {
            try {
                val loginIntent = LineLoginApi.getLoginIntent(
                    this@SignInActivity,
                    getString(R.string.channel_id),
                    LineAuthenticationParams.Builder()
                        .scopes(Arrays.asList(Scope.PROFILE))
                        // .nonce("<a randomly-generated string>") // nonce can be used to improve security
                        .build()
                );
                startActivityForResult(loginIntent, AppConstants.RequestCode.LineLoginRequestCode);

            } catch (e: Exception) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    private fun loginApiCalling() {
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("email", edittextSignInEmail.text.toString())
        sendParams?.addProperty("password", edittextSignInPassword.text.toString().trim())
        sendParams?.addProperty("fcm_token", appPreferences?.getFcmToken(this@SignInActivity))
        sendParams?.addProperty("device_id", "")
        sendParams?.addProperty("os_type", "AN")
        sendParams?.addProperty("device_name", "")
        sendParams?.addProperty("device_modal", "")

        val apiService: MyApiEndpointInterface? =
            ApiClient(this@SignInActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callSignin: Call<JsonObject?>? = apiService?.callSignin(sendParams)
        callApi(true, callSignin, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val token = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "token",
                            ""
                        )
                        val email = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "email",
                            ""
                        )
                        val userNameId = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "username",
                            ""
                        )
                        val gender = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "gender",
                            ""
                        )
                        val profile = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "profile",
                            ""
                        )
                        if (token != null && !TextUtils.isEmpty(token))
                            appPreferences?.setAuthToken(this@SignInActivity, token)
                        appPreferences?.setEmail(this@SignInActivity, email)
                        appPreferences?.setFullName(
                            this@SignInActivity,
                            AppConstants.Defaults.string
                        )
                        appPreferences?.setGander(this@SignInActivity, gender)
                        appPreferences?.setStremeID(this@SignInActivity, userNameId)
                        appPreferences?.setProfileImage(this@SignInActivity, profile)
                        startActivity(DashBoardActivity.getInstance(this@SignInActivity))
                        finishAffinity()
                    }
                    NotFound -> {
                        val msg =
                            getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@SignInActivity, msg, Toast.LENGTH_LONG).show()
                    }
                    ValidationError -> {
                        val msg = mainObject?.get("message")?.asString
                        ShowAlertFailed(this@SignInActivity, msg)
                    }
                    else -> {
                    }
                }
//                val otpType: String? =
//                getStringFromJson(record, otpType, AppConstants.Defaults.string)
//                val otp: Int? = getIntFromJson(record, otp, AppConstants.Defaults.integer)
//                appPreferences?.setEmail(mContext, email)
//                appPreferences?.setCountryCode(mContext, country?.countryCode)
//                appPreferences?.setPhone(mContext, edt_signup_mobile_number.text.toString().trim())
//                appPreferences?.setOtpType(mContext, otpType)

//                val intent = Intent(mContext, VerifyOtpActivity::class.java)
//                intent.action = AppConstants.IntentActions.actionSignUp
//                intent.putExtra(AppConstants.IntentExtras.otp, otp)
//                startActivity(intent)
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    private fun validation(): Boolean {
        return (validationEmptyField(this@SignInActivity, edittextSignInEmail) == true)
                && (validatePassword(this@SignInActivity, edittextSignInPassword) == true)
    }

    private fun googleLogin() {
        val signInIntent: Intent? = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, AppConstants.RequestCode.GoogleLoginRequestCode)
    }

    private fun faceBookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(tag, "Facebook token: " + loginResult.accessToken.token)
                    Log.v(tag, "Facebookdata Retriving data from facebook...")
                    val accessToken = loginResult.accessToken?.token
                    val request: GraphRequest? = GraphRequest.newMeRequest(
                        loginResult.accessToken
                    ) { `object`, response ->
                        Log.v(tag, "Facebookdata Data Retrived... ${`object`.toString()}")
                        getFaceBookData(`object`)
                    }

                    val parameters: Bundle? = Bundle()
                    parameters?.putString("fields", "id,name,first_name, last_name, email,link")
                    request?.parameters = parameters
                    request?.executeAsync()
                }

                override fun onCancel() {
                    Log.d(tag, "Facebook onCancel.")

                }

                override fun onError(error: FacebookException) {
                    Log.d(tag, "Facebook onError. ${error.message}")
                }
            })
    }

    private fun getstringFromJSON(jsonObject: JSONObject?, key: String, default: String): String {
        return if (jsonObject?.has(key) == true) {
            if (!jsonObject.isNull(key)) {
                jsonObject.getString(key)
            } else default
        } else default
    }

    private fun getFaceBookData(jsonObject: JSONObject?) {
//      {"id":"795457494206472","name":"Mmf Jain","first_name":"Mmf","last_name":"Jain","email":"mmf.jain@gmail.com"}
        val id = getstringFromJSON(jsonObject, "id", "r")//jsonObject?.getString("id")
        val name = getstringFromJSON(
            jsonObject,
            "name",
            "AppConstants.Defaults.string"
        )//jsonObject?.getString("name")
        val firstName = getstringFromJSON(
            jsonObject,
            "first_name",
            "AppConstants.Defaults.string"
        )//jsonObject?.getString("first_name")
        val lastName = getstringFromJSON(
            jsonObject,
            "last_name",
            "AppConstants.Defaults.string"
        )//jsonObject?.getString("last_name")
        val email = getstringFromJSON(
            jsonObject,
            "email",
            "AppConstants.Defaults.string"
        )//jsonObject?.getString("email")
        Log.v(tag, "Facebookdata Data $id $name $email $firstName $lastName")
        val socialLogin =
            SocialLogin(AppConstants.LoginTypes.FacebookLogin, id, firstName, lastName, name, email)
        socialLoginApi(socialLogin)

    }

    private fun socialLoginApi(SocialDetail: SocialLogin) {
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("email", SocialDetail.socialemail)
        sendParams?.addProperty("social_id", SocialDetail.socialId)
        sendParams?.addProperty("login_type", SocialDetail.socialType)
        sendParams?.addProperty("username", SocialDetail.socialFullName)
        sendParams?.addProperty("gender", "")
        sendParams?.addProperty("fcm_token", appPreferences?.getFcmToken(this@SignInActivity))
        sendParams?.addProperty("device_id", getDeviceId(this@SignInActivity))
        sendParams?.addProperty("device_modal", Build.BOARD)
        sendParams?.addProperty("device_name", Build.BRAND)
//        sendParams?.addProperty("os_type", BuildConfig.OS.toString())
        sendParams?.addProperty("os_type","1")
        Log.d("httt", "jslj Token $sendParams")
        Log.d("httt", "Token ${appPreferences?.getFcmToken(this@SignInActivity)}")
        val apiService: MyApiEndpointInterface? =
            ApiClient(this@SignInActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callSignin: Call<JsonObject?>? = apiService?.callSocialLogin(sendParams)
        callApi(true, callSignin, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val token = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "token",
                            ""
                        )
                        if (token != null && !TextUtils.isEmpty(token))
                            appPreferences?.setAuthToken(this@SignInActivity, token)
                        val email = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "email",
                            ""
                        )
                        val userNameId = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "username",
                            ""
                        )
                        val profile = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "profile",
                            ""
                        )
                        val gender = getStringFromJson(
                            getJsonObjFromJson(mainObject, record, JsonObject()),
                            "gender",
                            ""
                        )
                        if (token != null && !TextUtils.isEmpty(token))
                            appPreferences?.setAuthToken(this@SignInActivity, token)
                        appPreferences?.setEmail(this@SignInActivity, email)
                        appPreferences?.setFullName(
                            this@SignInActivity,
                            AppConstants.Defaults.string
                        )
                        appPreferences?.setGander(this@SignInActivity, gender)
                        appPreferences?.setStremeID(this@SignInActivity, userNameId)
                        appPreferences?.setProfileImage(this@SignInActivity, profile)
                        startActivity(DashBoardActivity.getInstance(this@SignInActivity))
                        finishAffinity()
                    }
                    NotFound -> {
                        val msg =
                            getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@SignInActivity, msg)
//                        Toast.makeText(this@SignInActivity, "${msg}", Toast.LENGTH_LONG).show()
                    }
                    NotVerify -> {
                        appPreferences?.setEmail(this@SignInActivity, SocialDetail.socialemail)
                        startActivity(SignUpActivity.getInstance(this@SignInActivity, SocialDetail))
                    }
                    ValidationError -> {
                        val msg = mainObject?.get("message")?.asString
                        ShowAlertFailed(this@SignInActivity, msg)
                    }
                    else -> {
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.RequestCode.GoogleLoginRequestCode -> {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            }
            AppConstants.RequestCode.LineLoginRequestCode -> {
                val result: LineLoginResult = LineLoginApi.getLoginResultFromIntent(data)
                handleLineResult(result, result.responseCode)
            }
            else -> {

            }
        }
    }

    private fun handleLineResult(res: LineLoginResult, responseCode: LineApiResponseCode) {
        when (responseCode) {
            LineApiResponseCode.SUCCESS -> {
                // Login successful
                val accessToken: String =
                    res.lineCredential?.accessToken!!.tokenString
                Log.d(tag, "line id ${accessToken}, userId ${res.lineProfile?.userId} creads ${res.lineCredential}")
                Log.d(tag, "liresponseCode $responseCode")
                val socialLogin = SocialLogin(
                    AppConstants.LoginTypes.LineLogin,
                    res.lineProfile?.userId,
                    res.lineProfile?.displayName,
                    "",
                    res.lineProfile?.displayName,
                    res.lineProfile?.displayName
                )
                socialLoginApi(socialLogin)
            }
            LineApiResponseCode.CANCEL ->             // Login canceled by user
                Log.e("ERROR", "LINE Login Canceled by user.")
            else -> {
                // Login canceled due to other error
                Log.e("ERROR", "Login FAILED!")
                Log.e("ERROR", res.errorData.toString())
            }
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.v(
                tag,
                " ${account} email ${account?.email} ,id ${account?.id} displayName ${account?.displayName}, photoUrl ${account?.photoUrl}"
            )
            // Signed in successfully, show authenticated UI.
            val socialLogin = SocialLogin(
                AppConstants.LoginTypes.GoogleLogin,
                account?.id, account?.displayName, "", account?.displayName, account?.email
            )
            socialLoginApi(socialLogin)
        } catch (e: ApiException) {
            Log.v(
                tag,
                "signInResult:failed code=" + e.statusCode + e.localizedMessage
            )
            e.printStackTrace()
        }
    }
}

