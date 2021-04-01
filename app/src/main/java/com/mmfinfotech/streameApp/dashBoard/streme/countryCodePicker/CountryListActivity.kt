package com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.models.Country
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.RecyclerTouchListener
import kotlinx.android.synthetic.main.activity_country_list.*
import java.util.*
import kotlin.collections.ArrayList

class CountryListActivity : AppCompatActivity() {
    private var arrMainCountry: ArrayList<Country?>? = null
    private var recyclerTouchListener: RecyclerTouchListener? = null
    private var intentFrom: String? = null

    companion object {
        fun getInstance(
                context: Context?
        ) = Intent(context, CountryListActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

             initView()
    }

    override fun onResume() {
        super.onResume()
        if (arrMainCountry.isNullOrEmpty())
            arrMainCountry = ArrayList(getCountryCodes(this@CountryListActivity,AppConstants.AssertName.assertCountryCode) ?: ArrayList())

        setCountryAdapter(arrMainCountry)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.activity_country_code_picker, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_country_search)
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchForData(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
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

    private fun searchForData(newText: String?) {
        val searchCountry: ArrayList<Country?>? = ArrayList()
        if (TextUtils.isEmpty(newText) || newText?.length == 0) {
            setCountryAdapter(arrMainCountry)
            searchCountry?.clear()
        } else {
            arrMainCountry?.forEach { country ->
                //                if (country?.name?.toLowerCase(Locale.getDefault())?.contains(newText) == true){
                if (newText?.let {
                        country?.name?.toLowerCase(Locale.getDefault())?.contains(it)
                    } == true) {
                    searchCountry?.add(country)
                }
            }
            if (searchCountry?.isEmpty() == true)
                searchCountry.add(null)
            setCountryAdapter(searchCountry)
        }
    }

    private fun setCountryAdapter(country: ArrayList<Country?>?) {
        val countryAdapter: CountryAdapter? = CountryAdapter(this@CountryListActivity, CountryAdapter.code, country)
        recyclerview_country_list?.adapter = countryAdapter
        setRecyclerViewLisners(country)
    }

    private fun setRecyclerViewLisners(countrys: ArrayList<Country?>?) {
        if (recyclerTouchListener != null)
            recyclerview_country_list.removeOnItemTouchListener(recyclerTouchListener!!)

        recyclerTouchListener = RecyclerTouchListener(
            this@CountryListActivity,
            recyclerview_country_list,
            object : RecyclerTouchListener.ClickListener {
                override fun onClick(view: View, position: Int) {
                    val intent  = Intent()
                    intent.putExtra(AppConstants.IntentExtras.country, countrys?.get(position))
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }
                override fun onLongClick(view: View, position: Int) {
                }
            })
        recyclerview_country_list.addOnItemTouchListener(recyclerTouchListener!!)
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}
