package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

class AddressList (
    @field:SerializedName("adrs_id")
    @field:JsonField(name = arrayOf("adrs_id"))
    val adrs_id: String? = null,

    @field:SerializedName("address_type")
    @field:JsonField(name = arrayOf("address_type"))
    val address_type: String? = null,

    @field:SerializedName("house_flat")
    @field:JsonField(name = arrayOf("house_flat"))
    val house_flat: String? = null,

    @field:SerializedName("zip")
    @field:JsonField(name = arrayOf("zip"))
    val zip: String? = null ,

    @field:SerializedName("road_name")
    @field:JsonField(name = arrayOf("road_name"))
    val road_name: String? = null,

    @field:SerializedName("landmark")
    @field:JsonField(name = arrayOf("landmark"))
    val landmark: String? = null,

    @field:SerializedName("default")
    @field:JsonField(name = arrayOf("default"))
    val default: String? = null  ,



    @field:SerializedName("current_stock")
    @field:JsonField(name = arrayOf("current_stock"))
    var current_stock: String? = null,

    @field:SerializedName("product_id")
    @field:JsonField(name = arrayOf("product_id"))
    var product_id: String? = null,

    @field:SerializedName("product_title")
    @field:JsonField(name = arrayOf("product_title"))
    val product_title: String? = null,

    @field:SerializedName("qty")
    @field:JsonField(name = arrayOf("qty"))
    val qty: String? = null,

    @field:SerializedName("prd_qty")
    @field:JsonField(name = arrayOf("prd_qty"))
    val prd_qty: String? = null,

    @field:SerializedName("unit")
    @field:JsonField(name = arrayOf("unit"))
    val unit: String? = null ,

    @field:SerializedName("price")
    @field:JsonField(name = arrayOf("price"))
    val price : String? = null,

    @field:SerializedName("currency")
    @field:JsonField(name = arrayOf("currency"))
    val currency: String? = null,

    @field:SerializedName("product_image")
    @field:JsonField(name = arrayOf("product_image"))
    val product_image: String? = null ,

    @field:SerializedName("rating")
    @field:JsonField(name = arrayOf("rating"))
    val rating: String? = null,

    @field:SerializedName("category")
    @field:JsonField(name = arrayOf("category"))
    val category: String? = null,





    @field:SerializedName("sale_id")
    @field:JsonField(name = arrayOf("sale_id"))
    val sale_id: String? = null,

    @field:SerializedName("product_details")
    @field:JsonField(name = arrayOf("product_details"))
    val product_details: ArrayList<Productdtls?>? = null,

    @field:SerializedName("delivery_status")
    @field:JsonField(name = arrayOf("delivery_status"))
    val delivery_status: String? = null,

    @field:SerializedName("delivary_datetime")
    @field:JsonField(name = arrayOf("delivary_datetime"))
    val delivary_datetime: String? = null,

    @field:SerializedName("country_id")
    @field:JsonField(name = arrayOf("country_id"))
    val country_id: String? = null,

    @field:SerializedName("country_name")
    @field:JsonField(name = arrayOf("country_name"))
    val country_name: String? = null,

    @field:SerializedName("country_code")
    @field:JsonField(name = arrayOf("country_code"))
    val country_code: String? = null ,

    @field:SerializedName("service_category_id")
    @field:JsonField(name = arrayOf("service_category_id"))
    val service_category_id: String? = null,

    @field:SerializedName("service_category_name")
    @field:JsonField(name = arrayOf("service_category_name"))
    val service_category_name: String? = null,

    @field:SerializedName("service_type_id")
    @field:JsonField(name = arrayOf("service_type_id"))
    val service_type_id: String? = null,

    @field:SerializedName("service_type")
    @field:JsonField(name = arrayOf("service_type"))
    val service_type: String? = null





)