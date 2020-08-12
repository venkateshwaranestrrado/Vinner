package com.estrrado.vinner.ui.browse

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer

import com.estrrado.vinner.R

class BrowseFragment : Fragment() {

    companion object {
        fun newInstance() = BrowseFragment()
    }

    private lateinit var viewModel: BrowseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(BrowseViewModel::class.java)
        val root = inflater.inflate(R.layout.browse_fragment, container, false)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // TODO: Use the ViewModel
    }

}
