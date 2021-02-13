package com.estrrado.vinner.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.retrofit.ApiClient.gson
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.toolbar_prev.*
import kotlinx.android.synthetic.main.track_order.*

class TrackFragment : Fragment() {

    var vModel: HomeVM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.track_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageTitle.text = "Track Order"

        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(activity, ApiClient.apiServices!!)
                )
            )
        ).get(HomeVM::class.java)
//21021277
        tvTrack.setOnClickListener {
            if (etOrderId.text.trim().length > 0) {
                loadValues()
            }
        }

    }

    fun loadValues() {
        progressTrack.visibility = View.VISIBLE
        vModel!!.trackOrder(
            RequestModel(
                accessToken = Preferences.get(activity, Constants.ACCESS_TOKEN),
                order_id = etOrderId.text.toString()
            )
        ).observe(requireActivity(),
            androidx.lifecycle.Observer {
                progressTrack.visibility = View.GONE
                if (it?.status == "success") {
                    etOrderId.setText("")
                    Validation.hideKeyboard(requireActivity())
                    val bundle =
                        bundleOf("data" to gson.toJson(it.data?.track_order))
                    view?.findNavController()
                        ?.navigate(R.id.action_navigation_track_to_trackOrderDetail, bundle)
                } else {
                    Validation.printToast(requireContext(), it!!.message!!)
                }
            })

    }

}