package com.estrrado.vinner.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Preferences.ADDRESS_ID
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.testpay.Constants
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_address_add.*
import kotlinx.android.synthetic.main.fragment_address_add.SaveBtn
import kotlinx.android.synthetic.main.fragment_address_add.addrsstype
import kotlinx.android.synthetic.main.fragment_address_add.check_default
import kotlinx.android.synthetic.main.fragment_address_add.tv_landmark
import kotlinx.android.synthetic.main.fragment_address_add.tv_roadname
import kotlinx.android.synthetic.main.fragment_address_add.tv_zipcode
import kotlinx.android.synthetic.main.fragment_address_add.tvaddress
import kotlinx.android.synthetic.main.fragment_address_list.*
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.toolbar_back.*

class EditFragment : Fragment() {
    var vModel: HomeVM? = null
    var AddressType:String?=null
    var Housename:String?=null
    var Pincode:String?=null
    var Roadname:String?=null
    var Landmark:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AddressType=arguments?.getString(ADDDRESS_TYPE, "").toString()
        Housename=arguments?.getString(HOUSENAME, "").toString()
       Pincode=arguments?.getString(PINCODE, "").toString()
       Roadname=arguments?.getString(ROAD_NAME, "").toString()
        Landmark= arguments?.getString(LANDMARK, "").toString()

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
        pageTitle.text="Edit Address"

    }

    private fun initControll(){

        addrssEtype.setText(AddressType)
        tvEaddress.setText(Housename)
        tv_Ezipcode.setText(Pincode)
        tv_Eroadname.setText(Roadname)
        tv_Elandmark.setText(Landmark)

        SaveeditBtn.setOnClickListener {
            progresseditaddress.visibility=View.VISIBLE
            if (Helper.isNetworkAvailable(requireContext())){
                vModel!!.getEaddress(
                    RequestModel
                    (accessToken = Preferences.get(activity, ACCESS_TOKEN),
                        address_type = addrssEtype.text.toString(),
                        address_id = Preferences.get(activity, ADDRESS_ID),
                     house_flat = tvEaddress.text.toString(),
                    zipcode = tv_Ezipcode.text.toString(),
                    road_name = tv_Eroadname.text.toString(),
                    landmark = tv_Elandmark.text.toString(),
                    default = if (check_default.isChecked) {
                        1
                    } else {
                        0
                    })).observe(requireActivity(),
                    Observer {
                        if (it!!.status=="success"){
                            progresseditaddress.visibility=View.GONE
                            Toast.makeText(context,"Address updated successfully", Toast.LENGTH_SHORT).show()
                            requireActivity().supportFragmentManager.beginTransaction().
                            replace(R.id.nav_host_fragment, Address_list()).commit()
                        }

                        else
                        {
                            if (it.message.equals("Invalid access token")) {
                                startActivity(Intent(activity, LoginActivity::class.java))
                                requireActivity().finish()
                            } else {
                                printToast(requireContext(), it.message!!)
                            }
                            printToast(this.requireContext(), it.message.toString())
                        }
                    }) }
            else
            {
                Toast.makeText(context,"No Network Available",Toast.LENGTH_SHORT).show()
                progresseditaddress.visibility=View.GONE
            }

        }
    }


}