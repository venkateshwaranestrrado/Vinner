package com.estrrado.vinner.data.models.response

import com.estrrado.vinner.data.models.BannerSlider
import com.estrrado.vinner.data.models.Category
import com.estrrado.vinner.data.models.Featured
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

}