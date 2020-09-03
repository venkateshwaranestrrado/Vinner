package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PayfortTokenResponse {
    @SerializedName("access_code")
    @Expose
    var accessCode: String? = null

    @SerializedName("device_id")
    @Expose
    var deviceId: String? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("merchant_identifier")
    @Expose
    var merchantIdentifier: String? = null

    @SerializedName("response_code")
    @Expose
    var responseCode: String? = null

    @SerializedName("response_message")
    @Expose
    var responseMessage: String? = null

    @SerializedName("sdk_token")
    @Expose
    var sdkToken: String? = null

    @SerializedName("service_command")
    @Expose
    var serviceCommand: String? = null

    @SerializedName("signature")
    @Expose
    var signature: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

}