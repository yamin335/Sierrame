package com.mmfinfotech.streameApp.util.retrofit

import com.google.gson.JsonObject

/**
 * Created by Somil Rawal on 10-07-2020.
 */
interface OnApiResponse {
    fun onSuccess(status:String?, mainObject: JsonObject?)
    fun onFailure()
}