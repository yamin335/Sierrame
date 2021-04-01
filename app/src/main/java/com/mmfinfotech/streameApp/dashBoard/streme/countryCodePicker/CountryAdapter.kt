package com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.models.Country

class CountryAdapter(val context: Context?,val toShow: Int , private val countryDTOS: ArrayList<Country?>?) : Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val currency : Int = 0
        const val code : Int = 1
    }
    private val itemCountryData:Int = 0
    private val itemNoData:Int = 1

    override fun getItemViewType(position: Int): Int {
        return if (countryDTOS?.get(position) != null) itemCountryData else itemNoData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == itemCountryData) {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_country, parent, false)
            return MyViewHolder(view)
        }else if (viewType == itemNoData){
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
            return NoDataViewHolder(view)
        }else {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.partial_item_row_no_data, parent, false)
            return NoDataViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val country: Country? = countryDTOS?.get(position)
            holder.countryname.text = country?.name
            val code = "+${country?.countryCode}"
            if (toShow == currency)
            holder.countrycode.text = country?.currencytSymbol.toString()
            else
            holder.countrycode.text = code

            Glide.with(context!!)
                .load(Uri.parse("file:///android_asset/flags/${country?.isoCode}.webp"))
                .into(holder.image)
        }else if (holder is NoDataViewHolder){

        }
    }

    override fun getItemCount(): Int {
        return countryDTOS?.size!!
    }

    inner class MyViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var countryname: TextView = view.findViewById(R.id.tv_item_row_country_name)
        var countrycode: TextView = view.findViewById(R.id.tv_item_row_country_code)
        var image: ImageView = view.findViewById(R.id.imgv_item_row_country_flag)
    }

    inner class NoDataViewHolder constructor(view: View) : RecyclerView.ViewHolder(view)

}