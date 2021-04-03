package com.mmfinfotech.streameApp.dashBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.Coin

class CoinAdapter(
    val mContext: Context?,
    val arrCoin: ArrayList<Coin?>?,
    val onCoinListener: OnCoinListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val NoData: Int = 0
    val Data: Int = 1
    private var checkedPosition = 0

    override fun getItemViewType(position: Int): Int {
        return if (arrCoin?.get(position) == null) NoData else Data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Data -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_coin, parent, false)
                DataViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }


    override fun getItemCount(): Int {
        if (arrCoin?.size == 0) {
            arrCoin.add(null)
        } else {
            if (arrCoin?.contains(null) == true && arrCoin.size > 1) arrCoin.remove(null)
        }
        return (arrCoin?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {

            val coi = arrCoin?.get(position)?.coins.toString()
            val c = coi + "c"
            holder.title.text = c
            holder.price.text = arrCoin?.get(position)?.currency.toString() + " " + arrCoin?.get(position)?.price.toString()

            if (checkedPosition == position)
                holder.parentContainer.background = ContextCompat.getDrawable(mContext!!, R.drawable.background_gradiant_pink_yellow)
            else
                holder.parentContainer.background = ContextCompat.getDrawable(mContext!!, android.R.color.darker_gray)

        } else if (holder is NoDataViewHolder) {

        }
    }

    inner class DataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var parentContainer: ConstraintLayout = view.findViewById(R.id.constraintParentContainerItemRowCoin)
        var title: TextView = view.findViewById(R.id.textViewItemRowCoinTitle)
        var price: TextView = view.findViewById(R.id.textViewItemRowCoinPrice)

        init {
            parentContainer.setOnClickListener {
                checkedPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    fun getSelectedCoin() : Int = checkedPosition

    interface OnCoinListener {
        fun onItemClick()
    }
}