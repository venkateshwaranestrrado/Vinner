package com.estrrado.vinner.data.retrofit

import com.estrrado.vinner.data.models.response.Model
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
    ): Observable<Model>


}