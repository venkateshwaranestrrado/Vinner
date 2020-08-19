package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Regions {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

}