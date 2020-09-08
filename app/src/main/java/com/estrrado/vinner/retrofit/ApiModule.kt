package com.estrrado.vinner.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object ApiModule {
    var apiData = WebConfig.TEST
    var LIVE_URL = apiData.getLiveUrl()
    var scheme: String = ""
        set(url) {
            field = url.toHttpUrlOrNull()!!.scheme
        }


    var host: String = ""
        set(url) {
            field = url.toHttpUrlOrNull()!!.host
        }


    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request()

                val newUrl: HttpUrl?
                newUrl = when {
                    scheme != null && host != null -> request.url.newBuilder()
                        .scheme(scheme)
                        .host(host)
                        .addQueryParameter("apikey", "something")
                        .build()
                    else -> request.url.newBuilder()
                        .addQueryParameter("apikey", "something")
                        .build()
                }

                it.proceed(
                    request.newBuilder()
                        .url(newUrl)
                        .build())

            }
            .build()!!


    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {

        val okHttpBuilder = okHttpClient.newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(httpLoggingInterceptor)


        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpBuilder.build())
            .baseUrl(LIVE_URL)

    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideExampleService(retrofit: Retrofit.Builder): APIService = retrofit.build().create(APIService::class.java)

}