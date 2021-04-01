package com.mmfinfotech.streameApp.dashBoard.live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.MessageBean

/**
 * Created by Somil Rawal on Mon-07-September-2020.
 */
class MessageAdapter(
    val context: Context?,
    private val messageBeanList: ArrayList<MessageBean?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val itemNoData = 0
    private val itemTypeMessage = 1

    override fun getItemViewType(position: Int): Int {
        return if (messageBeanList?.get(position) == null) itemNoData else itemTypeMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemTypeMessage -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.partial_item_row_message, parent, false)
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
                messageBeanList?.get(position)?.let { _bean ->
                    holder.name?.let { textView -> textView.text = _bean.profileName }
                    holder.message?.let { textView -> textView.text = _bean.message }
                    context?.let { context ->
                        holder.imageViewProfile?.let { imageView ->
                            Glide.with(context)
                                .load(_bean.profileImage)
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.drawable.placeholder_profile_circle)
                                        .error(R.drawable.placeholder_profile_circle)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .priority(Priority.HIGH)
                                )
                                .apply(RequestOptions.circleCropTransform())
                                .into(imageView)
                        }
                    }
                }

//                bean?.message?.text?.let { _text ->
//                    val jsonObject: JsonObject? = Gson().fromJson(_text, JsonObject::class.java)
//                    jsonObject?.let { _json ->
//                        val image: String? = getStringFromJson(_json, "image", AppConstants.Defaults.string)
//                        val name: String? = getStringFromJson(_json, "name", AppConstants.Defaults.string)
//                        val message: String? = getStringFromJson(_json, "message", AppConstants.Defaults.string)
//                        holder.message?.let { it.text = message }
//                        holder.name?.let { it.text = name }
//                        context?.let { _context ->
//                            holder.imageViewProfile?.let { _imageView ->
//                                Glide.with(_context)
////                                    .load("https://staging-streame-bucket.s3.amazonaws.com/uploads/5f58832f34e25.jpg")
//                                    .load(image)
//                                    .apply(
//                                        RequestOptions()
//                                            .placeholder(R.drawable.placeholder_profile_circle)
//                                            .error(R.drawable.placeholder_profile_circle)
//                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                            .priority(Priority.HIGH)
//                                    )
//                                    .apply(RequestOptions.circleCropTransform())
//                                    .into(_imageView)
//                            }
//                        }
//                    }
//                }
            }
            is NoDataViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return messageBeanList?.size ?: 0
    }

    inner class MessageViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var message: TextView? = view.findViewById(R.id.textViewMessageMessage)
        var name: TextView? = view.findViewById(R.id.textViewMessageName)
        var imageViewProfile: AppCompatImageView? = view.findViewById(R.id.imageViewMessageProfile)
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)
}