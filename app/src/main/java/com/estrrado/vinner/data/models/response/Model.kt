package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.estrrado.vinner.data.models.Datum
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Model(

    @field:SerializedName("data")
    @field:JsonField(name = arrayOf("data"))
    val data: Data? = null,

    @field:SerializedName("user_status")
    @field:JsonField(name = arrayOf("user_status"))
    val userstatus: String? = null,

    @field:SerializedName("message")
    @field:JsonField(name = arrayOf("message"))
    val message: String? = null,

    @field:SerializedName("status")
    @field:JsonField(name = arrayOf("status"))
    val status: String? = null,

    @field:SerializedName("httpcode")
    @field:JsonField(name = arrayOf("httpcode"))
    val httpcode: Int? = 0

)