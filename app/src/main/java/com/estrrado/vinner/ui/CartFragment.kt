package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.CartAdapter
import com.estrrado.vinner.adapters.RegionAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.Cart
import com.estrrado.vinner.data.models.CartItem
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Datum
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.ADDRESS
import com.estrrado.vinner.helper.Constants.CART_ID
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.OPERATOR_ID
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.logo
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.ui.more.AddAddress
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.empty_cart.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_notification_bage.*

class CartFragment : Fragment(), CartadapterCallBack {

    var vModel: HomeVM? = null
    var cartId: String? = null
    var cartAdapter: CartAdapter? = null
    var cartItems: ArrayList<CartItem?>? = null
    var currency: String? = null
    var operators: List<Datum>? = null
    var operatorId: String? = null
    var address: String? = null
    var housename: String? = null
    var Roadname: String? = null
    var landmark: String? = null
    var pincode: String? = null
    var addressType: String? = null
     private  var cartFound:Boolean=false

    var json_string:String?=null
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView5.text="Cart"
//        (activity as VinnerActivity).refreshBadgeView()
//        (activity as VinnerActivity).close()
        progresscart.visibility= View.VISIBLE
        Glide.with(requireContext())
            .load(logo)
            .thumbnail(0.1f)
            .into(img_logo)
        initControl()
        getCart()
        getOperators()
        textView8.setOnClickListener {
            spinner_operators.visibility=View.VISIBLE
            textView8.visibility=View.INVISIBLE

        }
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
        checkout.setOnClickListener {
            if (cartFound == true) {
                if (operatorId != null) {
                    if (address != null) {
                        val bundle = bundleOf(
                            OPERATOR_ID to operatorId,
                            CART_ID to cartId,
                            ADDRESS to address,
                            PINCODE to pincode,
                            HOUSENAME to housename,
                            LANDMARK to landmark,
                            ADDDRESS_TYPE to addressType,
                            ROAD_NAME to Roadname

                        )
//                val bundle = bundleOf(OPERATOR_ID to "1", CART_ID to cartId)
                        view.findNavController()
                            .navigate(R.id.action_navigation_cart_to_checkoutFragment, bundle)
                    }else {
                        printToast(requireContext(), "Please Enter a valid Address")
                    }

                } else {
                    printToast(requireContext(), "Please select shipping operators")
                }
            }
            else{
                printToast(requireContext(), "No Cart data found")
            }
        }

}

@SuppressLint("SetTextI18n")
private fun getDeleveryFee(position: Int) {
//    progresscart.visibility= View.VISIBLE
    if (Helper.isNetworkAvailable(requireContext())) {
        val requestModel = RequestModel()
        operatorId = operators!!.get(position).getShippingOperatorId()
        Preferences.put(activity, Preferences.OPERATOR_ID,operatorId!!)
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.operatorId = operatorId
        requestModel.countryCode=Preferences.get(activity, REGION_NAME)
        vModel!!.deliveryFee(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    progresscart.visibility= View.GONE
                    txt_delivery.text = it!!.data!!.getDeliveryExpDate()
                    txt_delivery_fee.text = it.data!!.getDeliveryFee() + " " + it.data.getCurrency()
                    price.text = it.data.getPrice() + " " + it.data.getCurrency()
                    txt_sub_total.text = it.data.getSubTotal() + " " + it.data.getCurrency()
                    totalAmount.text = it.data.getTotalAmount() + " " + it.data.getCurrency()
                    checkout.setEnabled(true);
                }
                else
                {
                    txt_delivery_fee.text = "0"
                    printToast(requireContext(), "No Shipping is available in this area")
                    txt_sub_total.text = "0"
                    totalAmount.text= "0"

                    checkout.setEnabled(false);
//                    if (it?.message.equals("Invalid access token")) {
//                        startActivity(Intent(activity, LoginActivity::class.java))
//                        requireActivity().finish()
////                    } else {
////                      printToast(requireContext(), it?.message!!)
//                    }
////                  printToast(this.requireContext(), it?.message.toString())
                }
            })
    }
    else{
        Toast.makeText(context,"No Network Available",Toast.LENGTH_SHORT).show()
        progresscart.visibility= View.GONE
    }
}

private fun getOperators() {
//    progresscart.visibility= View.VISIBLE
    if (Helper.isNetworkAvailable(requireContext())) {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.countryCode=Preferences.get(activity, REGION_NAME)
        vModel!!.shippingOperators(requestModel).observe(requireActivity(),
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    progresscart.visibility= View.GONE
                    operators = it!!.data
                    setOperators()
                }        else
                {
                    if (it?.message.equals("Invalid access token")) {
                        startActivity(Intent(activity, LoginActivity::class.java))
                        requireActivity().finish()
                    } else {
//                        printToast   (requireContext(), it?.message!!)
                    }
//                    printToast(this.requireContext(), it?.message.toString())
                }
            })
    }
    else{
        progresscart.visibility= View.GONE
        Toast.makeText(context,"No Network Available",Toast.LENGTH_SHORT).show()
    }
}

private fun setOperators() {

    val dOperator: ArrayList<String> = ArrayList<String>()

    for (i in 0 until operators!!.size) {
        dOperator.add(operators!!.get(i).getOperator()!!)
    }

    val aa = ArrayAdapter(requireContext(), R.layout.spinner_item, dOperator.toTypedArray())
    spinner_operators.adapter = aa
}

private fun initControl() {

    cont_shop.setOnClickListener {
        requireActivity().supportFragmentManager.beginTransaction().
        replace(R.id.nav_host_fragment, HomeFragment()).commit()
    }



    productList.layoutManager = LinearLayoutManager(activity as VinnerActivity)
    // productList.isNestedScrollingEnabled = false
}

@SuppressLint("SetTextI18n")
private fun getCart() {
//    progresscart.visibility= View.VISIBLE
                       if (Helper.isNetworkAvailable(requireContext())) {
                        val requestModel = RequestModel()
                        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
                        requestModel.countryCode=Preferences.get(activity, REGION_NAME)
                        //requestModel.accessToken="122874726"
                        vModel!!.getCartPage(requestModel).observe(requireActivity(),
                            Observer {
                                if (it?.status.equals(SUCCESS)) {


//                                    requireActivity().notificationsBadgeTextView.setText(it!!.data!!.getItemsTotal())
                               if ((it!!.data!!.getItemsTotal()==null)||(it.data!!.getItemsTotal()=="0")){
                                   empty.visibility=View.VISIBLE
                               (activity as VinnerActivity).refreshBadgeView()
                                  }

                    if (it.data!!.getAddress() != null) {
                        cardview_deliveryaddress.visibility = View.VISIBLE

                        img_edit_address.setOnClickListener {
                            requireActivity().supportFragmentManager.beginTransaction().
                            replace(R.id.nav_host_fragment, Address_list()).commit()
                        }
                        txt_address.text =
                            it.data.getAddress()!!.houseFlat + ", " + it.data.getAddress()!!.roadName +
                                    ", " + it.data.getAddress()!!.zip

                        address=  it.data.getAddress()!!.houseFlat + ", " + it.data.getAddress()!!.roadName +
                                ", " + it.data.getAddress()!!.zip

                        housename=it.data.getAddress()!!.houseFlat
                        Roadname=it.data.getAddress()!!.roadName
                        pincode=it.data.getAddress()!!.zip
                        addressType=it.data.getAddress()!!.addressType
                        landmark=it.data.getAddress()!!.landmark



                    }
                    else
                    {
                        layout_add_address.visibility=View.VISIBLE
                        layout_add_address.setOnClickListener {
                            requireActivity().supportFragmentManager.beginTransaction().
                            replace(R.id.nav_host_fragment, AddAddress()).commit()
                        }
                    }
                    progresscart.visibility= View.GONE
                    cartItems = it.data.getCartItems()

                    if (cartItems != null) {
                        cartId = it.data.getCartItems()!!.get(0)!!.cartId
                        cartAdapter = CartAdapter(requireActivity(), cartItems, this)
                        productList.adapter = cartAdapter
                        itemCount.text = cartItems!!.size.toString() + " Items"
                        cartFound = true
                        requireActivity().notificationsBadgeTextView.setText(it.data.getItemsTotal())
                    }else{
                        empty.visibility=View.VISIBLE
                        cart.visibility=View.GONE
                        cartFound = false
//                        requireActivity().notificationsBadgeTextView.visibility==View.GONE
                        printToast(requireContext(),"Not Found")
                    }

                    setCartDetails(it.data.getCart())
//                var cartId1 = it!!.data.cart_items!!.get(0).cart_item_id
//                Toast.makeText(context,cartId1.toString(),Toast.LENGTH_SHORT).show()
                } else {
                    if(it?.message.equals("Invalid access token")){
                        startActivity(Intent(activity, LoginActivity::class.java))
                        requireActivity().finish()
                    }
//                    else {
//                      printToast(requireContext(), it?.message!!)
//                    }
//                                  printToast(requireContext(), it?.message.toString())
                }
            })
    }
    else{
        progresscart.visibility= View.GONE
        Toast.makeText(context,"No Network Available",Toast.LENGTH_SHORT).show()
    }
}

@SuppressLint("SetTextI18n")
private fun setCartDetails(cart: Cart?) {
    if (!cart!!.totalAmount.equals("null")) {
        price.text = cart.totalAmount + " " + cart.currency
        totalAmount.text = cart.grandTotal + " " + cart.currency
        currency = cart.currency
    } else {
        price.text = "0 " + cart.currency
        totalAmount.text = "0 " + cart.currency
    }
}

@SuppressLint("SetTextI18n")
override fun productUpdated(productId: String, count: String, position: Int) {
//    progresscart.visibility= View.VISIBLE
    if (Helper.isNetworkAvailable(requireContext())) {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.cartId = cartId
        requestModel.productId = productId
        requestModel.productQty = count
        requestModel.countryCode=Preferences.get(activity, REGION_NAME)
        vModel!!.updateCart(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {

                    progresscart.visibility= View.GONE
//                  requireActivity().notificationsBadgeTextView.setText(it!!.data!!.getItemsTotal())

                    cartItems!!.get(position)!!.productTotal = it!!.data!!.getProductTotal()
                    cartItems!!.get(position)!!.productQuantity = it.data!!.getProductQty()
                    cartAdapter!!.notifyDataSetChanged()
                    price.text = it.data.getTotalAmount() + " " + currency
                    totalAmount.text = it.data.getGrandTotal() + " " + currency

                }
//
//                else
//
//                  printToast(requireContext(), it?.message.toString()
//                  )
            })
    }
    else{
        progresscart.visibility= View.GONE
        Toast.makeText(context,"No Network Available",Toast.LENGTH_SHORT).show()
    }
}

@SuppressLint("SetTextI18n")
override fun productRemoved(productId: String, position: Int) {
//    progresscart.visibility= View.VISIBLE
    if (Helper.isNetworkAvailable(requireContext())) {
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.countryCode=Preferences.get(activity, REGION_NAME)
        requestModel.cartId = cartId
        requestModel.productId = productId
        vModel!!.deleteCart(requestModel).observe(this,
            Observer {
//                printToast(requireContext(), it?.message.toString())
                if (it?.status.equals(SUCCESS)) {
                    progresscart.visibility= View.GONE
                    cartItems!!.removeAt(position)
                    cartAdapter!!.notifyDataSetChanged()
                    getCart()
                    itemCount.text = cartItems!!.size.toString() + " Items"
                    if (cartItems!=null){
                        requireActivity().notificationsBadgeTextView.setText(it!!.data!!.getItemsTotal())
                    }


                }
            })
    }
    else{
        progresscart.visibility= View.GONE
        Toast.makeText(context,"No Network Available",Toast.LENGTH_SHORT).show()
    }
}
    private fun readFromAsset(): List<RegionSpinner> {
        val file_name = "login_region.json"
        val bufferReader = requireActivity().assets.open(file_name).bufferedReader()
        json_string = bufferReader.use {
            it.readText()
        }
        val gson = Gson()
        val modelList: List<RegionSpinner> = gson.fromJson(json_string, Array<RegionSpinner>::class.java).toList()
        return modelList
    }
}

interface CartadapterCallBack {
    fun productUpdated(productId: String, count: String, position: Int)
    fun productRemoved(productId: String, position: Int)
}