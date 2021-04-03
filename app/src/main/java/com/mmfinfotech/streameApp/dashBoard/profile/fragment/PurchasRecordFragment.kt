package com.mmfinfotech.streameApp.dashBoard.profile.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.profile.activity.MyBabyCoinGiftsActivity
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterPurchaseRecord
import com.mmfinfotech.streameApp.models.HomeLive
import kotlinx.android.synthetic.main.fragment_purchas_record.*


class PurchasRecordFragment : Fragment() {
    private val TAG = PurchasRecordFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrEventLive: ArrayList<HomeLive?>? = null
    private var adapterPurchasOrder: AdapterPurchaseRecord? = null
    private var pageNo: Int? = 1
    val gridLayoutManager = GridLayoutManager(activity, 4)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_purchas_record, container, false)
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

        recyclerPurchaseRecordFrag.layoutManager = gridLayoutManager
        adapterPurchasOrder = AdapterPurchaseRecord(
            mContext,
            arrPurchaseRecord,
            object : AdapterPurchaseRecord.OnPurchaseRecordLisner {
                override fun onClick(position: Int) {
                    startActivity(MyBabyCoinGiftsActivity.getInstance(mContext))
                }
            })

        recyclerPurchaseRecordFrag.adapter = adapterPurchasOrder
    }

    private fun setLisners() {

    }
}