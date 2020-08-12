package com.estrrado.vinner.webService

enum class WebConfig {

    PUBLISH {
        override fun getLiveUrl() = "https://drssystem.co.uk/api/"
        override fun getStageUrl() = "https://drssystem.co.uk/uat/api/"
        override fun getLocalUrl() = "https://estrradodemo.com/drs/uat/api/"
    };

    abstract fun getLiveUrl()  : String
    abstract fun getStageUrl()  : String
    abstract fun getLocalUrl()  : String

}
