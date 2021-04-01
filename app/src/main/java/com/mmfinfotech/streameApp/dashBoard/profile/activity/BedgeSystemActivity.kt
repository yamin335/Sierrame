package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.profile.fragment.PurchasRecordFragment
import com.mmfinfotech.streameApp.dashBoard.profile.fragment.ChallengeBedgeFragment
import kotlinx.android.synthetic.main.activity_bedge_system.*

class BedgeSystemActivity : AppCompatActivity() {
    val Tag: String? = BedgeSystemActivity::class.java.simpleName
    private var isMysearch: Boolean? = true
    private var toggleItemIndex: Int? = 0
    private var mHandler: Handler? = null

   companion object {
        fun getInstance(
                context: Context?
        ) = Intent(context, BedgeSystemActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bedge_system)
        mHandler= Handler()
        initView()
        setLisners()
        loadHomeFragment()
    }
    private fun initView() {
            findViewById<ConstraintLayout>(R.id.includeToolbarBadgeSystem)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.badge_system)
    }

    private fun setLisners() {
        togglebtnMysearch.setOnClickListener { toggleButton(true) }
        togglebtnChallenge.setOnClickListener { toggleButton(false) }
    }

    private fun toggleButton(isMySearch: Boolean) {
        this.isMysearch = isMySearch
        togglebtnMysearch.isChecked = isMySearch
        togglebtnChallenge.isChecked = !isMySearch
        onButtonToggleFragmetSelectedLisner(if (isMySearch) togglebtnMysearch else togglebtnChallenge)
    }

    private fun onButtonToggleFragmetSelectedLisner(dsa: ToggleButton?) {
        when (dsa) {
            togglebtnMysearch -> {
                toggleItemIndex = 0
            }
            togglebtnChallenge -> {
                toggleItemIndex = 1
            }
            else -> {
                toggleItemIndex = 0
            }
        }
        loadHomeFragment()
    }
    private fun getFragment(): Fragment? {
        return when (toggleItemIndex) {
            0 -> {
                PurchasRecordFragment()
            }
            1 -> {
                ChallengeBedgeFragment()
            }
            else -> PurchasRecordFragment()
        }
    }
    private fun loadHomeFragment() {
        val mPendingRunnable = Runnable {
            val fragment: Fragment? = getFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            fragmentTransaction.replace(R.id.frameToggleBedge, fragment!!, "currentTag")
            fragmentTransaction.commitAllowingStateLoss()
        }

        if (mPendingRunnable != null) {
            mHandler?.post(mPendingRunnable)
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