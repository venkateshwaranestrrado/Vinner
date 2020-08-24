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

class CartFragment : Fragment(), CartadapterCallBack {

    var vModel: HomeVM? = null
    var cartId: String? = null
    var cartAdapter: CartAdapter? = null
    var cartItems: ArrayList<CartItem?>? = null
    var currency: String? = null
    var operators: List<Datum>? = null

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
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).close()
        Glide.with(this!!.activity!!)
            .load(logo)
            .thumbnail(0.1f)
            .into(img_logo)
        Helper.setLocation(spnr_region, this!!.context!!)
        initControl()
        getCart()
        getOperators()

        spinner_operators.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getDeleveryFee(position)
            }

        }
    }

    private fun getDeleveryFee(position: Int) {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.operatorId = operators!!.get(position).getShippingOperatorId()

        vModel!!.deliveryFee(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    txt_delivery.text = it!!.data!!.getDeliveryExpDate()
                    txt_delivery_fee.text = it.data!!.getDeliveryFee() + " " + currency
                    price.text = it!!.data!!.getPrice() + " " + currency
                    txt_sub_total.text = it.data!!.getSubTotal() + " " + currency
                    totalAmount.text = it.data!!.getTotalAmount() + " " + currency
                } else printToast(this!!.context!!, it?.message.toString())
            })
    }

    private fun getOperators() {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)

        vModel!!.shippingOperators(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    operators = it!!.data
                    setOperators()
                } else printToast(this!!.context!!, it?.message.toString())
            })
    }

    private fun setOperators() {

        var dOperator: ArrayList<String> = ArrayList<String>()

        for (i in 0 until operators!!.size) {
            dOperator.add(operators!!.get(i).getOperator()!!)
        }

        val aa = ArrayAdapter(this!!.context!!, R.layout.spinner_item, dOperator!!.toTypedArray())
        spinner_operators.adapter = aa
    }

    private fun initControl() {

        productList.layoutManager = LinearLayoutManager(activity as VinnerActivity)
        // productList.isNestedScrollingEnabled = false
    }

    private fun getCart() {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)

        vModel!!.getCartPage(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    cartId = it!!.data!!.getCartItems()!!.get(0)!!.cartId
                    cartItems = it!!.data!!.getCartItems()
                    cartAdapter = CartAdapter(requireActivity(), cartItems, this)
                    productList.adapter = cartAdapter
                    if (cartItems != null)
                        itemCount.text = cartItems!!.size.toString() + " Items"
                    if (it.data!!.getAddress() != null) {
                        txt_address.text =
                            it.data.getAddress()!!.houseFlat + ", " + it.data.getAddress()!!.roadName + ", " + it.data.getAddress()!!.zip
                    }
                    setCartDetails(it.data!!.getCart())
                } else printToast(this!!.context!!, it?.message.toString())
            })
    }

    private fun setCartDetails(cart: Cart?) {
        if (!cart!!.totalAmount.equals("null")) {
            price.text = cart!!.totalAmount + " " + cart.currency
            totalAmount.text = cart.grandTotal + " " + cart.currency
            currency = cart.currency
        } else {
            price.text = "0 " + cart.currency
            totalAmount.text = "0 " + cart.currency
        }
    }

    override fun productUpdated(productId: String, count: String, position: Int) {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.cartId = cartId
        requestModel.productId = productId
        requestModel.productQty = count
        vModel!!.updateCart(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    cartItems!!.get(position)!!.productTotal = it!!.data!!.getProductTotal()
                    cartItems!!.get(position)!!.productQuantity = it!!.data!!.getProductQty()
                    cartAdapter!!.notifyDataSetChanged()
                    price.text = it!!.data!!.getTotalAmount() + " " + currency
                    totalAmount.text = it.data!!.getGrandTotal() + " " + currency
                } else printToast(this!!.context!!, it?.message.toString())
            })
    }

    override fun productRemoved(productId: String, position: Int) {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.cartId = cartId
        requestModel.productId = productId
        vModel!!.deleteCart(requestModel).observe(this,
            Observer {
                printToast(this!!.context!!, it?.message.toString())
                if (it?.status.equals(SUCCESS)) {
                    cartItems!!.removeAt(position)
                    cartAdapter!!.notifyDataSetChanged()
                    price.text = it!!.data!!.getTotalAmount() + " " + currency
                    totalAmount.text = it.data!!.getGrandTotal() + " " + currency
                }
            })
    }
}

interface CartadapterCallBack {
    fun productUpdated(productId: String, count: String, position: Int)
    fun productRemoved(productId: String, position: Int)
}