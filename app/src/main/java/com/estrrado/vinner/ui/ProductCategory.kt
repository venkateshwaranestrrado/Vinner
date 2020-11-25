package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.adapters.CategryList
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Constants.PRODUCT_NAME
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Preferences.get
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_address_list.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_product_category.*
import kotlinx.android.synthetic.main.item_categry_list.*
import kotlinx.android.synthetic.main.item_home_product.*

import kotlinx.android.synthetic.main.item_product.*
import kotlinx.android.synthetic.main.toolbar_back.*
import org.w3c.dom.Text
import java.util.prefs.Preferences


class ProductCategory : Fragment() {
    private var vModel: HomeVM? = null
    var productId: String = ""
    var categoryId: String = ""
    var categoryName:String=""
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
progresscategorylist.visibility=View.VISIBLE
        initcontroll()
        categoryId = arguments?.getString(PRODUCT_ID)!!
        categoryName=arguments?.getString(PRODUCT_NAME)!!
        pageTitle.text=categoryName
    }

    private fun initcontroll(){
        categoryId = arguments?.getString(PRODUCT_ID)!!
        categoryName=arguments?.getString(PRODUCT_NAME)!!
        val requestModel = RequestModel()
        requestModel.accessToken = get(activity, ACCESS_TOKEN)
        requestModel.countryCode=get(activity, REGION_NAME)
        requestModel.category_id=categoryId
        vModel!!.getCategorylist(requestModel)
            .observe(requireActivity(),
            Observer {
                if(it!!.data!!.size>0) {
                    progresscategorylist.visibility = View.GONE

                    recy_categry_lst.adapter = CategryList(requireActivity(), it!!.data,view)
                    recy_categry_lst.layoutManager = (GridLayoutManager(activity, 2))

                }
                else{
                    progresscategorylist.visibility = View.GONE
                    emptylist.visibility=View.VISIBLE
                }

            })

            }


        }



