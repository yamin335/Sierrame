package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.AutoMessage
import com.mmfinfotech.streameApp.util.listners.OnAutoMessageSelectListner

/**
 * Created by Somil Rawal on Mon-14-September-2020.
 */
class AutoMessageAdapter(
    val context: Context?,
    val arrAutoMessages: ArrayList<AutoMessage?>?,
    val onAutoMessageSelectListner: OnAutoMessageSelectListner?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val itemNoData = 0
    private val itemTypeMessage = 1

    override fun getItemViewType(position: Int): Int {
        return if (arrAutoMessages?.get(position) == null) itemNoData else itemTypeMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemTypeMessage -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_auto_message, parent, false)
                MessageViewHolder(view)
            }
            itemNoData -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_message_no_data, parent, false)
                NoDataViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_message_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                val bean: AutoMessage? = arrAutoMessages?.get(position)
                holder.message?.text = bean?.message
            }
            is NoDataViewHolder -> {
            }
        }
    }


    override fun getItemCount(): Int {
        return arrAutoMessages?.size ?: 0
    }

    inner class MessageViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var message: TextView? = view.findViewById(R.id.textView_message)
        init {
            message?.setOnClickListener { onAutoMessageSelectListner?.onClick(arrAutoMessages?.get(adapterPosition)) }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)
}