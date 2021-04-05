package com.mmfinfotech.streameApp.dashBoard.fragment

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
    private var pageNo: Int? = 1
    var liveItemIndex: Int? = AppConstants.FragmentLiveIndex.FragmentLive
    private var mHandler: Handler? = null
    private val index by lazy { 0 }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home_live, container, false).also {
        it.findViewById<View>(R.id.txtViewEvents).isSelected = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mHandler = Handler(Looper.getMainLooper())
        loadHomeFragment()
    }

    private fun listener() {
        txtViewEvents.setOnClickListener {
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLive
            toggleHeaderSelection(it)
        }
        txtViewNewComer.setOnClickListener {
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentNeComer
            toggleHeaderSelection(it)
        }
        txtViewHotTheme.setOnClickListener {
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLiveHotTheme
            toggleHeaderSelection(it)
        }
        txtViewStreme.setOnClickListener {
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLiveStremePro
            toggleHeaderSelection(it)
        }
        txtViewStremeMusic.setOnClickListener {
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLiveStremeMusic
            toggleHeaderSelection(it)
        }
        img1?.setOnClickListener {
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentNotification
            toggleHeaderSelection(it)
        }
        img2?.setOnClickListener {
            liveItemIndex = AppConstants.FragmentLiveIndex.FragmentLeaderBoard
            toggleHeaderSelection(it)
        }
    }

    private fun toggleHeaderSelection(view: View?) {
        txtViewEvents?.isSelected = txtViewEvents?.id == view?.id
        txtViewNewComer?.isSelected = txtViewNewComer?.id == view?.id
        txtViewHotTheme?.isSelected = txtViewHotTheme?.id == view?.id
        txtViewStreme?.isSelected = txtViewStreme?.id == view?.id
        txtViewStremeMusic?.isSelected = txtViewStremeMusic?.id == view?.id
        loadHomeFragment()
    }

    private fun loadHomeFragment() {
        val mPendingRunnable = Runnable {
            val fragment: Fragment = getFragments()
            childFragmentManager.beginTransaction().apply {
                replace(R.id.liveFrames, fragment, "")
                commitAllowingStateLoss()
            }
        }
        if (mPendingRunnable != null) mHandler?.post(mPendingRunnable)
    }

    private fun getFragments(): Fragment {
        return when (liveItemIndex) {
            AppConstants.FragmentLiveIndex.FragmentLive -> LiveFragment()
            AppConstants.FragmentLiveIndex.FragmentNeComer -> CategoryFragment.newInstance(0)
            AppConstants.FragmentLiveIndex.FragmentLiveHotTheme -> CategoryFragment.newInstance(1)
            AppConstants.FragmentLiveIndex.FragmentLiveStremePro -> CategoryFragment.newInstance(2)
            AppConstants.FragmentLiveIndex.FragmentLiveStremeMusic -> CategoryFragment.newInstance(3)
            AppConstants.FragmentLiveIndex.FragmentNotification -> NotificationFragment.getInstance()
            AppConstants.FragmentLiveIndex.FragmentLeaderBoard -> LeaderBoardFragment.getInstance()
            else -> LiveFragment()
        }
    }
}