package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.NotificationAdapter
import com.mmfinfotech.streameApp.models.Notification
import com.mmfinfotech.streameApp.util.getIntFromJson
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.ApiClient
import com.mmfinfotech.streameApp.util.retrofit.MyApiEndpointInterface
import com.mmfinfotech.streameApp.util.retrofit.OnApiResponse
import com.mmfinfotech.streameApp.util.retrofit.Sccess
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_notification.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class NotificationFragment : Fragment() {
    private var arrNotification: ArrayList<Notification?>? = null

    companion object {
        @JvmStatic
        fun getInstance() = NotificationFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
        arrNotification = ArrayList()
        getAllNotifications()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notification, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerviewNotification?.adapter = NotificationAdapter(context, arrNotification)
        val spaceHorizontal = context?.resources?.getDimensionPixelSize(R.dimen._14sdp)
        val spaceVertical = context?.resources?.getDimensionPixelSize(R.dimen._10sdp)
        recyclerviewNotification?.addItemDecoration(SpaceItemDecoration(spaceHorizontal ?: 0, spaceVertical ?: 0))
        val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        context?.let { divider.setDrawable(ContextCompat.getDrawable(it, R.drawable.divider_notification)!!) }
        recyclerviewNotification?.addItemDecoration(divider)
    }

    private fun getAllNotifications() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(context)}"

        val apiService: MyApiEndpointInterface? = ApiClient(context).getClient()?.create(MyApiEndpointInterface::class.java)
        val callHotTheme: Call<JsonObject?>? = apiService?.callAllNotification(headers)
        (context as DashBoardActivity).callApi(true, callHotTheme, object : OnApiResponse {
            override fun onSuccess(status: String?, mainObject: JsonObject?) {
                when (status) {
                    Sccess -> {
                        val record: JsonObject? = getJsonObjFromJson(mainObject, "record", JsonObject())
                        val data: JsonArray? = getJsonArrayFromJson(record, "data", JsonArray())
                        for (i in 0 until (data?.size() ?: 0)) {
                            val item: JsonObject? = getJsonObjFromJson(data, i, JsonObject())
                            val id: Int = getIntFromJson(item, "id", AppConstants.Defaults.integer)
                            val receiver: Int = getIntFromJson(item, "receiver", AppConstants.Defaults.integer)
                            val payload: String = getStringFromJson(item, "payload", AppConstants.Defaults.string)
                            val type: Int = getIntFromJson(item, "type", AppConstants.Defaults.integer)
                            val addedOn: String = getStringFromJson(item, "added_on", AppConstants.Defaults.string)
                            val senderId: Int = getIntFromJson(item, "sender_id", AppConstants.Defaults.integer)
                            val senderName: String = getStringFromJson(item, "sender_name", AppConstants.Defaults.string)
                            val senderProfile: String = getStringFromJson(item, "sender_profile", AppConstants.Defaults.string)
                            val notification: String = getStringFromJson(item, "notification", AppConstants.Defaults.string)
                            val referenceId: Int = getIntFromJson(item, "reference_id", AppConstants.Defaults.integer)
                            val content: Int = getIntFromJson(item, "content", AppConstants.Defaults.integer)
                            arrNotification?.add(
                                Notification(
                                    id = id, receiver = receiver, payload = payload, type = type, addedOn = addedOn,
                                    senderId = senderId, senderName = senderName, senderProfile = senderProfile,
                                    notification = notification, referenceId = referenceId, content = content,
                                )
                            )
                        }
                        recyclerviewNotification?.adapter?.notifyDataSetChanged()
                    }
                    else -> {

                    }
                }
                if ((context as DashBoardActivity).dialog?.isShowing == true)
                    (context as DashBoardActivity).dialog?.dismiss()
            }

            override fun onFailure() {}
        })
    }
}