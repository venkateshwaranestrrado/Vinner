package com.estrrado.vinner.retrofit

enum class WebConfigPayFort {

    PUBLISH {
        override fun getLiveUrl() = "https://paymentservices.payfort.com/"
    },
    TEST {
        override fun getLiveUrl() = "https://sbpaymentservices.payfort.com/"
    };

    abstract fun getLiveUrl(): String
}