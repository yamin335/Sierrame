package com.mmfinfotech.streameApp.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mmfinfotech.streameApp.R

fun progressDialog(ctx: Context): Dialog? {
    val dialog: Dialog? = Dialog(ctx)
    dialog?.setCanceledOnTouchOutside(false)
    dialog?.setCancelable(false)
    dialog?.setContentView(R.layout.progress_dialog)
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val img = dialog?.findViewById<ImageView?>(R.id.imageViewLoader)
    Glide.with(ctx)
        .load(R.drawable.loader)
        .into(img!!)
    return dialog
}