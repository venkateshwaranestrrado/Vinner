package com.estrrado.vinner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.estrrado.vinner.R
import com.estrrado.vinner.helper.printT
import com.estrrado.vinner.helper.validate
import kotlinx.android.synthetic.main.fragment_login.*

class MainActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        initControl()
    }


    private  fun initControl(){
        tvnewuser.setOnClickListener(this)
        tvnewregister.setOnClickListener(this)
        tvSubmit.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvnewuser, R.id.tvnewregister ->{

                startActivity(Intent(this, RegisterActivity::class.java))
                finish()

            }

            R.id.tvSubmit ->{

                if(phone.validate()&&phone.length()==10) {
                    startActivity(Intent(this, OtpActivity::class.java))
                    finish()
                }

                else{

                    printT(this,"Invalid credentials")
                }

            }

        }
    }
}
