package com.estrrado.vinner.ui

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.adapters.RegionAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.request.RequestModel
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
import com.estrrado.vinner.helper.Constants.TOTAL_PAYABLE
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.testpay.PayFortActivity
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_cart.price
import kotlinx.android.synthetic.main.fragment_cart.totalAmount
import kotlinx.android.synthetic.main.fragment_cart.txt_delivery_fee
import kotlinx.android.synthetic.main.fragment_cart.txt_sub_total
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_back.*

class CheckoutFragment : Fragment() {

    var vModel: HomeVM? = null
    var operatorId: String? = null
    var address: String? = null
    var totalPayable: String? = null
    var housename: String? = null
    var Roadname: String? = null
    var landmark: String? = null
    var pincode: String? = null
    var addressType: String? = null
    var regionList: List<RegionSpinner>? = null
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
        address = arguments?.getString(ADDRESS)
        housename = arguments?.getString(HOUSENAME)
        Roadname = arguments?.getString(ROAD_NAME)
        pincode = arguments?.getString(PINCODE)
        addressType = arguments?.getString(ADDDRESS_TYPE)
        landmark = arguments?.getString(LANDMARK)
        return root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageTitle.text = "Checkout"
        progresscheckout.visibility = View.VISIBLE
        getDeleveryFee()
        txt_address.setText(address)
//        spnr_region.visibility = View.VISIBLE
//        spnr_region.isEnabled = false
//        regionList = readFromAsset(requireActivity())
//        val regionAdapter = RegionAdapter(requireContext()!!, regionList!!)
//        spnr_region.adapter = regionAdapter
//        if (!Preferences.get(activity, Preferences.COUNTRY_POSITION).equals(""))
//            spnr_region.setSelection(
//                Preferences.get(activity, Preferences.COUNTRY_POSITION)!!.toInt()
//            )
//        card_payfort.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString(CART_ID, arguments?.getString(CART_ID)!!)
//            bundle.putString(TOTAL_PAYABLE, totalPayable)
//            bundle.putString(OPERATOR_ID, operatorId)
//            bundle.putString(ADDRESS, address)
//            bundle.putString(HOUSENAME, housename)
//            bundle.putString(ROAD_NAME, Roadname)
//            bundle.putString(PINCODE, pincode)
//            bundle.putString(ADDDRESS_TYPE, addressType)
//            bundle.putString(LANDMARK, landmark)
//            val intent = Intent(activity, PayFortActivity::class.java)
//            intent.putExtras(bundle)
//            startActivity(intent)
//        }

        cod.setOnClickListener {

            vModel!!.PaymentStatus(
                RequestModel(
                    accessToken = Preferences.get(activity, ACCESS_TOKEN),
                    address_type = arguments?.getString(ADDDRESS_TYPE),
                    housename = arguments?.getString(HOUSENAME),
                    road_name = arguments?.getString(ROAD_NAME),
                    landmark = arguments?.getString(LANDMARK),
                    pincode = arguments?.getString(PINCODE),
                    payment_status = "pending",
                    payment_method = "cod",
                    operatorId = operatorId

                )
            ).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        progresscheckout.visibility = View.GONE
                        printToast(requireContext(), "payment successfull")
                        val fragmenttransaction: FragmentTransaction =
                            requireActivity().supportFragmentManager.beginTransaction()
                        val OrderList = OrderList()
                        fragmenttransaction.replace(
                            R.id.nav_host_fragment,
                            OrderList
                        ).addToBackStack("OrderList")
                        fragmenttransaction.commit()

                    } else {
                        if (it?.message.equals("Invalid access token")) {
                            startActivity(
                                Intent(
                                    activity,
                                    LoginActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        } else {
                            printToast(requireActivity(), it?.message!!)
                        }
                        printToast(requireContext(), it?.message.toString())
                    }

                })
        }
    }

    private fun getDeleveryFee() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.operatorId = operatorId

        vModel!!.deliveryFee(requestModel).observe(requireActivity(),
            Observer {
                if (it?.status.equals(SUCCESS)) {

                    progresscheckout.visibility = View.GONE
                    txt_delivery_fee.text =
                        it!!.data!!.getDeliveryFee() + " " + it.data!!.getCurrency()
                    price.text = it!!.data!!.getPrice() + " " + it.data!!.getCurrency()
                    txt_sub_total.text = it.data!!.getSubTotal() + " " + it.data!!.getCurrency()
                    totalAmount.text = it.data!!.getTotalAmount() + " " + it.data!!.getCurrency()
                    totalPayable = it.data!!.getTotalAmount()
                } else {
                    if (it?.message.equals("Invalid access token")) {
                        startActivity(Intent(activity, LoginActivity::class.java))
                        requireActivity().finish()
                    } else {
                        printToast(requireContext(), it?.message!!)
                    }
                    printToast(this!!.requireContext()!!, it?.message.toString())
                }
            })
    }


}