package com.estrrado.vinner.data.models.request

class RequestModel(
    var phoneNumber: String? = null,
    var otpType: String? = null,
    var otp: String? = null,
    var accessToken: String? = null,
    var countryCode: String? = null,
    var productId: String? = null,
    var cartId: String? = null,
    var productQty: String? = null,
    var operatorId: String? = null,
    var serviceCommand: String? = null,
    var accessCode: String? = null,
    var merchantIdentifier: String? = null,
    var language: String? = null,
    var deviceId: String? = null,
    var signature: String? = null,
    var limit: Int? = 0,
    var offset: Int? = 0
)