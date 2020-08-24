package com.estrrado.vinner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.CartAdapter
import com.estrrado.vinner.data.models.Cart
import com.estrrado.vinner.data.models.CartItem
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Datum
import com.estrrado.vinner.data.retrofit.ApiClient
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_back.*

class CheckoutFragment : Fragment() {

    var vModel: HomeVM? = null
    var operatorId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(
                        activity,
                        ApiClient.apiServices!!
                    )
                )
            )
        ).get(HomeVM::class.java)
        val root = inflater.inflate(R.layout.fragment_checkout, container, false)
        operatorId = arguments?.getString(OPERATOR_ID)!!
        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageTitle.text = "Checkout"
        getDeleveryFee()
    }

    private fun getDeleveryFee() {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.operatorId = operatorId

        vModel!!.deliveryFee(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    txt_delivery_fee.text =
                        it!!.data!!.getDeliveryFee() + " " + it.data!!.getCurrency()
                    price.text = it!!.data!!.getPrice() + " " + it.data!!.getCurrency()
                    txt_sub_total.text = it.data!!.getSubTotal() + " " + it.data!!.getCurrency()
                    totalAmount.text = it.data!!.getTotalAmount() + " " + it.data!!.getCurrency()
                } else printToast(this!!.context!!, it?.message.toString())
            })
    }

}