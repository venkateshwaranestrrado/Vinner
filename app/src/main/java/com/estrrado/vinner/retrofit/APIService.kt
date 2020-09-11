package com.estrrado.vinner.retrofit

import com.estrrado.vinner.data.models.response.Model
import com.estrrado.vinner.data.models.response.DataListModel
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") username: String?,
        @Field("email") email: String?,
        @Field("mobile") mobile: String?,
        @Field("password") password: String?,
        @Field("confirm_password") confirm: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("sendotp")
    fun sendotp(
        @Field("mobile") mobile: String?

    ): Observable<Model>

    @FormUrlEncoded
    @POST("verifyotp")
    fun verifyOtp(
        @Field("mobile") mobile: String?,
        @Field("otp") otp: String?

    ): Observable<Model>

    @FormUrlEncoded
    @POST("home")
    fun home(
        @Field("access_token") mobile: String?,
        @Field("country_code") otp: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("product")
    fun getProductList(
        @Field("access_token") accessToken: String?,
        @Field("limit") limit: Int?,
        @Field("offset") offset: Int?,
        @Field("country_code") countryCode: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("product_detail")
    fun productDetail(
        @Field("access_token") accessToken: String?,
        @Field("country_code") countryCode: String?,
        @Field("product_id") productId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("cart_page")
    fun cartPage(
        @Field("access_token") accessToken: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("add_cart")
    fun addCart(
        @Field("access_token") accessToken: String?,
        @Field("country_code") countryCode: String?,
        @Field("product_id") productId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("update_cart")
    fun updateCart(
        @Field("access_token") accessToken: String?,
        @Field("cart_id") cartId: String?,
        @Field("product_id") productId: String?,
        @Field("product_qty") productQty: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("delete_cart")
    fun deleteCart(
        @Field("access_token") accessToken: String?,
        @Field("cart_id") cartId: String?,
        @Field("product_id") productId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("empty_cart")
    fun emptyCart(
        @Field("access_token") accessToken: String?,
        @Field("cart_id") cartId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("shipping_operators")
    fun shippingOperators(
        @Field("access_token") accessToken: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("delivery_fee")
    fun deliveryFee(
        @Field("access_token") accessToken: String?,
        @Field("operator_id") operatorId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("signout")
    fun signout(
        @Field("access_token") accessToken: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("category")
    fun getCategory(
        @Field("access_token") accessToken: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("industry")
    fun getIndustry(
        @Field("access_token") accessToken: String?
    ): Observable<DataListModel>
    
}