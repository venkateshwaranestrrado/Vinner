package com.estrrado.vinner.vm

import androidx.lifecycle.ViewModel
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel

class HomeVM(var respo: VinnerRespository) : ViewModel() {
    fun getProductList(input: RequestModel) = respo.getProductList(input)
    fun getCartPage(input: RequestModel) = respo.getCartPage(input)
    fun addCart(input: RequestModel) = respo.addCart(input)
    fun home(input: RequestModel) = respo.home(input)
    fun productDetail(input: RequestModel) = respo.productDetail(input)
    fun updateCart(input: RequestModel) = respo.updateCart(input)
    fun deleteCart(input: RequestModel) = respo.deleteCart(input)
    fun emptyCart(input: RequestModel) = respo.emptyCart(input)
    fun shippingOperators(input: RequestModel) = respo.shippingOperators(input)
    fun deliveryFee(input: RequestModel) = respo.deliveryFee(input)
}