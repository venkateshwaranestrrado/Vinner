package com.estrrado.vinner

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressModel
import com.estrrado.vinner.data.models.response.DataListModel
import com.estrrado.vinner.data.models.response.Model
import com.estrrado.vinner.helper.getMultipartString
import com.estrrado.vinner.retrofit.APIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VinnerRespository(var context: Context?, var apiService: APIService?) {

    companion object {
        @Volatile
        private var instance: VinnerRespository? = null

        @JvmStatic
        fun getInstance(context: Context?, apiService: APIService?) = instance
            ?: synchronized(this) {
                instance
                    ?: VinnerRespository(context, apiService).also {
                        instance = it
                    }
            }
    }

    @SuppressLint("CheckResult")
    fun register(input: Input): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.registerUser(
            input.username,
            input.email,
            input.mobile,
            input.password,
            input.confirm_password,
            input.c_code
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun login(input: Input): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.sendotp(
            input.mobile,
            input.c_code
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun verifyOTP(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.verifyOtp(
            input.phoneNumber,
            input.otp,
            input.countryCode,
            input.device_token,
            input.deviceId,
            input.deviceName,
            input.os
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun home(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.home(
            input.accessToken,
            input.countryCode
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun productDetail(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.productDetail(
            input.accessToken,
            input.countryCode,
            input.productId
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun getFeatureProductList(input: RequestModel): MutableLiveData<DataListModel?> {
        val data = MutableLiveData<DataListModel?>()
        apiService?.getFeatureProductList(
            input.accessToken,
            input.limit,
            input.offset,
            input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun getProductList(input: RequestModel): MutableLiveData<DataListModel?> {
        val data = MutableLiveData<DataListModel?>()
        apiService?.getProductList(
            input.accessToken,
            input.limit,
            input.offset,
            input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun getCartPage(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.cartPage(
            input.accessToken, input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun addCart(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.addCart(
            input.accessToken,
            input.countryCode,
            input.productId
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun updateCart(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.updateCart(
            input.accessToken,
            input.cartId,
            input.productId,
            input.productQty, input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun deleteCart(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.deleteCart(
            input.accessToken,
            input.cartId,
            input.productId, input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()
            })
        return data
    }

    @SuppressLint("CheckResult")
    fun emptyCart(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.emptyCart(
            input.accessToken,
            input.cartId, input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun shippingOperators(input: RequestModel): MutableLiveData<DataListModel?> {
        val data = MutableLiveData<DataListModel?>()
        apiService?.shippingOperators(
            input.accessToken
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun deliveryFee(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.deliveryFee(
            input.accessToken,
            input.operatorId,
            input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun signout(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.signout(
            input.accessToken
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun getCategory(input: RequestModel): MutableLiveData<DataListModel?> {
        val data = MutableLiveData<DataListModel?>()
        apiService?.getCategory(
            input.accessToken
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun getIndustries(input: RequestModel): MutableLiveData<DataListModel?> {
        val data = MutableLiveData<DataListModel?>()
        apiService?.getIndustry(
            input.accessToken
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun addAddress(input: RequestModel): MutableLiveData<DataListModel?> {
        val data = MutableLiveData<DataListModel?>()
        apiService?.addAddress(
            input.accessToken, input.address_type, input.house_flat,
            input.zipcode, input.road_name, input.landmark,
            input.default, input.country, input.city,
            input.name, input.contactNumber, input.email, input.buildingName
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun geteditAddress(input: RequestModel): MutableLiveData<DataListModel?> {
        val data = MutableLiveData<DataListModel?>()
        apiService?.addressedit(
            input.accessToken,
            input.address_type,
            input.address_id, input.house_flat, input.zipcode, input.road_name,
            input.landmark, input.default, input.country, input.city,
            input.name, input.contactNumber, input.email, input.buildingName
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun AddressList(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.addressList(
            input.accessToken
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun checkoutAddressList(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.checkoutAddressList(
            input.accessToken,
            input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun Profile(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.profile(
            input.accessToken
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun UpdateProfile(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.updateprofile(
            getMultipartString(input.accessToken.toString()),
            getMultipartString(input.name.toString()),
            getMultipartString(input.address1.toString()),
            getMultipartString(input.address2.toString()),
            getMultipartString(input.post.toString()),
            getMultipartString(input.state.toString()),
            getMultipartString(input.mobile.toString()),
            getMultipartString(input.email.toString()),
            getMultipartString(input.city.toString()),
            input.profile_pic
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun Category(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.category(
            input.accessToken, input.countryCode, input.category_id
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun BrowseIndustry(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.browseindustry(
            input.accessToken, input.countryCode, input.industry_id
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun Search(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.search(
            input.accessToken, input.countryCode, input.search
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun Addressdlte(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.addressdelete(
            input.accessToken, input.address_id
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun OrderList(input: RequestModel): MutableLiveData<DataListModel?> {
        val data = MutableLiveData<DataListModel?>()
        apiService?.orderlist(
            input.accessToken, input.search_date, input.search_orderId
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun Payment(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.payment(
            input.accessToken,
            input.payment_status,
            input.payment_method,
            input.operatorId,
            input.address_type,
            input.housename,
            input.road_name,
            input.landmark,
            input.pincode,
            input.country,
            input.city,
            input.name,
            input.phone,
            input.email,
            input.building,
            input.s_address_type,
            input.s_housename,
            input.s_road_name,
            input.s_landmark,
            input.s_pincode,
            input.s_country,
            input.s_city,
            input.s_name,
            input.s_phone,
            input.s_email,
            input.s_building,
            input.payment_details, input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()
                Log.e("Errro Msg ", it?.message ?: "")
            })
        return data
    }

    @SuppressLint("CheckResult")
    fun PaymentResponse(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.paymentResponse(
            input.accessToken,
            input.payment_status,
            input.payment_method,
            input.operatorId,
            input.address_type,
            input.housename,
            input.road_name,
            input.landmark,
            input.pincode,
            input.country,
            input.city,
            input.name,
            input.phone,
            input.email,
            input.building,
            input.s_address_type,
            input.s_housename,
            input.s_road_name,
            input.s_landmark,
            input.s_pincode,
            input.s_country,
            input.s_city,
            input.s_name,
            input.s_phone,
            input.s_email,
            input.s_building,
            input.merchant_reference,
            input.payment_details, input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()
                Log.e("Errro Msg ", it?.message ?: "")
            })
        return data
    }

    @SuppressLint("CheckResult")
    fun ReqService(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.reqService(
            input.accessToken,
            input.service_category,
            input.service_type,
            input.type_detail,
            input.name,
            input.address,
            input.city,
            input.country,
            input.email,
            input.mobile,
            input.date,
            input.time,
            input.remark
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun trackOrder(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.trackOrder(
            input.accessToken,
            input.order_id
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun notifications(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.notifications(
            input.accessToken
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()
            })
        return data
    }

    @SuppressLint("CheckResult")
    fun ReqDemo(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.reqDemo(
            input.accessToken,
            input.address,
            input.name,
            input.city,
            input.country,
            input.mobile,
            input.email,
            input.date,
            input.time,
            input.product_id,
            input.remarks
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun Countries(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.countries()!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun Demo(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.demoproduct(input.accessToken)!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun Servcecateg(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.service_category(input.accessToken)!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun ServceType(input: RequestModel): MutableLiveData<AddressModel?> {
        val data = MutableLiveData<AddressModel?>()
        apiService?.servicetype(input.accessToken)!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }


    @SuppressLint("CheckResult")
    fun Oderdtl(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.orderdetail(input.accessToken, input.order_id)!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun viewReview(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.viewReview(
            input.accessToken,
            input.review_id
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun addreview(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.saveReview(
            input.accessToken,
            input.productId,
            input.rating,
            input.title,
            input.review
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    @SuppressLint("CheckResult")
    fun getsdktoken(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.getsdktoken(
            input.accessToken,
            input.device_id,
            input.payment_status,
            input.payment_method,
            input.address_type,
            input.housename,
            input.road_name,
            input.landmark,
            input.pincode,
            input.country,
            input.city,
            input.name,
            input.phone,
            input.email,
            input.building,
            input.s_address_type,
            input.s_housename,
            input.s_road_name,
            input.s_landmark,
            input.s_pincode,
            input.s_country,
            input.s_city,
            input.s_name,
            input.s_phone,
            input.s_email,
            input.s_building,
            input.operator_id, input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()
            })
        return data
    }

    @SuppressLint("CheckResult")
    fun ChangeLocation(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService?.changeLocation(
            input.accessToken,
            input.countryCode
        )!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

}