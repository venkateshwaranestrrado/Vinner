package com.estrrado.vinner.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_vinner.*


@Suppress("NAME_SHADOWING")
class VinnerActivity : AppCompatActivity() {
    var vModel: HomeVM? = null
    private var bottomNavigationView: BottomNavigationView? = null

    private var notificationBadge: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vinner)
        bottomNavigationView= findViewById(R.id.nav_view)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
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

        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(
                        this,
                        ApiClient.apiServices!!
                    )
                )
            )
        ).get(HomeVM::class.java)
        if (Helper.isNetworkAvailable(this)) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(this, ACCESS_TOKEN)
            requestModel.countryCode= Preferences.get(this, Preferences.REGION_NAME)
            //requestModel.accessToken="122874726"
            vModel!!.getCartPage(requestModel).observe(this,
                Observer {
                    if (it?.status.equals(SUCCESS)) {
                        var itemcount = it?.data?.getItemsTotal()
                        if (itemcount != null) {
                            var itemInt = itemcount.toInt()
                            if(itemInt > 0) {
                                SetNotificationBadge(itemcount)
                            }
                        }


                    }
                })
        }
    }
    fun open(){
        nav_view.visibility=View.VISIBLE

    }
    fun onBack(view: View) {
        onBackPressed()
    }


    fun close(){

    nav_view.visibility=View.GONE
    }

    fun SetNotificationBadge(itemCount:String) {
        val menuView =
            bottomNavigationView!!.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(2) as BottomNavigationItemView

        notificationBadge =
            LayoutInflater.from(this).inflate(R.layout.view_notification_bage, menuView, false)
        var notificationBadgeTextView = notificationBadge!!.findViewById<TextView>(R.id.notificationsBadgeTextView)
        notificationBadgeTextView.setText(itemCount)
        itemView.addView(notificationBadge)

    }

    public fun refreshBadgeView() {
        val badgeIsVisible = notificationBadge!!.visibility != VISIBLE
        if(badgeIsVisible == false){
            notificationBadge!!.visibility = GONE
        }
        //notificationBadge!!.visibility = if (badgeIsVisible) View.VISIBLE else View.GONE
        // button.setText(if (badgeIsVisible) "Hide badge" else " Show badge")
    }
}
