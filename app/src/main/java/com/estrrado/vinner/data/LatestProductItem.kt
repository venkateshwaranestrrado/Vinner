package com.estrrado.vinner.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

data class LatestProductItem(
    val title: String, val price: String, val image: Int,

    @field:SerializedName("product_image")
    @field:JsonField(name = arrayOf("product_image"))
    val path: String? = null,

    @field:SerializedName("product_id")
    @field:JsonField(name = arrayOf("product_id"))

    val productId: String? = null,

    @field:SerializedName("product_code")
    @field:JsonField(name = arrayOf("product_code"))

    val productcode: String? = null,

    @field:SerializedName("product_title")
    @field:JsonField(name = arrayOf("product_title"))

    val producttitle: String? = null,

    @field:SerializedName("product_model")
    @field:JsonField(name = arrayOf("product_model"))

    val productmodel: String? = null,

    @field:SerializedName("sale_price")
    @field:JsonField(name = arrayOf("sale_price"))

    val saleprice: String? = null,

    @field:SerializedName("category")
    @field:JsonField(name = arrayOf("category"))

    val category: String? = null




    )
