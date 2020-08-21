package com.estrrado.vinner.data.models.response

import com.estrrado.vinner.data.models.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Data {
    @SerializedName("logo")
    @Expose
    var logo: String? = null

    @SerializedName("banner_slider")
    @Expose
    var bannerSlider: List<BannerSlider>? = null

    @SerializedName("featured")
    @Expose
    var featured: List<Featured>? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null

    @SerializedName("regions")
    @Expose
    private val regions: List<Region>? = null

    @SerializedName("access_token")
    @Expose
    private var accessToken: String? = null

    @SerializedName("username")
    @Expose
    private var username: String? = null

    @SerializedName("email")
    @Expose
    private var email: String? = null

    @SerializedName("mobile")
    @Expose
    private var mobile: String? = null

    @SerializedName("redirect")
    @Expose
    private var redirect: String? = null

    fun getAccessToken(): String? {
        return accessToken
    }

    fun setAccessToken(accessToken: String?) {
        this.accessToken = accessToken
    }

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getMobile(): String? {
        return mobile
    }

    fun setMobile(mobile: String?) {
        this.mobile = mobile
    }

    fun getRedirect(): String? {
        return redirect
    }

    fun setRedirect(redirect: String?) {
        this.redirect = redirect
    }

    @SerializedName("product")
    @Expose
    private var product: Product? = null

    @SerializedName("reviews")
    @Expose
    private var reviews: List<Review?>? = null

    @SerializedName("related_products")
    @Expose
    private var relatedProducts: List<Any?>? = null

    fun getProduct(): Product? {
        return product
    }

    fun setProduct(product: Product?) {
        this.product = product
    }

    fun getReviews(): List<Review?>? {
        return reviews
    }

    fun setReviews(reviews: List<Review?>?) {
        this.reviews = reviews
    }

    fun getRelatedProducts(): List<Any?>? {
        return relatedProducts
    }

    fun setRelatedProducts(relatedProducts: List<Any?>?) {
        this.relatedProducts = relatedProducts
    }


}