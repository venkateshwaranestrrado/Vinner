package com.estrrado.vinner.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.ReviewAdapter
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Data
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.toolbar.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProductDetails : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var vModel: HomeVM? = null
    var productId: String = ""

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
        val root = inflater.inflate(R.layout.fragment_product_details, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).close()
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
        Glide.with(this!!.activity!!)
            .load(logo)
            .thumbnail(0.1f)
            .into(img_logo)
        productId = arguments?.getString(PRODUCT_ID)!!
        initControl()
        getProductdetail()
    }

    private fun initControl() {
        addcart.setOnClickListener(this)
        buy.setOnClickListener(this)
    }

    private fun getProductdetail() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.countryCode = "AE"
        requestModel.productId = productId

        vModel!!.productDetail(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    setProductDetail(it!!.data!!)
                } else printToast(this!!.context!!, it?.message.toString())

            })
    }

    private fun addToCart() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.countryCode = "AE"
        requestModel.productId = productId

        vModel!!.addCart(requestModel).observe(this,
            Observer {
                printToast(this!!.context!!, it?.message.toString())
                if (it?.status.equals(SUCCESS)) {
                    setProductDetail(it!!.data!!)
                }
            })
    }

    private fun setProductDetail(detail: Data?) {
        val product = detail!!.getProduct()
        if (product != null) {
            if (product.productImage != null)
                Glide.with(this!!.activity!!)
                    .load(product.productImage!!.get(0))
                    .thumbnail(0.1f)
                    .into(ivProducts)
            Helper.setLocation(spnr_region, this!!.context!!)
            productName.text = product.productName
            productDescription.text = product.category
            price.text = product.price + " " + product.currency
            tvdescription.text = product.description
            ratingBar2.rating = product.rating!!.toFloat()
            rating_total.rating = product.rating!!.toFloat()
            txt_rating_count.text = product.reatedCustomers + " " + "Customer ratings"
            txt_rating_total.text = product.rating + " " + "out of 5"
        }
        recycle_rating.adapter = ReviewAdapter(this!!.activity!!, detail.getReviews())

    }


    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.addcart -> {
                addToCart()
            }

            R.id.buy -> {
                addToCart()
                Navigation.findNavController(v).navigate(
                    R.id.navigation_cart

                )
            }

        }

    }

}
