package com.mmfinfotech.streameApp.dashBoard.profile.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.util.primeuserAgeVarificationDialog
import kotlinx.android.synthetic.main.activity_prime_user.*

class PrimeUserActivity : AppCompatActivity() {
    companion object {
        fun getInstance(
                context: Context?
        ) = Intent(context, PrimeUserActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prime_user)
        initView()
        setListners()
    }
    private fun initView() {
        findViewById<ConstraintLayout>(R.id.includeToolbarPrimeUser)?.
        findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.prime_user)
    }
    private fun setListners() {
        textView9.setOnClickListener { primeuserAgeVarificationDialog(this@PrimeUserActivity) }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}