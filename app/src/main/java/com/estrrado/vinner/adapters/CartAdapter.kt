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
import com.estrrado.vinner.data.CartItem
import com.estrrado.vinner.data.CategoryItem
import com.estrrado.vinner.data.LatestProductItem

class CartAdapter(
    private var activity: FragmentActivity,
    private var dataList: List<CartItem?>?
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.item_cart, parent, false)
        val holder = CartAdapter.ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.name.text = (dataList?.get(position) as CartItem).name?:""
        Glide.with(activity).load((dataList?.get(position) as CartItem).image).into(holder.image)

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.product_name)
        val image: ImageView = v.findViewById(R.id.ivcheckout)
        val mView: View = v
    }

}