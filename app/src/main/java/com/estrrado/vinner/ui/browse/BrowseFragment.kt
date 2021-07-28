package com.estrrado.vinner.ui.browse

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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.`interface`.AlertCallback
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.CategoryAdapter
import com.estrrado.vinner.adapters.IndustryAdapter
import com.estrrado.vinner.adapters.RegionAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.Datum
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.logo
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.readFromAsset
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.browse_fragment.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject

class BrowseFragment : Fragment(), AlertCallback {

    var vModel: HomeVM? = null
    var regionList: List<RegionSpinner>? = null
    var spnrSelected: Int = 0
    var spnrPosition: Int = 0

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

        if (VinnerActivity.notify_count > 0) {
            notifyCount.visibility = View.VISIBLE
            notifyCount.text = VinnerActivity.notify_count.toString()
        } else {
            notifyCount.visibility = View.GONE
        }

        recycle_cat.setLayoutManager(GridLayoutManager(context, 4))
        recycle_industry.setLayoutManager(GridLayoutManager(context, 3))

        searchtool.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_browse_to_searchFragment)
        }

        notifyView.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_browse_to_allNotification)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressbrowse.visibility = View.VISIBLE
        Glide.with(this.requireActivity())
            .load(logo)
            .thumbnail(0.1f)
            .into(img_logo)
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
                        Preferences.REGION_CODE
                    )) && (requireActivity() as VinnerActivity).getCartCount() > 0
                )
                    Helper.showAlert(
                        "If you change Region, Your cart items will be removed.",
                        1,
                        alertCallback = this@BrowseFragment,
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

        initControl()

    }

    override fun onResume() {
        super.onResume()
        if (!Preferences.get(activity, Preferences.COUNTRY_POSITION).equals("")) {
            spnrSelected = 0
            spnr_region.setSelection(
                Preferences.get(activity, Preferences.COUNTRY_POSITION)!!.toInt()
            )
        }
    }

    fun initControl() {
        getCategories()
        getIndustries()
    }

    private fun setCountry() {
        val code = regionList!!.get(spnrPosition).code
        val name = regionList!!.get(spnrPosition).name
        val fullname = regionList!!.get(spnrPosition).fullname
        Preferences.put(activity, Preferences.REGION_NAME, name)
        Preferences.put(activity, Preferences.REGION_FULLNAME, fullname)
        Preferences.put(activity, Preferences.COUNTRY_POSITION, spnrPosition.toString())
        Preferences.put(activity, Preferences.REGION_CODE, code)
    }

    private fun getCategories() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)

            vModel!!.getCategory(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        val json = Helper.getGson().toJson(it!!.data)
                        val list = Helper.getGson()
                            .fromJson(
                                json,
                                object : TypeToken<List<Datum>>() {}.type
                            ) as ArrayList<Datum>
                        progressbrowse.visibility = View.GONE
                        recycle_cat.adapter = CategoryAdapter(requireActivity(), null, list)
                    } else {
                        if (it?.message.equals("Invalid access token")) {
                            startActivity(Intent(activity, LoginActivity::class.java))
                            requireActivity().finish()
                        } else if (it?.httpcode == 402) {
                            val json = JSONObject(Helper.getGson().toJson(it.data))
                            Helper.showSingleAlert(
                                it.message ?: "",
                                requireContext(),
                                object : AlertCallback {
                                    override fun alertSelected(isSelected: Boolean, from: Int) {
                                        changeRegions(json.getString("country_code"))
                                    }
                                })
                        } else {
                            printToast(requireContext(), it?.message!!)
                        }
                        printToast(this!!.requireContext()!!, it?.message.toString())
                    }

                })
        } else {
            progressbrowse.visibility = View.GONE
            Toast.makeText(activity, "No Network Available", Toast.LENGTH_SHORT).show()

        }
    }

    private fun getIndustries() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)

            vModel!!.getIndustries(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        val json = Helper.getGson().toJson(it!!.data)
                        val list = Helper.getGson()
                            .fromJson(
                                json,
                                object : TypeToken<List<Datum>>() {}.type
                            ) as ArrayList<Datum>
                        progressbrowse.visibility = View.GONE
                        recycle_industry.adapter = IndustryAdapter(requireActivity(), list)
                    } else {
                        if (it?.message.equals("Invalid access token")) {
                            startActivity(Intent(activity, LoginActivity::class.java))
                            requireActivity().finish()
                        } else if (it?.httpcode == 402) {
                            val json = JSONObject(Helper.getGson().toJson(it.data))
                            Helper.showSingleAlert(
                                it.message ?: "",
                                requireContext(),
                                object : AlertCallback {
                                    override fun alertSelected(isSelected: Boolean, from: Int) {
                                        changeRegions(json.getString("country_code"))
                                    }
                                })
                        } else {
                            printToast(requireContext(), it?.message!!)
                        }
                        printToast(this!!.requireContext()!!, it?.message.toString())
                    }

                })
        } else {
            Toast.makeText(activity, "No Network Available", Toast.LENGTH_SHORT).show()
            progressbrowse.visibility = View.GONE
        }
    }

    override fun alertSelected(isSelected: Boolean, from: Int) {
        if (isSelected) {
            setCountry()
            if (Helper.isNetworkAvailable(requireContext())) {
                val requestModel = RequestModel()
                requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
                requestModel.cartId = "0"
                progressbrowse.visibility = View.VISIBLE
                vModel!!.emptyCart(requestModel).observe(requireActivity(),
                    Observer {
                        (activity as VinnerActivity).refreshBadgeView("0")
                        progressbrowse.visibility = View.GONE
                        printToast(requireContext(), it?.message!!)
                        initControl()
                    }

                )
            } else {
                progressbrowse.visibility = View.GONE
                Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
            }
        } else {
            spnrSelected = 0
            if (!Preferences.get(activity, Preferences.COUNTRY_POSITION).equals(""))
                spnr_region.setSelection(
                    Preferences.get(activity, Preferences.COUNTRY_POSITION)!!.toInt()
                )
        }
    }

    fun clearCart() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.cartId = "0"
            progressbrowse.visibility = View.VISIBLE
            vModel!!.emptyCart(requestModel).observe(requireActivity(),
                Observer {
                    progressbrowse.visibility = View.GONE
                    (activity as VinnerActivity).refreshBadgeView("0")
                }
            )
        } else {
            progressbrowse.visibility = View.GONE
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
                        Preferences.REGION_CODE
                    )
                ) {
                    spnrPosition = ind
                    //setCountry()
                    spnr_region.setSelection(ind)
                }
            }
        }
    }

    fun changeLocation() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
            progressbrowse.visibility = View.VISIBLE
            vModel!!.ChangeLocation(requestModel).observe(requireActivity(),
                Observer {
                    progressbrowse.visibility = View.GONE
                }
            )
        } else {
            progressbrowse.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

}
