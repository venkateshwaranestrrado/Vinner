package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackOrdDetModel {

    @SerializedName("expt_to")
    @Expose
    var expt_to: String? = null

    @SerializedName("expt_from")
    @Expose
    var expt_from: String? = null

    @SerializedName("track_id")
    @Expose
    var track_id: String? = null

    @SerializedName("track_link")
    @Expose
    var track_link: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("order_id")
    @Expose
    var order_id: String? = null

    @SerializedName("order_date")
    @Expose
    var order_date: String? = null

    @SerializedName("d_status")
    @Expose
    var d_status: String? = null

    @SerializedName("ship_operator")
    @Expose
    var ship_operator: String? = null

    @SerializedName("products")
    @Expose
    var products = ArrayList<Products>()
}

class Products {
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("name")
    @Expose
    var name: String = ""

}