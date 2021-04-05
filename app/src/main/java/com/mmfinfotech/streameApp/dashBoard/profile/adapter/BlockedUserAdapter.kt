package com.mmfinfotech.streameApp.dashBoard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.BlockedUser
import com.mmfinfotech.streameApp.model.Liver

class BlockedUserAdapter(
    val context: Context?,
    private val arrReqstLivers: ArrayList<BlockedUser?>?,
    private val onBlockedUserListener: OnBlockedUserListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val itemNoData: Int = 0
    val itemType: Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrReqstLivers?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.items_blocked_user, parent, false)
                LiveSchedulViewHolder(view)
            }
            itemNoData -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LiveSchedulViewHolder -> {

                holder.TextViewName?.text = arrReqstLivers?.get(position)?.blockedUserName
                holder.textViewComments?.text = arrReqstLivers?.get(position)?.blockedUserProfileStatus
                Glide.with(context!!)
                    .load(arrReqstLivers?.get(position)?.blockedUserProfile)
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
        if (arrReqstLivers?.size == 0) {
            arrReqstLivers?.add(null)
        } else {
            if (arrReqstLivers?.contains(null) == true && arrReqstLivers?.size > 1) arrReqstLivers.remove(
                null
            )
        }
        return (arrReqstLivers?.size ?: 0)
    }


    inner class LiveSchedulViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var textViewComments: TextView = view.findViewById(R.id.textViewComments)
        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)
        var layoutItem: ConstraintLayout = view.findViewById(R.id.layoutItem)
        var buttonUnBlock: Button = view.findViewById(R.id.buttonUnBlock)

        init {
            buttonUnBlock.setOnClickListener { onBlockedUserListener.onUnBlockUser(adapterPosition) }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnBlockedUserListener {
        fun onUnBlockUser(p: Int)
    }
}