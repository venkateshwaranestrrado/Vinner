package com.estrrado.vinner.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.os.bundleOf
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
import com.estrrado.vinner.activity.VinnerActivity.Companion.notify_count
import com.estrrado.vinner.activity.VinnerActivity.Companion.shareProdId
import com.estrrado.vinner.adapters.CategoryAdapter
import com.estrrado.vinner.adapters.ProductsAdapter
import com.estrrado.vinner.adapters.RegionAdapter
import com.estrrado.vinner.adapters.SliderAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.BannerSlider
import com.estrrado.vinner.data.models.Category
import com.estrrado.vinner.data.models.Featured
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.CART_ID
import com.estrrado.vinner.helper.Constants.FROM_LOGIN
import com.estrrado.vinner.helper.Constants.PROFILEMAIL
import com.estrrado.vinner.helper.Constants.PROFILENAME
import com.estrrado.vinner.helper.Constants.PROFILE_IMAGE
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.logo
import com.estrrado.vinner.helper.Constants.regions
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Helper.showAlert
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Preferences.COUNTRY_POSITION
import com.estrrado.vinner.helper.Preferences.REGION_CODE
import com.estrrado.vinner.helper.Preferences.REGION_FULLNAME
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.readFromAsset
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class HomeFragment : Fragment(), AlertCallback {

    var vModel: HomeVM? = null
    var mTimer = Timer()
    var timerLoad: Boolean = true
    var spnrSelected: Int = 0
    var spnrPosition: Int = 0
    var cartCount: Int = 0
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
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).open()
        cartCount = 0

        searchtool.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_home_to_searchFragment)
        }

        notifyView.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_home_to_allNotification)
        }

        spnr_region.visibility = View.VISIBLE
        regionList = readFromAsset(requireActivity())
        val regionAdapter = RegionAdapter(requireContext(), regionList!!)
        spnr_region.adapter = regionAdapter

        spnr_region.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                Constants.addressSelected = null
                Constants.shipAddressSelected = null
                spnrPosition = position
                if ((regionList!!.get(spnrPosition).code != Preferences.get(
                        activity,
                        REGION_CODE
                    )) && cartCount > 0
                )
                    showAlert(
                        "If you change Region, Your cart items will be removed.",
                        1,
                        alertCallback = this@HomeFragment,
                        context = requireContext()
                    )
                else {
                    setCountry()
                    initControl()
                    changeLocation()
                }
                spnrSelected = spnrSelected + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        tv_prod_see_all.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_home_to_productListFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        if (!Preferences.get(activity, COUNTRY_POSITION).equals("")) {
            spnrSelected = 0
            spnr_region.setSelection(Preferences.get(activity, COUNTRY_POSITION)!!.toInt())
        }
    }

    private fun setCountry() {
        val code = regionList!!.get(spnrPosition).code
        val name = regionList!!.get(spnrPosition).name
        val fullname = regionList!!.get(spnrPosition).fullname
        Preferences.put(activity, REGION_NAME, name)
        Preferences.put(activity, REGION_FULLNAME, fullname)
        Preferences.put(activity, COUNTRY_POSITION, spnrPosition.toString())
        Preferences.put(activity, Preferences.REGION_CODE, code!!)
    }

    private fun initControl() {
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

                        Preferences.put(activity, PROFILEMAIL, "")
                        Preferences.put(activity, PROFILE_IMAGE, "")
                        Preferences.put(activity, PROFILENAME, "")
                        it.data?.profiledata?.let { profiledata ->
                            profiledata.image?.let {
                                Preferences.put(activity, PROFILE_IMAGE, it)
                            }
                            profiledata.name?.let {
                                Preferences.put(activity, PROFILENAME, it)
                            }
                            profiledata.email?.let {
                                Preferences.put(activity, PROFILEMAIL, it)
                            }
                        }

                        (activity as VinnerActivity).refreshBadgeView(it.data!!.cartcount)
                        progresshome.visibility = View.GONE
                        regions = it!!.data!!.regions!!
                        setBannerImgs(it!!.data!!.bannerSlider)
                        setProducts(it.data!!.featured)
                        setCategories(it.data!!.categories)
                        logo = it!!.data?.logo!!
                        if (it.data.cartcount != null && !it.data.cartcount.equals("")) {
                            cartCount = it.data!!.cartcount!!.toInt()
                            if (it.data.cartcount!!.toInt() > 0 && FROM_LOGIN == 1) {
                                FROM_LOGIN = 0
                                checkCartRegion()
                            } else if (it.data.cartcount!!.toInt() <= 0) {
                                FROM_LOGIN = 0
                                clearCart()
                                gotoShareProduct()
                            } else {
                                gotoShareProduct()
                            }
                        }
                        Glide.with(this!!.requireActivity()!!)
                            .load(logo)
                            .thumbnail(0.1f)
                            .into(img_logo)

                        notify_count = it.data.notify_count
                        if (notify_count > 0) {
                            notifyCount.visibility = View.VISIBLE
                            notifyCount.text = notify_count.toString()
                        } else {
                            notifyCount.visibility = View.GONE
                        }

                    } else {
                        if (it?.message.equals("Invalid access token")) {
                            progresshome.visibility = View.GONE
                            startActivity(Intent(activity, LoginActivity::class.java))
                            requireActivity().finish()
                        } else if (it?.httpcode == 402) {
                            Helper.showSingleAlert(
                                it.message ?: "",
                                requireContext(),
                                object : AlertCallback {
                                    override fun alertSelected(isSelected: Boolean, from: Int) {
                                        changeRegions(it.data?.country_code!!)
                                    }
                                })
                        } else {
                            printToast(requireContext(), it?.message!!)
                        }
                    }
                }

            )
        } else {
            progresshome.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    fun gotoShareProduct() {
        if (shareProdId != "") {
            view?.findNavController()
                ?.navigate(
                    R.id.action_homeFragment_to_ProductFragment,
                    bundleOf(Constants.PRODUCT_ID to shareProdId)
                )
        }
    }

    private fun checkCartRegion() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            vModel!!.getCartPage(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {

                        it?.data?.getCart()?.currency?.let { currency ->
                            val indx =
                                regionList?.indexOfFirst { model -> currency.contains(model.name) }
                            indx?.let { ind ->
                                if (ind >= -1) {
                                    if (regionList!!.get(ind).code != Preferences.get(
                                            activity,
                                            REGION_CODE
                                        )
                                    ) {
                                        spnrPosition = ind
                                        setCountry()
                                        spnr_region.setSelection(ind)
                                    } else {
                                        gotoShareProduct()
                                    }
                                }
                            }
                        }

                        /*for (i in 0 until regionList!!.size) {
                            if (it!!.data!!.getCart()!!.currency.toString().contains(
                                    regionList!!.get(i).name,
                                    true
                                )
                            ) {
                                if (regionList!!.get(i).code != Preferences.get(
                                        activity,
                                        REGION_CODE
                                    )
                                ) {
                                    spnrPosition = i
                                    setCountry()
                                    spnr_region.setSelection(i)
                                }
                            }
                        }*/
                    }
                })
        }
    }

    private fun setCategories(categories: List<Category>?) {
        categoryList.adapter = CategoryAdapter(requireActivity(), categories, null)
    }

    private fun setProducts(featured: List<Featured>?) {
        homeList.adapter = ProductsAdapter(this.requireActivity(), featured, null, view)
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

    override fun alertSelected(isSelected: Boolean, from: Int) {
        if (isSelected) {
            setCountry()
            if (Helper.isNetworkAvailable(requireContext())) {
                val requestModel = RequestModel()
                requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
                requestModel.cartId = "0"
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

    fun clearCart() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.cartId = "0"
            progresshome.visibility = View.VISIBLE
            vModel!!.emptyCart(requestModel).observe(requireActivity(),
                Observer {
                    progresshome.visibility = View.GONE
                    (activity as VinnerActivity).refreshBadgeView("0")
                }
            )
        } else {
            progresshome.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    fun changeLocation() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, REGION_NAME)
            progresshome.visibility = View.VISIBLE
            vModel!!.ChangeLocation(requestModel).observe(requireActivity(),
                Observer {
                    progresshome.visibility = View.GONE
                }
            )
        } else {
            progresshome.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    fun changeRegions(ccode: String) {
        val indx =
            regionList?.indexOfFirst { model -> ccode.contains(model.name) }
        indx?.let { ind ->
            if (ind >= -1) {
                if (regionList!!.get(ind).code != Preferences.get(
                        activity,
                        REGION_CODE
                    )
                ) {
                    spnrPosition = ind
                    //setCountry()
                    spnr_region.setSelection(ind)
                } else {
                    gotoShareProduct()
                }
            }
        }
    }

}


