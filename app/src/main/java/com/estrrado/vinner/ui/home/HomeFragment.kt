package com.estrrado.vinner.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.estrrado.vinner.R
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.adapters.CategoryAdapter
import com.estrrado.vinner.adapters.HomeProductsAdapter
import com.estrrado.vinner.adapters.SliderAdapter
import com.estrrado.vinner.data.CategoryItem

import com.estrrado.vinner.data.LatestProductItem
import com.estrrado.vinner.data.models.response.Data
import com.estrrado.vinner.data.models.response.Model
import com.estrrado.vinner.vm.HomeVM
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import java.io.IOException
import java.lang.IllegalStateException

import java.util.*

class HomeFragment : Fragment() {
    var vModel: HomeVM? = null
    private lateinit var homeViewModel: HomeViewModel
    private val banners = listOf(R.drawable.banner,R.drawable.banner,R.drawable.banner

    )
    var mTimer = Timer()
    var timerLoad:Boolean = true
    private val productItem= listOf( LatestProductItem("Vital Oxide","20 AED",R.drawable.pro1),
        LatestProductItem("Vital Oxide","20 AED",R.drawable.pro1),
        LatestProductItem("Vital Oxide","20 AED",R.drawable.pro1)
    )

    private val categoryItem= listOf(
        CategoryItem(R.drawable.ic_cleaning,"Cleaning"),
        CategoryItem(R.drawable.ic_protection,"Protection"),
        CategoryItem(R.drawable.ic_air_purifier,"Air purifier"),
        CategoryItem(R.drawable.ic_hand_care,"Hand care"),
        CategoryItem(R.drawable.ic_sanitization,"Sanitization")
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
       /* homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)*/
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        // val textView: TextView = root.findViewById(R.id.text_home)
       /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })*/

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as VinnerActivity).open()
     /*  vModel =
            ViewModelProviders.of(
                this,
                VinnerFactory(
                    HomeVM(
                        VinnerRespository.getInstance(
                            activity,
                            ApiClient.apiServices
                        )
                    )
                )
            ).get(HomeVM::class.java)*/
       initControl()
    }

    private fun initControl() {
       // Helper.showLoading(activity)
        pager.adapter = SliderAdapter(requireActivity(),banners)
        pager.setPageTransformer(false) { v, p ->
            var position = Math.abs(Math.abs(p) - 1)
            v.scaleX = position / 2 + 0.6f
            v.scaleY = position / 2 + 0.5f

        }
        timerPager(pager)

        pager.currentItem = 0
        tab.setupWithViewPager(pager)


        //val call = ApiClient.apiServices!!.getHomeList()




        categoryList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        categoryList.adapter=CategoryAdapter(requireActivity(),categoryItem)

       // fetchJson()

  try {


            vModel!!.getHomeList().observe(this, Observer {

                homeList.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//                homeList.adapter = HomeProductsAdapter(requireActivity(), it!!.data!!.results)



           // Log.d("Home Fragment",it!!.data!!.)







            })
        }

        catch(e: Exception) {


        }

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
    private fun getHomeList(): MutableLiveData<Model?> {

        Log.d("Home","testing")
        var data = MutableLiveData<Model?>()
    /* ApiClient.apiServices!!.getHomeList()!!.subscribeOn(
            Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("Home",data.value.toString())
                data.value = it

            }, {
                it.printStackTrace()
                //Helper.hideLoading()

            })*/
        return data
    }


//    fun fetchJson() {
//
//        val url = "https://estrradodemo.com/vinner/api/product"
//
//        val request = Request.Builder().url(url).build()
//        val client = OkHttpClient()
//        client.newCall(request).enqueue(object: Callback {
//            override fun onResponse(call: Call, response: Response) {
//                val body = response.body().toString()
//                println(body)
//
//                val gson = GsonBuilder().create()
//
//                try {
//                    val fromUserJson = Gson().toJson(body)
//                    val feed = gson.fromJson(fromUserJson, Data::class.java)
//                }
//                catch (e:IllegalStateException){
//                    Log.d("Message",e.message)
//
//                }
//
//               // Log.d("Message",feed.get(0).toString())
//
//
//
//            }
//
//            override fun onFailure(call: Call, e: IOException) {
//                println("Request Failed")
//            }
//        })
//
//    }

}


