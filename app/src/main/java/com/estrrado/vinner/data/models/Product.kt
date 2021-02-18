package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Product {

    @SerializedName("product_id")
    @Expose
    var productId: String? = null

    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("qty")
    @Expose
    var qty: String? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null

    @SerializedName("product_url")
    @Expose
    var product_url: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

    @SerializedName("return_policy")
    @Expose
    var return_policy: String? = null

    @SerializedName("reated_customers")
    @Expose
    var reatedCustomers: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("current_stock")
    @Expose
    var current_stock: String? = null

    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("product_image")
    @Expose
    val productImage: List<String>? = null

    @SerializedName("weight")
    @Expose
    var weight: String? = null

    @SerializedName("length")
    @Expose
    var length: String? = null

    @SerializedName("width")
    @Expose
    var width: String? = null

    @SerializedName("height")
    @Expose
    var height: String? = null

    @SerializedName("weight_unit")
    @Expose
    var weight_unit: String? = null

    @SerializedName("dim_unit")
    @Expose
    var dim_unit: String? = null

}