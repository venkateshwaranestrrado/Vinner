package com.estrrado.vinner.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.estrrado.vinner.R
import com.estrrado.vinner.ui.request.RequestDemo
import com.estrrado.vinner.ui.request.RequestService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottomsheet.*


class BottomsheetFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initcontroll()
    }

    private fun initcontroll() {
        cardView2_demo.setOnClickListener {
            requireActivity()!!.getSupportFragmentManager().beginTransaction().replace(
                R.id.nav_host_fragment,
                RequestDemo()
            )
                .addToBackStack(null).commit()
            requireActivity().onBackPressed()
            bottomSheet.visibility=View.GONE
        }

        cardView3_service.setOnClickListener {
            bottomSheet.visibility=View.GONE
            requireActivity()!!.supportFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment, RequestService())
                  .addToBackStack(null)
                    .commit()
                requireActivity().onBackPressed()
            }

        }
    }

}