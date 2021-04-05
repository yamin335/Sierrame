package com.mmfinfotech.streameApp.dashBoard.streme.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_my_clip.*
import kotlinx.android.synthetic.main.activity_my_post.buttonPost
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import java.util.*


class AddClipActivity : DashBoardBaseActivity() {
    private val TAG: String? = AddClipActivity::class.java.simpleName
    var filePath: String? = null

    companion object {
        fun getInstance(context: Context, filePath: String?) = Intent(context, AddClipActivity::class.java).apply {
            putExtra(AppConstants.IntentExtras.MyClipPath, filePath)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_clip)
        filePath = intent.getStringExtra(AppConstants.IntentExtras.MyClipPath)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(VideoClip)
        mediaController.setMediaPlayer(VideoClip)
        VideoClip?.setMediaController(mediaController)

        VideoClip?.setVideoPath(filePath)
        VideoClip?.requestFocus()
        VideoClip?.start()
        dialog?.show()
        VideoClip.setOnPreparedListener { p0 ->
            p0?.start()
            p0?.setOnVideoSizeChangedListener { p0, p1, p2 ->
                dialog?.dismiss()
                p0?.start()
            }
        }

        setListners()
    }

    private fun setListners() {
        imageToolbarBack.setOnClickListener {
           finish()
        }

        buttonPost.setOnClickListener {
            if(validationEmptyField(this@AddClipActivity,editTextClipsTitle)==true && validationEmptyField(this@AddClipActivity,editTextClipsDesc)
                ==true){
                callApiAddClips()
            }
            else{

            }

        }
    }

    private fun callApiAddClips() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@AddClipActivity)}"

        val FileType: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), "2")
        val title: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), editTextClipsTitle.text.toString())
        val Desc: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), editTextClipsDesc.text.toString())
        val category: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), "")

        val file = File(filePath ?: "")
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("video/*"), file)
        val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
                MyApiEndpointInterface::class.java)
        val callPost: Call<JsonObject?>? = apiService?.callAddMyClip(headers, fileToUpload, FileType, title, Desc, category)
        callApi(true, callPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        CompleteActionDialog(this@AddClipActivity, getString(R.string.txt_complete), View.OnClickListener { v ->
                            when (v?.id) {
                                R.id.doneTask -> {
                                    finish()
                                }
                            }
                        })
                    }
                    NotVerify -> {
                    }
                    ValidationError -> {
                        ShowAlertInformation(this@AddClipActivity, getStringFromJson(mainObject, "msg", ""))
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