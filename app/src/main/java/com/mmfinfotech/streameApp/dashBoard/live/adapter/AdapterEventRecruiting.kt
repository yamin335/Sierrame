package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.Rank

class AdapterEventRecruiting(
    val mContext: Context?,
    val arrEventRecruiting: ArrayList<Rank?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val eventRecruitingData: Int = 0
    private val eventRecruitingNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            eventRecruitingData -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_profile_event, parent, false)
                EventRecruitingViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrEventRecruiting?.get(position) == null) eventRecruitingNoData else eventRecruitingData
    }

    override fun getItemCount(): Int {
        if (arrEventRecruiting?.size == 0) {
            arrEventRecruiting?.add(null)
        } else {
            if (arrEventRecruiting?.contains(null) == true && arrEventRecruiting?.size > 1) arrEventRecruiting.remove(
                null
            )
        }
        return (arrEventRecruiting?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventRecruitingViewHolder) {
            holder.name?.text = arrEventRecruiting?.get(position)?.username
            holder.coin?.text = arrEventRecruiting?.get(position)?.spentCoins
            if (mContext != null) {
                Glide.with(mContext)
                    .load(arrEventRecruiting?.get(position)?.userProfile)
                    .apply(
                        RequestOptions().error(R.drawable.ic_dmmy_user)
                            .placeholder(R.drawable.ic_dmmy_user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.profile!!)
            }

        } else if (holder is NoDataViewHolder) {
        }
    }

    inner class EventRecruitingViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView? = view.findViewById(R.id.textViewName)
        var coin: TextView? = view.findViewById(R.id.textViewCoin)
        var profile: ImageView? = view.findViewById(R.id.imgProfile)
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)
}