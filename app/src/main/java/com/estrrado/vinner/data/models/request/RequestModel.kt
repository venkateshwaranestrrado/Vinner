package com.estrrado.vinner.data.models.request

import okhttp3.MultipartBody

class RequestModel(
    var phoneNumber: String? = null,
    var otpType: String? = null,
    var otp: String? = null
)