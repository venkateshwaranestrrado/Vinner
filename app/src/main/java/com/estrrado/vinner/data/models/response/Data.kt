package com.estrrado.vinner.data.models.response

import com.estrrado.vinner.data.models.BannerSlider
import com.estrrado.vinner.data.models.Category
import com.estrrado.vinner.data.models.Featured
import com.estrrado.vinner.data.models.Region
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

    @SerializedName("product_id")
    @Expose
    private var productId: String? = null

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
    internal var price: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("product_image")
    @Expose
    private var productImage: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

    @SerializedName("category")
    @Expose
    private var category: String? = null

    fun getProductId(): String? {
        return productId
    }

    fun setProductId(productId: String?) {
        this.productId = productId
    }

    fun getPrice(): String? {
        return price
    }

    fun setPrice(price: String?) {
        this.price = price
    }

    fun getProductImage(): String? {
        return productImage
    }

    fun setProductImage(productImage: String?) {
        this.productImage = productImage
    }

    fun getCategory(): String? {
        return category
    }

    fun setCategory(category: String?) {
        this.category = category
    }

}