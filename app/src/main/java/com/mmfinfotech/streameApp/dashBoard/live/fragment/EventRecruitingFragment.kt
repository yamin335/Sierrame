package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterEventRecruiting
import com.mmfinfotech.streameApp.models.HomeLive
import kotlinx.android.synthetic.main.fragment_event_recruiting.*

/**
 * A simple [Fragment] subclass.
 * Use the [EventRecruitingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventRecruitingFragment : Fragment() {
    private val TAG: String? = EventRecruitingFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrEventRecruiting: ArrayList<HomeLive?>? = null
    private var adapterEventRecruiting: AdapterEventRecruiting? = null
    private var pageNo: Int? = 1
    private var mLayoutManager: LinearLayoutManager? = null
    var tabLayout: TabLayout? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_recruiting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = view.findViewById<View>(R.id.tabLayoutRequiter) as TabLayout
        tabLayout?.addTab(tabLayout!!.newTab().setText(getString(R.string.txt_recruiting)))
        tabLayout?.addTab(tabLayout!!.newTab().setText(getString(R.string.txt_details)))

        val fragmentTransaction = (mContext as DashBoardBaseActivity).supportFragmentManager?.beginTransaction()
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        fragmentTransaction?.replace(R.id.frameEventsRequiterView, EventRequiterReqitingFragment(), "")
        fragmentTransaction?.commitAllowingStateLoss()
        tabLayout?.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setLisners()
    }



    private fun getHomeFragment(tab: TabLayout.Tab?): Fragment? {
        return when (tab?.position) {
            0 -> EventRequiterReqitingFragment()
           1 -> RequiterDetailsFragment()
            else -> EventRequiterReqitingFragment()
        }
    }

    private fun setLisners() {

    }

    private fun fragmentGoesInThisTab(tab: TabLayout.Tab?) {
        val fragment: Fragment? = getHomeFragment(tab)
        val fragmentTransaction =  (mContext as DashBoardBaseActivity).supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameEventsRequiterView, fragment!!, "")
        fragmentTransaction?.commitAllowingStateLoss()
    }
}