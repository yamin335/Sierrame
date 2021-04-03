package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.LiveUserConnected

class AdapterLiveUsersConnected(var mContext: Context?,
                                val arrLiveUserConnected: ArrayList<LiveUserConnected?>?,
                                val onLiveUsersListner: OnLiveUsersLisner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.getContext()
        return when (viewType) {
            LiveData -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user_concected, parent, false)
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
        return if (arrLiveUserConnected?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (arrLiveUserConnected?.size == 0) {
            arrLiveUserConnected?.add(null)
        } else {
            if (arrLiveUserConnected?.contains(null) == true && arrLiveUserConnected?.size > 1) arrLiveUserConnected.remove(
                    null
            )
        }
        return (arrLiveUserConnected?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LiveHomeViewHolder) {


            Glide.with(mContext!!)
                    .load(arrLiveUserConnected?.get(position)?.user_profile)
                    .apply(
                            RequestOptions().error(R.drawable.ic_user)
                                    .placeholder(R.drawable.ic_user)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .circleCrop()
                    .into(holder.ImgUserprofile!!)

        } else if (holder is NoDataViewHolder) {

        }

    }

    inner class LiveHomeViewHolder constructor(view: View) :
            RecyclerView.ViewHolder(view) {

        var ImgUserprofile: ImageView = view.findViewById(R.id.userprofile)

        init {
            ImgUserprofile.setOnClickListener {
                onLiveUsersListner?.onClick(adapterPosition)
            }

        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnLiveUsersLisner {
        fun onClick(position: Int)
    }
}