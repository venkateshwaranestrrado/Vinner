package com.estrrado.vinner.ui.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.estrrado.vinner.R
import com.estrrado.vinner.activity.VinnerActivity
import kotlinx.android.synthetic.main.request_demo.spCity
import kotlinx.android.synthetic.main.request_demo.spCountry
import kotlinx.android.synthetic.main.request_service.*
import kotlinx.android.synthetic.main.toolbar_back.*

class RequestService : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.request_service, container, false)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).close()
        pageTitle.text = "Request For Service"
        initControl()

    }

    private fun initControl(){
        val city = resources.getStringArray(R.array.city)

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, city)
        spCity.adapter = adapter
        spCity.prompt="city"

        val country = resources.getStringArray(R.array.country)

        val countryadapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, country)
        spCountry.adapter = countryadapter

        val packages = resources.getStringArray(R.array.packages)
        val packageadapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, packages)
        spPackages.adapter = packageadapter


    }

}