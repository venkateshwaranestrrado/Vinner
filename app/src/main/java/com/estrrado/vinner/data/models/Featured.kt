package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Featured {
    @SerializedName("prd_id")
    @Expose
    var prdId: String? = null

    @SerializedName("prd_name")
    @Expose
    var prdName: String? = null

    @SerializedName("prd_image")
    @Expose
    var prdImage: String? = null

    @SerializedName("qty")
    @Expose
    var qty: String? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

}