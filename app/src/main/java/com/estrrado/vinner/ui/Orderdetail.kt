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
import com.estrrado.vinner.helper.Constants.DELIVERED
import com.estrrado.vinner.helper.Constants.ON_DELIVERY
import com.estrrado.vinner.helper.Constants.PENDING
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.priceFormat
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_order_details.*

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

        pageTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }

        initControl()
    }

    private fun initControl() {

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, Constants.ACCESS_TOKEN)
            requestModel.order_id = arguments?.getString(Preferences.ORDER_ID)

            vModel!!.getorderdetail(requestModel).observe(requireActivity(),
                Observer {
                    if (it?.status.equals(Constants.SUCCESS)) {
                        textView34.setText(it!!.data!!.order_date)
                        textView35.setText(it.data!!.order_id)
                        textView36.setText(it.data.getCurrency() + " " + priceFormat(it.data.order_total))
                        txt_orderd.setText(it.data.ordered)
                        txt_dlvrd.setText(it.data.delivered)
                        txt_pymntmthd.setText(it.data.payment_method)
                        txt_bill_house.setText(it.data.billing_address!!.get(0)!!.house_flat)
                        txt_road.setText(it.data.billing_address!!.get(0)!!.road_name)
                        txt_lnd.setText(it.data.billing_address!!.get(0)!!.landmark)
                        txt_zip.setText(it.data.billing_address!!.get(0)!!.zip)
                        txt_region.setText(it.data.billing_address!!.get(0)!!.country)

                        txt_ship_house.setText(it.data.shipping_address!!.get(0)!!.s_house_flat)
                        txt_ship_road.setText(it.data.shipping_address!!.get(0)!!.s_road_name)
                        txt_ship_lnd.setText(it.data.shipping_address!!.get(0)!!.s_landmark)
                        txt_ship_zip.setText(it.data.shipping_address!!.get(0)!!.s_zip)
                        txt_ship_region.setText(it.data.shipping_address!!.get(0)!!.s_country)

                        txt_items.setText(it.data.getCurrency() + " " + priceFormat(it.data.getTotalAmount()))
                        txt_packing.setText(it.data.getCurrency() + " " + priceFormat(it.data.shipping_cost))
                        txt_Orderstotal.setText(it.data.getCurrency() + " " + priceFormat(it.data.getGrandTotal()))
                        seek_bar.isEnabled = false
                        seek_bar.setMaxValue(2.0F).apply()
                        if (it.data.delivery_status.equals(PENDING))
                            seek_bar.setMaxStartValue(0.0F).apply()
                        else if (it.data.delivery_status.equals(ON_DELIVERY))
                            seek_bar.setMaxStartValue(1.0F).apply()
                        else if (it.data.delivery_status.equals(DELIVERED))
                            seek_bar.setMaxStartValue(2.0F).apply()
                        seek_bar.visibility = View.VISIBLE
                        textView.visibility = View.VISIBLE
                        textView7.visibility = View.VISIBLE
                        textView6.visibility = View.VISIBLE
                        recyclerView.layoutManager = LinearLayoutManager(
                            activity,
                            LinearLayoutManager.VERTICAL,
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
    ) :
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
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {


            var rating = 0
//        holder.rating.rating= dataItem?.get(position)!!.rating!!.toInt()
            holder.qty.text = String.format("Quantity : %s", dataItem!!.get(position).qty)
            holder.name.text = dataItem!!.get(position).name
            holder.price.text = newitem!!.getCurrency() + " " + priceFormat(dataItem?.get(position)?.price)

            val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
            Glide.with(activity)
                .load(dataItem?.get(position)!!.image)
                .transform(RoundedCorners(radius))
                .thumbnail(0.1f)
                .into(holder.image)


        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val qty: TextView = itemView.findViewById(R.id.textView39)
            val price: TextView = itemView.findViewById(R.id.textView38)
            val name: TextView = itemView.findViewById(R.id.textView37)
            val image: ImageView = itemView.findViewById(R.id.imageView9)

            val rating: RatingBar = itemView.findViewById(R.id.rating_bar)

        }


    }
}