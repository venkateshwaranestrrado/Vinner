package com.estrrado.vinner.ui.request

import android.R.attr.button
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.helper.Validation.validate
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.ui.HomeFragment
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.request_demo.spCountry
import kotlinx.android.synthetic.main.request_service.*
import kotlinx.android.synthetic.main.toolbar_back.*
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList


class RequestService : Fragment() {
    var vModel: HomeVM? = null

    var countryId: String? = null
    var DemoId: String? = null
    var Servicecateg: String? = null
    var ServiceType: String? = null
    var Date: String? = null
    var Time: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.request_service, container, false)
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as VinnerActivity).close()
        progressservice.visibility = View.VISIBLE
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
        pageTitle.text = "Request For Service"
        initControl()
        getdata()

    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initControl() {

        etDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val date = DatePickerDialog(
                    requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val month = monthOfYear + 1
                        var formattedMonth = "" + month
                        var formattedDayOfMonth = "" + dayOfMonth

                        if (month < 10) {
                            formattedMonth = "0" + month
                        }
                        if (dayOfMonth < 10) {

                            formattedDayOfMonth = "0" + dayOfMonth;
                        }
//                        var monthStr = "0" + month.toString()

                        Date = "" + year + "-" + formattedMonth + "-" + formattedDayOfMonth
                        etDate.setText(Date)

                    },
                    year,
                    month,
                    day
                )
                date.show()
            }
        })

        etTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val cal = Calendar.getInstance()
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        etTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
                        Time = SimpleDateFormat("HH:mm").format(cal.time)
                    }
                TimePickerDialog(
                    requireContext(),
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                )
                    .show()
            }
        })
        vModel!!.getcountries(
            RequestModel()
        ).observe(requireActivity(),
            Observer {

                if (it!!.status == "success") {
                    progressservice.visibility = View.GONE
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
            }
        )

        vModel!!.getdemo(
            RequestModel(accessToken = Preferences.get(activity, ACCESS_TOKEN))

        ).observe(requireActivity(),
            Observer {
                if (it!!.status == "success") {
                    progressservice.visibility = View.GONE
                    var demodata = it.data
                    var demo = ArrayList<String>()
                    for (item: AddressList in demodata!!) {
                        demo.add(item.product_title.toString())
                    }
                    val productadapter = ArrayAdapter(
                        requireContext(),
                        R.layout.spinner_item, demo
                    )
                    spPackages.prompt = "Demo Product"
                    spPackages.adapter = productadapter


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
                spPackages.prompt = "Demo Product"
                spPackages.adapter = productadapter
                spPackages.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        DemoId = demoid.get(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                })


            })
        vModel!!.getservicecateg(
            RequestModel(accessToken = Preferences.get(activity, ACCESS_TOKEN))

        ).observe(requireActivity(),
            Observer {
                if (it!!.status == "success") {
                    progressservice.visibility = View.GONE
                    var categdata = it.data
                    var categ = ArrayList<String>()
                    for (item: AddressList in categdata!!) {
                        categ.add(item.service_category_name.toString())
                    }
                    val categadapter = ArrayAdapter(
                        requireContext(),
                        R.layout.spinner_item, categ
                    )
                    servicecateg.prompt = "Sevice Category"
                    servicecateg.adapter = categadapter


                }

                var categdata = it.data
                var categ = ArrayList<String>()
                var categid = ArrayList<String>()
                for (item: AddressList in categdata!!) {
                    categid.add(item.service_category_id.toString())
                    categ.add(item.service_category_name.toString())
                }
                val categadapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item, categ
                )
                servicecateg.prompt = "Sevice Category"
                servicecateg.adapter = categadapter
                servicecateg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        Servicecateg = categid.get(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                })


            })


        vModel!!.getserviceType(
            RequestModel(accessToken = Preferences.get(activity, ACCESS_TOKEN))

        ).observe(requireActivity(),
            Observer {
                if (it!!.status == "success") {
                    progressservice.visibility = View.GONE
                    var typedata = it.data
                    var type = ArrayList<String>()
                    for (item: AddressList in typedata!!) {
                        type.add(item.service_type.toString())
                    }


                    val typeadapter = ArrayAdapter(
                        requireContext(),
                        R.layout.spinner_item, type
                    )
                    servicetype.prompt = "Sevice Type"
                    servicetype.adapter = typeadapter


                }

                var typedata = it.data
                var type = ArrayList<String>()
                var typeid = ArrayList<String>()
                for (item: AddressList in typedata!!) {
                    typeid.add(item.service_type_id.toString())
                    type.add(item.service_type.toString())
                }
                val typeadapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item, type
                )
                servicetype.prompt = "Sevice Type"
                servicetype.adapter = typeadapter
                servicetype.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        ServiceType = typeid.get(position).toString()
                        if (ServiceType == "3") {
                            ettype_detail.visibility = View.VISIBLE
                        } else

                            ettype_detail.visibility = View.GONE
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                })


            })


    }


    private fun getdata() {

        btnSubmit.setOnClickListener {

            if (Helper.isNetworkAvailable(requireContext())) {

                if (etphonenumber.validate()
                ) {
                    if (etemail.validate()
                    ) {
                        if (validate(
                                arrayOf(
                                    etName,
                                    etAddress,
                                    spCity,
                                    etName,
                                    etRequestDemo,
                                    etDate,
                                    etTime
                                )
                            )
                        ) {
                            progressservice.visibility = View.VISIBLE
                            vModel!!.getreqservice(
                                RequestModel(
                                    accessToken = Preferences.get(activity, ACCESS_TOKEN),
                                    service_category = Servicecateg,
                                    service_type = ServiceType,
                                    name = etName.text.toString(),
                                    address = etAddress.text.toString(),
                                    city = spCity.text.toString(),
                                    country = countryId,
                                    type_detail = ettype_detail.text.toString(),
                                    email = etemail.text.toString(),
                                    mobile = etphonenumber.text.toString(),
                                    date = Date,
                                    time = Time,
                                    remark = etRequestDemo.text.toString()
                                )
                            ).observe(requireActivity(),
                                Observer {
                                    progressservice.visibility = View.GONE
                                    printToast(
                                        requireActivity(),
                                        it!!.message!!
                                    )
                                    if (it!!.status == "success") {

                                        requireActivity().supportFragmentManager.beginTransaction()
                                            .replace(R.id.nav_host_fragment, HomeFragment())
                                            .commit()
                                    }
                                })
                        }
                    } else {
                        printToast(requireContext()!!, "Invalid Email Address")
                    }
                } else {
                    printToast(requireContext()!!, "Invalid Phone number")
                }
            } else {
                printToast(requireContext()!!, "No network Available")
            }

        }


        btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment()).commit()
        }
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
