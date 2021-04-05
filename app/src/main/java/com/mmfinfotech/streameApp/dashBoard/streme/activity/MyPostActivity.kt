package com.mmfinfotech.streameApp.dashBoard.streme.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_my_post.*
import kotlinx.android.synthetic.main.activity_my_post.buttonPost
import kotlinx.android.synthetic.main.activity_my_post.editTextPostDesc
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import java.util.*


class MyPostActivity : DashBoardBaseActivity() {
    private val Tag: String? = MyPostActivity::class.java.simpleName
    var filePath: String? = null

    companion object {
        fun getInstance(context: Context, filePath: String?) = Intent(context, MyPostActivity::class.java).apply {
            putExtra(AppConstants.IntentExtras.MyPostPath, filePath)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post)
        filePath = intent.getStringExtra(AppConstants.IntentExtras.MyPostPath)

        Glide.with(this)
            .load(filePath)
            .placeholder(R.drawable.background_2)
            .into(imgPost)

        setListners()

    }

    private fun setListners() {
        buttonPost.setOnClickListener {
            if (validationEmptyField(this@MyPostActivity,editTextPostTitle ) == true && validationEmptyField(this@MyPostActivity, editTextPostDesc)== true
            ) {
                callApiPost()
            } else {
            }
        }
        imageToolbarBack.setOnClickListener{
            finish()
        }
    }

    private fun callApiPost() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@MyPostActivity)}"

        val FileType: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), "1"
        )
        val title: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), editTextPostTitle.text.toString()
        )
        val Desc: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), editTextPostDesc.text.toString()
        )

        val file = File(filePath ?: "")
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callPost: Call<JsonObject?>? = apiService?.callMyPost(headers, fileToUpload, FileType, title, Desc)
        callApi(true, callPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        CompleteActionDialog(this@MyPostActivity, getString(R.string.txt_complete), View.OnClickListener { v ->
                            when (v?.id) {
                                R.id.doneTask -> {
                                    finish()
                                }
                            }
                        })

                    }

                    ValidationError -> {
                        ShowAlertInformation(this@MyPostActivity, getStringFromJson(mainObject, "msg", ""))
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