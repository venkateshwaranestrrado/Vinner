package com.estrrado.vinner.retrofit

enum class WebConfig {

    PUBLISH {
        override fun getLiveUrl() = "https://vinshopify.com/api/"
        override fun getImagUrl() = "http://13.126.102.0:7016/iMAGEStorage/"
    },

    TEST {
        override fun getLiveUrl() = "https://vinshopify.com/uat/api/"
        override fun getImagUrl() = "http://13.126.102.0:7016/iMAGEStorage/"
    };


    abstract fun getLiveUrl(): String
    abstract fun getImagUrl(): String
}