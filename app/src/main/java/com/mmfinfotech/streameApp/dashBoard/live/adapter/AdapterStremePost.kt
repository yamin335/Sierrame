package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.Post

class AdapterStremePost(var mContext: Context?,
                        val arrPost: ArrayList<Post?>?,
                        val onPostListner: OnPostLisner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.getContext()
        return when (viewType) {
            LiveData -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_post, parent, false)
                LiveHomeViewHolder(view)
            }

            else -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrPost?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (arrPost?.size == 0) {
            arrPost?.add(null)
        } else {
            if (arrPost?.contains(null) == true && arrPost?.size > 1) arrPost.remove(
                    null
            )
        }
        return (arrPost?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LiveHomeViewHolder) {
            Glide.with(mContext!!)
                    .load(arrPost?.get(position)?.thumb)
                    .apply(
                            RequestOptions().error(R.drawable.ic_user)
                                    .placeholder(R.drawable.ic_user)

                    )
                    .into(holder.ImageViewPost!!)

        } else if (holder is NoDataViewHolder) {

        }

    }

    inner class LiveHomeViewHolder constructor(view: View) :
            RecyclerView.ViewHolder(view) {
        var ImageViewPost: ImageView = view.findViewById(R.id.imageViewPost)

        init {
            ImageViewPost.setOnClickListener {
                onPostListner?.onClick(adapterPosition)
            }

        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnPostLisner {
        fun onClick(position: Int)
    }
}