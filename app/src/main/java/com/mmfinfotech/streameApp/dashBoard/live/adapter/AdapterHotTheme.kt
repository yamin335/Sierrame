package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.HotTheme
import com.mmfinfotech.streameApp.utils.AppConstants

class AdapterHotTheme(
    var mContext: Context?,
    val arrHomeLive: ArrayList<HotTheme?>?,
    val onOnHotThemListner: OnHotThemeLisner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.getContext()
        return when (viewType) {
            LiveData -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_streme, parent, false)
                LiveHomeViewHolder(view)
            }

            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrHomeLive?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (arrHomeLive?.size == 0) {
            arrHomeLive?.add(null)
        } else {
            if (arrHomeLive?.contains(null) == true && arrHomeLive?.size > 1) arrHomeLive.remove(
                null
            )
        }
        return (arrHomeLive?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LiveHomeViewHolder) {
            arrHomeLive?.get(position)?.let {_data ->
                holder.txtviewRating?.text = _data.user_score.toString()
                holder.txtSubTitle?.text = _data.hashtags
                holder.textUserName?.text = _data.user_name
                mContext?.let {_context ->
                    holder.ImgBroadcaster?.let {_imageView ->
                        Glide.with(_context)
                            .load(_data.user_profile)
                            .apply(
                                RequestOptions().error(R.drawable.ic_user)
                                    .placeholder(R.drawable.ic_user)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                            )
                            .into(_imageView)
                    }
                }
                holder.constraintLayoutLockScreen?.visibility = if (_data.accessOption == AppConstants.BroadCastAccessOption.Private) View.VISIBLE
                else View.GONE
            }
        } else if (holder is NoDataViewHolder) {

        }
    }

    inner class LiveHomeViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var txtSubTitle: TextView? = view.findViewById(R.id.txtSubTitle)
        var txtviewRating: TextView? = view.findViewById(R.id.txtviewRating)
        var textUserName: TextView? = view.findViewById(R.id.textViewUserName)
        var ImgBroadcaster: ImageView? = view.findViewById(R.id.imgBroadcaster)
        var constraintLayoutLockScreen: ConstraintLayout? = view.findViewById(R.id.constraintLayoutLockScreen)

        init {
            ImgBroadcaster?.let {
                it.setOnClickListener { onOnHotThemListner?.onClick(adapterPosition) }
            }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnHotThemeLisner {
        fun onClick(position: Int)
    }
}