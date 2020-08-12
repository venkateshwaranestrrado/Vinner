package com.estrrado.vinner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.estrrado.vinner.R
import com.estrrado.vinner.data.CategoryItem

class RequestAdapter(private var activity: FragmentActivity, private var dataList: List<CategoryItem?>?)
    :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.item_request, parent, false)
        val holder = CategoryAdapter.ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.name.text = (dataList?.get(position) as CategoryItem).name?:""
        Glide.with(activity).load((dataList?.get(position) as CategoryItem).image).into(holder.image)
        holder.mView!!.setOnClickListener {

            when(position){

                0 -> {
                    Navigation.findNavController(it).navigate(
                        R.id.navigation_demo

                    )

                }

                1 -> {
                    Navigation.findNavController(it).navigate(
                        R.id.navigation_service

                    )
                }

            }
            // Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_productFragment, bundleOf(BRAND_ID to dataList!![position]!!.brandId))
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.name)
        val image: ImageView = v.findViewById(R.id.image)
        val mView: View = v
    }


}