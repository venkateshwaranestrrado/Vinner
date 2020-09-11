package com.estrrado.vinner

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.estrrado.vinner.data.models.PayfortTokenResponse
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Model
import com.estrrado.vinner.data.models.response.DataListModel
import com.estrrado.vinner.retrofit.APIService

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VinnerRespository(var context: Context?, var apiService: APIService?) {

    companion object {
        @Volatile
        private var instance: VinnerRespository? = null

        @JvmStatic fun getInstance(context: Context?, apiService: APIService?) = instance
            ?: synchronized(this) {
                instance
                    ?: VinnerRespository(context, apiService).also {
                        instance = it
                    }
            }
    }

    fun register(input: Input): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.registerUser(
            input.username,
            input.email,
            input.mobile,
            input.password,
            input.confirm_password
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    fun login(input: Input): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.sendotp(
            input.mobile
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    fun verifyOTP(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.verifyOtp(
            input.phoneNumber,
            input.otp
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

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

    fun getCartPage(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
            apiService?.cartPage(
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

    fun updateCart(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
            apiService?.updateCart(
                input.accessToken,
                input.cartId,
                input.productId,
                input.productQty
            )!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        data.value = it
                }, {
                    it.printStackTrace()

                })
        return data
    }

    fun deleteCart(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
            apiService?.deleteCart(
                input.accessToken,
                input.cartId,
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

    fun emptyCart(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
            apiService?.emptyCart(
                input.accessToken,
                input.cartId
            )!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        data.value = it
                }, {
                    it.printStackTrace()

                })
        return data
    }

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

    fun deliveryFee(input: RequestModel): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
            apiService?.deliveryFee(
                input.accessToken,
                input.operatorId
            )!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        data.value = it
                }, {
                    it.printStackTrace()

                })
        return data
    }

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

}