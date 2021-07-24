package com.estrrado.vinner.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.estrrado.vinner.helper.Constants.BUILDINGNAME
import com.estrrado.vinner.helper.Constants.CART_ID
import com.estrrado.vinner.helper.Constants.CITY
import com.estrrado.vinner.helper.Constants.CONTACTNO
import com.estrrado.vinner.helper.Constants.COUNTRY
import com.estrrado.vinner.helper.Constants.DO_YOU_CONFIRM_TO_CHECK_OUT
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
    var totalPayable: String? = null
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

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageTitle.text = "Checkout"
        progresscheckout.visibility = View.VISIBLE
        getDeleveryFee()
        txt_address.setText(arguments?.getString(ADDRESS))
        txt_shipaddress.setText(arguments?.getString(S_ADDRESS))

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
        (activity as VinnerActivity).refreshBadgeView("0")
        if (requestCode == reqCode && resultCode == Activity.RESULT_OK) {
            requireView().findNavController()
                .navigate(R.id.action_checkoutFragment_to_order_list)
        }
    }

    override fun alertSelected(isSelected: Boolean, from: Int) {
        if (isSelected) {
            if (from == 1)
                if (Preferences.get(activity, Constants.PROFILEMAIL) != "") {
                    payFort()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please update email address in profile.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            if (from == 2)
                cod()
        }
    }

    private fun payFort() {

        Constants.addressSelected = null
        Constants.shipAddressSelected = null

        val bundle = Bundle()
        bundle.putString(CART_ID, arguments?.getString(CART_ID)!!)
        bundle.putString(TOTAL_PAYABLE, totalPayable)
        bundle.putString(OPERATOR_ID, operatorId)

        bundle.putString(ADDRESS, arguments?.getString(ADDRESS))
        bundle.putString(HOUSENAME, arguments?.getString(HOUSENAME))
        bundle.putString(ROAD_NAME, arguments?.getString(ROAD_NAME))
        bundle.putString(PINCODE, arguments?.getString(PINCODE))
        bundle.putString(ADDDRESS_TYPE, arguments?.getString(ADDDRESS_TYPE))
        bundle.putString(LANDMARK, arguments?.getString(LANDMARK))
        bundle.putString(CITY, arguments?.getString(CITY))
        bundle.putString(COUNTRY, arguments?.getString(COUNTRY))
        bundle.putString(NAME, arguments?.getString(NAME))
        bundle.putString(EMAIL, arguments?.getString(EMAIL))
        bundle.putString(BUILDINGNAME, arguments?.getString(BUILDINGNAME))
        bundle.putString(CONTACTNO, arguments?.getString(CONTACTNO))
        bundle.putString(S_ADDRESS, arguments?.getString(S_ADDRESS))
        bundle.putString(S_HOUSENAME, arguments?.getString(S_HOUSENAME))
        bundle.putString(S_ROAD_NAME, arguments?.getString(S_ROAD_NAME))
        bundle.putString(S_PINCODE, arguments?.getString(S_PINCODE))
        bundle.putString(S_ADDDRESS_TYPE, arguments?.getString(S_ADDDRESS_TYPE))
        bundle.putString(S_LANDMARK, arguments?.getString(S_LANDMARK))
        bundle.putString(S_CITY, arguments?.getString(S_CITY))
        bundle.putString(S_COUNTRY, arguments?.getString(S_COUNTRY))
        bundle.putString(S_NAME, arguments?.getString(S_NAME))
        bundle.putString(S_EMAIL, arguments?.getString(S_EMAIL))
        bundle.putString(S_BUILDINGNAME, arguments?.getString(S_BUILDINGNAME))
        bundle.putString(S_CONTACTNO, arguments?.getString(S_CONTACTNO))
        bundle.putString(Constants.CCURRENCY, arguments?.getString(Constants.CCURRENCY))
        val intent = Intent(activity, PayFortActivity::class.java)
        intent.putExtras(bundle)
        startActivityForResult(intent, reqCode)
    }

    private fun cod() {

        Constants.addressSelected = null
        Constants.shipAddressSelected = null

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
                name = arguments?.getString(NAME),
                email = arguments?.getString(EMAIL),
                phone = arguments?.getString(CONTACTNO),
                building = arguments?.getString(BUILDINGNAME),
                s_address_type = arguments?.getString(S_ADDDRESS_TYPE),
                s_housename = arguments?.getString(S_HOUSENAME),
                s_road_name = arguments?.getString(S_ROAD_NAME),
                s_landmark = arguments?.getString(S_LANDMARK),
                s_pincode = arguments?.getString(S_PINCODE),
                s_country = arguments?.getString(S_COUNTRY),
                s_city = arguments?.getString(S_CITY),
                s_name = arguments?.getString(S_NAME),
                s_phone = arguments?.getString(S_CONTACTNO),
                s_email = arguments?.getString(S_EMAIL),
                s_building = arguments?.getString(S_BUILDINGNAME)
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