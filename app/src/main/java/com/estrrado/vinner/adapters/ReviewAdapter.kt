package com.estrrado.vinner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.Review

class ReviewAdapter(
    private var activity: FragmentActivity,
    private var dataList: List<Review?>?
) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(activity).inflate(R.layout.item_review, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = dataList?.get(position)!!.user
        holder.title.text = dataList?.get(position)!!.user
        holder.review.text = dataList?.get(position)!!.user
        holder.ratingBar.rating = dataList?.get(position)!!.rating!!.toFloat()
        val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
//        Glide.with(activity)
//            .load(dataList?.get(position)!!.categoryImage)
//            .transform(RoundedCorners(radius))
//            .thumbnail(0.1f)
//            .into(holder.image)
        holder.mView!!.setOnClickListener {
            // Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_productFragment, bundleOf(BRAND_ID to dataList!![position]!!.brandId))
        }
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.txt_name)
        val image: ImageView = v.findViewById(R.id.img_profile)
        val ratingBar: RatingBar = v.findViewById(R.id.rating_bar)
        val title: TextView = v.findViewById(R.id.txt_title)
        val review: TextView = v.findViewById(R.id.txt_review)
        val mView: View = v
    }


}