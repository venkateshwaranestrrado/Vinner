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


}