package com.mmfinfotech.streameApp.dashBoard.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.models.LikeDisLikeResponse
import com.mmfinfotech.streameApp.util.getFormattedDate
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_play_streaming.*
import kotlinx.android.synthetic.main.activity_post_discription.*
import kotlinx.android.synthetic.main.activity_post_discription.view.*
import retrofit2.Call
import java.util.*

class PostDescriptionActivity : DashBoardBaseActivity() {
    //    private var PostData: Post? = null
    private var postId: String? = AppConstants.Defaults.string
    var likeCounts: Int? = 0

    companion object {
        fun getInstance(context: Context?, postId: String?) = Intent(context, PostDescriptionActivity::class.java).apply {
            putExtra(AppConstants.IntentExtras.PostDetails, postId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_discription)
//        PostData = intent.getParcelableExtra(AppConstants.IntentExtras.PostDetails)
        postId = intent.getStringExtra(AppConstants.IntentExtras.PostDetails)
        apiPostDeatils()
        setListners()
    }

    private fun setListners() {
        ImageButtonBack.setOnClickListener { onBackPressed() }
        ImageButtonHeart.setOnClickListener { callApiLikeDisLike() }
        ImageButtonComments.setOnClickListener { startActivity(CommentsActivity.getInstance(this@PostDescriptionActivity, postId.toString())) }
        ImageButtonDelete?.setOnClickListener {
            val builder = AlertDialog.Builder(this@PostDescriptionActivity).apply {
                setTitle(R.string.txt_alert)
                setMessage(R.string.txt_desc_delete_post)
                setIcon(android.R.drawable.ic_dialog_alert)
                setPositiveButton("Yes"){dialogInterface, which ->
                    callDeletePostApi()
                    dialogInterface.dismiss()
                }
                setNegativeButton("No"){dialogInterface, which -> }
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
//            callDeletePostApi()
        }
    }

    private fun callDeletePostApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@PostDescriptionActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callLogout: Call<JsonObject?>? = apiService?.callDeletePost(headers, postId)
        callApi(true, callLogout, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
                when (status) {
                    Success -> {
                        finish()
                    }
                    else -> {
                        val message = getStringFromJson(mainObject, "message", AppConstants.Defaults.string)
                        Toast.makeText(this@PostDescriptionActivity, message,Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure() { }
        })
    }

    private fun callApiLikeDisLike() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? =
            ApiClient(this@PostDescriptionActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callLogout: Call<LikeDisLikeResponse?>? = apiService?.callLikeDisslike(headers, postId, AppConstants.LikeTypes.TypePost)
        callRemoteApi(true, callLogout, object : ApiClient.ApiCallbackListener<LikeDisLikeResponse> {
            override fun onDataFetched(response: LikeDisLikeResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@PostDescriptionActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<LikeDisLikeResponse> {
                        override fun onSuccess(response: LikeDisLikeResponse) {
                            if (response.status_changed.equals("1")) {
                                ImageButtonHeart.setImageResource(R.drawable.ic__fillheart_24)
                                likeCounts = likeCounts?.plus(1)
                                val text = likeCounts.toString() + getString(R.string.like) //likeCounts.toString()+" Likes"
                                textViewLikes.text = text
                            } else {
                                ImageButtonHeart.setImageResource(R.drawable.ic_baseline_emptyheart);
                                likeCounts = likeCounts?.minus(1)
                                val text = likeCounts.toString() + getString(R.string.like)  // "${likeCounts.toString()} Likes"
                                textViewLikes.text = text
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                        }
                    })
            }
        })
    }

    private fun apiPostDeatils() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@PostDescriptionActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@PostDescriptionActivity).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callProfile: Call<JsonObject?>? = apiService?.callPostDetails(headers, postId ?: "")
        callApi(true, callProfile, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val recordData = getJsonObjFromJson(mainObject, "record", JsonObject())
                        val id = getStringFromJson(recordData, "id", AppConstants.Defaults.string)
                        val user_id = getStringFromJson(recordData, "user_id", AppConstants.Defaults.string)
                        val title = getStringFromJson(recordData, "title", AppConstants.Defaults.string)
                        val description = getStringFromJson(recordData, "description", AppConstants.Defaults.string)
                        val file = getStringFromJson(recordData, "file", AppConstants.Defaults.string)
                        val thumb = getStringFromJson(recordData, "thumb", AppConstants.Defaults.string)
                        val file_type = getStringFromJson(recordData, "file_type", AppConstants.Defaults.string)
                        val status = getStringFromJson(recordData, "status", AppConstants.Defaults.string)
                        val added_on = getStringFromJson(recordData, "added_on", AppConstants.Defaults.string)
                        val update_on = getStringFromJson(recordData, "update_on", AppConstants.Defaults.string) + "000"
                        val user_name = getStringFromJson(recordData, "user_name", AppConstants.Defaults.string)
                        val user_profile = getStringFromJson(recordData, "user_profile", AppConstants.Defaults.string)
                        val profile_status = getStringFromJson(recordData, "profile_status", AppConstants.Defaults.string)
                        val like_status = getStringFromJson(recordData, "like_status", AppConstants.Defaults.string)
                        val like_count = getStringFromJson(recordData, "like_count", AppConstants.Defaults.string)
                        val comment_count = getStringFromJson(recordData, "comment_count", AppConstants.Defaults.string)

                        if (appPreferences?.getUserId(this@PostDescriptionActivity).equals(user_id, ignoreCase = true))
                            ImageButtonDelete?.visibility = View.VISIBLE

                        likeCounts = like_count.toInt()
                        textViewLikes.text =  like_count + getString(R.string.like)
                        textViewComments.text = comment_count + getString(R.string.txt_comment)
                        textViewName.text = user_name
                        textViewTime.text = getFormattedDate(this@PostDescriptionActivity, update_on.toLong())
                        textViewTitles.text = title
                        textViewSubTitle.text = description
                        Glide.with(this@PostDescriptionActivity)
                            .load(user_profile)
                            .placeholder(R.drawable.background_1)
                            .apply(
                                RequestOptions().error(R.drawable.ic_dmmy_user)
                                    .placeholder(R.drawable.ic_dmmy_user)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                            )
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageViewProfile)
//
                        Glide.with(this@PostDescriptionActivity)
                            .load(file)
                            .placeholder(R.drawable.background_1)
                            .into(imagePost)

                        if (like_status.equals("1")) {
                            ImageButtonHeart.setImageResource(R.drawable.ic__fillheart_24);
                        } else {
                            ImageButtonHeart.setImageResource(R.drawable.ic_baseline_emptyheart);
                        }

                    }

                    NotFound -> {
                        val msg = getStringFromJson(mainObject, "message", AppConstants.Defaults.string)
//                        Toast.makeText((mContext as DashBoardActivity), "${msg}", Toast.LENGTH_LONG).show()
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