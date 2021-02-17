package com.estrrado.vinner.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.Review
import com.estrrado.vinner.helper.Constants
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_vinner.*

@Suppress("NAME_SHADOWING")
class VinnerActivity : AppCompatActivity() {

    companion object {
        var shareProdId = ""
    }

    var notificationBadgeTextView: TextView? = null
    private var bottomNavigationView: BottomNavigationView? = null

    private var notificationBadge: View? = null
    private var cartCount = 0

    var reviews: List<Review?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vinner)
        bottomNavigationView = findViewById(R.id.nav_view)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_cart,
                R.id.navigation_browse,
                R.id.navigation_more
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        SetNotificationBadge()
    }

    fun selectMenu() {
        bottomNavigationView?.selectedItemId = R.id.navigation_home

    }

    fun getCartCount(): Int {
        return cartCount
    }

    fun open() {
        nav_view.visibility = View.VISIBLE

    }

    fun onBack(view: View) {
        onBackPressed()
    }

    fun close() {
        nav_view.visibility = View.GONE
    }

    fun SetNotificationBadge() {
        val menuView =
            bottomNavigationView!!.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(2) as BottomNavigationItemView

        notificationBadge =
            LayoutInflater.from(this).inflate(R.layout.view_notification_bage, menuView, false)
        notificationBadgeTextView =
            notificationBadge!!.findViewById<TextView>(R.id.notificationsBadgeTextView)
        itemView.addView(notificationBadge)
        notificationBadge!!.visibility = View.GONE

    }

    fun refreshBadgeView(count: Int) {
        cartCount = cartCount + count
        if (cartCount <= 0) {
            notificationBadge!!.visibility = GONE
        } else {
            notificationBadge!!.visibility = View.VISIBLE
            notificationBadgeTextView!!.text = cartCount.toString()
        }
    }

    fun refreshBadgeView(count: String?) {
        if (count == null || count.equals("") || count.toInt() <= 0) {
            notificationBadge!!.visibility = GONE
            cartCount = 0
        } else {
            cartCount = count.toInt()
            notificationBadge!!.visibility = View.VISIBLE
            notificationBadgeTextView!!.text = count
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.reqCode && resultCode == Activity.RESULT_OK) {
            selectMenu()
        }
    }

}
