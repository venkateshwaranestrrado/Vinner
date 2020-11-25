package com.estrrado.vinner.adapters

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.estrrado.vinner.data.models.Product
import com.estrrado.vinner.data.models.RelatedProducts
import com.estrrado.vinner.data.models.Review
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.ui.ProductDetails

class RelatedProuctsAdapter(
    private var activity: FragmentActivity,
    private var dataList: List<RelatedProducts?>
) :
    RecyclerView.Adapter<RelatedProuctsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(activity).inflate(R.layout.item_home_product, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var productId=dataList.get(position)!!.product_id
        var rating = ""
        rating= dataList?.get(position)!!.rating!!
        holder.name.text = dataList?.get(position)!!.product_title
        holder.qty.text = dataList?.get(position)!!.unit
        if (rating != null && !rating.equals("")) {
            holder.ratingBar.rating = rating!!.toFloat().toFloat()
        }
        holder.price.text = dataList?.get(position)!!.price +" "+ dataList?.get(position)!!.currency
        val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
        Glide.with(activity)
            .load(dataList?.get(position)!!.product_image)
            .transform(RoundedCorners(radius))
            .thumbnail(0.1f)
            .into(holder.image)
        val bundle = Bundle()
        bundle.putString(Constants.PRODUCT_ID, productId)
        val mfragment= ProductDetails()
        mfragment.arguments=bundle
        holder.card.setOnClickListener {
            activity.getSupportFragmentManager().beginTransaction().replace(
                R.id.nav_host_fragment,
                mfragment
            )
                .addToBackStack(null).commit()
        }
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.name)
        val image: ImageView = v.findViewById(R.id.image)
        val ratingBar: RatingBar = v.findViewById(R.id.ratingBar2)
        val qty: TextView = v.findViewById(R.id.qty)
        val price: TextView = v.findViewById(R.id.price)
        val card: CardView = v.findViewById(R.id.lyt_container)
    }
}