package com.estrrado.vinner.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler

import androidx.appcompat.app.AppCompatActivity
import com.estrrado.vinner.R
import com.estrrado.vinner.helper.IS_LOGIN
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.TRUE


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_page)
        Handler().postDelayed({
            if (Preferences.get(this, IS_LOGIN).equals(TRUE)) {
                startActivity(Intent(this, VinnerActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 1000)

    }

}
