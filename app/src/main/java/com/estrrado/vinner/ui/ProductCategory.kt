package com.estrrado.vinner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.adapters.CategryList
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Constants.PRODUCT_NAME
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Preferences.get
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_product_category.*
import kotlinx.android.synthetic.main.toolbar_back.*

class ProductCategory : Fragment() {
    private var vModel: HomeVM? = null
    var productId: String = ""
    var categoryId: String = ""
    var categoryName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_product_category, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progresscategorylist.visibility = View.VISIBLE
        initcontroll()
        categoryId = arguments?.getString(PRODUCT_ID)!!
        categoryName = arguments?.getString(PRODUCT_NAME)!!
        pageTitle.text = categoryName
    }

    private fun initcontroll() {
        categoryId = arguments?.getString(PRODUCT_ID)!!
        categoryName = arguments?.getString(PRODUCT_NAME)!!
        val requestModel = RequestModel()
        requestModel.accessToken = get(activity, ACCESS_TOKEN)
        requestModel.countryCode = get(activity, REGION_NAME)
        requestModel.category_id = categoryId
        vModel!!.getCategorylist(requestModel)
            .observe(requireActivity(),
                Observer {
                    if (it!!.data!!.size > 0) {
                        progresscategorylist.visibility = View.GONE

                        recy_categry_lst.adapter = CategryList(requireActivity(), it!!.data, view)
                        recy_categry_lst.layoutManager = (GridLayoutManager(activity, 2))

                    } else {
                        progresscategorylist.visibility = View.GONE
                        emptylist.visibility = View.VISIBLE
                    }

                })

    }


}



