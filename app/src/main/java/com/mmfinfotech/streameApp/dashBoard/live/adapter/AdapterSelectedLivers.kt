package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.Liver

class AdapterSelectedLivers(
        val context: Context?,
        private val arrReqstLivers: ArrayList<Liver?>?,
        private val onSelectedReqstLiversListener: OnSelectedReqstLiversListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag: String? = AdapterSelectedLivers::class.java.simpleName
    val itemNoData: Int = 0
    val itemType: Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrReqstLivers?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.items_selected_livers, parent, false)
                LiveSchedulViewHolder(view)
            }

            itemNoData -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
            else -> {
                val view : View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LiveSchedulViewHolder -> {
//                holder.ImageProfile.animation=AnimationUtils.loadAnimation(context,R.anim.anim)
                holder.TextViewName?.text = arrReqstLivers?.get(position)?.username
                 Glide.with(context!!)
                    .load(arrReqstLivers?.get(position)?.profile)
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
            if (arrReqstLivers?.size == 0) {
                arrReqstLivers?.add(null)
            } else {
                if (arrReqstLivers?.contains(null) == true && arrReqstLivers?.size > 1) arrReqstLivers.remove(
                        null
                )
            }
            return (arrReqstLivers?.size ?: 0)

    }


    inner class LiveSchedulViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
       var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)
        var imageViewClose: ImageView = view.findViewById(R.id.imageViewClose)
        var layoutItem: ConstraintLayout = view.findViewById(R.id.layoutItem)
        init {
            layoutItem.setOnClickListener {
                onSelectedReqstLiversListener.onReqstLiveListner(
                    adapterPosition)
            }
            imageViewClose.setOnClickListener {
                onSelectedReqstLiversListener.onCancelClicking(
                    adapterPosition)
            }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnSelectedReqstLiversListner {
      fun  onReqstLiveListner(p:Int)
      fun  onCancelClicking(p:Int)

    }
}