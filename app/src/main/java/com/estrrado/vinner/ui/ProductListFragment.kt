package com.estrrado.vinner.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.adapters.ProductsAdapter
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.Validation.printToastCenter
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.android.synthetic.main.toolbar_back.*


class ProductListFragment : Fragment() {

    private var vModel: HomeVM? = null
    var adapter: ProductsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        pageTitle.setText("Featured Product List")
        recycle_products.setLayoutManager(GridLayoutManager(context, 2))
        getProductList()

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

    private fun getProductList() {

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            requestModel.limit = null
            requestModel.offset = 0
            progressproductlist.visibility = View.VISIBLE
            vModel!!.getFeatureProductList(requestModel).observe(requireActivity(),
                Observer {
                    progressproductlist.visibility = View.GONE
                    if (it?.status.equals(SUCCESS)) {
                        adapter =
                            ProductsAdapter(this.requireActivity(), null, it!!.data, view)
                        recycle_products.adapter = adapter
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
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
            progressproductlist.visibility = View.GONE
        }
    }


}