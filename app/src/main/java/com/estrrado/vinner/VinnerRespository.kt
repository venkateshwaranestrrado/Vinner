package com.estrrado.vinner

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.response.Model
import com.estrrado.vinner.data.models.retrofit.APIService
import com.google.gson.JsonSyntaxException

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VinnerRespository(var context: FragmentActivity?, var apiService: APIService?) {
    fun privateGetHomeList(): LiveData<Model?> = getHomeList()


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

}