package com.estrrado.vinner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.ui.CartadapterCallBack

class CartAdapter(
    private var activity: FragmentActivity,
    private var dataList: List<com.estrrado.vinner.data.models.CartItem?>?,
    private var cartadapterCallBack: CartadapterCallBack
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.item_cart, parent, false)
        val holder = CartAdapter.ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.name.text = dataList?.get(position)!!.productName
        holder.txtDesc.text = dataList?.get(position)!!.categoryName
        holder.txtCost.text =
            dataList?.get(position)!!.productTotal + " " + dataList?.get(position)!!.currency
        holder.txtDelivery.text = dataList?.get(position)!!.delivery
        holder.txtQty.text = dataList?.get(position)!!.productQuantity
        Glide.with(activity!!)
            .load(dataList?.get(position)!!.productImage)
            .thumbnail(0.1f)
            .into(holder.image)
        holder.txtPlus.setOnClickListener {
            changeQty(dataList?.get(position)!!.productId.toString(), true, holder.txtQty, position)
        }

        holder.txtMinus.setOnClickListener {
            changeQty(dataList?.get(position)!!.productId.toString(), false, holder.txtQty, position)
        }

        holder.txtRemove.setOnClickListener {
            cartadapterCallBack.productRemoved(dataList?.get(position)!!.productId.toString(), position)
        }
    }

    private fun changeQty(productId: String, isAdded: Boolean, txtQty: TextView, position: Int) {
        val qty = txtQty.text.toString().toInt()
        if (!isAdded && qty > 0) {
            txtQty.text = (qty - 1).toString()
        } else if (isAdded)
            txtQty.text = (qty + 1).toString()
        cartadapterCallBack.productUpdated(productId, txtQty.text.toString(), position)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.product_name)
        val image: ImageView = v.findViewById(R.id.ivcheckout)
        val txtPlus: TextView = v.findViewById(R.id.txt_qty_plus)
        val txtMinus: TextView = v.findViewById(R.id.txt_qty_minus)
        val txtQty: TextView = v.findViewById(R.id.txt_qty)
        val txtDesc: TextView = v.findViewById(R.id.product_description)
        val txtCost: TextView = v.findViewById(R.id.cost)
        val txtDelivery: TextView = v.findViewById(R.id.txt_delivery_amt)
        val txtRemove: TextView = v.findViewById(R.id.tvRemove)
    }

}