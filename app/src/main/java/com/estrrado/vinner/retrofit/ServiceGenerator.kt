package com.estrrado.vinner.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceGenerator {
    var apiData = WebConfig.PUBLISH
    var apiBaseUrl = apiData.getLiveUrl()

    var apiPayFortData = WebConfigPayFort.TEST
    var apiBaseUrlPayFort = apiPayFortData.getLiveUrl()
    private val retrofit: Retrofit? = null
    private var builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(apiBaseUrl)
    private val httpClient = OkHttpClient.Builder()
    fun changeApiBaseUrl() {
        apiBaseUrl = apiBaseUrlPayFort
        builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(apiBaseUrl)
    }
}