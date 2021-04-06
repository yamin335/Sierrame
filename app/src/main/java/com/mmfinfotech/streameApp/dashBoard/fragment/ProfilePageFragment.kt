package com.mmfinfotech.streameApp.dashBoard.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.activity.AnonymousCheeringActivity
import com.mmfinfotech.streameApp.dashBoard.activity.CheeringLiveActivity
import com.mmfinfotech.streameApp.dashBoard.live.fragment.StremerPostFragment
import com.mmfinfotech.streameApp.dashBoard.profile.activity.*
import com.mmfinfotech.streameApp.dashBoard.profile.fragment.ClipsFragment
import com.mmfinfotech.streameApp.dashBoard.profile.fragment.PostFragment
import com.mmfinfotech.streameApp.dashBoard.streme.activity.CameraActivity
import com.mmfinfotech.streameApp.models.*
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.listners.OnScaduleClickListners
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.fragment_profile_page.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class ProfilePageFragment : Fragment() {
    private var mContext: Context? = null
    private var arrlist: ArrayList<HomeLive?>? = null
    private var arrSchedule: ArrayList<Schedule?>? = null
    private var imageFilePath: String? = null
    private var imageUri: Uri? = null
    private var removeImg: String? = ""
    var navItemIndex: Int? = AppConstants.FragmentsProfileIndex.FragmentPost

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrlist = ArrayList()
        arrSchedule = ArrayList()
        setInit()
        fragmentGoesInThisTab()
        apiMyProfile()
        apiMySchedule()
        loadProfileImage((mContext as DashBoardActivity).appPreferences?.getProfileImage(mContext))
        setListeners()
    }

    private fun setInit() {
        MenuMoreOptionHide.visibility = View.VISIBLE
        imageViewPost.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
        imageViewPlay.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textSecondary), android.graphics.PorterDuff.Mode.MULTIPLY);
        imageViewRecording.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textSecondary), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private fun setListeners() {
        imageEdit.setOnClickListener { startActivity(EditProfileActivity.getInstance(context)) }
        imageBack.setOnClickListener {  }
        imageViewPost.setOnClickListener {
            navItemIndex = AppConstants.FragmentsProfileIndex.FragmentPost
            fragmentGoesInThisTab()
            imageViewPost.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewPlay.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textSecondary), android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewRecording.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textSecondary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        imageViewPlay.setOnClickListener {
            navItemIndex = AppConstants.FragmentsProfileIndex.FragmentClips
            fragmentGoesInThisTab()
            imageViewPlay.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewPost.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textSecondary), android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewRecording.setColorFilter(ContextCompat.getColor(mContext as DashBoardActivity, R.color.textSecondary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        imgProfile.setOnClickListener {
            dialogSelectPhoto(mContext) { v ->
                when (v?.id) {
                    R.id.textViewGallery -> {
                        if (checkHasPermission(mContext, Manifest.permission.CAMERA) == true &&
                            checkHasPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == true &&
                            checkHasPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == true
                        ) {
                            onGalleryClick()
                        } else {
                            requestPermissions(
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ),
                                AppConstants.Permission.multiplePermissions
                            )
                        }
                    }
                    R.id.textViewCamera -> {
                        if (checkHasPermission(mContext, Manifest.permission.CAMERA) == true &&
                            checkHasPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == true &&
                            checkHasPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == true
                        ) {
                            getCamera()
                        } else {
                            requestPermissions(
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ),
                                AppConstants.Permission.multiplePermissions
                            )
                        }

                    }
                    R.id.textViewRemove -> {
                        removeImg = "1"
                        callUpdateProfile(imageFilePath, removeImg)
                    }
                }
            }

        }
        buttonPost.setOnClickListener {
            navItemIndex = AppConstants.FragmentsProfileIndex.FragmentPost
            startActivity(CameraActivity.getInstance(mContext))
//            fragmentGoesInThisTab()
        }
        textViewLevelHead.setOnClickListener {
            startActivity(LevelActivity.getInstance(context))
        }
        containerFollower?.setOnClickListener {
            startActivity(FollowNLikeActivity.getInstance(context, FollowNLikeActivity.ACTION_FOLLOWER))
        }
        textViewFollowingHead.setOnClickListener {
            startActivity(FollowNLikeActivity.getInstance(context, FollowNLikeActivity.ACTION_FOLLOWING))
        }
        textViewLikeHead.setOnClickListener {
            startActivity(FollowNLikeActivity.getInstance(context, FollowNLikeActivity.ACTION_LIKE))
        }
        textvirewScadule.setOnClickListener {
            showScheduleDialog(mContext, arrSchedule, object : OnScaduleClickListners {
                override fun onCancelClick(view: View?) {
                    apiMySchedule()
                }

                override fun onSaveClicklistner(s: String?, jsonArray: JsonArray?) {
                    callAddSchedule(jsonArray)
                }
            })
        }
        textViewArmy.setOnClickListener {
            arrlist?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
            arrlist?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
            arrlist?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
            showArmyDialog(mContext, arrlist) { }
        }
        textViewGurdian.setOnClickListener {
            showGuardianDialog(mContext) { v ->
                when (v?.id) {
                    R.id.imageViewHelp -> {
                        showGuardianRulesDialog(mContext) { }
                    }
                }
            }
        }

        textvirewRankingImage.setOnClickListener {
            startActivity(RankingCharringProfileActivity.getInstance(mContext, RankingCharringProfileActivity.ACTION_Ranking))
        }
        textViewCheering.setOnClickListener {
            startActivity(CheeringLiveActivity.getInstance(mContext))
        }
        textViewMore.setOnClickListener {
            if (MenuMoreOptionHide.visibility == View.VISIBLE) {
                MenuMoreOptionHide.visibility = View.GONE
            } else {
                MenuMoreOptionHide.visibility = View.VISIBLE
            }
        }
        textViewMission.setOnClickListener { startActivity(MissionActivity.getInstance(mContext)) }
        textViewLiveSchaduling.setOnClickListener { startActivity(LiveScaduleActivity.getInstance(mContext)) }
        textViewPrimeUser.setOnClickListener { startActivity(PrimeUserActivity.getInstance(mContext)) }
        textViewMyBabyCoin.setOnClickListener { startActivity(MyBabyCoinActivity.getInstance(mContext)) }
        textViewBadgeSystem.setOnClickListener { startActivity(BedgeSystemActivity.getInstance(mContext)) }
        textViewMyCollection.setOnClickListener { startActivity(FollowNLikeActivity.getInstance(context, FollowNLikeActivity.ACTION_MYCOLLECTION)) }
        textViewAnnonymousCheering.setOnClickListener { startActivity(AnonymousCheeringActivity.getInstance(context)) }
        textViewAdvanceSetting.setOnClickListener { startActivity(AdvanceSettingActivity.getInstance(context)) }
    }

    private fun getCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = mContext?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, AppConstants.RequestCode.imageCamera)
    }

    private fun loadProfileImage(path: String?) {
        Glide.with(mContext!!)
                .load(path)
                .apply(
                        RequestOptions().error(R.drawable.ic_dmmy_user)
                                .placeholder(R.drawable.ic_dmmy_user)
                )
                .apply(RequestOptions.circleCropTransform())
                .into(imgProfile)
    }

    private fun apiMySchedule() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val apiService: MyApiEndpointInterface? = ApiClient(mContext as DashBoardActivity).getClient()?.create(
                MyApiEndpointInterface::class.java)
        val callChangeId: Call<ScheduleResponse?>? = apiService?.callScheduleMy(headers)
        (mContext as DashBoardActivity).callRemoteApi(true, callChangeId, object : ApiClient.ApiCallbackListener<ScheduleResponse> {
            override fun onDataFetched(response: ScheduleResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(requireActivity(), response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<ScheduleResponse> {
                    override fun onSuccess(response: ScheduleResponse) {
                        arrSchedule?.clear()
                        val records = response.record ?: return
                        for (schedule in records) {
                            arrSchedule?.add(schedule)
                        }
                    }

                    override fun onFailed(status: String, message: String) {
                        when (status) {
                            NotFound -> {
                                arrSchedule?.clear()
                            }
                        }
                    }
                })
            }
        })
    }

    private fun apiMyProfile() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"

        val apiService: MyApiEndpointInterface? = ApiClient(mContext as DashBoardActivity).getClient()?.create(
                MyApiEndpointInterface::class.java)
        val callProfile: Call<ProfileResponse?>? = apiService?.callGetProfile(headers)
        (mContext as DashBoardActivity).callRemoteApi(true, callProfile, object : ApiClient.ApiCallbackListener<ProfileResponse> {
            override fun onDataFetched(response: ProfileResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(requireActivity(), response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<ProfileResponse> {
                    override fun onSuccess(response: ProfileResponse) {
                        val record = response.record ?: return

                        (mContext as DashBoardActivity).appPreferences?.setUserID(mContext, record.id.toString())
                        (mContext as DashBoardActivity).appPreferences?.setProfileImage(mContext, record.profile)
                        (mContext as DashBoardActivity).appPreferences?.setPhone(mContext, record.phone)

                        (mContext as DashBoardActivity).appPreferences?.setPhoneAuthentication(mContext, record.phone_authentication)
                        (mContext as DashBoardActivity).appPreferences?.setPrivateFollowList(mContext, record.private_follow_list)
                        (mContext as DashBoardActivity).appPreferences?.setSecretMode(mContext, record.secret_mode)

                        textViewTitle.text = record.username
                        textViewfollower.text = record.followers.toString()
                        textViewLikeNo.text = record.like_count.toString()
                        val text = getString(R.string.like) + record.like_count.toString()
                        textviewLike?.text = text
                        textViewfollowing.text = record.followings.toString()
                        textViewLevel.text = record.lavel
//                        textviewLike.text = "Like ${likes}"
//                        textvirewRankingImage.text = "Ranking ${ranking}"
                        fragmentGoesInThisTab()
                    }

                    override fun onFailed(status: String, message: String) {
                        when (status) {
                            NotFound -> {
                                arrSchedule?.clear()
                            }
                        }
                    }
                })
            }
        })
    }

    private fun callAddSchedule(scheduleArray: JsonArray?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"
        val array: RequestBody = RequestBody.create(
                MediaType.parse("mulipart/form-data"),
                scheduleArray.toString()
        )
        val apiService: MyApiEndpointInterface? = ApiClient(context).getClient()?.create(MyApiEndpointInterface::class.java)
        val callAddSchedule: Call<AddScheduleResponse?>? = apiService?.callAddSchedule(headers, array)

        (context as DashBoardActivity).callRemoteApi(true, callAddSchedule, object : ApiClient.ApiCallbackListener<AddScheduleResponse> {
            override fun onDataFetched(response: AddScheduleResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(requireActivity(), response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<AddScheduleResponse> {
                        override fun onSuccess(response: AddScheduleResponse) {
                            apiMySchedule()
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onFailed(status: String, message: String) {
                            when (status) {
                                NotFound -> {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    })
            }
        })
    }

    private fun onGalleryClick() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, AppConstants.RequestCode.imagePicker)
    }

    private fun callUpdateProfile(imageFilePath: String?, remove: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(mContext)}"
        val removeImg: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), remove)
        val file = File(imageFilePath ?: "")
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("profile", file.name, requestBody)

        val apiService: MyApiEndpointInterface? = ApiClient(mContext as DashBoardActivity).getClient()?.create(
                MyApiEndpointInterface::class.java)
        val callProfile: Call<JsonObject?>? = apiService?.callUpdateProfile(headers, fileToUpload, removeImg)
        (mContext as DashBoardActivity).callApi(true, callProfile, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Success -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val prorile = getStringFromJson(record, "profile", AppConstants.Defaults.string)
                        (mContext as DashBoardActivity).appPreferences?.setProfileImage(mContext, prorile)
                        loadProfileImage((mContext as DashBoardActivity).appPreferences?.getProfileImage(mContext))
//                        Toast.makeText((mContext as DashBoardActivity), "${getStringFromJson(mainObject, "msg", "")}", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    }
                }
                if ((mContext as DashBoardActivity).dialog?.isShowing == true)
                    (mContext as DashBoardActivity).dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.RequestCode.imagePicker && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    val contentURI = data.data
                    val sImageUri = contentURI
                    val file = File(getRealPathFromURI(mContext, contentURI))
                    val imageFilePath = file.absolutePath
                    Log.d("b ", "dhjk $imageFilePath")
                    callUpdateProfile(imageFilePath, removeImg)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("b ", "dhjk $e")
                    Toast.makeText(mContext, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == AppConstants.RequestCode.imageCamera && resultCode == Activity.RESULT_OK) {
            val file = File(getRealPathFromURI((mContext as DashBoardActivity), imageUri))
            val imageFilePath = file.absolutePath
            Log.d("hjakhg ", "hfh ${imageFilePath}")
            callUpdateProfile(imageFilePath, removeImg)
        }
    }

    override fun onResume() {
        super.onResume()
        apiMyProfile()
    }

    private fun fragmentGoesInThisTab() {
        val fragment: Fragment? = showFragment()
        val fragmentTransaction = (mContext as DashBoardBaseActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framePostClipsRecordingView, fragment!!, "")
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun showFragment(): Fragment? {
        return when (navItemIndex) {
            AppConstants.FragmentsProfileIndex.FragmentPost -> PostFragment()
            AppConstants.FragmentsProfileIndex.FragmentClips -> ClipsFragment()
            else -> StremerPostFragment.getInstance(null)
        }
    }

}
