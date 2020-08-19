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
import com.estrrado.vinner.data.CategoryItem
import com.estrrado.vinner.data.models.Category
import kotlinx.android.synthetic.main.toolbar.*

class CategoryAdapter (
    private var activity: FragmentActivity,
    private var dataList: List<Category>?
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val itemView = LayoutInflater.from(activity).inflate(R.layout.item_home_category, parent, false)
    val holder = ViewHolder(itemView)
    return holder
}

override fun getItemCount(): Int {
    return dataList!!.size
}

override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.name.text = dataList?.get(position)!!.categoryName
    Glide.with(activity)
        .load(dataList?.get(position)!!.categoryImage)
        .thumbnail(0.1f)
        .into(holder.image)
    holder.mView!!.setOnClickListener {
        // Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_productFragment, bundleOf(BRAND_ID to dataList!![position]!!.brandId))
    }
}







class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = v.findViewById(R.id.name)
    val image: ImageView = v.findViewById(R.id.image)
    val mView: View = v
}



}