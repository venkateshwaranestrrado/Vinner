package com.estrrado.vinner.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.data.models.Notification
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Helper
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.toolbar_prev.*

class AllNotification : Fragment() {

    private var vModel: HomeVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageTitle.text = "Notifications"

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

        rvNotification.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        getNotificationList()

    }

    private fun getNotificationList() {

        if (Helper.isNetworkAvailable(requireContext())) {
            val requestModel = RequestModel()
            requestModel.accessToken = Preferences.get(activity, Constants.ACCESS_TOKEN)
            progressnotify.visibility = View.VISIBLE
            vModel!!.notifications(requestModel).observe(requireActivity(),
                Observer {
                    progressnotify.visibility = View.GONE
                    if (it?.status.equals(Constants.SUCCESS)) {
                        rvNotification.adapter = ItemAdapter(it?.data?.notifications)
                    } else {
                        if (it?.message.equals("Invalid access token")) {
                            startActivity(Intent(activity, LoginActivity::class.java))
                            requireActivity().finish()
                        } else {
                            Validation.printToast(requireContext(), it?.message!!)
                        }
                        Validation.printToast(this!!.requireContext()!!, it?.message.toString())
                    }

                })
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show()
            progressnotify.visibility = View.GONE
        }
    }

    inner class ItemAdapter(
        private var notifyist: List<Notification?>?
    ) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView =
                LayoutInflater.from(activity).inflate(R.layout.adapter_notification, parent, false)
            val holder = ViewHolder(itemView)
            return holder
        }

        override fun getItemCount(): Int {
            notifyist?.let {
                return it.size
            }
            return 0
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtTitle.text = notifyist?.get(position)?.title
            holder.txtDesc.text = notifyist?.get(position)?.desc

            var draw = 0
            when (notifyist?.get(position)?.notify_type) {
                "review_status" -> {
                    draw = R.drawable.star
                }
                "delivery_status" -> {
                    draw = R.drawable.truck
                }
                "payment_status" -> {
                    draw = R.drawable.money
                }
                else -> {
                    draw = R.drawable.cart
                }
            }

            holder.imgView.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    draw
                )
            )

        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val txtDesc: TextView = v.findViewById(R.id.txtDesc)
            val txtTitle: TextView = v.findViewById(R.id.txtTitle)
            val imgView: ImageView = v.findViewById(R.id.imgView)
        }

    }

}