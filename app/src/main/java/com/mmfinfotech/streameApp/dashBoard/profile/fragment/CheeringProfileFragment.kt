package com.mmfinfotech.streameApp.dashBoard.profile.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.FolloweNLikeAdapter
import com.mmfinfotech.streameApp.dashBoard.profile.activity.RankingCharringProfileActivity
import com.mmfinfotech.streameApp.model.HomeLive
import kotlinx.android.synthetic.main.fragment_cheering_total.*

class CheeringProfileFragment : Fragment() {
    private val TAG: String? = CheeringProfileFragment::class.java.simpleName
    private var mContext: Context? = null
    private var arrCheeringProfile: ArrayList<HomeLive?>? = null
    private var adapterFollowNLikeCheeringProfile: FolloweNLikeAdapter? = null
    private var pageNo: Int? = 1
    private var mLayoutManager: LinearLayoutManager? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cheering_total, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrCheeringProfile = ArrayList()
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2020/02/09/neon-balls-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2015/11/19/colorful-candys-wallpaper.jpg"))
        arrCheeringProfile?.add(HomeLive("","","Time 00:00:00000","https://free4kwallpapers.com/uploads/originals/2018/08/01/silly-birbs-wallpaper.jpg"))

//        setLisners()
        setAdapter(arrCheeringProfile)

    }
    private fun setAdapter(arrCheeringProfile:  ArrayList<HomeLive?>?){
        mLayoutManager = recyclerCheering.layoutManager as LinearLayoutManager
        adapterFollowNLikeCheeringProfile =
            FolloweNLikeAdapter(
                mContext,
                arrCheeringProfile,
                RankingCharringProfileActivity.ACTION_Cheering,
                object :
                    FolloweNLikeAdapter.OnFllowLikeListner {
                    override fun onFollower() {

                    }

                    override fun onFollowing() {

                    }

                    override fun onLike(position: Int) {

                    }

                    override fun onAnnonymouseClicking(position: Int) {

                    }

                })
        recyclerCheering.adapter = adapterFollowNLikeCheeringProfile
    }

}