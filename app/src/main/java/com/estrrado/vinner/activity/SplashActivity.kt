package com.estrrado.vinner.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import com.estrrado.vinner.R
import com.estrrado.vinner.helper.Constants.IS_LOGIN
import com.estrrado.vinner.helper.Constants.TRUE

import com.estrrado.vinner.helper.Preferences

import kotlinx.android.synthetic.main.splash_page.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_page)
        progresssplash.visibility=View.VISIBLE
        Handler().postDelayed({
            if (Preferences.get(this, IS_LOGIN).equals(TRUE)) {
                progresssplash.visibility=View.GONE
                startActivity(Intent(this, VinnerActivity::class.java))
            } else {
                progresssplash.visibility=View.GONE
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 1000)

    }

}
