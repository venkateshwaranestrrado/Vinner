package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
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
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.toolbar_back.*


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
    var json_string: String? = null
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
//        (activity as VinnerActivity).close()
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
//        Glide.with(this!!.requireActivity()!!)
//            .load(logo)
//            .thumbnail(0.1f)
//            .into(img_logo)
        productId = arguments?.getString(PRODUCT_ID, "").toString()
        pageTitle.setText("Product Detail")
        initControl()
        getProductdetail()
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
//            requestModel.productId = productId
            requestModel.productId = productId
            vModel!!.productDetail(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        progressproductdetail.visibility = View.GONE
                        setProductDetail(it!!.data!!)


                        if (it!!.data!!.getProduct()!!.return_policy != "") {
                            button3.setOnClickListener(object : View.OnClickListener {
                                override fun onClick(view: View) {
                                    val alertDialog: android.app.AlertDialog? =
                                        android.app.AlertDialog.Builder(activity)
                                            .create() //Read Update
                                    alertDialog!!.setTitle("Return Policy")
                                    alertDialog.setMessage(it.data!!.getProduct()!!.return_policy)
                                    alertDialog.setButton("Cancel",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            // here you can add functions
                                        })
                                    alertDialog.show() //<-- See This!
                                }
                            })
                        } else
                            printToast(
                                requireContext(),
                                "No Return Policy Available for this product"
                            )
                    } else
                        printToast(this!!.requireContext()!!, it?.message.toString())

                })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
            progressproductdetail.visibility = View.GONE
        }
    }

    private fun addToCart() {

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
            requestModel.productId = productId

            vModel!!.addCart(requestModel).observe(this,
                Observer {
                    printToast(this!!.requireContext()!!, it?.message.toString())
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

            if ((product.price == "null") || (product.price == "") || (product.price == "0")) {
                addcart.visibility = View.GONE
                buy.visibility = View.GONE
                Validation.printToast(requireContext(), "THE PRODUCT IS UNAVAILABLE IN THIS REGION")
            }
            if (product.productImage != null)
                Glide.with(this!!.requireActivity()!!)
                    .load(product.productImage!!.get(0))
                    .thumbnail(0.1f)
                    .into(ivProducts)
//            Helper.setLocation(spnr_region, this!!.requireContext()!!)
            productName.text = product.productName
            productDescription.text = product.category
            price.text = product.price + " " + product.currency
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
            if (detail.getReviews()!!.size > 0) {
                ratingBar2.rating = product.rating!!.toFloat()
                rating_total.rating = product.rating!!.toFloat()
                txt_rating_count.text = product.reatedCustomers + " " + "Customer ratings"
                txt_rating_total.text = product.rating + " " + "out of 5"
                recycle_rating.adapter = ReviewAdapter(
                    this!!.requireActivity()!!,
                    detail.getReviews()
                )
            }
        }


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

}
