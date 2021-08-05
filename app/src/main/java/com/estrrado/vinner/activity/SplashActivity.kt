package com.estrrado.vinner.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estrrado.vinner.R
import com.estrrado.vinner.helper.Constants.IS_LOGIN
import com.estrrado.vinner.helper.Constants.TRUE
import com.estrrado.vinner.helper.Preferences
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.splash_page.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_page)
        progresssplash.visibility = View.VISIBLE

        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Toast.makeText(this, "Please update to continue", Toast.LENGTH_LONG).show()
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                    )
                    finish()
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                    finish()
                }
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (Preferences.get(this, IS_LOGIN).equals(TRUE)) {
                        progresssplash.visibility = View.GONE
                        startActivity(Intent(this, VinnerActivity::class.java))
                    } else {
                        progresssplash.visibility = View.GONE
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    finish()
                }, 1000)
            }
        }

    }

}
