package com.estrrado.vinner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_review.*
import kotlinx.android.synthetic.main.toolbar_back.*

class AddReview : Fragment() {

    var vModel: HomeVM? = null
    var productName: String? = null
    var productImage: String? = null
    var productId: String? = null
    var txtRatingValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_review, container, false)
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
        initControll()

    }

    private fun initControll() {

        ratingBar3.setOnRatingBarChangeListener({ ratingBar, rating, fromUser ->
            textView30.setText(rating.toString())
            txtRatingValue = rating.toString()
        })
        productImage = arguments?.getString(Preferences.PROFILEIMAGE)
        productName = arguments?.getString(Preferences.PRODUCTNAME)
        productId = arguments?.getString(Constants.PRODUCT_ID)

        val radius = requireActivity().resources.getDimensionPixelSize(R.dimen._15sdp)
        Glide.with(requireActivity())
            .load(productImage)
            .transform(RoundedCorners(radius))
            .thumbnail(0.1f)
            .into(imageView13)
        tv_prd_image_name.setText(productName)

        if (arguments?.getString("REVIEW_ID")?.toInt() != 0) {
            pageTitle.text = "Review"
            btn_submitt.visibility = View.GONE
            editTextTextPersonName2.isEnabled = false
            editTextTextPersonName3.isEnabled = false
            viewReview()
        } else {
            pageTitle.text = "Write Review"
            btn_submitt.visibility = View.VISIBLE
            editTextTextPersonName2.isEnabled = true
            editTextTextPersonName3.isEnabled = true
        }

        btn_submitt.setOnClickListener {

            progress_review.visibility = View.VISIBLE
            vModel!!.saveReview(
                RequestModel(
                    accessToken = Preferences.get(activity, Constants.ACCESS_TOKEN),
                    productId = productId,
                    review = editTextTextPersonName2.text.toString(),
                    rating = txtRatingValue,
                    title = editTextTextPersonName3.text.toString()
                )
            ).observe(requireActivity(), Observer {
                progress_review.visibility = View.GONE
                printToast(this.requireContext(), it?.message.toString())
                if (it?.status.equals(Constants.SUCCESS)) {
                    requireActivity().onBackPressed()
                }
            })

        }

    }

    fun viewReview() {
        progress_review.visibility = View.VISIBLE
        vModel!!.viewReview(
            RequestModel(
                accessToken = Preferences.get(activity, Constants.ACCESS_TOKEN),
                review_id = arguments?.getString("REVIEW_ID")
            )
        ).observe(requireActivity(), Observer {
            progress_review.visibility = View.GONE
            printToast(this.requireContext(), it?.message.toString())
            if (it?.status.equals(Constants.SUCCESS)) {
                it?.data?.review?.let {
                    editTextTextPersonName2.setText(it.review)
                    editTextTextPersonName3.setText(it.review_title)
                    if (it.rating != "") {
                        ratingBar3.rating = it.rating!!.toFloat()
                    }
                }
            }
        })
    }

}