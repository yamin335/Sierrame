package com.mmfinfotech.streameApp.dashBoard.streme.activity
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.models.LiveStreamHashTag
import com.mmfinfotech.streameApp.utils.AppConstants
import kotlinx.android.synthetic.main.activity_add_hash_tags.*
import java.security.SecureRandom

class AddHashTagsActivity : AppCompatActivity() {
    private var AddHash: ArrayList<LiveStreamHashTag?>? = ArrayList()

    companion object {
        fun getInstance(
                context: Context?, hashArray: ArrayList<LiveStreamHashTag?>?
        ) = Intent(context, AddHashTagsActivity::class.java).apply {
            putExtra(HashTagesActivity.HashTags, hashArray)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hash_tags)
        listListner()
    }

    private fun listListner() {
        backButton.setOnClickListener {
            finish()
        }
        appCompatButtonAdd.setOnClickListener {
            AddHash = intent?.getParcelableArrayListExtra(HashTagesActivity.HashTags)
            AddHash?.add(LiveStreamHashTag(0, editTextAddTag.text.toString(),false))
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(AppConstants.IntentExtras.addNewHashTag, editTextAddTag.text.toString())
            })
            finish()
        }
    }
}