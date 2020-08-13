package com.estrrado.vinner.data.models.response

import com.estrrado.vinner.data.models.BannerSlider
import com.estrrado.vinner.data.models.Category
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Data {
    @SerializedName("logo")
    @Expose
    var logo: String? = null

    @SerializedName("banner_slider")
    @Expose
    var bannerSlider: List<BannerSlider>? = null

    @SerializedName("featured")
    @Expose
    var featured: Any? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null

}