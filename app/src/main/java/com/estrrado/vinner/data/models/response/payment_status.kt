package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

class payment_status (
    @field:SerializedName("vendor")
    @field:JsonField(name = arrayOf("vendor"))
    val vendor: String? = null,

    @field:SerializedName("status")
@field:JsonField(name = arrayOf("status"))
val status: String? = null

)