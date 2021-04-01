package com.mmfinfotech.streameApp.dashBoard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.HomeLive

class AdapterGistsRecord (val mContext: Context?,
                          val arrGists: ArrayList<HomeLive?>?,
                          val onPurchaseGistsListner: OnPurchaseGistsLisner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LiveData -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_gift_purchase_record, parent, false)
                GiftPurchaseViewHolder(view)
            }

            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrGists?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (arrGists?.size == 0) {
            arrGists?.add(null)
        } else {
            if (arrGists?.contains(null) == true && arrGists?.size > 1) arrGists.remove(
                null
            )
        }
        return (arrGists?.size ?: 0)
    }

    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position: Int) {
        if (holder is GiftPurchaseViewHolder) {

//           holder.TextViewTitle.text = arrGists?.get(position)?.Name
//           holder.TextViewTitle.text = arrGists?.get(position)?.Name
           holder.TextViewTitle.text ="300C"
           holder.TextViewSubtitle.text = "Y120"

            mContext?.let {
                Glide.with(it)
                    .load(arrGists?.get(position)?.Image)
                    .placeholder(R.drawable.ic_user)
                    .into(holder.ImageViewGift!!)
            }

       } else if (holder is NoDataViewHolder) {

        }

    }

    inner class GiftPurchaseViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        var TextViewSubtitle: TextView = view.findViewById(R.id.textViewSubtitle)
        var ImageViewGift: ImageView=view.findViewById(R.id.imageViewGift)
        init {
            ImageViewGift.setOnClickListener {
                onPurchaseGistsListner?.onClick(adapterPosition)
            }

        }
    }
    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnPurchaseGistsLisner {
        fun onClick(position: Int)
    }
}