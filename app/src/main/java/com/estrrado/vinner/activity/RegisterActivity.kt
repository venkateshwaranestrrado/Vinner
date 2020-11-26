package com.estrrado.vinner.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.helper.Constants.EMAIL_ID_NOT_VALID
import com.estrrado.vinner.helper.Constants.MOBILE
import com.estrrado.vinner.helper.Constants.PASSWORD_DOES_NOT_MATCH
import com.estrrado.vinner.helper.Constants.REQUIRED
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.emailPattern
import com.estrrado.vinner.helper.Validation.hasText
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.AuthVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    var authenticateVM: AuthVM? = null
    var pHNEnumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)
        initControl()
        pHNEnumber = Preferences.get(this, MOBILE)
        edt_mobile.setText(pHNEnumber)
    }

    private fun initControl() {
        tvRSubmit.setOnClickListener(this)

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
            R.id.tvRSubmit -> {
                if (Helper.isNetworkAvailable(this)) {
                    val userName = etUsername.text.toString()
                    val email = etemail.text.toString()
                    val refferl = etReferral.text.toString()
                    val phoneNumber = pHNEnumber

                    val password = edt_password.text.toString()
                    val confirmPassword = edt_confirm_password.text.toString()
                    if (hasText(etUsername, REQUIRED) && hasText(etemail, REQUIRED) && hasText(
                            edt_mobile,
                            REQUIRED
                        ) &&
                        hasText(edt_password, REQUIRED) && hasText(edt_confirm_password, REQUIRED)
                    ) {
                        if (!email.matches(emailPattern.toRegex())) {
                            progressregister.visibility = View.GONE

                            printToast(this, EMAIL_ID_NOT_VALID)
                        } else if (!password.equals(confirmPassword)) {
                            progressregister.visibility = View.GONE

                            printToast(this, PASSWORD_DOES_NOT_MATCH)
                        } else {
                            progressregister.visibility = View.VISIBLE
                            authenticateVM!!.register(
                                Input(
                                    userName,
                                    email,
                                    phoneNumber,
                                    password,
                                    confirmPassword,
                                    c_code = Preferences.get(this, Preferences.REGION_CODE)
                                )
                            ).observe(this,
                                Observer {
                                    printToast(this, it?.message.toString())
                                    if (it?.status.equals(SUCCESS)) {
                                        progressregister.visibility = View.GONE
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    }
                                    progressregister.visibility = View.GONE
                                })
                        }
                    }
                } else {
                    progressregister.visibility = View.GONE
                    printToast(this, "No Network Available")
                }

            }


        }
    }
}
