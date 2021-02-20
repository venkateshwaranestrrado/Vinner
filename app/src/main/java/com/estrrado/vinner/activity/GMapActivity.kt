package com.estrrado.vinner.activity

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.estrrado.vinner.R
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.helper.readFromAsset
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_g_map.*
import kotlinx.android.synthetic.main.toolbar_prev.*
import java.io.IOException

class GMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var mMap: GoogleMap? = null

    var latitude = 0.0
    var longitude = 0.0
    var city = ""
    var country = ""
    var zipcode = ""
    var roadname = ""
    var validateRegion = false
    var regionList: List<RegionSpinner>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_map)

        regionList = readFromAsset(this)

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
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))
        }

        txtCountry.text = country

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


        mMap?.setOnCameraIdleListener(object : GoogleMap.OnCameraIdleListener {
            override fun onCameraIdle() {
                val latLng = mMap?.cameraPosition?.target
                try {
                    latLng?.let {
                        val addresses = Geocoder(this@GMapActivity).getFromLocation(
                            it.latitude,
                            it.longitude
                            , 1
                        )
                        if (addresses.size > 0) {
                            addresses[0].locality?.let {
                                city = it
                            }
                            addresses[0].countryCode?.let {
                                country = it
                            }
                            addresses[0].postalCode?.let {
                                zipcode = it
                            }
                            addresses[0].featureName?.let {
                                roadname = it
                            }
                            txtCountry.text = country
                            validateRegion = validateRegion(country)
                            txtError.isVisible = !validateRegion
                            txtOk.isVisible = validateRegion
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        })

    }

    fun validateRegion(region: String): Boolean {
        for (i in 0 until regionList!!.size) {
            if (regionList!!.get(i).name.equals(region)) {
                return true
            }
        }
        return false
    }

}