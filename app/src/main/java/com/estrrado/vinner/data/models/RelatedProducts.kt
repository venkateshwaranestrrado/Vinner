package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RelatedProducts {

    @SerializedName("product_id")
    @Expose
    var product_id: String? = null

    @SerializedName("product_title")
    @Expose
    var product_title: String? = null

    @SerializedName("prd_qty")
    @Expose
    var prd_qty: String? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("product_image")
    @Expose
    var product_image: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null
}
