package com.mmfinfotech.streameApp.dashBoard.live.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.live.fragment.CommentsFragment
import com.mmfinfotech.streameApp.dashBoard.live.fragment.IntroductionFragment

class RecomendationActivity : AppCompatActivity() {
    private val TAG : String? = RecomendationActivity::class.java.simpleName

    companion object {
        fun getInstance(
                context: Context?
        ) = Intent(context, RecomendationActivity::class.java).apply {
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendation)

        val tabLayout = findViewById<View>(R.id.tabLayoutRecomendation) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Introduction"))
        tabLayout.addTab(tabLayout.newTab().setText("Comment"))

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameRecomendedView, IntroductionFragment(), "")
        fragmentTransaction.commitAllowingStateLoss()
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                fragmentGoesInThisTab(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                fragmentGoesInThisTab(tab)
            }
        })
    }
    private fun getHomeFragment(tab: TabLayout.Tab?): Fragment? {
        return when (tab?.position) {
            0 -> IntroductionFragment()
            1 -> CommentsFragment()
            else -> IntroductionFragment()
        }
    }
    private fun fragmentGoesInThisTab(tab: TabLayout.Tab?) {
        val fragment: Fragment? = getHomeFragment(tab)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameRecomendedView, fragment!!, "")
        fragmentTransaction.commitAllowingStateLoss()
    }
}
