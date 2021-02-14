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

    class MyReview {

        @SerializedName("review_id")
        @Expose
        var review_id: String? = null

        @SerializedName("user_id")
        @Expose
        var user_id: String? = null

        @SerializedName("product_id")
        @Expose
        var product_id: String? = null

        @SerializedName("review_title")
        @Expose
        var review_title: String? = null

        @SerializedName("review")
        @Expose
        var review: String? = null

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("review_date")
        @Expose
        var review_date: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

    }

}