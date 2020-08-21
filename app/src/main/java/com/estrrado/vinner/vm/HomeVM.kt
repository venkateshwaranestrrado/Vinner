package com.estrrado.vinner.vm

import androidx.lifecycle.ViewModel
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel

class HomeVM(var respo: VinnerRespository) : ViewModel() {
    fun getProductList(input: RequestModel) = respo.getProductList(input)
    fun home(input: RequestModel) = respo.home(input)
    fun productDetail(input: RequestModel) = respo.productDetail(input)
}