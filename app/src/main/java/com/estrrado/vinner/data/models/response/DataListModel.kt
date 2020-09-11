package com.estrrado.vinner.data.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DataListModel {
    @SerializedName("httpcode")
    @Expose
    var httpcode: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

}