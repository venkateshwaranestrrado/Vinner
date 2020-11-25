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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.data.CategoryItem
import com.estrrado.vinner.data.models.Category
import com.estrrado.vinner.data.models.response.Datum
import com.estrrado.vinner.helper.Constants.BRAND_ID

import kotlinx.android.synthetic.main.toolbar.*

class IndustryAdapter(
    private var activity: FragmentActivity,
    private var industryList: List<Datum>?
) :
    RecyclerView.Adapter<IndustryAdapter.ViewHolder>() {
    var brandId: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(activity).inflate(R.layout.item_industry, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
            return industryList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = industryList?.get(position)!!.getIndustryName()
            val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
            Glide.with(activity)
                .load(industryList?.get(position)!!.getIndustryImage())
                .transform(RoundedCorners(radius))
                .thumbnail(0.1f)
                .into(holder.image)
        holder.mView!!.setOnClickListener {
            brandId= industryList!!.get(position).getIndustryId()!!

            val bundle = bundleOf(BRAND_ID to brandId)
             Navigation.findNavController(it).navigate(R.id.action_navigation_browse_to_industrycategory,bundle)
        }
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.name)
        val image: ImageView = v.findViewById(R.id.image)
        val mView: View = v
    }


}