package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.data.models.response.AddressList
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.BRAND_ID
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_industry_category.*
import kotlinx.android.synthetic.main.fragment_product_category.emptylist
import kotlinx.android.synthetic.main.toolbar_back.*


class Industrycategory : Fragment() {

    private var vModel: HomeVM? = null
    var brandId: String = ""

    var adapter: IndstryList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(
                        activity,
                        ApiClient.apiServices!!
                    )
                )
            )
        ).get(HomeVM::class.java)
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_industry_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressindustrylist.visibility = View.VISIBLE

        pageTitle.setText(arguments?.getString("BRAND_NAME"))
        initcontroll()

        val searchTextId: Int = searchView.getContext().getResources()
            .getIdentifier("android:id/search_src_text", null, null)
        val searchText = searchView.findViewById<TextView>(searchTextId)
        if (searchText != null) {
            searchText.setTextColor(Color.WHITE)
            searchText.setHintTextColor(Color.WHITE)
        }

        searchView.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                imageView8.visibility = View.VISIBLE
                pageTitle.visibility = View.VISIBLE
                searchView.visibility = View.GONE
                return false
            }
        })

        imageView8.setOnClickListener {
            imageView8.visibility = View.GONE
            pageTitle.visibility = View.GONE
            searchView.visibility = View.VISIBLE
            searchView.isIconified = false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                adapter?.let {
                    it.filter.filter(p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter?.let {
                    it.filter.filter(p0)
                }
                return false
            }

        })

    }

    private fun initcontroll() {
        brandId = arguments?.getString(BRAND_ID)!!
        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(activity, ACCESS_TOKEN)
        requestModel.countryCode = Preferences.get(activity, Preferences.REGION_NAME)
        requestModel.industry_id = brandId
        vModel!!.getIndustrylist(requestModel)
            .observe(requireActivity(),
                Observer {
                    if (it!!.data!!.size > 0) {
                        progressindustrylist.visibility = View.GONE
                        adapter = IndstryList(requireActivity(), it!!.data, view)
                        recy_indstry_lst.adapter = adapter
                        recy_indstry_lst.layoutManager = (GridLayoutManager(activity, 2))

                    } else {
                        progressindustrylist.visibility = View.GONE
                        emptylist.visibility = View.VISIBLE
                    }

                })

    }

    class IndstryList(
        private var activity: FragmentActivity,
        var dataItem: ArrayList<AddressList>?,
        private var view: View?
    ) : RecyclerView.Adapter<IndstryList.ViewHolder>(), Filterable {

        var productId: String = ""
        var dataItemAll = ArrayList<AddressList>()

        init {
            dataItem?.let {
                dataItemAll.addAll(it)
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val price: TextView = itemView.findViewById(R.id.price)
            val name: TextView = itemView.findViewById(R.id.name)
            val image: ImageView = itemView.findViewById(R.id.cat_image)
            val qty: TextView = itemView.findViewById(R.id.qty)
            val rating: RatingBar = itemView.findViewById(R.id.ratingBar)
            val cardView: CardView = itemView.findViewById(R.id.lyt_catgry)
            val INDname: TextView = itemView.findViewById(R.id.INDname)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_industry_list, parent, false)
            )
        }


        //            }
        override fun getItemCount(): Int {
            return dataItem?.size ?: 0
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var rating = ""
            if (dataItem != null) {
                if (dataItem!!.get(position).current_stock == "0") {
                    holder.image.alpha = 0.35f
                    holder.INDname?.text = "OUT OF STOCK"
                    holder.INDname?.visibility = View.VISIBLE
                    holder.INDname?.setTextColor(activity.getResources().getColor(R.color.red));
                    holder.cardView.setEnabled(false);
                    holder.cardView.setClickable(false);
                } else {
                    holder.image.alpha = 1f
                }
                rating = dataItem?.get(position)!!.rating!!
                holder.name.text = dataItem!!.get(position).product_title
                holder.price.text =
                    dataItem?.get(position)!!.price + " " + dataItem?.get(position)!!.currency

                holder.price.visibility =
                    if (dataItem?.get(position)!!.price == "0") View.INVISIBLE else View.VISIBLE

                holder.qty.text = dataItem?.get(position)!!.unit

                if (rating != null && !rating.equals(""))

                    holder.rating.rating = rating.toFloat()

                val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
                Glide.with(activity)
                    .load(dataItem?.get(position)!!.product_image)
                    .transform(RoundedCorners(radius))
                    .thumbnail(0.1f)
                    .into(holder.image)


                holder.cardView.setOnClickListener {
                    productId = dataItem!!.get(position).product_id!!
                    val bundle = bundleOf(PRODUCT_ID to productId)
                    view!!.findNavController()
                        .navigate(R.id.action_industrycategory_to_navigation_product, bundle)


                }
            } else {
                activity.emptylist.visibility = View.VISIBLE
            }

        }

        inner class ItemFilter : Filter() {

            override fun performFiltering(p0: CharSequence?): FilterResults {
                dataItemAll?.let {
                    dataItem?.clear()
                    dataItem?.addAll(it.filter { model ->
                        model!!.product_title!!.toLowerCase()
                            .startsWith(p0.toString().toLowerCase())
                    })
                }
                return FilterResults()
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                notifyDataSetChanged()
                dataItem?.let {
                    if (it.size <= 0) {
                        Validation.printToastCenter(activity, "Product Not Found")
                    }
                }
            }

        }

        override fun getFilter(): Filter {
            return ItemFilter()
        }

    }
}





