package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.SerializedName

class Shippingaddress(
    @field:SerializedName("s_address_type")
    @field:JsonField(name = arrayOf("s_address_type"))
    val s_address_type: String? = null,

    @field:SerializedName("s_house_flat")
    @field:JsonField(name = arrayOf("s_house_flat"))
    val s_house_flat: String? = null,

    @field:SerializedName("name")
    @field:JsonField(name = arrayOf("name"))
    val name: String? = null,

    @field:SerializedName("s_road_name")
    @field:JsonField(name = arrayOf("s_road_name"))
    val s_road_name: String? = null,

    @field:SerializedName("s_landmark")
    @field:JsonField(name = arrayOf("s_landmark"))
    val s_landmark: String? = null,

    @field:SerializedName("s_zip")
    @field:JsonField(name = arrayOf("s_zip"))
    val s_zip: String? = null,

    @field:SerializedName("s_country")
    @field:JsonField(name = arrayOf("s_country"))
    val s_country: String? = null,

    @field:SerializedName("s_phone")
    @field:JsonField(name = arrayOf("s_phone"))
    val s_phone: String? = null,

    @field:SerializedName("s_email")
    @field:JsonField(name = arrayOf("s_email"))
    val s_email: String? = null,

    @field:SerializedName("s_building")
    @field:JsonField(name = arrayOf("s_building"))
    val s_building: String? = null

)