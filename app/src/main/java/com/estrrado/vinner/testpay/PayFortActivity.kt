package com.estrrado.vinner.testpay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.CART_ID
import com.estrrado.vinner.helper.Constants.CITY
import com.estrrado.vinner.helper.Constants.COUNTRY
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.NAME
import com.estrrado.vinner.helper.Constants.OPERATOR_ID
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Constants.STATUS
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.TOTAL_PAYABLE
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.Validation.printToast
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.payfort.fort.android.sdk.base.FortSdk
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager
import com.payfort.sdk.android.dependancies.base.FortInterfaces
import com.payfort.sdk.android.dependancies.models.FortRequest
import webconnect.com.webconnect.WebConnect
import webconnect.com.webconnect.listener.OnWebCallback
import java.security.MessageDigest


/**
 * this activity is PayFort Demo
 * you need to download FORTSDK and add dependencies
 * https://docs.payfort.com/docs/mobile-sdk/build/index.html#before-starting-the-integration
 *
 * */
class PayFortActivity : AppCompatActivity(), OnWebCallback {
    var vModel: HomeVM? = null
    var operatorId: String? = null
    var address: String? = null
    var totalPayable: String? = null
    var housename: String? = null
    var Roadname: String? = null
    var landmark: String? = null
    var pincode: String? = null
    var addressType: String? = null
    var fortCallback: FortCallBackManager? = null
    val TAG = "payTag"
    lateinit var deviceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(
                        this,
                        ApiClient.apiServices!!
                    )
                )
            )
        ).get(HomeVM::class.java)
        deviceId = FortSdk.getDeviceId(this)
//        deviceId = "sdjabdgvhgchkahsbdausiydfhuscnjihoiuhodivoduih"

        initFortCallback()

        getToknSdk()
//        }
    }

    fun getHashString(t: String): String {
        val bytes = t.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashSt = digest.fold("", { str, it -> str + "%02x".format(it) })
        return hashSt
    }

    private fun getToknSdk() {

        val param = linkedMapOf<String, String>()

        param.put("access_code", "WGG6Avj6KSL4SX4zfWGQ")
        param.put("device_id", deviceId)
        param.put("language", "en")
        /**
         * you should get 'merchant_identifier' from your payfort account
         * */
        param.put("merchant_identifier", "879b45fb")
        param.put("service_command", "SDK_TOKEN")


        val sb = StringBuilder()
        /**
         * change 'Hello' by what you got from payFort.
        you will find it in your account*/
        sb.append("27aVEaXzC8qDf5aHJhze6o?}")
        param.forEach { (k, v) ->
            sb.append("$k=$v")
//            sb.append(k)
//            sb.append("=")
//            sb.append(v)
        }
        sb.append("27aVEaXzC8qDf5aHJhze6o?}")
        param.put("signature", getHashString(sb.toString()))

        /**
         * calling api https://sbpaymentservices.payfort.com/FortAPI/paymentApi
         *
         * */
        WebConnect.with(this, "paymentApi")
            .post()
            /**
             * put url depending on the Environment you targeting
             * */
            .baseUrl("https://sbpaymentservices.payfort.com/FortAPI/")
            .bodyParam(param)
            .taskId(11)
            .callback(WebCallback(this, this), ResponsePay::class.java, ErrorModel::class.java)
            .connect()

    }

    /**
     * start buy operation via SDK
     * put required keys and there values
     * */
    fun startPayFortSdk(sign: String) {
        val model = FortRequest()
        /**
         * to see response in log
         * */
        model.isShowResponsePage = true
        val hash = hashMapOf<String, String>()
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
//        getHashString.put("command", "PURCHASE")
        hash.put("command", "AUTHORIZATION")
        hash.put("customer_email", "m.salem@clickapps.co")
        hash.put("currency", intent.getStringExtra(Constants.CCURRENCY)!!)
        /**
        = 10000 => 100, should be multi by some value depending on your currency, check payfort Docs fore more detail
         */

        var amount = getIntent().getExtras()!!.getString(TOTAL_PAYABLE)!!.toDouble()
        if (intent.getStringExtra(Constants.CCURRENCY) == "BHD") {
            amount = amount * 1000
        } else {
            amount = amount * 100
        }

        hash.put("amount", amount.toInt().toString())
        hash.put("language", "en")
        /**
         * merchant_reference represented purchase id, it should be unique
         * here we let user to entered as our test requirement.
         * */
//        val x = editOne.text.toString()
        hash.put("merchant_reference", ts + getIntent().getExtras()!!.getString(CART_ID)!!)
        /**
         * you can also add any option key-value pairs
         * */
//        getHashString.put("customer_name", "Sam")
//        getHashString.put("customer_ip", "172.150.16.10")
//        getHashString.put("payment_option", "VISA")
//        getHashString.put("eci", "ECOMMERCE")
//        getHashString.put("order_description", "DESCRIPTION")
        hash.put("sdk_token", sign)
        hash.put("token_name", "8A70320AF209")
        hash.put("payment_option", "VISA")


        model.requestMap = hash.toMap()


        /**
         * start SDK
         *
         * user will input his info e.g card number...
         * then will  receive the result in callbacks bellow
         * */
        FortSdk
            .getInstance()
            .registerCallback(this, model,
                FortSdk.ENVIRONMENT.TEST, 5,
                fortCallback, true, object : FortInterfaces.OnTnxProcessed {
                    override fun onSuccess(
                        p0: MutableMap<String, Any>?,
                        p1: MutableMap<String, Any>?
                    ) {


                        vModel!!.PaymentStatus(
                            RequestModel(
                                accessToken = Preferences.get(this@PayFortActivity, ACCESS_TOKEN),
                                address_type = getIntent().getExtras()!!.getString(ADDDRESS_TYPE),
                                housename = getIntent().getExtras()!!.getString(HOUSENAME),
                                road_name = getIntent().getExtras()!!.getString(ROAD_NAME),
                                landmark = getIntent().getExtras()!!.getString(LANDMARK),
                                pincode = getIntent().getExtras()!!.getString(PINCODE),
                                payment_status = "paid",
                                payment_method = "payfort",
                                operatorId = getIntent().getExtras()!!.getString(OPERATOR_ID),
                                country = getIntent().getExtras()!!.getString(COUNTRY),
                                city = getIntent().getExtras()!!.getString(CITY),
                                name = getIntent().getExtras()!!.getString(NAME)

                            )
                        ).observe(this@PayFortActivity,
                            Observer {
                                printToast(applicationContext, it!!.message.toString())
                                if (it?.status.equals(SUCCESS)) {
                                    val intent = Intent()
                                    intent.putExtra(STATUS, SUCCESS)
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                } else {
                                    if (it?.message.equals("Invalid access token")) {
                                        startActivity(
                                            Intent(
                                                this@PayFortActivity,
                                                LoginActivity::class.java
                                            )
                                        )
                                        this@PayFortActivity.finish()
                                    } else {
                                        printToast(this@PayFortActivity, it?.message!!)
                                    }
                                    printToast(applicationContext, it?.message.toString())
                                }
                            })
                        Log.d(TAG, "onSuccess")
                        Log.d(TAG, p0.toString())
                        Log.d(TAG, p1.toString())
                    }

                    override fun onFailure(
                        p0: MutableMap<String, Any>?,
                        p1: MutableMap<String, Any>?
                    ) {
                        Log.d(TAG, "onFailure")
                        Log.d(TAG, p0.toString())
                        Log.d(TAG, p1.toString())
                        Toast.makeText(
                            this@PayFortActivity,
                            "Error: ${p1?.get("response_message")}",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()

                    }

                    override fun onCancel(
                        p0: MutableMap<String, Any>?,
                        p1: MutableMap<String, Any>?
                    ) {
                        Log.d(TAG, "onCancel")
                        Log.d(TAG, p0.toString())
                        Log.d(TAG, p1.toString())
                        finish()
                    }

                })
    }

    fun initFortCallback() {
        fortCallback = FortCallBackManager.Factory.create();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fortCallback!!.onActivityResult(requestCode, resultCode, data);
    }


    //region apis callback
    override fun <T : Any?> onSuccess(`object`: T?, taskId: Int) {
//        textOne.text = "TRY"

        if (`object` is ResponsePay) {
            if (`object`.sdk_token.isEmpty()) {
                Toast.makeText(this, "Error: ${`object`.response_message}", Toast.LENGTH_LONG)
                    .show()
            } else {
                startPayFortSdk(`object`.sdk_token)
            }
        }


    }

    override fun <T : Any?> onError(`object`: T?, error: String?, taskId: Int) {
//        textOne.text = "TRY"
        Toast.makeText(this, "onError $error", Toast.LENGTH_SHORT).show()

    }

    //endregion
}
