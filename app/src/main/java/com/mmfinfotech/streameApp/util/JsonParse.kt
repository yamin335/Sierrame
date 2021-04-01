package com.mmfinfotech.streameApp.util

import android.text.TextUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * Created by Somil Rawal on 21-09-2019.
 */

/**
 * get String from JsonObject
 *
 * @param object is JsonObject
 * @param key is the required key to get
 * @param def is the default value to return
 * @return  if object is null, return defaultValue
 * if key is null or empty, return defaultValue
 */
fun getStringFromJson(`object`: JsonObject?, key: String?, def: String): String {
    return if (`object`?.has(key) == true) if (`object`.get(key).isJsonNull) def else `object`.get(key).asString else def
}


fun getJsonObjFromJson(`object`: JsonObject?, key: String?, def: JsonObject?): JsonObject? {
    return if (`object`?.has(key) == true) if (`object`.get(key).isJsonNull) def else `object`.get(key).asJsonObject else def
}

fun getJsonObjFromJson(`object`: JsonArray?, index: Int?, def: JsonObject?): JsonObject? {
    return if (`object`?.size()!! > 0 ) if (`object`.get(index!!).isJsonNull) def
    else if (`object`.get(index).isJsonObject) `object`.get(index).asJsonObject
    else def
    else def
}

fun getJsonArrayFromJson(`object`: JsonObject?, key: String?, def: JsonArray?):JsonArray?{
    return if (`object`?.has(key) == true) if (`object`.get(key).isJsonNull) def else `object`.get(key).asJsonArray else def
}

/**
 * get Int from JsonObject
 *
 * @param object is JsonObject
 * @param key is the required key to get
 * @param def is the default value to return
 * @return  if object is null, return defaultValue
 * if key is null or empty, return defaultValue
 */
fun getIntFromJson(
    `object`: JsonObject?,
    key: String?,
    def: Int
): Int {
    return if (`object`?.has(key) == true) if (`object`.get(key).isJsonNull) def else `object`.get(key).asInt else def
}

fun getFloatFromJson(`object`: JsonObject?, key: String?, def: Float): Float {
    return if (`object`?.has(key) == true) if (`object`.get(key).isJsonNull) def else `object`.get(key).asFloat else def
}
