package com.mmfinfotech.streameApp.dashBoard.profile.activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity

class LevelActivity : DashBoardBaseActivity() {
    private val TAG: String? = LevelActivity::class.java.simpleName
    companion object {
        fun getInstance(
            context: Context?
        ) = Intent(context, LevelActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        initView()
        setListners()
    }

    private fun setListners() {
        findViewById<ConstraintLayout>(R.id.includeToolbarLevel)?.
        findViewById<ImageView>(R.id.imageToolbarBack)?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        findViewById<ConstraintLayout>(R.id.includeToolbarLevel)?.
        findViewById<TextView>(R.id.textViewTitle)?.text = getString(R.string.level)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}