package com.estrrado.vinner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.data.CategoryItem
import com.estrrado.vinner.data.models.Category
import com.estrrado.vinner.data.models.response.Datum

import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Constants.PRODUCT_NAME


import kotlinx.android.synthetic.main.toolbar.*

class CategoryAdapter(
    private var activity: FragmentActivity,
    private var dataList: List<Category>?,
    private var catList: List<Datum>?
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var productId: String = ""
    var produtName:String=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(activity).inflate(R.layout.item_home_category, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        if (dataList != null)
            return dataList!!.size
        else
            return catList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (dataList != null) {

            holder.name.text = dataList?.get(position)!!.categoryName
            val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
            Glide.with(activity)
                .load(dataList?.get(position)!!.categoryImage)
                .transform(RoundedCorners(radius))
                .thumbnail(0.1f)
                .into(holder.image)
            productId= dataList!!.get(position).categoryId!!
            produtName=dataList!!.get(position).categoryName!!
            holder.mView!!.setOnClickListener {
                productId= dataList!!.get(position).categoryId!!
                produtName=dataList!!.get(position).categoryName!!
//                printToast(activity,productId)
                val bundle = bundleOf(PRODUCT_ID to productId, PRODUCT_NAME to produtName)

         Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_productCategory,bundle)
            }

        }

        else {


            holder.name.text = catList?.get(position)!!.getCategoryName()
            val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
            Glide.with(activity)
                .load(catList?.get(position)!!.getCategoryImage())
                .transform(RoundedCorners(radius))
                .thumbnail(0.1f)
                .into(holder.image)


            holder.mView!!.setOnClickListener {
                productId = catList!!.get(position).getCategoryId()!!
                produtName=catList!!.get(position).getCategoryName()!!
//                printToast(activity, productId)
                val bundle = bundleOf(PRODUCT_ID to productId, PRODUCT_NAME to produtName)

                Navigation.findNavController(it)
                    .navigate(R.id.action_navigation_browse_to_productCategory, bundle)
            }
        }
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.name)
        val image: ImageView = v.findViewById(R.id.image)
        val mView: ConstraintLayout = v.findViewById(R.id.mView)
    }


}