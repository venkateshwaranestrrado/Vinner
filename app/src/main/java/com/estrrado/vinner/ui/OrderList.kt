package com.estrrado.vinner.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.OrderModel
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.DELIVERED
import com.estrrado.vinner.helper.Constants.PRODUCT_ID
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_order_list.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderList : Fragment() {

    var vModel: HomeVM? = null
    private val myCalendar = Calendar.getInstance()
    var orderIdSearch = ""
    var dateSearch = ""

    var sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        getData()

        searchlist.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                dateSearch = ""
                orderIdSearch = ""
                if (searchlist.text.trim().toString().length > 0) {
                    var date: Date? = null
                    try {
                        date = sdf.parse(searchlist.text.trim().toString())
                    } catch (e: Exception) {
                    }
                    if (date != null) {
                        dateSearch = searchlist.text.trim().toString()
                    } else {
                        orderIdSearch = searchlist.text.trim().toString()
                    }
                    getData()
                }
                return@OnEditorActionListener true
            }
            false
        })

        txtCancel.setOnClickListener {
            searchlist.setText("")
            dateSearch = ""
            orderIdSearch = ""
            getData()
        }

        pageTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getData() {

        if (com.estrrado.vinner.helper.Helper.isNetworkAvailable(requireContext())) {
            progressorderlist.visibility = View.VISIBLE

            vModel!!.getOrderList(
                RequestModel(
                    accessToken = Preferences.get(activity, ACCESS_TOKEN),
                    search_date = dateSearch,
                    search_orderId = orderIdSearch
                )
            ).observe(requireActivity(), Observer {
                progressorderlist.visibility = View.GONE
                if (it!!.data != null && it.data!!.size > 0) {
                    recy_order_list.layoutManager = LinearLayoutManager(requireContext())
                    val orders = ArrayList<OrderModel>()
                    for (i in 0..it.data!!.size - 1) {
                        for (j in 0..it.data!![i].getProductDetails()!!.size - 1) {
                            val ord = it.data!![i]
                            val item = it.data!![i].getProductDetails()?.get(j)
                            orders.add(
                                OrderModel(
                                    ord.getSaleId(),
                                    ord.getOrderId(),
                                    item?.id,
                                    item?.name,
                                    item?.image,
                                    item?.reviewId,
                                    ord.getOrderDate(),
                                    ord.getDeliveryStatus(),
                                    ord.getDelivaryDatetime()
                                )
                            )
                        }
                    }
                    recy_order_list.adapter =
                        Orderliist(orders, this.requireView(), requireActivity())
                    progressorderlist.visibility = View.GONE
                } else printToast(requireContext(), it.message.toString())
            })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
        }
    }

    class Orderliist(
        var dataItem: ArrayList<OrderModel>,
        var view: View,
        private var activity: FragmentActivity
    ) : RecyclerView.Adapter<Orderliist.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val delivstatus: TextView = itemView.findViewById(R.id.tv_deliv_status)
            val name: TextView = itemView.findViewById(R.id.tv_prdct_name)
            val image: ImageView = itemView.findViewById(R.id.Productimage)
            val review: TextView = itemView.findViewById(R.id.tv_review)
            val rating: RatingBar = itemView.findViewById(R.id.product_rating)
            val orderlist: CardView = itemView.findViewById(R.id.ordrlist)
            val tvreview: TextView = itemView.findViewById(R.id.tv_review)
            val tvOrderId: TextView = itemView.findViewById(R.id.tv_order_id)
            val tvOrderDate: TextView = itemView.findViewById(R.id.tv_order_date)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_order_list,
                    parent,
                    false
                )
            )
        }


        //              }
        override fun getItemCount(): Int {
            return dataItem!!.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var rating = 0.0
            if (dataItem.get(position).delivery_status == DELIVERED) {
                holder.tvreview.visibility = View.VISIBLE
                val isReviewAdded = dataItem.get(position).review_id != 0
                /*for (i in 0 until dataItem.get(position)!!.getProductDetails()!!.size) {
                    if (dataItem.get(position)!!.getProductDetails()!!.get(i)!!.reviewId != 0
                    ) {
                        rating = dataItem.get(position)!!.getProductDetails()!!
                            .get(i)!!.rating!!.toDouble()
                        isReviewAdded = true
                        break
                    }
                }*/
                if (!isReviewAdded)
                    holder.tvreview.text = Constants.WRITE_A_REVIEW
                else
                    holder.tvreview.text = Constants.VIEW_REVIEW
            } else
                holder.tvreview.visibility = View.GONE

            holder.rating.rating = rating.toFloat()
            val radius = activity.resources.getDimensionPixelSize(R.dimen._15sdp)
            Glide.with(activity)
                .load(dataItem.get(position).image)
                .transform(RoundedCorners(radius))
                .thumbnail(0.1f)
                .into(holder.image)

            holder.name.text = dataItem.get(position).name
            holder.delivstatus.text = dataItem.get(position).delivary_datetime
            holder.tvOrderId.text = dataItem.get(position).order_id

            dataItem.get(position).order_date?.let {
                val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).parse(it)
                date?.let {
                    holder.tvOrderDate.text =
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
                }
            }

            holder.orderlist.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Preferences.ORDER_ID, dataItem.get(position).sale_id)
                view.findNavController()
                    .navigate(R.id.action_navigation_orderList_to_orderDetail, bundle)
            }

            holder.tvreview.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(
                    Preferences.PRODUCTNAME,
                    dataItem.get(position).name
                )
                bundle.putString(
                    Preferences.PROFILEIMAGE,
                    dataItem.get(position).image
                )
                bundle.putString(
                    PRODUCT_ID,
                    dataItem.get(position).prodid
                )
                view.findNavController()
                    .navigate(R.id.action_navigation_orderList_to_addReview, bundle)
            }


        }

    }

    private fun showCalender() {
        val datePickerDialog = DatePickerDialog(
            requireContext(), date, myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
//        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private val date =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

    private fun updateDate() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf =
            SimpleDateFormat(myFormat, Locale.getDefault())
        searchlist!!.setText(sdf.format(myCalendar.getTime()))
        orderIdSearch = ""
        dateSearch = searchlist.text.toString()
        getData()
    }

}



