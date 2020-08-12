package com.estrrado.vinner.ui.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.estrrado.vinner.R
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.HomeProductsAdapter
import com.estrrado.vinner.adapters.SliderAdapter
import com.estrrado.vinner.data.CategoryItem
import com.estrrado.vinner.data.LatestProductItem
import kotlinx.android.synthetic.main.fragment_home.homeList
import kotlinx.android.synthetic.main.fragment_home.pager
import kotlinx.android.synthetic.main.fragment_home.tab
import kotlinx.android.synthetic.main.fragment_request.*
import java.util.*

class RequestFragment : Fragment(),View.OnClickListener {

    private lateinit var requestViewModel: RequestViewModel
    private val banners = listOf(R.mipmap.banner,R.mipmap.banner,R.mipmap.banner

    )
    var mTimer = Timer()
    var timerLoad:Boolean = true
    private val productItem= listOf( LatestProductItem("Vital Oxide","20 AED",R.mipmap.pro1),
        LatestProductItem("Vital Oxide","20 AED",R.mipmap.pro1),
        LatestProductItem("Vital Oxide","20 AED",R.mipmap.pro1)
    )
    private val categoryItem= listOf(
        CategoryItem(R.drawable.ic_demo,"Request For Demo"),
        CategoryItem(R.drawable.ic_service,"Request For Service")

        )
    override fun onResume() {

        super.onResume()
        initControl()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requestViewModel =
            ViewModelProviders.of(this).get(RequestViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_request, container, false)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).open()
        initControl()
    }

    private fun initControl() {
        pager.adapter = SliderAdapter(requireActivity(),banners)
        pager.setPageTransformer(false) { v, p ->
            var position = Math.abs(Math.abs(p) - 1)
            v.scaleX = position / 2 + 0.6f
            v.scaleY = position / 2 + 0.5f

        }


        pager.currentItem = 0
        tab.setupWithViewPager(pager)
        homeList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        homeList.adapter = HomeProductsAdapter(requireActivity(), productItem)
        //requestList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        //requestList.adapter= RequestAdapter(requireActivity(),categoryItem)
        timerPager(pager)


        ivdemo.setOnClickListener(this)
        ivservice.setOnClickListener(this)

    }
    override fun onDestroy() {

        timerLoad = true
        mTimer.cancel()
        super.onDestroy()

    }

    private fun timerPager(viewPager: ViewPager) {
        if(timerLoad) {
            timerLoad = false
            mTimer.cancel()
            mTimer.purge()
            mTimer = Timer()
            var mTt = object : TimerTask() {
                override fun run() {
                    activity!!.runOnUiThread(Runnable {
                        try {
                            if (viewPager.currentItem == viewPager.adapter!!.count - 1) {
                                viewPager.currentItem = 0
                            } else {
                                viewPager.currentItem = viewPager.currentItem + 1
                            }
                        } catch (e: Exception) {
                            mTimer.cancel()
                            mTimer.purge()
                        }
                    })
                }
            }
            try {
                mTimer.schedule(mTt, 300, 2000)
            } catch (e: Exception) {
                mTimer.cancel()
                mTimer.schedule(mTt, 300, 2000)
            }
        }
    }

    override fun onClick(v: View?) {

        when(v!!.id){


            R.id.ivdemo->{

                Navigation.findNavController(v).navigate(
                    R.id.navigation_demo

                )
            }

            R.id.ivservice->{

                Navigation.findNavController(v).navigate(
                    R.id.navigation_service

                )
            }



        }

    }


}
