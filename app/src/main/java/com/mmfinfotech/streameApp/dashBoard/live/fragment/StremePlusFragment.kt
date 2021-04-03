package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.live.activity.RecomendationActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterEventLiving
import com.mmfinfotech.streameApp.models.HomeLive
import kotlinx.android.synthetic.main.fragment_streme_plus.*

class StremePlusFragment : Fragment() {
    private val TAG = StremePlusFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrEventLive: ArrayList<HomeLive?>? = null
    private var adapterEventLiving : AdapterEventLiving? = null
    private var pageNo: Int? = 1
    val gridLayoutManager = GridLayoutManager(activity, 2)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_streme_plus, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        arrEventLive = ArrayList()
        arrEventLive?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
                )
        )
        arrEventLive?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"
                )
        )
        arrEventLive?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                )
        )

        setLisners()
        setAdapter(arrEventLive)

    }

    private fun setLisners() {
        imageViewCard.setOnClickListener {
            startActivity(
                    RecomendationActivity.getInstance(
                            mContext))
        }
    }

    private fun setAdapter(arrEventLive: ArrayList<HomeLive?>?) {
        recyclerviewStreaming.layoutManager = gridLayoutManager
        adapterEventLiving = AdapterEventLiving(
                mContext,
                arrEventLive,
                object : AdapterEventLiving.OnEventLiveLisner {
                    override fun onClick(position: Int) {
                    }
                })

        recyclerviewStreaming.adapter = adapterEventLiving
    }

}