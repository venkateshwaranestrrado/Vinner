package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

class Productdetails(

    @field:SerializedName("id")
    @field:JsonField(name = arrayOf("id"))
    val id: String? = null,

    @field:SerializedName("name")
    @field:JsonField(name = arrayOf("name"))
    val name: String? = null,

    @field:SerializedName("image")
    @field:JsonField(name = arrayOf("image"))
    val image: String? = null,

    @field:SerializedName("rating")
    @field:JsonField(name = arrayOf("rating"))
    val rating: Int? = null,

    @field:SerializedName("price")
    @field:JsonField(name = arrayOf("price"))
    val price: String? = null,

    @field:SerializedName("qty")
    @field:JsonField(name = arrayOf("qty"))
    val qty: String? = null,

    @field:SerializedName("subtotal")
    @field:JsonField(name = arrayOf("subtotal"))
    val subtotal: String? = null

)