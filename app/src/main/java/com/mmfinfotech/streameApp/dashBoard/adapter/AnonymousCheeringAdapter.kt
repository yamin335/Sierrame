package com.mmfinfotech.streameApp.dashBoard.adapter

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
import com.mmfinfotech.streameApp.models.AnonymousCheering

class AnonymousCheeringAdapter(
    val mContext: Context?,
    val arrAnonymousCheering: ArrayList<AnonymousCheering?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val eventRecruitingData: Int = 0
    private val eventRecruitingNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            eventRecruitingData -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_anonymous_cheering, parent, false)
                EventRecruitingViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrAnonymousCheering?.get(position) == null) eventRecruitingNoData else eventRecruitingData
    }

    override fun getItemCount(): Int {
        if (arrAnonymousCheering?.size == 0) {
            arrAnonymousCheering?.add(null)
        } else {
            if (arrAnonymousCheering?.contains(null) == true && arrAnonymousCheering?.size > 1) arrAnonymousCheering.remove(
                null
            )
        }
        return (arrAnonymousCheering?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventRecruitingViewHolder) {
            holder.name?.text = arrAnonymousCheering?.get(position)?.username
            holder.coin?.text = arrAnonymousCheering?.get(position)?.spentCoins
            if (mContext != null) {
                Glide.with(mContext)
                    .load(arrAnonymousCheering?.get(position)?.userProfile)
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
        var name: TextView? = view.findViewById(R.id.textViewItemRowAnonymousCheeringName)
        var coin: TextView? = view.findViewById(R.id.textViewItemRowAnonymousCheeringCoinCount)
        var profile: ImageView? = view.findViewById(R.id.imageViewItemRowAnonymousCheeringProfile)
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)
}