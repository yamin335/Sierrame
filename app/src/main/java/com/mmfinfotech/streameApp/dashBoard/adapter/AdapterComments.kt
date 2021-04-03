package com.mmfinfotech.streameApp.dashBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.CommentsChet
import com.mmfinfotech.streameApp.util.getFormattedDate

class AdapterComments(
        val context: Context?,
        private val arrComments: ArrayList<CommentsChet?>?,
        private val onCommentsLisner: OnCommentsListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag: String? = AdapterComments::class.java.simpleName
    val itemNoData: Int = 0
    val itemType: Int = 1

    override fun getItemViewType(position: Int): Int {
        return when {
            arrComments?.get(position) == null -> itemNoData
            else -> itemType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.items_comments, parent, false)
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

                if (arrComments?.get(position)?.update_on!= null)
                    holder.textTime.text = getFormattedDate(context, arrComments?.get(position)?.update_on!!.toLong())
                holder.TextViewName?.text =arrComments?.get(position)?.user_name
                holder.textViewDesc?.text =arrComments?.get(position)?.comment
                  Glide.with(context!!)
                    .load(arrComments?.get(position)?.user_profile)
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
            if (arrComments?.size == 0) {
                arrComments?.add(null)
            } else {
                if (arrComments?.contains(null) == true && arrComments?.size > 1) arrComments.remove(
                        null
                )
            }
            return (arrComments?.size ?: 0)

    }


    inner class LiveSchedulViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var TextViewName: TextView = view.findViewById(R.id.textViewName)
        var textViewDesc: TextView = view.findViewById(R.id.textViewComments)
        var textTime: TextView = view.findViewById(R.id.textTime)
        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)


    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnCommentsListner {
      fun  onCommentsClick(p:Int)

    }
}