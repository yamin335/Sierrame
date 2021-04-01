package com.mmfinfotech.streameApp.util.listners

import android.view.View
import com.google.gson.JsonArray

interface OnScaduleClickListners {
    fun onCancelClick(view: View?)
    fun onSaveClicklistner(s: String?,jsonArray: JsonArray?)
}