package com.estrrado.vinner.ui.more

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.ui.Address_list
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_address_update.*
import kotlinx.android.synthetic.main.toolbar_back.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class AddAddress : Fragment(), LocationListener {
    var vModel: HomeVM? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

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
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

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
        pageTitle.text = "Add Address"
        locationRequest = LocationRequest().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            //
            // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
            // targetSdkVersion) may receive updates less frequently than this interval when the app
            // is no longer in the foreground.
            interval = TimeUnit.SECONDS.toMillis(60000)

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = TimeUnit.SECONDS.toMillis(5000)

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
//            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

//                        if (locationResult?.lastLocation != null) {

                // Normally, you want to save a new location to a database. We are simplifying
                // things a bit and just saving it as a local variable, as we only need it again
                // if a Notification is created (when user navigates away from app).
//                            currentLocation = locationResult.lastLocation
//                        }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun initControll() {

        addresslyt.setOnClickListener {
            getLocation()
        }
        SaveBtn.setOnClickListener {

            if (Validation.hasText(edt_name, Constants.REQUIRED) && Validation.hasText(
                    addrsstype,
                    Constants.REQUIRED
                ) && Validation.hasText(
                    tvaddress,
                    Constants.REQUIRED
                ) &&
                Validation.hasText(tv_zipcode, Constants.REQUIRED) && Validation.hasText(
                    tv_roadname,
                    Constants.REQUIRED
                ) && Validation.hasText(tv_landmark, Constants.REQUIRED) && Validation.hasText(
                    txt_country,
                    Constants.REQUIRED
                ) && Validation.hasText(edt_city, Constants.REQUIRED)
            ) {

                if (Helper.isNetworkAvailable(requireContext())) {
                    progressaddress.visibility = View.VISIBLE
                    vModel!!.addAddress(
                        RequestModel
                            (
                            accessToken = Preferences.get(activity, ACCESS_TOKEN),
                            address_type = addrsstype.text.toString(),
                            house_flat = tvaddress.text.toString(),
                            zipcode = tv_zipcode.text.toString(),
                            road_name = tv_roadname.text.toString(),
                            landmark = tv_landmark.text.toString(),
                            name = edt_name.text.toString(),
                            city = edt_city.text.toString(),
                            country = txt_country.text.toString(),
                            default = if (check_default.isChecked) {
                                1
                            } else {
                                0
                            }
                        )
                    ).observe(requireActivity(),
                        Observer {
                            progressaddress.visibility = View.GONE
                            printToast(this.requireContext(), it!!.message.toString())
                            if (it!!.status == "success") {
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.nav_host_fragment, Address_list()).commit()
                            } else {
                                if (it.message.equals("Invalid access token")) {
                                    startActivity(Intent(activity, LoginActivity::class.java))
                                    requireActivity().finish()
                                }

                            }
                        })

                } else {
                    Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
                    progressaddress.visibility = View.GONE
                }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                    getLocation()
                }
                PackageManager.PERMISSION_DENIED -> {
                }//Tell to user the need of grant permission
            }
        }
    }

    fun getLocation() {

        val locationManager =
            context?.getSystemService(LOCATION_SERVICE) as LocationManager

        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGPSEnabled) {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                )
                return
            } else {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            getAddress(location.latitude, location.longitude)
                        } else printToast(requireContext(), "Location not available")
                    }
                fusedLocationClient.lastLocation
            }
        } else isGPSEnabled()
    }

    fun isGPSEnabled() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(requireActivity())
                .checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->

        }

        result.addOnSuccessListener { response ->
            val states = response.locationSettingsStates
            if (states.isLocationPresent) {
                getLocation()
            }
        }
        result.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    startIntentSenderForResult(
                        e.getResolution().getIntentSender(),
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        null,
                        0,
                        0,
                        0,
                        null
                    );
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
//            edt_name.setText(addresses[0].getAddressLine(0))
            edt_city.setText(addresses[0].locality)
            val state = addresses[0].adminArea
            txt_country.setText(addresses[0].countryName)
            tv_zipcode.setText(addresses[0].postalCode)
            tv_roadname.setText(addresses[0].featureName)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LocationRequest.PRIORITY_HIGH_ACCURACY) {
            val locationManager =
                context?.getSystemService(LOCATION_SERVICE) as LocationManager
            Handler().postDelayed({
                val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

                if (isGPSEnabled) {
                    getLocation()
                }
            }, 1000)
        }
    }


}

