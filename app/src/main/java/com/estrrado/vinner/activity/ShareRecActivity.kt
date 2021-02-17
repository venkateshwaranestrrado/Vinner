package com.estrrado.vinner.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.estrrado.vinner.R

class ShareRecActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_rec)

        val uri = intent.data
        if (uri != null) {
            val strings = uri.path?.split("/")
            strings?.let {
                if (it.size > 1) {
                    VinnerActivity.shareProdId = it.get(it.size - 2)
                    launchActivity()
                }
            }
        } else {
            launchActivity()
        }

    }

    fun launchActivity() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

}