package com.estrrado.vinner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.response.Model
import com.estrrado.vinner.data.models.retrofit.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)
        initControl()
    }

    private  fun initControl(){
        tvRSubmit.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvRSubmit ->{


              registerApiCall(Input("prabudh","prabudh.pk@gmail.com","9895097555","123456","123456")).observe(this,
                  Observer {

                  Log.d("Testing",it?.message)


                  })


              /*  val call: Call<Input> = ApiClient.apiServices!!.registerUser("prabudh","prabudh.pk@gmail.com","9895097555","123456","123456")
                call.enqueue(object : Callback<Input> {

                    override fun onResponse(call: Call<Input>, response: Response<Input>) {
                        var str_response = response!!.body()!!.toString()
                        val gson = Gson()

                        var json_contact:JSONObject = JSONObject( gson.toJson(str_response))
                        var shttp:String=json_contact.getString("http")

                        Log.d("Testing",shttp)

                    }

                    override fun onFailure(call: Call<Input>?, t: Throwable?) {
                        Log.d("Testing failure",t!!.localizedMessage.toString())
                    }

                })*/

                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }

        }
    }




    private fun registerApiCall(input: Input): MutableLiveData<Model?> {
        var data = MutableLiveData<Model?>()
        ApiClient.apiServices!!.registerUser(
            input.username,

            input.email,
            input.mobile,
            input.password,
            input.confirm_password
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                data.value = it
            }, {
                it.printStackTrace()

            })
        return data
    }
}
