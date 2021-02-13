package com.estrrado.vinner.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.Products
import com.estrrado.vinner.data.models.TrackOrdDetModel
import com.estrrado.vinner.retrofit.ApiClient.gson
import kotlinx.android.synthetic.main.fragment_track_order_detail.*
import kotlinx.android.synthetic.main.toolbar_prev.*

class TrackOrderDetail : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageTitle.text = "Track Order Details"
        val data = arguments?.getString("data")
        val dataModel = gson.fromJson(data, TrackOrdDetModel::class.java)

        txtOrderId.text = "Order ID : " + dataModel.order_id
        txtOrdDate.text = "Order Date : " + dataModel.order_date
        txtExpDate.text = "Expected Delivery : " + dataModel.expt_from + " to " + dataModel.expt_to
        txtShipOp.text = "Shipping Operator : " + dataModel.ship_operator
        txtStatus.text = dataModel.d_status

        when (dataModel.d_status) {
            "pending" -> {
                txtExpDate.visibility = View.VISIBLE
                txtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
            }
            "delivered" -> {
                txtExpDate.visibility = View.GONE
                txtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
            else -> {
                txtExpDate.visibility = View.GONE
                txtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        }

        rvItems.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvItems.adapter = ItemAdapter(dataModel.products)


    }

    inner class ItemAdapter(
        private var productsList: ArrayList<Products>
    ) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView =
                LayoutInflater.from(activity).inflate(R.layout.adapter_trackorder, parent, false)
            val holder = ViewHolder(itemView)
            return holder
        }

        override fun getItemCount(): Int {
            return productsList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtSno.text = (position + 1).toString()
            holder.txtName.text = productsList.get(position).name
        }


        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val txtSno: TextView = v.findViewById(R.id.txtSno)
            val txtName: TextView = v.findViewById(R.id.txtName)
        }


    }

}