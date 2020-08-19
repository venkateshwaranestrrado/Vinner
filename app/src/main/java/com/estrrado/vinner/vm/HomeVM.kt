package com.estrrado.vinner.vm

import androidx.lifecycle.ViewModel
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel

class HomeVM (var respo: VinnerRespository) : ViewModel() {
    fun getHomeList() = respo.privateGetHomeList()
    fun home(input: RequestModel) = respo.home(input)
}