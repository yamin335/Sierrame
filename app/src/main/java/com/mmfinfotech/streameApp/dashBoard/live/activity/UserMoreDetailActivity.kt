package com.mmfinfotech.streameApp.dashBoard.live.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.fragment.StremerClipsFragment
import com.mmfinfotech.streameApp.dashBoard.live.fragment.StremerPostFragment
import com.mmfinfotech.streameApp.dashBoard.profile.activity.LeaderBoardActivity
import com.mmfinfotech.streameApp.models.FollowUnFollowResponse
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_live.*
import kotlinx.android.synthetic.main.activity_user_more_detail.*
import retrofit2.Call
import java.util.*
import kotlin.collections.set

class UserMoreDetailActivity : DashBoardBaseActivity() {
    private val TAG: String? = UserMoreDetailActivity::class.java.simpleName
    private var StreamerId: String? = null
    var navItemIndex: Int? = AppConstants.FragmentsProfileIndex.FragmentPost
    private var streamerName: String? = AppConstants.Defaults.string

    companion object {
        fun getInstance(
            context: Context?,
            userId: String?
        ) = Intent(context, UserMoreDetailActivity::class.java).apply {
            putExtra(AppConstants.IntentExtras.StremeLiveId, userId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_more_detail)
        StreamerId = intent.getStringExtra(AppConstants.IntentExtras.StremeLiveId)
        apiStremeProfile()
        initView()
        fragmentGoesInThisTab()
        setListners()
    }

    private fun setListners() {
        findViewById<ConstraintLayout>(R.id.toolbarUserDetail)?.findViewById<ImageView>(R.id.imageToolbarBack)
            ?.setOnClickListener {
                onBackPressed()
            }
        buttonStremeFollow.setOnClickListener {
            callFollowUnFollow()
        }
        textViewFollowingHead.setOnClickListener {
            startActivity(
                StremerFollowNLikeActivity.getInstance(
                    this,
                    StremerFollowNLikeActivity.ACTION_Stremer_FOLLOWING,
                    StreamerId
                )
            )
        }
        textViewLikeHead.setOnClickListener {
            startActivity(
                StremerFollowNLikeActivity.getInstance(
                    this,
                    StremerFollowNLikeActivity.ACTION_Stremer_LIKE,
                    StreamerId
                )
            )
        }
        textViewFolloweraHead.setOnClickListener {
            startActivity(
                StremerFollowNLikeActivity.getInstance(this, StremerFollowNLikeActivity.ACTION_Stremer_FOLLOWER, StreamerId)
            )
        }
        imageViewPost.setOnClickListener {
            navItemIndex = AppConstants.FragmentsProfileIndex.FragmentPost
            fragmentGoesInThisTab()
            imageViewPost.setColorFilter(
                ContextCompat.getColor(this, R.color.textWhite),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            imageViewPlay.setColorFilter(
                ContextCompat.getColor(this, R.color.textSecondary),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            imageViewRecording.setColorFilter(
                ContextCompat.getColor(this, R.color.textSecondary),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
        }
        imageViewPlay.setOnClickListener {
            navItemIndex = AppConstants.FragmentsProfileIndex.FragmentClips
            fragmentGoesInThisTab()
            imageViewPlay.setColorFilter(
                ContextCompat.getColor(this, R.color.textWhite),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            imageViewPost.setColorFilter(
                ContextCompat.getColor(this, R.color.textSecondary),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            imageViewRecording.setColorFilter(
                ContextCompat.getColor(this, R.color.textSecondary),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
        }
        textvirewStremerRanking.setOnClickListener {
            startActivity(LeaderBoardActivity.getInstance(this@UserMoreDetailActivity, streamerName))
        }
    }

    private fun initView() {
        findViewById<ConstraintLayout>(R.id.toolbarUserDetail)?.findViewById<TextView>(R.id.textViewTitle)?.text =
            getString(R.string.titleToolbarUsername)
        findViewById<ConstraintLayout>(R.id.toolbarUserDetail)?.findViewById<ImageView>(R.id.imageToolbarMenu)
            ?.setImageDrawable(getDrawable(R.drawable.ic_timeline))
        imageViewPost.setColorFilter(
            ContextCompat.getColor(this, R.color.textWhite),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );
        imageViewPlay.setColorFilter(
            ContextCompat.getColor(this, R.color.textSecondary),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );
        imageViewRecording.setColorFilter(
            ContextCompat.getColor(this, R.color.textSecondary),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );

    }

    private fun apiStremeProfile() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callstremerProfile: Call<JsonObject?>? =
            apiService?.callStreamProfile(headers, StreamerId.toString())
        callApi(true, callstremerProfile, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
//                        {
//                        "streamer_id":"74","streamer_name":"soeda","streamer_profile":"https://staging-streame-bucket.s3.amazonaws.com/default/user.png",
//                        "streamer_status":null,"streamer_ranking":101736,"about_streamer":null,
//                        "secret_mode":"0","orignal_photo":"0",
//                        "private_follow_list":"0","private_account":"0","region":"0","notification":"0",
//                        "follow_status":"2",
//                        "profile_notes":"", "followings_count":0,"followers_count":1,"like_count":2,"schedules":[]
//                        }
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val id = getStringFromJson(record, "id", AppConstants.Defaults.string)
                        val user_id = getStringFromJson(record, "user_id", AppConstants.Defaults.string)
                        val streamer_id = getStringFromJson(record, "streamer_id", AppConstants.Defaults.string)
                        val streamer_name = getStringFromJson(record, "streamer_name", AppConstants.Defaults.string)
                        val streamer_profile = getStringFromJson(record, "streamer_profile", AppConstants.Defaults.string)
                        val streamer_status = getStringFromJson(record, "streamer_status", AppConstants.Defaults.string)
                        val streamer_ranking = getStringFromJson(record, "streamer_ranking", AppConstants.Defaults.string)
                        val follow_status = getStringFromJson(record, "follow_status", AppConstants.Defaults.string)
                        val profile_notes = getStringFromJson(record, "profile_notes", AppConstants.Defaults.string)
                        val followings_count = getStringFromJson(record, "followings_count", AppConstants.Defaults.string)
                        val about_streamer = getStringFromJson(record, "about_streamer", AppConstants.Defaults.string)
                        val followers_count = getStringFromJson(record, "followers_count", AppConstants.Defaults.string)
                        val like_count = getStringFromJson(record, "like_count", AppConstants.Defaults.string)
                        val private_follow_list = getStringFromJson(record, "private_follow_list", AppConstants.Defaults.string)

                        if (private_follow_list == "1") {
                            textViewFolloweraHead?.setOnClickListener(null)
                            textViewFollowingHead?.setOnClickListener(null)
                        }

                        findViewById<ConstraintLayout>(R.id.toolbarUserDetail)?.findViewById<TextView>(R.id.textViewTitle)?.text = streamer_name
                        streamerName = streamer_name
                        textViewStatus.text = about_streamer.trim()
                        textViewMainStatus.text = streamer_status
                        textViewfollowing.text = followings_count
                        textViewStremerLike.text = like_count
                        textViewStremerfollower.text = followers_count
                        textviewStremGift.text = getString(R.string.txt_gift)
                        textvirewStremerRanking.text = getString(R.string.titleRanking)

                        if (follow_status.equals("1")) {
                            buttonStremeFollow.text = getString(R.string.following)
                        } else {
                            buttonStremeFollow.text = getString(R.string.follow)
                        }

                        Glide.with(this@UserMoreDetailActivity)
                            .load(streamer_profile)
                            .apply(
                                RequestOptions().error(R.drawable.ic_dmmy_user)
                                    .placeholder(R.drawable.ic_dmmy_user)
                            )
                            .apply(RequestOptions.circleCropTransform())
                            .into(imgProfileStreme)
                    }
                    NotFound -> {
//                        arrSchedule?.clear()
                        val msg =
                            getStringFromJson(mainObject, message, AppConstants.Defaults.string)
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

    fun callFollowUnFollow() {
        val headers = HashMap<String, String>()
        headers["Authorization"] =
            "Bearer ${AppPreferences().getAuthToken(this@UserMoreDetailActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@UserMoreDetailActivity).getClient()
            ?.create(MyApiEndpointInterface::class.java)
        val callFollowUnFollow: Call<FollowUnFollowResponse?>? =
            apiService?.callFollowUnFollow(headers, StreamerId)

        callRemoteApi(true, callFollowUnFollow, object : ApiClient.ApiCallbackListener<FollowUnFollowResponse> {
            override fun onDataFetched(response: FollowUnFollowResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@UserMoreDetailActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<FollowUnFollowResponse> {
                        override fun onSuccess(response: FollowUnFollowResponse) {
                            if (response.record?.follow_status.equals("1")) {
                                buttonStremeFollow.text = getString(R.string.following)
                            } else {
                                buttonStremeFollow.text = getString(R.string.follow)
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(this@UserMoreDetailActivity, message, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        })
    }

    private fun fragmentGoesInThisTab() {
        val fragment: Fragment? = showFragment()
        val fragmentTransaction = supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameStremerPostClipsRecordingView, fragment!!, "")
        fragmentTransaction?.commitAllowingStateLoss()
    }

    private fun showFragment(): Fragment? {
        return when (navItemIndex) {
            AppConstants.FragmentsProfileIndex.FragmentPost -> StremerPostFragment.getInstance(
                StreamerId
            )
            AppConstants.FragmentsProfileIndex.FragmentClips -> StremerClipsFragment.getInstance(
                StreamerId
            )
            else -> StremerPostFragment.getInstance(StreamerId)
        }
    }
}