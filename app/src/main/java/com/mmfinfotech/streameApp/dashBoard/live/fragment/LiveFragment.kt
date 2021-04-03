package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.adapter.AdapterHomeLive
import com.mmfinfotech.streameApp.dashBoard.live.activity.EventActivity
import com.mmfinfotech.streameApp.models.HomeLive
import kotlinx.android.synthetic.main.fragment_live.*

class LiveFragment : Fragment() {
    private val TAG = LiveFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrHomeLive: ArrayList<HomeLive?>? = null
    private var adapterHomeLive: AdapterHomeLive? = null
    private var pageNo: Int? = 1
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(inflater : LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrHomeLive = ArrayList()
        arrHomeLive?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrHomeLive?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"))
        arrHomeLive?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"))
        setAdapter(arrHomeLive)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setAdapter(arrHomeLive: ArrayList<HomeLive?>?){
        mLayoutManager = recyclerview_home_frag.layoutManager as LinearLayoutManager
        adapterHomeLive = AdapterHomeLive(
                mContext,
                arrHomeLive,
                object : AdapterHomeLive.OnHomeLiveLisner {
                    override fun onClick(position: Int) {

                        startActivity(
                            EventActivity.getInstance(
                                mContext))

                    }
                })

        recyclerview_home_frag.adapter = adapterHomeLive
    }

}