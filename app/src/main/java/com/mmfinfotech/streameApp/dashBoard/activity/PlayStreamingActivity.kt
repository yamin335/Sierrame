package com.mmfinfotech.streameApp.dashBoard.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.models.*
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.util.sharingDialog
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_live.*
import kotlinx.android.synthetic.main.activity_play_streaming.*
import kotlinx.android.synthetic.main.activity_play_streaming.imageViewLike
import kotlinx.android.synthetic.main.activity_post_discription.*
import retrofit2.Call
import java.util.*


class PlayStreamingActivity : DashBoardBaseActivity() {
    private val TAG: String? = PlayStreamingActivity::class.java.simpleName
    private var Clip: Clips? = null
    private var RefrenceId: String? = null
    private val arrObject: ArrayList<ImageView?>? = ArrayList()
    private var LikeCounts: Int? = 0
    private val arrAnimator: ArrayList<Animator?>? = ArrayList()

    companion object {
        fun getInstance(context: Context?, modelData: Clips?) = Intent(context, PlayStreamingActivity::class.java).apply {
            putExtra(AppConstants.IntentExtras.MyClipsDetails, modelData)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_streaming)
        Clip = intent.getParcelableExtra(AppConstants.IntentExtras.MyClipsDetails)
        apiDetilClips()
//        setPreview()
        setListners()
    }

    private fun setData() {
        textViewVideoTitle.text = Clip?.title
        textViewVideoDescription.text = Clip?.description
        live_room_name.text = Clip?.user_name
//        live_room_broadcaster_uid.text=Clip?.id

        Glide.with(this@PlayStreamingActivity)
            .load(Clip?.file)
            .placeholder(R.drawable.background_1)
            .apply(
                RequestOptions().error(R.drawable.ic_dmmy_user)
                    .placeholder(R.drawable.ic_dmmy_user)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .apply(RequestOptions.circleCropTransform())
            .into(live_name_board_icon)

        tvLikeCount.text = Clip?.like_count.toString()
    }

    private fun setPreview() {

//      videoView.setVideoPath(Clip?.file)
//      videoView.start()
        Clip?.file
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView?.setMediaController(mediaController)
        val uri: Uri = Uri.parse(Clip?.file)
        videoView?.setVideoURI(uri)
        videoView?.requestFocus()
        videoView?.start()
        dialog?.show()
        videoView.setOnPreparedListener { p0 ->
            p0?.start()
            p0?.setOnVideoSizeChangedListener { p0, p1, p2 ->
                dialog?.dismiss()
                p0?.start()
            }
        }
    }

    private fun setListners() {
        tv_follow.setOnClickListener { followUnfollow() }
        closeIcon.setOnClickListener { onBackPressed() }
        imageViewLike.setOnClickListener { callApiLikeDisLike() }
        imageButtonViewDelete?.setOnClickListener {
            val builder = AlertDialog.Builder(this@PlayStreamingActivity).apply {
                setTitle(R.string.txt_alert)
                setMessage(R.string.txt_desc_delete_clip)
                setIcon(android.R.drawable.ic_dialog_alert)
                setPositiveButton("Yes"){dialogInterface, which ->
                    callApiDeleteClip()
                    dialogInterface.dismiss()
                }
                setNegativeButton("No"){dialogInterface, which -> }
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
//            callApiDeleteClip()
        }
        imageViewShare.setOnClickListener {
            sharingDialog(this@PlayStreamingActivity, object : View.OnClickListener {
                override fun onClick(v: View?) {
                    when (v?.id) {
                        R.id.imageButtonGoogle -> Log.d(TAG, "imageButtonGoogle")
                        R.id.imageButtonline -> Log.d(TAG, "imageButtonline ")
                        R.id.imageButtonFb -> Log.d(TAG, "imageButtonFb ")
                        else -> { }
                    }
                }
            })
        }
    }

    private fun callApiDeleteClip() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@PlayStreamingActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@PlayStreamingActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callDeleteClip: Call<ClipDeleteResponse?>? = apiService?.callDeleteClip(headers, Clip?.id?.toString())

        callRemoteApi(true, callDeleteClip, object : ApiClient.ApiCallbackListener<ClipDeleteResponse> {
            override fun onDataFetched(response: ClipDeleteResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@PlayStreamingActivity, response?.success, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<ClipDeleteResponse> {
                        override fun onSuccess(response: ClipDeleteResponse) {
                            finish()
                            Toast.makeText(this@PlayStreamingActivity, response.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(this@PlayStreamingActivity, message, Toast.LENGTH_LONG).show()
                        }
                    })
            }
        })
    }

    private fun followUnfollow() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@PlayStreamingActivity)}"
        val apiService: MyApiEndpointInterface? = ApiClient(this@PlayStreamingActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callFollowUnFollow: Call<FollowUnFollowResponse?>? = apiService?.callFollowUnFollow(headers, Clip?.user_id)

        callRemoteApi(true, callFollowUnFollow, object : ApiClient.ApiCallbackListener<FollowUnFollowResponse> {
            override fun onDataFetched(response: FollowUnFollowResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@PlayStreamingActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<FollowUnFollowResponse> {
                        override fun onSuccess(response: FollowUnFollowResponse) {
                            if (response.record?.follow_status?.equals("1") == true) {
                                tv_follow.text = getString(R.string.following)
                            } else {
                                tv_follow.text = getString(R.string.follow)
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(this@PlayStreamingActivity, message, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        })
    }

    fun heart(view: View) {
        Handler().postDelayed({
            for (i in 0 until 10) {
                val outLocation = IntArray(2)
                view.getLocationOnScreen(outLocation)

                val display: Display? = windowManager.defaultDisplay
                val size = Point()
                display?.getSize(size)
                val width: Int = size.x
//                val height: Int? = size?.y

                val heart: Drawable? = ContextCompat.getDrawable(this@PlayStreamingActivity, R.drawable.ic_heart_animatn)

                val imageView: ImageView = ImageView(baseContext).apply {
                    setImageDrawable(heart)
                    val imageSizeMax = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, resources.displayMetrics).toInt()
                    val imageSizeMin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25F, resources.displayMetrics).toInt()
                    val paramsImage =
                        RelativeLayout.LayoutParams(
                            kotlin.random.Random.nextInt(imageSizeMin, imageSizeMax),
                            kotlin.random.Random.nextInt(imageSizeMin, imageSizeMax)
                        )
                    x = (outLocation[0].plus(
                        view.width.div(2).minus(paramsImage.width.div(2))
                    )).toFloat()
                    y = (outLocation[1].minus(
                        view.height.div(2).minus(paramsImage.height.div(2))
                    )).toFloat()
                    layoutParams = paramsImage

                }

                runOnUiThread { relativeLayout?.addView(imageView) }
                val randomy = kotlin.random.Random.nextInt(width.div(1.5).toInt(), width)
                val animationY: ObjectAnimator? = ObjectAnimator.ofFloat(imageView, "translationY", randomy.toFloat()).apply {
                    duration = kotlin.random.Random.nextLong(2000, 6000)
                    startDelay = kotlin.random.Random.nextLong(10, 30)
                }

//                val max = width?.div(4) ?: 0
//                val min = width?.div(12) ?: 0
//            val randomX = (Random.nextInt(max.plus(min)).minus(min)) * 10
                val randomX = kotlin.random.Random.nextInt(width.div(1.5).toInt(), width)
                val animationX: ObjectAnimator? = ObjectAnimator.ofFloat(imageView, "translationX", randomX.toFloat()).apply {
                    duration = 1250
//                duration = Random.nextLong(2000, 6000)
//                repeatCount = ValueAnimator.INFINITE
//                repeatMode = ValueAnimator.REVERSE
                    startDelay = kotlin.random.Random.nextLong(10, 30)
                }

                val fadeOut: ObjectAnimator? = ObjectAnimator.ofFloat(imageView, View.ALPHA, 1f, 0f).apply {
                    duration = animationY?.duration?.div(1.5)?.toLong()
                        ?: 0//Random.nextLong(1000, animationY?.duration ?: 2000)
                    interpolator = LinearInterpolator()
                    startDelay = animationY?.duration?.div(4) ?: 0
                }

                val animationListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        Log.v(TAG, "onAnimationEnd ==> index : start")
                        arrAnimator?.add(animation)
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        Log.v(TAG, "onAnimationEnd ==> index : ${arrAnimator?.indexOf(animation)}")
                        arrAnimator?.indexOf(animation)?.let {
                            arrObject?.get(it).let { _imageView ->
                                relativeLayout?.removeView(_imageView)
                                arrObject?.remove(_imageView)
                            }
                            arrAnimator.removeAt(it)
                        }
//                        relativeLayout?.removeView(arrObject?.get(arrAnimator?.indexOf(animation) ?: 0))
//                    arrAnimator?.remove(animation)
//                    arrObject?.removeAt(arrAnimator?.indexOf(animation) ?: 0)
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                }

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(animationX, animationY, fadeOut)
                animatorSet.addListener(animationListener)
                animatorSet.start()
                arrObject?.add(imageView)
            }
        }, 100)
    }

    private fun callApiLikeDisLike() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val apiService: MyApiEndpointInterface? =
            ApiClient(this@PlayStreamingActivity).getClient()?.create(MyApiEndpointInterface::class.java)
        val callLogout: Call<LikeDisLikeResponse?>? = apiService?.callLikeDisslike(headers, Clip?.id?.toString(), AppConstants.LikeTypes.TypeClips)
        callRemoteApi(true, callLogout, object : ApiClient.ApiCallbackListener<LikeDisLikeResponse> {
            override fun onDataFetched(response: LikeDisLikeResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@PlayStreamingActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<LikeDisLikeResponse> {
                        override fun onSuccess(response: LikeDisLikeResponse) {
                            if (response.status_changed.equals("1")) {
                                heart(imageViewLike)
                                LikeCounts = LikeCounts?.plus(1)
                                tvLikeCount.text = LikeCounts.toString()
                                imageViewLike.colorFilter = null
                            } else {
                                LikeCounts = LikeCounts?.minus(1)
                                tvLikeCount.text = LikeCounts.toString()
                                imageViewLike.setColorFilter(resources.getColor(R.color.textSecondary))
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                        }
                    })
            }
        })
    }

    private fun apiDetilClips() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@PlayStreamingActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@PlayStreamingActivity).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callProfile: Call<ClipDetailsResponse?>? = apiService?.callClipsDetails(headers, Clip?.id.toString())
        callRemoteApi(true, callProfile, object : ApiClient.ApiCallbackListener<ClipDetailsResponse> {
            override fun onDataFetched(response: ClipDetailsResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@PlayStreamingActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<ClipDetailsResponse> {
                        override fun onSuccess(response: ClipDetailsResponse) {

                            val recordData = response.record ?: return

                            if (appPreferences?.getUserId(this@PlayStreamingActivity).equals(recordData.user_id, ignoreCase = true))
                                imageButtonViewDelete?.visibility = View.VISIBLE

                            if (recordData.user_id.equals(appPreferences?.getUserId(this@PlayStreamingActivity))) {
                                linearLayout.visibility = View.GONE
                            } else {
                                linearLayout.visibility = View.VISIBLE

                            }

                            textViewVideoTitle.text = Clip?.title
                            textViewVideoDescription.text = Clip?.description
                            live_room_name.text = Clip?.user_name
                            live_room_broadcaster_uid.text = getString(R.string.txt_shot_movie)


                            Glide.with(this@PlayStreamingActivity)
                                .load(Clip?.user_profile)
                                .placeholder(R.drawable.background_1)
                                .apply(
                                    RequestOptions().error(R.drawable.ic_dmmy_user)
                                        .placeholder(R.drawable.ic_dmmy_user)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                )
                                .apply(RequestOptions.circleCropTransform())
                                .into(live_name_board_icon)
                            LikeCounts = recordData.like_count
                            tvLikeCount.text = LikeCounts.toString()
                            setPreview()
                            if (recordData.like_status.equals("1")) {
                                imageViewLike.colorFilter = null

                            } else {
                                imageViewLike.setColorFilter(resources.getColor(R.color.textSecondary))
                            }
                            if (recordData.follow_status.equals("1")) {
                                tv_follow.text = getString(R.string.following) // "Following"
                            } else {
                                tv_follow.text = getString(R.string.follow) // "Follow"
                            }
                        }

                        override fun onFailed(status: String, message: String) {
                        }
                    })
            }
        })
    }

}