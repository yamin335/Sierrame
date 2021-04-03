package com.mmfinfotech.streameApp.dashBoard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.HomeLive

class AdapterSetting(
        val context: Context?,
        private val arrSetting: ArrayList<HomeLive?>?,
        private val onSettingListener: OnSettingListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val tag: String? = AdapterSetting::class.java.simpleName
    val itemNoData: Int = 0
    val itemType: Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrSetting?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_setting, parent, false)
                FollowViewHolder(view)
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
            is FollowViewHolder -> {
                holder.TextViewName?.text = arrSetting?.get(position)?.Name
                holder.TextViewText?.text = arrSetting?.get(position)?.Time
//                holder.TextViewLike?.text = arrSetting?.get(position)?.Time

            }
            is NoDataViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        if (arrSetting?.isEmpty() == true) arrSetting.add(null)
        return arrSetting?.size ?: 0
    }


    inner class FollowViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var TextViewText: TextView = view.findViewById(R.id.textViewText)
        var TextViewLike: TextView = view.findViewById(R.id.textViewLike)

        init {
        }

    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnSettingListner {

    }
}