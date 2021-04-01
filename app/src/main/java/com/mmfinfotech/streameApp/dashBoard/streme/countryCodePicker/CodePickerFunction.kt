package com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.models.Country
import com.mmfinfotech.streameApp.util.getStringFromJson
import com.mmfinfotech.streameApp.utils.AppConstants
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 *
 */

fun getCountryCodes(context: Context?, assertName: String?): ArrayList<Country?>? {
    val returnCountryCodes: ArrayList<Country?>? = ArrayList()

    val asda: String? = loadJSONFromAsset(context, assertName)
    val jsonarray: JsonArray = JsonParser().parse(asda.toString()).asJsonArray
    for (i in 0 until jsonarray.size()) {
        val jsonObject: JsonObject? = jsonarray[i].asJsonObject
        val currencytSymbol: String? = getStringFromJson(
            jsonObject, "currencytSymbol",
            AppConstants.Defaults.string
        )
        val isoCode: String? =
            getStringFromJson(jsonObject, "isoCode", AppConstants.Defaults.string)
        val currencyCode: String? =
            getStringFromJson(jsonObject, "currencyCode", AppConstants.Defaults.string)
        val countryCode: String? =
            getStringFromJson(jsonObject, "countryCode", AppConstants.Defaults.string)
        val name: String? = getStringFromJson(jsonObject, "name", AppConstants.Defaults.string)
        returnCountryCodes?.add(Country(currencytSymbol, isoCode, currencyCode, countryCode, name))
    }
    return returnCountryCodes
}

private fun loadJSONFromAsset(context: Context?, assetName: String?): String? {
    var json: String? = ""
    try {
        val inputStream: InputStream? = context?.assets?.open(assetName!!)
        val size :Int? = inputStream?.available()
        val buffer : ByteArray? = ByteArray(size!!)
        inputStream.read(buffer!!)
        inputStream.close()
        json = String(buffer, Charset.forName(StandardCharsets.UTF_8.name()))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return json
    }
    return json
}

fun getCurrencySymbol(context: Context?, currencyCode: String?) : String? {
    var value :String? = AppConstants.Defaults.string
    getCountryCodes(context, AppConstants.AssertName.assertCountryCode)?.forEach { country: Country? ->
        if (currencyCode.equals(country?.currencyCode, ignoreCase = true)) {
            value = country?.currencytSymbol
            return value
        }
    }
    return value
}