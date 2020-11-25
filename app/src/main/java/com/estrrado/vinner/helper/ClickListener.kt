package com.estrrado.vinner.helper

import android.os.SystemClock
import android.view.View


abstract class ClickListener : View.OnClickListener {

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        try {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
                lastClickTime = currentTime
                onOneClick(v)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract fun onOneClick(v: View)

    companion object {
        private const val MIN_CLICK_INTERVAL: Long = 3000
    }
}