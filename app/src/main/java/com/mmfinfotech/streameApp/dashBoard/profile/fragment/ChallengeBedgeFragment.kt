package com.mmfinfotech.streameApp.dashBoard.profile.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterSetting
import com.mmfinfotech.streameApp.model.HomeLive
import kotlinx.android.synthetic.main.fragment_setting.*


class ChallengeBedgeFragment : Fragment() {
    private val TAG = ChallengeBedgeFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrEventLive: ArrayList<HomeLive?>? = null
    private var adapterSetting: AdapterSetting? = null
    private var pageNo: Int? = 1
    var mLinearLayoutManager : LinearLayoutManager? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrEventLive = ArrayList()
        arrEventLive?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
            )
        )
        arrEventLive?.add(
            HomeLive(
                "",
                "gift",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrEventLive?.add(
            HomeLive(
                "",
                "gift",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrEventLive?.add(
            HomeLive(
                "",
                "gift",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrEventLive?.add(
            HomeLive(
                "",
                "gift",
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
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        setLisners()
        setAdapter(arrEventLive)


    }

    private fun setAdapter(arrPurchaseRecord: ArrayList<HomeLive?>?) {
        mLinearLayoutManager  = recyclerSetting.layoutManager as LinearLayoutManager

//        recyclerSetting.layoutManager = gridLayoutManager
        adapterSetting = AdapterSetting(
            mContext,
            arrPurchaseRecord,
            object : AdapterSetting.OnSettingListner {

            })

        recyclerSetting.adapter = adapterSetting
    }

    private fun setLisners() {

    }
}