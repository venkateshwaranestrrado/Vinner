package com.estrrado.vinner.webService

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

//    @GET("customer/version")
//    fun getVersion(): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/validatemobile")
//    fun validateMobile(
//        @Field("phone_number") phone_number: String?,
//        @Field("country_code") country_code: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/forgot_password")
//    fun forgotPassword(
//        @Field("country_code") country_code: String?,
//        @Field("phone_number") phone_number: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/reset_password")
//    fun resetPassword(
//        @Field("country_code") country_code: String?,
//        @Field("phone_number") phone_number: String?,
//        @Field("otp_token") otp_token: String?,
//        @Field("password") password: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/resend_otp")
//    fun resendOtp(
//        @Field("phone_number") phone_number: String?,
//        @Field("country_code") country_code: String?,
//        @Field("otp_type") otp_type: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/otp_verify")
//    fun verifyOtp(
//        @Field("country_code") country_code: String?,
//        @Field("phone_number") phone_number: String?,
//        @Field("otp") otp: String?,
//        @Field("otp_type") otp_type: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/signup")
//    fun signUp(
//        @Field("country_code") countryCode: String?,
//        @Field("phone_number") phoneNumber: String?,
//        @Field("customer_name") customerName: String?,
//        @Field("password") password: String?,
//        @Field("referral_id") referlId: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/signin")
//    fun signIn(
//        @Field("phone_number") phoneNumber: String?,
//        @Field("country_code") countryCode: String?,
//        @Field("password") password: String?,
//        @Field("device_token") deviceToken: String?,
//        @Field("os") os: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/our_merchants")
//    fun ourMerchant(
//        @Field("access_token") accessToken: String?,
//        @Field("latitude") lattitude: String?,
//        @Field("longitude") longitude: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/merchant_list")
//    fun merchantList(
//        @Field("access_token") accessToken: String?,
//        @Field("category") category: String?,
//        @Field("search_key") searchKey: String?,
//        @Field("latitude") lattitude: String?,
//        @Field("longitude") longitude: String?,
//        @Field("offset") offset: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/merchant_view")
//    fun merchantDetail(
//        @Field("access_token") accessToken: String?,
//        @Field("merchant_id") merchantId: String?,
//        @Field("latitude") lattitude: String?,
//        @Field("longitude") longitude: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/signout")
//    fun signOut(@Field("access_token") accessToken: String?): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/wallet/topup_page")
//    fun topupPage(@Field("access_token") accessToken: String?): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/wallet/topup_payment")
//    fun topUpPayment(
//        @Field("access_token") accessToken: String?,
//        @Field("amount") amount: String?,
//        @Field("payment_type") payType: String?,
//        @Field("bank_receipt") bankReceipt: MultipartBody.Part?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/voucher/topup_page")
//    fun voucherPage(@Field("access_token") accessToken: String?): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/voucher/topup_payment")
//    fun topUpVoucher(
//        @Field("access_token") accessToken: String?,
//        @Field("voucher_id") voucherId: Int?,
//        @Field("payment_type") payType: String?,
//        @Field("bank_receipt") bankReceipt: MultipartBody.Part?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/dashboard")
//    fun dashboard(@Field("access_token") accessToken: String?): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/settingpage")
//    fun settings(@Field("access_token") accessToken: String?): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/account")
//    fun accountSettings(@Field("access_token") accessToken: String?): Observable<ResponseModel>
//
//    @GET("customer/countries")
//    fun countries(): Observable<ResponseModel>
//
//    @Multipart
//    @POST("customer/account/update/profile_image")
//    fun updateProfilePic(
//        @Part("access_token") accessToken: RequestBody?,
//        @Part profile_img: MultipartBody.Part?
//    ): Call<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/account/update/general_info")
//    fun updateGeneralInfo(
//        @Field("access_token") accessToken: String?,
//        @Field("first_name") firstName: String?,
//        @Field("last_name") lastName: String?,
//        @Field("password") password: String?,
//        @Field("age") age: String?,
//        @Field("gender") gender: String?,
//        @Field("email") email: String?,
//        @Field("id_no") id_no: String?,
//        @Field("address") address: String?,
//        @Field("country") country: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/account/update/bank_info")
//    fun updateBankInfo(
//        @Field("access_token") accessToken: String?,
//        @Field("bank_name") bankName: String?,
//        @Field("account_holder") accountHolder: String?,
//        @Field("account_number") accountNumber: String?,
//        @Field("swift_code") swiftCode: String?,
//        @Field("bank_address") bankAddress: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/wallet/history")
//    fun walletHistory(
//        @Field("access_token") accessToken: String?,
//        @Field("search_key") searchKey: String?,
//        @Field("last_date") lastDate: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/voucher/history")
//    fun voucherHistory(
//        @Field("access_token") accessToken: String?,
//        @Field("search_key") searchKey: String?,
//        @Field("last_date") lastDate: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/merchant/qrscan")
//    fun qrCodeDetail(
//        @Field("access_token") accessToken: String?,
//        @Field("qr_code") qrCode: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/merchant/qrscan")
//    fun qrCodeDetails(
//        @Field("access_token") accessToken: String?,
//        @Field("qr_code") qrCode: String?
//    ): Call<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/merchant/payment")
//    fun payment(
//        @Field("access_token") accessToken: String?,
//        @Field("merchant_id") merchantId: String?,
//        @Field("payment_pin") paymentPin: String?,
//        @Field("amount") amount: String?,
//        @Field("payment_mode") paymentMode: String?,
//        @Field("used_vp") usedVP: String?,
//        @Field("profit_share") profitShare: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/referal/withdrawal_page")
//    fun withdrawalPage(
//        @Field("access_token") accessToken: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/referal/withdrawal_payment")
//    fun withDrawPayment(
//        @Field("access_token") accessToken: String?,
//        @Field("amount") amount: String?,
//        @Field("withdraw_type") withdrawType: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/referal/invite")
//    fun invite(
//        @Field("access_token") accessToken: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/merchant/payment/history")
//    fun paymentHistory(
//        @Field("access_token") accessToken: String?,
//        @Field("search_key") searchKey: String?,
//        @Field("filter") filter: String?,
//        @Field("last_date") lastDate: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/referal/withdrawal_history")
//    fun withdrawalHistory(
//        @Field("access_token") accessToken: String?,
//        @Field("search_key") searchKey: String?,
//        @Field("last_date") lastDate: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/promotions/view")
//    fun promoView(
//        @Field("access_token") accessToken: String?,
//        @Field("id") id: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/assistant/view")
//    fun assistantView(
//        @Field("access_token") accessToken: String?,
//        @Field("id") id: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/referal/rewards")
//    fun rewards(
//        @Field("access_token") accessToken: String?,
//        @Field("offset") offset: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/merchant/review/view")
//    fun review(
//        @Field("access_token") accessToken: String?,
//        @Field("merchant_id") merchantId: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/merchant/review/update")
//    fun reviewUpdate(
//        @Field("access_token") accessToken: String?,
//        @Field("merchant_id") merchantId: String?,
//        @Field("price_range_id") priceRangeId: String?,
//        @Field("rating") rating: String?,
//        @Field("review") review: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("support/list")
//    fun supportList(
//        @Field("access_token") accessToken: String?,
//        @Field("offset") offset: String?,
//        @Field("search_key") searchKey: String?
//    ): Observable<ResponseModel>
//
//    @Multipart
//    @POST("support/create1")
//    fun supportCreate(
//        @Part("access_token") accessToken: RequestBody?,
//        @Part("chat_msg") chatMsg: RequestBody?,
//        @Part file: MultipartBody.Part?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("support/message")
//    fun supportMessageList(
//        @Field("access_token") accessToken: String?,
//        @Field("chat_id") chatId: String?
//    ): Observable<ResponseModel>
//
//    @Multipart
//    @POST("support/send/message1")
//    fun supportSendMessage(
//        @Part("access_token") accessToken: RequestBody?,
//        @Part("chat_id") chatId: RequestBody?,
//        @Part("chat_msg") chatMsg: RequestBody?,
//        @Part file: MultipartBody.Part?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("support/delete")
//    fun supportDelete(
//        @Field("access_token") accessToken: String?,
//        @Field("chat_id") chatId: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/notificatinos")
//    fun notifications(
//        @Field("access_token") accessToken: String?,
//        @Field("search_key") searchKey: String?,
//        @Field("offset") offset: String?
//    ): Observable<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("customer/news")
//    fun news(
//        @Field("access_token") accessToken: String?,
//        @Field("id") id: String?
//    ): Observable<ResponseModel>
}