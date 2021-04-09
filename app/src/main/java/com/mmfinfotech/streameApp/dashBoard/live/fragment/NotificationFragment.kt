package com.mmfinfotech.streameApp.dashBoard.live.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.DashBoardActivity
import com.mmfinfotech.streameApp.dashBoard.live.adapter.NotificationAdapter
import com.mmfinfotech.streameApp.models.FollowUnFollowResponse
import com.mmfinfotech.streameApp.models.Notification
import com.mmfinfotech.streameApp.models.NotificationResponse
import com.mmfinfotech.streameApp.util.getIntFromJson
import com.mmfinfotech.streameApp.util.getJsonArrayFromJson
import com.mmfinfotech.streameApp.util.getJsonObjFromJson
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_play_streaming.*
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
        val callAllNotification: Call<NotificationResponse?>? = apiService?.callAllNotification(headers)
        (context as DashBoardActivity).callRemoteApi(true, callAllNotification, object : ApiClient.ApiCallbackListener<NotificationResponse> {
            override fun onDataFetched(response: NotificationResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create((context as DashBoardActivity), response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<NotificationResponse> {
                        override fun onSuccess(response: NotificationResponse) {
                            val notifications = response.record?.data ?: return
                            for (notification in notifications) {
                                arrNotification?.add(notification)
                            }
                            recyclerviewNotification?.adapter?.notifyDataSetChanged()
                        }

                        override fun onFailed(status: String, message: String) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        })
    }
}