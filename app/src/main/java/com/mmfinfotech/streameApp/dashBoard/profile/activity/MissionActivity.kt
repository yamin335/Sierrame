package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterMission
import com.mmfinfotech.streameApp.models.HomeLive
import kotlinx.android.synthetic.main.activity_mission.*

class MissionActivity : AppCompatActivity() {
    private val Tag : String? = LiveScaduleActivity::class.java.simpleName
    private var mLayoutManager: LinearLayoutManager? = null
    private var arrMission: ArrayList<HomeLive?>? = null
    private var adapterMission: AdapterMission? = null

    companion object {
        fun getInstance(context : Context?) = Intent(context, MissionActivity :: class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission)

        arrMission = ArrayList()
        arrMission?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                )
        )
        arrMission?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
                )
        )
        setAdapter(arrMission)
    }
    private fun setAdapter(arrHomeLive: ArrayList<HomeLive?>?){
        mLayoutManager = recyclerMission.layoutManager as LinearLayoutManager
        adapterMission = AdapterMission(
                this@MissionActivity,
                arrHomeLive,
                object : AdapterMission.OnMissionListner {
                })

        recyclerMission.adapter = adapterMission
    }

    private fun setLisners() {}
}