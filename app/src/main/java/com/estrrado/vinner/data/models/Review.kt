package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Review {
    @SerializedName("user")
    @Expose
    var user: String? = null

    @SerializedName("review_title")
    @Expose
    var reviewTitle: String? = null

    @SerializedName("review")
    @Expose
    var review: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

    @SerializedName("review_date")
    @Expose
    var reviewDate: String? = null

}