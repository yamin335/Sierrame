package com.mmfinfotech.streameApp.utils

import android.app.Dialog

fun Dialog?.dismissSafely() {
    if (this?.isShowing == true) this.dismiss()
}