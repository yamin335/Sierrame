package com.mmfinfotech.streameApp.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.OnBoardingBaseActivity
import kotlinx.android.synthetic.main.activity_wel_come.*

class WelComeActivity : OnBoardingBaseActivity() {

    companion object {
        fun getInstance(context: Context?) = Intent(context, WelComeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wel_come)

        buttonLoginGO.setOnClickListener {
            startActivity(SignInActivity.getInstance(this@WelComeActivity))
        }
        buttonSignUpGo.setOnClickListener {
            startActivity(SignUpActivity.getInstance(this@WelComeActivity))
        }
    }
}