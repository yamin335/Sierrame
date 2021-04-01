package com.mmfinfotech.streameApp.dashBoard.streme.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.model.Hashtags
import com.mmfinfotech.streameApp.utils.AppConstants
import kotlinx.android.synthetic.main.activity_hash_tages.*
import kotlin.collections.ArrayList

class HashTagesActivity : AppCompatActivity() {
    private var hashTags: ArrayList<Hashtags?>? = ArrayList()
    private var SelectedHashTags: ArrayList<Hashtags?>? = ArrayList()
    private var NewHastges: String? = null

    companion object {
        val HashTags = "HashTags"
        fun getInstance(
                context: Context?,
                selectedList: ArrayList<Hashtags?>? = ArrayList()
                , choosenList: ArrayList<Hashtags?>?
        ) = Intent(context, HashTagesActivity::class.java).apply {
            putExtra(HashTags, selectedList)
            putExtra("List2", choosenList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hash_tages)
        hashTags = intent?.getParcelableArrayListExtra(HashTags)
        SelectedHashTags = intent?.getParcelableArrayListExtra("List2")
        if (hashTags?.isEmpty()!! && hashTags == null) {
        } else {
            setHashTagChipGroup()
        }
        setListners()
    }

    private fun setListners() {
        buttonConfirm.setOnClickListener {
            if (SelectedHashTags?.isNullOrEmpty() == true) {
                Toast.makeText(this@HashTagesActivity, getString(R.string.please_select_hash), Toast.LENGTH_SHORT).show()
            } else {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(AppConstants.IntentExtras.selectedStatus, textViewHashTags.text.toString())
                    putParcelableArrayListExtra(AppConstants.IntentExtras.selectedHashTags, SelectedHashTags)
                })
                finish()
            }
        }
        textViewHashTags.setOnClickListener {
            startActivityForResult(
                    AddHashTagsActivity.getInstance(this@HashTagesActivity,
                            intent?.getParcelableArrayListExtra(HashTags)),
                    AppConstants.RequestCode.activityAddHashTags)
        }
    }

    private fun setHashTagChipGroup() {
        for (i in 0 until hashTags?.size!!) {
            val chip = this.layoutInflater.inflate(R.layout.dynamic_chip_item, null, false) as Chip
            chip.text = hashTags?.get(i)?.Tag
            for (j in 0 until SelectedHashTags?.size!!) {
                if (SelectedHashTags?.get(j)?.id == hashTags?.get(i)?.id) {
                    chip.isChecked = true
                    hashTags?.get(i)?.selectedValue = true
                } else {
                }
            }
            chip.isClickable = true
            chip.setOnClickListener {
                if (hashTags?.get(i)?.selectedValue == true) {
                    hashTags?.get(i)?.selectedValue = false
                    try {
                        for (j in 0 until SelectedHashTags?.size!!) {
                            if (hashTags?.get(i)?.Tag?.equals(SelectedHashTags?.get(j)?.Tag)!!) {
                                SelectedHashTags?.remove(SelectedHashTags?.get(j))
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
                    }

                } else {
                    hashTags?.get(i)?.selectedValue = true
                    SelectedHashTags?.add(hashTags?.get(i))
                }

            }
            chipGroupHashTagList.addView(chip)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.RequestCode.activityAddHashTags -> {
                if (Activity.RESULT_OK == resultCode) {
                    if (Activity.RESULT_OK == resultCode) {
                        NewHastges = data?.getStringExtra(AppConstants.IntentExtras.addNewHashTag).toString()
                        hashTags?.add(Hashtags("", NewHastges, false))
                        chipGroupHashTagList.removeAllViews()
                        setHashTagChipGroup()
                    }
                }
            }
        }
    }
}