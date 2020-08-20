package com.estrrado.vinner.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.estrrado.vinner.R
import com.estrrado.vinner.activity.VinnerActivity
import kotlinx.android.synthetic.main.fragment_product_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductDetails : Fragment(),View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_product_details, container, false)

        return root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).close()
        initControl()
    }

    private fun initControl(){
        addcart.setOnClickListener(this)

    }



    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.addcart->{

                Navigation.findNavController(v).navigate(
                    R.id.navigation_cart

                )
            }

        }

    }

}
