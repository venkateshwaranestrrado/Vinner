package com.estrrado.vinner.data.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Datum {
    @SerializedName("product_id")
    @Expose
    var productId: String? = null

    @SerializedName("product_title")
    @Expose
    var productTitle: String? = null

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

    @SerializedName("product_image")
    @Expose
    var productImage: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

    @SerializedName("category")
    @Expose
    var category: String? = null

}