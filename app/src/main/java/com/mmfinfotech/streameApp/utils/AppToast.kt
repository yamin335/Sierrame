package com.mmfinfotech.streameApp.utils

/**
 * Created by Somil Rawal on Mon-07-September-2020.
 */
import android.content.Context
import android.view.Gravity
import android.widget.Toast

class AppToast {
    companion object {
        fun showToast(context: Context?, message: String? = "N/A") {
            Toast.makeText(context, message, Toast.LENGTH_SHORT)?.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }
}