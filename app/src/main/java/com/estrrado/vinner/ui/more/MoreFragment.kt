package com.estrrado.vinner.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.estrrado.vinner.R
import com.estrrado.vinner.activity.VinnerActivity
import kotlinx.android.synthetic.main.profile_fragment.*

class MoreFragment : Fragment(),View.OnClickListener {

    companion object {
        fun newInstance() = MoreFragment()
    }

    private lateinit var viewModel: MoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.profile_fragment, container, false)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         initControl()
        (activity as VinnerActivity).open()
        // TODO: Use the ViewModel
    }

    private fun initControl(){

       profile.setOnClickListener(this)

    }




    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.profile->{

               Navigation.findNavController(v) .navigate(R.id.navigation_editprofile)

            }



        }



    }

}
