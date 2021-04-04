package com.mmfinfotech.streameApp.util

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.live.adapter.*
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterArmy
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterPurchaseRecord
import com.mmfinfotech.streameApp.dashBoard.profile.adapter.AdapterSchedule
import com.mmfinfotech.streameApp.dashBoard.streme.adapter.AdapterLiveList
import com.mmfinfotech.streameApp.models.*
import com.mmfinfotech.streameApp.util.listners.*
import com.mmfinfotech.streameApp.utils.OverlapDecoration
import kotlinx.android.synthetic.main.fragment_hot_theme.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

fun ShowAlertSuccess(context: Context?, msg: String?) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    alertDialog.window?.setGravity(Gravity.CENTER)
//    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener)
    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_alert_success,
        null
    )
    alertDialog.setView(view, 0, 0, 0, 0)
    view?.findViewById<TextView>(R.id.textViewSuccessMessage)?.text = msg
    view?.findViewById<AppCompatButton>(R.id.buttonAlertFailure)?.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}

fun ShowAlertFailed(context: Context?, message: String?) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
//    alertDialog.window?.setGravity(Gravity.BOTTOM)
//    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener)
    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_alert_failed,
        null
    )
    alertDialog.setView(view, 0, 0, 0, 0)
    view?.findViewById<TextView>(R.id.textViewFailureMessage)?.text = message
    view?.findViewById<Button>(R.id.buttonAlertFailure)?.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}

fun ShowAlertInformation(
    context: Context?,
    message: String?,
    listener: View.OnClickListener? = null
) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
//    alertDialog.window?.setGravity(Gravity.BOTTOM)
//    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener)
    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_alert_information,
        null
    )
    alertDialog.setView(view, 0, 0, 0, 0)
    view?.findViewById<TextView>(R.id.textViewFailureInformation)?.text = message
    view?.findViewById<Button>(R.id.buttonAlertFailure)?.setOnClickListener {
        alertDialog.dismiss()
        listener?.onClick(it)
    }
    alertDialog.show()
}

/**
 * alert for Api Request Fail
 *
 **/
fun ShowAlertRequestFailed(context: Context?) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
//  alertDialog.window?.setGravity(Gravity.BOTTOM)
//  alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener)
    val view: View? =
        (context as Activity).layoutInflater.inflate(R.layout.dialog_alert_request_failed, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    view?.findViewById<Button>(R.id.buttonAlertRequestFailure)?.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}

/**
 * alert for Api Request Fail
 *
 **/
fun showScheduleDialog(
    context: Context?,
    arrayList: ArrayList<Schadule?>?,
    listener: OnScaduleClickListners?
) {
    val jsonArrays: JsonArray? = JsonArray()
    var adapterSchedule: AdapterSchedule? = null
    var arr: ArrayList<Schadule?>? = ArrayList()
    arr = arrayList
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_schadule, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    alertDialog.window?.setGravity(Gravity.CENTER)
    var recycler = view?.findViewById<RecyclerView>(R.id.recyclerSchedule)

    recycler?.visibility = View.VISIBLE
    recycler?.layoutManager as LinearLayoutManager
    adapterSchedule = AdapterSchedule(
        context,
        arr,
        object : AdapterSchedule.OnScheduleListners {
        })
    recycler?.adapter = adapterSchedule

    view?.findViewById<TextView>(R.id.textViewNew)?.setOnClickListener {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val myYear = year
                val myday = dayOfMonth
                val myMonth = month.plus(1)

                TimePickerDialog(
                    context!!,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        val time =
                            (if (hourOfDay > 12) (if ((hourOfDay % 12) < 10) "0${hourOfDay % 12}" else hourOfDay % 12) else (if (hourOfDay < 10) "0$hourOfDay" else hourOfDay)).toString() + ":" + (if (minute < 10) "0$minute" else minute) + " " + if (hourOfDay >= 12) "PM" else "AM"
                        val date =
                            "${(if (myday < 10) "0$myday" else myday)}-${(if (myMonth < 10) "0$myMonth" else myMonth)}-${myYear} ${time}"
                        if (arrayList?.size!! <= 10) {
                            arr?.add(
                                Schadule(
                                    "", "", "", "", "", date, "",
                                    "", "", "", true
                                )
                            )
                            adapterSchedule?.notifyDataSetChanged()
                            val obj: JsonObject? = JsonObject()
                            obj?.addProperty("date_time", date)
                            obj?.addProperty("title", "")
                            obj?.addProperty("description", "")
                            jsonArrays?.add(obj)
                        } else {
                            ShowAlertInformation(
                                context,
                                context.getString(R.string.txt_only_ten_schedule_can_add)
                            )
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            }, year, month, day
        )
        datePickerDialog.show()
    }

    view?.findViewById<Button>(R.id.buttonCancel)?.setOnClickListener {
        listener?.onCancelClick(view?.findViewById<Button>(R.id.buttonCancel))
//       if(arr?.isEmpty()!! && arr?.size==0){}else{
//        for(i in 0 until arr.size) {
//            if (arr.get(i)?.staticMember == true) {
//                arr?.removeAt(i)
//            }
//        }
//        }

        alertDialog.dismiss()
    }
    view?.findViewById<Button>(R.id.buttonSave)?.setOnClickListener {
        listener?.onSaveClicklistner(
            view?.findViewById<TextView>(R.id.textViewNew).text.toString(),
            jsonArrays
        )
        alertDialog.dismiss()
    }
    alertDialog.show()
}

/**
 *  Dialog for Exit
 *  @param Title heading of Alert
 *  @param subHeading heading of Alert
 *  @param listener heading of Alert
 *
 * */
fun exitDialog(
    context: Context?,
    Title: String?,
    subHeading: String?,
    listener: View.OnClickListener
) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_exit, null)

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.CENTER)
    view?.findViewById<TextView>(R.id.textViewTitle)?.text = Title
    view?.findViewById<TextView>(R.id.textViewSubTitle)?.text = subHeading

    view?.findViewById<Button>(R.id.buttonCancel)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view.findViewById<Button>(R.id.buttonCancel))
    }
    view?.findViewById<Button>(R.id.buttonSave)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view.findViewById<Button>(R.id.buttonSave))
    }
    alertDialog.show()
}

/**
 *  Dialog for Complete Action
 *  @param Title heading of Alert
 *  @param listener heading of Alert
 *
 * */
fun CompleteActionDialog(context: Context?, Title: String?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? =
        (context as Activity).layoutInflater.inflate(R.layout.dialog_complete_action, null)

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.CENTER)
    view?.findViewById<TextView>(R.id.textViewTitle)?.text = Title

    view?.findViewById<AppCompatButton>(R.id.doneTask)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view.findViewById<Button>(R.id.doneTask))
    }
    alertDialog.show()
}

/**
 *  alert Annonymouse Support
 *
 * */
fun annonymouseDialog(context: Context?) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))

    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_annonymouse,
        null
    )

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.CENTER)
    view?.findViewById<ImageView>(R.id.imageViewClose)?.setOnClickListener {
        alertDialog?.dismiss()
    }

    alertDialog.show()
}

/**
 *  alert Prime Dialog
 *
 * */
fun primeuserAgeVarificationDialog(context: Context?) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_age_varificatn,
        null
    )

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.CENTER)
    view?.findViewById<ImageView>(R.id.imageViewClose)?.setOnClickListener {
        alertDialog?.dismiss()
    }

    alertDialog.show()
}

/**
 *  alert Bottom Sharing Dialog
 *
 * */
fun sharingDialog(context: Context?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_sharing, null)

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    view?.findViewById<ImageButton>(R.id.imageButtonline)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view.findViewById<ImageButton>(R.id.imageButtonline))
    }
    view?.findViewById<ImageButton>(R.id.imageButtonFb)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view.findViewById<ImageButton>(R.id.imageButtonFb))
    }
    view?.findViewById<ImageButton>(R.id.imageButtonGoogle)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view.findViewById<ImageButton>(R.id.imageButtonGoogle))
    }
    alertDialog.show()
}

/**
 *  alert Bottom  Guardian Dialog
 *
 * */
fun showGuardianDialog(context: Context?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_guardian, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    view?.findViewById<ImageView>(R.id.imageViewHelp)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view.findViewById<ImageView>(R.id.imageViewHelp))
    }
    alertDialog.show()
}

/**
 * alert Bottom  Guardian  Rules Dialog
 *
 **/
fun showGuardianRulesDialog(context: Context?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_rules_guardian,
        null
    )

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    view?.findViewById<ImageView>(R.id.imageViewHelp)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view.findViewById<ImageView>(R.id.imageViewHelp))
    }
    alertDialog.show()
}

/**
 * alert Bottom  Guardian  Rules Dialog
 *
 **/
fun showArmyDialog(
    context: Context?,
    arrayList: ArrayList<HomeLive?>?,
    listener: View.OnClickListener
) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_army, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    var mLayoutManager: LinearLayoutManager? =
        view?.findViewById<RecyclerView>(R.id.recyclerArmy)?.layoutManager as LinearLayoutManager
    val adapterHomeLive = AdapterArmy(
        context,
        arrayList,
        object : AdapterArmy.OnArmyListner {

        })

    view?.findViewById<RecyclerView>(R.id.recyclerArmy).adapter = adapterHomeLive

    view?.findViewById<AppCompatButton>(R.id.buttonPartake)?.setOnClickListener {
        alertDialog?.dismiss()
        showArmyPriviledgesDialog(context, listener)
//        listener.onClick(view.findViewById<AppCompatButton>(R.id.buttonPartake))
    }
    alertDialog.show()
}


/**
 *Priviledges of armay
 * */
fun showArmyPriviledgesDialog(context: Context?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_army_priveledges,
        null
    )
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    view?.findViewById<AppCompatButton>(R.id.buttonPartakePrev)?.setOnClickListener {
//        alertDialog?.dismiss()
        showArmeyMadaleTxtDialog(context)
    }

    alertDialog.show()
}

/**
 * Dialog for Armey text
 * */
fun showArmeyMadaleTxtDialog(context: Context?) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_armytext, null)

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.CENTER)
    view?.findViewById<ImageView>(R.id.imageViewClose)?.setOnClickListener {
        alertDialog?.dismiss()
    }

    alertDialog.show()
}

/**
 * Dialog for Armey text
 *
 * @param context Instance of the class calling this function.
 * */
fun showSearchDialog(context: Context?) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_search, null)

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.TOP)
    view?.findViewById<EditText>(R.id.editTextSearch)?.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
    alertDialog.show()

}


/**
 * Dialog for Armey text
 *
 * @param context Instance of the class calling this function.
 *  @param listener through id get the view and perform ation
 * */
fun showGanderSelectiionDialog(context: Context?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_gander_selection,
        null
    )

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    view?.findViewById<TextView>(R.id.textView_Male)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view?.findViewById<TextView>(R.id.textView_Male))
    }
    view?.findViewById<TextView>(R.id.textView_Female)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view?.findViewById<TextView>(R.id.textView_Female))
    }
    view?.findViewById<TextView>(R.id.textView_Private)?.setOnClickListener {
        alertDialog?.dismiss()
        listener.onClick(view?.findViewById<TextView>(R.id.textView_Private))
    }

    alertDialog.show()
}

/**
 * Dialog for Streame
 *
 * @param context Instance of the class calling this function.
 *  @param listener through id get the view and perform ation
 * */
fun showAlertStreame(context: Context?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_streme, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT
    );
    alertDialog.window?.setGravity(Gravity.CENTER)
//    view?.findViewById<TextView>(R.id.textViewPost)?.setOnClickListener {
//        alertDialog.dismiss()
//        listener.onClick(view?.findViewById<TextView>(R.id.textViewPost))
//    }
//    view?.findViewById<TextView>(R.id.textViewLive)?.setOnClickListener {
//        alertDialog.dismiss()
//        listener.onClick(view?.findViewById<TextView>(R.id.textViewLive))
//    }
//    view?.findViewById<TextView>(R.id.textViewRecording)?.setOnClickListener {
//        alertDialog.dismiss()
//        listener.onClick(view?.findViewById<TextView>(R.id.textViewRecording))
//    }
//    view?.findViewById<ImageView>(R.id.imageViewClips)?.setOnClickListener {
//        alertDialog.dismiss()
//        listener.onClick(view?.findViewById<ImageView>(R.id.imageViewClips))
//    }
    view?.findViewById<ConstraintLayout>(R.id.container_post)?.setOnClickListener {
        alertDialog.dismiss()
        listener.onClick(view.findViewById<TextView>(R.id.container_post))
    }
    view?.findViewById<ConstraintLayout>(R.id.container_Live)?.setOnClickListener {
        alertDialog.dismiss()
        listener.onClick(view.findViewById<TextView>(R.id.container_Live))
    }
    view?.findViewById<ConstraintLayout>(R.id.container_clips)?.setOnClickListener {
        alertDialog.dismiss()
        listener.onClick(view.findViewById<TextView>(R.id.container_clips))
    }


    alertDialog.show()
}

/**
 * dialog for live Caution
 *
 * @param context Instance of the class calling this function.
 *  @param listener through id get the view and perform ation
 * */
fun showAlertCoutionLiveStreame(context: Context?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_live_streme,
        null
    )

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.CENTER)
    view?.findViewById<AppCompatButton>(R.id.buttonAuthentication)?.setOnClickListener {
        alertDialog.dismiss()
        listener.onClick(view?.findViewById<AppCompatButton>(R.id.buttonAuthentication))
    }
    view?.findViewById<AppCompatButton>(R.id.buttonLater)?.setOnClickListener {
        alertDialog.dismiss()
    }


    alertDialog.show()
}

/**
 * dialog for live Caution
 *
 * @param context Instance of the class calling this function.
 *  @param arrayList list to show
 *
 **/
fun showMyEvents(context: Context?, arrayList: ArrayList<HomeLive?>?) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_my_event, null)

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    alertDialog.window?.setGravity(Gravity.CENTER)
    view?.findViewById<AppCompatButton>(R.id.buttonAuthentication)?.setOnClickListener {
        alertDialog.dismiss()
//      listener.onClick(view?.findViewById<AppCompatButton>(R.id.buttonAuthentication))
    }

    view?.findViewById<TextView>(R.id.textViewLastDay)?.setOnClickListener {
//        alertDialog.dismiss()
        getDate(context, view?.findViewById<TextView>(R.id.textViewLastDay))
    }
    view?.findViewById<TextView>(R.id.textViewGift)?.setOnClickListener {
        showGiftItem(context, arrayList)
    }

    alertDialog.show()
}

/**
 * Alert for recycler List in Live
 *
 * @param context Instance of the class calling this function.
 * @param arrayList list to show on dialog
 * @param textView view on which to set data
 *  @param listener through id get the view and perform ation
 *
 **/
fun showChetegoryLiveList(
    context: Context?,
    arrayList: ArrayList<LiveStreamCategory?>?,
    textView: TextView?,
    listners: OnMemoAddListners
) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_live_list, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    var mLayoutManager: LinearLayoutManager? =
        view?.findViewById<RecyclerView>(R.id.recyclerList)?.layoutManager as LinearLayoutManager
    val adapterHomeLive = AdapterLiveList(
        context,
        arrayList,
        object : AdapterLiveList.OnLiveListListner {
            override fun onitemClickListners(p: Int?) {
                textView?.text = arrayList?.get(p!!)?.name
                listners.saveStringListners(
                    textView as TextView,
                    arrayList?.get(p!!)?.id.toString()
                )
                alertDialog.dismiss()
            }
        })
    view.findViewById<RecyclerView>(R.id.recyclerList).adapter = adapterHomeLive
    alertDialog.show()
}

/**
 * Alert for recycler List in Live
 *
 * @param context Instance of the class calling this function.
 *  @param arrayList list to show
 *
 **/
fun showGiftItem(context: Context?, arrayList: ArrayList<HomeLive?>?) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_gift_list, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    val gridLayoutManager = GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)
//    GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false)
    view?.findViewById<RecyclerView>(R.id.recyclerviewGifts)?.layoutManager = gridLayoutManager
    val adapterGift =
        AdapterPurchaseRecord(
            context,
            arrayList,
            object : AdapterPurchaseRecord.OnPurchaseRecordLisner {
                override fun onClick(position: Int) {
                }
            })

    view?.findViewById<RecyclerView>(R.id.recyclerviewGifts)?.adapter = adapterGift
    alertDialog.show()
}

/**
 * Alert for recycler List in Live
 *
 * @param context Instance of the class calling this function.
 * @param gifList list to show
 *
 **/
fun showGIFSheet(context: Context?, gifList: ArrayList<String?>?, listners: OnGifListners) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_gif_list, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )

    alertDialog.window?.setGravity(Gravity.BOTTOM)
    val gridLayoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
//    GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false)
    view?.findViewById<RecyclerView>(R.id.recyclerviewGifts)?.layoutManager = gridLayoutManager
    val adapterGift =
        AdapterGifSheet(
            context,
            gifList,
            object : AdapterGifSheet.OnPurchaseRecordLisner {
                override fun onClick(position: Int) {
                    listners.onGifClicking(gifList?.get(position))
                    alertDialog.dismiss()
                }
            })

    view?.findViewById<RecyclerView>(R.id.recyclerviewGifts)?.adapter = adapterGift
    alertDialog.show()
}

/**
 * Alert for Live user Dialog
 *
 * @param context Instance of the class calling this function.
 * @param liversProfile models class contains all view
 *  @param listener through id get the view and perform ation
 *
 **/
fun showLiveUser(context: Context?, liversProfile: LiversProfile?, listners: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val view: View? =
        (context as Activity).layoutInflater.inflate(R.layout.dialog_live_user_detail, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    alertDialog.window?.setGravity(Gravity.CENTER)
    alertDialog?.setCanceledOnTouchOutside(false)
    view?.findViewById<TextView>(R.id.textViewUserTitle)?.text = liversProfile?.streamerName

    val testViewFollow = view?.findViewById<TextView>(R.id.textViewFollowDialog)
    view?.findViewById<TextView>(R.id.textViewStatus)?.text =
        liversProfile?.streamerStatus //getStringFromJson(userData, "streamer_status", AppConstants.Defaults.string)

    testViewFollow?.text = if (liversProfile?.followStatus.equals("1"))
        context.resources.getString(R.string.following) else context.resources.getString(R.string.txt_follow)

    val HashTages =
        liversProfile?.hashTags//getStringFromJson(userData, "hashtags", AppConstants.Defaults.string) + "," + ","

    if (HashTages == null && HashTages.equals("")) {

    } else {
        val value: List<String?>? = HashTages?.split(",")
        if (value?.size ?: 0 > 0)
            view?.findViewById<TextView>(R.id.textViewWeight)?.text = value?.get(0)
        if (value?.size ?: 0 > 1)
            view?.findViewById<TextView>(R.id.textViewHeight)?.text = value?.get(1)
    }
    Glide.with(context)
        .load(liversProfile?.streamerProfile)
        .into(view?.findViewById(R.id.imageViewDialogLiversProfile) as ImageView)

    if (liversProfile?.followStatus.equals("1")) {
        setTextViewDrawableColor(testViewFollow!!, R.color.seletedTint)
    } else {
        setTextViewDrawableColor(testViewFollow!!, R.color.seletedTint)
    }

    view?.findViewById<TextView>(R.id.textViewMoreDetail)?.setOnClickListener {
        listners.onClick(view.findViewById<TextView>(R.id.textViewMoreDetail))
        alertDialog.dismiss()
    }
    view?.findViewById<TextView>(R.id.textViewFollowDialog)?.setOnClickListener {
        listners.onClick(view.findViewById<TextView>(R.id.textViewFollowDialog))
        alertDialog.dismiss()
    }
    view?.findViewById<Button>(R.id.buttonRespose)?.setOnClickListener {
        listners.onClick(view.findViewById<Button>(R.id.buttonRespose))
        alertDialog.dismiss()
    }
    view?.findViewById<Button>(R.id.buttonRecording)?.setOnClickListener {
        listners.onClick(view.findViewById<Button>(R.id.buttonRespose))
    }
    view?.findViewById<ImageView>(R.id.imageViewSchedule)?.setOnClickListener {
        listners.onClick(it)
        alertDialog.dismiss()
    }
    view?.findViewById<ImageView>(R.id.imageViewMemo)?.setOnClickListener {
        listners.onClick(view.findViewById<ImageView>(R.id.imageViewMemo))
        alertDialog.dismiss()
    }
    view?.findViewById<ImageView>(R.id.imaegViewClose)?.setOnClickListener {
        listners.onClick(view.findViewById<ImageView>(R.id.imaegViewClose))
        alertDialog.dismiss()
    }
    alertDialog.show()
}

/**
 * Alert Dialog for Add Memo for Stremer
 *
 * @param context Context of Profile
 * @param profileMemo memo
 * @param lisners for add the string  listners
 * @param userName User name for Memo
 *
 * */
fun showMemoliveStremer(
    context: Context?,
    profileMemo: String?,
    userName: String?,
    listners: OnMemoAddListners
) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_memo_add, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    alertDialog.window?.setGravity(Gravity.CENTER)

    view?.findViewById<TextView>(R.id.textViewUserTitle)?.text = userName
    view?.findViewById<EditText>(R.id.editTextMemo)?.setText(profileMemo)

    view?.findViewById<Button>(R.id.buttonSave)?.setOnClickListener {
        if (validationEmptyField(
                context,
                view?.findViewById<EditText>(R.id.editTextMemo)
            ) == true
        ) {
            listners.saveStringListners(
                it,
                view?.findViewById<EditText>(R.id.editTextMemo).text.toString()
            )
            alertDialog.dismiss()
        } else {
        }

    }
    view?.findViewById<Button>(R.id.buttonCancel)?.setOnClickListener {
        listners.cancelViewListners(view?.findViewById<Button>(R.id.buttonCancel))
        alertDialog.dismiss()
    }
    alertDialog.show()
}

/**
 * Alert Dialog for show the Schedule for stremer
 *
 * */

fun dialogScheduleListliveStremer(
    context: Context?,
    arrayList: ArrayList<LiveHotThemeSchedule?>?,
    userName: String?
) {
    val arrayLS: ArrayList<LiveHotThemeSchedule?>? = ArrayList()
    arrayLS?.addAll(arrayList!!)
    val alertDialog = AlertDialog.Builder(context!!).create()

    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val view: View? = (context as Activity).layoutInflater.inflate(R.layout.dialog_schedule, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    alertDialog.window?.setGravity(Gravity.CENTER)
    var mLayoutManager: LinearLayoutManager? =
        view?.findViewById<RecyclerView>(R.id.recyclerScheduleHotTheme)?.layoutManager as LinearLayoutManager
    val adapterLiveSceduleHotTheme = AdapterLiveSceduleHotTheme(
        context,
        arrayList,
        object : AdapterLiveSceduleHotTheme.OnLiveListListner {

        })

    view?.findViewById<RecyclerView>(R.id.recyclerScheduleHotTheme).adapter =
        adapterLiveSceduleHotTheme
    view?.findViewById<TextView>(R.id.textViewUserTitle)?.text = userName
    view?.findViewById<ImageView>(R.id.imaegViewClose)?.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}

/**
 * Alert Dialog for photo selection
 *
 *  @param context Instance of the class calling this function.
 *  @param listener through id get the view and perform ation
 * */

fun dialogSelectPhoto(context: Context?, listener: View.OnClickListener) {
    val alertDialog = AlertDialog.Builder(context!!, R.style.MyDialog).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? = (context as Activity).layoutInflater.inflate(
        R.layout.dialog_choose_profile,
        null
    )
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    alertDialog.window?.setGravity(Gravity.BOTTOM)
    view?.findViewById<TextView>(R.id.textViewRemove)?.setOnClickListener {
        listener.onClick(it)
        alertDialog.dismiss()
    }
    view?.findViewById<TextView>(R.id.textViewGallery)?.setOnClickListener {
        listener.onClick(it)
        alertDialog.dismiss()
    }
    view?.findViewById<TextView>(R.id.textViewCamera)?.setOnClickListener {
        listener.onClick(it)
        alertDialog.dismiss()
    }
    alertDialog.show()
}


/**
 * Alert Dialog for private Boradcasting to generate and set password.
 *
 * @author Somil Rawal
 * @param context Instance of the class calling this function.
 * @param description Text description to be shown on the alert description field.
 * @param autoGeneratePass Boolean to auto generate password in case of broadcasting live for private.
 * @param listener for action to be perform.
 * */
fun createPrivateBroadCasting(
    context: Context?,
    description: String?,
    autoGeneratePass: Boolean?,
    listener: OnDialogPasswordListner?
) {
    context?.let { _context ->
        AlertDialog.Builder(_context).create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            setCanceledOnTouchOutside(false)
            setView(
                (context as Activity).layoutInflater.inflate(
                    R.layout.dialog_private_broadcasting,
                    null
                ).apply {
                    findViewById<Button>(R.id.buttonDialogCreate).apply {
                        text = if (autoGeneratePass == true) context.getString(
                            R.string
                                .btn_create
                        ) else context.getString(R.string.txt_join)
                    }
                    findViewById<AppCompatTextView>(R.id.textViewDialogDescription).apply {
                        text = description
                    }

                    findViewById<AppCompatEditText>(R.id.editTextAutoGeneratePassword).apply {
                        if (autoGeneratePass == true) setText(
                            randomPassword()
                        )
                    }
                    findViewById<AppCompatImageButton>(R.id.appCompatImageButtonRefreshPassword).apply {
                        if (autoGeneratePass == false) visibility = View.GONE
                    }.setOnClickListener {
                        findViewById<AppCompatEditText>(R.id.editTextAutoGeneratePassword).setText(
                            randomPassword()
                        )
                    }

                    findViewById<Button>(R.id.buttonDialogCreate).setOnClickListener {
                        if (TextUtils.isEmpty(findViewById<AppCompatEditText>(R.id.editTextAutoGeneratePassword).text)) {
                            findViewById<AppCompatEditText>(R.id.editTextAutoGeneratePassword).error =
                                context.getString(R.string.txt_cant_be_empty)
                            return@setOnClickListener
                        }
                        dismiss()
                        listener?.onButtonClick(
                            it,
                            findViewById<AppCompatEditText>(R.id.editTextAutoGeneratePassword).text.toString()
                        )
                    }
                    findViewById<Button>(R.id.buttonDialogShare).apply {
                        if (autoGeneratePass == false) visibility = View.GONE
                    }.setOnClickListener {
                        if (TextUtils.isEmpty(findViewById<AppCompatEditText>(R.id.editTextAutoGeneratePassword).text)) {
                            findViewById<AppCompatEditText>(R.id.editTextAutoGeneratePassword).error =
                                context.getString(R.string.txt_cant_be_empty)
                        } else {
                            listener?.onShareButtonClick(
                                findViewById<Button>(R.id.buttonDialogShare),
                                findViewById<AppCompatEditText>(
                                    R.id.editTextAutoGeneratePassword
                                ).text.toString()
                            )
                        }
                        dismiss()
                    }
                }, 0, 0, 0, 0
            )
            show()
        }
    }
}

private fun randomPassword(): String? {
    val generator = Random
    val randomStringBuilder = StringBuilder()
    val randomLength = generator.nextInt(7, 8)
    var tempChar: Char
    for (i in 0 until randomLength) {
        tempChar = (generator.nextInt(96) + 32).toChar()
        randomStringBuilder.append(tempChar)
    }
    return randomStringBuilder.toString()
}

fun showTestDialog(context: Context?, listener: OnTestListner?) {
    context?.let {
        AlertDialog.Builder(it)
            .setPositiveButton(context.getString(R.string.btnOk)) { dialog, _ ->
                listener?.onClick(
                    dialog
                )
            }
            .create().apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                setCanceledOnTouchOutside(false)
                setMessage("hsagdhjas")
                show()
            }
    }


}

/**
 * this dialog use for  show  the livers to make them reaquest for join this Live
 *
 * */
fun showRequestBrodcaterDialog(
    context: Context?,
    listners: View.OnClickListener
) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? =
        (context as Activity).layoutInflater.inflate(R.layout.dialog_requestbrodcast, null)
    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    alertDialog.window?.setGravity(Gravity.CENTER)

    view?.findViewById<TextView>(R.id.textViewAddContacts)?.setOnClickListener {
        listners.onClick(view?.findViewById<TextView>(R.id.textViewAddContacts))
        alertDialog.dismiss()
    }
    view?.findViewById<ImageView>(R.id.imageButton)?.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}


fun showRequestSendDialog(
    context: Context?,
    selectedLiveUsers: ArrayList<Liver?>?,
    adapterSelectedRequest: AdapterSelectedLivers?,
    listners: OnMemoAddListners
) {
    val alertDialog = AlertDialog.Builder(context!!).create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    val view: View? =
        (context as Activity).layoutInflater.inflate(R.layout.dialog_request_send, null)

    alertDialog.setView(view, 0, 0, 0, 0)
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    var adapterSelectedLiveUsersRequest: AdapterSelectedLiveUsersRequest? = null
    var rcLiveUsers = view?.findViewById<RecyclerView>(R.id.rcLiveUsers)
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    layoutManager.reverseLayout = true
    layoutManager.stackFromEnd = true
    rcLiveUsers?.layoutManager = layoutManager
    rcLiveUsers?.addItemDecoration(OverlapDecoration())
//    rcLiveUsers?.layoutManager as LinearLayoutManager
    adapterSelectedLiveUsersRequest = AdapterSelectedLiveUsersRequest(
        context,
        selectedLiveUsers,
        object : AdapterSelectedLiveUsersRequest.OnSelectedReqstLiversListner {
            override fun onReqstLiveListner(p: Int) {
            }

            override fun onCancelClicking(p: Int) {
//                selectedLiveUsers?.removeAt(p)
//                adapterSelectedLiveUsersRequest?.notifyItemRemoved(p)
//                adapterSelectedRequest?.notifyItemRemoved(p)
            }
        })
    rcLiveUsers?.adapter = adapterSelectedLiveUsersRequest
    view?.findViewById<AppCompatButton>(R.id.buttonCancel)?.setOnClickListener {
        alertDialog.dismiss()
    }
    view?.findViewById<AppCompatButton>(R.id.buttonStremeSend)?.setOnClickListener {
        if (validationEmptyField(
                context,
                view?.findViewById<EditText>(R.id.edtTextComments)
            ) == true
        ) {
            listners.saveStringListners(
                it,
                view.findViewById<EditText>(R.id.edtTextComments).text.toString()
            )
            alertDialog.dismiss()
        } else {
        }
    }
    alertDialog.window?.setGravity(Gravity.CENTER)
    alertDialog.show()
}

fun ShowAlertInvitationToJoinLive(
    context: Context?,
    message: String?,
    listners: View.OnClickListener?
) {
    context?.let { ctx ->
        AlertDialog.Builder(ctx).create().apply {
            this.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            this.window?.setGravity(Gravity.CENTER)
//            this.window?.setType(WindowManager.LayoutParams.INTERNAL_SYSTEM_WINDOW )
//            val view: View? = (ctx as Activity).layoutInflater.inflate(
            val view: View? = LayoutInflater.from(ctx).inflate(
                R.layout.dialog_alert_invite_to_live, null).also {
                findViewById<TextView>(R.id.textViewSubTitle)?.text = message
                findViewById<Button>(R.id.buttonCancel)?.setOnClickListener { button ->
                    listners?.onClick(button)
                    dismiss()
                }
                findViewById<Button>(R.id.buttonSave)?.setOnClickListener { button ->
                    listners?.onClick(button)
                    dismiss()
                }
            }
            setView(view, 0, 0, 0, 0)
        }.show()
    }
}