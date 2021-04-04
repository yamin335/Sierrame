package com.mmfinfotech.streameApp.dashBoard.streme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.LiveStreamCategory

class AdapterLiveList(
    val context: Context?,
    private val arrlive : ArrayList<LiveStreamCategory?>?,
    private val onLiveListListener : OnLiveListListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag : String? = AdapterLiveList::class.java.simpleName
    val itemNoData : Int = 0
    val itemType : Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrlive?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
                LiveListViewHolder(view)
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
            is LiveListViewHolder -> {
               holder.TextViewName?.text = arrlive?.get(position)?.name
            }
            is NoDataViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        if (arrlive?.isEmpty() == true) arrlive.add(null)
        return arrlive?.size ?: 0
    }


    inner class LiveListViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)

        init {
            TextViewName.setOnClickListener {
                onLiveListListener.onitemClickListners(adapterPosition)
            }
        }

    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnLiveListListner {
        fun onitemClickListners(p:Int?)
    }
}