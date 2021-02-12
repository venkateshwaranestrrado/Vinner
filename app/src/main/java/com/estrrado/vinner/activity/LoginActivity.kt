package com.estrrado.vinner.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.adapters.RegionAdapter
import com.estrrado.vinner.data.RegionSpinner
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.helper.Constants.MOBILE
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Preferences.REGION_CODE
import com.estrrado.vinner.helper.Preferences.REGION_FULLNAME
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.Validation.validate
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.AuthVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_login.*

class LoginActivity : AppCompatActivity(), OnClickListener {

    var authenticateVM: AuthVM? = null
    var json_string: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        initControl()
    }

    private fun initControl() {
        val regionList: List<RegionSpinner> = readFromAsset()
        val regionAdapter = RegionAdapter(this, regionList)
        region_spinner.adapter = regionAdapter
        region_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val gson = Gson()
                val modelList: List<RegionSpinner> =
                    gson.fromJson(json_string, Array<RegionSpinner>::class.java).toList()
                val code = modelList.get(position).code
                val name = modelList.get(position).name
                val fullname = regionList.get(position).fullname
                Preferences.put(this@LoginActivity, REGION_NAME, name)
                Preferences.put(this@LoginActivity, REGION_CODE, code)
                Preferences.put(this@LoginActivity, REGION_FULLNAME, fullname)
                Preferences.put(
                    this@LoginActivity,
                    Preferences.COUNTRY_POSITION,
                    position.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        tvSubmit.setOnClickListener(this)

        authenticateVM = ViewModelProvider(
            this,
            MainViewModel(
                AuthVM(
                    VinnerRespository.getInstance(
                        this,
                        ApiClient.apiServices
                    )
                )
            )
        ).get(AuthVM::class.java)
    }

    override fun onResume() {
        super.onResume()
        if (!Preferences.get(this, Preferences.COUNTRY_POSITION).equals("")) {
            region_spinner.setSelection(
                Preferences.get(this, Preferences.COUNTRY_POSITION)!!.toInt()
            )
        }
    }

    private fun validation(): Boolean {
        if (phone.length() < 7) {
            printToast(this, "Invalid phone number. Pls give correct phone number.")
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvSubmit -> {
                progress.visibility = View.VISIBLE
                val Regioncode = Preferences.get(this, REGION_CODE)
                if ((phone.validate())) {
                    if ((validation())) {
                        if (Helper.isNetworkAvailable(this)) {
                            var phoneNum = phone.text.toString()
                            phoneNum = phoneNum
                            authenticateVM!!.login(
                                Input(
                                    "", "",
                                    phoneNum,
                                    c_code = Regioncode
                                )
                            ).observe(this,
                                Observer {
                                    progress.visibility = View.GONE
                                    printToast(this, it?.message.toString())
                                    if (it?.status.equals(SUCCESS)) {
                                        Preferences.put(this, MOBILE, phoneNum)
                                        startActivity(Intent(this, OtpActivity::class.java))
                                    }
                                })
                        } else {
                            progress.visibility = View.GONE
                            Toast.makeText(this, "No Network Available", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        progress.visibility = View.GONE
                    }
                } else {
                    progress.visibility = View.GONE
                }
            }

        }
    }

    private fun readFromAsset(): List<RegionSpinner> {
        val file_name = "login_region.json"
        val bufferReader = application.assets.open(file_name).bufferedReader()
        json_string = bufferReader.use {
            it.readText()
        }
        val gson = Gson()
        val modelList: List<RegionSpinner> =
            gson.fromJson(json_string, Array<RegionSpinner>::class.java).toList()
        return modelList
    }
}
