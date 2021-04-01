package com.mmfinfotech.streameApp.util.listners
import android.view.View

/**
 * this is used to pass the  valies and views
 * */
interface OnMemoAddListners {
    fun saveStringListners(view: View, s: String)
    fun cancelViewListners(view: View)
}