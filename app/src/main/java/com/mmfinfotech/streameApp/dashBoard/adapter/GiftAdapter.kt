package com.mmfinfotech.streameApp.dashBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.Gift

class GiftAdapter(
    val mContext: Context?,
    val arrGifts: ArrayList<Gift?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val NoData: Int = 0
    val Data: Int = 1

    override fun getItemViewType(position: Int): Int {
        return if (arrGifts?.get(position) == null) NoData else Data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Data -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_gift, parent, false)
                DataViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }


    override fun getItemCount(): Int {
        if (arrGifts?.size == 0) {
            arrGifts.add(null)
        } else {
            if (arrGifts?.contains(null) == true && arrGifts.size > 1) arrGifts.remove(null)
        }
        return (arrGifts?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {

            holder.name.text = arrGifts?.get(position)?.title

            mContext?.let {
                Glide.with(it)
                    .load(arrGifts?.get(position)?.image)
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.imageView)
            }

        } else if (holder is NoDataViewHolder) {

        }
    }

    inner class DataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.textViewItemRowGiftName)
        var imageView: ImageView = view.findViewById(R.id.imageViewItemRowGift)
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface onGiftListner {
        fun onItemClick()
    }
}