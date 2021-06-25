package com.estrrado.vinner.retrofit

import com.estrrado.vinner.data.models.response.AddressModel
import com.estrrado.vinner.data.models.response.DataListModel
import com.estrrado.vinner.data.models.response.Model
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") username: String?,
        @Field("email") email: String?,
        @Field("mobile") mobile: String?,
        @Field("password") password: String?,
        @Field("confirm_password") confirm: String?,
        @Field("c_code") c_code: String?

    ): Observable<Model>

    @FormUrlEncoded
    @POST("sendotp")
    fun sendotp(
        @Field("mobile") mobile: String?,
        @Field("c_code") c_code: String?

    ): Observable<Model>

    @FormUrlEncoded
    @POST("verifyotp")
    fun verifyOtp(
        @Field("mobile") mobile: String?,
        @Field("otp") otp: String?,
        @Field("c_code") c_code: String?,
        @Field("device_token") devic_token: String?,
        @Field("os") os: String?

    ): Observable<Model>

    @FormUrlEncoded
    @POST("home")
    fun home(
        @Field("access_token") mobile: String?,
        @Field("country_code") otp: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("product")
    fun getProductList(
        @Field("access_token") accessToken: String?,
        @Field("limit") limit: Int?,
        @Field("offset") offset: Int?,
        @Field("country_code") countryCode: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("featured_product")
    fun getFeatureProductList(
        @Field("access_token") accessToken: String?,
        @Field("limit") limit: Int?,
        @Field("offset") offset: Int?,
        @Field("country_code") countryCode: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("product_detail")
    fun productDetail(
        @Field("access_token") accessToken: String?,
        @Field("country_code") countryCode: String?,
        @Field("product_id") productId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("cart_page")
    fun cartPage(
        @Field("access_token") accessToken: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("add_cart")
    fun addCart(
        @Field("access_token") accessToken: String?,
        @Field("country_code") countryCode: String?,
        @Field("product_id") productId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("update_cart")
    fun updateCart(
        @Field("access_token") accessToken: String?,
        @Field("cart_id") cartId: String?,
        @Field("product_id") productId: String?,
        @Field("product_qty") productQty: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("delete_cart")
    fun deleteCart(
        @Field("access_token") accessToken: String?,
        @Field("cart_id") cartId: String?,
        @Field("product_id") productId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("clear_cart")
    fun emptyCart(
        @Field("access_token") accessToken: String?,
        @Field("cart_id") cartId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("shipping_operators")
    fun shippingOperators(
        @Field("access_token") accessToken: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("delivery_fee")
    fun deliveryFee(
        @Field("access_token") accessToken: String?,
        @Field("operator_id") operatorId: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("signout")
    fun signout(
        @Field("access_token") accessToken: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("category")
    fun getCategory(
        @Field("access_token") accessToken: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("industry")
    fun getIndustry(
        @Field("access_token") accessToken: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("address_edit")
    fun addressedit(
        @Field("access_token") accessToken: String?,
        @Field("address_type") address_type: String?,
        @Field("address_id") address_id: String?,
        @Field("house_flat") house_flat: String?,
        @Field("zipcode") zipcode: String?,
        @Field("road_name") road_name: String?,
        @Field("landmark") landmark: String?,
        @Field("default") default: Int?,
        @Field("country_code") country_code: String?,
        @Field("city") city: String?,
        @Field("name") name: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("address")
    fun addAddress(
        @Field("access_token") accessToken: String?,
        @Field("address_type") address_type: String?,
        @Field("house_flat") house_flat: String?,
        @Field("zipcode") zipcode: String?,
        @Field("road_name") road_name: String?,
        @Field("landmark") landmark: String?,
        @Field("default") default: Int?,
        @Field("country_code") country_code: String?,
        @Field("city") city: String?,
        @Field("name") name: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("checkout_address_list")
    fun checkoutAddressList(
        @Field("access_token") accessToken: String?,
        @Field("country_code") countryCode: String?
    ): Observable<AddressModel>

    @FormUrlEncoded
    @POST("address_list")
    fun addressList(
        @Field("access_token") accessToken: String?
    ): Observable<AddressModel>

    @Multipart
    @POST("updateprofiledata")
    fun updateprofile(
        @Part("access_token") accessToken: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("address1") address1: RequestBody?,
        @Part("address2") address2: RequestBody?,
        @Part("post") post: RequestBody?,
        @Part("state") state: RequestBody?,
        @Part("mobile") mobile: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part profile_pic: MultipartBody.Part?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("profile")
    fun profile(
        @Field("access_token") accessToken: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("browse_category")
    fun category(
        @Field("access_token") accessToken: String?,
        @Field("country_code") countryCode: String?,
        @Field("category_id") category_id: String?
    ): Observable<AddressModel>

    @FormUrlEncoded
    @POST("browse_industry")
    fun browseindustry(
        @Field("access_token") accessToken: String?,
        @Field("country_code") countryCode: String?,
        @Field("industry_id") industry_id: String?
    ): Observable<AddressModel>

    @FormUrlEncoded
    @POST("search")
    fun search(
        @Field("access_token") accessToken: String?,
        @Field("country_code") countryCode: String?,
        @Field("search") search: String?
    ): Observable<AddressModel>


    @FormUrlEncoded
    @POST("address_delete")
    fun addressdelete(
        @Field("access_token") accessToken: String?,
        @Field("address_id") address_id: String?
    ): Observable<Model>


    @FormUrlEncoded
    @POST("order_list")
    fun orderlist(
        @Field("access_token") accessToken: String?,
        @Field("search_date") search_date: String?,
        @Field("search_orderId") search_orderId: String?
    ): Observable<DataListModel>

    @FormUrlEncoded
    @POST("demo_product")
    fun demoproduct(
        @Field("access_token") accessToken: String?

    ): Observable<AddressModel>

    @FormUrlEncoded
    @POST("service_category")
    fun service_category(
        @Field("access_token") accessToken: String?

    ): Observable<AddressModel>

    @FormUrlEncoded
    @POST("service_type")
    fun servicetype(
        @Field("access_token") accessToken: String?

    ): Observable<AddressModel>


    @FormUrlEncoded
    @POST("order_detail")
    fun orderdetail(
        @Field("access_token") accessToken: String?,
        @Field("order_id") order_id: String?


    ): Observable<Model>

    @FormUrlEncoded
    @POST("saveReview")
    fun saveReview(
        @Field("access_token") accessToken: String?,
        @Field("product_id") productId: String?,
        @Field("rating") rating: String?,
        @Field("title") title: String?,
        @Field("review") review: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("viewReview")
    fun viewReview(
        @Field("access_token") accessToken: String?,
        @Field("review_id") review_id: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("payment")
    fun payment(
        @Field("access_token") accessToken: String?,
        @Field("address_type") address_type: String?,
        @Field("housename") housename: String?,
        @Field("roadname") roadname: String?,
        @Field("landmark") landmark: String?,
        @Field("pincode") pincode: String?,
        @Field("payment_status") payment_status: String?,
        @Field("payment_method") payment_method: String?,
        @Field("operator_id") operator_id: String?,
        @Field("country_name") country_name: String?,
        @Field("city") city: String?,
        @Field("name") name: String?,
        @Field("payment_details") payment_details: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("reqService")
    fun reqService(
        @Field("access_token") accessToken: String?,
        @Field("service_category") service_category: String?,
        @Field("service_type") service_type: String?,
        @Field("type_detail") type_detail: String?,
        @Field("name") name: String?,
        @Field("address") address: String?,
        @Field("city") city: String?,
        @Field("country") country: String?,
        @Field("email") email: String?,
        @Field("mobile") mobile: String?,
        @Field("date") date: String?,
        @Field("time") time: String?,
        @Field("remark") remark: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("reqDemo")
    fun reqDemo(
        @Field("access_token") accessToken: String?,
        @Field("address") address: String?,
        @Field("name") name: String?,
        @Field("city") city: String?,
        @Field("country") country: String?,
        @Field("mobile") mobile: String?,
        @Field("email") email: String?,
        @Field("date") date: String?,
        @Field("time") time: String?,
        @Field("product_id") product_id: Int?,
        @Field("remarks") remarks: String?
    ): Observable<Model>

    @GET("countries")
    fun countries(): Observable<AddressModel>

    @FormUrlEncoded
    @POST("trackOrder")
    fun trackOrder(
        @Field("access_token") accessToken: String?,
        @Field("order_id") order_id: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("notifications")
    fun notifications(
        @Field("access_token") accessToken: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("payfortSdkToken")
    fun getsdktoken(
        @Field("access_token") access_token: String?,
        @Field("device_id") device_id: String?,
        @Field("address_type") address_type: String?,
        @Field("housename") housename: String?,
        @Field("roadname") roadname: String?,
        @Field("landmark") landmark: String?,
        @Field("pincode") pincode: String?,
        @Field("payment_status") payment_status: String?,
        @Field("payment_method") payment_method: String?,
        @Field("country_name") country_name: String?,
        @Field("city") city: String?,
        @Field("name") name: String?,
        @Field("operator_id") operator_id: String?
    ): Observable<Model>

    @FormUrlEncoded
    @POST("paymentResponse")
    fun paymentResponse(
        @Field("access_token") accessToken: String?,
        @Field("address_type") address_type: String?,
        @Field("housename") housename: String?,
        @Field("roadname") roadname: String?,
        @Field("landmark") landmark: String?,
        @Field("pincode") pincode: String?,
        @Field("payment_status") payment_status: String?,
        @Field("payment_method") payment_method: String?,
        @Field("operator_id") operator_id: String?,
        @Field("country_name") country_name: String?,
        @Field("city") city: String?,
        @Field("name") name: String?,
        @Field("merchant_reference") merchant_reference: String?,
        @Field("payment_details") payment_details: String?
    ): Observable<Model>


}