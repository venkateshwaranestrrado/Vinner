package com.estrrado.vinner.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.estrrado.vinner.R
import com.estrrado.vinner.activity.VinnerActivity

class EditProfile: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.edit_profile, container, false)
        // val textView: TextView = root.findViewById(R.id.text_home)


        return root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as VinnerActivity).close()
        // TODO: Use the ViewModel
    }

}