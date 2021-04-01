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
import com.mmfinfotech.streameApp.model.LiveSChedule

class AdapterLiveSchadule(
        val context: Context?,
        private val arrLiveSchadule: ArrayList<LiveSChedule?>?,
        private val onLiveSchaduleListener: OnLiveSchaduleListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag: String? = AdapterLiveSchadule::class.java.simpleName
    val itemNoData: Int = 0
    val itemType: Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrLiveSchadule?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_live_scadule_event, parent, false)
                LiveSchedulViewHolder(view)
            }

            itemNoData -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
            else -> {
                val view : View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LiveSchedulViewHolder -> {
                holder.TextViewName?.text = arrLiveSchadule?.get(position)?.name
                holder.TextViewTime?.text = arrLiveSchadule?.get(position)?.time
                 Glide.with(context!!)
                    .load(arrLiveSchadule?.get(position)?.profile)
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
            if (arrLiveSchadule?.size == 0) {
                arrLiveSchadule?.add(null)
            } else {
                if (arrLiveSchadule?.contains(null) == true && arrLiveSchadule?.size > 1) arrLiveSchadule.remove(
                        null
                )
            }
            return (arrLiveSchadule?.size ?: 0)

    }


    inner class LiveSchedulViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var TextViewTime: TextView = view.findViewById(R.id.textViewTime)
        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)
        init {
            ImageProfile.setOnClickListener {
                onLiveSchaduleListener.liveProfileListner(adapterPosition)
            }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnLiveSchaduleListner {
      fun  liveProfileListner(p:Int)

    }
}