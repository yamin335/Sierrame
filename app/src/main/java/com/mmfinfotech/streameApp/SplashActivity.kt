package com.mmfinfotech.streameApp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.mmfinfotech.streameApp.baseActivity.OnBoardingBaseActivity
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.onBoarding.WelComeActivity
import com.mmfinfotech.streameApp.utils.AppConstants
import kotlinx.android.synthetic.main.activity_splash.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashActivity : OnBoardingBaseActivity() {
    private val tag: String? = SplashActivity::class.java.simpleName
    private var primaryProgressStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.v(tag, "Firebase token = ${appPreferences?.getFcmToken(this@SplashActivity)}")

        Thread {
            while (primaryProgressStatus < 100) {
                primaryProgressStatus += 10
                try {
                    Thread.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progressBarSplash.progress = primaryProgressStatus
                if (primaryProgressStatus >= 100) {
                    if (appPreferences?.getAuthToken(this@SplashActivity).equals(AppConstants.Defaults.string)
                    ) {
                        startActivity(WelComeActivity.getInstance(this@SplashActivity))
                        finishAffinity()
                    } else {
                        startActivity(DashBoardActivity.getInstance(this@SplashActivity))
                        finishAffinity()
                    }
                }
            }
        }.start()
    }
}
