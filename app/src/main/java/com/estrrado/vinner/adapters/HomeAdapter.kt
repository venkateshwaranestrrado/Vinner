package com.estrrado.vinner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.estrrado.vinner.R
import com.estrrado.vinner.data.CategoryItem
import com.estrrado.vinner.data.LatestProductItem

class HomeAdapter(var activity: FragmentActivity): RecyclerView.Adapter<HomeAdapter.ViewHolder>(){

    private val productItem= listOf( LatestProductItem("Vital Oxide","20 AED",R.drawable.pro1),LatestProductItem("Vital Oxide","20 AED",R.drawable.pro1),LatestProductItem("Vital Oxide","20 AED",R.drawable.pro1))

    private val categoryItem= listOf(CategoryItem(R.drawable.ic_cleaning,"Cleaning"),
        CategoryItem(R.drawable.ic_protection,"Protection"),CategoryItem(R.drawable.ic_air_purifier,"Air purifier"),CategoryItem(R.drawable.ic_hand_care,"Hand care"),CategoryItem(R.drawable.ic_sanitization,"Sanitization")
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.recycle.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        when (position) {
            0 -> {
                holder.category.text = "Featured Products"
               // holder.recycle.adapter = HomeProductsAdapter(activity, productItem)
            }
            1 -> {
                holder.category.text = "Browse By Category"
             //holder.recycle.adapter = CategoryAdapter(activity, categoryItem)
            }
            else -> {
               // holder.recycle.adapter = HomeCategoryAdapter(activity, data.products)
            }
        }
        holder.viewMore.setOnClickListener {
            when (position) {
                0 -> {
                    //Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_productFragment)
                }
                1 -> {
                    //Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_brandFragment)
                }
            }
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val category: TextView = v.findViewById(R.id.category)
        val viewMore: TextView = v.findViewById(R.id.viewMore)
       // val recycle: RecyclerView = v.findViewById(R.id.recycle)
    }








}