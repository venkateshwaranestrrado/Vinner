package com.estrrado.vinner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.vm.AuthVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_login_otp.*

class OtpActivity : AppCompatActivity(), View.OnClickListener {

    var authenticateVM: AuthVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_otp)
        initControl()
    }

    private fun initControl() {
        etOtp.setOnClickListener(this)
        btotpSubmit.setOnClickListener(this)

        authenticateVM = ViewModelProvider(
            this,
            MainViewModel(
                AuthVM(
                    VinnerRespository.getInstance(
                        this,
                        ApiClient.apiServices
                    )
                )
            )
        ).get(AuthVM::class.java)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.etOtp -> {


            }

            R.id.btotpSubmit -> {

                if (hasText(etOtp, REQUIRED)) {
                    val otp = etOtp.text.toString()
                    val requestModel = RequestModel()
                    requestModel.otp = otp
                    requestModel.phoneNumber = Preferences.get(this, MOBILE)

                    authenticateVM!!.verifyOTP(
                        requestModel
                    ).observe(this,
                        Observer {
                            printToast(this, it?.message.toString())
                            if (it?.status.equals(SUCCESS)) {
                                Preferences.put(this, ACCESS_TOKEN, it!!.data!!.getAccessToken()!!)
                                Preferences.put(this, IS_LOGIN, TRUE)
                                startActivity(Intent(this, VinnerActivity::class.java))
                                finish()
                            }
                        })
                }

            }

        }
    }
}
