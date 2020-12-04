package com.estrrado.vinner.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import kotlinx.android.synthetic.main.item_search_product.*


class SearchAdapter(private var activity: FragmentActivity) : RecyclerView.Adapter<ItemViewHolder>() {
     var mSearch = ArrayList<AddressList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(
                activity
            ).inflate(R.layout.item_search_product, parent, false)
        )

    }


        override fun getItemCount(): Int {
            return mSearch.size
        }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (mSearch!!.get(position)!!.current_stock =="0"){

            holder.srchname?.text="OUT OF STOCK"
//                    holder.name?.visibility=View.GONE
            holder.srchname?.visibility=View.VISIBLE
            holder.srchname?.setTextColor(activity.getResources().getColor(R.color.red));
            holder.view.setEnabled(false);
            holder.view.setClickable(false);
        }

        var rating = ""
        var productId= mSearch?.get(position)!!.product_id!!
        holder.prdctname.text = mSearch?.get(position)!!.product_title
        holder.price.text = mSearch?.get(position)!!.price  + " " + mSearch?.get(position)!!.currency
        holder.unit.text = mSearch?.get(position)!!.unit
        val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
        Glide.with(activity)
            .load(mSearch?.get(position)!!.product_image)
            .transform(RoundedCorners(radius))
            .thumbnail(0.1f)
            .into(holder.prdctimage)

        rating=mSearch?.get(position)!!.rating!!
        if (rating!= null && !rating.equals("")) {
            holder.prdctratingBar.rating = rating.toFloat()
        }

        holder.view.setOnClickListener {

            val bundle = bundleOf(PRODUCT_ID to productId)

                it.findNavController()
                    ?.navigate(R.id.navigation_product,bundle)



        }


    }
    fun setSongs(search: ArrayList<AddressList>){
        mSearch = search
    }

    private fun hide(){
        activity.srch_lyt.setClickable(false);
        activity.srch_lyt.setEnabled(false);
    }



}
class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val prdctname: TextView = v.findViewById(R.id.name)
    val prdctimage: ImageView = v.findViewById(R.id.image)
    val prdctratingBar: RatingBar = v.findViewById(R.id.ratingBar2)
    val price: TextView = v.findViewById(R.id.price)
    val unit: TextView = v.findViewById(R.id.unit)
        val view:ConstraintLayout=v.findViewById(R.id.srch_lyt)
    val srchname: TextView = v.findViewById(R.id.srchname)

}

