package com.estrrado.vinner.ui.more

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast

import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.ui.Address_list
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_address_update.*
import kotlinx.android.synthetic.main.toolbar_back.*
import java.io.IOException
import java.util.*

class AddAddress : Fragment(),LocationListener {
    var vModel: HomeVM? = null
    private lateinit var location:Location
    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(
                        activity,
                        ApiClient.apiServices!!
                    )
                )
            )
        ).get(HomeVM::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_address_update, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initControll()
        pageTitle.text="Add Address"
    }
      private fun initControll(){

          addresslyt.setOnClickListener {
//              var loc = getlocation()
//              onLocationChanged1(loc)
              getlocation()
          }
           SaveBtn.setOnClickListener {
               progressaddress.visibility=View.VISIBLE
               if (Helper.isNetworkAvailable(requireContext())){
                   vModel!!.addAddress(RequestModel
                (accessToken = Preferences.get(activity, ACCESS_TOKEN),
                address_type = addrsstype.text.toString(),
                house_flat = tvaddress.text.toString(),
                zipcode = tv_zipcode.text.toString(),
                road_name = tv_roadname.text.toString(),
                landmark = tv_landmark.text.toString(),
                default = if (check_default.isChecked) {
                    1
                } else {
                    0
                })).observe(requireActivity(),
                Observer {
                    if (it!!.status=="success"){
                        progressaddress.visibility=View.GONE
                        Toast.makeText(context,"Address updated successfully", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.beginTransaction().
                        replace(R.id.nav_host_fragment, Address_list()).commit()
                    }

                    else
                    {
                        if (it.message.equals("Invalid access token")) {
                            startActivity(Intent(activity, LoginActivity::class.java))
                            requireActivity().finish()
                        } else {
                            printToast(requireContext(), it.message!!)
                        }
                        printToast(this.requireContext(), it.message.toString())
                    }
                }) }
        else
        {
            Toast.makeText(context,"No Network Available",Toast.LENGTH_SHORT).show()
            progressaddress.visibility=View.GONE
        }

    }
}

    override fun onLocationChanged(location: Location) {

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

//    fun getlocation(){
//        var locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager?
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this.requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
//            return
//        }
//        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f,this);
//        var criteria = Criteria();
//        var bestProvider = locationManager?.getBestProvider(criteria, true);
//        var location = locationManager?.getLastKnownLocation(bestProvider);
//        if (location == null) {
//            Toast.makeText(requireContext(), "GPS signal not found", Toast.LENGTH_SHORT).show();
//        }
//        if (location != null) {
//            onLocationChanged1(location);
//        }
//    }
    fun onLocationChanged1(location: Location) {
    val geocoder: Geocoder
    val addresses: List<Address>?
    geocoder = Geocoder(requireContext(), Locale.getDefault())
    val latitude = location.latitude
    val longitude = location.longitude
    try {
        addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null && addresses.size > 0) {
            val address: String = addresses[0].getAddressLine(0)
            val city: String = addresses[0].getLocality()
            val state: String = addresses[0].getAdminArea()
            val country: String = addresses[0].getCountryName()
            val postalCode: String = addresses[0].getPostalCode()
            val knownName: String = addresses[0].featureName
            val sublocality: String = addresses[0].subLocality
            tv_zipcode.setText(postalCode)
            tv_roadname.setText(sublocality + "," + city + "," +state+ "," + country)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> {
                    getlocation()
                }
                PackageManager.PERMISSION_DENIED -> {
                }//Tell to user the need of grant permission
            }
        }
    }
//    fun getlocation():Location{
//
//        var locationManager =
//            context?.getSystemService(LOCATION_SERVICE) as LocationManager
//
//// getting GPS status
//
//// getting GPS status
//        var isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//
//        if (isGPSEnabled) {
//
//            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(
//                    this.requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION
//                )
//
//
//                locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    0L,
//                    0f,
//                    this,
//                    null
//                )
//                if (locationManager != null) {
//                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                    if (location != null) {
//                        //latitude = location.getLatitude()
//                        //longitude = location.getLongitude()
//                        return location
//                    }
//                }
//            }
//        }
//// getting network status
//// getting network status
//        var isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//
//        if (isNetworkEnabled) {
//            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(
//                    this.requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION
//                )
//
//            }
//            locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER,
//                0L,
//                0f,
//                this,
//                null
//            )
//            //Log.v("Network", "Network is enabled")
//            if (locationManager != null) {
//                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//                if (location != null) {
////                latitude = location.getLatitude()
////                longitude = location.getLongitude()
//                    return location
//                    //Log.v("LocationTracker", "Location : " + latitude.toString() + ", " + longitude)
//                } else {
//                    //Log.v("LocationTracker", "Location is null")
//                }
//            } else {
//                // Log.v("LocationTracker", "Location manager is null")
//            }
//
//        }
//        return location
//    }
    fun getlocation(){

        var locationManager =
            context?.getSystemService(LOCATION_SERVICE) as LocationManager

// getting GPS status

// getting GPS status
        var isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGPSEnabled) {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                )
return

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    this,
                    null
                )
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location != null) {
                        //latitude = location.getLatitude()
                        //longitude = location.getLongitude()
//                        return location
                        onLocationChanged1(location)
                    }
                }
            }
        }
// getting network status
// getting network status
        var isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isNetworkEnabled) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                )
                return

            }
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                this,
                null
            )
            //Log.v("Network", "Network is enabled")
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {

                    onLocationChanged1(location)
//                latitude = location.getLatitude()
//                longitude = location.getLongitude()
//                    return location
                    //Log.v("LocationTracker", "Location : " + latitude.toString() + ", " + longitude)
                } else {
                    //Log.v("LocationTracker", "Location is null")
                }
            } else {
                // Log.v("LocationTracker", "Location manager is null")
            }

        }
//        return location
    }
}

