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
import com.mmfinfotech.streameApp.models.Rank

class LeaderBoardAdapter(
    val mContext: Context?,
    private val arrRank: ArrayList<Rank?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val FirstData: Int = 0
    val Data: Int = 1
    val NoData: Int = 2

    override fun getItemViewType(position: Int): Int = when {
        arrRank?.get(position) == null -> NoData
        position == 0 -> FirstData
        else -> Data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FirstData -> LeaderBoardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_first_leader_board, parent,
                false))
            Data -> LeaderBoardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_leader_board, parent, false))
            else -> NoDataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false))
        }
    }

    override fun getItemCount(): Int {
        if (arrRank?.size == 0) {
            arrRank.add(null)
        } else {
            if (arrRank?.contains(null) == true && arrRank.size > 1)
                arrRank.remove(null)
        }
        return (arrRank?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LeaderBoardViewHolder) {

            holder.position?.text = position.plus(1).toString()
            holder.userName?.text = arrRank?.get(position)?.username
            holder.score?.text = arrRank?.get(position)?.score?.toString()
            holder.coins?.text = arrRank?.get(position)?.spentCoins

            mContext?.let { context ->
                holder.profile?.let { imageView ->
                    Glide.with(context)
                        .load(arrRank?.get(position)?.userProfile)
                        .apply(
                            RequestOptions().error(R.drawable.placeholder_profile_circle)
                                .placeholder(R.drawable.placeholder_profile_circle)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        )
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.placeholder_profile_circle)
                        .into(imageView)
                }
            }
        } else if (holder is NoDataViewHolder) {
        }
    }

    inner class LeaderBoardViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var position: TextView? = view.findViewById(R.id.textViewLeaderBoardPosition)
        var profile: ImageView? = view.findViewById(R.id.imageViewLeaderBoardProfile)
        var userName: TextView? = view.findViewById(R.id.textViewLeaderBoardUserName)
        var score: TextView? = view.findViewById(R.id.textViewLeaderBoardUserScore)
        var coins: TextView? = view.findViewById(R.id.textViewLeaderBoardUserSpentCoins)
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)
}