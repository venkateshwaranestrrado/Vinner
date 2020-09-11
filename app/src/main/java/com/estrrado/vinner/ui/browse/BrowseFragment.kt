package com.estrrado.vinner.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.adapters.CategoryAdapter
import com.estrrado.vinner.adapters.IndustryAdapter
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.browse_fragment.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*


class BrowseFragment : Fragment() {

    var vModel: HomeVM? = null
    companion object {
        fun newInstance() = BrowseFragment()
    }

    private lateinit var viewModel: BrowseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(BrowseViewModel::class.java)
        val root = inflater.inflate(R.layout.browse_fragment, container, false)

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
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycle_cat.setLayoutManager(GridLayoutManager(context, 4))
        recycle_industry.setLayoutManager(GridLayoutManager(context, 3))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(this!!.activity!!)
            .load(logo)
            .thumbnail(0.1f)
            .into(img_logo)
        getCategories()
        getIndustries()
    }

    private fun getCategories() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)

        vModel!!.getCategory(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    recycle_cat.adapter = CategoryAdapter(requireActivity(), null, it!!.data )
                } else printToast(this!!.context!!, it?.message.toString())

            })
    }

    private fun getIndustries() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)

        vModel!!.getIndustries(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    recycle_industry.adapter = IndustryAdapter(requireActivity(), it!!.data )
                } else printToast(this!!.context!!, it?.message.toString())

            })
    }

}
