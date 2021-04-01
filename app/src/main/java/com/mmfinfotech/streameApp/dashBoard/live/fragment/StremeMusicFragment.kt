package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterStreameMusicLiving
import com.mmfinfotech.streameApp.model.HomeLive
import kotlinx.android.synthetic.main.fragment_streme_music.*

class StremeMusicFragment : Fragment() {
    private val TAG = HotThemeFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrStremeMusic: ArrayList<HomeLive?>? = null
    private var adapterStremeMusic: AdapterStreameMusicLiving? = null
    private var pageNo: Int? = 1
    val gridLayoutManager = GridLayoutManager(activity, 2)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_streme_music, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrStremeMusic = ArrayList()
        arrStremeMusic?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrStremeMusic?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"))
        arrStremeMusic?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"))
        arrStremeMusic?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrStremeMusic?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"))
        arrStremeMusic?.add(HomeLive("", "", "Time 00:00:00000", "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"))
        setAdapter(arrStremeMusic)
    }
    private fun setAdapter(arrStremeMusic: ArrayList<HomeLive?>?){
        recyclerviewMusic.layoutManager = gridLayoutManager
        adapterStremeMusic = AdapterStreameMusicLiving(
                mContext,
                arrStremeMusic, object : AdapterStreameMusicLiving.OnStreameMusicListner {
            override fun onClick(position: Int) {
            }
        })

        recyclerviewMusic.adapter = adapterStremeMusic
    }

}