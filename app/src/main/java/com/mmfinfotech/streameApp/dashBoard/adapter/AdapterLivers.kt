package com.mmfinfotech.streameApp.dashBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.Liver

class AdapterLivers(
        val context: Context?,
        private val arrLivers: ArrayList<Liver?>?,
        private val onLiversListener: OnSearchLiversListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag: String? = AdapterLivers::class.java.simpleName
    val itemNoData: Int = 0
    val itemType: Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrLivers?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.items_livers, parent, false)
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
                holder.ImageProfile.animation=AnimationUtils.loadAnimation(context,R.anim.anim)
                holder.TextViewName?.text = arrLivers?.get(position)?.name
                holder.textViewDesc?.text = arrLivers?.get(position)?.my_status
                 Glide.with(context!!)
                    .load(arrLivers?.get(position)?.profile)
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
            if (arrLivers?.size == 0) {
                arrLivers?.add(null)
            } else {
                if (arrLivers?.contains(null) == true && arrLivers?.size > 1) arrLivers.remove(
                        null
                )
            }
            return (arrLivers?.size ?: 0)

    }


    inner class LiveSchedulViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var textViewDesc: TextView = view.findViewById(R.id.textViewDesc)
        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)
        var layoutItem: ConstraintLayout = view.findViewById(R.id.layoutItem)
        init {
            layoutItem.setOnClickListener {
                onLiversListener.liveProfileListner(adapterPosition)
            }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnSearchLiversListner {
      fun  liveProfileListner(p:Int)

    }
}