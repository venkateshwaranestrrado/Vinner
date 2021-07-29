package com.estrrado.vinner.ui.more

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.`interface`.AlertCallback
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.RegionAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.PROFILENAME
import com.estrrado.vinner.helper.Constants.PROFILE_IMAGE
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.browse_fragment.*
import kotlinx.android.synthetic.main.dialog_signout.*
import kotlinx.android.synthetic.main.moree_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_more.notifyCount
import kotlinx.android.synthetic.main.toolbar_more.notifyView
import kotlinx.android.synthetic.main.toolbar_more.searchtool

class MoreFragment : Fragment(), View.OnClickListener, AlertCallback {

    var vModel: HomeVM? = null
    var regionList: List<RegionSpinner>? = null
    var spnrSelected: Int = 0
    var spnrPosition: Int = 0

    companion object {
        fun newInstance() = MoreFragment()
    }

    private lateinit var viewModel: MoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.moree_fragment, container, false)

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as VinnerActivity).open()
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
                        alertCallback = this@MoreFragment,
                        context = requireContext()
                    )
                else {
                    if ((regionList!!.get(spnrPosition).code != Preferences.get(
                            activity,
                            Preferences.REGION_CODE
                        ))
                    ) {
                        setCountry()
                        changeLocation()
                    } else {
                        setCountry()
                    }
                }
                spnrSelected = spnrSelected + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initControl()

        if (VinnerActivity.notify_count > 0) {
            notifyCount.visibility = View.VISIBLE
            notifyCount.text = VinnerActivity.notify_count.toString()
        } else {
            notifyCount.visibility = View.GONE
        }

        /*vModel!!.getProfile(
            RequestModel(accessToken = Preferences.get(activity, ACCESS_TOKEN))

        ).observe(requireActivity(),
            Observer {
                if (it!!.status == "success") {
                    Glide.with(this)
                        .load(it.data!!.path)
                        .placeholder(R.drawable.profile)
                        .thumbnail(0.1f)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ivprofilemorephoto)
                    tvProfileName.setText(it.data!!.name)

                }
            })*/

        terms.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse("https://vinshopify.com/home/legal/terms_conditions")
                startActivity(intent)
            }
        })

        share.setOnClickListener(object : ClickListener() {
            override fun onOneClick(v: View) {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vinshopify")
                    val shareMessage =
                        "https://vinshopify.page.link/N8fh"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                } catch (e: Exception) {
                    //e.toString();
                }
            }
        })

        lyt_logout.setOnClickListener {
            progressmore.visibility = View.VISIBLE
            val mbuilder = AlertDialog.Builder(view?.context)
            val dialogview =
                LayoutInflater.from(view?.context).inflate(R.layout.dialog_signout, null, false);
            mbuilder.setView(dialogview)
            val malertDialog = mbuilder.show()
            malertDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
            malertDialog?.yes?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    progressmore.visibility = View.GONE
                    signout()
                    malertDialog.cancel()
                }
            })
            malertDialog?.no?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    progressmore.visibility = View.GONE
                    malertDialog.cancel()
                }

            })
        }

        searchtool.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_more_to_searchFragment)
        }

        notifyView.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_more_to_allNotification)
        }

        trackOrder.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_more_to_navigation_track)
        }

        Glide.with(requireContext())
            .load(Constants.logo)
            .thumbnail(0.1f)
            .into(img_logo)

    }

    private fun signout() {
        progressmore.visibility = View.VISIBLE
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)

            vModel!!.signout(requestModel).observe(this,
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        progressmore.visibility = View.GONE
                        Preferences.put(requireActivity(), Constants.IS_LOGIN, "Is Login")
                        startActivity(Intent(activity, LoginActivity::class.java))
                        requireActivity().finish()
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
            progressmore.visibility = View.GONE
        }
    }

    private fun initControl() {
        Glide.with(this)
            .load(Preferences.get(activity, PROFILE_IMAGE))
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ivprofilemorephoto)
        tvProfileName.setText(Preferences.get(activity, PROFILENAME))
        profilelyt.setOnClickListener(this)
        myorderslyt.setOnClickListener(this)
        deliveryaddressfragment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.profilelyt -> {
                Navigation.findNavController(v).navigate(R.id.navigation_editprofile)
            }
            R.id.myorderslyt -> {
                Navigation.findNavController(v).navigate(R.id.orderList)
            }
            R.id.deliveryaddressfragment -> {
                Navigation.findNavController(v).navigate(R.id.address_list)
            }
        }
    }

    override fun alertSelected(isSelected: Boolean, from: Int) {
        if (isSelected) {
            setCountry()
            if (Helper.isNetworkAvailable(requireContext())) {
                val requestModel = RequestModel()
                requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
                requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
                requestModel.cartId = "0"
                progressmore.visibility = View.VISIBLE
                vModel!!.emptyCart(requestModel).observe(requireActivity(),
                    Observer {
                        (activity as VinnerActivity).refreshBadgeView("0")
                        progressmore.visibility = View.GONE
                        printToast(requireContext(), it?.message!!)
                    }

                )
            } else {
                progressmore.visibility = View.GONE
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

    override fun onResume() {
        super.onResume()
        if (!Preferences.get(activity, Preferences.COUNTRY_POSITION).equals("")) {
            spnrSelected = 0
            spnr_region.setSelection(
                Preferences.get(activity, Preferences.COUNTRY_POSITION)!!.toInt()
            )
        }
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

    fun changeLocation() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
            progressmore.visibility = View.VISIBLE
            vModel!!.ChangeLocation(requestModel).observe(requireActivity(),
                Observer {
                    progressmore.visibility = View.GONE
                    initControl()
                }
            )
        } else {
            progressmore.visibility = View.GONE
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

}
