package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfinfotech.streameApp.R

class AdapterGifSheet (val mContext: Context?,
                       val arrHomeLive: ArrayList<String?>?,
                       val onPurchaseRecordListner: OnPurchaseRecordLisner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LiveData -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_gif_record, parent, false)
                LiveHomeViewHolder(view)
            }

            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrHomeLive?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (arrHomeLive?.size == 0) {
            arrHomeLive?.add(null)
        } else {
            if (arrHomeLive?.contains(null) == true && arrHomeLive?.size > 1) arrHomeLive.remove(
                null
            )
        }
        return (arrHomeLive?.size ?: 0)
    }

    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position: Int) {
        if (holder is LiveHomeViewHolder) {

           holder.TextViewName.text = mContext?.getString(R.string.txt_gifno, position.toString()) //"GifNo $position"
           Glide.with(mContext!!)
              .load(arrHomeLive?.get(position))
              .into(holder.ImageViewGif!!)

       } else if (holder is NoDataViewHolder) {

        }

    }

    inner class LiveHomeViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var ImageViewGif: ImageView=view.findViewById(R.id.imageViewGif)
        init {
            ImageViewGif.setOnClickListener {
                onPurchaseRecordListner?.onClick(adapterPosition)
            }

        }
    }
    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnPurchaseRecordLisner {
        fun onClick(position: Int)
    }
}