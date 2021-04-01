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
import com.mmfinfotech.streameApp.model.HomeLive

class AdapterMission(
        val context: Context?,
        private val arrMission: ArrayList<HomeLive?>?,
        private val onMissionListener: OnMissionListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG : String? = AdapterMission::class.java.simpleName
    val itemNoData : Int = 0
    val itemType : Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrMission?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_mission, parent, false)
                MissionViewHolder(view)
            }

            itemNoData -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MissionViewHolder -> {
                holder.TextViewName?.text = arrMission?.get(position)?.Name
                holder.TextViewText?.text = arrMission?.get(position)?.Time
                Glide.with(context!!)
                        .load(arrMission?.get(position)?.Image)
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
        if (arrMission?.isEmpty() == true) arrMission.add(null)
        return arrMission?.size ?: 0
    }


    inner class MissionViewHolder constructor(view: View) :
            RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var TextViewText: TextView = view.findViewById(R.id.textViewText)

        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)

        init {
        }

    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnMissionListner {

    }
}