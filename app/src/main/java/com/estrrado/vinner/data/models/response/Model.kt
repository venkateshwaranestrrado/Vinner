package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

data class Model (
    
    @field:SerializedName("data")
    @field:JsonField(name = arrayOf("data"))
    val data: Data? = null,


    @field:SerializedName("message")
    @field:JsonField(name = arrayOf("message"))
    val message: String? = null,


    @field:SerializedName("status")
    @field:JsonField(name = arrayOf("status"))
    val status: String? = null

)