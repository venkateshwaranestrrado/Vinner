package com.estrrado.vinner.data.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {
    @SerializedName("product_id")
    @Expose
    private var productId: String? = null

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
    internal var price: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("product_image")
    @Expose
    private var productImage: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

    @SerializedName("category")
    @Expose
    private var category: String? = null

    fun getProductId(): String? {
        return productId
    }

    fun setProductId(productId: String?) {
        this.productId = productId
    }

    fun getPrice(): String? {
        return price
    }

    fun setPrice(price: String?) {
        this.price = price
    }

    fun getProductImage(): String? {
        return productImage
    }

    fun setProductImage(productImage: String?) {
        this.productImage = productImage
    }

    fun getCategory(): String? {
        return category
    }

    fun setCategory(category: String?) {
        this.category = category
    }
}