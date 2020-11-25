package com.estrrado.vinner.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.response.Productdetails












//
//
//class OrderdetailAdapter(private var activity: FragmentActivity,
//                         var dataItem: ArrayList<Productdetails>?):
//    RecyclerView.Adapter<OrderdetailAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_order_details, parent, false))
//    }
//
//    override fun getItemCount(): Int {
//        return dataItem?.size ?: 0
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: ViewHolder, position: Int){
//
//
//        var rating = 0
////        holder.rating.rating= dataItem?.get(position)!!.rating!!.toInt()
//        holder.name.text = dataItem!!.get(position).name
//        holder.price.text = dataItem?.get(position)!!.price + " " + productList?.get(position)!!.currency
//        holder.qty.text = dataItem?.get(position)!!.qty
//            val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
//            Glide.with(activity)
//                .load(dataItem?.get(position)!!.image)
//                .transform(RoundedCorners(radius))
//                .thumbnail(0.1f)
//                .into(holder.image)
//
//
//    }
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val price: TextView = itemView.findViewById(R.id.textView38)
//        val name: TextView = itemView.findViewById(R.id.textView37)
//        val image: ImageView = itemView.findViewById(R.id.imageView9)
//        val qty: TextView = itemView.findViewById(R.id.textView39)
//        val rating: RatingBar = itemView.findViewById(R.id.rating_bar)
//
//    }
//
//
//}
