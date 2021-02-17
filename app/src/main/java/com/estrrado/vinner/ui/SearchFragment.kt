package com.estrrado.vinner.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.adapters.SearchAdapter
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Preferences.REGION_NAME
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.searchtoolbar.*

class SearchFragment : Fragment() {

    var mSearch = ArrayList<AddressList>()
    var mSearchFilter = ArrayList<AddressList>()
    private var vModel: HomeVM? = null

    private lateinit var gridlayoutmanager: GridLayoutManager
    private lateinit var adapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


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

        return inflater.inflate(R.layout.fragment_search, container, false)


    }

    override fun onResume() {
        super.onResume()
        editTextTextPersonName.requestFocus()
        Validation.openKeyboard(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = SearchAdapter(requireActivity())
        gridlayoutmanager = GridLayoutManager(requireContext(), 3)
        recycle_search.layoutManager = gridlayoutmanager
        recycle_search.adapter = adapter
        initcntroll()

        button2.setOnClickListener {
            if (editTextTextPersonName.text.toString().length > 0) {
                editTextTextPersonName.setText("")
            } else {
                Validation.hideKeyboard(requireActivity())
                requireActivity().onBackPressed()
            }
        }

        editTextTextPersonName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (count >= 1) {
                    // isSearching = true
                    var filteredSong = mSearch.filter {
                        it.product_title?.toLowerCase()!!.contains(s.toString().toLowerCase())
                    }

                    if (filteredSong.size <= 0) {
                        Validation.printToastCenter(requireContext(), "Product Not Found")
                    }

                    mSearchFilter.clear()
                    mSearchFilter.addAll(filteredSong)

                    updateSearchFilterList()
                } else {
                    updateSearchList()
                }
            }
        })

    }

    private fun initcntroll() {

        vModel!!.getsearch(
            RequestModel(
                accessToken = com.estrrado.vinner.helper.Preferences.get(activity, ACCESS_TOKEN),
                countryCode = com.estrrado.vinner.helper.Preferences.get(activity, REGION_NAME),
                search = ""
            )
        ).observe(requireActivity(),
            Observer {
                recycle_search.setLayoutManager(GridLayoutManager(context, 2))
                if (it != null) {
                    mSearch.addAll(it.data!!)

                    updateSearchList()

                }
            })


    }

    fun updateSearchList() {
        adapter.setSongs(mSearch)
        adapter.notifyDataSetChanged()
    }

    fun updateSearchFilterList() {
        adapter.setSongs(mSearchFilter)
        adapter.notifyDataSetChanged()
    }


}
