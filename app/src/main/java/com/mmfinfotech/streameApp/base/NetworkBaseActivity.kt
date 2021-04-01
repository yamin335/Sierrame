package com.mmfinfotech.streameApp.base

import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.mmfinfotech.streameApp.util.networkWatcher.NetworkConnectionChecker
import com.mmfinfotech.streameApp.util.networkWatcher.alertNoInternetConnectionBlockUi


/**
 * Created by Somil Rawal on 10-07-2020.
 */
open class NetworkBaseActivity : BaseActivity(), NetworkConnectionChecker.OnConnectivityChangedListener {
    private val tag = NetworkBaseActivity::class.java.simpleName
    private var noInternetConnectionDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (noInternetConnectionDialog == null)
            noInternetConnectionDialog = alertNoInternetConnectionBlockUi(this@NetworkBaseActivity)
    }

    override fun onResume() {
        super.onResume()
        networkConnectionChecker?.registerListener(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        networkConnectionChecker?.unregisterListener(this)
    }

    override fun connectivityChanged(availableNow: Boolean?) {
        if (availableNow == true) {
            if (noInternetConnectionDialog?.isShowing == true) noInternetConnectionDialog?.dismiss()
        } else {
            if (noInternetConnectionDialog?.isShowing == false) noInternetConnectionDialog?.show()
        }
    }

    open fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos: List<ActivityManager.RunningAppProcessInfo>? = activityManager.runningAppProcesses
        if (procInfos != null) {
            for (processInfo in procInfos) {
                if (processInfo.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }

}