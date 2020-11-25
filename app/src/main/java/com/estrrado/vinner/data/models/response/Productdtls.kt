package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

class Productdtls (

    @field:SerializedName("name")
    @field:JsonField(name = arrayOf("name"))
    val name: String? = null,

    @field:SerializedName("image")
    @field:JsonField(name = arrayOf("image"))
    val image: String? = null ,

    @field:SerializedName("id")
    @field:JsonField(name = arrayOf("id"))
    val id: String? = null ,

    @field:SerializedName("rating")
    @field:JsonField(name = arrayOf("rating"))
    val rating: String? = null,

    @field:SerializedName("delivery_status")
    @field:JsonField(name = arrayOf("delivery_status"))
    var delivery_status: String? = null,

    @field:SerializedName("delivary_datetime")
    @field:JsonField(name = arrayOf("delivary_datetime"))
    var delivary_datetime: String? = null,

    @field:SerializedName("sale_id")
    @field:JsonField(name = arrayOf("sale_id"))
    var sale_id: String? = null
)