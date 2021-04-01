package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterGistsRecord
import com.mmfinfotech.streameApp.dashBoard.profile.fragment.PurchasRecordFragment
import com.mmfinfotech.streameApp.model.HomeLive
import kotlinx.android.synthetic.main.activity_my_baby_coin_gifts.*

class MyBabyCoinGiftsActivity : AppCompatActivity() {
    private val TAG = PurchasRecordFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrGiftRecordList: ArrayList<HomeLive?>? = null
    private var adapterGistsRecord: AdapterGistsRecord? = null
    private var pageNo: Int? = 1
    val gridLayoutManager = GridLayoutManager(this, 3)
    companion object {
        fun getInstance(
            context: Context?
        ) = Intent(context, MyBabyCoinGiftsActivity::class.java).apply {
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_baby_coin_gifts)
        arrGiftRecordList = ArrayList()
        initView()
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
            )
        )
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "gift",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "gift",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "gift",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "gift",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"
            )
        )
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
            )
        )
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"
            )
        )
        arrGiftRecordList?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        setLisners()
        setAdapter(arrGiftRecordList)
    }

    private fun setAdapter(arrPurchaseRecord: ArrayList<HomeLive?>?) {

        recyclerGiftRecord.layoutManager = gridLayoutManager
        adapterGistsRecord = AdapterGistsRecord(
            mContext,
            arrPurchaseRecord,
            object : AdapterGistsRecord.OnPurchaseGistsLisner {
                override fun onClick(position: Int) {
                }
            })
        recyclerGiftRecord.adapter = adapterGistsRecord
    }
    private fun initView() {
    findViewById<ConstraintLayout>(R.id.includeToolbarBabyCoinGift)?.findViewById<TextView>(R.id.textViewTitle)?.text="js,Connos coin"
    findViewById<ConstraintLayout>(R.id.includeToolbarBabyCoinGift)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.setImageDrawable(getDrawable(R.drawable.ic_diamond))
    findViewById<ConstraintLayout>(R.id.includeToolbarBabyCoinGift)?.findViewById<TextView>(R.id.textViewnotification)?.visibility=View.VISIBLE
    findViewById<ConstraintLayout>(R.id.includeToolbarBabyCoinGift)?.findViewById<TextView>(R.id.textViewnotification)?.text="1"
    }

    private fun setLisners() {

    }
}