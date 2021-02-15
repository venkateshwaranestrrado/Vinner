package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.ProductImageAdapter
import com.estrrado.vinner.adapters.RelatedProuctsAdapter
import com.estrrado.vinner.adapters.ReviewAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Data
import com.estrrado.vinner.helper.ClickListener
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.shareLink
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.Validation.printToastCenter
import com.estrrado.vinner.helper.priceFormat
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_product_details.price
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.toolbar_back.*

class ProductDetails : Fragment(), View.OnClickListener {

    var vModel: HomeVM? = null
    var productId: String = ""
    var json_string: String? = null

    var cartQty = 0
    var stockQty = 0
    var prod_id = ""
    var prod_name = ""
    var return_policy = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        progressproductdetail.visibility = View.VISIBLE
        productId = arguments?.getString(PRODUCT_ID, "").toString()

        Log.e("productId", productId.toString())

        pageTitle.setText("Product Detail")
        initControl()
        getProductdetail()

        txt_see_all.setOnClickListener {
            gotoAllReview()
        }

        txt_see_all_arrow.setOnClickListener {
            gotoAllReview()
        }

        button3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                var msg = "No Return Policy available for " + prod_name
                if (return_policy != "") {
                    msg = return_policy
                }
                val alertDialog: android.app.AlertDialog? =
                    android.app.AlertDialog.Builder(activity)
                        .create()
                alertDialog!!.setTitle("Return Policy")
                alertDialog.setMessage(msg)
                alertDialog.setButton("Cancel",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                alertDialog.show()
            }
        })

    }

    fun gotoAllReview() {
        (requireActivity() as VinnerActivity).reviews?.let {
            if (it.size > 0) {
                requireActivity().findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.action_navigation_product_to_allReviews)
            } else {
                printToastCenter(requireContext(), "No Reviews Found.")
            }
        }
    }

    private fun initControl() {
        imageView14.setOnClickListener(object : ClickListener() {
            override fun onOneClick(v: View) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "VINNER")
                val shareMessage = "$shareLink"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Share VINNER using"))
            }
        })
        addcart.setOnClickListener(this)
        buy.setOnClickListener(this)
    }

    private fun getProductdetail() {

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
            requestModel.productId = productId
            vModel!!.productDetail(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        progressproductdetail.visibility = View.GONE
                        setProductDetail(it!!.data!!)
                        return_policy = ""
                        it.data!!.getProduct()!!.return_policy?.let {
                            if (it != "") {
                                return_policy = it
                            } else {
                                printToast(
                                    requireContext(),
                                    "No Return Policy available for this product"
                                )
                            }
                        }
                    } else
                        printToast(this.requireContext(), it?.message.toString())
                })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
            progressproductdetail.visibility = View.GONE
        }
    }

    private fun validation(): Boolean {
        if (cartQty >= stockQty) {
            return false
        }
        return true
    }

    private fun addToCart(type: View?) {

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
            requestModel.productId = productId

            vModel!!.addCart(requestModel).observe(this,
                Observer {
                    if (cartQty <= 0) {
                        (activity as VinnerActivity).refreshBadgeView(1)
                    }
                    cartQty += 1
                    if (type != null) {
                        Navigation.findNavController(requireView()).navigate(R.id.navigation_cart)
                    } else {
                        printToast(this.requireContext(), it?.message.toString())
                    }
                    //(activity as VinnerActivity).getCartItemCount()
                    if (it?.status.equals(SUCCESS)) {
                        progressproductdetail.visibility = View.GONE
                        setProductDetail(it!!.data!!)
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
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProductDetail(detail: Data?) {
        val product = detail!!.getProduct()

        if (product != null) {
            progressproductdetail.visibility = View.GONE

            (requireActivity() as VinnerActivity).reviews = detail.getReviews()

            product.productId?.let {
                prod_id = it
                if (cartQty <= 0)
                    getCart()
            }
            product.productName?.let {
                prod_name = it
            }
            if (product.current_stock != "") {
                product.current_stock?.let {
                    stockQty = it.toInt()
                }
            }

            if ((product.price == "null") || (product.price == "") || (product.price == "0")) {
                addcart.visibility = View.GONE
                buy.visibility = View.GONE
                printToast(requireContext(), "THE PRODUCT IS UNAVAILABLE IN THIS REGION")
            } else {
                addcart.visibility = View.VISIBLE
                buy.visibility = View.VISIBLE
            }
            if (product.productImage != null) {
                setProductImgs(product.productImage)
            }
            productName.text = product.productName
            productDescription.text = product.category
            price.text = product.currency + " " + priceFormat(product.price)

            tvWeight.text = "Product Weight : " + product.weight
            tvLength.text = "Product Length : " + product.length
            tvWidth.text = "Product Width : " + product.width
            tvHeight.text = "Product Height : " + product.height

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvdescription.setText(
                    Html.fromHtml(
                        product.description.toString(),
                        Html.FROM_HTML_MODE_COMPACT
                    )
                );
            } else {
                tvdescription.setText(Html.fromHtml(product.description.toString()));
            }

            //if (detail.getReviews()!!.size > 0) {
            ratingBar2.rating = product.rating!!.toFloat()
            rating_total.rating = product.rating!!.toFloat()
            txt_rating_count.text = product.reatedCustomers + " " + "Customer ratings"
            product.rating?.let {
                if (it != "") {
                    it.toDouble().also {
                        txt_rating_total.text =
                            (if (it > it.toInt()) String.format(
                                "%s",
                                it
                            ) else String.format("%d", it.toInt())) + " out of 5"
                    }
                }
            }
            recycle_rating.adapter = ReviewAdapter(
                this!!.requireActivity()!!,
                detail.getReviews()
            )
        }

        detail.getRelatedProducts()?.let {
            if (detail.getRelatedProducts()!!.size > 0) {
                recycle_related_prod.layoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                recycle_related_prod.adapter = RelatedProuctsAdapter(
                    requireActivity(), detail.getRelatedProducts()!!
                )
            }
        }

    }

    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.addcart -> {
                if (validation()) {
                    addToCart(null)
                } else {
                    printToast(requireContext(), "Out Of Stock!")
                }
            }

            R.id.buy -> {
                if (validation()) {
                    addToCart(v)
                } else {
                    Navigation.findNavController(v).navigate(R.id.navigation_cart)
                }
            }

        }

    }

    private fun readFromAsset(): List<RegionSpinner> {
        val file_name = "login_region.json"
        val bufferReader = requireActivity()!!.assets.open(file_name).bufferedReader()
        json_string = bufferReader.use {
            it.readText()
        }
        val gson = Gson()
        val modelList: List<RegionSpinner> = gson.fromJson(
            json_string,
            Array<RegionSpinner>::class.java
        ).toList()
        return modelList
    }

    private fun getCart() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
            vModel!!.getCartPage(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        it?.data?.getCartItems()?.let {
                            for (item in it) {
                                if (item!!.productId == prod_id) {
                                    cartQty = item.productQuantity!!.toInt()
                                }
                            }
                        }
                    }
                })
        } else {
            progresscart.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    fun setProductImgs(images: List<String>?) {
        pager.adapter = ProductImageAdapter(requireActivity(), images)
        pager.setPageTransformer(false) { v, p ->
            val position = Math.abs(Math.abs(p) - 1)
            v.scaleX = position / 2 + 0.6f
            v.scaleY = position / 2 + 0.5f
        }
        tab.setupWithViewPager(pager)
        pager.currentItem = 0
    }

}
