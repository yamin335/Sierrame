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
import com.mmfinfotech.streameApp.models.HomeLive

class AdapterStreameMusicLiving (val mContext: Context?,
                                 val arrStreameMusic: ArrayList<HomeLive?>?,
                                 val onstreameMusicListner: OnStreameMusicListner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LiveData -> {
                val view : View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_streme_music, parent, false)
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
        return if (arrStreameMusic?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (arrStreameMusic?.size == 0) {
            arrStreameMusic?.add(null)
        } else {
            if (arrStreameMusic?.contains(null) == true && arrStreameMusic?.size > 1) arrStreameMusic.remove(
                null
            )
        }
        return (arrStreameMusic?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LiveHomeViewHolder) {

           holder.txtSubTitle.text = arrStreameMusic?.get(position)?.Time

           Glide.with(mContext!!)
               .load(arrStreameMusic?.get(position)?.Image).
               placeholder(R.drawable.ic_user)

               .into(holder.ImgBroadcaster!!)

       } else if (holder is NoDataViewHolder) {

        }

    }

    inner class LiveHomeViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var txtSubTitle: TextView = view.findViewById(R.id.txtSubTitle)
        var ImgBroadcaster: ImageView=view.findViewById(R.id.imgBroadcaster)
        init {
            txtSubTitle.setOnClickListener {
            onstreameMusicListner?.onClick(adapterPosition)
            }

        }
    }
    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnStreameMusicListner {
        fun onClick(position: Int)
    }
}