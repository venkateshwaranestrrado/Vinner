package com.estrrado.vinner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.estrrado.vinner.R
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.CartAdapter
import com.estrrado.vinner.data.CartItem
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment: Fragment() {
    private val cartItems= listOf(CartItem("Beaver",R.mipmap.pro1),CartItem("Beaver",R.mipmap.pro1)

    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        return root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).close()
        initControl()

    }
    private fun initControl() {

        productList.layoutManager = LinearLayoutManager(activity as VinnerActivity)
       // productList.isNestedScrollingEnabled = false
      productList.adapter = CartAdapter(requireActivity(),cartItems)
    }


}