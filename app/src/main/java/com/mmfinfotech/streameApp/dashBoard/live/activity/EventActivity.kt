package com.mmfinfotech.streameApp.dashBoard.live.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.fragment.EventLiveingFragment
import com.mmfinfotech.streameApp.dashBoard.live.fragment.EventRecruitingFragment

class EventActivity : DashBoardBaseActivity() {
 private val TAG : String? = EventActivity::class.java.simpleName
companion object {
    fun getInstance(
        context: Context?
    ) = Intent(context, EventActivity::class.java).apply {
    }
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_event)

    val tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
    tabLayout.addTab(tabLayout.newTab().setText( getString(R.string.txt_heading_event_requiting) ))
    tabLayout.addTab(tabLayout.newTab().setText( getString(R.string.txt_liveing)))

    val fragmentTransaction = supportFragmentManager.beginTransaction()
//  fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
    fragmentTransaction.replace(R.id.frameEventsView, EventRecruitingFragment(), "")
    fragmentTransaction.commitAllowingStateLoss()
    tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
//            fragmentGoesInThisTab(tab)
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            fragmentGoesInThisTab(tab)
        }
    })
}
private fun getHomeFragment(tab: TabLayout.Tab?): Fragment? {
    return when (tab?.position) {
        0-> EventRecruitingFragment()
        1 -> EventLiveingFragment()
        else -> EventRecruitingFragment()
    }
}
private fun fragmentGoesInThisTab(tab: TabLayout.Tab?) {
    val fragment: Fragment? = getHomeFragment(tab)
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.frameEventsView, fragment!!, "")
    fragmentTransaction.commitAllowingStateLoss()
}
}
