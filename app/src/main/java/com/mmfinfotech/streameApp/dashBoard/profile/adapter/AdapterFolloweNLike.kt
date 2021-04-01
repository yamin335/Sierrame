package com.mmfinfotech.streameApp.dashBoard.profile.adapter

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
import com.mmfinfotech.streameApp.dashBoard.profile.activity.FollowNLikeActivity
import com.mmfinfotech.streameApp.dashBoard.profile.activity.RankingCharringProfileActivity
import com.mmfinfotech.streameApp.model.HomeLive

class FolloweNLikeAdapter(
    val context: Context?,
    private val arrFollowNLike : ArrayList<HomeLive?>?,
    private val action : String?,
    private val onFollowLikeListener : OnFllowLikeListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag: String? = FolloweNLikeAdapter::class.java.simpleName
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
                    .inflate(R.layout.partial_item_profile_event, parent, false)
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
                holder.TextViewName?.text = arrFollowNLike?.get(position)?.Name
                holder.TextViewText?.text = arrFollowNLike?.get(position)?.Time
                 Glide.with(context!!)
                    .load(arrFollowNLike?.get(position)?.Image)
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
        if (arrFollowNLike?.isEmpty() == true) arrFollowNLike.add(null)
        return arrFollowNLike?.size ?: 0
    }

    inner class FollowViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var TextViewText: TextView = view.findViewById(R.id.textViewText)
        var ImageViewCancel: TextView = view.findViewById(R.id.ImageViewCancel)
        var TextFollowTitle: TextView = view.findViewById(R.id.textFollowTitle)
        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)

        init {
            when (action) {
                FollowNLikeActivity.ACTION_FOLLOWER -> {
                    TextFollowTitle.visibility=View.VISIBLE
                    ImageViewCancel.visibility=View.GONE
                    TextFollowTitle.setOnClickListener {
                        onFollowLikeListener?.onFollower()
                    }
                     }
                FollowNLikeActivity.ACTION_FOLLOWING  -> {
                    TextFollowTitle.visibility=View.GONE
                    ImageViewCancel.visibility=View.VISIBLE
                    ImageViewCancel.setOnClickListener {
                        onFollowLikeListener?.onFollowing()
                    }

                    }
                FollowNLikeActivity.ACTION_LIKE -> {
                    TextFollowTitle.visibility=View.GONE
                    ImageViewCancel.visibility=View.VISIBLE
                    ImageViewCancel.setOnClickListener {
                        onFollowLikeListener?.onLike(adapterPosition)
                    }
                }
                RankingCharringProfileActivity.ACTION_Cheering->{
                    TextFollowTitle.visibility=View.GONE
                    ImageViewCancel.visibility=View.VISIBLE
                    ImageViewCancel.setOnClickListener {
                        onFollowLikeListener?.onLike(adapterPosition)
                    }
                }
                FollowNLikeActivity.ACTION_MYCOLLECTION->{
                    TextFollowTitle.visibility=View.GONE
                    ImageViewCancel.visibility=View.VISIBLE
                    ImageViewCancel.setOnClickListener {
                        onFollowLikeListener?.onLike(adapterPosition)
                    }
                }
                FollowNLikeActivity.ACTION_ANNONYMOUSCHARING->{
                    TextFollowTitle.visibility=View.GONE
                    ImageViewCancel.visibility=View.VISIBLE
                    ImageViewCancel.setOnClickListener {
                        onFollowLikeListener?.onAnnonymouseClicking(adapterPosition)
                    }
                }
                else -> {
                    TextFollowTitle.visibility=View.VISIBLE
                    ImageViewCancel.visibility=View.GONE
                }
            }
        }
    }
    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnFllowLikeListner {
        fun onFollower()
        fun onFollowing()
        fun onLike(position: Int)
        fun onAnnonymouseClicking(position: Int)
    }
}