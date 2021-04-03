package com.mmfinfotech.streameApp.dashBoard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.Clips
import java.util.*
import kotlin.collections.ArrayList


class AdapterStremeClips(var mContext: Context?,
                         val arrClips: ArrayList<Clips?>?,
                         val onPostListner: OnPostLisner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1
    private val set = ConstraintSet()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.getContext()
        return when (viewType) {
            LiveData -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_clip, parent, false)
                LiveHomeViewHolder(view)
            }

            else -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrClips?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (arrClips?.size == 0) {
            arrClips?.add(null)
        } else {
            if (arrClips?.contains(null) == true && arrClips?.size > 1) arrClips.remove(
                    null
            )
        }
        return (arrClips?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LiveHomeViewHolder) {
            holder.videoViewThumbnail.layoutParams.height =holder.getRandomIntInRange(500, 350)
            Glide.with(mContext!!)
                    .load(arrClips?.get(position)?.thumb)
                    .placeholder(R.drawable.background_2)
                    .into(holder.videoViewThumbnail)


        } else if (holder is NoDataViewHolder) {
        }

    }

    inner class LiveHomeViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
         val mRandom   =Random()

        var videoViewThumbnail: ImageView = view.findViewById(R.id.videoViewThumbnail)

        init {

            videoViewThumbnail.setOnClickListener {
                onPostListner?.onClick(adapterPosition)
            }
        }
        fun getRandomIntInRange(max: Int, min: Int): Int {
            return mRandom.nextInt(max - min + min) + min
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnPostLisner {
        fun onClick(position: Int)
    }
}