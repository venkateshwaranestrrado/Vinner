package com.estrrado.vinner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.Featured
import com.estrrado.vinner.data.models.response.Data
import com.estrrado.vinner.data.models.response.Datum
import com.estrrado.vinner.helper.PRODUCT_ID

class ProductsAdapter(
    private var activity: FragmentActivity,
    private var dataList: List<Featured?>?,
    private var productList: List<Datum>?,
    private var view: View?
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView =
            LayoutInflater.from(activity).inflate(R.layout.item_home_product, parent, false)
        if (dataList == null)
            itemView =
                LayoutInflater.from(activity).inflate(R.layout.item_product, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        if (dataList != null)
            return dataList?.size ?: 0
        else
            return productList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var name = ""
        var qty = ""
        var price = ""
        var rating = ""
        var img = ""
        var productId = ""

        if (dataList != null) {
            name = dataList?.get(position)!!.prdName!!
            qty = dataList?.get(position)!!.qty + " " + dataList?.get(position)!!.unit
            price = dataList?.get(position)!!.price + " " + dataList?.get(position)!!.currency
            rating = dataList?.get(position)!!.rating!!
            img = dataList?.get(position)!!.prdImage!!
            productId = dataList?.get(position)!!.prdId!!
        } else {
            name = productList?.get(position)!!.productTitle!!
            qty = productList?.get(position)!!.qty + " " + productList?.get(position)!!.unit
            price = productList?.get(position)!!.price + " " + productList?.get(position)!!.currency
            rating = productList?.get(position)!!.rating.toString()
            img = productList?.get(position)!!.productImage!!
            productId = productList?.get(position)!!.productId!!
        }

        holder.name.text = name
        holder.qty.text = qty
        holder.price.text = price

        if (rating != null && !rating.equals(""))
            holder.rating.rating = rating!!.toFloat()

        /*if (dataList!![position]!!.fav!!) {
            holder.like.setImageResource(R.drawable.ic_heart_select)
        } else {
            holder.like.setImageResource(R.drawable.ic_heart)
        }*/

        /* holder.like.setOnClickListener {
             if (!dataList!![position]!!.fav!!) {
                 dataList!![position]!!.fav = true
                 holder.like.setImageResource(R.drawable.ic_heart_select)
                 //callApi(dataList!![position]!!.productId, true)
             } else {
                 dataList!![position]!!.fav = false
                 holder.like.setImageResource(R.drawable.ic_heart)
                // callApi(dataList!![position]!!.productId, false)
             }
         }*/

        Glide.with(activity).load(img)
            .into(holder.image)
//        holder.image.setOnClickListener {
//            Navigation.findNavController(it).navigate(
//                R.id.action_homeFragment_to_ProductFragment
//
//            )
//        }

        holder.cardView.setOnClickListener {
            val bundle = bundleOf(PRODUCT_ID to productId)
            if (dataList != null)
                view?.findNavController()
                    ?.navigate(R.id.action_homeFragment_to_ProductFragment, bundle)
            else
                view?.findNavController()
                    ?.navigate(R.id.action_productListFragment_to_navigation_product, bundle)
        }

    }

    /* private fun callApi(productId: String?, flag: Boolean) {
         if (flag) {
             homeVM.addWishList(Input(productId = productId)).observe(activity!!, Observer {
                 printT(activity, it!!.message!!)
             })
         } else {
             homeVM.removeWishList(Input(productId = productId)).observe(activity!!, Observer {
                 printT(activity, it!!.message!!)
             })
         }
     }*/

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val price: TextView = v.findViewById(R.id.price)
        val name: TextView = v.findViewById(R.id.name)
        val image: ImageView = v.findViewById(R.id.image)
        val qty: TextView = v.findViewById(R.id.qty)
        val rating: RatingBar = v.findViewById(R.id.ratingBar2)
        val cardView: CardView = v.findViewById(R.id.lyt_container)

        // val like: ImageView = v.findViewById(R.id.like)
        val itemview: View = v

        /* init {
             if (!AppPreference.getInstance().readBoolean(IS_LOGGED)) {
                 like.visibility = View.GONE
             }
         }*/


    }
}