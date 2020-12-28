package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.ADDRESS_ID
import com.estrrado.vinner.helper.Constants.CITY
import com.estrrado.vinner.helper.Constants.COUNTRY
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.IS_DEFAULT
import com.estrrado.vinner.helper.Constants.IS_EDIT
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.NAME
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.addressSelected
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.ui.more.AddAddress
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_address_list.*
import kotlinx.android.synthetic.main.toolbar_back.*
import java.util.*
import kotlin.collections.ArrayList


class Address_list : Fragment() {

    var vModel: HomeVM? = null
    var address: String? = null
    private var addressAdapter: AddresslistAdapter? = null
    var from: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_address_list, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        initControll()
        from = arguments?.getInt(Constants.FROM)
        if (arguments?.getInt(Constants.FROM) == null || arguments?.getInt(Constants.FROM) != 1)
            getData()
        else getCheckoutAddress()
        initialiseSearch()
        pageTitle.text = "Address List"
    }

    private fun initialiseSearch() {
        edt_search.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edt_search.getRight() - edt_search.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    addressAdapter?.filter?.filter(edt_search.text.toString())
                    return@OnTouchListener true
                }
            }
            false
        })

        edt_search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                addressAdapter?.filter?.filter(edt_search.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun initControll() {
        const_add_new_address.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, AddAddress()).commit()
            val bundle = bundleOf(Constants.FROM to from)
            requireView().findNavController()
                .navigate(R.id.action_address_list_to_addAddress, bundle)
        }


    }

    private fun getData() {
        if (com.estrrado.vinner.helper.Helper.isNetworkAvailable(requireContext())) {
            progressaddresslist.visibility = View.VISIBLE

            vModel!!.getaddresslist(
                RequestModel
                    (accessToken = Preferences.get(activity, Constants.ACCESS_TOKEN))
            ).observe(requireActivity(), Observer {
                progressaddresslist.visibility = View.GONE
                if (it!!.status.equals(SUCCESS)) {
                    addressAdapter = AddresslistAdapter(
                        it.data,
                        vModel!!,
                        Helper,
                        from,
                        view,
                        requireActivity()
                    )
                    recy_address_list.adapter = addressAdapter
                    recy_address_list.layoutManager = LinearLayoutManager(requireContext())
                    address = it.data!!.get(0).adrs_id
                    Preferences.put(activity, Preferences.ADDRESS_ID, address!!)
                } else printToast(requireContext(), it.message.toString())
            })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    class AddresslistAdapter(
        var dataItem: ArrayList<AddressList>?,
        val vModel: HomeVM,
        val param: Helper,
        val from: Int?,
        val view: View?,
        private var activity: FragmentActivity
    ) : RecyclerView.Adapter<AddresslistAdapter.ViewHolder>(), Filterable {

        var addressFilterList: List<AddressList?>? = ArrayList<AddressList?>()

        init {
            addressFilterList = this.dataItem!!
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name = itemView.findViewById<TextView?>(R.id.tv_name)
            val address1 = itemView.findViewById<TextView?>(R.id.tv_address1)
            val number = itemView.findViewById<TextView?>(R.id.tv_number)
            val starimage = itemView.findViewById<ImageView?>(R.id.img_star)
            val txtRoadName = itemView.findViewById<TextView?>(R.id.tv_road_name)
            val txtCity = itemView.findViewById<TextView?>(R.id.tv_city)
            val txtCountry = itemView.findViewById<TextView?>(R.id.tv_country)
            val txtLandMark = itemView.findViewById<TextView?>(R.id.tv_landmark)
            val txtAddressType = itemView.findViewById<TextView?>(R.id.tv_address_type)
            val txtZip = itemView.findViewById<TextView?>(R.id.tv_zipcode)
            val constContainer = itemView.findViewById<ConstraintLayout?>(R.id.const_container)

            val buttonedit = itemView.findViewById<Button?>(R.id.btn_edit)
            val buttondlte = itemView.findViewById<Button?>(R.id.btn_delete)
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charSearch = constraint.toString()
                    if (charSearch.isEmpty()) {
                        if (dataItem != null) {
                            addressFilterList = dataItem
                        }
                    } else {
                        val resultList = ArrayList<AddressList?>()
                        for (i in 0 until dataItem!!.size) {
                            if (dataItem!!.get(i)!!.name!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT)) || dataItem!!.get(
                                    i
                                )!!.address_type!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT)) || dataItem!!.get(
                                    i
                                )!!.city!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT)) || dataItem!!.get(
                                    i
                                )!!.country!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT)) || dataItem!!.get(
                                    i
                                )!!.zip!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT)) || dataItem!!.get(
                                    i
                                )!!.landmark!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT)) || dataItem!!.get(
                                    i
                                )!!.house_flat!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT)) || dataItem!!.get(
                                    i
                                )!!.road_name!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT)) || dataItem!!.get(
                                    i
                                )!!.house_flat!!.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT))
                            ) {
                                resultList.add(dataItem!!.get(i)!!)
                            }
                        }
                        addressFilterList = resultList
                    }
                    val filterResults = FilterResults()
                    filterResults.values = addressFilterList
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    addressFilterList = results?.values as List<AddressList?>
                    notifyDataSetChanged()
                    if (addressFilterList!!.size <= 0)
                        printToast(activity, activity!!.getString(R.string.no_address_found))
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_address_list,
                    parent,
                    false
                )
            )
        }


        //            }
        override fun getItemCount(): Int {
            return addressFilterList?.size ?: 0
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            if (addressFilterList != null) {
                val item = addressFilterList!![holder.adapterPosition] as AddressList
                holder.name?.text = addressFilterList?.get(position)!!.name
                holder.address1?.text = addressFilterList!!.get(
                    position
                )!!.house_flat
                holder.txtRoadName?.text = addressFilterList?.get(position)!!.road_name
                holder.txtZip?.text = addressFilterList?.get(position)!!.zip
                holder.txtCity?.text = addressFilterList?.get(position)!!.city
                holder.txtCountry?.text = addressFilterList?.get(position)!!.country
                holder.txtLandMark?.text = addressFilterList?.get(position)!!.landmark
                holder.txtAddressType?.text = addressFilterList?.get(position)!!.address_type
//                holder.number?.text = categoriesFilterList?.get(position)!!.p
                if (addressFilterList!!.get(position)!!.default == "1") {
                    holder.starimage!!.visibility = View.VISIBLE
                } else {
                    holder.starimage!!.visibility = View.INVISIBLE
                }

                holder.buttonedit!!.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(ADDRESS_ID, addressFilterList!!.get(position)!!.adrs_id)
                    bundle.putString(
                        Constants.ADDDRESS_TYPE,
                        addressFilterList!!.get(position)!!.address_type
                    )
                    bundle.putString(HOUSENAME, addressFilterList!!.get(position)!!.house_flat)
                    bundle.putString(ROAD_NAME, addressFilterList!!.get(position)!!.road_name)
                    bundle.putString(PINCODE, addressFilterList!!.get(position)!!.zip)
                    bundle.putString(LANDMARK, addressFilterList!!.get(position)!!.landmark)
                    bundle.putString(NAME, addressFilterList!!.get(position)!!.name)
                    bundle.putString(CITY, addressFilterList!!.get(position)!!.city)
                    bundle.putString(COUNTRY, addressFilterList!!.get(position)!!.country_code)
                    bundle.putString(IS_DEFAULT, addressFilterList!!.get(position)!!.default)
                    bundle.putBoolean(IS_EDIT, true)
                    if (from != null) {
                        bundle.putInt(Constants.FROM, from)
                    }
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, AddAddress()).commit()
                    view?.findNavController()
                        ?.navigate(R.id.action_address_list_to_addAddress, bundle)
                }

                holder.buttondlte!!.setOnClickListener {
                    activity.progressaddresslist.visibility = View.VISIBLE
                    deleteItem(item, vModel, param)
                    AddresslistAdapter(
                        (addressFilterList as ArrayList<AddressList>?)!!,
                        vModel,
                        param,
                        from,
                        view,
                        activity
                    ).notifyDataSetChanged()
                    activity.recy_address_list.invalidate()
                }

                holder.constContainer!!.setOnClickListener {
                    vModel.getAddress.value = item
                    if (from != null && from == 1) {
                        addressSelected = item
                        activity.onBackPressed()
                    }
                }


            }
        }


        private fun deleteItem(
            item: AddressList, vModel: HomeVM, param: Helper
        ) {
            if (Helper.isNetworkAvailable(activity)) {
                vModel.getAddressdlte(
                    RequestModel(
                        accessToken = Preferences.get(activity, ACCESS_TOKEN),
                        address_id = item.adrs_id
                    )
                ).observe(activity, Observer {
                    if (it!!.status == "success") {
                        activity.progressaddresslist.visibility = View.GONE
                        notifyDataSetChanged()
                        activity.supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, Address_list()).commit()
                        printToast(activity!!, "Address deleted successfully")
                    }
                })
            }

        }
    }

    @SuppressLint("SetTextI18n")
    fun getCheckoutAddress() {
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
            requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
            progressaddresslist.visibility = View.VISIBLE
            vModel!!.getCheckoutAddressList(requestModel).observe(this,
                Observer {
                    progressaddresslist.visibility = View.GONE
                    if (it?.status.equals(Constants.SUCCESS)) {
                        addressAdapter = AddresslistAdapter(
                            it!!.data,
                            vModel!!,
                            Helper,
                            from,
                            view,
                            requireActivity()
                        )
//                        var checkoutAddressList = it!!.data
//                        if (checkoutAddressList != null && checkoutAddressList.size > 0) {
//                            cardview_deliveryaddress.visibility = View.VISIBLE
//                            layout_add_address.visibility = View.GONE
//                            var adress = ""
//                            for (i in 0 until checkoutAddressList!!.size) {
//                                if (i == 0 || checkoutAddressList!!.get(i)!!.default == "1")
//                                    adress = checkoutAddressList.get(i).name + ", " +
//                                            checkoutAddressList.get(i).road_name + ", " +
//                                            checkoutAddressList.get(i).city + " " +
//                                            checkoutAddressList.get(i).zip + ", " +
//                                            checkoutAddressList.get(i).country + ", "
//
//                            }
//                            txt_address.text = adress
//                        } else {
//                            cardview_deliveryaddress.visibility = View.GONE
//                            layout_add_address.visibility = View.VISIBLE
//                        }

                        recy_address_list.adapter = addressAdapter
                        recy_address_list.layoutManager = LinearLayoutManager(requireContext())
//                        address = it!!.data!!.get(0).adrs_id
//                        Preferences.put(activity, Preferences.ADDRESS_ID, address!!)
                    } else {
                        printToast(requireContext(), it!!.message!!)
//                        cardview_deliveryaddress.visibility = View.GONE
//                        layout_add_address.visibility = View.VISIBLE
                    }
                })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

}


