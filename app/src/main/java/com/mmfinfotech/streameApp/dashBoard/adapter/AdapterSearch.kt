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
import com.mmfinfotech.streameApp.model.Clips

class AdapterSearch (val mContext: Context?,
                     val arrSearch: ArrayList<Clips?>?,
                     val onSearchListner: OnSearchLisner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LiveData -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row_search, parent, false)
                SearchViewHolder(view)
            }

            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrSearch?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (arrSearch?.size == 0) {
            arrSearch?.add(null)
        } else {
            if (arrSearch?.contains(null) == true && arrSearch?.size > 1) arrSearch.remove(
                null
            )
        }
        return (arrSearch?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchViewHolder) {

            holder.TextViewName.text = arrSearch?.get(position)?.user_name

            Glide.with(mContext!!)
                .load(arrSearch?.get(position)?.thumb)
                .placeholder(R.drawable.background_2)
                .into(holder.ImageViewThumbnail!!)

            Glide.with(mContext!!)
                .load(arrSearch?.get(position)?.user_profile)
                .placeholder(R.drawable.background_1)
                .apply(
                    RequestOptions().error(R.drawable.placeholder_profile)
                        .placeholder(R.drawable.placeholder_profile)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageViewUser!!)

        } else if (holder is NoDataViewHolder) {

        }
    }

    inner class SearchViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var imageViewUser: ImageView=view.findViewById(R.id.imageViewUser)
        var ImageViewThumbnail: ImageView=view.findViewById(R.id.imageViewThumbnail)
        var ImageViewPlay: ImageView=view.findViewById(R.id.imageViewPlay)
        init {
            ImageViewThumbnail.setOnClickListener {
            onSearchListner?.onClick(adapterPosition)
            }
        }
    }
    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnSearchLisner {
        fun onClick(position: Int)
    }
}