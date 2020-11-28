package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
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
import com.estrrado.vinner.data.models.response.Data
import com.estrrado.vinner.data.models.response.Productdetails
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_order_details.*
import kotlinx.android.synthetic.main.toolbar_prev.*


class Orderdetail : Fragment() {
    var vModel: HomeVM? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false)
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

        initControl()
    }



    private fun initControl(){
        pageTitle.setText("Order Detail")
        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, Constants.ACCESS_TOKEN)
            requestModel.order_id= arguments?.getString(Preferences.ORDER_ID)

            vModel!!.getorderdetail(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(Constants.SUCCESS)) {
                        textView34.setText(it!!.data!!.order_date)
                        textView35.setText(it.data!!.order_id)
                        textView36.setText(it.data.order_total+"(" + it.data.items_count + "item"+")" +""+it.data.getCurrency())
                        txt_orderd.setText(it.data.ordered)
                        txt_dlvrd.setText(it.data.delivered)
                        txt_pymntmthd.setText(it.data.payment_method)
                        txt_bill_house.setText(it.data.billing_address!!.get(0)!!.house_flat)
                        txt_road.setText(it.data.billing_address!!.get(0)!!.road_name)
                        txt_lnd.setText(it.data.billing_address!!.get(0)!!.landmark)
                        txt_zip.setText(it.data.billing_address!!.get(0)!!.zip)

                        txt_ship_house.setText(it.data.shipping_address!!.get(0)!!.s_house_flat)
                        txt_ship_road.setText(it.data.shipping_address!!.get(0)!!.s_road_name)
                        txt_ship_lnd.setText(it.data.shipping_address!!.get(0)!!.s_landmark)
                        txt_ship_zip.setText(it.data.shipping_address!!.get(0)!!.s_zip)


                        txt_items.setText(it.data.getTotalAmount()+""+ it.data.getCurrency())
                        txt_packing.setText( it.data.shipping_cost+""+it.data.getCurrency())
                        txt_before_tax.setText(it.data.getTotalAmount()+""+ it.data.getCurrency())
                        txt_tax.setText(it.data.tax +""+ it.data.getCurrency())
                        txt_total.setText(it.data.getTotalAmount()+""+ it.data.getCurrency())
                        txt_Orderstotal.setText(it.data.getGrandTotal()+""+ it.data.getCurrency())


                        recyclerView.layoutManager = LinearLayoutManager(
                            activity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        recyclerView.adapter = OrderdetailAdapter(
                            requireActivity(),
                            it.data,
                            it.data.product_details
                        )

                    }

                })

                }

    }

    class OrderdetailAdapter(
        private var activity: FragmentActivity,
        var newitem: Data?,
        var dataItem: ArrayList<Productdetails>?
    ):
        RecyclerView.Adapter<OrderdetailAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.item_order_details,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return dataItem?.size ?: 0
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int){


            var rating = 0
//        holder.rating.rating= dataItem?.get(position)!!.rating!!.toInt()
            holder.name.text = dataItem!!.get(position).name
            holder.price.text = dataItem?.get(position)!!.price + " " + newitem!!.getCurrency()

            val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
            Glide.with(activity)
                .load(dataItem?.get(position)!!.image)
                .transform(RoundedCorners(radius))
                .thumbnail(0.1f)
                .into(holder.image)


        }
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val price: TextView = itemView.findViewById(R.id.textView38)
            val name: TextView = itemView.findViewById(R.id.textView37)
            val image: ImageView = itemView.findViewById(R.id.imageView9)

            val rating: RatingBar = itemView.findViewById(R.id.rating_bar)

        }


    }
}