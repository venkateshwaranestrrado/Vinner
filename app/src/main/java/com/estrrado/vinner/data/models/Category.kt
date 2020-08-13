package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Category {
    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null

    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null

    @SerializedName("category_image")
    @Expose
    var categoryImage: String? = null

}