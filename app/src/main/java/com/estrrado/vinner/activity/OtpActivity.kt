package com.estrrado.vinner.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.FROM_LOGIN
import com.estrrado.vinner.helper.Constants.IS_LOGIN
import com.estrrado.vinner.helper.Constants.MOBILE
import com.estrrado.vinner.helper.Constants.REQUIRED
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.TRUE
import com.estrrado.vinner.helper.Constants.VERIFIED
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Preferences.REGION_CODE
import com.estrrado.vinner.helper.Validation.hasText
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
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
                progressotp!!.visibility = View.VISIBLE
                if (Helper.isNetworkAvailable(this)) {
                    if (hasText(etOtp, REQUIRED)) {
                        val otp = etOtp.text.toString()
                        val requestModel = RequestModel()
                        requestModel.otp = otp
                        requestModel.phoneNumber = Preferences.get(this, MOBILE)
                        requestModel.countryCode = Preferences.get(this, REGION_CODE)
                        requestModel.os = "android"
                        requestModel.deviceId = "abcdefghijklmnoqrstuvwxyz"

                        authenticateVM!!.verifyOTP(
                            requestModel
                        ).observe(this,
                            Observer {
                                printToast(this, it?.message.toString())
                                progressotp!!.visibility = View.GONE
                                if (it?.status.equals(SUCCESS)) {

                                    if (it?.userstatus.equals(VERIFIED)) {
                                        Preferences.put(
                                            this,
                                            ACCESS_TOKEN,
                                            it!!.data!!.getAccessToken()!!
                                        )
                                        Preferences.put(this, IS_LOGIN, TRUE)
                                        FROM_LOGIN = 1
                                        val intent = Intent(this, VinnerActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        val intent = Intent(this, RegisterActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                        finish()
                                    }
                                }

                            })
                    } else {
                        progressotp.visibility = View.GONE
                        Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "No Network Available", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}
