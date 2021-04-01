package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.base.NetworkBaseActivity
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.fragment.EventLiveingFragment
import com.mmfinfotech.streameApp.dashBoard.profile.fragment.CheeringProfileFragment
import com.mmfinfotech.streameApp.dashBoard.profile.fragment.RankingTotalFragment
import com.mmfinfotech.streameApp.utils.AppConstants

class RankingCharringProfileActivity : NetworkBaseActivity() {
    private val TAG: String? = RankingCharringProfileActivity::class.java.simpleName
    private var intentFrom: String? = AppConstants.Defaults.string

    /**
     * Cheering Menu item in profile
     * Ranking Clicking
     * */

    companion object {
        const val ACTION_Ranking = "action_ranking"
        const val ACTION_Cheering = "action_cheering"
        fun getInstance(
            context: Context?, action: String?
        ) = Intent(context, RankingCharringProfileActivity::class.java).apply {
            setAction(action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        intentFrom = intent?.action
        initView()

        findViewById<ConstraintLayout>(R.id.includeToolbarRankingCharring)?.findViewById<ImageView>(R.id.imageToolbarBack)
            ?.setOnClickListener {
                onBackPressed()
            }

        val tabLayout = findViewById<View>(R.id.tabLayoutRanking) as TabLayout
        when (intentFrom) {
            ACTION_Ranking -> {
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.txt_total)))
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.txt_month)))
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.txt_day)))

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameRanksView, RankingTotalFragment(), "")
                fragmentTransaction.commitAllowingStateLoss()
            }
            ACTION_Cheering -> {
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.txt_this_month)))
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.txt_total)))

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameRanksView, CheeringProfileFragment(), "")
                fragmentTransaction.commitAllowingStateLoss()
            }
            else -> { }
        }

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                fragmentGoesInThisTab(tab)
            }
        })
    }


    private fun fragmentGoesInThisTab(tab: TabLayout.Tab?) {
        val fragment: Fragment? = getCheeringFragment(tab)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameRanksView, fragment!!, "")
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun initView() {
        when (intentFrom) {
            ACTION_Ranking -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarRankingCharring)?.
                findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.titleRanking)
            }
            ACTION_Cheering -> {
                findViewById<ConstraintLayout>(R.id.includeToolbarRankingCharring)?.
                findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.titleCheering)
            }
        }
    }

    private fun getCheeringFragment(tab: TabLayout.Tab?): Fragment? {
       when (intentFrom) {
            ACTION_Ranking -> {
                return when (tab?.position) {
                    0 -> RankingTotalFragment()
                    1 -> EventLiveingFragment.getInstance("2")
                    2 -> EventLiveingFragment.getInstance("3")
                    else -> RankingTotalFragment()
                }
            }
            ACTION_Cheering -> {
                return when (tab?.position) {
                    0 -> CheeringProfileFragment()
                    1 -> EventLiveingFragment()
                    else -> CheeringProfileFragment()
                }
            }
            else -> {
               return RankingTotalFragment()
                }
            }
        }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
