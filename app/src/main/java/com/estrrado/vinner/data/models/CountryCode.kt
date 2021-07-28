package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CountryCode {

    @SerializedName("country_code")
    @Expose
    var country_code: String? = null

}