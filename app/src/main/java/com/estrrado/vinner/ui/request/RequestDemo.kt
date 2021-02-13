package com.estrrado.vinner.ui.request

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.Validation.validate
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.ui.HomeFragment
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.request_demo.*
import kotlinx.android.synthetic.main.toolbar_back.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RequestDemo : Fragment() {
    var vModel: HomeVM? = null
    var DemoId: Int? = null

    companion object {
        fun newInstance() = RequestDemo()
    }

    var countryId: String? = null
    var Date: String? = null
    var Time: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.request_demo, container, false)

        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as VinnerActivity).close()
        pageTitle.text = "Request For Demo"

        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(activity, ApiClient.apiServices!!)
                )
            )
        ).get(HomeVM::class.java)
        initControl()
        getdata()
    }

    private fun initControl() {

        etdemoDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val date = DatePickerDialog(
                    requireContext(),
                    { view, year, monthOfYear, dayOfMonth ->
                        val month = monthOfYear + 1
                        var formattedMonth = "" + month
                        var formattedDayOfMonth = "" + dayOfMonth

                        if (month < 10) {
                            formattedMonth = "0" + month
                        }
                        if (dayOfMonth < 10) {
                            formattedDayOfMonth = "0" + dayOfMonth;
                        }
                        Date = "" + year + "-" + formattedMonth + "-" + formattedDayOfMonth
                        etdemoDate.setText(
                            String.format(
                                "%s/%s/%s",
                                formattedDayOfMonth,
                                formattedMonth,
                                year
                            )
                        )
                    },
                    year,
                    month,
                    day
                )
                date.datePicker.minDate = System.currentTimeMillis() - 1000
                date.show()
            }
        })

        etdemoTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val cal = Calendar.getInstance()
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        etdemoTime.setText(SimpleDateFormat("hh:mm a").format(cal.time))
                        Time = SimpleDateFormat("hh:mm a").format(cal.time)
                    }
                TimePickerDialog(
                    requireContext(),
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                )
                    .show()
            }
        })

        vModel!!.getcountries(
            RequestModel()
        ).observe(requireActivity(), androidx.lifecycle.Observer {

            if (it!!.status == "success") {
                progressdemo.visibility = View.GONE
                var countryData = it.data
                var country = ArrayList<String>()
                for (item: AddressList in countryData!!) {
                    country.add(item.country_name.toString())
                }
                val countryadapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item, country
                )
                spCountry.prompt = "Country"
                spCountry.adapter = countryadapter

            }


            var countryData = it.data
            var country = ArrayList<String>()
            var countryid = ArrayList<String>()
            for (item: AddressList in countryData!!) {
                country.add(item.country_name.toString())
                countryid.add(item.country_id.toString())
            }
            val countryadapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item, country
            )
            spCountry.prompt = "Country"
            spCountry.adapter = countryadapter
            spCountry.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    countryId = countryid.get(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
        })



        vModel!!.getdemo(
            RequestModel(accessToken = Preferences.get(activity, ACCESS_TOKEN))

        ).observe(requireActivity(),
            androidx.lifecycle.Observer {
                if (it!!.status == "success") {
                    progressdemo.visibility = View.GONE
                    var demodata = it.data
                    var demo = ArrayList<String>()
                    for (item: AddressList in demodata!!) {
                        demo.add(item.product_title.toString())
                    }
                    val productadapter = ArrayAdapter(
                        requireContext(),
                        R.layout.spinner_item, demo
                    )
                    Selectproduct.prompt = "Demo Product"
                    Selectproduct.adapter = productadapter


                }

                var demodata = it.data
                var demo = ArrayList<String>()
                var demoid = ArrayList<String>()
                for (item: AddressList in demodata!!) {
                    demoid.add(item.product_id.toString())
                    demo.add(item.product_title.toString())
                }
                val productadapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item, demo
                )
                Selectproduct.prompt = "Demo Product"
                Selectproduct.adapter = productadapter
                Selectproduct.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        DemoId = demoid.get(position).toInt()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                })


            })

    }

    private fun getdata() {

        btndemoSubmit.setOnClickListener {
            if (Helper.isNetworkAvailable(requireContext())) {
                if (validation() && etdemoDate.validate() && etdemoTime.validate()
                ) {
                    if (etdemoemail.validate()) {
                        if (validate(
                                arrayOf(
                                    etdemoName,
                                    etdemoAddress,
                                    spdemoCity,
                                    etdemoRequestDemo
                                )
                            )
                        ) {
                            progressdemo.visibility = View.VISIBLE
                            vModel!!.getreqdemo(
                                RequestModel(
                                    accessToken = Preferences.get(activity, ACCESS_TOKEN),
                                    name = etdemoName.text.toString(),
                                    address = etdemoAddress.text.toString(),
                                    city = spdemoCity.text.toString(),
                                    country = countryId,
                                    email = etdemoemail.text.toString(),
                                    mobile = etdemophonenumber.text.toString(),
                                    date = Date,
                                    time = Time,
                                    product_id = DemoId,
                                    remarks = etdemoRequestDemo.text.toString()
                                )
                            ).observe(requireActivity(),
                                androidx.lifecycle.Observer {
                                    printToast(requireContext(), it!!.message!!)
                                    if (it!!.status == "success") {
                                        requireActivity().supportFragmentManager.beginTransaction()
                                            .replace(R.id.nav_host_fragment, HomeFragment())
                                            .commit()
                                    }
                                    progressdemo.visibility = View.GONE
                                })
                        } else {
                            printToast(requireContext(), "Enter Mandatory Fields")

                        }
                    } else {
                        printToast(requireContext()!!, "Invalid Email Address")
                    }
                }
            } else {
                printToast(requireContext()!!, "No network Available")
            }

        }



        btndemoCancel.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment()).commit()
        }
    }

    private fun validation(): Boolean {
        if (etdemophonenumber.length() < 7) {
            printToast(requireContext(), "Mobile number must be atleast 7 characters in length")
            return false
        }
        return true
    }

}


private fun validate(elements: Array<EditText>): Boolean {
    val boolean: ArrayList<Boolean> = ArrayList()
    for (i in 0 until elements.size) {
        boolean.add(hasText(elements[i]))
    }
    return !boolean.contains(false)
}

private fun hasText(editText: EditText?): Boolean {
    if (editText != null) {
        val text = editText.text.toString().trim { it <= ' ' }
        editText.error = null
        if (text.isEmpty()) {
            editText.error = "Mandatory Field"
            return false
        }
    } else {
        return false
    }
    return true
}

