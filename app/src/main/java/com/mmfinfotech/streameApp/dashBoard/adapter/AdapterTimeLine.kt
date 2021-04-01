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
import com.mmfinfotech.streameApp.model.Post
import com.mmfinfotech.streameApp.util.getFormattedDate

class AdapterTimeLine(
        val context: Context?,
        private val arrLivers: ArrayList<Post?>?,
        private val onTimeLineListner: OnTimeLineListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag: String? = AdapterTimeLine::class.java.simpleName
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
                    .inflate(R.layout.items_timeline, parent, false)
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


                if (arrLivers?.get(position)?.added_on!= null)
                    holder.textViewTime.text = getFormattedDate(context, arrLivers?.get(position)?.update_on!!.toLong())
                if (arrLivers?.get(position)?.like_status.equals("1")) {
                    holder.ImageButtonHeart.setImageResource(R.drawable.ic__fillheart_24);
                } else {
                  holder.ImageButtonHeart.setImageResource(R.drawable.ic_baseline_emptyheart);
                }
                holder.textViewName?.text = arrLivers?.get(position)?.user_name
                holder.textViewTitles?.text = arrLivers?.get(position)?.title
                holder.textViewSubTitle?.text = arrLivers?.get(position)?.description
//                holder.textViewTime?.text = arrLivers?.get(position)?.added_on
                 Glide.with(context!!)
                    .load(arrLivers?.get(position)?.user_profile)
                    .apply(
                        RequestOptions().error(R.drawable.ic_user)
                            .placeholder(R.drawable.ic_user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .apply(RequestOptions.circleCropTransform())
                     .placeholder(R.drawable.ic_user)
                     .into(holder.ImageProfile!!)

                Glide.with(context!!)
                    .load(arrLivers?.get(position)?.file)
                    .apply(
                        RequestOptions().error(R.drawable.background_1)
                            .placeholder(R.drawable.background_1)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                     .placeholder(R.drawable.background_1)
                     .into(holder.imagePost!!)
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
        var textViewTitles: TextView = view.findViewById(R.id.textViewTitles)
        var textViewSubTitle: TextView = view.findViewById(R.id.textViewSubTitle)
        var textViewName: TextView = view.findViewById(R.id.textViewName)
        var textViewTime: TextView = view.findViewById(R.id.textViewTime)
        var textViewMore: TextView = view.findViewById(R.id.textViewMore)
        var ImageProfile: ImageView = view.findViewById(R.id.imageViewProfile)
        var ImageButtonHeart: ImageView = view.findViewById(R.id.ImageButtonHeart)
        var ImageButtonComments: ImageView = view.findViewById(R.id.ImageButtonComments)
        var ImageButtonMenuDots: ImageView = view.findViewById(R.id.ImageButtonMenuDots)
        var ImageButtonShare: ImageView = view.findViewById(R.id.ImageButtonShare)
        var imagePost: ImageView = view.findViewById(R.id.imagePost)

        init {
            textViewMore.setOnClickListener {
                onTimeLineListner.onMoreClickingListner(adapterPosition)
            }
            ImageButtonHeart.setOnClickListener {
                onTimeLineListner.onLikeClickingListner(adapterPosition,ImageButtonHeart)
            }
            ImageButtonComments.setOnClickListener {
                onTimeLineListner.onCommentClickingListner(adapterPosition)
            }
            ImageButtonMenuDots.setOnClickListener {
                onTimeLineListner.onClickTimeLineListner(adapterPosition)
            }
            ImageButtonShare.setOnClickListener {
                onTimeLineListner.onShareClickingListner(adapterPosition)
            }
        }
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnTimeLineListner {
      fun  onClickTimeLineListner(p:Int)
        fun onMoreClickingListner(p:Int)
        fun onCommentClickingListner(p:Int)
        fun onLikeClickingListner(p: Int, ImageButtonHeart: ImageView)
        fun onShareClickingListner(p:Int)

    }
}