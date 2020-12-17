package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ProductDetail {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

    @SerializedName("review_id")
    @Expose
    var reviewId: Int? = null

}