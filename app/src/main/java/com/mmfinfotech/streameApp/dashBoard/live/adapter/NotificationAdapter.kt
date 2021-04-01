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
import com.mmfinfotech.streameApp.model.Notification

class NotificationAdapter(
    val mContext: Context?,
    private val arrNotifications: ArrayList<Notification?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val Data: Int = 0
    val NoData: Int = 1

    override fun getItemViewType(position: Int): Int = if (arrNotifications?.get(position) == null) NoData else Data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Data -> NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_notification, parent, false))
            else -> NoDataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false))
        }
    }

    override fun getItemCount(): Int {
        if (arrNotifications?.size == 0) {
            arrNotifications.add(null)
        } else {
            if (arrNotifications?.contains(null) == true && arrNotifications.size > 1)
                arrNotifications.remove(null)
        }
        return (arrNotifications?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotificationViewHolder) {

            holder.message?.text = arrNotifications?.get(position)?.notification
            holder.date?.text = arrNotifications?.get(position)?.addedOn

            mContext?.let { context ->
                holder.profile?.let { imageView ->
//                    Glide.with(context)
//                        .load(arrNotifications?.get(position)?.senderProfile)
//                        .placeholder(R.drawable.placeholder_profile_circle)
//                        .into(imageView)
                    Glide.with(context)
                        .load(arrNotifications?.get(position)?.senderProfile)
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

    inner class NotificationViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var message: TextView? = view.findViewById(R.id.textViewNotificationMessage)
        var date: TextView? = view.findViewById(R.id.textViewNotificationDate)
        var profile: ImageView? = view.findViewById(R.id.imageViewNotificationProfile)
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)
}