package com.mmfinfotech.streameApp.util.networkWatcher

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.mmfinfotech.streameApp.R

/**
 * Created by Somil Rawal on 25-09-2019.
 */
fun alertNoInternetConnectionBlockUi(context: Context?): Dialog? {
    // custom dialog
    val dialog: Dialog? = Dialog(context!!)
    dialog?.setContentView(R.layout.dialog_no_network_connection_block_ui)
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog?.setCanceledOnTouchOutside(false)
    dialog?.setCancelable(false)
    return dialog
}

fun alertNoInternetConnection(context: Context?){
    // custom dialog
    val dialog: Dialog? = Dialog(context!!)
    dialog?.setContentView(R.layout.dialog_no_network_connection)
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog?.show()
}
