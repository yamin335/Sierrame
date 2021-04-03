package com.mmfinfotech.streameApp.dashBoard.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.live.fragment.*
import com.mmfinfotech.streameApp.util.retrofit.ApiClient
import com.mmfinfotech.streameApp.util.retrofit.MyApiEndpointInterface
import com.mmfinfotech.streameApp.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_home_live.*

class HomeLiveFragment : Fragment() {
    private val TAG = HomeLiveFragment::class.java.simpleName
    private var mContext: Context? = null
    private var pageNo: Int? = 1
    var liveItemIndex: Int? = AppConstants.FragmentLiveIndex.FragmentLive
    private var mHandler: Handler? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreateView(
        inflater : LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_live, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mHandler= Handler()

        val fragmentTransaction = (mContext as DashBoardActivity).supportFragmentManager.beginTransaction()
//      fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.liveFrames, LiveFragment(), "")
        fragmentTransaction.commitAllowingStateLoss()
        headersListners()
        loadHomeFragment()
        setLisners()
//        callHotThemeList(false, true, pageNo)
    }

    private fun getLiveFragment(): Fragment {
        return when (liveItemIndex) {
            AppConstants.FragmentLiveIndex.FragmentLive -> LiveFragment()
            AppConstants.FragmentLiveIndex.FragmentLiveHotTheme -> HotThemeFragment()
            AppConstants.FragmentLiveIndex.FragmentLiveStremePlus -> StremePlusFragment()
            AppConstants.FragmentLiveIndex.FragmentLiveStremeMusic -> StremeMusicFragment()
            AppConstants.FragmentLiveIndex.FragmentNotification -> NotificationFragment.getInstance()
            AppConstants.FragmentLiveIndex.FragmentLeaderBoard -> LeaderBoardFragment.getInstance()
            else -> LiveFragment()
        }
    }

    private fun loadHomeFragment() {
        Log.v(TAG, "LoadHomeFragment method")
          val mPendingRunnable = Runnable {
             val fragment: Fragment? = getLiveFragment()
                val fragmentTransaction = (mContext as DashBoardActivity).supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.liveFrames, fragment!!, "")
                fragmentTransaction.commitAllowingStateLoss()
        }
        if (mPendingRunnable != null) {
            mHandler?.post(mPendingRunnable)
        }
    }

    private fun headersListners() {
        txtViewEvents.setOnClickListener {
            txtViewEvents.isSelected = true
            txtViewHotTheme.isSelected = false
            txtViewStreme.isSelected = false
            txtViewStremeMusic.isSelected = false
//            startActivity(
//                EventActivity.getInstance(
//                    mContext))
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLive
            loadHomeFragment()
        }
        txtViewHotTheme.setOnClickListener {
            txtViewHotTheme.isSelected = true
            txtViewEvents.isSelected = false
            txtViewStreme.isSelected = false
            txtViewStremeMusic.isSelected = false
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLiveHotTheme
            loadHomeFragment()
        }
        txtViewStreme.setOnClickListener {
            txtViewStreme.isSelected = true
            txtViewHotTheme.isSelected = false
            txtViewEvents.isSelected = false
            txtViewStremeMusic.isSelected = false

            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLiveStremePlus
            loadHomeFragment()
        }
        txtViewStremeMusic.setOnClickListener {
            txtViewStremeMusic.isSelected = true
            txtViewHotTheme.isSelected = false
            txtViewStreme.isSelected = false
            txtViewEvents.isSelected = false

            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLiveStremeMusic
            loadHomeFragment()
        }
    }

    private fun setLisners() {
        img1?.setOnClickListener {
//            Toast.makeText(context, "Image one", Toast.LENGTH_SHORT).show()
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentNotification
            loadHomeFragment()
        }
        img2?.setOnClickListener {
//            Toast.makeText(context, "Image two", Toast.LENGTH_SHORT).show()
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLeaderBoard
            loadHomeFragment()
        }
    }

    private fun callHotThemeList(showDialog:Boolean?, isRefresh:Boolean?, pageNo :Int?) {
        showDialog==true
        val sendParams: JsonObject? = JsonObject()
        sendParams?.addProperty(
            "user_id",
        "")
        sendParams?.addProperty("page_no", "1")


        val apiService: MyApiEndpointInterface? = ApiClient(mContext).getClient()?.create(
            MyApiEndpointInterface::class.java
        )
//        val call: Call<JsonObject?>? = apiService?.call(sendParams)
      /*  (mContext as DashBoardActivity).callApi(true, call, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {

                        if ((mContext as DashBoardActivity).appPreferences?.isShowing == true)
                            (mContext as DashBoardActivity).dialog?.dismiss()
                    }
                }
            }

            override fun onFailure() {

            }
        })
    }
    */
}
}