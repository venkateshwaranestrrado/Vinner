package com.estrrado.vinner

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Model
import com.estrrado.vinner.data.models.response.ProductsModel
import com.estrrado.vinner.data.retrofit.APIService
import com.estrrado.vinner.data.retrofit.ApiClient

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VinnerRespository(var context: FragmentActivity?, var apiService: APIService?) {

    companion object {
        @Volatile
        private var instance: VinnerRespository? = null

        fun getInstance(context: FragmentActivity?, apiService: APIService?) = instance
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
            ?.subscribe({ApiClient.apiServices
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
            ?.subscribe({ApiClient.apiServices
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
            ?.subscribe({ApiClient.apiServices
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
            ?.subscribe({ApiClient.apiServices
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
            ?.subscribe({ApiClient.apiServices
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    fun getProductList(input: RequestModel): MutableLiveData<ProductsModel?> {
        val data = MutableLiveData<ProductsModel?>()
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

}