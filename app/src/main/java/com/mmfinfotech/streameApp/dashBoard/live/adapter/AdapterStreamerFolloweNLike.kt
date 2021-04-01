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
import com.mmfinfotech.streameApp.dashBoard.live.activity.StremerFollowNLikeActivity
import com.mmfinfotech.streameApp.model.Following

class AdapterStreamerFolloweNLike(
    val context: Context?,
    private val arrFollowNLike : ArrayList<Following?>?,
    private val action : String?,
    private val onFollowLikeListener : OnFllowLikeListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG: String? = AdapterStreamerFolloweNLike::class.java.simpleName
    val itemNoData: Int = 0
    val itemType: Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrFollowNLike?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_follow_n_like_event, parent, false)
                FollowViewHolder(view)
            }

            itemNoData -> {
                 val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FollowViewHolder -> {
                holder.TextViewName?.text = arrFollowNLike?.get(position)?.name
                holder.TextViewDesc?.text = arrFollowNLike?.get(position)?.profile_status
                 Glide.with(context!!)
                    .load(arrFollowNLike?.get(position)?.profile)
                    .apply(
                        RequestOptions().error(R.drawable.ic_user)
                            .placeholder(R.drawable.ic_user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_user)
                    .into(holder.ImageProfile!!)
            }
            is NoDataViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        if (arrFollowNLike?.size == 0) {
            arrFollowNLike?.add(null)
        } else {
            if (arrFollowNLike?.contains(null) == true && arrFollowNLike?.size > 1) arrFollowNLike.remove(
                    null
            )
        }
        return (arrFollowNLike?.size ?: 0)
    }

    inner class FollowViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var TextViewDesc: TextView = view.findViewById(R.id.textViewDesc)
        var ImageViewCancel: TextView = view.findViewById(R.id.ImageViewCancel)
        var TextFollowTitle: TextView = view.findViewById(R.id.textFollowTitle)
        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)
        var layoutItem: ConstraintLayout = view.findViewById(R.id.layoutItem)

        init {
            when (action) {
                StremerFollowNLikeActivity.ACTION_Stremer_FOLLOWER -> {
                    TextFollowTitle.visibility=View.VISIBLE
                    ImageViewCancel.visibility=View.GONE
                    TextFollowTitle.setOnClickListener {
                        onFollowLikeListener?.onFollower(adapterPosition,TextFollowTitle)
                    }
                     }
                StremerFollowNLikeActivity.ACTION_Stremer_FOLLOWING  -> {
                    TextFollowTitle.visibility=View.GONE
                    ImageViewCancel.visibility=View.GONE
                    ImageViewCancel.setOnClickListener {
                        onFollowLikeListener?.onFollowing(adapterPosition,TextFollowTitle)
                    }
                    }
                StremerFollowNLikeActivity.ACTION_Stremer_LIKE -> {
                    TextFollowTitle.visibility=View.GONE
                    ImageViewCancel.visibility=View.VISIBLE
                    ImageViewCancel.setOnClickListener {
                        onFollowLikeListener?.onLike(adapterPosition)
                    }
                }

                else -> {
                    TextFollowTitle.visibility=View.VISIBLE
                    ImageViewCancel.visibility=View.GONE
                }
            }

            layoutItem.setOnClickListener {
                onFollowLikeListener.onProfileClick(adapterPosition)
            }
        }
    }
    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnFllowLikeListner {
        fun onFollower(position: Int, textFollowTitle: TextView)
        fun onFollowing(position: Int,textFollowTitle: TextView)

        fun onLike(position: Int)
        fun onProfileClick(position: Int)
    }
}