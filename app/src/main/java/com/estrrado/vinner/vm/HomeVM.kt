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
    fun signout(input: RequestModel) = respo.signout(input)
    fun getCategory(input: RequestModel) = respo.getCategory(input)
    fun getIndustries(input: RequestModel) = respo.getIndustries(input)
    fun addAddress(input: RequestModel) = respo.addAddress(input)
    fun getEaddress(input: RequestModel) = respo.geteditAddress(input)
    fun getaddresslist(input: RequestModel) = respo.AddressList(input)
    fun getProfile(input: RequestModel) = respo.Profile(input)
    fun getUpdatedProfile(input: RequestModel) = respo.UpdateProfile(input)
    fun getCategorylist(input: RequestModel) = respo.Category(input)
    fun getIndustrylist(input: RequestModel) = respo.BrowseIndustry(input)
    fun getAddressdlte(input: RequestModel) = respo.Addressdlte(input)
    fun getOrderList(input: RequestModel) = respo.OrderList(input)
    fun getcountries(input: RequestModel) = respo.Countries(input)
    fun getdemo(input: RequestModel) = respo.Demo(input)
    fun PaymentStatus(input: RequestModel) = respo.Payment(input)
    fun getservicecateg(input: RequestModel) = respo.Servcecateg(input)
    fun getserviceType(input: RequestModel) = respo.ServceType(input)
    fun getreqservice(input: RequestModel) = respo.ReqService(input)
    fun getreqdemo(input: RequestModel) = respo.ReqDemo(input)
    fun getsearch(input: RequestModel) = respo.Search(input)
    fun getorderdetail(input: RequestModel) = respo.Oderdtl(input)
    fun getreviewadd(input: RequestModel) = respo.addreview(input)
}