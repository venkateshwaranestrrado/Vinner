package com.estrrado.vinner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.data.LatestProductItem

class HomeProductsAdapter(private var activity: FragmentActivity,
                          private var dataList: List<LatestProductItem?>?
                        ):RecyclerView.Adapter<HomeProductsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.item_home_product, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = (dataList?.get(position) as LatestProductItem).producttitle
        holder.productcode.text=(dataList?.get(position) as LatestProductItem).productcode
        holder.price.text = (dataList?.get(position) as LatestProductItem).saleprice

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

        Glide.with(activity).load((dataList?.get(position) as LatestProductItem).path).into(holder.image)
        holder.image.setOnClickListener {
         Navigation.findNavController(it).navigate(
                R.id.action_homeFragment_to_ProductFragment

            )
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
        val productcode: TextView = v.findViewById(R.id.amount)

       // val like: ImageView = v.findViewById(R.id.like)
        val itemview: View = v

       /* init {
            if (!AppPreference.getInstance().readBoolean(IS_LOGGED)) {
                like.visibility = View.GONE
            }
        }*/


    }
}