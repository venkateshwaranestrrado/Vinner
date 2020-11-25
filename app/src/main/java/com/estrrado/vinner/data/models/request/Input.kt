package com.estrrado.vinner.data.models.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

class Input(

    @field:SerializedName("username")
    @field:JsonField(name = arrayOf("username"))
    val username: String? = null,

    @field:SerializedName("email")
    @field:JsonField(name = arrayOf("email"))
    val email: String? = null,

    @field:SerializedName("mobile")
    @field:JsonField(name = arrayOf("mobile"))
    val mobile: String? = null,

    @field:SerializedName("password")
    @field:JsonField(name = arrayOf("password"))
    val password: String? = null,

    @field:SerializedName("confirm_password")
    @field:JsonField(name = arrayOf("confirm_password"))
    val confirm_password: String? = null,

    @field:SerializedName("status")
    @field:JsonField(name = arrayOf("status"))
    val status: String? = null ,

    @field:SerializedName("c_code")
    @field:JsonField(name = arrayOf("c_code"))
    val c_code: String? = null

)




