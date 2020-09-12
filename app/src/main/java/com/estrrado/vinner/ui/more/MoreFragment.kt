package com.estrrado.vinner.ui.more

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide

import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.activity.RegisterActivity
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.dialog_signout.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.toolbar.*

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

        val root = inflater.inflate(R.layout.profile_fragment, container, false)

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
        (activity as VinnerActivity).open()
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lyt_logout.setOnClickListener {
            val mbuilder = AlertDialog.Builder(view?.context)
            val dialogview =
                LayoutInflater.from(view?.context).inflate(R.layout.dialog_signout, null, false);
            mbuilder.setView(dialogview)
            val malertDialog = mbuilder.show()
            malertDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
            malertDialog?.yes?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
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
    }

    private fun signout() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)

        vModel!!.signout(requestModel).observe(this,
            Observer {
                if (it?.status.equals(SUCCESS)) {
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity!!.finish()
                } else printToast(this!!.context!!, it?.message.toString())

            })
    }

    private fun initControl() {

        profile.setOnClickListener(this)

    }


    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.profile -> {

                Navigation.findNavController(v).navigate(R.id.navigation_editprofile)

            }


        }


    }

}
