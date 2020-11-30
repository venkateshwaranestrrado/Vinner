package com.estrrado.vinner.helper

import android.app.Activity
import com.estrrado.vinner.data.RegionSpinner
import com.google.gson.Gson

fun readFromAsset(requireActivity: Activity): List<RegionSpinner> {
    val file_name = "login_region.json"
    val bufferReader = requireActivity.assets.open(file_name).bufferedReader()
    var json_string:String?=null
    json_string = bufferReader.use {
        it.readText()
    }
    val gson = Gson()
    val modelList: List<RegionSpinner> = gson.fromJson(
        json_string,
        Array<RegionSpinner>::class.java
    ).toList()
    return modelList
}