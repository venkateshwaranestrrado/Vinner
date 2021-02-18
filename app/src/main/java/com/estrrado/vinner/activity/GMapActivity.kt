package com.estrrado.vinner.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.estrrado.vinner.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_g_map.*
import kotlinx.android.synthetic.main.toolbar_prev.*


class GMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var mMap: GoogleMap? = null

    var latitude = 0.0
    var longitude = 0.0
    var city = ""
    var country = ""
    var zipcode = ""
    var roadname = ""
    var validateRegion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_map)

        pageTitle.text = "Back"

        intent?.let {
            latitude = it.getDoubleExtra("latitude", 0.0)
            longitude = it.getDoubleExtra("longitude", 0.0)
            it.getStringExtra("city")?.let {
                city = it
            }
            it.getStringExtra("country")?.let {
                country = it
            }
            it.getStringExtra("zipcode")?.let {
                zipcode = it
            }
            it.getStringExtra("roadname")?.let {
                roadname = it
            }
            validateRegion = it.getBooleanExtra("validateRegion", false)
        }

        (map as SupportMapFragment).getMapAsync(this)


    }

    fun onBack(view: View) {
        onBackPressed()
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0

        if (latitude != 0.0 && longitude != 0.0) {
            mMap!!.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title(country))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))
        }

        txtError.isVisible = !validateRegion
        txtOk.isVisible = validateRegion

        txtOk.setOnClickListener {
            val intent = Intent()
            intent.putExtra("city", city)
            intent.putExtra("country", country)
            intent.putExtra("zipcode", zipcode)
            intent.putExtra("roadname", roadname)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

}