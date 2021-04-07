package com.mmfinfotech.streameApp.dashBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.CommentsChet
import com.mmfinfotech.streameApp.dashBoard.activity.CommentsActivity
import com.mmfinfotech.streameApp.util.getFormattedDate

class AdapterComments(
    val context: Context?,
    private val arrComments: ArrayList<CommentsChet?>?,
    private val onCommentsListner: OnCommentsListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                LiveScheduleViewHolder(view)
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
            is LiveScheduleViewHolder -> {
                holder.imageProfile.animation = AnimationUtils.loadAnimation(context, R.anim.anim)

                if (arrComments?.get(position)?.update_on != null)
                    holder.textTime.text = getFormattedDate(context, arrComments.get(position)?.update_on!!.toLong())
                holder.textViewName.text = arrComments?.get(position)?.user_name
                holder.textViewDesc.text = arrComments?.get(position)?.comment
                Glide.with(context!!)
                    .load(arrComments?.get(position)?.user_profile)
                    .apply(
                        RequestOptions().error(R.drawable.ic_user)
                            .placeholder(R.drawable.ic_user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_user)
                    .into(holder.imageProfile)

                if (arrComments?.get(position)?.owner_id.toString() ==  (context as CommentsActivity).appPreferences?.getUserId (context) ||
                    arrComments?.get(position)?.user_id.toString() == context.appPreferences?.getUserId (context)){
                    holder.imageButtonMessageDelete?.visibility = View.VISIBLE
                }else {
                    holder.imageButtonMessageDelete?.visibility = View.GONE
                }
            }
            is NoDataViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        if (arrComments?.size == 0) {
            arrComments.add(null)
        } else {
            if (arrComments?.contains(null) == true && arrComments.size > 1) arrComments.remove(
                null
            )
        }
        return (arrComments?.size ?: 0)

    }


    inner class LiveScheduleViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView = view.findViewById(R.id.textViewName)
        var textViewDesc: TextView = view.findViewById(R.id.textViewComments)
        var textTime: TextView = view.findViewById(R.id.textTime)
        var imageProfile: ImageView = view.findViewById(R.id.imageViewProfile)
        val imageButtonMessageDelete: ImageButton? = view.findViewById(R.id.imageButtonMessageDelete)

        init {
            imageButtonMessageDelete?.setOnClickListener { onCommentsListner.onDeleteClick(adapterPosition) }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnCommentsListener {
        fun onCommentsClick(p: Int)
        fun onDeleteClick(p: Int)
    }
}