package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.live.adapter.AdapterEventRecruiting
import com.mmfinfotech.streameApp.model.HomeLive
import kotlinx.android.synthetic.main.fragment_requiter_requiting.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class EventRequiterReqitingFragment : Fragment() {
    private val TAG = EventRequiterReqitingFragment::class.java.simpleName
    private var mContext : Context? = null
    private var arrEventRecruiting: ArrayList<HomeLive?>? = null
    private var adapterEventRecruiting : AdapterEventRecruiting? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_requiter_requiting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        arrEventRecruiting = ArrayList()
        arrEventRecruiting?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrEventRecruiting?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrEventRecruiting?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )
        arrEventRecruiting?.add(
            HomeLive(
                "",
                "",
                "Time 00:00:00000",
                "https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"
            )
        )


        setLisners()
        setAdapter(arrEventRecruiting)

    }

    private fun setAdapter(arrEventLive: ArrayList<HomeLive?>?) {
        mLayoutManager = recyclerviewEventRequitingFrag.layoutManager as LinearLayoutManager
        adapterEventRecruiting = AdapterEventRecruiting(mContext, ArrayList())


        recyclerviewEventRequitingFrag.adapter = adapterEventRecruiting
    }

    private fun setLisners() {

    }
}