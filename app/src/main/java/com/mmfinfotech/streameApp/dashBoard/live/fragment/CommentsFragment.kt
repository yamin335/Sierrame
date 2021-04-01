package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterComments
import com.mmfinfotech.streameApp.model.HomeLive
import kotlinx.android.synthetic.main.fragment_comments.*


class CommentsFragment : Fragment() {
    private val TAG = CommentsFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrComments: ArrayList<HomeLive?>? = null
    private var adapterComments: AdapterComments? = null
    private var pageNo: Int? = 1
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrComments = ArrayList()
        arrComments?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrComments?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"))
        arrComments?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"))
        arrComments?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrComments?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"))
        arrComments?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"))

        setAdapter(arrComments)
    }

    private fun setAdapter(arrHomeLive: ArrayList<HomeLive?>?){
        mLayoutManager = recyclerComment.layoutManager as LinearLayoutManager
        adapterComments = AdapterComments(
                mContext,
                arrHomeLive,
                object : AdapterComments.OnCommentsLisner{
                    override fun onClick(position: Int) {

                    }
                })

        recyclerComment.adapter = adapterComments
    }

}