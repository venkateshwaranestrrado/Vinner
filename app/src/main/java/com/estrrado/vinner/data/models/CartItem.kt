package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CartItem {
    @SerializedName("cart_item_id ")
    @Expose
    var cartItemId: String? = null

    @SerializedName("cart_id ")
    @Expose
    var cartId: String? = null

    @SerializedName("product_id ")
    @Expose
    var productId: String? = null

    @SerializedName("product_name ")
    @Expose
    var productName: String? = null

    @SerializedName("product_price")
    @Expose
    var productPrice: String? = null

    @SerializedName("product_quantity")
    @Expose
    var productQuantity: String? = null

    @SerializedName("product_total ")
    @Expose
    var productTotal: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null

    @SerializedName("product_image")
    @Expose
    var productImage: String? = null

    @SerializedName("delivery")
    @Expose
    var delivery: String? = null

}