package com.estrrado.vinner

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Model
import com.estrrado.vinner.data.models.retrofit.APIService
import com.estrrado.vinner.data.models.retrofit.ApiClient

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

    fun privateGetHomeList(): LiveData<Model?> = getHomeList()


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

    fun home(): MutableLiveData<Model?> {
        val data = MutableLiveData<Model?>()
        apiService!!.home()?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ApiClient.apiServices
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }

    private fun getHomeList(): MutableLiveData<Model?> {
        var data = MutableLiveData<Model?>()


            apiService?.getHomeList()!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                        data.value = it



                }, {
                    it.printStackTrace()

                })
        return data
    }

}