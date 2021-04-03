package com.mmfinfotech.streameApp.dashBoard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.BabyCoin

class MyBabyCoinAdapter(
    val context: Context?,
//    val arrData: ArrayList<BabyCoin?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LiveData: Int = 0
    val LiveNoData: Int = 1
    private var showProfile: Boolean? = true
    private var array: ArrayList<BabyCoin?>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LiveData -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_my_baby_coin, parent, false)
                BabyCoinViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (array?.get(position) == null) LiveNoData else LiveData
    }

    override fun getItemCount(): Int {
        if (array?.size == 0) {
            array?.add(null)
        } else {
            if (array?.contains(null) == true && (array?.size ?: 0) > 1)
                array?.remove(null)
        }
        return (array?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BabyCoinViewHolder) {

            val item = array?.get(position)

//            holder.name?.text = if (item?.planId == 0) "${item.senderName} gifted you coins" else item?.senderName
            holder.name?.text = if (item?.planId == 0)  context?.getString(R.string.gifted_you_coins, item.senderName) else item?.senderName
            holder.date?.text = item?.addedOn
            holder.coinCount?.text = item?.coins?.toString()

            if (showProfile == false) holder.profile?.visibility = View.GONE
            else holder.profile?.visibility = View.VISIBLE


            context?.let { context ->
                holder.profile?.let { imageView ->
                    Glide.with(context)
                        .load(item?.senderImage)
                        .placeholder(R.drawable.ic_dmmy_user)
                        .apply(
                            RequestOptions().error(R.drawable.ic_dmmy_user)
                                .placeholder(R.drawable.ic_dmmy_user)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        )
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView)
                }
            }

        } else if (holder is NoDataViewHolder) {

        }
    }

    inner class BabyCoinViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView? = view.findViewById(R.id.textViewItemRowCheeringLiveName)
        var date: TextView? = view.findViewById(R.id.textViewItemRowCheeringLiveDate)
        var coinCount: TextView? = view.findViewById(R.id.textViewItemRowCheeringLiveCount)
        var profile: ImageView? = view.findViewById(R.id.imageViewItemRowCheeringLiveProfile)
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    fun setProfileVisibility(visible: Boolean?) {
        this.showProfile = visible
        notifyDataSetChanged()
    }

    fun setData(arrData: ArrayList<BabyCoin?>?) {
        this.array?.clear()
        this.array?.addAll(arrData ?: ArrayList())
        notifyDataSetChanged()
    }
}