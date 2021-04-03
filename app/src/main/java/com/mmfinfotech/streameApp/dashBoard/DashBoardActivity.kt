package com.mmfinfotech.streameApp.dashBoard

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.fragment.*
import com.mmfinfotech.streameApp.dashBoard.live.activity.LiveActivity
import com.mmfinfotech.streameApp.dashBoard.streme.activity.AddClipActivity
import com.mmfinfotech.streameApp.dashBoard.streme.activity.AuthenticatLive
import com.mmfinfotech.streameApp.dashBoard.streme.activity.AuthenticationActivity
import com.mmfinfotech.streameApp.dashBoard.streme.activity.CameraActivity
import com.mmfinfotech.streameApp.models.Category
import com.mmfinfotech.streameApp.models.Hashtags
import com.mmfinfotech.streameApp.models.LiversProfile
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_dash_board.*
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class DashBoardActivity : DashBoardBaseActivity() {
    var navItemIndex: Int? = AppConstants.FragmentIndex.FragmentHomeLive
    var currentTag: String? = AppConstants.FragmentTag.TagHomeLive
    private var mHandler: Handler? = null
    private var navigation: BottomNavigationView? = null

    companion object {
        fun getInstance(context: Context?) = Intent(context, DashBoardActivity::class.java)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == AppConstants.IntentActions.actionNotification){
            Toast.makeText(this@DashBoardActivity, "New Intent Notification", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        mHandler = Handler()
        navigation = findViewById(R.id.bottom_navigation_view)
        navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            navItemIndex = AppConstants.FragmentIndex.FragmentHomeLive
            currentTag = AppConstants.FragmentTag.TagHomeLive
            loadHomeFragment()
        }

        if (intent.action == AppConstants.IntentActions.actionNotification) {
//            Log.d("djk","jkh ${intent.action}")
//            val intentGo  = Intent(this, NotificationActivity::class.java)
//            intentGo.action = AppConstants.IntentActions.actionNotification
//            intentGo.putExtra(AppConstants.IntentExtras.NotifictnRemoteRefrenceId,intent.getStringExtra(AppConstants.IntentExtras.NotifictnRemoteRefrenceId))
//            intentGo.putExtra(AppConstants.IntentExtras.NotifictnRemoteMsg,intent.getStringExtra(AppConstants.IntentExtras.NotifictnRemoteMsg))
//            intentGo.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intentGo)
            Toast.makeText(this@DashBoardActivity, "OnCreate Intent Notification", Toast.LENGTH_LONG).show()

            showDialogAcceptReject(intent.getStringExtra(AppConstants.IntentExtras.NotifictnRemoteRefrenceId),
                intent.getStringExtra(AppConstants.IntentExtras.NotifictnRemoteMsg))
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_live -> {
                navItemIndex = AppConstants.FragmentIndex.FragmentHomeLive
                currentTag = AppConstants.FragmentTag.TagHomeLive
            }
            R.id.navigation_search -> {
                navItemIndex = AppConstants.FragmentIndex.FragmentSearch
                currentTag = AppConstants.FragmentTag.TagSearch
            }
            R.id.navigation_streme -> {
                showAlertStreame(this@DashBoardActivity) { view ->
                    when (view?.id) {
                        R.id.container_post -> {
                            startActivity(CameraActivity.getInstance(this))
                        }
                        R.id.container_Live -> {
                            LiveAthentication()
                        }
                        R.id.container_clips -> {
                            if (checkHasPermission(this@DashBoardActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == true &&
                                checkHasPermission(this@DashBoardActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == true
                            ) {
                                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                galleryIntent.type = "video/*"
                                startActivityForResult(galleryIntent, AppConstants.RequestCode.videoPicker)
                            } else {
                                requestPermissions(
                                    arrayOf(
                                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ), AppConstants.Permission.multiplePermissions
                                )
                            }
                        }
                    }
                }
            }
            R.id.navigation_timeline -> {
                navItemIndex = AppConstants.FragmentIndex.FragmentTimeLine
                currentTag = AppConstants.FragmentTag.TagTimeLine
            }
            R.id.navigation_profile_page -> {
                navItemIndex = AppConstants.FragmentIndex.FragmentProfilePage
                currentTag = AppConstants.FragmentTag.TagProfilePage
            }
            else -> {
                navItemIndex = AppConstants.FragmentIndex.FragmentHomeLive
                currentTag = AppConstants.FragmentTag.TagHomeLive
            }
        }
        loadHomeFragment()
        true
    }

    private fun loadHomeFragment() {
        // set toolbar title
        setToolbarTitle()
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        val mPendingRunnable = Runnable {
            // update the main content by replacing fragments

            val fragment: Fragment? = getHomeFragment()
            val checkFragment = supportFragmentManager.findFragmentByTag(currentTag)
            // If fragment is already present then what code should do.

            if (checkFragment != null && checkFragment.isVisible) {

            } else {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame_container_dashboard, fragment!!, currentTag)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler?.post(mPendingRunnable)
        }

        // refresh toolbar menu
        invalidateOptionsMenu()
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        if (AppConstants.IntentActions.actionNotificationAccept.equals(
//                intent?.action,
//                ignoreCase = true
//            )
//        ) {}
//        else if (intent?.action == AppConstants.IntentActions.actionNotification) {
//                val intentGo  = Intent(this, NotificationActivity::class.java)
//                intentGo.action = AppConstants.IntentActions.actionNotification
//                intentGo.putExtra(AppConstants.IntentExtras.NotifictnRemoteRefrenceId,intent.getStringExtra(AppConstants.IntentExtras.NotifictnRemoteRefrenceId))
//                intentGo.putExtra(AppConstants.IntentExtras.NotifictnRemoteMsg,intent.getStringExtra(AppConstants.IntentExtras.NotifictnRemoteMsg))
//                intentGo.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intentGo)
//            }
//        else{}
//}

    private fun showDialogAcceptReject(RefrenceId: String?, msg:String?) {
        val dialog = LayoutInflater.from(this@DashBoardActivity).inflate(
            R.layout.accept_reject_invitatn,
            null,
            false
        )

        val builder = AlertDialog.Builder(this@DashBoardActivity)
        builder.setView(dialog)
        dialog.setBackgroundColor(Color.WHITE)
        dialog.findViewById<TextView>(R.id.textviewSubTitle).text=msg
        val buttonOk = dialog.findViewById<TextView>(R.id.imagebuttonConfirm)
        val buttonCancel = dialog.findViewById<TextView>(R.id.imagebuttonCancel)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
//        val vibrator: Vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100,200,300,400,500,400,300,200,400)
//        vibrator.vibrate(pattern , 0)
        buttonOk.setOnClickListener {
            alertDialog.dismiss()
//            vibrator.cancel()
            acceptInvitation(RefrenceId)
        }
        buttonCancel.setOnClickListener {
            RejectInvitation(RefrenceId)
            alertDialog.dismiss()
//            vibrator.cancel()
        }
        Handler().postDelayed({
            if(alertDialog.isShowing) {
                if(this.isDestroyed) {
                } else {
                    alertDialog.dismiss()
//                    vibrator.cancel()
                }
            }
        }, 60000)
        alertDialog.show()
    }

    private fun acceptInvitation(id: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("streaming_id", id)

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callStreamPost: Call<JsonObject?>? = apiService?.callAcceptInvitation(headers, sendParams)
        callApi(true, callStreamPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        val id: String? = getStringFromJson(record, "id", AppConstants.Defaults.string)
                        val channelId: String? = getStringFromJson(record, "channel_id", AppConstants.Defaults.string)
                        val userId: String? = getStringFromJson(record, "user_id", AppConstants.Defaults.string)
                        val title: String? = getStringFromJson(record, "title", AppConstants.Defaults.string)
                        val description: String? = getStringFromJson(record, "discription", AppConstants.Defaults.string)
                        val hashTags: String? = getStringFromJson(record, "hashtags", AppConstants.Defaults.string)
                        val category: String? = getStringFromJson(record, "category", AppConstants.Defaults.string)
                        val startTime: String? = getStringFromJson(record, "start_time", AppConstants.Defaults.string)
                        val endTime: String? = getStringFromJson(record, "end_time", AppConstants.Defaults.string)
                        val status: String? = getStringFromJson(record, "status", AppConstants.Defaults.string)
                        val addedOn: String? = getStringFromJson(record, "added_on", AppConstants.Defaults.string)
                        val updateOn: String? = getStringFromJson(record, "update_on", AppConstants.Defaults.string)
                        val streamerStatus: String? = getStringFromJson(record, "streamer_status", AppConstants.Defaults.string)
                        val streamerId: String? = getStringFromJson(record, "streamer_id", AppConstants.Defaults.string)
                        val streamerName: String? = getStringFromJson(record, "streamer_name", AppConstants.Defaults.string)
                        val streamerProfile: String? = getStringFromJson(record, "streamer_profile", AppConstants.Defaults.string)
                        val like_status: String? = getStringFromJson(record, "like_status", AppConstants.Defaults.string)
                        val rtcToken: String? = getStringFromJson(record, "rtc_token", AppConstants.Defaults.string)
                        val rtmObject: JsonObject? = getJsonObjFromJson(record, "rtm", JsonObject())
                        val rtmToken: String? = getStringFromJson(rtmObject, "rtm", AppConstants.Defaults.string)
                        val userAccount: String? = getStringFromJson(rtmObject, "user_account", AppConstants.Defaults.string)


                        val liversProfile: LiversProfile? = LiversProfile(
                            id, channelId, userId, title, description, hashTags, category,
                            startTime, endTime, status, addedOn, updateOn, "",
                            streamerStatus, "",like_status, "", "",
                            streamerId, streamerName, streamerProfile, rtcToken, rtmToken, userAccount,ArrayList(), ArrayList(), ArrayList()
                        )
                        config()?.channelName = channelId
                        config()?.rtcChannelToken = rtcToken
                        config()?.rtmChannelToken = rtmToken
                        config()?.streamingId = userId
                        startActivity(LiveActivity.getInstance(this@DashBoardActivity, liversProfile, true))
                        finishAffinity()
                    }
                    ValidationError -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@DashBoardActivity, "${msg}", Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })


    }

    private fun RejectInvitation(id: String?) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this)}"
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty("streaming_id", id)

        val apiService: MyApiEndpointInterface? = ApiClient(this).getClient()?.create(
            MyApiEndpointInterface::class.java
        )

        val callStreamPost: Call<JsonObject?>? = apiService?.callRejectStreame(headers, sendParams)
        callApi(true, callStreamPost, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record = getJsonObjFromJson(mainObject, record, JsonObject())
                        finish()
                    }
                    ValidationError -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(this@DashBoardActivity, "${msg}", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()

//                    vibrator.cancel()
            }

            override fun onFailure() {}
        })


    }
    private fun setToolbarTitle() {
        when (navItemIndex) {
            AppConstants.FragmentIndex.FragmentHomeLive -> {
            }
            AppConstants.FragmentIndex.FragmentSearch -> {
            }
            AppConstants.FragmentIndex.FragmentTimeLine -> {
            }
            AppConstants.FragmentIndex.FragmentProfilePage -> {
            }
            AppConstants.FragmentIndex.FragmentStreame -> {
            }
            else -> {
            }
        }
    }

    private fun getHomeFragment(): Fragment? {
        return when (navItemIndex) {
            AppConstants.FragmentIndex.FragmentHomeLive -> HomeLiveFragment()
            AppConstants.FragmentIndex.FragmentSearch -> SearchFragment()
            AppConstants.FragmentIndex.FragmentTimeLine -> TimeLineFragment()
            AppConstants.FragmentIndex.FragmentProfilePage -> ProfilePageFragment()
            AppConstants.FragmentIndex.FragmentStreame -> StreameFragment()
            else -> HomeLiveFragment()
        }
    }

    private fun LiveAthentication() {
        val hasTagArray: ArrayList<Hashtags?>? = ArrayList()
        val arrcategoryArray: ArrayList<Category?>? = ArrayList()
        val headers = HashMap<String, String>()
//      headers["Authorization"] = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMDBiMDc1NjgyYzNhMTViMTA2ZmNmYTJkNjNiZDNlZTA2OGMxMjBmZjU4ODYzMDAxNTAyMDRjNGJjMWI3MDIwNzI1OGIyZWE5OWNkYzEzNmEiLCJpYXQiOjE1OTc0MDY0NDksIm5iZiI6MTU5NzQwNjQ0OSwiZXhwIjoxNjI4OTQyNDQ5LCJzdWIiOiI5Iiwic2NvcGVzIjpbXX0.pF_G1N3P2Td8GAm1I-WYZ2hbR_WmmhvjhaRWi9qaP_fTUHXQnGBrE7HpAw3QIg_4_X2FMWjZiPmc4dg3l2Z0ddAwgdIsHYnmk5mtz2bC4ib1kQDIih2bSZZcZ2zx3bcy5c_kbTkvivLzzFutSBbZ4mXKRls7JgQMayx0kxH1AT-yoXC3I1jMEoZqk7xVx_vA4IDxdNKijkOvLDH3YkcvHcbRrC1ck9Dc3KGtIhhDgAraCF3v1LPiVJ6Sf2arTyM7i5T7asE_uFnvwmcxQ3kYYWIIB-KgwR2hTQy6MwoYBPgYDyl0E8k1PO5z72T_qXxFR_r0YVnvk9xFDN0mou9qBaQwKWs6YIhMAp2mBWu22Yx0p6ymYw82Xr4nIlWpUWjSiR1IrvB1W7B0xyJhL35fOhT-dDA55DQcLCO60keYCyyNchnyy2X7JtnGQ6rmaPu9dUmwHJ0atb91YWsrzLtKSsQ8efoN7vUMCdLCoQF_m3hpo0_NB0jFX0CqZU84wLSu7-Mw57Y0H0xQqRXVGY7RAgmEZtTgiFK_XumUOntIiDhydkwayXg3JukTqp_gMosiO4tQgO3eQotNYwlm-sz91tw-q-wEBXT1poDtmJ7vQSRlNvxJyY1OHMACRRpGPJhbb27NFI1tHtqeZz4xFunJsEHns0Dm326f3_1XVIeY_qM"
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@DashBoardActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@DashBoardActivity).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
        val callStremeData: Call<JsonObject?>? = apiService?.callStremeData(headers)
        callApi(true, callStremeData, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val recordObj = getJsonObjFromJson(mainObject, record, JsonObject())
                        val hashArray = getJsonArrayFromJson(recordObj, "hashtags", JsonArray())
                        val uuid = getStringFromJson(recordObj, "uuid", "")

                        for (i in 0 until hashArray!!.size()) {
                            val hashTagsObjects = getJsonObjFromJson(hashArray, i, JsonObject())
                            val id = getStringFromJson(
                                hashTagsObjects,
                                "id",
                                AppConstants.Defaults.string
                            )
                            val tag_name = getStringFromJson(
                                hashTagsObjects,
                                "tag_name",
                                AppConstants.Defaults.string
                            )
                            hasTagArray?.add(Hashtags(id, tag_name))
                        }
                        val categoryArray = getJsonArrayFromJson(recordObj, "category", JsonArray())
                        for (i in 0 until categoryArray!!.size()) {
                            val categoryObjects = getJsonObjFromJson(categoryArray, i, JsonObject())
                            val id = getStringFromJson(
                                categoryObjects,
                                "id",
                                AppConstants.Defaults.string
                            )
                            val name = getStringFromJson(
                                categoryObjects,
                                "name",
                                AppConstants.Defaults.string
                            )
                            arrcategoryArray?.add(Category(id, name))
                        }
                        startActivity(
                            AuthenticatLive.getInstance(
                                this@DashBoardActivity,
                                uuid,
                                hasTagArray,
                                arrcategoryArray
                            )
                        )
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
//                        Toast.makeText(this@DashBoardActivity, "${msg}", Toast.LENGTH_LONG).show()
                    }
                    NotVerify -> {
                        showAlertCoutionLiveStreame(
                            this@DashBoardActivity,
                            object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    when (v?.id) {
                                        R.id.buttonAuthentication -> {
                                            val recordObj = getJsonObjFromJson(
                                                mainObject,
                                                record,
                                                JsonObject()
                                            )
                                            startActivity(AuthenticationActivity.getInstance(this@DashBoardActivity, AuthenticationActivity.ActionLive))
                                        }
                                    }
                                }
                            })
                        val msg = getStringFromJson(
                            mainObject,
                            message,
                            AppConstants.Defaults.string
                        )
                        Toast.makeText(this@DashBoardActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
                if (dialog?.isShowing == true)
                    dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }

    fun setConfig(channel: String?, rtcToken: String?, rtmToken: String?, streamId: String?) {
        config()?.channelName = channel
        config()?.rtcChannelToken = rtcToken
        config()?.rtmChannelToken = rtmToken
        config()?.streamingId = streamId
    }

    override fun onBackPressed() {
        if (currentTag == AppConstants.FragmentTag.TagHomeLive) {
            exitDialog(
                this@DashBoardActivity,
                getString(R.string.titleExit),
                getString(R.string.txt_are_you_sure_you_want_to_logout),
                View.OnClickListener { v ->
                    when (v?.id) {
                        R.id.buttonSave -> {
                            finishAffinity()
                        }
                        R.id.buttonCancel -> {
                        }
                    }
                })
            bottom_navigation_view.menu.getItem(0).setChecked(true);
        } else {
            navItemIndex = AppConstants.FragmentIndex.FragmentHomeLive
            currentTag = AppConstants.FragmentTag.TagHomeLive
            loadHomeFragment()
            bottom_navigation_view.menu.getItem(0).setChecked(true);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.RequestCode.videoPicker && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    val contentURI = data.data
                    val sImageUri = contentURI
                    val file = File(getRealPathFromURI(this@DashBoardActivity, contentURI))
                    val videoPath = file.absolutePath
                    val size = getVideoDuration(this, file)
//                    val thumb: Bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MICRO_KIND)
//                    val thumbString = BitmaToString(thumb)
//                    Log.d("jj", "thumb $thumbString")

                    if (size / 1000 in 3..60) {
                        startActivity(
                            AddClipActivity.getInstance(
                                this@DashBoardActivity,
                                videoPath
                            )
                        )
                    } else {
                        ShowAlertInformation(
                            this,
                            getString(R.string.video_longer_please_try_another_limitation )
                        )
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppConstants.Permission.multiplePermissions -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                    (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                ) {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    galleryIntent.type = "video/*"
                    startActivityForResult(galleryIntent, AppConstants.RequestCode.videoPicker)
                }
                return
            }
        }
    }

//    public String BitMapToString(Bitmap bitmap){
//        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//        byte [] b=baos.toByteArray();
//        String temp=Base64.encodeToString(b, Base64.DEFAULT);
//        return temp;
//    }

    fun BitmaToString(bitmap: Bitmap?): String? {
        val baos: ByteArrayOutputStream? = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray? = baos?.toByteArray()
        val temp: String? = Base64.encodeToString(b, Base64.DEFAULT)
        return temp
    }


}