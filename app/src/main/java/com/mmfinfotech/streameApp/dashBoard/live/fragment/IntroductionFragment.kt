package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterRecomended
import com.mmfinfotech.streameApp.models.HomeLive
import kotlinx.android.synthetic.main.fragment_introduction.*

class IntroductionFragment : Fragment() {

    private val TAG = IntroductionFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrIntroduction: ArrayList<HomeLive?>? = null
    private var adapterIntroduction: AdapterRecomended? = null
    private var pageNo: Int? = 1

    val gridLayoutManager = GridLayoutManager(context, 1,GridLayoutManager.HORIZONTAL, false)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }
    override fun onCreateView(inflater :  LayoutInflater , container : ViewGroup?,
                              savedInstanceState : Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_introduction, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrIntroduction = ArrayList()
        arrIntroduction?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                ))
           arrIntroduction?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                ))
        arrIntroduction?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                )
        )
         arrIntroduction?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                )
        )
         arrIntroduction?.add(
                HomeLive(
                        "",
                        "",
                        "Time 00:00:00000",
                        "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"
                )
        )
        setLisners()
        setAdapter(arrIntroduction)


    }

    private fun setAdapter(arrIntroduction: ArrayList<HomeLive?>?) {
     recyclerIntroduction.layoutManager=gridLayoutManager
        adapterIntroduction = AdapterRecomended(
                mContext,
                arrIntroduction,
                object : AdapterRecomended.OnRecomendedLisner {
                    override fun onClick(position: Int) {
                    }
                })

        recyclerIntroduction.adapter = adapterIntroduction
    }

    private fun setLisners() {

    }
}