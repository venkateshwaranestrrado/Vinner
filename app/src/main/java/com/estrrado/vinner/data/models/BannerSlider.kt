package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BannerSlider {
    @SerializedName("slider_id")
    @Expose
    var sliderId: String? = null

    @SerializedName("slider_name")
    @Expose
    var sliderName: String? = null

    @SerializedName("slider_image")
    @Expose
    var sliderImage: String? = null

}
