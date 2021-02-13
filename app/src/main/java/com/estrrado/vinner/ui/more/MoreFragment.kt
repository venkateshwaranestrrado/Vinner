package com.estrrado.vinner.ui.more

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.ClickListener
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Preferences.PROFILEIMAGE
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.dialog_signout.*
import kotlinx.android.synthetic.main.moree_fragment.*
import kotlinx.android.synthetic.main.toolbar_more.*

class MoreFragment : Fragment(), View.OnClickListener {
    var vModel: HomeVM? = null

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
        initControl()
        textView5.text = "Settings"
        (activity as VinnerActivity).open()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vModel!!.getProfile(
            RequestModel(accessToken = Preferences.get(activity, ACCESS_TOKEN))

        ).observe(requireActivity(),
            Observer {
                if (it!!.status == "success") {
                    Glide.with(this)
                        .load(it.data!!.path)
                        .thumbnail(0.1f)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ivprofilemorephoto)
                    tvProfileName.setText(it.data!!.name)

                }
            })

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
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "VINNER")
                val shareMessage = "${Constants.shareLink}"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Share VINNER using"))
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
                    malertDialog.cancel()
                }

            })
        }

        searchtool.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_more_to_searchFragment)
        }

        trackOrder.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_more_to_navigation_track)
        }

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
        Glide.with(this.requireActivity())
            .load(Preferences.get(activity, PROFILEIMAGE))
            .thumbnail(0.1f)
            .into(ivprofilemorephoto)
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


}
