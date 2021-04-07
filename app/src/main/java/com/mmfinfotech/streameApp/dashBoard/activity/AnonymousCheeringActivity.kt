package com.mmfinfotech.streameApp.dashBoard.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.base.NetworkBaseActivity
import com.mmfinfotech.streameApp.dashBoard.adapter.AnonymousCheeringAdapter
import com.mmfinfotech.streameApp.dashBoard.streme.activity.AuthenticatLive
import com.mmfinfotech.streameApp.dashBoard.streme.activity.AuthenticationActivity
import com.mmfinfotech.streameApp.models.AnonymousCheering
import com.mmfinfotech.streameApp.models.AnonymousCheeringResponse
import com.mmfinfotech.streameApp.models.LiveStreamHashTagCategoryResponse
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.util.retrofit.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import com.mmfinfotech.streameApp.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_annonymous_cheering.*
import kotlinx.android.synthetic.main.activity_cheering_live.*
import kotlinx.android.synthetic.main.activity_leader_board.*
import retrofit2.Call
import java.util.HashMap

class AnonymousCheeringActivity : NetworkBaseActivity() {
    private val tag: String? = AnonymousCheeringActivity::class.java.simpleName
    private var arrAnonymousCheering: ArrayList<AnonymousCheering?>? = null

    companion object {
        fun getInstance(context: Context?) = Intent(context, AnonymousCheeringActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annonymous_cheering)

        findViewById<ConstraintLayout>(R.id.includeToolbarAnonymousCheering)?.findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.anonymous_cheering)
        findViewById<ConstraintLayout>(R.id.includeToolbarAnonymousCheering)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.setImageDrawable(getDrawable(R.drawable.ic_help_))
        arrAnonymousCheering = ArrayList()

        setListener()

        callDataApi()

        val spaceHorizontal = resources?.getDimensionPixelSize(R.dimen._10sdp)
        val spaceVertical = resources?.getDimensionPixelSize(R.dimen._10sdp)
        recyclerViewAnonymousCheering?.addItemDecoration(SpaceItemDecoration(spaceHorizontal ?: 0, spaceVertical ?: 0))
        recyclerViewAnonymousCheering?.adapter = AnonymousCheeringAdapter(this@AnonymousCheeringActivity, arrAnonymousCheering)
    }

    private fun setListener() {
        findViewById<ConstraintLayout>(R.id.includeToolbarAnonymousCheering)?.findViewById<ImageView>(R.id.imageToolbarBack)?.setOnClickListener {
            onBackPressed()
        }
        findViewById<ConstraintLayout>(R.id.includeToolbarAnonymousCheering)?.findViewById<ImageView>(R.id.imageToolbarMenu)?.setOnClickListener {
            showDialog()
        }
    }

    private fun callDataApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${AppPreferences().getAuthToken(this@AnonymousCheeringActivity)}"

        val apiService: MyApiEndpointInterface? = ApiClient(this@AnonymousCheeringActivity).getClient()?.create(MyApiEndpointInterface::class.java)

        val callCheeringAnonymous: Call<AnonymousCheeringResponse?>? = apiService?.callCheeringAnonymous(headers)

        callRemoteApi(true, callCheeringAnonymous, object : ApiClient.ApiCallbackListener<AnonymousCheeringResponse> {
            override fun onDataFetched(response: AnonymousCheeringResponse?, isSuccess: Boolean, message: String) {
                if (!isSuccess) return
                ApiResponse.create(this@AnonymousCheeringActivity, response?.status, response?.message ?: message,
                    response, object : ApiClient.ApiResponseListener<AnonymousCheeringResponse> {
                    override fun onSuccess(response: AnonymousCheeringResponse) {
                        val data = response.record?.data ?: return
                        for (item in data) {
                            arrAnonymousCheering?.add(item)
                        }
                        recyclerViewAnonymousCheering.adapter?.notifyDataSetChanged()
                    }

                    override fun onFailed(status: String, message: String) {
                        Toast.makeText(this@AnonymousCheeringActivity, message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        })
    }

    private fun showDialog() {
        Dialog(this@AnonymousCheeringActivity).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.partial_layout_dialog_anonymous_support)
            findViewById<ImageButton>(R.id.imageButtonClose).setOnClickListener {
                dismiss()
            }
            show()
        }
    }
}