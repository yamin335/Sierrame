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
import com.mmfinfotech.streameApp.models.HomeLive

class AdapterComments(val mContext: Context?,
                      val arrEventRecruiting: ArrayList<HomeLive?>?,
                      val onCommentsListner: OnCommentsLisner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val EventRecruitingData: Int = 0
    val EventRecruitingNoData: Int = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EventRecruitingData -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_comments, parent, false)
                EventRecruitingViewHolder(view)
            }

            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrEventRecruiting?.get(position) == null) EventRecruitingNoData else EventRecruitingData
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
//            holder.txtSubTitle.text = arrEventRecruiting?.get(position)?.Time
            if (mContext != null) {
                Glide.with(mContext)
                    .load(arrEventRecruiting?.get(position)?.Image)
                    .apply(
                        RequestOptions().error(R.drawable.ic_user)
                            .placeholder(R.drawable.ic_user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_user)
                    .into(holder.ImgProfile!!)
            }

    } else if (holder is NoDataViewHolder) {
     }
    }

    inner class EventRecruitingViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var ImgProfile: ImageView =view.findViewById(R.id.imgProfile)
        init {
            TextViewName.setOnClickListener {
                onCommentsListner?.onClick(adapterPosition)
            }
        }
    }
    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnCommentsLisner {
        fun onClick(position: Int)
    }
}