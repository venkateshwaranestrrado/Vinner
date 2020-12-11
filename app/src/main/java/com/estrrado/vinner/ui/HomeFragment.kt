package com.estrrado.vinner.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.`interface`.AlertCallback
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.CategoryAdapter
import com.estrrado.vinner.adapters.ProductsAdapter
import com.estrrado.vinner.adapters.RegionAdapter
import com.estrrado.vinner.adapters.SliderAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.BannerSlider
import com.estrrado.vinner.data.models.Category
import com.estrrado.vinner.data.models.Featured
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.CART_ID
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.logo
import com.estrrado.vinner.helper.Constants.regions
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Helper.showAlert
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Preferences.COUNTRY_POSITION
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.readFromAsset
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_notification_bage.*
import java.util.*


class HomeFragment : Fragment(), AlertCallback {
    var vModel: HomeVM? = null
    var mTimer = Timer()
    var timerLoad: Boolean = true
    var spnrSelected: Int = 0
    var spnrPosition: Int = 0
    var regionList: List<RegionSpinner>? = null

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

        searchtool.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, SearchFragment()).commit()
        }

        spnr_region.visibility = View.VISIBLE
        regionList = readFromAsset(requireActivity())
        val regionAdapter = RegionAdapter(requireContext()!!, regionList!!)
        spnr_region.adapter = regionAdapter
        if (!Preferences.get(activity, COUNTRY_POSITION).equals("")) {
            spnrSelected = 0
            spnr_region.setSelection(Preferences.get(activity, COUNTRY_POSITION)!!.toInt())
        }
//                        setLocation(spnr_region, this!!.requireContext()!!)

        spnr_region.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                spnrPosition = position
                if (spnrSelected != 0)
                    showAlert(
                        "If you change Region, Your cart items will be removed.",
                        1,
                        alertCallback = this@HomeFragment,
                        context = requireContext()
                    )
                else {
                    setCountry()
                    initControl()
                }
                spnrSelected = spnrSelected + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        tv_prod_see_all.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_productListFragment)
        }
    }

    private fun setCountry() {
        val code = regionList!!.get(spnrPosition).code
        val name = regionList!!.get(spnrPosition).name
        Preferences.put(activity, REGION_NAME, name!!)
        Preferences.put(activity, COUNTRY_POSITION, spnrPosition.toString())
        Preferences.put(activity, Preferences.REGION_CODE, code!!)
    }


    private fun initControl() {
        // Helper.showLoading(activity)
        categoryList.setLayoutManager(GridLayoutManager(activity, 4))
        homeList.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        getHome()

    }

    private fun getHome() {

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            progresshome.visibility = View.VISIBLE
            vModel!!.home(requestModel).observe(requireActivity(),
                Observer {
                    progresshome.visibility = View.GONE
                    if (it?.status.equals(SUCCESS)) {

                        if (it!!.data!!.cartid != null)
                            Preferences.put(activity, CART_ID, it.data!!.cartid.toString())
                        else
                            Preferences.put(activity, CART_ID, "0")
                        (activity as VinnerActivity).refreshBadgeView(it.data!!.cartcount)
                        progresshome.visibility = View.GONE
                        regions = it!!.data!!.regions!!
                        setBannerImgs(it!!.data!!.bannerSlider)
                        setProducts(it.data!!.featured)
                        setCategories(it.data!!.categories)
                        logo = it!!.data?.logo!!
                        Glide.with(this!!.requireActivity()!!)
                            .load(logo)
                            .thumbnail(0.1f)
                            .into(img_logo)
                    } else {
                        if (it?.message.equals("Invalid access token")) {
                            progresshome.visibility = View.GONE
                            startActivity(Intent(activity, LoginActivity::class.java))
                            requireActivity().finish()
                        } else {
                            printToast(requireContext(), it?.message!!)
                        }
                        printToast(this!!.requireContext()!!, it?.message.toString())
                    }
                }

            )
        } else {
            progresshome.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCategories(categories: List<Category>?) {
        categoryList.adapter = CategoryAdapter(requireActivity(), categories, null)
    }

    private fun setProducts(featured: List<Featured>?) {
        homeList.adapter = ProductsAdapter(this!!.requireActivity()!!, featured, null, view)
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

    override fun alertSelected(isSelected: Boolean) {
        if (isSelected) {
            setCountry()
            if (Helper.isNetworkAvailable(requireContext())) {
                if (Preferences.get(activity, CART_ID).equals("0")) {
                    initControl()
                    return
                }
                val requestModel = RequestModel()
                requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
                requestModel.cartId = Preferences.get(activity, CART_ID)
                progresshome.visibility = View.VISIBLE
                vModel!!.emptyCart(requestModel).observe(requireActivity(),
                    Observer {
                        progresshome.visibility = View.GONE
                        printToast(requireContext(), it?.message!!)
                        initControl()
                    }

                )
            } else {
                progresshome.visibility = View.GONE
                Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
            }
        } else {
            spnrSelected = 0
            if (!Preferences.get(activity, COUNTRY_POSITION).equals(""))
                spnr_region.setSelection(Preferences.get(activity, COUNTRY_POSITION)!!.toInt())
        }
    }

    override fun onResume() {
        super.onResume()
        spnrSelected = 0
    }

}


