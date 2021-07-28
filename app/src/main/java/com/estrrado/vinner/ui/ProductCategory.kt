package com.estrrado.vinner.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.`interface`.AlertCallback
import com.estrrado.vinner.adapters.CategryList
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Constants.PRODUCT_NAME
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Preferences.get
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_product_category.*
import kotlinx.android.synthetic.main.toolbar_back.*
import org.json.JSONObject

class ProductCategory : Fragment() {

    private var vModel: HomeVM? = null
    var productId: String = ""
    var categoryId: String = ""
    var categoryName: String = ""

    var adapter: CategryList? = null

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

        val searchTextId: Int = searchView.getContext().getResources()
            .getIdentifier("android:id/search_src_text", null, null)
        val searchText = searchView.findViewById<TextView>(searchTextId)
        if (searchText != null) {
            searchText.setTextColor(Color.WHITE)
            searchText.setHintTextColor(Color.WHITE)
        }

        searchView.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                imageView8.visibility = View.VISIBLE
                pageTitle.visibility = View.VISIBLE
                searchView.visibility = View.GONE
                return false
            }
        })

        imageView8.setOnClickListener {
            imageView8.visibility = View.GONE
            pageTitle.visibility = View.GONE
            searchView.visibility = View.VISIBLE
            searchView.isIconified = false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                adapter?.let {
                    it.filter.filter(p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter?.let {
                    it.filter.filter(p0)
                }
                return false
            }

        })

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
                    if (it?.status.equals(Constants.SUCCESS)) {
                        if (it!!.data!!.size > 0) {
                            progresscategorylist.visibility = View.GONE
                            adapter = CategryList(requireActivity(), it!!.data, view)
                            recy_categry_lst.adapter = adapter
                            recy_categry_lst.layoutManager = (GridLayoutManager(activity, 2))

                        } else {
                            progresscategorylist.visibility = View.GONE
                            emptylist.visibility = View.VISIBLE
                        }
                    } else {
                        if (it?.httpcode == 402) {
                            val json = JSONObject(Helper.getGson().toJson(it.data))
                            Helper.showSingleAlert(
                                it.message ?: "",
                                requireContext(),
                                object : AlertCallback {
                                    override fun alertSelected(isSelected: Boolean, from: Int) {
                                        requireActivity().onBackPressed()
                                    }
                                })
                        } else {
                            Validation.printToast(requireContext(), it?.message!!)
                        }
                    }
                })

    }


}



