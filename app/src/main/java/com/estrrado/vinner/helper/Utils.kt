package com.estrrado.vinner.helper

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.estrrado.vinner.data.RegionSpinner
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.URISyntaxException
import java.text.DecimalFormat

fun readFromAsset(requireActivity: Activity): List<RegionSpinner> {
    val file_name = "login_region.json"
    val bufferReader = requireActivity.assets.open(file_name).bufferedReader()
    var json_string: String? = null
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

fun getMultipartString(descriptionString: String): RequestBody {
    return RequestBody.create(
        MultipartBody.FORM, descriptionString
    )
}

fun getMultipartImage(
    fileUri: Uri,
    imageName: String,
    activity: FragmentActivity?
): MultipartBody.Part? {

    var filepath: String? = null
    try {
        filepath =
            ContentUriUtils.getFilePath(activity!!.applicationContext, fileUri!!)
    } catch (e: URISyntaxException) {
        e.printStackTrace()
    }

    val file = File(filepath)
    val requestFile =
        file.asRequestBody(
            activity!!.contentResolver.getType(fileUri)?.toMediaTypeOrNull()
        )
    return MultipartBody.Part.createFormData(imageName, file.name, requestFile)
}

fun priceFormat(value: String?): String {
    value?.let {
        if (it != "") {
            val price = it.replace(",", "")
            return DecimalFormat("#,###.00").format(price.toDouble())
        }
    }
    return "0.00"
}
