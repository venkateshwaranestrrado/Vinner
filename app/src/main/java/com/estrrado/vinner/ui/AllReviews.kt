package com.estrrado.vinner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estrrado.vinner.R
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.data.models.Review
import kotlinx.android.synthetic.main.fragment_all_reviews.*
import kotlinx.android.synthetic.main.toolbar_prev.*

class AllReviews : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageTitle.text = "All Reviews"

        rvReviews.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvReviews.adapter = ItemAdapter((requireActivity() as VinnerActivity).reviews)

    }

    inner class ItemAdapter(
        private var reviewList: List<Review?>?
    ) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView =
                LayoutInflater.from(activity).inflate(R.layout.adapter_allreviews, parent, false)
            val holder = ViewHolder(itemView)
            return holder
        }

        override fun getItemCount(): Int {
            reviewList?.let {
                return it.size
            }
            return 0
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtTitle.text = reviewList?.get(position)?.reviewTitle
            holder.txtReview.text = reviewList?.get(position)?.review
            holder.txtUser.text = "Reviewed by " + reviewList?.get(position)?.user
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val txtTitle: TextView = v.findViewById(R.id.txtTitle)
            val txtReview: TextView = v.findViewById(R.id.txtReview)
            val txtUser: TextView = v.findViewById(R.id.txtUser)
        }

    }

}