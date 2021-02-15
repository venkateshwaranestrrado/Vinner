package com.estrrado.vinner.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.`interface`.AlertCallback
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.ADDRESS
import com.estrrado.vinner.helper.Constants.CART_ID
import com.estrrado.vinner.helper.Constants.CITY
import com.estrrado.vinner.helper.Constants.COUNTRY
import com.estrrado.vinner.helper.Constants.DO_YOU_CONFIRM_TO_CHECK_OUT
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.NAME
import com.estrrado.vinner.helper.Constants.OPERATOR_ID
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Constants.STATUS
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.TOTAL_PAYABLE
import com.estrrado.vinner.helper.Constants.reqCode
import com.estrrado.vinner.helper.Helper.showAlert
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.priceFormat
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.testpay.PayFortActivity
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_cart.price
import kotlinx.android.synthetic.main.fragment_cart.totalAmount
import kotlinx.android.synthetic.main.fragment_cart.txt_delivery_fee
import kotlinx.android.synthetic.main.fragment_cart.txt_sub_total
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.toolbar_back.*

class CheckoutFragment : Fragment(), AlertCallback {

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

        card_payfort.setOnClickListener {
            showAlert(DO_YOU_CONFIRM_TO_CHECK_OUT, 1, this, requireContext())
        }

        cod.setOnClickListener {
            showAlert(DO_YOU_CONFIRM_TO_CHECK_OUT, 2, this, requireContext())
        }
    }

    private fun getDeleveryFee() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.operatorId = operatorId
        progresscheckout.visibility = View.VISIBLE
        vModel!!.deliveryFee(requestModel).observe(requireActivity(),
            Observer {
                progresscheckout.visibility = View.GONE
                if (it?.status.equals(SUCCESS)) {
                    txt_delivery_fee.text =
                        it!!.data!!.getCurrency() + " " + priceFormat(it.data!!.getDeliveryFee())
                    price.text = it!!.data!!.getCurrency() + " " + priceFormat(it.data!!.getPrice())
                    txt_sub_total.text =
                        it.data!!.getCurrency() + " " + priceFormat(it.data!!.getSubTotal())
                    totalAmount.text =
                        it.data!!.getCurrency() + " " + priceFormat(it.data!!.getTotalAmount())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.getStringArrayExtra(STATUS) != null) {
            val message = data.getStringExtra(STATUS)
            if (message.equals(SUCCESS))
                requireView().findNavController()
                    .navigate(R.id.action_checkoutFragment_to_order_list)
        }
    }

    override fun alertSelected(isSelected: Boolean, from: Int) {
        if (isSelected) {
            if (from == 1)
                payFort()
            if (from == 2)
                cod()
        }
    }

    private fun payFort() {
        val bundle = Bundle()
        bundle.putString(CART_ID, arguments?.getString(CART_ID)!!)
        bundle.putString(TOTAL_PAYABLE, totalPayable)
        bundle.putString(OPERATOR_ID, operatorId)
        bundle.putString(ADDRESS, address)
        bundle.putString(HOUSENAME, housename)
        bundle.putString(ROAD_NAME, Roadname)
        bundle.putString(PINCODE, pincode)
        bundle.putString(ADDDRESS_TYPE, addressType)
        bundle.putString(LANDMARK, landmark)
        bundle.putString(CITY, arguments?.getString(CITY))
        bundle.putString(COUNTRY, arguments?.getString(COUNTRY))
        bundle.putString(NAME, arguments?.getString(NAME))
        bundle.putString(Constants.CCURRENCY, arguments?.getString(Constants.CCURRENCY))

        Log.e("bundle ", bundle.toString())

        val intent = Intent(activity, PayFortActivity::class.java)
        intent.putExtras(bundle)
        requireActivity().startActivityForResult(intent, reqCode)
    }

    private fun cod() {
        progresscheckout.visibility = View.VISIBLE
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
                operatorId = operatorId,
                country = arguments?.getString(COUNTRY),
                city = arguments?.getString(CITY),
                name = arguments?.getString(NAME)
            )
        ).observe(requireActivity(),
            Observer {
                progresscheckout.visibility = View.GONE
                if (it?.status.equals(SUCCESS)) {
                    printToast(requireContext(), "payment successfull")
                    (activity as VinnerActivity).refreshBadgeView("0")
                    requireView().findNavController()
                        .navigate(R.id.action_checkoutFragment_to_order_list)
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