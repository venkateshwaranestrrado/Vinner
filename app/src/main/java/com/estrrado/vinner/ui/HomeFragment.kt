package com.estrrado.vinner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.CategoryAdapter
import com.estrrado.vinner.adapters.ProductsAdapter
import com.estrrado.vinner.adapters.SliderAdapter
import com.estrrado.vinner.data.models.BannerSlider
import com.estrrado.vinner.data.models.Category
import com.estrrado.vinner.data.models.Featured
import com.estrrado.vinner.data.models.Region
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.retrofit.ApiClient
import com.estrrado.vinner.helper.ACCESS_TOKEN
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.SUCCESS
import com.estrrado.vinner.helper.printToast
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    var vModel: HomeVM? = null
    var mTimer = Timer()
    var timerLoad: Boolean = true

    override fun onResume() {
        super.onResume()
        initControl()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        // val textView: TextView = root.findViewById(R.id.text_home)
        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             //textView.text = it
         })*/

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).open()
        initControl()
        tv_prod_see_all.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_productListFragment)
        }
    }

    private fun initControl() {
        // Helper.showLoading(activity)
        categoryList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        homeList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        getHome()

    }

    private fun getHome() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.countryCode = "AE"

        vModel!!.home(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    setLocation(it!!.data!!.regions)
                    setBannerImgs(it!!.data!!.bannerSlider)
                    setProducts(it.data!!.featured)
                    setCategories(it.data!!.categories)
                    Glide.with(this!!.activity!!)
                        .load(it!!.data?.logo)
                        .thumbnail(0.1f)
                        .into(search)
                } else printToast(this!!.context!!, it?.message.toString())

            })
    }

    private fun setLocation(region: List<Region>?) {

        var regions:ArrayList<String> = ArrayList<String>()

        for(i in 0 until region!!.size){
            regions.add(region.get(i).countryName!!)
        }

        val aa = ArrayAdapter(this!!.context!!, R.layout.spinner_item, regions!!.toTypedArray())
        spnr_region.adapter = aa
    }

    private fun setCategories(categories: List<Category>?) {
        categoryList.adapter = CategoryAdapter(requireActivity(), categories)
    }

    private fun setProducts(featured: List<Featured>?) {
        homeList.adapter = ProductsAdapter(this!!.activity!!, featured, null, view)
    }

    private fun setBannerImgs(bannerSlider: List<BannerSlider>?) {
        pager.adapter = SliderAdapter(requireActivity(), bannerSlider)
        pager.setPageTransformer(false) { v, p ->
            var position = Math.abs(Math.abs(p) - 1)
            v.scaleX = position / 2 + 0.6f
            v.scaleY = position / 2 + 0.5f

        }
        timerPager(pager)

        pager.currentItem = 0
        tab.setupWithViewPager(pager)
    }

    override fun onDestroy() {

        timerLoad = true
        mTimer.cancel()
        super.onDestroy()

    }


    private fun timerPager(viewPager: ViewPager) {
        if (timerLoad) {
            timerLoad = false
            mTimer.cancel()
            mTimer.purge()
            mTimer = Timer()
            var mTt = object : TimerTask() {
                override fun run() {
                    activity!!.runOnUiThread(Runnable {
                        try {
                            if (viewPager.currentItem == viewPager.adapter!!.count - 1) {
                                viewPager.currentItem = 0
                            } else {
                                viewPager.currentItem = viewPager.currentItem + 1
                            }
                        } catch (e: Exception) {
                            mTimer.cancel()
                            mTimer.purge()
                        }
                    })
                }
            }
            try {
                mTimer.schedule(mTt, 300, 2000)
            } catch (e: Exception) {
                mTimer.cancel()
                mTimer.schedule(mTt, 300, 2000)
            }
        }
    }

}


