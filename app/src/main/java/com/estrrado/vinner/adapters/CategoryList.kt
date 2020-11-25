package com.estrrado.vinner.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.Constants
import kotlinx.android.synthetic.main.fragment_product_category.*
import kotlinx.android.synthetic.main.item_categry_list.*

class CategryList(private var activity: FragmentActivity, var dataItem: ArrayList<AddressList>?,
                  private var view: View?) : RecyclerView.Adapter<CategryList.ViewHolder>() {

    var productId: String = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_categry_list, parent, false))
    }


    //            }
    override fun getItemCount(): Int {
        return dataItem?.size ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        var rating = ""
        if (dataItem != null) {


            rating=dataItem?.get(position)!!.rating!!
            holder.name?.text = dataItem!!.get(position).product_title
            holder.price?.text = dataItem?.get(position)!!.price +" " + dataItem?.get(position)!!.currency
            holder.qty?.text = dataItem?.get(position)!!.unit

            if (dataItem!!.get(position).current_stock =="0"){

                holder.newname?.text="OUT OF STOCK"
//                holder.name?.visibility=View.GONE
                holder.newname?.visibility=View.VISIBLE
                holder.newname?.setTextColor(activity.getResources().getColor(R.color.red));
                holder.cardView.setEnabled(false);
                holder.cardView.setClickable(false);
            }

            if (rating != null && !rating.equals(""))

                holder.rating.rating = rating!!.toFloat()

            val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
            Glide.with(activity)
                .load(dataItem?.get(position)!!.product_image)
                .transform(RoundedCorners(radius))
                .thumbnail(0.1f)
                .into(holder.image)


            holder.cardView!!.setOnClickListener {

                productId= dataItem!!.get(position).product_id!!
                val bundle = bundleOf(Constants.PRODUCT_ID to productId)
                view!!.findNavController()
                    .navigate(R.id.action_productCategory_to_navigation_product, bundle)


            }


        }
        else{
            activity.emptylist.visibility= View.VISIBLE
        }

    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val price: TextView = itemView.findViewById(R.id.price)
        val name: TextView = itemView.findViewById(R.id.name)
        val newname: TextView = itemView.findViewById(R.id.newname)
        val image: ImageView = itemView.findViewById(R.id.cat_image)
        val qty: TextView = itemView.findViewById(R.id.qty)
        val rating: RatingBar = itemView.findViewById(R.id.ratingBar)
        val cardView: CardView = itemView.findViewById(R.id.lyt_catgry)

    }

}
