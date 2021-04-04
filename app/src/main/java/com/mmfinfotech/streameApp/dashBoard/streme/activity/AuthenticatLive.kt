package com.mmfinfotech.streameApp.dashBoard.streme.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.agora.base.BaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.activity.LiveActivity
import com.mmfinfotech.streameApp.models.HomeLive
import com.mmfinfotech.streameApp.models.LiveStreamCategory
import com.mmfinfotech.streameApp.models.LiveStreamHashTag
import com.mmfinfotech.streameApp.models.LiversProfile
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.listners.OnDialogPasswordListner
import com.mmfinfotech.streameApp.util.listners.OnMemoAddListners
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.AppToast
import kotlinx.android.synthetic.main.activity_authentication_process.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList

class AuthenticatLive : BaseActivity() {
    private val tag: String? = AuthenticatLive::class.java.simpleName
    var hasTagArray: ArrayList<LiveStreamHashTag?>? = null
    var arrSelectedHashTag: ArrayList<LiveStreamHashTag?>? = null
    private var StremerUserID: String? = null
    private var CategoryId: String? = ""

    private val PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    companion object {
        val Hastags = "HASTAGS"
        val Category = "Category"
        val UuId = "UUID"
        fun getInstance(
            context: Context?,
            uuId: String?,
            hastags: ArrayList<LiveStreamHashTag?>?,
            categorys: ArrayList<LiveStreamCategory?>?
        ) = Intent(context, AuthenticatLive::class.java).apply {
            putParcelableArrayListExtra(Hastags, hastags)
            putParcelableArrayListExtra(Category, categorys)
            putExtra(UuId, uuId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_process)
        arrSelectedHashTag = ArrayList()
        hasTagArray = ArrayList()
        hasTagArray = intent?.getParcelableArrayListExtra(Hastags)
        StremerUserID = intent.getStringExtra(UuId)
        setListners()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == AppConstants.PERMISSION_REQ_CODE) {
            checkPermission()
        }
    }

    private fun setListners() {
        buttonCreate.setOnClickListener { checkPermission() }

        textViewGift.setOnClickListener {
            val arrayList: ArrayList<String?>? = ArrayList()
            arrayList?.add("BoY")
            arrayList?.add("Girl")
            arrayList?.add("BoY")
            arrayList?.add("BoY")
            arrayList?.add("Girl")
        }

        textViewAuthenticateLiveCategory.setOnClickListener {
            showChetegoryLiveList(this@AuthenticatLive, intent?.getParcelableArrayListExtra(Category), textViewAuthenticateLiveCategory,
                object : OnMemoAddListners {
                    override fun saveStringListners(view: View, s: String) {
                        CategoryId = s
                    }

                    override fun cancelViewListners(view: View) {}
                })
        }

        textViewMyEvent.setOnClickListener {
            val arrEventLive: ArrayList<HomeLive?>? = ArrayList()
            arrEventLive?.add(
                HomeLive(
                    "",
                    "",
                    "Time 00:00:00000",
                    "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                )
            )
            arrEventLive?.add(
                HomeLive(
                    "",
                    "",
                    "Time 00:00:00000",
                    "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                )
            )
            showMyEvents(this@AuthenticatLive, arrEventLive)
        }

        buttonHashTag.setOnClickListener {
            startActivityForResult(
                HashTagesActivity.getInstance(this@AuthenticatLive, hasTagArray, arrSelectedHashTag),
                AppConstants.RequestCode.activityHashTags
            )
        }
    }

    private fun setHashTagChipGroup(hashTagsList: ArrayList<LiveStreamHashTag?>?) {
        for (i in 0 until (hashTagsList?.size ?: 0)) {
            val chip = Chip(this@AuthenticatLive)
            chip.text = hashTagsList?.get(i)?.tag_name
            // necessary to get single selection working
            chip.isClickable = false
            chip.isCheckable = false
            chip.setOnClickListener {
                if (hashTagsList?.get(i)?.id == 0) {
                    chip.isClickable = true
                    hashTagsList.remove(hashTagsList[i])
                    chipGroupSelectedHashTagList.removeView(chip)
                } else {
                    chip.isClickable = false
                }
            }
            chipGroupSelectedHashTagList.addView(chip)
        }
    }

    private fun apiGoLive(accessOption: String?, password: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@AuthenticatLive)}"

        val list: ArrayList<String?>? = ArrayList()
        for (i in 0 until arrSelectedHashTag?.size!!) list?.add(arrSelectedHashTag?.get(i)?.tag_name)
        list?.joinToString()
        val requestChennelId: RequestBody = RequestBody.create(MediaType.parse("text/plain"), StremerUserID ?: AppConstants.Defaults.string)
        val requestTitle: RequestBody = RequestBody.create(MediaType.parse("text/plain"), textViewAuthenticateLiveTitle.text.toString())
        val requestDiscription: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
        val requestCategory: RequestBody = RequestBody.create(MediaType.parse("text/plain"), CategoryId ?: AppConstants.Defaults.string)
        val requestAccessOption: RequestBody = RequestBody.create(MediaType.parse("text/plain"), accessOption ?: AppConstants.Defaults.string)
        val requestPwd: RequestBody = RequestBody.create(MediaType.parse("text/plain"), password ?: AppConstants.Defaults.string)
        val array: RequestBody = RequestBody.create(MediaType.parse("text/plain"), list?.joinToString() ?: AppConstants.Defaults.string)

        val apiService: MyApiEndpointInterface? = ApiClient(this@AuthenticatLive).getClient()?.create(MyApiEndpointInterface::class.java)
        val callAddSchedule: Call<JsonObject?>? = apiService?.callStreamLive(
            headers, requestChennelId, requestTitle, requestDiscription,
            array, requestCategory, requestAccessOption, requestPwd
        )
        callApi(true, callAddSchedule, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val channel =
                            getStringFromJson(record, "channel", AppConstants.Defaults.string)
                        val rtc = getStringFromJson(record, "rtc", AppConstants.Defaults.string)
                        val rtm = getStringFromJson(record, "rtm", AppConstants.Defaults.string)
                        val userAccount = getStringFromJson(record, "user_account", AppConstants.Defaults.string)

                        val streamingId = getStringFromJson(record, "streaming_id", AppConstants.Defaults.string)

                        config()?.channelName = channel
                        config()?.rtcChannelToken = rtc
                        config()?.rtmChannelToken = rtm
                        config()?.streamingId = streamingId

                        val liversProfile: LiversProfile? = LiversProfile(
                            streamingId, "", "", "", "", "", "",
                            "", "", "", "", "", "",
                            "", "", "", "","",
                            streamingId, "", "", rtc, rtm, userAccount,
                            ArrayList(), ArrayList(), ArrayList()
                        )

                        Log.i(tag, " Authenticate Channel Name $channel")
                        Log.i(tag, " Authenticate RTC $rtc")
                        Log.i(tag, " Authenticate RTM $rtm")
                        Log.i(tag, " Authenticate streamingId $streamingId")
                        startActivity(LiveActivity.getInstance(applicationContext, liversProfile, true))
//                        startActivity(LiveTwoActivity.getInstance(applicationContext, liversProfile,true))
//                        val intent = Intent(intent)
//                        intent.putExtra(AppConstants.KEY_CLIENT_ROLE, Constants.CLIENT_ROLE_BROADCASTER)
//                        intent.setClass(applicationContext, LiveActivityDemo::class.java)
//                        intent.putExtra(AppConstants.IntentExtras.liversProfile, liversProfile)
//                        startActivity(intent)
                        finish()
                    }
                    NotFound -> {
                        val msg =
                            getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@AuthenticatLive, msg, Toast.LENGTH_LONG).show()
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
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.RequestCode.activityHashTags -> {
                if (Activity.RESULT_OK == resultCode) {
                    if (Activity.RESULT_OK == resultCode) {
                        arrSelectedHashTag?.removeAll(Collections.singleton(null))
                        arrSelectedHashTag?.clear()
                        val status = data?.getStringExtra(AppConstants.IntentExtras.selectedStatus)
                        val list: ArrayList<LiveStreamHashTag?>? = ArrayList()
                        list?.addAll(data?.getParcelableArrayListExtra(AppConstants.IntentExtras.selectedHashTags)!!)
                        for (i in 0 until (list?.size ?: 0)) {
                            if (list?.get(i)?.selectedValue == true) arrSelectedHashTag?.add(list[i])
                        }
                        chipGroupSelectedHashTagList.removeAllViews()
                        setHashTagChipGroup(arrSelectedHashTag)
                    } else if (Activity.RESULT_CANCELED == resultCode) {
                        arrSelectedHashTag?.clear()
                    }
                }
            }
        }
    }

    private fun validation(): Boolean? {
        val authenticateLiveTitle = textViewAuthenticateLiveTitle?.text
        val authenticateLiveCategory = textViewAuthenticateLiveCategory?.text
        if (TextUtils.isEmpty(authenticateLiveTitle)) {
            AppToast.showToast(this@AuthenticatLive, getString(R.string.title_empty))
            return false
        }

        if (arrSelectedHashTag?.isNullOrEmpty() == true) {
            AppToast.showToast(this@AuthenticatLive, getString(R.string.empty_hashTags))
            return false
        }

        if (TextUtils.isEmpty(authenticateLiveCategory)) {
            AppToast.showToast(this@AuthenticatLive, getString(R.string.please_choose_category))
            return false
        }

        return true
    }

    private fun checkPermission() {
        var granted = true
        for (per in PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false
                break
            }
        }
        if (granted) {
//            apiGoLive()
            if (validation() == true) {
                if (appCompactCheckboxPrivateBroadcast?.isChecked == true) {
                    createPrivateBroadCasting(this@AuthenticatLive, getString(R.string.dialogDescriptionPassUserGoLive), true,
                        object : OnDialogPasswordListner {
                            override fun onButtonClick(view: View?, s: String?) {
                                apiGoLive(AppConstants.BroadCastAccessOption.Private, s)
                            }

                            override fun onShareButtonClick(view: View?, PasswordToShare: String?) {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, PasswordToShare)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                startActivity(shareIntent)
                            }
                        })
                } else {
                    apiGoLive(AppConstants.BroadCastAccessOption.Public, "")
                }
            }
        } else {
            requestPermissions()
        }
    }

    private fun permissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this@AuthenticatLive,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, AppConstants.PERMISSION_REQ_CODE)
    }

    fun onCloseClick(view: View) {
        onBackPressed()
    }
}