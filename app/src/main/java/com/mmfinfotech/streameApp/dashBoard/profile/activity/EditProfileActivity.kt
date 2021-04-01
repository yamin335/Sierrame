package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.activity_edit_profile.*
import retrofit2.Call
import java.util.*


class EditProfileActivity : DashBoardBaseActivity() {
    private val TAG: String? = EditProfileActivity::class.java.simpleName

    companion object {
        fun getInstance(context: Context?) = Intent(context, EditProfileActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        initView()
        initFields()
        setListners()
    }

    private fun initFields() {
        editTextStremeId.setText(appPreferences?.getStremeId(this@EditProfileActivity))
        textViewGander.text = appPreferences?.getGander(this@EditProfileActivity)
        editTextEmail.setText(appPreferences?.getEmail(this@EditProfileActivity))
        textViewBirthDay.setText(appPreferences?.getBirthDay(this@EditProfileActivity))
        textViewGander.setText(appPreferences?.getGander(this@EditProfileActivity))
        editTextMultitext.setText(appPreferences?.getMultilineComment(this@EditProfileActivity))
        editTextComments.setText(appPreferences?.getComment(this@EditProfileActivity))
        editTextTextweb.setText(appPreferences?.getWeb(this@EditProfileActivity))
    }

    private fun setListners() {
        findViewById<ConstraintLayout>(R.id.toolbarEdit)?.findViewById<ImageView>(R.id.imageToolbarBack)?.setOnClickListener {
            onBackPressed()
        }
        textViewGander.setOnClickListener {
            showGanderSelectiionDialog(this@EditProfileActivity, object : View.OnClickListener {
                override fun onClick(v: View?) {
                    when (v?.id) {
                        R.id.textView_Male -> {
                            textViewGander.text = getString(R.string.male)
                        }
                        R.id.textView_Female -> {
                            textViewGander.text = getString(R.string.woman)
                        }
                        R.id.textView_Private -> {
                            textViewGander.text = getString(R.string.txt_private)
                        }
                    }
                }
            })
        }
        btnConfirm.setOnClickListener {
            if ((validationEmptyField(this@EditProfileActivity, editTextStremeId) == true)) {
                changeStremeIdApi()
            } else {
            }
        }
        btnComplete.setOnClickListener {
//            callUserUpdateApi()
            if (validation()) {
                if(textViewBirthDay.text.equals("")){
//                    Toast.makeText(this@EditProfileActivity,"",Toast.LENGTH_SHORT)
                    ShowAlertInformation(this@EditProfileActivity,getString(R.string.please_select_birthdate))
                }
                else{
                    callUserUpdateApi()
                }
            } else {
            }
        }
        textViewBirthDay.setOnClickListener {
            getDate(this, textViewBirthDay)
        }
    }

    private fun validation(): Boolean {
return ((validationEmptyField(this@EditProfileActivity, editTextTextweb) == true)
    && (validationEmptyField(this@EditProfileActivity, editTextComments) == true)
    && (validationEmptyField(this@EditProfileActivity, editTextMultitext) == true))
    }

    private fun initView() {
        findViewById<ConstraintLayout>(R.id.toolbarEdit)?.findViewById<TextView>(R.id.textViewTitle)?.text = appPreferences?.getStremeId(this@EditProfileActivity)
        findViewById<ConstraintLayout>(R.id.toolbarEdit)?.findViewById<ImageView>(R.id.imageToolbarMenu)
            ?.setImageDrawable(getDrawable(R.drawable.ic_edit_grey_icon))
    }

    private fun changeStremeIdApi() {
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("username", editTextStremeId?.text.toString())
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"

        val apiService: MyApiEndpointInterface? =
            ApiClient(this@EditProfileActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callChangeId: Call<JsonObject?>? = apiService?.callChangeStremeId(headers, sendParams)
        callApi(true, callChangeId, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        appPreferences?.setStremeID(this@EditProfileActivity, editTextStremeId?.text.toString())
                        ShowAlertSuccess(this@EditProfileActivity, getString(R.string.sucessfully_updated))
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        ShowAlertInformation(this@EditProfileActivity, msg)
                    }
                    NotVerify -> {
//                     appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
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

    private fun callUserUpdateApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("country_code", "")
        sendParams?.addProperty("gender", textViewGander?.text.toString())
        sendParams?.addProperty("email", editTextEmail?.text.toString())
        sendParams?.addProperty("dob", textViewBirthDay?.text.toString())
        sendParams?.addProperty("state", "")
        sendParams?.addProperty("city", "")
        sendParams?.addProperty("address", "")
        sendParams?.addProperty("latitude", "")
        sendParams?.addProperty("longitude", "")
        sendParams?.addProperty("about_me", editTextMultitext?.text.toString())
        val apiService: MyApiEndpointInterface? =
            ApiClient(this@EditProfileActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callChangeId: Call<JsonObject?>? = apiService?.callUserUpdateinfo(headers, sendParams)
        callApi(true, callChangeId, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        appPreferences?.setGander(this@EditProfileActivity, textViewGander?.text.toString())
                        appPreferences?.setEmail(this@EditProfileActivity, editTextEmail.text.toString())
                        appPreferences?.setBirthDay(this@EditProfileActivity, textViewBirthDay?.text.toString())
                        appPreferences?.setEmail(this@EditProfileActivity, editTextEmail.text.toString())
                        appPreferences?.setWeb(this@EditProfileActivity, editTextTextweb.text.toString())
                        appPreferences?.setComment(this@EditProfileActivity, editTextComments.text.toString())
                        appPreferences?.setMultilineComment(this@EditProfileActivity, editTextMultitext.text.toString())
                        finish()
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
//                        Toast.makeText(this@EditProfileActivity, "${msg}", Toast.LENGTH_LONG).show()
                        ShowAlertInformation(this@EditProfileActivity, msg)
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

            override fun onFailure() {}
        })
    }

}