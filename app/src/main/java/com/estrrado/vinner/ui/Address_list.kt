package com.estrrado.vinner.ui

import android.R.attr.key
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.CITY
import com.estrrado.vinner.helper.Constants.COUNTRY
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.IS_DEFAULT
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.NAME
import com.estrrado.vinner.helper.Constants.PHONE
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.ui.more.AddAddress
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_address_list.*
import kotlinx.android.synthetic.main.toolbar_back.*


class Address_list : Fragment() {

    var vModel: HomeVM? = null
    var address: String? = null
    private val Addressadapter: Adapter? = null
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
        getData()
        pageTitle.text = "Address List"
    }

    private fun initControll() {
        const_add_new_address.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, AddAddress()).commit()
        }


    }

    private fun getData() {
        if (com.estrrado.vinner.helper.Helper.isNetworkAvailable(requireContext())) {
//            progressaddresslist.visibility = View.VISIBLE

            vModel!!.getaddresslist(
                RequestModel
                    (accessToken = Preferences.get(activity, Constants.ACCESS_TOKEN))
            ).observe(requireActivity(), Observer {
                recy_address_list.adapter = Addresslist(
                    it!!.data,
                    vModel!!,
                    Helper,
                    requireActivity()
                )
                recy_address_list.layoutManager = LinearLayoutManager(requireContext())
                address = it.data!!.get(0).adrs_id
                Preferences.put(activity, Preferences.ADDRESS_ID, address!!)
                progressaddresslist.visibility = View.GONE
            })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    class Addresslist(
        var dataItem: ArrayList<AddressList>?,
        val vModel: HomeVM,
        val param: Helper,
        private var activity: FragmentActivity
    ) : RecyclerView.Adapter<Addresslist.ViewHolder>() {


        var adrsId: String? = null
        var AddressType: String? = null
        var Housename: String? = null
        var Pincode: String? = null
        var Roadname: String? = null
        var Landmark: String? = null
        var isDefault: String? = null
        var country: String? = null
        var city: String? = null
        var name: String? = null


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

            val buttonedit = itemView.findViewById<Button?>(R.id.btn_edit)
            val buttondlte = itemView.findViewById<Button?>(R.id.btn_delete)
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
            return dataItem?.size ?: 0
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            if (dataItem != null) {
                AddressType = dataItem!!.get(position).address_type
                Housename = dataItem!!.get(position).house_flat
                Roadname = dataItem!!.get(position).road_name
                Pincode = dataItem!!.get(position).zip
                Landmark = dataItem!!.get(position).landmark
                isDefault = dataItem!!.get(position).default
                country = dataItem!!.get(position).country
                city = dataItem!!.get(position).city
                name = dataItem!!.get(position).name

                val item = dataItem!![holder.adapterPosition] as AddressList
                adrsId = dataItem!!.get(position).adrs_id
                holder.name?.text = dataItem?.get(position)!!.name
                holder.address1?.text = dataItem?.get(position)!!.address_type + " " + dataItem!!.get(
                    position
                ).house_flat
                holder.txtRoadName?.text = dataItem?.get(position)!!.road_name
                holder.txtZip?.text = dataItem?.get(position)!!.zip
                holder.txtCity?.text = dataItem?.get(position)!!.city
                holder.txtCountry?.text = dataItem?.get(position)!!.country
                holder.txtLandMark?.text = dataItem?.get(position)!!.landmark
                holder.txtAddressType?.text = dataItem?.get(position)!!.address_type
//                holder.number?.text = dataItem?.get(position)!!.p

                if (dataItem!!.get(position).default == "1") {
                    holder.starimage!!.visibility = View.VISIBLE
                } else {
                    holder.starimage!!.visibility = View.INVISIBLE
                }

                holder.buttonedit!!.setOnClickListener {
                    val fragment = EditFragment()
                    val bundle = Bundle()
                    bundle.putString(Constants.ADDDRESS_TYPE, AddressType)
                    bundle.putString(HOUSENAME, Housename)
                    bundle.putString(ROAD_NAME, Roadname)
                    bundle.putString(PINCODE, Pincode)
                    bundle.putString(LANDMARK, Landmark)
                    bundle.putString(NAME, name)
                    bundle.putString(CITY, city)
                    bundle.putString(COUNTRY, country)
                    bundle.putString(PHONE, Landmark)
                    bundle.putString(IS_DEFAULT, isDefault)
                    fragment.arguments = bundle
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit()
                }

                holder.buttondlte!!.setOnClickListener {
                    activity.progressaddresslist.visibility = View.VISIBLE
                    deleteItem(item, vModel, param)
                    Addresslist(dataItem!!, vModel, param, activity).notifyDataSetChanged()
                    activity.recy_address_list.invalidate()
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

}


