package com.estrrado.vinner.data.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Datum {
    @SerializedName("product_id")
    @Expose
    var productId: String? = null

    @SerializedName("product_title")
    @Expose
    var productTitle: String? = null

    @SerializedName("qty")
    @Expose
    var qty: String? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("product_image")
    @Expose
    var productImage: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("shipping_operator_id")
    @Expose
    private var shippingOperatorId: String? = null

    @SerializedName("operator")
    @Expose
    private var operator: String? = null

    fun getShippingOperatorId(): String? {
        return shippingOperatorId
    }

    fun setShippingOperatorId(shippingOperatorId: String?) {
        this.shippingOperatorId = shippingOperatorId
    }

    fun getOperator(): String? {
        return operator
    }

    fun setOperator(operator: String?) {
        this.operator = operator
    }

    /**
     * industry list
     */
    @SerializedName("industry_id")
    @Expose
    private var industryId: String? = null

    @SerializedName("industry_name")
    @Expose
    private var industryName: String? = null

    @SerializedName("industry_image")
    @Expose
    private var industryImage: String? = null

    fun getIndustryId(): String? {
        return industryId
    }

    fun setIndustryId(industryId: String?) {
        this.industryId = industryId
    }

    fun getIndustryName(): String? {
        return industryName
    }

    fun setIndustryName(industryName: String?) {
        this.industryName = industryName
    }

    fun getIndustryImage(): String? {
        return industryImage
    }

    fun setIndustryImage(industryImage: String?) {
        this.industryImage = industryImage
    }

    /**
     * category list
     */
    @SerializedName("category_id")
    @Expose
    private var categoryId: String? = null

    @SerializedName("category_name")
    @Expose
    private var categoryName: String? = null

    @SerializedName("category_image")
    @Expose
    private var categoryImage: String? = null

    fun getCategoryId(): String? {
        return categoryId
    }

    fun setCategoryId(categoryId: String?) {
        this.categoryId = categoryId
    }

    fun getCategoryName(): String? {
        return categoryName
    }

    fun setCategoryName(categoryName: String?) {
        this.categoryName = categoryName
    }

    fun getCategoryImage(): String? {
        return categoryImage
    }

    fun setCategoryImage(categoryImage: String?) {
        this.categoryImage = categoryImage
    }

}