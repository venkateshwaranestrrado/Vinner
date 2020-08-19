package com.estrrado.vinner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.retrofit.ApiClient
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.vm.AuthVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginActivity : AppCompatActivity(), OnClickListener {

    var authenticateVM: AuthVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        initControl()
    }


    private fun initControl() {
        tvnewuser.setOnClickListener(this)
        tvnewregister.setOnClickListener(this)
        tvSubmit.setOnClickListener(this)

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
            R.id.tvnewuser, R.id.tvnewregister -> {

                startActivity(Intent(this, RegisterActivity::class.java))

            }

            R.id.tvSubmit -> {

                if (phone.validate()) {
                    val phoneNum = phone.text.toString()
                    authenticateVM!!.login(
                        Input("","",
                            phoneNum
                        )
                    ).observe(this,
                        Observer {
                            printToast(this, it?.message.toString())
                            if (it?.status.equals(SUCCESS)) {
                                Preferences.put(this, MOBILE, phoneNum)
                                startActivity(Intent(this, OtpActivity::class.java))
                                finish()
                            }
                        })

                }

            }

        }
    }
}
