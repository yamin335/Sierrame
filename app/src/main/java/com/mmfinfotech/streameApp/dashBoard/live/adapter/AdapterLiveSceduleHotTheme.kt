package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.LiveHotThemeSchedule
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterLiveSceduleHotTheme(
        val context: Context?,
        private val arrliveschedule : ArrayList<LiveHotThemeSchedule?>?,
        private val onLiveListListener : OnLiveListListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag : String? = AdapterLiveSceduleHotTheme::class.java.simpleName
    val itemNoData : Int = 0
    val itemType : Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrliveschedule?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_hottheme_liveschedule, parent, false)
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
               holder.TextViewDay?.text = arrliveschedule?.get(position)?.day
                val h_mm_a = SimpleDateFormat("h:mm a")
                val hh_mm_ss = SimpleDateFormat("HH:mm")
                try {
                    val Time: Date = h_mm_a.parse(arrliveschedule?.get(position)?.time)
                    holder.TextViewTime?.text=hh_mm_ss.format(Time)
                    System.out.println(hh_mm_ss.format(Time))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                holder.TextViewDate?.text = arrliveschedule?.get(position)?.date
            }
            is NoDataViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        if (arrliveschedule?.size == 0) {
            arrliveschedule?.add(null)
        } else {
            if (arrliveschedule?.contains(null) == true && arrliveschedule?.size > 1) arrliveschedule.remove(
                    null
            )
        }
        return (arrliveschedule?.size ?: 0)

    }
    inner class LiveListViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewDay: TextView = view.findViewById(R.id.textViewday)
        var TextViewTime: TextView = view.findViewById(R.id.textViewTime)
        var TextViewDate: TextView = view.findViewById(R.id.textViewDate)

        init {
        }

    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnLiveListListner {

    }
}