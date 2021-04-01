package com.mmfinfotech.streameApp.dashBoard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.model.Schadule
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import retrofit2.Call
import java.lang.Exception
import java.util.HashMap

class AdapterSchedule(
        val context: Context?,
        private val arrSchadule: ArrayList<Schadule?>?,
        private val onScheduleListener: OnScheduleListners?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag: String? = AdapterSchedule::class.java.simpleName
    val itemNoData: Int = 0
    val itemType: Int = 1

    override fun getItemViewType(position: Int): Int {
        return if (arrSchadule?.get(position) == null) itemNoData else itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemType -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_schedule, parent, false)
                ArmyViewHolder(view)
            }

            itemNoData -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_row_no_data, parent, false)
                NoDataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArmyViewHolder -> {
                holder.TextViewDate?.text = arrSchadule?.get(position)?.date_time

                holder.ImageViewCross.setOnClickListener {
                    try {
                        if (arrSchadule?.get(position)?.staticMember == true) {
                            arrSchadule?.remove(arrSchadule?.get(position))
                            notifyDataSetChanged()
                        } else {
                            callRemoveSchedule(arrSchadule?.get(position)?.id, position)
                        }
                    } catch (e: Exception) {
                    }
                }
            }
            is NoDataViewHolder -> {

            }
        }
    }

    private fun callRemoveSchedule(id: String?, position: Int) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"
        val apiService: MyApiEndpointInterface? = ApiClient(context).getClient()?.create(MyApiEndpointInterface::class.java)
        val callScheduleRemove: Call<JsonObject?>? = apiService?.callScheduleRemove(headers, id)
        (context as DashBoardActivity).callApi(true, callScheduleRemove, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
//                        Toast.makeText(context, "${msg}", Toast.LENGTH_LONG).show()
                        arrSchadule?.remove(arrSchadule?.get(position))
                        notifyDataSetChanged()
//                        ShowAlertInformation(context,msg)
                    }
                    NotFound -> {
                        val msg = getStringFromJson(mainObject, message, AppConstants.Defaults.string)
                        Toast.makeText(context, "${msg}", Toast.LENGTH_LONG).show()
//                        ShowAlertInformation(context,msg)
                    }
                    NotVerify -> {
//                        appPreferences?.setEmail(this@EditProfileActivity,SocialDetail.socialemail)
                    }
                    else -> {
                    }
                }
                if (context.dialog?.isShowing == true)
                    context.dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }


    override fun getItemCount(): Int {
        if (arrSchadule?.size == 0) {
            arrSchadule?.add(null)
        } else {
            if (arrSchadule?.contains(null) == true && arrSchadule?.size > 1) arrSchadule.remove(
                    null
            )
        }
        return (arrSchadule?.size ?: 0)
    }

    inner class ArmyViewHolder constructor(view: View) :
            RecyclerView.ViewHolder(view) {
        var TextViewDate: TextView = view.findViewById(R.id.textDate)

        //       var TextViewText : TextView = view.findViewById(R.id.textViewText)
        var ImageViewCross: ImageView = view.findViewById(R.id.imageViewCross)

        init {

        }

    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

    interface OnScheduleListners {

    }
}