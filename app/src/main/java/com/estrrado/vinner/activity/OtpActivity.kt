package com.estrrado.vinner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.estrrado.vinner.R
import com.estrrado.vinner.helper.printT
import kotlinx.android.synthetic.main.fragment_login_otp.*

class OtpActivity : AppCompatActivity(),View.OnClickListener {
    var mtoken: String? = "1234"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_otp)
        initControl()
    }

    private fun initControl(){
        etOtp.setOnClickListener(this)
        btotpSubmit.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.etOtp ->{


            }

            R.id.btotpSubmit ->{

                if (mtoken!! == etOtp.text.toString()) {

                    startActivity(Intent(this, VinnerActivity::class.java))
                    finish()

                }

                else{
                    printT(this, "Invalid OTP")

                }

            }

        }
    }
}
