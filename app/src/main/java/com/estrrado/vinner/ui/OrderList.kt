package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.data.models.response.Productdtls
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_order_list.*
import kotlinx.android.synthetic.main.toolbar_back.*


class OrderList : Fragment() {

    var vModel: HomeVM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_list, container, false)
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

        getData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageTitle.text="Order List"
    }

    private fun getData() {
        if (com.estrrado.vinner.helper.Helper.isNetworkAvailable(requireContext())) {
//            progressorderlist.visibility = View.VISIBLE

            vModel!!.getOrderList(
                RequestModel(
                    accessToken = Preferences.get(activity, ACCESS_TOKEN),
                    search_date = "",
                    search_orderId = ""
                )
            ).observe(requireActivity(), Observer {
                var products = ArrayList<Productdtls?>()
                var data = it!!.data
                for (item: AddressList in data!!) {
                    var prod = item.product_details!!
                    if (prod != null) {
                        prod.map {
                            it?.delivery_status = item.delivery_status
                            it?.delivary_datetime = item.delivary_datetime
                            it?.sale_id = item.sale_id

                        }
                        products.addAll(prod)
                    }
                    recy_order_list.layoutManager = LinearLayoutManager(requireContext())
                    recy_order_list.adapter = Orderliist(products, requireActivity())
                    progressorderlist.visibility = View.GONE
                }
            })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }
    class Orderliist(
        var dataItem: ArrayList<Productdtls?>,

        private var activity: FragmentActivity
    ) : RecyclerView.Adapter<Orderliist.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val delivstatus: TextView = itemView.findViewById(R.id.tv_deliv_status)
            val name: TextView = itemView.findViewById(R.id.tv_prdct_name)
            val image: ImageView = itemView.findViewById(R.id.Productimage)
            val review: TextView = itemView.findViewById(R.id.tv_review)
            val rating: RatingBar = itemView.findViewById(R.id.product_rating)
            val orderlist: CardView = itemView.findViewById(R.id.ordrlist)

            val tvreview: TextView = itemView.findViewById(R.id.tv_review)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_order_list,
                    parent,
                    false
                )
            )
        }


        //              }
        override fun getItemCount(): Int {
            return dataItem!!.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var rating = ""

            if (dataItem != null) {

                if (dataItem!!.get(position)!!.delivery_status=="delivered"){
                    if (rating != null && !rating.equals(""))
                        rating= dataItem?.get(position)!!.rating!!
                    holder.rating.rating = rating!!.toFloat()
                }


                val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
                Glide.with(activity)
                    .load(dataItem?.get(position)!!.image)
                    .transform(RoundedCorners(radius))
                    .thumbnail(0.1f)
                    .into(holder.image)

                holder.name?.text =dataItem?.get(position)!!.name
                holder.delivstatus?.text = dataItem?.get(position)!!.delivary_datetime

                val bundle = Bundle()
                val mfragment=   Orderdetail()
                bundle.putString(Preferences.ORDER_ID, dataItem.get(position)!!.sale_id)
               mfragment.arguments=bundle
holder.orderlist.setOnClickListener {
    activity.getSupportFragmentManager().beginTransaction().replace(
        R.id.nav_host_fragment,
        mfragment
    )
        .addToBackStack(null).commit()
}



                holder.tvreview.setOnClickListener {
                    val mdfragment=   AddReview()

                    bundle.putString(Preferences.PRODUCTNAME, dataItem.get(position)!!.name)
                    bundle.putString(Preferences.PROFILEIMAGE, dataItem.get(position)!!.image)
                    bundle.putString(PRODUCT_ID, dataItem.get(position)!!.id)
                    mdfragment.arguments=bundle
                    activity.getSupportFragmentManager().beginTransaction().replace(
                        R.id.nav_host_fragment,
                        mdfragment
                    )
                        .addToBackStack(null).commit()
                }


            }
        }

    }



}



