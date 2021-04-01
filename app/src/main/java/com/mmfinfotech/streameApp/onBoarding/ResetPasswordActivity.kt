package com.mmfinfotech.streameApp.onBoarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mmfinfotech.streameApp.R

class ResetPasswordActivity : AppCompatActivity() {
    private val tag: String? = ResetPasswordActivity::class.java.simpleName

    companion object{
        fun getInstance(context: Context?) = Intent(context, ResetPasswordActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
    }

    fun onBack(view: View) = onBackPressed()
    fun onReset(view: View) {}
}