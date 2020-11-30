package com.estrrado.vinner.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Preferences.ADDRESS_ID
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_address_update.*
import kotlinx.android.synthetic.main.toolbar_back.*

class EditFragment : Fragment() {
    var vModel: HomeVM? = null
    var AddressType: String? = null
    var Housename: String? = null
    var Pincode: String? = null
    var Roadname: String? = null
    var Landmark: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_update, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AddressType = arguments?.getString(ADDDRESS_TYPE, "").toString()
        Housename = arguments?.getString(HOUSENAME, "").toString()
        Pincode = arguments?.getString(PINCODE, "").toString()
        Roadname = arguments?.getString(ROAD_NAME, "").toString()
        Landmark = arguments?.getString(LANDMARK, "").toString()

        initControll()

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
        pageTitle.text = "Edit Address"

    }

    private fun initControll() {

//        addrsstype.setText(AddressType)
        tvaddress.setText(Housename)
        tv_zipcode.setText(Pincode)
        tv_roadname.setText(Roadname)
        tv_landmark.setText(Landmark)

        SaveBtn.setOnClickListener {
            if (Helper.isNetworkAvailable(requireContext())) {
                progressaddress.visibility = View.VISIBLE
                vModel!!.getEaddress(
                    RequestModel
                        (
                        accessToken = Preferences.get(activity, ACCESS_TOKEN),
                        address_type = "addrsstype.text.toString()",
                        address_id = Preferences.get(activity, ADDRESS_ID),
                        house_flat = tvaddress.text.toString(),
                        zipcode = tv_zipcode.text.toString(),
                        road_name = tv_roadname.text.toString(),
                        landmark = tv_landmark.text.toString(),
                        default = if (check_default.isChecked) {
                            1
                        } else {
                            0
                        }
                    )
                ).observe(requireActivity(),
                    Observer {
                        progressaddress.visibility = View.GONE
                        if (it!!.status == "success") {
                            Toast.makeText(
                                context,
                                "Address updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, Address_list()).commit()
                        } else {
                            if (it.message.equals("Invalid access token")) {
                                startActivity(Intent(activity, LoginActivity::class.java))
                                requireActivity().finish()
                            } else {
                                printToast(requireContext(), it.message!!)
                            }
                            printToast(this.requireContext(), it.message.toString())
                        }
                    })
            } else {
                Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
            }

        }
    }


}