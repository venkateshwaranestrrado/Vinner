package com.estrrado.vinner.data.models

import com.bluelinelabs.logansquare.annotation.JsonField
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Address {
    @SerializedName("address_id")
    @Expose
    var addressId: String? = null

    @SerializedName("address_type")
    @Expose
    var addressType: String? = null

    @SerializedName("house_flat")
    @Expose
    var houseFlat: String? = null

    @SerializedName("zip")
    @Expose
    var zip: String? = null

    @SerializedName("road_name")
    @Expose
    var roadName: String? = null

    @SerializedName("landmark")
    @Expose
    var landmark: String? = null

    @SerializedName("default")
    @Expose
    var default: String? = null

    @SerializedName("city")
    @Expose
    val city: String? = null

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("country")
    @Expose
    val country: String? = null

}