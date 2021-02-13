package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Cart {
    @SerializedName("total_amount")
    @Expose
    var totalAmount: String? = null

    @SerializedName("grand_total")
    @Expose
    var grandTotal: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

}