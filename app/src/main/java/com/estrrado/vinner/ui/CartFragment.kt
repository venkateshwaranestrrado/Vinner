package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.`interface`.AlertCallback
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.CartAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.Cart
import com.estrrado.vinner.data.models.CartItem
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Datum
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.ADDRESS
import com.estrrado.vinner.helper.Constants.BUILDINGNAME
import com.estrrado.vinner.helper.Constants.CART_ID
import com.estrrado.vinner.helper.Constants.CCURRENCY
import com.estrrado.vinner.helper.Constants.CITY
import com.estrrado.vinner.helper.Constants.CONTACTNO
import com.estrrado.vinner.helper.Constants.COUNTRY
import com.estrrado.vinner.helper.Constants.EMAIL
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.NAME
import com.estrrado.vinner.helper.Constants.OPERATOR_ID
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.S_ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.S_ADDRESS
import com.estrrado.vinner.helper.Constants.S_BUILDINGNAME
import com.estrrado.vinner.helper.Constants.S_CITY
import com.estrrado.vinner.helper.Constants.S_CONTACTNO
import com.estrrado.vinner.helper.Constants.S_COUNTRY
import com.estrrado.vinner.helper.Constants.S_EMAIL
import com.estrrado.vinner.helper.Constants.S_HOUSENAME
import com.estrrado.vinner.helper.Constants.S_LANDMARK
import com.estrrado.vinner.helper.Constants.S_NAME
import com.estrrado.vinner.helper.Constants.S_PINCODE
import com.estrrado.vinner.helper.Constants.S_ROAD_NAME
import com.estrrado.vinner.helper.Constants.addressSelected
import com.estrrado.vinner.helper.Constants.shipAddressSelected
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Preferences.REGION_FULLNAME
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.priceFormat
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.empty_cart.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_prev.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CartFragment : Fragment(), CartadapterCallBack {

    var vModel: HomeVM? = null
    var cartId: String? = null
    var cartAdapter: CartAdapter? = null
    var cartItems: ArrayList<CartItem?>? = null
    var currency: String? = null
    var operators: List<Datum>? = null
    var operatorId: String? = null

    //billing Address
    var address: String? = null
    var addressRegion: String? = null
    var housename: String? = null
    var Roadname: String? = null
    var countryName: String? = null
    var city: String? = null
    var name: String? = null
    var landmark: String? = null
    var pincode: String? = null
    var addressType: String? = null
    var email: String? = null
    var phone: String? = null
    var building: String? = null

    //shipping Address
    var s_address: String? = null
    var s_addressRegion: String? = null
    var s_housename: String? = null
    var s_Roadname: String? = null
    var s_countryName: String? = null
    var s_city: String? = null
    var s_name: String? = null
    var s_landmark: String? = null
    var s_pincode: String? = null
    var s_addressType: String? = null
    var s_email: String? = null
    var s_phone: String? = null
    var s_building: String? = null

    var deliveryFee: Double = 0.00
    var weightMsg: String = ""
    private var cartFound: Boolean = false
    var regionList: List<RegionSpinner>? = null

    var json_string: String? = null
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
        pageTitle.text = "Cart"
        progresscart.visibility = View.VISIBLE
        initControl()
        getCart()
        getOperators()
        textView8.setOnClickListener {
            spinner_operators.visibility = View.VISIBLE
            textView8.visibility = View.INVISIBLE

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
                    if (address != null && s_address != null) {
                        if (weightMsg == "") {
                            if (Preferences.get(
                                    activity,
                                    REGION_FULLNAME
                                ) == addressRegion && Preferences.get(
                                    activity,
                                    REGION_FULLNAME
                                ) == s_addressRegion
                            ) {
                                val bundle = bundleOf(
                                    OPERATOR_ID to operatorId,
                                    CART_ID to cartId,
                                    ADDRESS to address,
                                    PINCODE to pincode,
                                    HOUSENAME to housename,
                                    LANDMARK to landmark,
                                    ADDDRESS_TYPE to addressType,
                                    ROAD_NAME to Roadname,
                                    COUNTRY to countryName,
                                    CITY to city,
                                    NAME to name,
                                    CONTACTNO to phone,
                                    EMAIL to email,
                                    BUILDINGNAME to building,
                                    S_ADDRESS to s_address,
                                    S_PINCODE to s_pincode,
                                    S_HOUSENAME to s_housename,
                                    S_LANDMARK to s_landmark,
                                    S_ADDDRESS_TYPE to s_addressType,
                                    S_ROAD_NAME to s_Roadname,
                                    S_COUNTRY to s_countryName,
                                    S_CITY to s_city,
                                    S_NAME to s_name,
                                    S_CONTACTNO to s_phone,
                                    S_EMAIL to s_email,
                                    S_BUILDINGNAME to s_building,
                                    CCURRENCY to currency
                                )
                                view.findNavController()
                                    .navigate(
                                        R.id.action_navigation_cart_to_checkoutFragment,
                                        bundle
                                    )
                            } else {
                                Helper.showSingleAlert(
                                    "Selected country not matching with your Billing/Shipping address. Please choose correct Billing/Shipping address.",
                                    context = requireContext()
                                )
                            }
                        } else {
                            printToast(requireContext(), weightMsg)
                        }
                    } else {
                        printToast(requireContext(), "Please Enter a valid Address")
                    }

                } else {
                    printToast(requireContext(), "Please select shipping operators")
                }
            } else {
                printToast(requireContext(), "No Cart data found")
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getDeleveryFee(position: Int) {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            operatorId = operators!!.get(position).getShippingOperatorId()
            Preferences.put(activity, Preferences.OPERATOR_ID, operatorId!!)
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.operatorId = operatorId
            requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            vModel!!.deliveryFee(requestModel).observe(this,
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        weightMsg = ""
                        progresscart.visibility = View.GONE
                        it?.let {
                            it.data?.getDeliveryExpDate()?.let { expDate ->
                                if (expDate != "") {
                                    val date =
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                                            expDate
                                        )
                                    date?.let {
                                        txt_delivery.text =
                                            SimpleDateFormat(
                                                "dd-MM-yyyy",
                                                Locale.getDefault()
                                            ).format(date)
                                    }
                                }
                            }

                            txt_delivery_fee.text =
                                it.data!!.getCurrency() + " " + priceFormat(it.data.getDeliveryFee())
                            if (!it.data!!.getDeliveryFee().equals(""))
                                deliveryFee = it.data!!.getDeliveryFee()!!.toDouble()
                            price.text =
                                it.data.getCurrency() + " " + priceFormat(it.data.getPrice())
                            txt_sub_total.text =
                                it.data.getCurrency() + " " + priceFormat(it.data.getSubTotal())
                            totalAmount.text =
                                it.data.getCurrency() + " " + priceFormat(it.data.getTotalAmount())
                        }
                    } else {
                        txt_delivery_fee.text = " 0.00"
                        deliveryFee = 0.00
                        it?.message?.let {
                            weightMsg = it
                        }
                        txt_sub_total.text = " 0.00"
                        totalAmount.text = " 0.00"
                    }
                })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
            progresscart.visibility = View.GONE
        }
    }

    private fun getOperators() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            vModel!!.shippingOperators(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        progresscart.visibility = View.GONE
                        val json = Helper.getGson().toJson(it!!.data)
                        val list = Helper.getGson()
                            .fromJson(
                                json,
                                object : TypeToken<List<Datum>>() {}.type
                            ) as ArrayList<Datum>
                        operators = list
                        setOperators()
                    } else {
                        if (it?.message.equals("Invalid access token")) {
                            startActivity(Intent(activity, LoginActivity::class.java))
                            requireActivity().finish()
                        } else if (it?.httpcode == 402) {
                            val json = JSONObject(Helper.getGson().toJson(it.data))
                            Helper.showSingleAlert(
                                it.message ?: "",
                                requireContext(),
                                object : AlertCallback {
                                    override fun alertSelected(isSelected: Boolean, from: Int) {
                                        Helper.setCountry(
                                            json.getString("country_code"),
                                            requireActivity()
                                        )
                                        getCart()
                                        getOperators()
                                    }
                                })
                        }
                    }
                })
        } else {
            progresscart.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setOperators() {

        val dOperator: ArrayList<String> = ArrayList<String>()

        for (i in 0 until operators!!.size) {
            dOperator.add(operators!!.get(i).getOperator()!!)
        }

        val aa = ArrayAdapter(requireContext(), R.layout.spinner_item, dOperator.toTypedArray())
        spinner_operators.adapter = aa
        if (dOperator.size > 0) {
            spinner_operators.visibility = View.VISIBLE
            textView8.visibility = View.GONE
        }
    }

    private fun initControl() {
        cont_shop.setOnClickListener {
            activity?.let {
                (activity is VinnerActivity).also {
                    if (it) {
                        (activity as VinnerActivity).selectMenu()
                    }
                }
            }
        }

        layout_add_address.setOnClickListener {
            val bundle = bundleOf(Constants.FROM to 1)
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_cart_to_address_list, bundle)
        }

        productList.layoutManager = LinearLayoutManager(activity as VinnerActivity)
    }

    @SuppressLint("SetTextI18n")
    private fun getCart() {

        Log.e("token", Preferences.get(activity, ACCESS_TOKEN)!!)

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            vModel!!.getCartPage(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        if ((it!!.data!!.getItemsTotal() == null) || (it.data!!.getItemsTotal() == "0")) {
                            empty?.visibility = View.VISIBLE
                        }
                        activity?.let { activity ->
                            (activity is VinnerActivity).also { res ->
                                if (res) {
                                    (activity as VinnerActivity).refreshBadgeView(it.data!!.getItemsTotal())
                                }
                            }
                        }
                        if (it.data!!.getAddress() != null) {
                            cardview_deliveryaddress?.visibility = View.VISIBLE
                            cardview_shippingaddress?.visibility = View.VISIBLE

                            txtChangeAddress?.setOnClickListener {
                                val bundle = bundleOf(Constants.FROM to 1)
                                Navigation.findNavController(it)
                                    .navigate(R.id.action_navigation_cart_to_address_list, bundle)
                            }

                            txtChangeShipAddress?.setOnClickListener {
                                val bundle = bundleOf(Constants.FROM to 2)
                                Navigation.findNavController(it)
                                    .navigate(R.id.action_navigation_cart_to_address_list, bundle)
                            }

                            if (addressSelected != null) {
                                setAddressSelected()
                            } else {
                                address =
                                    it.data.getAddress()!!.name + ", " + it.data.getAddress()!!.email + ", " + it.data.getAddress()!!.phone + ", " + it.data.getAddress()!!.houseFlat +
                                            ", " + it.data.getAddress()!!.building + ", " + it.data.getAddress()!!.roadName +
                                            ", " + it.data.getAddress()!!.city + ", " + it.data.getAddress()!!.landmark + ", " + it.data.getAddress()!!.country +
                                            ", " + it.data.getAddress()!!.zip
                                txt_address?.text = address
                                addressRegion = it.data.getAddress()!!.country

                                housename = it.data.getAddress()!!.houseFlat
                                Roadname = it.data.getAddress()!!.roadName
                                pincode = it.data.getAddress()!!.zip
                                addressType = it.data.getAddress()!!.addressType
                                landmark = it.data.getAddress()!!.landmark
                                countryName = it.data.getAddress()!!.country
                                city = it.data.getAddress()!!.city
                                name = it.data.getAddress()!!.name
                                phone = it.data.getAddress()!!.phone
                                email = it.data.getAddress()!!.email
                                building = it.data.getAddress()!!.building
                            }

                            if (shipAddressSelected != null) {
                                setShipAddressSelected()
                            } else {
                                s_address =
                                    it.data.getAddress()!!.name + ", " + it.data.getAddress()!!.email + ", " + it.data.getAddress()!!.phone + ", " + it.data.getAddress()!!.houseFlat +
                                            ", " + it.data.getAddress()!!.building + ", " + it.data.getAddress()!!.roadName +
                                            ", " + it.data.getAddress()!!.city + ", " + it.data.getAddress()!!.landmark + ", " + it.data.getAddress()!!.country +
                                            ", " + it.data.getAddress()!!.zip
                                txt_shipaddress?.text = s_address
                                s_addressRegion = it.data.getAddress()!!.country

                                s_housename = it.data.getAddress()!!.houseFlat
                                s_Roadname = it.data.getAddress()!!.roadName
                                s_pincode = it.data.getAddress()!!.zip
                                s_addressType = it.data.getAddress()!!.addressType
                                s_landmark = it.data.getAddress()!!.landmark
                                s_countryName = it.data.getAddress()!!.country
                                s_city = it.data.getAddress()!!.city
                                s_name = it.data.getAddress()!!.name
                                s_phone = it.data.getAddress()!!.phone
                                s_email = it.data.getAddress()!!.email
                                s_building = it.data.getAddress()!!.building
                            }

                        } else {
                            layout_add_address?.visibility = View.VISIBLE
                            cardview_shippingaddress?.visibility = View.GONE
                            cardview_deliveryaddress?.visibility = View.GONE
                        }
                        progresscart?.visibility = View.GONE
                        cartItems = it.data.getCartItems()

                        if (cartItems != null) {
                            cartId = it.data.getCartItems()!!.get(0)!!.cartId
                            cartAdapter = CartAdapter(requireActivity(), cartItems, this)
                            productList?.adapter = cartAdapter
                            itemCount?.text = cartItems!!.size.toString() + " Items"
                            cartFound = true
                            activity?.let { activity ->
                                (activity is VinnerActivity).also { res ->
                                    if (res) {
                                        (activity as VinnerActivity).refreshBadgeView(it.data!!.getItemsTotal())
                                    }
                                }
                            }
                        } else {
                            clearCart()
                            empty?.visibility = View.VISIBLE
                            cart?.visibility = View.GONE
                            cartFound = false
                        }
                        view?.let { view ->
                            setCartDetails(it.data.getCart())
                        }
                    } else {
                        if (it?.message.equals("Invalid access token")) {
                            startActivity(Intent(activity, LoginActivity::class.java))
                            requireActivity().finish()
                        } else if (it?.httpcode == 402) {
                            val json = JSONObject(Helper.getGson().toJson(it.data))
                            Helper.showSingleAlert(
                                it.message ?: "",
                                requireContext(),
                                object : AlertCallback {
                                    override fun alertSelected(isSelected: Boolean, from: Int) {
                                        Helper.setCountry(
                                            json.getString("country_code"),
                                            requireActivity()
                                        )
                                        getCart()
                                        getOperators()
                                    }
                                })
                        }
                    }
                })
        } else {
            progresscart.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCartDetails(cart: Cart?) {
        if (!cart!!.totalAmount.equals("null")) {
            price.text = cart.currency + " " + priceFormat(cart.totalAmount)
            totalAmount.text =
                cart.currency + " " + priceFormat((cart.grandTotal!!.toDouble() + deliveryFee).toString())
            txt_sub_total.text =
                cart.currency + " " + priceFormat((cart.grandTotal!!.toDouble() + deliveryFee).toString())
            currency = cart.currency
            Log.e("currency ", "" + currency)
        } else {
            price.text = cart.currency + " 0.00"
            txt_sub_total.text = cart.currency + " 0.00"
            totalAmount.text = cart.currency + " 0.00"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun productUpdated(productId: String, count: String, position: Int) {
        progresscart.visibility = View.VISIBLE
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.cartId = cartId
            requestModel.productId = productId
            requestModel.productQty = count
            requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            vModel!!.updateCart(requestModel).observe(this,
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        printToast(requireContext(), it?.message.toString())
                        progresscart.visibility = View.GONE
                        cartItems!!.get(position)!!.productTotal = it!!.data!!.getProductTotal()
                        cartItems!!.get(position)!!.productQuantity = it.data!!.getProductQty()
                        cartAdapter!!.notifyDataSetChanged()
                        price.text = currency + " " + priceFormat(it.data.getTotalAmount())
                        totalAmount.text = currency + " " + priceFormat(it.data.getGrandTotal())

                        totalAmount.text =
                            currency + " " + priceFormat(
                                (it.data.getGrandTotal()!!
                                    .toDouble() + deliveryFee).toString()
                            )
                        txt_sub_total.text =
                            currency + " " + priceFormat(
                                (it.data.getGrandTotal()!!
                                    .toDouble() + deliveryFee).toString()
                            )
                        if (spinner_operators.count > 0)
                            getDeleveryFee(spinner_operators.selectedItemPosition)
                    }
                })
        } else {
            progresscart.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun productRemoved(productId: String, position: Int) {
        progresscart.visibility = View.VISIBLE
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            requestModel.cartId = cartId
            requestModel.productId = productId
            vModel!!.deleteCart(requestModel).observe(this,
                Observer {
                    printToast(requireContext(), it?.message.toString())
                    if (it?.status.equals(SUCCESS)) {
                        progresscart.visibility = View.GONE
                        cartItems!!.removeAt(position)
                        cartAdapter!!.notifyDataSetChanged()
                        getCart()
                        itemCount.text = cartItems!!.size.toString() + " Items"
                        (activity as VinnerActivity).refreshBadgeView(it!!.data!!.getItemsTotal())
                        if (spinner_operators.count > 0)
                            getDeleveryFee(spinner_operators.selectedItemPosition)
                    }
                })
        } else {
            progresscart.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFromAsset(): List<RegionSpinner> {
        val file_name = "login_region.json"
        val bufferReader = requireActivity().assets.open(file_name).bufferedReader()
        json_string = bufferReader.use {
            it.readText()
        }
        val gson = Gson()
        val modelList: List<RegionSpinner> =
            gson.fromJson(json_string, Array<RegionSpinner>::class.java).toList()
        return modelList
    }

    private fun setShipAddressSelected() {
        s_address =
            shipAddressSelected!!.name + ", " + shipAddressSelected!!.email + ", " + shipAddressSelected!!.phone + ", " +
                    shipAddressSelected!!.house_flat + ", " + shipAddressSelected!!.building + ", " + shipAddressSelected!!.road_name +
                    ", " + shipAddressSelected!!.city + ", " + shipAddressSelected!!.landmark +
                    ", " + shipAddressSelected!!.country + ", " + shipAddressSelected!!.zip
        txt_shipaddress.text = s_address
        s_addressRegion = shipAddressSelected!!.country

        s_housename = shipAddressSelected!!.house_flat
        s_Roadname = shipAddressSelected!!.road_name
        s_pincode = shipAddressSelected!!.zip
        s_addressType = shipAddressSelected!!.address_type
        s_landmark = shipAddressSelected!!.landmark
        s_countryName = shipAddressSelected!!.country
        s_city = shipAddressSelected!!.city
        s_name = shipAddressSelected!!.name
        s_phone = shipAddressSelected!!.phone
        s_email = shipAddressSelected!!.email
        s_building = shipAddressSelected!!.building
    }

    private fun setAddressSelected() {
        address =
            addressSelected!!.name + ", " + addressSelected!!.email + ", " + addressSelected!!.phone + ", " +
                    addressSelected!!.house_flat + ", " + addressSelected!!.building + ", " + addressSelected!!.road_name +
                    ", " + addressSelected!!.city + ", " + addressSelected!!.landmark +
                    ", " + addressSelected!!.country + ", " + addressSelected!!.zip
        txt_address.text = address
        addressRegion = addressSelected!!.country

        housename = addressSelected!!.house_flat
        Roadname = addressSelected!!.road_name
        pincode = addressSelected!!.zip
        addressType = addressSelected!!.address_type
        landmark = addressSelected!!.landmark
        countryName = addressSelected!!.country
        city = addressSelected!!.city
        name = addressSelected!!.name
        phone = addressSelected!!.phone
        email = addressSelected!!.email
        building = addressSelected!!.building
    }

    fun clearCart() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.cartId = "0"
            progresscart.visibility = View.VISIBLE
            vModel!!.emptyCart(requestModel).observe(requireActivity(),
                Observer {
                    progresscart.visibility = View.GONE
                }
            )
        } else {
            progresscart.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }


}


interface CartadapterCallBack {
    fun productUpdated(productId: String, count: String, position: Int)
    fun productRemoved(productId: String, position: Int)
}