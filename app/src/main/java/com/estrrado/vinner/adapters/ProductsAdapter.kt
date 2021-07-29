package com.estrrado.vinner.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.Featured
import com.estrrado.vinner.data.models.response.Datum
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.helper.priceFormat
import com.estrrado.vinner.ui.ProductDetails
import kotlinx.android.synthetic.main.item_home_product.*

class ProductsAdapter(
    private var activity: FragmentActivity,
    private var dataList: List<Featured?>?,
    private var productList: List<Datum>?,
    private var view: View?
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>(), Filterable {

    var dataListAll: List<Featured?>? = null
    var productListAll: List<Datum>? = null

    init {
        dataListAll = dataList
        productListAll = productList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView =
            LayoutInflater.from(activity).inflate(R.layout.item_home_product, parent, false)
        if (dataList == null)
            itemView =
                LayoutInflater.from(activity).inflate(R.layout.item_product, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        if (dataList != null)
            return dataList?.size ?: 0
        else
            return productList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var name = ""
        var qty = ""
        var price = ""
        var rating = ""
        var img = ""
        var productId = ""

        if (dataList != null) {
            if (dataList!!.get(holder.adapterPosition)!!.current_stock == "0") {
                holder.homename.text = "OUT OF STOCK"
                holder.homename.visibility = View.VISIBLE
                holder.homename.setTextColor(activity.getResources().getColor(R.color.red));
                holder.cardView.setEnabled(false);
                holder.cardView.setClickable(false);
                holder.image.alpha = 0.35f
            } else {
                holder.homename.text = ""
                holder.homename.visibility = View.GONE
                holder.cardView.setEnabled(true)
                holder.cardView.setClickable(true)
                holder.image.alpha = 1f
            }

            name = dataList?.get(holder.adapterPosition)!!.prdName!!
            qty = dataList?.get(holder.adapterPosition)!!.unit!!
            if (!dataList?.get(holder.adapterPosition)!!.price.equals("0"))
                price =
                    dataList?.get(holder.adapterPosition)!!.currency + " " + priceFormat(
                        dataList?.get(
                            holder.adapterPosition
                        )!!.price
                    )
            else price = ""
            rating = dataList?.get(holder.adapterPosition)!!.rating!!
            img = dataList?.get(holder.adapterPosition)!!.prdImage!!
            productId = dataList?.get(holder.adapterPosition)!!.prdId!!
        } else {
            if (productList!!.get(holder.adapterPosition).current_stock == "0") {
                holder.homename.text = "OUT OF STOCK"
                holder.homename.visibility = View.VISIBLE
                holder.homename.setTextColor(activity.getResources().getColor(R.color.red));
                holder.cardView.setEnabled(false)
                holder.cardView.setClickable(false)
                holder.image.alpha = 0.35f
            } else {
                holder.image.alpha = 1f
            }
            name = productList?.get(holder.adapterPosition)!!.productTitle!!
            qty = productList?.get(holder.adapterPosition)!!.unit!!
            if (!productList?.get(holder.adapterPosition)!!.price.equals("0"))
                price =
                    productList?.get(holder.adapterPosition)!!.currency + " " + priceFormat(
                        productList?.get(
                            holder.adapterPosition
                        )?.price
                    )
            else price = productList?.get(holder.adapterPosition)!!.currency + " 0.00"
            rating = productList?.get(holder.adapterPosition)!!.rating.toString()
            img = productList?.get(holder.adapterPosition)!!.productImage!!
            productId = productList?.get(holder.adapterPosition)!!.productId!!

        }

        holder.name.text = name
        holder.price.text = price

        if (!rating.equals(""))
            holder.rating.rating = rating.toFloat()

        val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
        Glide.with(activity)
            .load(img)
            .transform(RoundedCorners(radius))
            .thumbnail(0.1f)
            .into(holder.image)

        holder.cardView.setOnClickListener {
            val bundle = Bundle()
            val mfragment = ProductDetails()
            if (dataList != null) {
                bundle.putString(PRODUCT_ID, dataList?.get(holder.adapterPosition)?.prdId)
                /*mfragment.setArguments(bundle)
                activity.getSupportFragmentManager().beginTransaction().replace(
                    R.id.nav_host_fragment,
                    mfragment
                )
                    .addToBackStack(null).commit()*/
                view?.findNavController()
                    ?.navigate(R.id.action_homeFragment_to_ProductFragment, bundle)
            } else {
                val bundle =
                    bundleOf(PRODUCT_ID to productList?.get(holder.adapterPosition)?.productId)
                view?.findNavController()
                    ?.navigate(R.id.action_productListFragment_to_navigation_product, bundle)
            }

        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val price: TextView = v.findViewById(R.id.price)
        val name: TextView = v.findViewById(R.id.name)
        val image: ImageView = v.findViewById(R.id.image)

        //val qty: TextView = v.findViewById(R.id.qty)
        val rating: RatingBar = v.findViewById(R.id.ratingBar2)
        val cardView: CardView = v.findViewById(R.id.lyt_container)
        val homename: TextView = v.findViewById(R.id.housename)

        // val like: ImageView = v.findViewById(R.id.like)
        val itemview: View = v

        /* init {
             if (!AppPreference.getInstance().readBoolean(IS_LOGGED)) {
                 like.visibility = View.GONE
             }
         }*/


    }

    private fun hide() {
        activity.lyt_container.setClickable(false);
        activity.lyt_container.setEnabled(false);
    }

    inner class ItemFilter : Filter() {

        override fun performFiltering(p0: CharSequence?): FilterResults {
            if (dataListAll != null) {
                dataListAll?.let {
                    dataList = it.filter { model ->
                        model!!.prdName!!.toLowerCase().startsWith(p0.toString().toLowerCase())
                    }
                }
            } else {
                productListAll?.let {
                    productList = it.filter { model ->
                        model!!.productTitle!!.toLowerCase().startsWith(p0.toString().toLowerCase())
                    }
                }
            }
            return FilterResults()
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            notifyDataSetChanged()
            if (dataListAll != null) {
                dataList?.let {
                    if (it.size <= 0) {
                        Validation.printToastCenter(activity, "Product Not Found")
                    }
                }
            } else {
                productList?.let {
                    if (it.size <= 0) {
                        Validation.printToastCenter(activity, "Product Not Found")
                    }
                }
            }
        }

    }

    override fun getFilter(): Filter {
        return ItemFilter()
    }

}