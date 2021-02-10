package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

class Billingaddress(

    @field:SerializedName("address_type")
    @field:JsonField(name = arrayOf("address_type"))
    val address_type: String? = null,

    @field:SerializedName("house_flat")
    @field:JsonField(name = arrayOf("house_flat"))
    val house_flat: String? = null,

    @field:SerializedName("road_name")
    @field:JsonField(name = arrayOf("road_name"))
    val road_name: String? = null,

    @field:SerializedName("landmark")
    @field:JsonField(name = arrayOf("landmark"))
    val landmark: String? = null,

    @field:SerializedName("zip")
    @field:JsonField(name = arrayOf("zip"))
    val zip: String? = null,

    @field:SerializedName("country")
    @field:JsonField(name = arrayOf("country"))
    val country: String? = null
)