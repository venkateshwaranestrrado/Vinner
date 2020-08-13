package com.estrrado.vinner.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.retrofit.ApiClient
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.vm.AuthVM
import com.estrrado.vinner.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    var authenticateVM: AuthVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)
        initControl()
    }

    private fun initControl() {
        tvRSubmit.setOnClickListener(this)

        authenticateVM = ViewModelProvider(
            this,
            MainViewModel(
                AuthVM(
                    VinnerRespository.getInstance(
                        this,
                        ApiClient.apiServices
                    )
                )
            )
        ).get(AuthVM::class.java)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvRSubmit -> {

                val userName = etUsername.text.toString()
                val email = etemail.text.toString()
                val refferl = etReferral.text.toString()
                val phoneNumber = edt_mobile.text.toString()
                val password = edt_password.text.toString()
                val confirmPassword = edt_confirm_password.text.toString()
                if (hasText(etUsername, REQUIRED) && hasText(etemail, REQUIRED) && hasText(
                        edt_mobile,
                        REQUIRED
                    ) &&
                    hasText(edt_password, REQUIRED) && hasText(edt_confirm_password, REQUIRED)
                ) {
                    if (!email.matches(emailPattern.toRegex())) {
                        printToast(this, EMAIL_ID_NOT_VALID)
                    } else if (!password.equals(confirmPassword)) {
                        printToast(this, PASSWORD_DOES_NOT_MATCH)
                    } else {
                        authenticateVM!!.register(
                            Input(
                                userName,
                                email,
                                phoneNumber,
                                password,
                                confirmPassword
                            )
                        ).observe(this,
                            Observer {
                                printToast(this, it?.message.toString())
                                if (it?.status.equals(SUCCESS)) {
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                            })
                    }
                }


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

            }

        }
    }
}
