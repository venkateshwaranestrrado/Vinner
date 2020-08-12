package com.estrrado.vinner.data.models.retrofit

import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.response.Model
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("username") username: String?,
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


    @POST("product")
    fun getHomeList(): Observable<Model>


}