package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.google.gson.annotations.SerializedName

@JsonObject
data class AddressModel(

    @field:SerializedName("httpcode")
    @field:JsonField(name = arrayOf("httpcode"))
    val httpcode: Int? = null,

    @field:SerializedName("status")
    @field:JsonField(name = arrayOf("status"))
    val status: String? = null,

    @field:SerializedName("message")
    @field:JsonField(name = arrayOf("message"))
    val message: String? = null,

    @field:SerializedName("data")
    @field:JsonField(name = arrayOf("data"))
    val data: ArrayList<AddressList>? = null

)