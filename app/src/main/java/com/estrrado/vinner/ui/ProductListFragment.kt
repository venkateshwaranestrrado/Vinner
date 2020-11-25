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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.adapters.ProductsAdapter
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Preferences.REGION_CODE
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_back.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var vModel: HomeVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageTitle.setText("Product List")
//        Glide.with(this!!.requireActivity()!!)
//            .load(logo)
//            .thumbnail(0.1f)
//            .into(img_logo)
        progressproductlist.visibility=View.VISIBLE
//        Helper.setLocation(spnr_region, this!!.requireContext()!!)
        recycle_products.setLayoutManager(GridLayoutManager(context, 2))
        getProductList()
    }

    private fun getProductList() {

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
           requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            requestModel.limit = null
            requestModel.offset = 0

            vModel!!.getProductList(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        progressproductlist.visibility=View.GONE
                        recycle_products.adapter =
                            ProductsAdapter(this!!.requireActivity()!!, null, it!!.data, view)
                    }

                    else
                    {
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
        else{
            Toast.makeText(context,"No Network Available", Toast.LENGTH_SHORT).show()
            progressproductlist.visibility=View.GONE
        }
    }


}